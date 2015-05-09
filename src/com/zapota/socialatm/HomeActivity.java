package com.zapota.socialatm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Button btnNearest = (Button) findViewById(R.id.btn_nearest);
		Button btnMybank = (Button) findViewById(R.id.btn_mybank);
		Button btnUsage = (Button) findViewById(R.id.btn_usage);
		
		btnNearest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Start your app main activity
                Intent i = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(i);			
			}
		});
		
		btnMybank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Start your app main activity
                Intent i = new Intent(HomeActivity.this, BankActivity.class);
                startActivity(i);
				
			}
		});
		
		btnUsage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Start your app main activity
                Intent i = new Intent(HomeActivity.this, UsageActivity.class);
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
}
