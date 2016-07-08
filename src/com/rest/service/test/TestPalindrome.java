package com.rest.service.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



/**
 * @author rohivis
 * A test program to verify palindrome counts
 */
public class TestPalindrome {
	
	private static long countPalindromes(String fullName){
		long count = 0;
		try{
			if(null!=fullName){
				float length = fullName.length();
				int halfOfLength = (int) Math.round(length/2);				
				int distinctCharCount=0;
				char[] chars = fullName.toCharArray();
				Set<Character> charSet = new HashSet<Character>();
				for (char c : chars) {
					charSet.add(c);
				}

				StringBuilder distinctStrBuilder = new StringBuilder();
				for (Character character : charSet) {
					distinctStrBuilder.append(character);
				}
				
				if(distinctStrBuilder.length()>0){
					distinctCharCount = distinctStrBuilder.length();
				}

				count = (long) Math.pow(distinctCharCount, halfOfLength);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return count;

	}
	
	public static void main (String[] args){		
		long start = new Date().getTime();
		System.out.println("Start "+new Date());
		List<String> strInput = new ArrayList<>();
		strInput.add("NicolaTesla");
		strInput.add("MaxPlanck");
		strInput.add("MarieCurie");
		strInput.add("MariaMayer");
		Iterator<String> itr = strInput.iterator();
		String inputText = null;
		long count =0;
		while(itr.hasNext()){
			inputText = itr.next();
			System.out.println("Input String "+inputText);
			count = countPalindromes(inputText);
			System.out.println("Palindromic count for string "+inputText+ " is " +count);
		}
		
		long end = new Date().getTime();
		System.out.println("End "+new Date());	
		System.out.println("Time taken in seconds " +(end-start)/1000);		

	}




}
