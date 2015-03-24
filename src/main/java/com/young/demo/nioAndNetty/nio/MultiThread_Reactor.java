package com.young.demo.nioAndNetty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class MultiThread_Reactor implements Runnable {
	
	final Selector selector;
	final ServerSocketChannel serverSocket;

	MultiThread_Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {

				selector.select();
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while(it.hasNext()) {
					dispatch(it.next());
				selected.clear();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable)(k.attachment());
		if(r != null)
			r.run();
	}

	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if (c != null)
					new Handler(selector, c);
			} catch (IOException e) {

			}
		}
	}

	final class Handler implements Runnable {

		final ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 3, 3, null, null);
		static final int PROCESSING = 3;
		final SocketChannel socket;
		final SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(100);
		ByteBuffer output = ByteBuffer.allocate(100);
		static final int READING = 0, SENDING = 1;
		int state = READING;

		Handler(Selector sel, SocketChannel c) throws IOException {
			socket = c;
			c.configureBlocking(false);
			sk = socket.register(sel, 0);
			sk.attach(this);
			sk.interestOps(SelectionKey.OP_READ);
			sel.wakeup();
		}

		boolean inputIsComplete() {
			return true;
		}

		boolean outputIsComplete() {
			return true;
		}

		void process() {
		}

		@Override
		public void run() {

			try {
				socket.read(input);
				if (inputIsComplete()) {
					
			}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		synchronized void read() {
			try {
				socket.read(input);
				if(inputIsComplete()) {
					state = PROCESSING;
					pool.execute(new Processer());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		synchronized void processAndHandOff() {
			process();
			state = SENDING;
			sk.attach(new Sender());
			sk.interestOps(SelectionKey.OP_WRITE);
			sk.selector().wakeup();
			
		}
		
		class Processer implements Runnable {
			public void run() {
				try {
					socket.write(output);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(outputIsComplete())
					sk.cancel();
			}
		}
		
		
		class Sender implements Runnable {
			public void run() {
				try {
					socket.write(output);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(outputIsComplete())
					sk.cancel();
			}
		}

	}

}
