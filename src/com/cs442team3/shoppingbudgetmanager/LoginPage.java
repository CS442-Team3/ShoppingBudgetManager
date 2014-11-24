package com.cs442team3.shoppingbudgetmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity implements OnClickListener{

	Button conform;
	EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
		
		conform = (Button) findViewById(R.id.button1);
		text = (EditText) findViewById(R.id.editText1);
		
		conform.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String user_name = text.getText().toString();
		SharedPreferences preference = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
    	editor.putString("name",user_name);
    	editor.commit();
    	Toast.makeText(this,"User "+user_name+" Added",Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this,AddMonth.class);
    	
    	Bundle b = new Bundle();
    	b.putBoolean("status_spent", false);
    	intent.putExtras(b);
    	
        startActivity(intent);
        finish();
	}

}
