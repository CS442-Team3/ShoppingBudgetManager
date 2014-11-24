package com.cs442team3.shoppingbudgetmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
 
public class AllMonths extends Activity {
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DBClass dbobj;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_months);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
  
        dbobj = new DBClass(this,null);
		SQLiteDatabase database = dbobj.getWritableDatabase();
		
		ArrayList<String> month_list = new ArrayList<String>();
		Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		
		int months_count = cursor.getCount()-1;
		
		String month ="";
	    if (cursor.moveToPosition(1)) 
	    {
	    	while ( !cursor.isAfterLast() ) {
	    		month = cursor.getString( cursor.getColumnIndex("name"));
    			month_list.add(month);
	            listDataHeader.add(month);
	            Log.d("Month",month);
	            cursor.moveToNext();
	        }
	    }
       
        
        List<String> temp;
        String[] coloums = {"_id","name","date","price"};
    
        for(int i=0;i<months_count;i++)
        {
        	temp = new ArrayList<String>();
        	cursor = database.query(month_list.get(i), coloums,null,null,null,null, null);
        	
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
    			temp.add(data);
    			data = "";	
    		}
    		listDataChild.put(listDataHeader.get(i), temp);
        }
        
    }
}
