package com.young.demo.nioAndNetty.test;

import java.util.Arrays;

public class TestHuiWen {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Input args length need more than 1");
			return ;
		}
		
		for(String str : args) {
			
			if(canBePalindrome(str)) {
				System.out.println("This input "+str+" can be converted to a palindrome.");
			} else {
				System.out.println("This input "+str+" can't be converted to a palindrome.");
			}
		}
		
	}
	
	private static boolean canBePalindrome(String str) {
		int strLength = str.length();
		char[] chars = str.toCharArray();
		Arrays.sort(chars);
		
		if(strLength % 2 == 0) {
			for(int i = 0; i < chars.length; i=i+2) {
				if(chars[i] == chars[i+1])
					continue;
				return false;
			}
		}else if(strLength % 2 == 1) {
			int diff = 0;
			for(int i = 0; i < chars.length-1; i = i+2) {
				if(chars[i] == chars[i+1]) {
					continue;
				} else {
					i = i-1;
					diff++;
				}
				return false;
			}
			if(diff > 1)
				return false;
			return true;
		}
		
		return true;
	}

}
