package com.zapota.socialatm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zapota.socialatm.app.AppController;
import com.zapota.socialatm.model.Bank;
import com.zapota.socialatm.util.BankListAdapter;

public class BankActivity extends Activity {

	// Log tag
		private static final String TAG = BankActivity.class.getSimpleName();

		// Movies json url
		private static final String url = "http://107.178.212.64/banks.json";
		private ProgressDialog pDialog;
		private List<Bank> bankList = new ArrayList<Bank>();
		private ListView listView;
		private BankListAdapter adapter;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank);				
				
		String[] listBanks = getResources().getStringArray(R.array.list_banks);
				
		listView = (ListView) findViewById(R.id.listBanks);
		adapter = new BankListAdapter(this, bankList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		
		final Spinner selectBanks = (Spinner) findViewById(R.id.selectBanks);
		
		
		
		Button btnAddBank = (Button) findViewById(R.id.btnAddBank);
		btnAddBank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View selectedBankItem) {
								
				String bank = selectBanks.getSelectedItem().toString();
				
				
				//TODO: save the selected bank list to db
				
			}
		});

		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								Bank bank = new Bank();
								bank.setTitle(obj.getString("title"));
								String logo = obj.getString("logo");
								if(logo != null){
									bank.setLogoUrl(obj.getString("logo"));
								}else{
									bank.setLogoUrl("http://107.178.212.64/img/bank.jpg");
								}								
								
								// adding movie to movies array
								bankList.add(bank);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hidePDialog();

					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
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
