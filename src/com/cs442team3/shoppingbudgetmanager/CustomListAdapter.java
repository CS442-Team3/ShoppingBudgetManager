/**
 * 
 */
package com.cs442team3.shoppingbudgetmanager;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author KaranJeet
 *
 */
@SuppressLint("InflateParams") 
public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflator;
	private List<Item> items;
	
	public CustomListAdapter(Activity activity,List<Item> items)
	{
		this.activity=activity;
		this.items=items;
	}
	
	@Override
	public int getCount()
	{
		return items.size();
	}
	
	@Override
	public Object getItem(int location)
	{
		return items.get(location);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(inflator == null)
			inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
			convertView=inflator.inflate(R.layout.list_row, null);
		TextView id_tv = (TextView) convertView.findViewById(R.id.id);
		TextView name_tv = (TextView) convertView.findViewById(R.id.name);
		TextView date_tv = (TextView) convertView.findViewById(R.id.date);
		TextView price_tv = (TextView) convertView.findViewById(R.id.price);
		
		Item i = items.get(position);
		if(i.getId()!=0)
		{
			id_tv.setText(String.valueOf(i.getId()-1));
			name_tv.setText(i.getName());
			date_tv.setText(i.getDate());
			price_tv.setText("$"+i.getPrice());
		}
		else
		{
			id_tv.setText("");
			name_tv.setText("Total spent");
			date_tv.setText("");
			price_tv.setText("$"+i.getPrice());
		}
		return convertView;
	}
}
