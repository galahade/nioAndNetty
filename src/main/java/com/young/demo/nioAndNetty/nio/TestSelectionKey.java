package com.young.demo.nioAndNetty.nio;

import java.nio.channels.SelectionKey;

public class TestSelectionKey {

	public static void main(String[] args) {
		System.out.println("OP_Read:"+SelectionKey.OP_READ);
		System.out.println("OP_ACCEPT:"+SelectionKey.OP_ACCEPT);
		System.out.println("OP_CONNECT:"+SelectionKey.OP_CONNECT);
		System.out.println("OP_WRITE:"+SelectionKey.OP_WRITE);

	}
}
