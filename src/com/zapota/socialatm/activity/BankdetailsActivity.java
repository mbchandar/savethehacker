package com.zapota.socialatm.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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

public class BankdetailsActivity extends Activity {

	private GooglePlaces client;
	
	private ProgressBar progressBar;
	private TextView txtbankName;
	
	ImageView imgBankIcon;
	TextView txtBankAddress;
	TextView txtTimings;
	TextView txtPhoneNumber;
	RatingBar rating;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankdetails);
		

		txtbankName = (TextView) findViewById(R.id.txtBankName);
		imgBankIcon = (ImageView) findViewById(R.id.imgBankIcon);
		txtBankAddress = (TextView) findViewById(R.id.txtBankAddress);
		rating = (RatingBar) findViewById(R.id.ratingAc);
		txtTimings = (TextView) findViewById(R.id.txtTimings);
		txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
		
		client = new GooglePlaces("AIzaSyArL2I3jVkYCTpJNtZ_zR6Buph-H5Ule6M");
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		Intent intent = getIntent();		
		String placeReference = intent.getStringExtra("placeReference");
				
		new PlacesLoadTask().execute(placeReference);
		
		Button btnSubmitRating = (Button) findViewById(R.id.btnSubmitRating);
		btnSubmitRating.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
				 startActivity(i);				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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
	
	 private class PlacesLoadTask extends AsyncTask<String, Void, PlaceDetailsResult> {

		 protected void onPreExecute() {
             // Runs on the UI thread before doInBackground
             // Good for toggling visibility of a progress indicator
             progressBar.setVisibility(ProgressBar.VISIBLE);
         }
		 
		@Override
		protected PlaceDetailsResult doInBackground(String... reference) {
			try {
				PlaceDetailsResult detailsResult =  (PlaceDetailsResult) client.getPlaceDetails(reference[0]);
				return detailsResult;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		 protected void onPostExecute(PlaceDetailsResult placeDetail) {	  
			 
			 if (placeDetail.getStatusCode() == StatusCode.OK) {
				    PlaceDetails placeDetails = placeDetail.getDetails();
				    
				    txtbankName.setText(placeDetails.getName());
				    rating.setRating((float)placeDetails.getRating());				    				    
				    
				    Uri url = Uri.parse(placeDetails.getIcon());
				    imgBankIcon.setImageURI(url);
				    
					//imgBankIcon.setImageURI(placeDetails.getIcon());
				    txtBankAddress.setText(placeDetails.getFormattedAddress());
				    txtPhoneNumber.setText(placeDetails.getFormattedPhoneNumber());
				    if(placeDetails.getOpeningHours() != null)
				    	txtTimings.setText("Open");
				    else
				    	txtTimings.setText("Close");
				}
			
			 
             // Hide the progress bar
             progressBar.setVisibility(ProgressBar.INVISIBLE);                          
         }
		 
	 }
}
