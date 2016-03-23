package com.bbcc.mobilehealth;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.bbcc.mobilehealth.view.PerWeekStepsChart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ActionHistoryActivity extends Activity{
	private PerWeekStepsChart perWeekStepsChart=null;
	private TextView stepTextView=null;
	private TextView calorieTextView=null;
	private TextView kilometerTextView=null;
	private TextView consumedTextView=null;
	private int steps=0;
	private SharedPreferences argsSPF;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_actionhistory);
		argsSPF=getSharedPreferences("step_data", MODE_PRIVATE);
		editor=argsSPF.edit();
		perWeekStepsChart=(PerWeekStepsChart) findViewById(R.id.perWeekStepsChart);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		String editorKey=String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
		perWeekStepsChart.setPerWeekSteps(4900, 2000, 18290, 6172, argsSPF.getInt(editorKey, 0), 0, 0);
		steps=perWeekStepsChart.getThurSteps();
		stepTextView=(TextView) findViewById(R.id.act_action_histery_step);
		calorieTextView=(TextView) findViewById(R.id.act_action_histery_calorie);
		kilometerTextView=(TextView) findViewById(R.id.act_action_histery_kilometer);
		consumedTextView=(TextView) findViewById(R.id.act_action_histery_consumed);
		calorieTextView.setText(calCalorie()+"");
		stepTextView.setText(steps+"");
		kilometerTextView.setText(calKilometer()+"");
	}
	private String calKilometer(){
		float f=(float) (steps*0.55/1000);
		DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(f);//format 返回的是字符串
		return p;
	}
	private int calCalorie(){
		return  (int) (((-55.0969+(0.6309*80)+(0.1988*65)+(0.2017*22))/4.184)*60*1);
	}
}
