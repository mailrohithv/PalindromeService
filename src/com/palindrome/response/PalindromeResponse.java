package com.palindrome.response;

/**
 * The response pay load class to hold the Palindrome response
 * @author rohivis
 *
 */
public class PalindromeResponse {

	private String name;
	
	private long count;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
}
