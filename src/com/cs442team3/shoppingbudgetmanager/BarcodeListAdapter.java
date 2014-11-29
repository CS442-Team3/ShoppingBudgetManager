/**
 * 
 */
package com.cs442team3.shoppingbudgetmanager;

/**
 * @author KaranJeet
 *
 */
import java.util.AbstractCollection;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebay.redlasersdk.BarcodeResult;

public class BarcodeListAdapter extends BaseAdapter 
{
	private final Context context;
	private final ArrayList<BarcodeResult> values;

	public BarcodeListAdapter(Context context, ArrayList<BarcodeResult> initialValues) 
	{
		super();
		this.context = context;
		if (initialValues != null)
		{
			values = new ArrayList<BarcodeResult>(initialValues);
		} else
		{
			values = new ArrayList<BarcodeResult>();
		}
	}

	
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.barcodelistlayout, parent, false);	
		
		// display last scanned code on top of list
//		BarcodeResult cellBacking = values.get(values.size() - position - 1);
		BarcodeResult cellBacking = values.get(position);
		
		TextView textView = (TextView) rowView.findViewById(R.id.barcodeText);
		BarcodeResult associatedBarcode = cellBacking.getAssociatedBarcode(values);
		if(associatedBarcode != null)
		{
			textView.setText(cellBacking.barcodeString + "->" + associatedBarcode.barcodeString);
		}
		else
		{
			textView.setText(cellBacking.barcodeString);
		}
		
		TextView typeView = (TextView) rowView.findViewById(R.id.barcodeType);
		typeView.setText(cellBacking.getBarcodeType());

		return rowView;
	}
	
	public void addBarcodes(AbstractCollection<BarcodeResult> results)
	{
		values.addAll(0, results);
		this.notifyDataSetChanged();
	}
	
	
	public void clearList()
	{
		values.clear();
		this.notifyDataSetChanged();
	}
	
	
	public int getCount()
	{
		return values.size();
	}
	
	public BarcodeResult getItem(int position)
	{
		return values.get(position);
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public ArrayList<BarcodeResult> getResultList()
	{
		return values;
	}

}
