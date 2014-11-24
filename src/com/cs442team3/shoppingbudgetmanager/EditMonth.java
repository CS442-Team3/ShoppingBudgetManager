package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditMonth extends Activity {

	WebView webView;
	ListView listview;
	DBClass dbobj;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_month);
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.addJavascriptInterface(new WebAppInterface(), "Android");
	    webView.getSettings().setJavaScriptEnabled(true); 
	    webView.loadUrl("file:///android_asset/chart.html");
	     
		listview = (ListView) findViewById(R.id.listView1);
		
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		String[] coloums = {"_id","name","date","price"};
		
		Cursor cursor = database.query(month, coloums,null,null,null,null, null);
		
		ArrayList<String> db_records = new ArrayList<String>();
		int id;
		String data="";
		while(cursor.moveToNext())
		{
			id = cursor.getInt(0);
			data += id+" ";
			data += cursor.getString(1);
			data += "     ";
			data += cursor.getString(2);
			data += "  ";
			data += cursor.getString(3);
			data += " $";
			Log.d("DB Values",data);
			db_records.add(data);
			data = "";
			
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,db_records);
		listview.setAdapter(adapter);
		
		database.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_month, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
	        case R.id.clear_all:
	            clear_all();
	            return true;
	            
	        case R.id.edit_items:
	            edit_items();
	            return true;
	            
	        case R.id.edit_budget:
	            edit_budget();
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	private void edit_budget() {
		Intent intent = new Intent(this,AddMonth.class);
		Bundle b = new Bundle();
    	b.putBoolean("status_spent", true);
    	intent.putExtras(b);
        startActivity(intent);
        finish();
	}

	private void edit_items() {
		// TODO Auto-generated method stub
		
	}

	private void clear_all() {
		
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
    	editor.putFloat("budget_spent",0);
    	editor.commit();
    	
    	Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		database.execSQL("delete from "+ month); 
		database.close();
		
		onCreate(null);
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
}
