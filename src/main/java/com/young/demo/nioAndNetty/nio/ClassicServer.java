package com.young.demo.nioAndNetty.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClassicServer implements Runnable {

	public void run() {
		try (ServerSocket ss = new ServerSocket()){
			while (!Thread.interrupted()) {
				new Thread(new Handler(ss.accept())).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class Handler implements Runnable {

		final Socket socket;
		Handler(Socket s) {
			socket = s;
		}
		@Override
		public void run() {
			
			byte[] input = new byte[1000];
			try {
				socket.getInputStream().read(input);
				byte[] output = process(input);
				socket.getOutputStream().write(output);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private byte[] process(byte[] cmd) {
			return cmd;
		}
		
	}
	
}
