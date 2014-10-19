package com.cs442team3.shoppingbudgetmanager;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.widget.RelativeLayout;

public class Activity2 extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2);
		openChart();
	}

	private void openChart(){
		GraphicalView mChartView = null;
		
        String[] code = new String[] {"Budget Used","Budget Left"};

        double[] distribution = { 30,70 } ;

        int[] colors = { Color.BLUE, Color.MAGENTA};

        CategorySeries distributionSeries = new CategorySeries("Budget Chart Of Month October");
        for(int i=0 ;i < distribution.length;i++){
            distributionSeries.add(code[i], distribution[i]);
        }

        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Budget Chart Of Month October");
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);

        mChartView = ChartFactory.getPieChartView(getBaseContext(), distributionSeries , defaultRenderer);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.chartsRelativeLayout);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        mChartView.setLayoutParams(params);
 
        layout.addView(mChartView);
        
        Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");
        startActivity(intent);
    }
}
