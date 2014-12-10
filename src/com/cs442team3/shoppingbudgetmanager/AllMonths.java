package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class AllMonths extends Activity {

    ListView listview;
	DBClass dbobj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_months);
		
		dbobj = new DBClass(this,null);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		listview = (ListView) findViewById(R.id.items_list);
		ArrayList<String> month_list = new ArrayList<String>();
		Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		
		String month ="";
	    if (cursor.moveToPosition(1)) 
	    {
	    	while ( !cursor.isAfterLast() ) {
	    		month = cursor.getString( cursor.getColumnIndex("name"));
    			month_list.add(month);
	            cursor.moveToNext();
	        }
	    }
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,month_list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    
				String month = ((TextView) view).getText().toString();
				
			    Intent intent = new Intent(AllMonths.this,PreviousMonth.class);
			    
			    Bundle b = new Bundle();
    	    	b.putString("month", month);
    	    	intent.putExtras(b);
    	    	
			    startActivity(intent);
			}
		});
	}

}
