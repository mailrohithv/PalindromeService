package com.palindrome.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.palindrome.response.PalindromeResponse;
import com.rest.service.util.ServiceUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;





/**
 * Palindrome service BO class with methods to support the business case
 * @author rohivis
 *
 */


public class PalindromeServiceBO{
	
	private static String NASA_BASE_URL=null;
	private static String RESOURCE=null;
	private static String DEMO_KEY=null;
	
	static{
		Properties prop = ServiceUtil.loadConfig();	
		if(null!=prop){
			NASA_BASE_URL=prop.getProperty("NASA_BASE_URL");
			RESOURCE=prop.getProperty("RESOURCE");
			DEMO_KEY=prop.getProperty("DEMO_KEY");
		}
	}
	
	private static final String RESULTS = "results";
	private static final String INNOVATOR = "innovator";
	private static final String LNAME = "lname";
	private static final String FNAME ="fname";
	private static final String NAME_SEPARATOR_COLON=":";
	private static final String NAME_SEPARATOR_SPACE=" ";
	
	
	/**
	 * A method which calls the NASA service to obtain the patents info based on the
	 * search query
	 * @param searchQuery
	 * @param queryLimit
	 * @return JsonObject containing the NASA service response
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JsonObject callDataService(String searchQuery, String queryLimit) {
		// call NASA service
		JsonObject jsonObj = null;
		try{
			Client client = Client.create();
			WebResource webResource = client.resource(NASA_BASE_URL+RESOURCE);		
			MultivaluedMap queryParams = new MultivaluedMapImpl();
			queryParams.add("api_key", DEMO_KEY);
			queryParams.add("query", searchQuery);
			queryParams.add("limit", String.valueOf(queryLimit));	
			webResource = webResource.queryParams(queryParams);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
			int status = response.getStatus();
			if(200 == status){
				String jsonStr = response.getEntity(String.class);
				jsonObj = ServiceUtil.convertStringToJsonObj(jsonStr);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonObj;
	}
	

	/**
	 * A method which parses the NASA json response object to obtain the
	 * innovator's first name and last name
	 * @param jsonObj
	 * @return A list of Strings containing the first name and last name of the innovator separated by :
	 */
	@SuppressWarnings("rawtypes")
	public List<StringBuilder> processNASAData(JsonObject jsonObj) {
		List<StringBuilder> fullNameList = new ArrayList<>();
		try{			
			JsonArray jsonArrayResults = jsonObj.getAsJsonArray(RESULTS);	
			if(null!=jsonArrayResults && jsonArrayResults.size()>0){
				Iterator resultsItr = jsonArrayResults.iterator();
				while(resultsItr.hasNext()){
					JsonObject resultsObj = (JsonObject)resultsItr.next();
					JsonArray jsonArrayInnovator = resultsObj.getAsJsonArray(INNOVATOR);
					if(null!=jsonArrayInnovator && jsonArrayInnovator.size()>0){
						Iterator innovatorItr = jsonArrayInnovator.iterator();
						while(innovatorItr.hasNext()){
							JsonObject innovatorObj = (JsonObject)innovatorItr.next();
							JsonPrimitive lname = innovatorObj.getAsJsonPrimitive(LNAME);
							JsonPrimitive fname = innovatorObj.getAsJsonPrimitive(FNAME);
							StringBuilder fullName = new StringBuilder();							
							if(fname!=null && fname.toString().length()>0){
								String fnameStr = fname.getAsString();	
								fullName = fullName.append(fnameStr);
							}
							fullName = fullName.append(NAME_SEPARATOR_COLON);
							if(lname!=null && lname.toString().length()>0){
								String lnameStr = lname.getAsString();
								fullName = fullName.append(lnameStr);
							}
							fullNameList.add(fullName);							
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
    	return fullNameList;
	}

	/**
	 * A method which generates the payload required for the Palindrome request.
	 * @param palindromeDataMap
	 * @return A list of PalindromeResponse object containing the innovator first name and last name and their palindromic count
	 * in sorted order from highest to smallest
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PalindromeResponse> generateResponse(Map<String, Long> palindromeDataMap) {
		List<PalindromeResponse> palindromeRespList = new ArrayList<>();
		try{
			if(null!=palindromeDataMap && palindromeDataMap.size()>0){
				Iterator<String> mapItr = palindromeDataMap.keySet().iterator();
				while(mapItr.hasNext()){
					PalindromeResponse palRespObj = new PalindromeResponse();
					String fullName = (String)mapItr.next();
					palRespObj.setName(fullName);
					palRespObj.setCount(palindromeDataMap.get(fullName));
					palindromeRespList.add(palRespObj);
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		Collections.sort(palindromeRespList, new Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				PalindromeResponse pObj0 = (PalindromeResponse)arg0;
				PalindromeResponse pObj1 = (PalindromeResponse)arg1;
		        if(pObj0.getCount() > pObj1.getCount()){
					return +1;
				}else if(pObj0.getCount() < pObj1.getCount()){
					return -1;
				}else{				
					return 0;
				}
			}
			
		});
		Collections.reverse(palindromeRespList);
		return palindromeRespList;
	}


	/**
	 * Determines the number of palindromic strings that can be created from inventor's name
	 * @param nasaInnovatorsList
	 * @return A Map containing "first name<space>last name" as key and palindromic count as the value 
	 */
	public Map<String, Long> generatePalindromes(List<StringBuilder> nasaInnovatorsList) {
		Map<String, Long> palindromeDataMap = new HashMap<>();
		try{
			if(null!=nasaInnovatorsList && nasaInnovatorsList.size()>0){
				Iterator<StringBuilder> itr = nasaInnovatorsList.iterator();
				while(itr.hasNext()){
					StringBuilder fullName = itr.next();
					String fullNameText = fullName.toString();
					fullNameText = fullNameText.replace(NAME_SEPARATOR_COLON, NAME_SEPARATOR_SPACE);
					fullName = fullName.deleteCharAt(fullName.indexOf(NAME_SEPARATOR_COLON));					
					long palindromeCount = countPalindromes(fullName.toString());
					palindromeDataMap.put(fullNameText, palindromeCount);					
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return palindromeDataMap;
	}
	
	
	/**
	 * An algorithm to identify the palindromic count for a given input string.
	 * @param fullName
	 * @return
	 */
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
	
}
