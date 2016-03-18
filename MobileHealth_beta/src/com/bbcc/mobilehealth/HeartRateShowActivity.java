package com.bbcc.mobilehealth;

import java.util.Calendar;
import java.util.Date;

import com.bbcc.mobilehealth.view.HeartRateView;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;

public class HeartRateShowActivity extends Activity implements OnClickListener{
	
	HeartRateView heartRateView;
	ImageView textView;
	CalendarView calendarView;
	ImageView imageView;
	TextView low,high;
	private int[] maxData = new int[]{60,70,75,80,85,85,90,122,130,125,
										100,95,130,90,85,86,88,80,75,
										85,84,83,84,75};
	private int[] minData = new int[] {
			35,45,55,56,32,21,20,15,18,25,
			24,55,53,52,56,64,68,69,70,
			64,55,56,45,63
			};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate_show);
		
		
		initView();
		initData();
	}
	
	private void initView () {
		Log.d("initview", "initview");
		heartRateView = (HeartRateView)findViewById(R.id.myview);
		textView = (ImageView)findViewById(R.id.bt_right);
		imageView = (ImageView)findViewById(R.id.bt_left);
		low = (TextView)findViewById(R.id.low);
		high = (TextView)findViewById(R.id.high);
		
		
		textView.setOnClickListener(this);
		imageView.setOnClickListener(this);
	}
	
	private void initData() {
		heartRateView.initData(maxData,minData);
		low.setText("最低心率: "+getMin(minData));
		high.setText("最高心率: "+getMax(maxData));
	}
	
	@Override
	protected void onPause() {

		Log.d("showpause", "ada");
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Log.d("showresume", "showresume");
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_right:
			View view = getLayoutInflater().inflate(R.layout.calendar_dialog, null);
			calendarView = (CalendarView)view.findViewById(R.id.calendar_view);
			final AlertDialog dlg = new AlertDialog.Builder(HeartRateShowActivity.this).setTitle("选择时间").setView(view)
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					                    public void onClick(DialogInterface dialog, int whichButton) {  
					                        
					                    	//edit_birthday_textview.setText(dateTime); 
					                    }  
					                })  
					                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
					                    public void onClick(DialogInterface dialog, int whichButton) {  
					                          //edit_birthday_textview.setText("");
					                    }  
					                }).show();;
			
			Calendar month = Calendar.getInstance();
			month.add(Calendar.MONTH,-1);
			
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.MONTH, 1);
			
			Calendar today = Calendar.getInstance();
			today.add(Calendar.DAY_OF_YEAR, 0);
			calendarView.setMinDate(month.getTimeInMillis());
			
			calendarView.setDate(new Date().getTime());
			calendarView.setFocusable(true);
			
			calendarView.setMaxDate(tomorrow.getTimeInMillis());
			//calendarView.set
			Log.d("tag", today.getTimeInMillis()+" ");
			dlg.show();
			calendarView.setOnDateChangeListener(new OnDateChangeListener() {
				
				@Override
				public void onSelectedDayChange(CalendarView arg0, int arg1, int arg2,
						int arg3) {
					
				}
			});
			break;
		case R.id.bt_left:
			HeartRateShowActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	private int getMax(int[] a) {
		int max = 0;
		for (int i = 0;i < a.length;i++) {
			if (a[i] > max) max = a[i];
		}
		
		return max;
	}
	
	private int getMin(int[] a) {
		int min=a[0];
		for (int i = 0;i < a.length;i++) {
			if (a[i] < min) min = a[i];
		}
		return min;
	}
}
