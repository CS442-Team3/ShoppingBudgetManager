/**
 * 
 */
package com.cs442team3.shoppingbudgetmanager;

/**
 * @author KaranJeet
 *
 */
import com.ebay.redlasersdk.RedLaserExtras;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;


public class Options extends Activity
{

	public static final String OPTIONS_EAN13 = "optionsEan13";
	public static final String OPTIONS_EAN8 = "optionsEan8";
	public static final String OPTIONS_EAN5 = "optionsEan5";
	public static final String OPTIONS_EAN2 = "optionsEan2";
	public static final String OPTIONS_UPCE = "optionsUpce";

	public static final String OPTIONS_CODE128 = "optionsCode128";
	public static final String OPTIONS_CODE39 = "optionsCode39";
	public static final String OPTIONS_CODE93 = "optionsCode93";
	public static final String OPTIONS_ITF = "optionsItf";

	public static final String OPTIONS_CODABAR = "optionsCodabar";
	public static final String OPTIONS_DATABAR = "optionsDatabar";
	public static final String OPTIONS_DATABAREXPANDED = "optionsDatabarExpanded";

	public static final String OPTIONS_QRCODE = "optionsQRCode";
	public static final String OPTIONS_DATAMATRIX = "optionsDataMatrix";
	public static final String OPTIONS_PDF417 = "optionsPDF417";
	
	public static final String OPTIONS_ORIENTATION = "optionsOrientation";
	
	public static final String OPTIONS_PREFERENCE = "scannerOptionsPrefs";
	
	SharedPreferences prefs;
	SharedPreferences.Editor prefsEditor;
	
	ToggleButton toggleEAN13;
	ToggleButton toggleEAN8;
	ToggleButton toggleEAN5;
	ToggleButton toggleEAN2;
	ToggleButton toggleUPCE;

	ToggleButton toggle128;
	ToggleButton toggle39;
	ToggleButton toggle93;
	ToggleButton toggleITF;
	
	ToggleButton toggleCodabar;
	ToggleButton toggleDatabar;
	ToggleButton toggleDatabarExpanded;

	ToggleButton toggleQR;
	ToggleButton toggleDataMatrix;
	ToggleButton togglePDF417;
	
	ToggleButton toggleOrientation;
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_view);
		
		prefs = this.getSharedPreferences(OPTIONS_PREFERENCE, MODE_PRIVATE);
		prefsEditor = prefs.edit();
		
		setupToggleButton(toggleEAN13, R.id.toggleEAN13, OPTIONS_EAN13);
		setupToggleButton(toggleEAN8, R.id.toggleEAN8, OPTIONS_EAN8);
		setupToggleButton(toggleEAN5, R.id.toggleEAN5, OPTIONS_EAN5);
		setupToggleButton(toggleEAN2, R.id.toggleEAN2, OPTIONS_EAN2);
		setupToggleButton(toggleUPCE, R.id.toggleUPCE, OPTIONS_UPCE);
		
		setupToggleButton(toggle128, R.id.toggle128, OPTIONS_CODE128);
		setupToggleButton(toggle39, R.id.toggle39, OPTIONS_CODE39);
		setupToggleButton(toggle93, R.id.toggle93, OPTIONS_CODE93);
		setupToggleButton(toggleITF, R.id.toggleITF, OPTIONS_ITF);
		
		setupToggleButton(toggleCodabar, R.id.toggleCodabar, OPTIONS_CODABAR);
		setupToggleButton(toggleDatabar, R.id.toggleDatabar, OPTIONS_DATABAR);
		setupToggleButton(toggleDatabarExpanded, R.id.toggleDatabarExpanded, OPTIONS_DATABAREXPANDED);

		setupToggleButton(toggleQR, R.id.toggleQR, OPTIONS_QRCODE);
		setupToggleButton(toggleDataMatrix, R.id.toggleDataMatrix, OPTIONS_DATAMATRIX);
		setupToggleButton(togglePDF417, R.id.togglePDF417, OPTIONS_PDF417);

		setupToggleButton(toggleOrientation, R.id.toggleOrientation, OPTIONS_ORIENTATION);
		
		TextView versionTextView = (TextView) findViewById(R.id.SDK_Version_TextField);
		versionTextView.setText("RL_SDK Version " + RedLaserExtras.getRedLaserSDKVersion());
    }
	
	protected void setupToggleButton(ToggleButton button, int viewID, final String optionsID)
	{
		final ToggleButton finalButton = (ToggleButton) findViewById(viewID);
		button = finalButton;
		finalButton.setChecked(prefs.getBoolean(optionsID, false));
		finalButton.setOnClickListener(new OnClickListener() 
		{
		    public void onClick(View v) 
		    {
		        // Perform action on clicks
		        if (finalButton.isChecked()) 
		        {
		        	prefsEditor.putBoolean(optionsID, true);
		        } else 
		        {
		        	prefsEditor.putBoolean(optionsID, false);
		        }
		    }
		});

	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		prefsEditor.commit();
	}
	
	
}
