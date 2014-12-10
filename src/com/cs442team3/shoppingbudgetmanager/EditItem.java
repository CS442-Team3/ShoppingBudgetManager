/**
 * 
 */
package com.cs442team3.shoppingbudgetmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.Toast;

/**
 * @author KaranJeet
 *
 */
public class EditItem extends Activity {
	Button edit_item;
	Button delete_item;
	EditText item_name;
	EditText item_price;
	DBClass dbobj;
	String old_name = "";
	String old_price = "";
	int id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		edit_item = (Button) findViewById(R.id.btn_edit_item);
		//edit_item.setOnClickListener(this);
		delete_item = (Button) findViewById(R.id.btn_delete_item);
		//delete_item.setOnClickListener(this);
		
		item_name = (EditText) findViewById(R.id.item_name);
		item_price = (EditText) findViewById(R.id.item_price);
		
		Intent intent = getIntent();
		old_name = intent.getStringExtra("Edit_Name");
		old_price = intent.getStringExtra("Edit_Price");
		id = intent.getIntExtra("Edit_Id", 0);
		if(old_name!=null)
			item_name.setText(old_name);
		if(old_price!=null)
			item_price.setText(old_price);
		
		edit_item.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				String name = item_name.getText().toString();
				//Intent intent = getIntent();
				SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				
				float spent = preference.getFloat("budget_spent",0);
				float total = preference.getFloat("budget_total", 0);
				float remaining = total-spent;

				Log.v("Remaining", ""+remaining);
				remaining+=Float.parseFloat(old_price);
				
				Log.v("old_price", old_price);
				Log.v("Remaining", ""+remaining);
				
				if(name == "")
				{
					Toast.makeText(getBaseContext(),"Item Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
					return;
				}
				String price = item_price.getText().toString();
				if(price == "")
				{
					Toast.makeText(getBaseContext(),"Item Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
					return;
				}
				if(Float.parseFloat(price) > remaining)
				{
					Toast.makeText(getBaseContext(), "Price Is Greater Than The Remaining Budget\nIncrease Your Budget", Toast.LENGTH_SHORT).show();
					return;
				}
				Calendar c = Calendar.getInstance();
				String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
				String date = df.format(c.getTime());
				
				
				dbobj = new DBClass(getBaseContext(),month);
				SQLiteDatabase database = dbobj.getWritableDatabase();
				
				//Cursor cursor = database.rawQuery("SELECT * FROM "+month, null);
			    //int count = cursor.getCount();
			    //cursor.close();
			    
				ContentValues content = new ContentValues();
				
				//int id = intent.getIntExtra("Edit_Id", 0);
				//content.put("_id",id);
				content.put("name",name);
				content.put("date", date);
				content.put("price",price);
				Log.v("Edit_Item_id", ""+id);
				long isUpdate=0;
				if(id>0)
					isUpdate = database.update(month, content, "_id="+id, null);
				Log.v("Edit_Item_isUpdate", ""+isUpdate);
				database.close();
				
				if(isUpdate > 0)
				{
					float item_price = Float.parseFloat(price);
					float old_price_f = Float.parseFloat(old_price);
					spent = spent + (item_price-old_price_f);
					
					editor.putFloat("budget_spent",spent);
					editor.commit();
					
					database = dbobj.getWritableDatabase();
					ContentValues content1 = new ContentValues();
					content1.put("price",spent);
					database.update(month, content1,"_id "+"="+0, null);
					database.close();
					
					Intent intent1 = new Intent(getBaseContext(),MainActivity.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent1.putExtra("EXIT", true);
					if(intent1.hasExtra("Edit_Id"))
					{
						intent1.removeExtra("Edit_Id");
			        	intent1.removeExtra("Edit_Name");
			        	intent1.removeExtra("Edit_Price");
					}
					startActivity(intent1);
			        
			        
			        finish();
				}

				else
				{
					Toast.makeText(getBaseContext(), "Item Failed To Add Please Try Again",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		delete_item.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				float spent = preference.getFloat("budget_spent", 0);
				spent -= Float.parseFloat(old_price);
		    	editor.putFloat("budget_spent",spent);
		    	editor.commit();
		    	
		    	Calendar c = Calendar.getInstance();
				String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
			
				dbobj = new DBClass(getBaseContext(),month);
				SQLiteDatabase database = dbobj.getWritableDatabase();
				database.execSQL("delete from "+ month+" where _id = "+id);
				
				ContentValues content1 = new ContentValues();
				content1.put("price",spent);
				database.update(month, content1,"_id "+"="+0, null);
				
				database.close();
				
				Intent intent1 = new Intent(getBaseContext(),MainActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent1.putExtra("EXIT", true);
				if(intent1.hasExtra("Edit_Id"))
				{
					intent1.removeExtra("Edit_Id");
		        	intent1.removeExtra("Edit_Name");
		        	intent1.removeExtra("Edit_Price");
				}
				startActivity(intent1);
		        
		        
		        finish();
			}
		});
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.add_item, menu);
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
*/
	
	
	@Override
	public void onBackPressed() {
		Log.d("EditItem","Back Pressed");
		finish();
		startActivity(new Intent(getBaseContext(),EditMonth.class));
		this.finish();
		super.onBackPressed();
	}
}
