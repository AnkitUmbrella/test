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

	

	    public CloseableHttpResponse geturl(String url) throws ClientProtocolException, IOException {
	
		CloseableHttpClient httpclient= HttpClients.createDefault(); 
		HttpGet httpget= new HttpGet(url);   
		CloseableHttpResponse closableResponse= httpclient.execute(httpget); 
		return closableResponse;	
	 
        }
	 
	    
	    public CloseableHttpResponse geturl(String url, HashMap<String,String> headerMap) throws ClientProtocolException, IOException {
	   
		CloseableHttpClient httpclient= HttpClients.createDefault(); 
		HttpGet httpget= new HttpGet(url);   
	
		for(Map.Entry<String,String> entry : headerMap.entrySet()){
		httpget.addHeader(entry.getKey(), entry.getValue());
		}
		
		CloseableHttpResponse closableResponse= httpclient.execute(httpget); 
	    return closableResponse;
	
	 
        }
	  
	 
	     public CloseableHttpResponse post(String url, String entitystring,  HashMap<String,String> headerMap ) throws ClientProtocolException, IOException {
		   
		 CloseableHttpClient httpclient= HttpClients.createDefault(); 
		 HttpPost httppost= new HttpPost(url);   
		 httppost.setEntity(new StringEntity(entitystring)); 
		  
		 for(Map.Entry<String,String> entry : headerMap.entrySet()){
		 httppost.addHeader(entry.getKey(), entry.getValue());
		 }
		  
		 CloseableHttpResponse closableResponse= httpclient.execute(httppost); 
		 return closableResponse;
	     }
	  
	  
	
	     public CloseableHttpResponse Put(String url, HashMap<String,String> headerMap) throws ClientProtocolException, IOException {
	   
		 CloseableHttpClient httpclient= HttpClients.createDefault(); 
		 HttpPut httpput= new HttpPut(url);   
		
		
		 for(Map.Entry<String,String> entry : headerMap.entrySet()){
		 httpput.addHeader(entry.getKey(), entry.getValue());
		 }
		
		 CloseableHttpResponse closableResponse= httpclient.execute(httpput); 
	     return closableResponse;
	 
	 
         }
         }

	
