package com.palindrome.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.JsonObject;
import com.palindrome.response.PalindromeResponse;
import com.palindrome.response.ServiceResponse;
import com.rest.service.util.ServiceUtil;
import com.rest.service.util.Services;

/**
 * Palindrome service class
 * @author rohivis
 *
 */

@Path("/palindromes")
public class PalindromeService {
	
	@Context 
	HttpHeaders httpHeaders;
	@Context
	UriInfo uriInfo;
	
	/**
	 * This method will service the GET request.
	 * Sample request - /palindromes?search=electricity&limit=3
	 * @param search(required)
	 * @param limit(default to 1)
	 * @param 
	 * @return Response	 */
		
	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPalindromes(@QueryParam("search") String search, @DefaultValue("1") @QueryParam("limit") String limit){
        long startTime = new Date().getTime();
		ServiceResponse resp = new ServiceResponse();
		resp.setServiceName(Services.PALINDROME_SERVICE.name());
		List<String> messages = new ArrayList<String>();
		resp.setResponseMessages(messages);
		String jsonResponse = null;	
     	boolean validated = true;
		boolean reqdFieldvalidated = ServiceUtil.validateRequiredFields(Services.PALINDROME_SERVICE, uriInfo,  messages);
		if(!reqdFieldvalidated){
			validated=false;
			messages.add(Services.PALINDROME_SERVICE.getReqdField()+" query is required!");
		}
		
		boolean dataValidated = ServiceUtil.validateData(limit, messages);
		if(!dataValidated){
			validated=false;
			messages.add("limit should be in a range between 1 to 5");
		}
		
		if(validated){
			try {
				PalindromeServiceBO srvcBO = new PalindromeServiceBO();
				JsonObject jsonObj = srvcBO.callDataService(search, limit);
				if(null!=jsonObj && jsonObj.get("count").getAsInt()>0){
					List<StringBuilder> nasaInnovatorsList = srvcBO.processNASAData(jsonObj);
					if(null!=nasaInnovatorsList && nasaInnovatorsList.size()>0){
						Map<String, Long> palindromeDataMap = srvcBO.generatePalindromes(nasaInnovatorsList);	
						if(null!=palindromeDataMap && palindromeDataMap.size()>0){
							List<PalindromeResponse> palindromeRespList = srvcBO.generateResponse(palindromeDataMap);
							resp.setPayload(palindromeRespList);
							resp.setResponseCode(200); //OK
						}						
					}					
				}else{
					//Search query didn't return any patents data
					messages.add("The service didn't find any results for your search query - " +search);
					resp.setResponseCode(200); //OK
				}
			} catch(Exception e) {
				messages.add("An Internal server error occured while servicing the request. Contact the administrator with the following - " + new Date().toString());
				e.printStackTrace();
				resp.setResponseCode(500); //internal server error
				resp.setPayload(null);
			}
		} else {
			resp.setPayload(null);
			resp.setResponseCode(400); //Bad client
		}  
       
        long endTime = new Date().getTime();
        float timeTakenInSec = (endTime-startTime)/1000;
        messages.add("Service response time in seconds "+timeTakenInSec);
        jsonResponse = ServiceUtil.serializeAsJson(resp);
        
		return Response.ok(jsonResponse,MediaType.APPLICATION_JSON_TYPE).build();
	}
	
}
