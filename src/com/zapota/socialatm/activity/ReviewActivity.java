package com.zapota.socialatm.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.models.PlaceDetails;
import com.a2plab.googleplaces.result.PlaceDetailsResult;
import com.a2plab.googleplaces.result.Result.StatusCode;
import com.zapota.socialatm.R;
import com.zapota.socialatm.R.id;
import com.zapota.socialatm.R.layout;
import com.zapota.socialatm.R.menu;

public class ReviewActivity extends Activity {

	private GooglePlaces client;
	
	private ProgressBar progressBar;
	private TextView textView;
	
	RatingBar ratingCash;
	RatingBar ratingSecurity;
	RatingBar ratingBank;
	RatingBar ratingAc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
	
		client = new GooglePlaces("AIzaSyArL2I3jVkYCTpJNtZ_zR6Buph-H5Ule6M");
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);	
		
		ratingCash = (RatingBar) findViewById(R.id.ratingCash);
		ratingSecurity = (RatingBar) findViewById(R.id.ratingSecurity);
		ratingBank = (RatingBar) findViewById(R.id.ratingBank);
		ratingAc = (RatingBar) findViewById(R.id.ratingAc);
		
		textView = (TextView) findViewById(R.id.bankId); 
		
		Intent intent = getIntent();		
		String placeReference = intent.getStringExtra("placeReference");						
		
		Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//TODO: submit the rating and move on
				
				// WebServer Request URL
				String serverURL = "http://192.168.24.86:3000/";
				
				// Use AsyncTask execute Method To Prevent ANR Problem
		        new PostRatingTask().execute(serverURL);
				
				
				
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.review, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class PostRatingTask extends AsyncTask<String, Void, Void> {

		 private final HttpClient Client = new DefaultHttpClient();private String Content;
	     private String Error = null;
	     private ProgressDialog Dialog = new ProgressDialog(ReviewActivity.this);
	     String data =""; 
	     protected void onPreExecute() {
	         // NOTE: You can call UI Element here.
	          
	         //Start Progress Dialog (Message)
	        
	         Dialog.setMessage("Please wait..");
	         Dialog.show();
	         
	         try{
	         	// Set Request parameter
	             data +="&" + URLEncoder.encode("data", "UTF-8");
		            	
	         } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	         
	     }

	     // Call after onPreExecute method
	     protected Void doInBackground(String... urls) {
	     	
	     	/************ Make Post Call To Web Server ***********/
	     	BufferedReader reader=null;

		             // Send data 
		            try
		            { 
		              
		               // Defined URL  where to send data
		               URL url = new URL(urls[0]);
		                 
		              // Send POST data request
		   
		              URLConnection conn = url.openConnection(); 
		              conn.setDoOutput(true); 
		              OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
		              wr.write( data ); 
		              wr.flush(); 
		          
		              // Get the server response 
		               
		              reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		              StringBuilder sb = new StringBuilder();
		              String line = null;
		            
			            // Read Server Response
			            while((line = reader.readLine()) != null)
			                {
			                       // Append server response in string
			                       sb.append(line + "");
			                }
		                
		                // Append Server Response To Content String 
		               Content = sb.toString();
		            }
		            catch(Exception ex)
		            {
		            	Error = ex.getMessage();
		            }
		            finally
		            {
		                try
		                {
		     
		                    reader.close();
		                }
		   
		                catch(Exception ex) {}
		            }
	     	
	         /*****************************************************/
	         return null;
	     }
	      
	     protected void onPostExecute(Void unused) {
	         // NOTE: You can call UI Element here.
	          
	         // Close progress dialog
	         Dialog.dismiss();
	          
	         if (Error == null) {

	           
	         	 
	         	
	          /****************** Start Parse Response JSON Data *************/
	         	
	         	String OutputData = "";
	             JSONObject jsonResponse;
	                   
	             try {
	                   
	                  /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
	                  jsonResponse = new JSONObject(Content);
	                   
	                  /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
	                  /*******  Returns null otherwise.  *******/
	                  JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
	                   
	                  /*********** Process each JSON Node ************/

	                  int lengthJsonArr = jsonMainNode.length();  

	                  for(int i=0; i < lengthJsonArr; i++) 
	                  {
	                      /****** Get Object for each JSON node.***********/
	                      JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	                       
	                      /******* Fetch node values **********/
	                      String name       = jsonChildNode.optString("name").toString();
	                      String number     = jsonChildNode.optString("number").toString();
	                      String date_added = jsonChildNode.optString("date_added").toString();
	                       
	                     
	                      OutputData += " Name 		    : "+ name +""
	                                  + "Number 		: "+ number +""
	                                  + "Time 				: "+ date_added +"" 
	                                  +"--------------------------------------------------";
	                     
	                      
	                 }
	                  
	                  Log.d("[POSTRATINGTASKDATA]", OutputData);
	                   
	                  
	                  //Show Parsed Output on screen (activity)
	                  
	                  
	                   
	              } catch (JSONException e) {
	       
	                  e.printStackTrace();
	              }

	              
	          }
	         
	         ratingCash.getRating();
				ratingSecurity.getRating();
				ratingBank.getRating();
				ratingAc.getRating();
				
				
				Intent i = new Intent(getApplicationContext(), ThanksActivity.class);
				startActivity(i);
	     }
	      
	 }


}
