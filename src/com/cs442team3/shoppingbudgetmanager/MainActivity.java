package com.cs442team3.shoppingbudgetmanager;

import java.util.Calendar;
import java.util.Locale;

import com.ebay.redlasersdk.RedLaserExtras;
import com.ebay.redlasersdk.RedLaserExtras.RLScannerReadyState;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btn_add, btn_this, btn_detail, btn_all;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		
		// Check if barcode available
		RLScannerReadyState rlState = RedLaserExtras.checkReadyStatus(getBaseContext());
		if(rlState!=RLScannerReadyState.EvalModeReady
				&& rlState!=RLScannerReadyState.LicensedModeReady)
		{
			editor.putBoolean("isBarcode", false);
			editor.commit();
			Log.d("Barcode", "Barcode initialization failed");
		}
		
    	if(preference.contains("name"))
    	{
    		
    		Calendar c = Calendar.getInstance();
    		String month_present = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
    		
    		String month = preference.getString("month", month_present);
    		Log.d("Month","Current "+month_present+" \nIn Pref-"+month);
    		if(month.equals(month_present))
    		{
    			
    			setContentView(R.layout.activity_main);
    			btn_add =  (Button) findViewById(R.id.btn_add);
        		btn_this = (Button) findViewById(R.id.btn_this);
        		btn_detail = (Button) findViewById(R.id.btn_detail);
        		btn_all = (Button) findViewById(R.id.btn_all);
        		btn_add.setOnClickListener(this);
        		btn_this.setOnClickListener(this);
        		btn_detail.setOnClickListener(this);
        		btn_all.setOnClickListener(this);
    		}
    		else
    		{
    			DBClass.DB_VERSION += 1;
    			Intent intent = new Intent(this,AddMonth.class);
    	    	
    	    	Bundle b = new Bundle();
    	    	b.putBoolean("status_spent", false);
    	    	intent.putExtras(b);
    	    	
    	        startActivity(intent);
    	        finish();
    		}    		
    	}
    	else {
    		Intent intent = new Intent(this,LoginPage.class);
            startActivity(intent);
            finish();
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_add:
			Intent intent = new Intent(this,AddNew.class);
            startActivity(intent);
			break;

		case R.id.btn_this:
			Intent intent1 = new Intent(this,EditMonth.class);
            startActivity(intent1);
			break;
		
		case R.id.btn_detail:
			Intent intent2 = new Intent(this,MonthDetail.class);
            startActivity(intent2);
			break;
			
		case R.id.btn_all:
			Intent intent3 = new Intent(this,AllMonths.class);
            startActivity(intent3);
			break;
			
		default:
			break;
		}
		
	}
}
