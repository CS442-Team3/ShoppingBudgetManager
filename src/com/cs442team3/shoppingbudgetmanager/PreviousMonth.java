package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PreviousMonth extends Activity {

	WebView webView;
	ListView listview;
	DBClass dbobj;
	Float amount_spent;
	Float total_budget;
	CustomListAdapter adapter;
	List<Item> itemList = new ArrayList<Item>();
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_month);
		
		Bundle b = getIntent().getExtras();
		String month = b.getString("month", null);
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.addJavascriptInterface(new WebAppInterface(), "Android");
	    webView.getSettings().setJavaScriptEnabled(true); 
	    webView.loadUrl("file:///android_asset/chart.html");
	     
		listview = (ListView) findViewById(R.id.listView1);
		
		adapter = new CustomListAdapter(this, itemList);
		listview.setAdapter(adapter);
		
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		String[] coloums = {"_id","name","date","price"};
		
		Cursor cursor = database.query(month, coloums,null,null,null,null, null);
		
		
		int id;
		String data="";
		while(cursor.moveToNext())
		{
			id = cursor.getInt(0);
			
			if(id != 0)
				{
					Item i = new Item();
					i.setId(id);
					i.setName(cursor.getString(1));
					i.setDate(cursor.getString(2));
					i.setPrice(cursor.getString(3));
					itemList.add(i);
				}
			else
			{
				total_budget = Float.parseFloat(cursor.getString(1));
				amount_spent = Float.parseFloat(cursor.getString(3));
			}
			
		}
		adapter.notifyDataSetChanged();
	}
	
	public class WebAppInterface {

		@JavascriptInterface
	    public float getNum1() {
	    	return (total_budget-amount_spent);
	     }
	  

		@JavascriptInterface
	    public float getNum2() {
	    	return amount_spent;
	     }
	}

}
