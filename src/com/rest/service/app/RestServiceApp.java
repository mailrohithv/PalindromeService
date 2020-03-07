package com.rest.service.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;




/**
 * @author rohivis
 * This class acts as a configuration file.
 * Refer to https://jersey.java.net/documentation/latest/deployment.html
 * Section 4.7.2.1. Descriptor-less deployment
 * Only supported on Servlet 3.0 and above. Hence I don't have an web.xml configuration for my Jersey Serlvet
 * Repo shared with megharohith on 3/7/2020
 */

@ApplicationPath("/*")
public class RestServiceApp extends Application{	

	//I don't need any custom configuration	
	
}
