package com.qa.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.data.Placeorder;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;
import com.qa.util.Xls_Reader;


public class PlaceOrderTest extends TestBase{
	
	public int id2;
	public String apurl;
	public String serviceurl;
	public RestClient client;
	public String url;
	//public TestBase testbase;
	public CloseableHttpResponse closableResponse;
	public Xls_Reader xls;
	

	 public PlaceOrderTest() {  // To call the testbase class methods
	super();
 }
	

	  @BeforeTest
	  public void Setup() throws ClientProtocolException, IOException {
		  apurl= prop.getProperty("url");
		  serviceurl= prop.getProperty("placeorderserviceurl");
		   //https://reqres.in/api/users?page=2
		   url= apurl + serviceurl;
	 	  System.out.println("Place Order endpoint");
	  }

	  
		//@Test(priority=1, dataProvider="getData")
	//	public void POSTAPIWithHeaders(Hashtable<String,String> data) throws JsonGenerationException, JsonMappingException, IOException {
		
	  @Test(priority=1, expectedExceptions= {com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
			public void POSTAPIWithHeaders() throws Exception {
			client= new RestClient();
			 System.out.println("*************Place Order *********************");
			HashMap<String, String> Headers= new HashMap<String, String>();
			Headers.put("Content-Type", "application/json"); // Adding headers in Post call
			// Now add Payload Json data in com.qa.data
			
			
			//Jackson API to convert Java object(users.java) to JSON.
			
			ObjectMapper map= new ObjectMapper(); // Available in Jackson Imported from Maven repo
		//	InputStream is = Test.class.getResourceAsStream("/Placeorder.json");
			//Placeorder po= new Placeorder(data.get("lat"),data.get("lat"));   // Expected Users Object
			
			//Placeorder po= new Placeorder("22.344674","114.124651", "22.375384", "114.182446");   
			//JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("D:\\Projects\\workspace2019\\RestApi_Automation\\src\\main\\java\\com\\qa\\data\\Placeorder2.json");
			
			
			//Object to JSON Object convertor
			Placeorder jsonObject= map.readValue(new File("D:\\Projects\\workspace2019\\RestApi_Automation\\src\\main\\java\\com\\qa\\data\\Placeorderdata.json"), Placeorder.class);
			
			
			
			// Json Object to JSON in String
			 
			 String UsersJsonstring= map.writeValueAsString(jsonObject);
			// System.out.println("This is result" +UsersJsonstring);
			
			 
			//Call Post method and Pass JSON payload as String with it
			 closableResponse = client.post(url, UsersJsonstring, Headers);
			
			 
			 
			 // Validating Response from API
			  //a. Check for validation of Status Code
			  int Statuscode= closableResponse.getStatusLine().getStatusCode();
	          Assert.assertEquals(Statuscode, RESPONSE_STATUS_CODE_201);
			
	 	
		       // Getting Response String
		  	   //b. JSON String
		  		String ResponseString= EntityUtils.toString(closableResponse.getEntity(), "UTF-8");
		        JSONObject responseJson= new JSONObject(ResponseString);   //Response string converted into JSON Object
		  		System.out.println("Response JSON from API->> " +responseJson); // Output of response json
		  		
		  	 //To find perpage value in JSON (Assertion in JSON data)
		  		String id = TestUtil.getValueByJPath(responseJson, "/id");
		  		System.out.println("Response id is: " +id);
		 

		  		 id2=Integer.parseInt(id);
		  		 
		  		
		  		//To validate the response and compare.
		  		//Json to JavaObject
		  		Placeorder usersrespobj= map.readValue(ResponseString,Placeorder.class);  //Actual User Object
		  		System.out.println(usersrespobj);
		  		
		  		//Assert the Name and Job
//		  		Assert.assertTrue((po.getLat().equals(usersrespobj.getLat())),  "Error in lat");
//		  		
//		  		Assert.assertTrue(po.getLng().equals(usersrespobj.getLng()),  "Error in lng");
//		  		
     	  
		  		
		  
		  	}
	
	  
	  
	
		
		
		
		@DataProvider
		public Object[][] getData(){  
			 xls = new Xls_Reader("D:\\Projects\\workspace2019\\RestApi_Automation\\src\\main\\java\\com\\qa\\testdata\\APIDATA.xlsx");
			 String sheetName="JSONDATA";
				String testCaseName="TestA";

			// reads data for only testCaseName
			
			int testStartRowNum=1;
			while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testCaseName)){
				testStartRowNum++;
			}
			System.out.println("Test starts from row - "+ testStartRowNum);
			int colStartRowNum=testStartRowNum+1;
			int dataStartRowNum=testStartRowNum+2;
			
			// calculate rows of data
			int rows=0;
			while(!xls.getCellData(sheetName, 0, dataStartRowNum+rows).equals("")){
				rows++;
			}
			System.out.println("Total rows are  - "+rows );
			
			//calculate total cols
			int cols=0;
			while(!xls.getCellData(sheetName, cols, colStartRowNum).equals("")){
				cols++;
			}
			System.out.println("Total cols are  - "+cols );
			Object[][] data = new Object[rows][1]; // Instead of printing data we make object array
			//read the data
			int dataRow=0;
			Hashtable<String,String> table=null;
			for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
				table = new Hashtable<String,String>();
				for(int cNum=0;cNum<cols;cNum++){
					String key=xls.getCellData(sheetName,cNum,colStartRowNum);
					String value= xls.getCellData(sheetName, cNum, rNum);
					table.put(key, value);
					// 0,0 0,1 0,2
					//1,0 1,1
				}
				data[dataRow][0] =table;
				dataRow++;
			}
			return data;
		}
		
	  
}
