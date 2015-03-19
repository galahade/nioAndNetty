package com.young.demo.nioAndNetty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Reactor implements Runnable {
	
	final Selector selector;
	final ServerSocketChannel serverSocket;
	
	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if(c != null)
					new Handler(selector,c);
			} catch(IOException e) {
				
			}
		}
	}
	
	final class Handler implements Runnable {
		
		final SocketChannel socket;
		final SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(100);
		ByteBuffer output = ByteBuffer.allocate(100);
		static final int READING = 0,
				SENDING = 1;
		int state = READING;
		
		Handler(Selector sel, SocketChannel c) throws IOException {
			socket = c;
			c.configureBlocking(false);
			sk = socket.register(sel, 0);
			sk.attach(this);
			sk.interestOps(SelectionKey.OP_READ);
			sel.wakeup();
		}
		boolean inputIsComplete(){
			return true;
		}
		boolean outputIsComplete(){
			return true;
		}
		void process() {}
		
		
		@Override
		public void run() {

			if(state == READING)
		}
		
		void read() throws IOException {
			socket.read(input);
			if(inputIsComplete()) {
				process();
				state = SENDING;
				sk.interestOps(SelectionKey.OP_WRITE);
			}
		}
		
		void send() throws IOException {
			socket.write(output);
			if(outputIsComplete())
				sk.cancel();;
		}
		
	}

}
