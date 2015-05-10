package com.zapota.socialatm.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.zapota.socialatm.R;
import com.zapota.socialatm.app.AppController;
import com.zapota.socialatm.model.Bank;


public class BankListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Bank> banks;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public BankListAdapter(Activity activity, List<Bank> banks) {
		this.activity = activity;
		this.banks = banks;
	}

	@Override
	public int getCount() {
		return banks.size();
	}

	@Override
	public Object getItem(int location) {
		return banks.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);

		// getting movie data for the row
		Bank m = banks.get(position);

		// thumbnail image
		thumbNail.setImageUrl("http://107.178.212.64/img/"+m.getLogoUrl(), imageLoader);
		
		// title
		title.setText(m.getTitle());
				
		return convertView;
	}

}