package com.cs442team3.shoppingbudgetmanager;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;

@SuppressLint("SetJavaScriptEnabled") 
public class AddNew extends Activity implements OnClickListener{

	Button btn_barcode,btn_manual;
	WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		 webView = (WebView)findViewById(R.id.webView_add_item);
	     webView.addJavascriptInterface(new WebAppInterface(), "Android");
	     webView.getSettings().setJavaScriptEnabled(true); 
	     webView.loadUrl("file:///android_asset/chart.html");
	     
	     btn_barcode = (Button) findViewById(R.id.btn_barcode);
	     btn_manual = (Button) findViewById(R.id.btn_manual);
	     
	     btn_barcode.setOnClickListener(this);
	     btn_manual.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new, menu);
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
	
	
	public class WebAppInterface {

		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		float amount = preference.getFloat("budget_total", 0);
		float spent = preference.getFloat("budget_spent",0);
	     
		@JavascriptInterface
	    public float getNum1() {
	    	return (amount-spent);
	     }
	  

		@JavascriptInterface
	    public float getNum2() {
	    	return spent;
	     }
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_barcode:
			Intent intent = new Intent(this,Barcode.class);
            startActivity(intent);
            finish();
			break;
			
		case R.id.btn_manual:
			Intent intent1 = new Intent(this,AddItem.class);
            startActivity(intent1);
            finish();
			break;

		default:
			break;
		}
		
	}
}



