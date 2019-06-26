package com.qa.test;

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

import com.qa.base.TestBase;
import com.qa.data.Placeorder;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;

public class FetchOrderDetail extends TestBase{

	public String apurl;
	public String serviceurl;
	public RestClient client;
	public String url;
	//public TestBase testbase;
	public CloseableHttpResponse closableResponse;
	  public Placeorder po= new Placeorder();
	
	
	 public FetchOrderDetail() {
	super();
  }
	 public void getdata(String id) {
			
		}
		
  
  
  @BeforeTest
  public void Setup() throws ClientProtocolException, IOException {
	 // testbase= new TestBase();
	   po= new Placeorder();
	  apurl= prop.getProperty("url");
	  serviceurl= prop.getProperty("serviceurlget");
	   //https://reqres.in/api/users?page=2
	   url= apurl + serviceurl;
 	  System.out.println("Fetch Order Details endpoint");
  }
  
  
	
	
	
	@Test(priority=1)
	public void GetAPIWithHeaders() throws ClientProtocolException, IOException {
		client= new RestClient();
		
		HashMap<String, String> Headers= new HashMap<String, String>();
		Headers.put("Content-Type", "application/json");
		
		
		closableResponse = client.geturl(url , Headers);
		
	      System.out.println("*************Fetch Order Details *********************");
	      
	      
	        //a. Status Code and (Assertion of Status Code).
	  		int status= closableResponse.getStatusLine().getStatusCode();   // Get status code with above object
	  		System.out.println("Status Code->> " +status);
	  		//Assert.assertEquals(status , RESPONSE_STATUS_CODE_200 , "Status code is not 200"); //Assertion
	  		
	  		
	       // Getting Response String
	  		//b. JSON String
	  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
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
	  		
	  		
	  		
	  		//c. All Headers
	  		Header[] headersArray= closableResponse.getAllHeaders();  // Getting all Header from response and stored it into hashmap
	  		HashMap<String,String> allheaders= new HashMap<String,String>();
	  		for(Header header : headersArray ) {
	  		allheaders.put(header.getName(),header.getValue());
	  		
	  		}
	  		
	  		System.out.println("Headers Array->> " +allheaders);
	  		
	  	}
		  	  
	}

	

	

