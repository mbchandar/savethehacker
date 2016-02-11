package com.zapota.socialatm.activity;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.zapota.socialatm.R;
import com.zapota.socialatm.R.id;
import com.zapota.socialatm.R.layout;
import com.zapota.socialatm.R.menu;

public class UsageActivity extends FragmentActivity implements OnSeekBarChangeListener,
OnChartValueSelectedListener{

	 // Cursor Adapter
    SimpleCursorAdapter adapter;
    
    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    private Typeface tf;
    
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
    		"BW-HDFCBK","BZ-CANBNK","BW-ICICIB"
    };

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usage);
		
		
      
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        
        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        
        mChart.setTransparentCircleColor(Color.WHITE);
        
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);   

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.setCenterText("ATM Usage Data");

        setData(3, 100);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.usage, menu);
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
	
	 @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	        tvX.setText("" + (mSeekBarX.getProgress() + 1));
	        tvY.setText("" + (mSeekBarY.getProgress()));

	        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
	    }

	    private void setData(int count, float range) {

	        float mult = range;

	        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
	        
	        

	     // Create Inbox box URI
			Uri inboxURI = Uri.parse("content://sms/inbox");
			 
			// List required columns
			String[] reqCols = new String[] { "_id", "address", "body" };
			 
			// Get Content Resolver object, which will deal with Content Provider
			ContentResolver cr = getContentResolver();
			 
			String[] selection = new String[] {"BW-HDFCBK","BZ-CANBNK","BW-ICICIB"};				
			
			// Fetch Inbox SMS Message from Built-in Content Provider
			Cursor cs = cr.query(inboxURI, reqCols, "address IN (?,?,?)", selection, null);
			
			//Cursor c = cr.query(inboxURI, reqCols, null,null, null);
			
			String messages = "";
			
			int hdfc = 0;
			int canbk = 0;
			int icici = 0;
			
			
			while (cs.moveToNext()) {
				if(cs.getString(cs.getColumnIndexOrThrow("address")) =="BW-HDFCBK"){
					hdfc+= 1;
				}else if(cs.getString(cs.getColumnIndexOrThrow("address")) =="BZ-CANBNK"){
					canbk+= 1;
				}else if(cs.getString(cs.getColumnIndexOrThrow("address")) =="BW-ICICIB"){
					icici+= 1;
				}
							
				
			    messages += cs.getString(cs.getColumnIndexOrThrow("address")) + "\n\n\t";
			}
			messages += "\n";
			
			//Log.d("[CURSORTAG]",messages);
			
	        // IMPORTANT: In a PieChart, no values (Entry) should have the same
	        // xIndex (even if from different DataSets), since no values can be
	        // drawn above each other.
	        for (int i = 0; i < count + 1; i++) {
	            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
	        }
	        	//yVals1.add(new Entry((float) hdfc));
	        	//yVals1.add(hdfc);

	        ArrayList<String> xVals = new ArrayList<String>();

	        for (int i = 0; i < count + 1; i++)
	            xVals.add(mParties[i % mParties.length]);

	        PieDataSet dataSet = new PieDataSet(yVals1, "Card Usage");
	        dataSet.setSliceSpace(3f);
	        dataSet.setSelectionShift(5f);

	        // add a lot of colors

	        ArrayList<Integer> colors = new ArrayList<Integer>();

	        for (int c : ColorTemplate.VORDIPLOM_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.JOYFUL_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.COLORFUL_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.LIBERTY_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.PASTEL_COLORS)
	            colors.add(c);

	        colors.add(ColorTemplate.getHoloBlue());

	        dataSet.setColors(colors);

	        PieData data = new PieData(xVals, dataSet);
	        data.setValueFormatter(new PercentFormatter());
	        data.setValueTextSize(11f);
	        data.setValueTextColor(Color.WHITE);
	        data.setValueTypeface(tf);
	        mChart.setData(data);

	        // undo all highlights
	        mChart.highlightValues(null);

	        mChart.invalidate();
	    }

	    @Override
	    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

	        if (e == null)
	            return;
	        Log.i("VAL SELECTED",
	                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
	                        + ", DataSet index: " + dataSetIndex);
	    }

	    @Override
	    public void onNothingSelected() {
	        Log.i("PieChart", "nothing selected");
	    }

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
}
