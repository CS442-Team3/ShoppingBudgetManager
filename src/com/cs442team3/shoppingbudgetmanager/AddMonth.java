package com.cs442team3.shoppingbudgetmanager;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMonth extends Activity implements OnClickListener{

	Button btn_confirm;
	DBClass dbobj;
	Boolean spent_state = false;
	Boolean new_month = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_month);
		
		Bundle b = getIntent().getExtras();
		spent_state = b.getBoolean("status_spent", false);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.d("Onclick", "Inside");
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		
		dbobj = new DBClass(this, month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		database.close();
		
		EditText edit = (EditText) findViewById(R.id.editText_month_amount);
		String temp = edit.getText().toString();
		if(temp == "")
		{
			Toast.makeText(this,"Budget Cant Be Empty",Toast.LENGTH_SHORT).show();
			return;
		}
		float amount = Float.parseFloat(temp);
		
		Float spent = preference.getFloat("budget_spent", 0);
		if(amount < spent)
		{
			Toast.makeText(this, "Insufficient Budget Provided", Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferences.Editor editor = preference.edit();
		editor.putString("month",month);
    	editor.putFloat("budget_total",amount);
   
		
		dbobj = new DBClass(this,month);
		database = dbobj.getWritableDatabase();
		
		ContentValues content = new ContentValues();
		content.put("name",amount);
		
    	if(!spent_state)
    	{
    		editor.putString("db_version", 1+"");
    		editor.putFloat("budget_spent",0);
    		
    		content.put("_id",0);
    		content.put("date", 0);
    		content.put("price",0);
    		database.insert(month,null, content);
    	}
    	else
    	{
    		database.update(month, content,"_id "+"="+0, null);
    	}
    	editor.commit();
    		
    	Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

	}
}
