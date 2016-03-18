package com.bbcc.mobilehealth;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.util.DatabaseHelper;
import com.bbcc.mobilehealth.util.MyUser;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public class StatisticsActivity extends Activity implements OnDateChangeListener {

	private CalendarView staCalendar;
	 
	private TextView  sta_stepCount;
	private TextView  sta_sitCount;
	private TextView  sta_walkCount;
	private TextView  sta_runCount;
	private TextView sta_upCount;
	private TextView sta_downCount;
	private ImageButton sta_back;
	
	private PieChart pieChar;
	private DatabaseHelper service;
	private String userId;
	
//	private TableRow t1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.statistics);
		super.onCreate(savedInstanceState);
		sta_stepCount=(TextView) findViewById(R.id.sta_stepCount);
		staCalendar=(CalendarView) findViewById(R.id.sta_calendarView);
		sta_sitCount=(TextView) findViewById(R.id.sta_sitCount);
		sta_walkCount=(TextView) findViewById(R.id.sta_walkCount);
		sta_runCount=(TextView) findViewById(R.id.sta_runCount);
		sta_upCount=(TextView) findViewById(R.id.sta_upCount);
		sta_downCount=(TextView) findViewById(R.id.sta_downCount);
		sta_back=(ImageButton) findViewById(R.id.sta_back);
		sta_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		pieChar=(PieChart) findViewById(R.id.spread_pie_chart);
		service=new DatabaseHelper(this);
		userId=BmobUser.getCurrentUser(this,MyUser.class).getObjectId();
		staCalendar.setOnDateChangeListener(this);
		
	}
	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int day) {
		// TODO Auto-generated method stub
		int realMonth=month+1;
		String selectedDateStr=year+"-"+realMonth+"-"+day;
		System.out.println(selectedDateStr);
		
		int[] getItems =service.getSelDateSta(userId,selectedDateStr);
		int todayStep=getItems[0];
		int sta_sitCountTime=getItems[1];
		int sta_walkCountTime=getItems[2];
		int sta_runCountTime=getItems[3];
		int sta_upCountTime=getItems[4];
		int sta_downCountTime=getItems[5];
		int allTime=sta_sitCountTime+sta_walkCountTime+sta_runCountTime+sta_upCountTime+sta_downCountTime;
		sta_stepCount.setText(""+todayStep+"��");
		sta_sitCount.setText(""+getStaTime(sta_sitCountTime));
		sta_walkCount.setText(""+getStaTime(sta_walkCountTime));
		sta_runCount.setText(""+getStaTime(sta_runCountTime));
		sta_upCount.setText(""+getStaTime(sta_upCountTime));
		sta_downCount.setText(""+getStaTime(sta_downCountTime));
		PieData mPieData = getPieData(5,allTime,sta_sitCountTime,sta_walkCountTime,sta_runCountTime,sta_upCountTime,sta_downCountTime);    
		initPieChar(pieChar, mPieData,selectedDateStr,todayStep);
	
	}
	
	public String getStaTime(int secAll)
	{
		String timeStr="";
		int hours=secAll/3600;
		int hoursMod=secAll%3600;
		int minute=hoursMod/60;
		int second=hoursMod%60;
		timeStr=hours+"Сʱ"+minute+"����"+second+"��";
		return timeStr;
		
	}
	//��ʼ����״ͼ
	public void initPieChar(PieChart pieChart, PieData pieData, String selectedDateStr, int todayStep)
	{
		    pieChar.setHoleColorTransparent(true);    
		    
	        pieChar.setHoleRadius(60f);  //�뾶    
	        pieChar.setTransparentCircleRadius(64f); // ��͸��Ȧ    
	        //pieChart.setHoleRadius(0)  //ʵ��Բ    
	    
	        pieChar.setDescription(selectedDateStr+" ״̬ʱ��ͳ��");    
	    
	        // mChart.setDrawYValues(true);    
	        pieChar.setDrawCenterText(true);  //��״ͼ�м�����������    
	    
	        pieChar.setDrawHoleEnabled(true);    
	    
	        pieChar.setRotationAngle(90); // ��ʼ��ת�Ƕ�    
	    
	        // draws the corresponding description value into the slice    
	        // mChart.setDrawXValues(true);    
	    
	        // enable rotation of the chart by touch    
	        pieChar.setRotationEnabled(true); // �����ֶ���ת    
	    
	        // display percentage values    
	        pieChar.setUsePercentValues(true);  //��ʾ�ɰٷֱ�    
	     
	    
	        pieChar.setCenterText("���չ�"+todayStep+"��");  //��״ͼ�м������    
	    
	        //��������    
	        pieChar.setData(pieData);     
	            
	   // undo all highlights    
//	      pieChart.highlightValues(null);    
//	      pieChart.invalidate();    
	    
	        Legend mLegend = pieChar.getLegend();  //���ñ���ͼ    
	        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);  //���ұ���ʾ    
//	      mLegend.setForm(LegendForm.LINE);  //���ñ���ͼ����״��Ĭ���Ƿ���    
	        mLegend.setXEntrySpace(7f);    
	        mLegend.setYEntrySpace(5f);    
	            
	        pieChar.animateXY(1000, 1000);  //���ö���    
	        // mChart.spin(2000, 0, 360);    
	}
	private PieData getPieData(int count, float allTime, int sta_sitCountTime, int sta_walkCountTime, int sta_runCountTime, int sta_upCountTime, int sta_downCountTime) {    
        
        ArrayList<String> xValues = new ArrayList<String>();  //xVals������ʾÿ�������ϵ�����    
    
         
       xValues.add("��ֹʱ��");  //��������ʾ��   
       xValues.add("����ʱ��");
       xValues.add("�ܲ�ʱ��");
       xValues.add("��¥ʱ��");
       xValues.add("��¥ʱ��");
          
    
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals������ʾ��װÿ�������ʵ������    
    
        // ��ͼ����    
        /**  
         
         */ 
       float quarterly1 = sta_sitCountTime;    
       float quarterly2 = sta_walkCountTime;    
       float quarterly3 = sta_runCountTime;    
       float quarterly4 = sta_upCountTime;    
       float quarterly5 = sta_downCountTime;
        yValues.add(new Entry(quarterly1, 0));    
        yValues.add(new Entry(quarterly2, 1));    
        yValues.add(new Entry(quarterly3, 2));    
        yValues.add(new Entry(quarterly4, 3));    
        yValues.add(new Entry(quarterly5, 4)); 
    
        //y��ļ���    
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*��ʾ�ڱ���ͼ��*/);    
        pieDataSet.setSliceSpace(0f); //���ø���״ͼ֮��ľ���    
    
        ArrayList<Integer> colors = new ArrayList<Integer>();    
    
        // ��ͼ��ɫ    
        colors.add(Color.rgb(250, 160, 147));    
        colors.add(Color.rgb(79, 229, 254));    
        colors.add(Color.rgb(119, 223, 9));    
        colors.add(Color.rgb(149, 150, 253));    
        colors.add(Color.rgb(225, 225, 0)); 
        pieDataSet.setColors(colors);    
    
        DisplayMetrics metrics = getResources().getDisplayMetrics();    
        float px = 5 * (metrics.densityDpi / 160f);    
        pieDataSet.setSelectionShift(px); // ѡ��̬����ĳ���    
    
        PieData pieData = new PieData(xValues, pieDataSet);    
            
        return pieData;    
    }    

}
