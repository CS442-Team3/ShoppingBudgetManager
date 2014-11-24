package com.cs442team3.shoppingbudgetmanager;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DBClass extends SQLiteOpenHelper{

	private static final String DB_NAME = "shoppingbudgetmanager";
	String TABLE_NAME ="";
	private static final String UID = "_id";
	private static final String NAME = "name";
	private static final String DATE ="date";
	private static final String PRICE = "price";
	public static int DB_VERSION = 1;
	private Context context;
	
	public DBClass(Context context, String name) {
		super(context, DB_NAME, null, DB_VERSION);
		if(name != null)
			TABLE_NAME = name;
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		try {
			db.execSQL("CREATE TABLE "+
						TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY,"
						+NAME+" VARCHAR(255),"
						+DATE+" VARCHAR(20),"
						+PRICE+" VARCHAR(30));");
			Toast.makeText(context,"DB Created "+TABLE_NAME, Toast.LENGTH_SHORT).show();
			Log.d("DB","Table "+TABLE_NAME+" Created");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		onCreate(db);
		
	}

}
