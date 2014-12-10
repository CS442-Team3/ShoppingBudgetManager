package com.cs442team3.shoppingbudgetmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.ebay.redlasersdk.BarcodeResult;
import com.ebay.redlasersdk.BarcodeScanActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Barcode extends BarcodeScanActivity {
	
	public static final String INTENT_MULTI_SCAN = "intent_multi_scan";
	int mCurrentApiVersion = android.os.Build.VERSION.SDK_INT;    
	
	// The viewfinderView is the rectangle in the center showing the user where to
		// place the barcode. FoundText shows how many barcodes have been found. hintText
		// is used to guide the user through multi-step decode processes.
		View viewfinderView = null;
		Button doneButton;
		Button requestImageButton;
		Button toggleTorchButton;
		TextView hintTextView;
		TextView foundTextView;
		
		private boolean isMultiScan = false;
		private boolean isInRange = false;

		Display mDisplay;
		String mRequestedOrientation;
	
	SharedPreferences preference;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		preference = this.getSharedPreferences(Options.OPTIONS_PREFERENCE, MODE_PRIVATE);
    	if (!preference.getBoolean(Options.OPTIONS_ORIENTATION, false))
    	{
    		mRequestedOrientation = BarcodeScanActivity.PREF_ORIENTATION_PORTRAIT;
    	} else
    	{
    		mRequestedOrientation = BarcodeScanActivity.PREF_ORIENTATION_LANDSCAPE;
    	}

		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.activity_barcode);
		startActivityForResult(this.getIntent(), EAN13);
		*/
		
		if(preference.getBoolean("isBarcode", true))
		{
		
		// Here we can set what types of barcode to scan
        enabledTypes.setEan13(preference.getBoolean(Options.OPTIONS_EAN13, false));
        enabledTypes.setEan8(preference.getBoolean(Options.OPTIONS_EAN8, false));
        enabledTypes.setEan5(preference.getBoolean(Options.OPTIONS_EAN5, false));
        enabledTypes.setEan2(preference.getBoolean(Options.OPTIONS_EAN2, false));
        enabledTypes.setUpce(preference.getBoolean(Options.OPTIONS_UPCE, true));
        enabledTypes.setCode128(preference.getBoolean(Options.OPTIONS_CODE128, false));
        enabledTypes.setCode39(preference.getBoolean(Options.OPTIONS_CODE39, false));
        enabledTypes.setCode93(preference.getBoolean(Options.OPTIONS_CODE93, false));
        enabledTypes.setITF(preference.getBoolean(Options.OPTIONS_ITF, false));
        enabledTypes.setCodabar(preference.getBoolean(Options.OPTIONS_CODABAR, false)); 
        enabledTypes.setGS1Databar(preference.getBoolean(Options.OPTIONS_DATABAR, false)); 
        enabledTypes.setGS1DatabarExpanded(preference.getBoolean(Options.OPTIONS_DATABAREXPANDED, false)); 
        enabledTypes.setQRCode(preference.getBoolean(Options.OPTIONS_QRCODE, false));
        enabledTypes.setDataMatrix(preference.getBoolean(Options.OPTIONS_DATAMATRIX, false));
        enabledTypes.setPDF417(preference.getBoolean(Options.OPTIONS_PDF417, false));
        
        // To Remove the Title bar from RedLaserSDK Activity, add the NoTitleBar Theme in your Android Manifest.
        // Remove the Status Bar from this window.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        

        // use different layout depending on whether phone is pre-2.2
        // pre-2.2 devices can only use the camera in landscape and must use rotated components 
        // to make the scan screen appear in portrait
    	if (mRequestedOrientation.equals(BarcodeScanActivity.PREF_ORIENTATION_PORTRAIT))
    	{
            if (mCurrentApiVersion >= 8)
            {
        		setContentView(R.layout.activity_barcode_new_portrait);
            } else
            {
            	setContentView(R.layout.activity_barcode_old_portrait);
            }
    	} else
    	{
    		setContentView(R.layout.activity_barcode_landscape);
    	}

        
    	// Find and position the viewfinder view
        viewfinderView = findViewById(R.id.view_finder);
        if (mCurrentApiVersion >= 8)
        {
        	LayoutParams params = new LayoutParams((int)(mDisplay.getWidth() * .75f), 
        			(int)(mDisplay.getHeight() * .33f));
        	params.gravity = Gravity.CENTER;
        	viewfinderView.setLayoutParams(params);
        }

        
        doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		doneScanning();
			}
        });

        
        requestImageButton = (Button) findViewById(R.id.button_request_image);
        requestImageButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				requestImageData();
			}
        });

        
        toggleTorchButton = (Button) findViewById(R.id.button_toggle_torch);
        toggleTorchButton.setOnClickListener(new OnClickListener()
        {

			public void onClick(View v) 
			{
				boolean torch = getTorch();
				setTorch(!torch);
			}
        });
        
        hintTextView = (TextView) findViewById(R.id.hint_text);
        foundTextView = (TextView) findViewById(R.id.num_found_text);
		}
	}
	
	/** Because the camera gets initialized in super.onResume(), we can't find out
	 *  whether the torch is available until after super.onResume() completes.
	 *  So, we enable or disable the 'Light' button here instead of in onCreate().
	 */
	protected void onResume()
	{
		super.onResume();
		
		toggleTorchButton.setEnabled(isTorchAvailable());
	}

	
	/** Camera error callback. Implement this method to handle any camera 
     *  errors thrown by Android OS.
     */
	@Override
	protected void onError(int error) 
	{
		Log.d("Barcode scanner activity", "Received an error from the Camera.");	
	}

	
	/** Called by the app repeatedly while scanning is happening. This method 
	 *  override is how app can find out about the status of the scanning
	 *  session in real time.
	 */

	@SuppressWarnings("deprecation")
	@Override
	protected void onScanStatusUpdate(Map<String, Object> scanStatus) {
		String foundText = "0 Barcodes Found";
		
		// Retrieve the set of found barcodes from the HashMap
	    @SuppressWarnings(value = "unchecked")
		Set<BarcodeResult> allResults = (Set<BarcodeResult>) scanStatus.get(Status.STATUS_FOUND_BARCODES);
		
	    if (allResults != null && allResults.size() > 0)
		{
			// Show how many barcodes we've found
			foundText = allResults.size() + " Barcodes Found";

			if (!isMultiScan)
			{
				// If we're in single scan mode and we've found something, we're done.
				doneScanning();
			}
		}
	    
	 // change color of viewfinder box if in-range status changes
	 		Boolean inRangeStatus = (Boolean) scanStatus.get(Status.STATUS_IN_RANGE);
	 		if (inRangeStatus != null)
	 		{
	 			if (isInRange != inRangeStatus.booleanValue())
	 			{
	 				isInRange = inRangeStatus.booleanValue();
	 				if (isInRange)
	 				{
	 					viewfinderView.setBackgroundDrawable(getResources().getDrawable(R.drawable.viewfinder_green));
	 				} else
	 				{
	 					viewfinderView.setBackgroundDrawable(getResources().getDrawable(R.drawable.viewfinder_white));
	 				}
	 			}
	 		}

	 		foundTextView.setText(foundText);
	 		
	 	// Guidance. This is for recognizing long barcodes in parts by swiping the device across
			// the barcode. See the documentaiton for more details.
			Integer guidanceLevel = (Integer) scanStatus.get(Status.STATUS_GUIDANCE);
			if (guidanceLevel == null)
			{
				hintTextView.setText("");
			} else
			{
				if (guidanceLevel.intValue() == 1)
				{
					hintTextView.setText(getResources().getText(R.string.guidance_string));
				} else if (guidanceLevel.intValue() == 2)
				{
					BarcodeResult partialBarcode = (BarcodeResult) scanStatus.get(Status.STATUS_PARTIAL_BARCODE);
					String partialString = partialBarcode.barcodeString;
					hintTextView.setText(partialString);
				}
			} 

			
			// This status string will only appear in response to a call to requestImageData().
			if (scanStatus.get(Status.STATUS_CAMERA_SNAPSHOT) != null)
			{
				// imageData will always be a JPEG image passed in a byte array. Depending on 
				// device hardware and API level, the image may be a preview frame that has
				// been JPEG compressed, or it may be a JPEG taken from the Camera's picture-taking
				// apparatus.
				byte[] imageData = (byte[]) scanStatus.get(Status.STATUS_CAMERA_SNAPSHOT);
				
				// Write the image data out to a file. Use a command like:
				//     adb pull /mnt/sdcard/Android/data/com.ebay.rlsample/files/PreviewCapture.jpg
				// to look at what gets captured.
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED))
				{
					FileOutputStream output = null;
					try {
						// Since this is a demo, just keep overwriting this one file
						File imageDir = new File(Environment.getExternalStorageDirectory(), 
								"Android/data/com.ebay.rlsample/files");
						imageDir.mkdirs();
						File imageFile = new File(imageDir, "PreviewCapture.jpg");
						output = new FileOutputStream(imageFile, false);
						output.write(imageData, 0, imageData.length);
						output.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}		
	}
	
	/** BarcodeScanActivity queries this method during onCreate() to determine which
	 *  orientation it should use.
	 */
	@Override
	public String getOrientationSetting() 
	{
		if (!preference.getBoolean(Options.OPTIONS_ORIENTATION, false))
		{
			return PREF_ORIENTATION_PORTRAIT;
		}
		else
		{
			return PREF_ORIENTATION_LANDSCAPE;
		}
	}    
}
