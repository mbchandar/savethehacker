package com.zapota.socialatm.activity;


import com.zapota.socialatm.json.BankParser;
import com.zapota.socialatm.json.BaseParser;
import com.zapota.socialatm.utils.TinyDB;
import com.zapota.socialatm.utils.Typedef;
import com.zapota.socialatm.utils.SmoothTask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.zapota.socialatm.R;
import com.zapota.socialatm.adapter.BankListAdapter;
import com.zapota.socialatm.model.BankModel;
import com.zapota.socialatm.network.Network;
import com.zapota.socialatm.network.Params;

public class BankActivity extends Activity implements SmoothTask.OnPosted {

	// Log tag
		private static final String TAG = BankActivity.class.getSimpleName();

		// Movies json url
		private static final String url = "http://107.178.212.64/banks.json";
		private ProgressDialog pDialog;
		private List<BankModel> bankList = new ArrayList<BankModel>();
		private Typedef.BankModelList bankList1;
		private ListView listView;
		private BankListAdapter adapter;
		private Spinner selectBanks;
		TinyDB db;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank);				
		 db = new TinyDB(this);
							
		selectBanks = (Spinner) findViewById(R.id.selectBanks);
						
		        
		Button btnAddBank = (Button) findViewById(R.id.btnAddBank);
		btnAddBank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View selectedBankItem) {
												
				int bank = (int) selectBanks.getSelectedItemId();
				BankModel bm = bankList1.get(bank);
							
				ArrayList<Integer> intList = db.getListInt("banks");
				if(!intList.contains(bm.getId())){
					Toast.makeText(getApplicationContext(), "You have selected " + bm.getTitle() +"  "+ bm.getId(), Toast.LENGTH_SHORT).show();
					intList.add(bm.getId());					
					db.putListInt("banks", intList);	
					
				}else{
					Toast.makeText(getApplicationContext(), "Already Exists " + bm.getTitle() +"  "+ bm.getId(), Toast.LENGTH_SHORT).show();
				}
				refreshList();
					
			}

			
		});	
		
		
				
		
	}

	private void refreshList() {
		
		ArrayList<Integer> intList = db.getListInt("banks");
		List<BankModel> bankList = new ArrayList<BankModel>();
		
		listView = (ListView) findViewById(R.id.listBanks);
			
		for(int i : intList){
			bankList.add(bankList1.get(i-1));
		}
	
		adapter = new BankListAdapter(this, bankList);
		adapter.notifyDataSetChanged();		
		listView.setAdapter(adapter);	
				
	}
	
    private void loadData() {     
    	pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		Log.d(TAG," LOAD DATA");
        SmoothTask task = new SmoothTask(this.getApplicationContext(), Network.banks_list, null, false, this);
        task.execute();
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
    public void onResume() {
        super.onResume();
        loadData();
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

	@Override
	public void done(JSONObject object) {
		hidePDialog();
		Log.d(TAG," activity done");
		Log.d(TAG, object.toString());
	
		 BankParser parser = new BankParser(object);
		 bankList1 = parser.parseArray();
		 
		
		 List<String> labels = new ArrayList<String>();
		 for(int i = 0; i< bankList1.size(); i++){
			 labels.add(bankList1.get(i).getTitle());
		 }
		 ArrayAdapter<String> sa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels);
		  
		 selectBanks.setAdapter(sa);		
		 refreshList();
	}

	public void remove(int bankId) {
		Log.d(TAG,"REMOVE DATA");
		ArrayList<Integer> intList = db.getListInt("banks");
		int position = intList.indexOf(bankId);
		intList.remove(position);	
		db.putListInt("banks", intList);
		refreshList();
	}
	 
	 

}
