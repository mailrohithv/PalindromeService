package com.rest.service.util;

/**
 * @author rohivis
 * A generic class which defines services and their required attributes
 */

public enum Services {
	
	PALINDROME_SERVICE("search");

	private String reqdField;
	
	Services(String field){
	 this.reqdField = field;	
	}

	public String getReqdField() {
		return reqdField;
	}

	public void setReqdField(String reqdField) {
		this.reqdField = reqdField;
	}
	
}
