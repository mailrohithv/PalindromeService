package com.palindrome.response;

import java.util.List;

/**
 * This class holds the response to the API call. <br/>
 * This class will be used irrespective of whether there was a success or a failure. <br/> 
 * @author rohivis
 *
 */
public class ServiceResponse {

	String serviceName;
	int responseCode; //the HTTP response code
	List<String> responseMessages;
	Object payload;

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public List<String> getResponseMessages() {
		return responseMessages;
	}
	public void setResponseMessages(List<String> responseMessages) {
		this.responseMessages = responseMessages;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
}
