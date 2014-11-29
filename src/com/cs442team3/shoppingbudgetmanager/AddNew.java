package com.cs442team3.shoppingbudgetmanager;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ebay.redlasersdk.RedLaserExtras;
import com.ebay.redlasersdk.BarcodeResult;
import com.ebay.redlasersdk.BarcodeScanActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;

@SuppressLint("SetJavaScriptEnabled") 
public class AddNew extends Activity implements OnClickListener{

	Button btn_barcode,btn_manual;
	WebView webView;
	BarcodeListAdapter listAdapter;
	boolean isLaunchingAnotherActivity = false;
	private static final String SAVED_INSTANCE_LIST = "BarcodeList";
	SharedPreferences preference;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		 webView = (WebView)findViewById(R.id.webView_add_item);
	     webView.addJavascriptInterface(new WebAppInterface(), "Android");
	     webView.getSettings().setJavaScriptEnabled(true); 
	     webView.loadUrl("file:///android_asset/chart.html");
	     
	     btn_manual = (Button) findViewById(R.id.btn_manual);
	     
	     btn_manual.setOnClickListener(this);
	     
			// by default enable all barcode types 
			preference = this.getSharedPreferences(Options.OPTIONS_PREFERENCE, MODE_PRIVATE);
			editor = preference.edit();
			if (!preference.contains(Options.OPTIONS_EAN13))
				editor.putBoolean(Options.OPTIONS_EAN13, true);
			if (!preference.contains(Options.OPTIONS_EAN8))
				editor.putBoolean(Options.OPTIONS_EAN8, true);
			if (!preference.contains(Options.OPTIONS_EAN5))
				editor.putBoolean(Options.OPTIONS_EAN5, true);
			if (!preference.contains(Options.OPTIONS_EAN2))
				editor.putBoolean(Options.OPTIONS_EAN2, true);
			if (!preference.contains(Options.OPTIONS_UPCE))
				editor.putBoolean(Options.OPTIONS_UPCE, true);

			if (!preference.contains(Options.OPTIONS_CODE128))
				editor.putBoolean(Options.OPTIONS_CODE128, true);
			if (!preference.contains(Options.OPTIONS_CODE39))
				editor.putBoolean(Options.OPTIONS_CODE39, true);
			if (!preference.contains(Options.OPTIONS_CODE93))
				editor.putBoolean(Options.OPTIONS_CODE93, true);
			if (!preference.contains(Options.OPTIONS_ITF))
				editor.putBoolean(Options.OPTIONS_ITF, true);

			if (!preference.contains(Options.OPTIONS_CODABAR))
				editor.putBoolean(Options.OPTIONS_CODABAR, true);
			if (!preference.contains(Options.OPTIONS_DATABAR))
				editor.putBoolean(Options.OPTIONS_DATABAR, true);
			if (!preference.contains(Options.OPTIONS_DATABAREXPANDED))
				editor.putBoolean(Options.OPTIONS_DATABAREXPANDED, true);

