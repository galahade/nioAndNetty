package com.young.demo.nioAndNetty;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FilePrintPosition {
	
	public static void main(String[] args) throws IOException {
		File file = File.createTempFile("temp", null);
		RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
		
		randomAccessFile.seek(1000);
		
		FileChannel fileChannel = randomAccessFile.getChannel();
		
		System.out.println("file pos:" + fileChannel.position());
		
		randomAccessFile.seek(500);
		
		System.out.println("file pos:" + fileChannel.position());
		
		fileChannel.position (200); 
		
		System.out.println ("file pos: " + randomAccessFile.getFilePointer( )); 
		fileChannel.close();
		randomAccessFile.close();
		
		
	}

}
