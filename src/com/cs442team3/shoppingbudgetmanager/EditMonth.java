package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditMonth extends Activity {

	WebView webView;
	ListView listview;
	DBClass dbobj;
	CustomListAdapter adapter;
	List<Item> itemList = new ArrayList<Item>();
	
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
		
		adapter = new CustomListAdapter(this, itemList);
		listview.setAdapter(adapter);
		
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		setTitle("Edit "+month);
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		String[] coloums = {"_id","name","date","price"};
		
		Cursor cursor = database.query(month, coloums,null,null,null,null, null);
		
		while(cursor.moveToNext())
		{
			Item i = new Item();
			i.setId(cursor.getInt(0));
			i.setName(cursor.getString(1));
			i.setDate(cursor.getString(2));
			i.setPrice(cursor.getString(3));
			itemList.add(i);
		}
		
		adapter.notifyDataSetChanged();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {

				// List<Item> itemlist = new ArrayList<Item>();
				Calendar c = Calendar.getInstance();
				String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
				String[] columns = {"_id","name","price"};
				DBClass dbobj = new DBClass(getBaseContext(),month);
				SQLiteDatabase database = dbobj.getWritableDatabase();
				Cursor cursor = database.query(month, columns, null, null, null, null, null);
				List<Item> items = new ArrayList<Item>();
				while(cursor.moveToNext())
				{
					Item i = new Item();
					i.setId(cursor.getInt(0));
					i.setName(cursor.getString(1));
					i.setPrice(cursor.getString(2));
					items.add(i);
				}
				database.close();
				if(items.size()>0&&position>0)
				{
					Log.v("Position", ""+position);
					int ID = items.get(position-1).getId();
					ID=(position==1)?ID+1:ID;
					Log.v("clicked item", items.get(ID).getId()+" "+items.get(ID).getName()+" $"+items.get(ID).getPrice());
					Intent intent = new Intent(getBaseContext(),EditItem.class);
					intent.putExtra("Edit_Id", items.get(ID).getId());
					intent.putExtra("Edit_Name", items.get(ID).getName());
					intent.putExtra("Edit_Price", items.get(ID).getPrice());
					startActivity(intent);
				}
			}
		});
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

	private void clear_all() {
		
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
    	editor.putFloat("budget_spent",0);
    	editor.commit();
    	
    	Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
	
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		database.execSQL("delete from "+ month+" where _id > 0");
		
		ContentValues content1 = new ContentValues();
		content1.put("price",0);
		database.update(month, content1,"_id "+"="+0, null);
		
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
