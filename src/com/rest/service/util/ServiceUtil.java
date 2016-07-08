package com.rest.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




/**
 * @author rohivis
 * Service utility class
 */


public class ServiceUtil {


	/**
	 * A method to validate required fields
	 * @param service
	 * @param uriInfo
	 * @param messages
	 * @return true if validated else false
	 */
	public static boolean validateRequiredFields(Services service, UriInfo uriInfo, List<String> messages){
		boolean validated = false;
		if(null != service &&  null != uriInfo){			
			MultivaluedMap<String, String> reqdFieldsMap = uriInfo.getQueryParameters();
			if(null!=reqdFieldsMap){
				String reqdFieldVal = reqdFieldsMap.getFirst(service.getReqdField());
				if(null!=reqdFieldVal && reqdFieldVal.length()>0){
					validated = true;
				}			
			}		
		}	
		return validated;
	}
	
	public static boolean validateData(String limitStr, List<String> messages){
		boolean validated = false;
		if(null!=limitStr && limitStr.length()>0){
			int limitInt = Integer.parseInt(limitStr);
			if(limitInt>=1 && limitInt <=5){
				validated = true;
			}
		}	
		return validated;		
	}



	/**
	 * A method to serialize a Java object to JSON using Google Gson
	 * @param obj
	 * @return json  representation of obj
	 */
	public static String serializeAsJson(Object obj){		
		String jsonData = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		jsonData = gson.toJson(obj);		
		System.out.println("jsonData  "+jsonData);
		return jsonData;		
	}
	
	
	/**
	 * A method to convert json string to JsonObject	 
	 * @param jsonStr
	 * @return JsonObject
	 */
	public static JsonObject convertStringToJsonObj(String jsonStr){
		JsonObject jsonObj = new JsonObject();
		if(null == jsonStr){
			return jsonObj;
		}
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(jsonStr);
		jsonObj = element.getAsJsonObject();
		return jsonObj;
	}
	
	
	public static Properties loadConfig(){
		Properties prop = new Properties();
		InputStream propInputStream = null;
		try {
				String propFile = "config.properties";
				propInputStream = ServiceUtil.class.getClassLoader().getResourceAsStream(propFile);
				if(propInputStream==null){
					System.out.println("missing config file " + propFile);
					return null;
				}
				prop.load(propInputStream);
			System.out.println(prop.getProperty("NASA_BASE_URL"));
			System.out.println(prop.getProperty("RESOURCE"));
			System.out.println(prop.getProperty("DEMO_KEY"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(propInputStream!=null){
				try {
					propInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prop;
	}


	

}