			if (!preference.contains(Options.OPTIONS_QRCODE))
				editor.putBoolean(Options.OPTIONS_QRCODE, true);
			if (!preference.contains(Options.OPTIONS_DATAMATRIX))
				editor.putBoolean(Options.OPTIONS_DATAMATRIX, true);
			if (!preference.contains(Options.OPTIONS_PDF417))
				editor.putBoolean(Options.OPTIONS_PDF417, true);
			editor.commit();

	}
	
	@Override
	protected void onResume()
	{
		btn_barcode = (Button) findViewById(R.id.btn_barcode);
		btn_barcode.setOnClickListener(this);
		
		super.onResume();
		RedLaserExtras.RLScannerReadyState state = RedLaserExtras.checkReadyStatus(getBaseContext());
		btn_barcode.setEnabled(false);
		
		switch (state)
		{
		case EvalModeReady:
			Log.i("SCANNER STATE", "Evaluation license.");
			btn_barcode.setEnabled(true);
			break;
			
		case LicensedModeReady:
			Log.i("SCANNER STATE", "Fully registered license.");
			btn_barcode.setEnabled(true);
			break;
			
		case BadLicense:
			Log.i("SCANNER STATE", "Bad license. Unable to scan.");
			break;
			
		case NoCamera:
			Log.i("SCANNER STATE", "No camera found.  Unable to scan.");
			break;
			
		case UnsupportedHardware:
			Log.i("SCANNER STATE", "Unsupported hardware.");
			break;

		case ScanLimitReached:
			Log.i("SCANNER STATE", "Scan limit reached. Unable to scan.");
			break;
			
		case APILevelTooLow:			
		case MissingPermissions:
		case UnknownState:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new, menu);
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
	
	
	public class WebAppInterface {

		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		float amount = preference.getFloat("budget_total", 0);
		float spent = preference.getFloat("budget_spent",0);
	     
		@JavascriptInterface
	    public float getNum1() {
	    	return (amount-spent);
	     }
	  

		@JavascriptInterface
	    public float getNum2() {
	    	return spent;
	     }
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_barcode:
			try {
				if (!isLaunchingAnotherActivity)
				{
					isLaunchingAnotherActivity = true;
					Intent scanIntent = new Intent(this,Barcode.class);
					scanIntent.putExtra(Barcode.INTENT_MULTI_SCAN, false);
					startActivityForResult(scanIntent,1);
					Log.d("BARCODE", "Starting Barcode Scan.");
				}
			} catch(Exception e)
			{
				Log.d("BARCODE",e.getLocalizedMessage()+" "+e.getCause());
			}
			break;
			
		case R.id.btn_manual:
			Intent intent1 = new Intent(this,AddItem.class);
            startActivity(intent1);
            finish();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// We came from the scanning activity; the return intent contains a RESULT_EXTRA key
		// whose value is an ArrayList of BarcodeResult objects that we found while scanning.
		// Get the list of objects and add them to our list view.
		
		Log.d("BarcodeResult", "onActivityResult Reached");
		
		if (resultCode == RESULT_OK) 
		{			
			Log.d("BarcodeResult", "Successful");
			Intent intent = new Intent(this,AddItem.class);
			ArrayList<BarcodeResult> barcodes = data.getParcelableArrayListExtra(BarcodeScanActivity.RESULT_EXTRA);
			if (barcodes != null)
			{
				Log.d("BarcodeResult", "Barcode found");
				
				BarcodeResult bcResult = barcodes.get(0);
				if(!bcResult.barcodeString.isEmpty())
				{
					String barcode = bcResult.barcodeString.toString();
					String name = barcode;
					try {
						name = new GetNameFromBarcode().execute(barcode).get();
					} catch (InterruptedException e) {
						name = barcode;
						Log.d("BarcodeResult", e.getLocalizedMessage()+" "+e.getCause());
					} catch (ExecutionException e) {
						name = barcode;
						Log.d("BarcodeResult", e.getLocalizedMessage()+" "+e.getCause());
					}
					
					intent.putExtra("Barcode_Result", name);
					Log.d("BarcodeResult", "String - "+name);			
				}			
				else
					Log.d("BarcodeResult", "String not found");
			}
			Log.d("BarcodeResut", "Starting AddItem activity");
	        startActivity(intent);
	        finish();
		}
	}
	
	private String getNameFromBarcode(String key)
	{
		String name=key;
		Log.d("NameFromBarcode", "Reached getNameFromBarcode with key = "+key);
		
		URL url;
		try {
			url = new URL("http://www.thefind.com/search?query="+key);
			URLConnection conn = url.openConnection();			 
			// open the stream and put it into BufferedReader
			Scanner scanner = new Scanner(conn.getInputStream());
			scanner.useDelimiter("elation.autotrack;");
			/*BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			Log.d("NameFromBarcode", "html received in bufferreader");*/
			String html= scanner.next();
			String line="";
/*			while((line = br.readLine())!=null)
				html.concat(line);*/
			Log.d("NameFromBarcode", "html filled");
			Log.d("NameFromBarcode", html);
			/*Pattern pattern = Pattern.compile("<meta name=\"keywords\" content=\"(.*?)\"",
					Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
			Matcher matcher = pattern.matcher(html);
			
			if(matcher.find())
			{
				Log.d("NameFromBarcode", "meta tag found");
				name = matcher.group(1);
				Log.d("NameFromBarcode", "meta string = " + name);
				String[] names = name.split("\\,");
				name = names[0].replace(" ", "");
				Log.d("NameFromBarcode", "First string extracted = "+name);
			}*/
			if(html!=null)
			{
			name = StringUtils.substringBetween(html, "\"keywords\"", "type=\"application/javascript\">");
			Log.d("NameFromBarcode", "meta tag found");
			Log.d("NameFromBarcode", "meta string = " + name);
			String[] names = name.split("\\,");
			name = names[0].replace("    ", "");
			name = name.substring(10);
			if(name.replace(" ", "")=="")
				name=key;
			Log.d("NameFromBarcode", "First string extracted = "+name);
			}
			
		} catch (MalformedURLException e) {
			Log.d("NameFromBarcode", e.getLocalizedMessage()+" "+e.getCause());
			return name;
		} catch (IOException e) {
			Log.d("NameFromBarcode", e.getLocalizedMessage()+" "+e.getCause());
			return name;
		}
		catch(NullPointerException e)
		{
			Log.d("NameFromBarcode", e.getLocalizedMessage()+" "+e.getCause());
			return key;
		}
		Log.d("NameFromBarcode", "Returning value = "+name);
		return name;
	}
	
	private class GetNameFromBarcode extends AsyncTask<String , Integer, String>
	{	   	      
	     
		@Override
		protected String doInBackground(String... params) {
			return getNameFromBarcode(params[0]);
		}
		
		@Override
	     protected void onPostExecute(String result) {
			Log.d("Async Task", result);
		}


	 }
	
}



