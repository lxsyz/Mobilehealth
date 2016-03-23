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
		sta_stepCount.setText(""+todayStep+"步");
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
		timeStr=hours+"小时"+minute+"分钟"+second+"秒";
		return timeStr;
		
	}
	//初始化饼状图
	public void initPieChar(PieChart pieChart, PieData pieData, String selectedDateStr, int todayStep)
	{
		    pieChar.setHoleColorTransparent(true);    
		    
	        pieChar.setHoleRadius(60f);  //半径    
	        pieChar.setTransparentCircleRadius(64f); // 半透明圈    
	        //pieChart.setHoleRadius(0)  //实心圆    
	    
	        pieChar.setDescription(selectedDateStr+" 状态时间统计");    
	    
	        // mChart.setDrawYValues(true);    
	        pieChar.setDrawCenterText(true);  //饼状图中间可以添加文字    
	    
	        pieChar.setDrawHoleEnabled(true);    
	    
	        pieChar.setRotationAngle(90); // 初始旋转角度    
	    
	        // draws the corresponding description value into the slice    
	        // mChart.setDrawXValues(true);    
	    
	        // enable rotation of the chart by touch    
	        pieChar.setRotationEnabled(true); // 可以手动旋转    
	    
	        // display percentage values    
	        pieChar.setUsePercentValues(true);  //显示成百分比    
	     
	    
	        pieChar.setCenterText("今日共"+todayStep+"步");  //饼状图中间的文字    
	    
	        //设置数据    
	        pieChar.setData(pieData);     
	            
	   // undo all highlights    
//	      pieChart.highlightValues(null);    
//	      pieChart.invalidate();    
	    
	        Legend mLegend = pieChar.getLegend();  //设置比例图    
	        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);  //最右边显示    
//	      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形    
	        mLegend.setXEntrySpace(7f);    
	        mLegend.setYEntrySpace(5f);    
	            
	        pieChar.animateXY(1000, 1000);  //设置动画    
	        // mChart.spin(2000, 0, 360);    
	}
	private PieData getPieData(int count, float allTime, int sta_sitCountTime, int sta_walkCountTime, int sta_runCountTime, int sta_upCountTime, int sta_downCountTime) {    
        
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容    
    
         
       xValues.add("静止时间");  //饼块上显示成   
       xValues.add("行走时间");
       xValues.add("跑步时间");
       xValues.add("上楼时间");
       xValues.add("下楼时间");
          
    
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据    
    
        // 饼图数据    
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
    
        //y轴的集合    
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);    
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离    
    
        ArrayList<Integer> colors = new ArrayList<Integer>();    
    
        // 饼图颜色    
        colors.add(Color.rgb(250, 160, 147));    
        colors.add(Color.rgb(79, 229, 254));    
        colors.add(Color.rgb(119, 223, 9));    
        colors.add(Color.rgb(149, 150, 253));    
        colors.add(Color.rgb(225, 225, 0)); 
        pieDataSet.setColors(colors);    
    
        DisplayMetrics metrics = getResources().getDisplayMetrics();    
        float px = 5 * (metrics.densityDpi / 160f);    
        pieDataSet.setSelectionShift(px); // 选中态多出的长度    
    
        PieData pieData = new PieData(xValues, pieDataSet);    
            
        return pieData;    
    }    

}
