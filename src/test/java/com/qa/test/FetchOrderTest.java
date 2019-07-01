package com.qa.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.qa.base.TestBase;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;



    public class FetchOrderTest extends TestBase{
	
	public String id="1";
	public String apurl;
	public String serviceurl;
	public RestClient client;
	public String url;
	public CloseableHttpResponse closableResponse;
	

	
	public FetchOrderTest() {  
	super();
    }
	

	@BeforeTest
	public void Setup() throws ClientProtocolException, IOException {
		 LoggerSetUp();
         apurl= prop.getProperty("url");
		 serviceurl= prop.getProperty("placeorderserviceurl")+""+"/"+id;
		 url= apurl + serviceurl;
		 logger.debug(("Setting up URL for Fetching Orders") +url);
         }

	  
	@Test(priority=1)
	public void GetAPIWithHeaders() throws Exception {
		 logger.debug("Initializing RestClient");
		 client= new RestClient();
		 HashMap<String, String> Headers= new HashMap<String, String>();
		 logger.debug("Adding Headers");
		 Headers.put("Content-Type", "application/json");
		 closableResponse = client.geturl(url , Headers);
		 logger.debug("Calling get method");
		      
		 int status= closableResponse.getStatusLine().getStatusCode(); 
		 System.out.println("Status Code->> " +status);
		 if(status==200){
		 logger.debug("Response code " +status);
	  			
		 String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
		 System.out.println("Response is" +ResponseString);
		 JSONObject responseJson= new JSONObject(ResponseString);   
		 logger.debug("Response JSON from API->> " +responseJson);
		
		  				
		 String currency = TestUtil.getValueByJPath(responseJson, "/fare/currency");
		 System.out.println("The Currency is-> " +currency);
		 Assert.assertEquals("HKD", currency); 
		 logger.debug("Verifying Response");		 		
		  	
		 
		 Header[] headersArray= closableResponse.getAllHeaders(); 
		 HashMap<String,String> allheaders= new HashMap<String,String>();
		 for(Header header : headersArray){
		 allheaders.put(header.getName(),header.getValue());
		  		
		 }
		  		
		 logger.debug("Headers Array->> " +allheaders);
		 }else if(status==404){
			 logger.debug("The order doesn’t exist- ORDER NOT FOUND");
			 } 
			 else {
		     logger.debug("Error in API");
			 }
			
		 }
	     }
		
	  
	  
	
		
		
		
		
