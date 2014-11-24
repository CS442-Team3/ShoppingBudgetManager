package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.Calendar;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_detail);
		
		listview = (ListView) findViewById(R.id.items_list);
		
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
	}

	@Override
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
	}
}
