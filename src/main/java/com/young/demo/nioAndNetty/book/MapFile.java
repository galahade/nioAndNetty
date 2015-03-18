package com.young.demo.nioAndNetty.book;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;

public class MapFile {
	
	public static void main(String[] args) throws IOException {
		File tempFile = File.createTempFile("mmaptest", "txt");
		
		RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
		
		FileChannel channel = file.getChannel();
		
		ByteBuffer temp = ByteBuffer.allocate(128);
		
		temp.put("This is the file content".getBytes());
		
		temp.flip();
		
		channel.write(temp,0);
		
		temp.clear();
		
		temp.put("This is more file content".getBytes( ));
		
		temp.flip();
		
		channel.write(temp, 8192);
		
		MappedByteBuffer ro = channel.map(MapMode.READ_ONLY, 0, channel.size());
		
		MappedByteBuffer rw = channel.map(MapMode.READ_WRITE, 0, channel.size());
		
		MappedByteBuffer cow = channel.map(MapMode.PRIVATE, 0, channel.size());
		
		System.out.println("Begin");
		
		System.out.println(Charset.defaultCharset().toString());
		
		/*
		
		showBuffers(ro, rw, cow);
		
		cow.position(8);
		cow.put("COW".getBytes());
		*/
		
	}
	
	public static void showBuffers(ByteBuffer ro, ByteBuffer rw, ByteBuffer cow) {
		dumpBuffer("R/O", ro);
		dumpBuffer ("R/W", rw);  
		dumpBuffer ("COW", cow); 
		System.out.println("");
	}
	
	public static void dumpBuffer(String prefix, ByteBuffer buffer) {
		System.out.print(prefix + ": '");
		int nulls = 0;
		int limit = buffer.limit();
		
		for(int i = 0; i < limit; i++) {
			char c = (char) buffer.get(i);
			if(c == '\u0000') {
				nulls++;
				continue;
			}
			if (nulls != 0) {
				System.out.println("|[" + nulls + " nulls]|");
			}
			System.out.print(c);
		}
		System.out.println("'");
	}

}
