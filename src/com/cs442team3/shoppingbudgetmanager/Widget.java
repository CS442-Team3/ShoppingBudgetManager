package com.cs442team3.shoppingbudgetmanager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) 
  {

    ComponentName thisWidget = new ComponentName(context, Widget.class);
    
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    
    for (int widgetId : allWidgetIds) 
    {

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

      
      SharedPreferences preference = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
      
      float spent = preference.getFloat("budget_spent",0);
      float budget = preference.getFloat("budget_total", 0); 
      
      remoteViews.setTextViewText(R.id.widget_spent,"Budget Spent : $"+String.format("%.2f", spent));
      remoteViews.setTextViewText(R.id.widget_budget, "Budget Total : $"+String.format("%.2f", budget));
      remoteViews.setTextViewText(R.id.widget_balance, "Budget Balance : $"+String.format("%.2f", (budget-spent)));
      Intent intent = new Intent(context, Widget.class);

      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
  }
  
  @Override
	public void onEnabled(Context context) {
		Log.v("onEnabled", "onEnabled called");
		super.onEnabled(context);
	}
} 