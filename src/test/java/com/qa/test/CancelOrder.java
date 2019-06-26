
	package com.qa.test;

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

import com.qa.base.TestBase;
	import com.qa.restclient.RestClient;
	import com.qa.util.*;

	public class CancelOrder extends TestBase{
		
		
		public String apurl;
		public String serviceurl;
		public RestClient client;
		public String url;
		//public TestBase testbase;
		public CloseableHttpResponse closableResponse;
		public Xls_Reader xls;
		public SoftAssert softAssert = new SoftAssert();

		 public CancelOrder() {  // To call the testbase class methods
		super();
	 }
		


		  @BeforeTest
		  public void Setup() throws ClientProtocolException, IOException {
			 // testbase= new TestBase();
			  apurl= prop.getProperty("url");
			  serviceurl= prop.getProperty("cancelorder");
			   //https://reqres.in/api/users?page=2
			   url= apurl + serviceurl;
		 	
		  }
			
			
			@Test(priority=1, expectedExceptions= {org.json.JSONException.class})
			public void PUTAPITAKEORDER() throws ClientProtocolException, IOException {
				client= new RestClient();
				 System.out.println("*************cancelorder Order *********************");
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json");
				
				
				closableResponse = client.Put(url , Headers);
			      
			      
			        //a. Status Code and (Assertion of Status Code).
			  		int status= closableResponse.getStatusLine().getStatusCode();   // Get status code with above object
			  		System.out.println("Status Code->> " +status);
			  		
			  		if(status==422){
			  			System.out.println("Logic flow is violated");
			  			
			  		}else if(status==404){
			  			System.out.println("The order doesn’t exist");
			  		} 
			  			else {
			  			System.out.println("Response code " +status);
			  		
			  		}
			  			
			  			
			       // Getting Response String
			  		//b. JSON String
			  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			        JSONObject responseJson= new JSONObject(ResponseString);   // Response string converted into JSON Object
			  		System.out.println("Response JSON from API->> " +responseJson);
			  		
			  		
			  		//To find perpage value in JSON (Assertion in JSON data)
			  		String responsejsonstatus = TestUtil.getValueByJPath(responseJson, "/status");
			  		System.out.println("The status is-> " +status);
			  		//Assert.assertEquals(responsejsonstatus, "ONGOING"); // Comparing
			  		
			  		
			  		
			  		//c. All Headers
			  		Header[] headersArray= closableResponse.getAllHeaders();  // Getting all Header from response and stored it into hashmap
			  		HashMap<String,String> allheaders= new HashMap<String,String>();
			  		for(Header header : headersArray ) {
			  		allheaders.put(header.getName(),header.getValue());
			  		
			  		}
			  		
			  		System.out.println("Headers Array->> " +allheaders);
			  		softAssert.assertAll();
			  	}
				  	  
			
			
			}

			


