package com.qa.restclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class RestClient {

	
	//1. GET Method Without Headers
	 public CloseableHttpResponse geturl(String url) throws ClientProtocolException, IOException {
	  //Getting Response code
		CloseableHttpClient httpclient= HttpClients.createDefault(); // Create a simple client
		HttpGet httpget= new HttpGet(url);   // Create Get connection with url
		CloseableHttpResponse closableResponse= httpclient.execute(httpget); // Hit the API/URL (Just like send button of postman)
		return closableResponse;	
	 
}
	 
	    //2. GET Method With Headers
	  public CloseableHttpResponse geturl(String url, HashMap<String,String> headerMap) throws ClientProtocolException, IOException {
	    //Getting Response code
		CloseableHttpClient httpclient= HttpClients.createDefault(); // Create a simple client
		HttpGet httpget= new HttpGet(url);   // Create Get connection with url
		
		//for headers:
		for(Map.Entry<String,String> entry : headerMap.entrySet()){
			httpget.addHeader(entry.getKey(), entry.getValue());
		}
		
		CloseableHttpResponse closableResponse= httpclient.execute(httpget); // Hit the API/URL (Just like send button of postman)
	    return closableResponse;
	
	 
}
	  
	  
	  //3. POST METHOD
	  public CloseableHttpResponse post(String url, String entitystring,  HashMap<String,String> headerMap ) throws ClientProtocolException, IOException {
		  CloseableHttpClient httpclient= HttpClients.createDefault(); // Create a simple client
		  HttpPost httppost= new HttpPost(url);    // Create Post connection with url
		  httppost.setEntity(new StringEntity(entitystring)); //for payload
		  
		  
		//for headers:
			for(Map.Entry<String,String> entry : headerMap.entrySet()){
				httppost.addHeader(entry.getKey(), entry.getValue());
			}
		  
			CloseableHttpResponse closableResponse= httpclient.execute(httppost); // Hit the API/URL (Just like send button of postman)
		    return closableResponse;
	  }
	  
	  
	//2. PUT Method
	  public CloseableHttpResponse Put(String url, HashMap<String,String> headerMap) throws ClientProtocolException, IOException {
	    //Getting Response code
		CloseableHttpClient httpclient= HttpClients.createDefault(); // Create a simple client
		HttpPut httpput= new HttpPut(url);   // Create Get connection with url
		
		//for headers:
		for(Map.Entry<String,String> entry : headerMap.entrySet()){
			httpput.addHeader(entry.getKey(), entry.getValue());
		}
		
		CloseableHttpResponse closableResponse= httpclient.execute(httpput); // Hit the API/URL (Just like send button of postman)
	    return closableResponse;
	
	 
}
}

	
