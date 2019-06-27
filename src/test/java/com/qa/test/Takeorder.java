
	package com.qa.test;

	import java.io.File;
import java.io.IOException;
	import java.util.HashMap;
	import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
	import org.apache.http.client.methods.CloseableHttpResponse;
	import org.apache.http.util.EntityUtils;
import org.json.JSONException;
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

	public class Takeorder extends TestBase{
		
		public String Newurl;
		public String UpdatedURL;
		public String apurl;
		public String serviceurl;
		public RestClient client;
		public String url;
		//public TestBase testbase;
		public CloseableHttpResponse closableResponse;
		public Xls_Reader xls;
		public SoftAssert softAssert = new SoftAssert();

		 public Takeorder() {  // To call the testbase class methods
		super();
	 }
		

		 

		  @BeforeTest
		  public void Setup() throws ClientProtocolException, IOException {
			  apurl= prop.getProperty("url");
			  serviceurl= prop.getProperty("placeorderserviceurl");
			   url= apurl + serviceurl;
			   System.out.println(url);
		 
		  }

		 
			
		  @Test(priority=1, expectedExceptions={com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
				public void POSTAPIWithHeaders() throws Exception {
				client= new RestClient();
				 System.out.println("*************Placing Order *********************");
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json"); // Adding headers in Post call
				// Now add Payload Json data in com.qa.data
				
				
				//Jackson API to convert Java object(users.java) to JSON.
				
				ObjectMapper map= new ObjectMapper(); // Available in Jackson Imported from Maven repo
	     
				//Object to JSON Object convertor
				Placeorder jsonObject= map.readValue(new File("D:\\Projects\\workspace2019\\RestApi_Automation\\src\\main\\java\\com\\qa\\data\\Placeorderdata.json"), Placeorder.class);
				
				
				
				// Json Object to JSON in String
				 String UsersJsonstring= map.writeValueAsString(jsonObject);
				
				
				 
				//Call Post method and Pass JSON payload as String with it
				 closableResponse = client.post(url, UsersJsonstring, Headers);
				
				 
				 
				 // Validating Response from API
				  //a. Check for validation of Status Code
				  int Statuscode= closableResponse.getStatusLine().getStatusCode();
		        //  Assert.assertEquals(Statuscode, RESPONSE_STATUS_CODE_201);
		          System.out.println(Statuscode);
				
		 	
			       // Getting Response String
			  	   //b. JSON String
			  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			        JSONObject responseJson= new JSONObject(ResponseString);   //Response string converted into JSON Object
			  		System.out.println("Response JSON from API->> " +responseJson); // Output of response json
			  		
			  	 //To find perpage value in JSON (Assertion in JSON data)
			  		String id = TestUtil.getValueByJPath(responseJson, "/id");
			  		System.out.println("Response id is: " +id);
			 
			  		Newurl= url+"/"+id;
			  		
			  		System.out.println(Newurl);
			  		
			  		//To validate the response and compare.
			  		//Json to JavaObject
			  		Placeorder usersrespobj= map.readValue(ResponseString,Placeorder.class);  //Actual User Object
			  	
			  		
			  	}
		
	
		  
		  @Test(priority=2)
			public void GetAPIWithHeaders() throws Exception {
				
		 
				 
				   
				   System.out.println("GetAPI URl " +Newurl);
				 
				client= new RestClient();
				
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json");
				
				
				closableResponse = client.geturl(Newurl , Headers);
				
			      System.out.println("*************Fetch Order Details *********************");
			      
			      
			        //a. Status Code and (Assertion of Status Code).
			  		int status= closableResponse.getStatusLine().getStatusCode();   // Get status code with above object
			  		System.out.println("Status Code->> " +status);
			  		Assert.assertEquals(status , RESPONSE_STATUS_CODE_200 , "Status code is not 200"); //Assertion
			  		
			  		
			       // Getting Response String
			  		//b. JSON String
			  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			  		System.out.println("Response is" +ResponseString);
			        JSONObject responseJson= new JSONObject(ResponseString);   // Response string converted into JSON Object
			  		System.out.println("Response JSON from API->> " +responseJson);
			  		if(status==200) {
			  			System.out.println("Response is OK- 200");
			  		}else
			  			System.out.println("The order doesn’t exist. Response code " +status);
			  		
			  		
			  		
			  		//To find perpage value in JSON (Assertion in JSON data)
			  		String currency = TestUtil.getValueByJPath(responseJson, "/fare/currency");
			  		System.out.println("The Currency is-> " +currency);
			  		Assert.assertEquals("HKD", currency); // Comparing values.
			  		
			  		
			  		UpdatedURL=Newurl+""+"/take";
			  		//c. All Headers
			  		Header[] headersArray= closableResponse.getAllHeaders();  // Getting all Header from response and stored it into hashmap
			  		HashMap<String,String> allheaders= new HashMap<String,String>();
			  		for(Header header : headersArray ) {
			  		allheaders.put(header.getName(),header.getValue());
			  		
			  		}
			  		
			  		System.out.println("Headers Array->> " +allheaders);
			  
			  	}
			
	
	
			
			@Test(priority=3,expectedExceptions={org.testng.TestException.class})
			public void PUTAPITAKEORDER() throws ClientProtocolException, IOException,Exception {
				
			 	
				client= new RestClient();
				
				 System.out.println("*************Take Order *********************");
				HashMap<String, String> Headers= new HashMap<String, String>();
				Headers.put("Content-Type", "application/json");
				System.out.println(UpdatedURL);
				
				closableResponse = client.Put(UpdatedURL, Headers);
			      
			      
			        //a. Status Code and (Assertion of Status Code).
			  		int status= closableResponse.getStatusLine().getStatusCode();   // Get status code with above object
			  		System.out.println("Status Code->> " +status);
			  		softAssert.assertEquals(status , RESPONSE_STATUS_CODE_200 , "Status code is not 200"); //Assertion
			  		if(status==422){
			  			System.out.println("Logic flow is violated");
			  			
			  		}else if(status==404){
			  			System.out.println("The order doesn’t exist");
			  		} 
			  			else {
			  			System.out.println(" Response code " +status);
			  		
			  		}
			  			
			  			
			       // Getting Response String
			  		//b. JSON String
			  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
			        JSONObject responseJson= new JSONObject(ResponseString);   // Response string converted into JSON Object
			  		System.out.println("Response JSON from API->> " +responseJson);
			  		
			  		
			  		//To find perpage value in JSON (Assertion in JSON data)
			  		String responsejsonstatus = TestUtil.getValueByJPath(responseJson, "/status");
			  		System.out.println("The status is-> " +status);
			  		Assert.assertEquals(responsejsonstatus, "ONGOING"); // Comparing
			  		
			  		
			  		
			  		
			  	}
				  	  
			
			
			
			
			}
	
			


