package com.cs442team3.shoppingbudgetmanager;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddMonth extends Activity implements OnClickListener{

	Button btn_confirm;
	DBClass dbobj;
	Boolean spent_state = false;
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_month, menu);
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
		Log.d("Onclick", "Inside");
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		
		dbobj = new DBClass(this,month);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		database.close();
		Log.d("Onclick", "Done DB");
		EditText edit = (EditText) findViewById(R.id.editText_month_amount);
		String temp = edit.getText().toString();
		float amount = Float.parseFloat(temp);
		
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor.putString("month",month);
    	editor.putFloat("budget_total",amount);
    	if(!spent_state)
    		editor.putFloat("budget_spent",0);
    	editor.commit();
    	
    	Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
	}
}
