package com.zapota.socialatm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener, OnCameraChangeListener, OnMapReadyCallback, OnMarkerClickListener {

	private SupportMapFragment mapFragment;
	GoogleMap map;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private long UPDATE_INTERVAL = 600000;  /* 10 mins */
	private long FASTEST_INTERVAL = 50000; /* 50 secs */
	
	private ProgressBar progressBar;
	
	private LatLng center;

	private GooglePlaces client;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		client = new GooglePlaces("AIzaSyArL2I3jVkYCTpJNtZ_zR6Buph-H5Ule6M");
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //StrictMode.setThreadPolicy(policy);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		
		mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);					
		} else {
			Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
		}

	}
 

    private void loadPlaces(LatLng latLng) {
    	if (latLng != null){
    		map.clear();
    		new PlacesLoadTask().execute(latLng);
    	}    	
	}
    
 // The types specified here are the input data type, the progress type, and the result type
    private class PlacesLoadTask extends AsyncTask<LatLng, Void, List<Place>> {
         protected void onPreExecute() {
             // Runs on the UI thread before doInBackground
             // Good for toggling visibility of a progress indicator
             progressBar.setVisibility(ProgressBar.VISIBLE);
         }

         protected List<Place> doInBackground(LatLng... latLng) {
        	 
        	Log.d("[PLACESLOADTASK]", latLng[0].toString());   
        	
        		//List<Place> places = client.getPlacesByRadar(latLng[0].latitude, latLng[0].longitude, 1000, 40, Param.name("types").value("atm"));
        	List<String> types = new ArrayList<String>();
        	types.add("atm");
        	
        		//PlacesResult places = client.getNearbyPlaces(types, 5000,latLng[0].latitude , latLng[0].longitude);
        	
        		PlacesResult result;
				try {
					
					
					
					result = (PlacesResult) client.getNearbyPlaces(types, 2000, latLng[0].latitude, latLng[0].longitude);
					if (result.getStatusCode() == StatusCode.OK) {
					    List<Place> placesList = (List<Place>) result.getResults();
					    return placesList;
					}else{
						return null;
					}					
		        	 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
        		
        		
					     		
         }         

         protected void onPostExecute(List<Place> placesList) {
        	
        	 if (placesList != null) {
        		 for (Place place : placesList) {
        			 mapFragment.getMap()
        	            .addMarker(new MarkerOptions()
        	                .position(new LatLng(place.getGeometry().location.lat, place.getGeometry().location.lng))
        	                .title(place.getName())
        	                .snippet(place.getFormattedAddress())
        	            .icon(BitmapDescriptorFactory.defaultMarker()));
				}
        		
        	 }
             // Hide the progress bar
             progressBar.setVisibility(ProgressBar.INVISIBLE);                          
         }
 
    }

	protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
		super.onStop();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				mGoogleApiClient.connect();
				break;
			}

		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (location != null) {
			Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			map.animateCamera(cameraUpdate);
            startLocationUpdates();            
            map.setOnCameraChangeListener(this);             
        } else {
			Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
                
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        
        loadPlaces(latLng);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //map.setOnCameraChangeListener(this);
    }

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		  center = map.getCameraPosition().target;   		  		  		           
          Log.d("[MAP]", center.toString());         
          try {
        	
          	loadPlaces(center);
          } catch (Exception e) {
          	
          }
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		 	map = googleMap;
	        if (map != null) {
	            // Map is ready
	           // Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
	            map.setMyLocationEnabled(true);            
	            
	            // Now that map has loaded, let's get our location!
	            mGoogleApiClient = new GoogleApiClient.Builder(this)
	                    .addApi(LocationServices.API)
	                    .addApi(Places.GEO_DATA_API)
	                    .addApi(Places.PLACE_DETECTION_API)
	                    .addConnectionCallbacks(this)
	                    .addOnConnectionFailedListener(this).build();
	                       
	            connectClient();
	                      
	        } else {
	            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
	        }
	}


	@Override
	public boolean onMarkerClick(Marker marker) {
		Log.e("TESTING", "on Marker click: " + marker.getTitle());
		marker.showInfoWindow();
		return false;
	}

}