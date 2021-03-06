package com.cs442team3.shoppingbudgetmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends Activity implements OnClickListener{

	Button add_item;
	AutoCompleteTextView item_name;
	EditText item_price;
	DBClass dbobj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		
		add_item = (Button) findViewById(R.id.btn_add_item);
		add_item.setOnClickListener(this);
		
		item_name = (AutoCompleteTextView) findViewById(R.id.item_name);
		item_price = (EditText) findViewById(R.id.item_price);
		
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		String[] coloums = {"name"};
		
		Cursor cursor = database.query(month, coloums,null,null,null,null, null);
		
		ArrayList<String> db_records = new ArrayList<String>();
		
		while(cursor.moveToNext())
		{
			Log.d("DB Values",cursor.getString(0));
			db_records.add(cursor.getString(0));
		}
	
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,db_records);
		
		item_name.setAdapter(adapter);
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("Barcode_Result_Name");
		String price = intent.getStringExtra("Barcode_Result_Price");
		
		if(name!=null)
			item_name.setText(name);
		if(price!=null)
			item_price.setText(price);
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.add_item, menu);
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

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		
		String name = item_name.getText().toString();
		
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		
		float spent = preference.getFloat("budget_spent",0);
		float total = preference.getFloat("budget_total", 0);
		float remaining = total-spent;
		
		if(name == "")
		{
			Toast.makeText(this,"Item Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
			return;
		}
		String price = item_price.getText().toString();
		if(price == "")
		{
			Toast.makeText(this,"Item Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
			return;
		}
		if(Float.parseFloat(price) > remaining)
		{
			Toast.makeText(this, "Price Is Greater Than The Remaining Budget\nIncrease Your Budget", Toast.LENGTH_SHORT).show();
			return;
		}
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
		String date = df.format(c.getTime());
		
		
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		Cursor cursor = database.rawQuery("SELECT * FROM "+month, null);
	    int count = cursor.getCount();
	    cursor.close();
	    
		ContentValues content = new ContentValues();
		
		content.put("_id",count+1);
		content.put("name",name);
		content.put("date", date);
		content.put("price",price);
		
		long id = database.insert(month,null, content);
		database.close();
		
		if(id > 0)
		{
			float item_price = Float.parseFloat(price);
			
			spent = spent + item_price;
			
			editor.putFloat("budget_spent",spent);
			editor.commit();
			
			database = dbobj.getWritableDatabase();
			ContentValues content1 = new ContentValues();
			content1.put("price",spent);
			database.update(month, content1,"_id "+"="+0, null);
			database.close();
			
			Intent intent = new Intent(this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
	        startActivity(intent);
	        finish();
		}

		else
		{
			Toast.makeText(this, "Item Failed To Add Please Try Again",Toast.LENGTH_SHORT).show();
		}
		
	}

}
