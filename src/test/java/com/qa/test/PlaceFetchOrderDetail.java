package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.data.Placeorder;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;

public class PlaceFetchOrderDetail extends TestBase{

	
	 public String Newurl;
	 public String apurl;
	 public String serviceurl;
	 public RestClient client;
	 public String url;
	 public CloseableHttpResponse closableResponse;
	 public FetchOrderTest po1;
	
	
	  public PlaceFetchOrderDetail() {
	  super();
      }
 
	 

	  @BeforeTest
	  public void Setup() throws ClientProtocolException, IOException {
		   LoggerSetUp();
		   apurl= prop.getProperty("url");
		   serviceurl= prop.getProperty("placeorderserviceurl");
		   url= apurl + serviceurl;
		   logger.debug(("Url for PostAPI ") +url);
	       }

	 
		
	  @Test(priority=1, expectedExceptions={com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
			public void POSTAPIWithHeaders() throws Exception {
			client= new RestClient();
			logger.debug("*************Placing Order *********************");
			HashMap<String, String> Headers= new HashMap<String, String>();
			Headers.put("Content-Type", "application/json"); 
		
			
			ObjectMapper map= new ObjectMapper(); 
		
			Placeorder jsonObject;
			try {
				jsonObject = map.readValue(
			new File((System.getProperty("user.dir") + "\\src\\main\\java\\com\\qa\\data\\PostInput.json")),Placeorder.class);
			}   catch (Exception e) {
			logger.debug("Json file data not found Exception Error-> com.fasterxml.jackson.databind.JsonMappingException" +e);
			
			}
			
			jsonObject = map.readValue(new File((System.getProperty("user.dir") + "\\src\\main\\java\\com\\qa\\data\\PostInput.json")),Placeorder.class);
			logger.debug("Converting Json Object to JSON in String");
			String UsersJsonstring= map.writeValueAsString(jsonObject);
			
			
			 
			logger.debug("Calling Post method and Pass JSON payload as String with it");
			closableResponse = client.post(url, UsersJsonstring, Headers);
			
			
			 
			logger.debug("Validating Response from API");
			int Statuscode= closableResponse.getStatusLine().getStatusCode();
			logger.debug("Status is " +Statuscode);
	        Assert.assertEquals(Statuscode, RESPONSE_STATUS_CODE_201);
			
	 	
		      
		  	String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
		    JSONObject responseJson= new JSONObject(ResponseString);   
		    logger.debug("Response JSON from API->> " +responseJson); 
		  		
		    logger.debug("Extracting ID from JSON");
		  	String id = TestUtil.getValueByJPath(responseJson, "/id");
		  	logger.debug("Response id is: " +id);
		 

		  	Newurl= url+"/"+id;
		  	
		  	logger.debug("Json to JavaObject");
		  	Placeorder usersrespobj= map.readValue(ResponseString,Placeorder.class);  
		  	
			
		  	}

	  
	
	   @Test(priority=2)
	   public void GetAPIWithHeaders() throws Exception {
	       logger.debug("Calling GetAPI against POSTAPI ID" +Newurl);
	       client= new RestClient();
	       HashMap<String, String> Headers= new HashMap<String, String>();
	       Headers.put("Content-Type", "application/json");
	       logger.debug("Initializing RestClient and Adding Headers");
		
	       closableResponse = client.geturl(Newurl , Headers);
		
	       logger.debug("*************Fetch Order Details *********************");
	      
	       int status= closableResponse.getStatusLine().getStatusCode();
	       logger.debug("Status Code->> " +status);
	       Assert.assertEquals(status , RESPONSE_STATUS_CODE_200 , "Status code is not 200"); 
	  		
	  		
	       String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
	       logger.debug("Response is" +ResponseString);
	       JSONObject responseJson= new JSONObject(ResponseString);   
	       logger.debug("Response JSON from API->> " +responseJson);
	  	   if(status==200) {
	  	   logger.debug("Response is OK- 200");
	  		}else
	  	   logger.debug("The order doesn’t exist. Response code " +status);
	  		
	  		
	  		
	  	   logger.debug("Extracting currency from Response JSON");
	  	   String currency = TestUtil.getValueByJPath(responseJson, "/fare/currency");
	  	   logger.debug("The Currency is-> " +currency);
	  	   Assert.assertEquals("HKD", currency); 
	  		
	  		
	  	
	  	   Header[] headersArray= closableResponse.getAllHeaders();  
	  	   logger.debug("Getting all Header from response and stored it into hashmap");
	  	   HashMap<String,String> allheaders= new HashMap<String,String>();
	  	   for(Header header : headersArray ) {
	  	   allheaders.put(header.getName(),header.getValue());
	  		
	  	   }
	  		
	  	   logger.debug("Headers Array->> " +allheaders);
	  
	       }
          	  
           }

	

