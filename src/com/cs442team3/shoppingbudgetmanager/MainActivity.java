package com.cs442team3.shoppingbudgetmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1 = (Button) findViewById(R.id.button1);
        
        OnClickListener clickListener_1 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_1(); }};

        btn1.setOnClickListener(clickListener_1);
        
        Button btn2 = (Button) findViewById(R.id.button2);
        
        OnClickListener clickListener_2 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_2(); }};

        btn2.setOnClickListener(clickListener_2);
        
        Button btn3 = (Button) findViewById(R.id.button3);
        
        OnClickListener clickListener_3 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_3(); }};

        btn3.setOnClickListener(clickListener_3);
        
        Button btn4 = (Button) findViewById(R.id.button4);
        
        OnClickListener clickListener_4 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_4(); }};

        btn4.setOnClickListener(clickListener_4);
        
        Button btn5 = (Button) findViewById(R.id.button5);
        
        OnClickListener clickListener_5 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_6(); }};

        btn5.setOnClickListener(clickListener_5);
        
Button btn6 = (Button) findViewById(R.id.button6);
        
        OnClickListener clickListener_6 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_7(); }};

        btn6.setOnClickListener(clickListener_6);
        
Button btn7 = (Button) findViewById(R.id.button7);
        
        OnClickListener clickListener_7 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_8(); }};

        btn7.setOnClickListener(clickListener_7);
        
Button btn8 = (Button) findViewById(R.id.button8);
        
        OnClickListener clickListener_8 = new OnClickListener() {
 
            @Override
            public void onClick(View v) {activity_10(); }};

        btn8.setOnClickListener(clickListener_8);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    private void activity_1(){
    	Intent intent = new Intent(this,Activity1.class);
        startActivity(intent);

    }
    
    private void activity_2(){
    	Intent intent = new Intent(this,Activity2.class);
        startActivity(intent);

    }
    
    private void activity_3(){
    	Intent intent = new Intent(this,Activity3.class);
        startActivity(intent);

    }
    
    private void activity_4(){
    	Intent intent = new Intent(this,Activity4.class);
        startActivity(intent);

    }
    
    private void activity_6(){
    	Intent intent = new Intent(this,Activity6.class);
        startActivity(intent);

    }
    
    private void activity_7(){
    	Intent intent = new Intent(this,BudgetEdit_Activity.class);
    	startActivity(intent);
    }
    
    private void activity_8(){
    	Intent intent = new Intent(this,Details_Fragment.class);
    	startActivity(intent);
    }
    
    private void activity_10(){
    	Intent intent = new Intent(this,MonthlyMain_Activity.class);
    	startActivity(intent);
    }

}
