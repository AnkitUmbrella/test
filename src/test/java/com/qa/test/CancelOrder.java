
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
	import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.data.Placeorder;
import com.qa.restclient.RestClient;
	import com.qa.util.*;

	public class CancelOrder extends TestBase{
		
		
		public String CompleteURL;
		public String Newurl;
		public String UpdatedURL;
		public String CancelUrl;
		public String apurl;
		public String serviceurl;
		public RestClient client;
		public String url;
		
		public CloseableHttpResponse closableResponse;
		public Xls_Reader xls;
		public SoftAssert softAssert = new SoftAssert();

		public CancelOrder() {  
		super();
	    }
		
			
		@BeforeTest
		public void Setup() throws ClientProtocolException, IOException {
		LoggerSetUp(); 
		apurl= prop.getProperty("url");
	    serviceurl= prop.getProperty("placeorderserviceurl");
	    url= apurl + serviceurl;
		 	
		 }
			
	   @Test(priority=1, expectedExceptions= {com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
	   public void POSTAPIWithHeaders() throws Exception {
	   client= new RestClient();
	   logger.debug("*************Placing Order *********************");
	   logger.debug("url");
	   HashMap<String, String> Headers= new HashMap<String, String>();
       Headers.put("Content-Type", "application/json"); 
				
				
	   ObjectMapper map= new ObjectMapper(); 
	   Placeorder jsonObject= map.readValue(new File((System.getProperty("user.dir") + "\\src\\main\\java\\com\\qa\\data\\PostInput.json")),Placeorder.class);
	   String UsersJsonstring= map.writeValueAsString(jsonObject);
	   closableResponse = client.post(url, UsersJsonstring, Headers);
				
	   int Statuscode= closableResponse.getStatusLine().getStatusCode();
	   logger.debug(Statuscode);
	   String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
	   JSONObject responseJson= new JSONObject(ResponseString);   
	   logger.debug("Response JSON from API->> " +responseJson); 
			  	
	   String id = TestUtil.getValueByJPath(responseJson, "/id");
	   logger.debug("Response id is: " +id);
	   Newurl= url+"/"+id;
	   logger.debug(Newurl);
	   Placeorder usersrespobj= map.readValue(ResponseString,Placeorder.class);  
	   }
		
	 
	@Test(priority=2)
	  public void GetAPIWithHeaders() throws Exception {
	  logger.debug("GetAPI URl " +Newurl);
	  client= new RestClient();
	  HashMap<String, String> Headers= new HashMap<String, String>();
	  Headers.put("Content-Type", "application/json");
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
			  		
			  		
	  String currency = TestUtil.getValueByJPath(responseJson, "/fare/currency");
	  logger.debug("The Currency is-> " +currency);
	  Assert.assertEquals("HKD", currency); // Comparing values.
	  UpdatedURL=Newurl+""+"/take";
	 
	  Header[] headersArray= closableResponse.getAllHeaders();  // Getting all Header from response and stored it into hashmap
      HashMap<String,String> allheaders= new HashMap<String,String>();
	  for(Header header : headersArray ) {
	  allheaders.put(header.getName(),header.getValue());
	  }
			  		
	  logger.debug("Headers Array->> " +allheaders);
			  
	  }
			
	
	
			
	  @Test(priority=3)
      public void TAKEORDER() throws ClientProtocolException, IOException,org.testng.TestException{
		  client= new RestClient();
		  logger.debug("*************Take Order *********************");
		  HashMap<String, String> Headers= new HashMap<String, String>();
		  Headers.put("Content-Type", "application/json");
		  logger.debug(UpdatedURL);
		  closableResponse = client.Put(UpdatedURL, Headers);
			      
	      int status= closableResponse.getStatusLine().getStatusCode();   
		  logger.debug("Status Code->> " +status);
		  if(status==422){
		  logger.debug("Logic flow is violated");
			  			
		  }else if(status==404){
		  logger.debug("The order doesn’t exist");
		  } 
		  else {
		  logger.debug(" Response code " +status);
			  		
		  }
			  			
			 
		  String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
		  JSONObject responseJson= new JSONObject(ResponseString);  
		  logger.debug("Response JSON from API->> " +responseJson);
		  CompleteURL=Newurl+""+"/complete";
		  String responsejsonstatus = TestUtil.getValueByJPath(responseJson, "/status");
	      logger.debug("The status is-> " +status);
		  Assert.assertEquals(responsejsonstatus, "ONGOING"); // Comparing
			  	
	    	}
				  	  

			
		@Test(priority=4)
		public void Complete_Order() throws ClientProtocolException, IOException{
				client= new RestClient();
				logger.debug("*************Complete Order *********************");
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json");
				closableResponse = client.Put(CompleteURL , Headers);
			     int status= closableResponse.getStatusLine().getStatusCode();  
			  		logger.debug("Status Code->> " +status);
			  		softAssert.assertEquals(status , RESPONSE_STATUS_CODE_200 , "Status code is not 200");
			  		if(status==422){
			  		logger.debug("Logic flow is violated");
			  			
			  		}else if(status==404){
			  		logger.debug("The order doesn’t exist");
			  		} 
			  		else {
			  		logger.debug("Response code " +status);
			  		
			  		}
			  			
			  			
			  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			        JSONObject responseJson= new JSONObject(ResponseString);  
			  		logger.debug("Response JSON from API->> " +responseJson);
			  		
			  		String responsejsonstatus = TestUtil.getValueByJPath(responseJson, "/status");
			  		logger.debug("The status is-> " +status);
			  		
			 
			  		Header[] headersArray= closableResponse.getAllHeaders();  // Getting all Header from response and stored it into hashmap
			  		HashMap<String,String> allheaders= new HashMap<String,String>();
			  		for(Header header : headersArray ) {
			  		allheaders.put(header.getName(),header.getValue());
			  		
			  		}
			  		
			  		logger.debug("Headers Array->> " +allheaders);
			  		
			     	}
				  	  
	 
			@Test(priority=5, expectedExceptions= {org.json.JSONException.class})
			public void Cancel_ORDER() throws ClientProtocolException, IOException {
				CancelUrl=Newurl+""+"/cancel";
				client= new RestClient();
				logger.debug("*************cancelorder Order *********************");
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json");
				closableResponse = client.Put(CancelUrl , Headers);
			      
			      
			    int status= closableResponse.getStatusLine().getStatusCode();   
			  	logger.debug("Status Code->> " +status);
			  	if(status==422){
			    logger.debug("Logic flow is violated");
			    }else if(status==404){
			    logger.debug("The order doesn’t exist");
			    } 
			    else {
			    logger.debug("Response code " +status);
			  		
			    }
			  			
			  			
			     String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			     JSONObject responseJson= new JSONObject(ResponseString);  
			  	 logger.debug("Response JSON from API->> " +responseJson);
			  	 String responsejsonstatus = TestUtil.getValueByJPath(responseJson, "/status");
			  	 logger.debug("The status is-> " +status);

			  	 Header[] headersArray= closableResponse.getAllHeaders();  
			  	 HashMap<String,String> allheaders= new HashMap<String,String>();
			  	 for(Header header : headersArray ) {
			  	 allheaders.put(header.getName(),header.getValue());
			  		
			  	}
			  		
			  	 logger.debug("Headers Array->> " +allheaders);
			  		
			  	}
				}

			


