package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MonthDetail extends Activity {

	ListView listview;
	DBClass dbobj;
	CustomListAdapter adapter;
	List<Item> itemList = new ArrayList<Item>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_detail);
		
		listview = (ListView) findViewById(R.id.items_list);
		
		adapter = new CustomListAdapter(this, itemList);
		listview.setAdapter(adapter);
		
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

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
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.month_detail, menu);
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
	}*/
}
