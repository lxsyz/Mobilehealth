package com.bbcc.mobilehealth.fragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.StatisticsActivity;
import com.bbcc.mobilehealth.service.LoactionService;
import com.bbcc.mobilehealth.util.HorizontalScrollViewAdapter;
import com.bbcc.mobilehealth.util.MyUser;
import com.bbcc.mobilehealth.util.SleepModel;
import com.bbcc.mobilehealth.view.MyHorizontalScrollView;
import com.bbcc.mobilehealth.view.MyHorizontalScrollView.OnItemClickListener;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;


public class PositionFragment extends Fragment implements OnClickListener  {
	
	//private TextView badTime, goodTime, title;
	private TextView sleepTime,deepsleep,qiansleep,title;
	private TextView rushuiTime,xinglaiTime,qingxingTime;
	private ImageView sanjiao;
	
	//private HorizontalListView mListView;
	
	private MyHorizontalScrollView mHorizontalScrollView;
	//private SleepAdapter myAdapter;
	private HorizontalScrollViewAdapter myAdapter;
	
	private List<SleepModel> data = new ArrayList<SleepModel>();
	private ArrayList<String> time = new ArrayList<String>();
	private int pencent[] = { 50, 56, 78, 35, 57, 78, 45, 26, 44, 60, 56, 78,
			35, 57, 78, 45, 26, 44, 60, 56, 78, 35, 57, 78 };
	private ImageView choose_date;
	private int genderInt = 0;

	//private AnimationDrawable animationDrawable;
	private LinearLayout content;
	
	
	private String selectedDate;
//	 private TextView longitude;
//	 private TextView latitude;
//	
//	
//	 private TextView address;
//	 
//	 private Receiver receiver;
//	 
//	 private Intent serviceIntent;
//	 
//	 private Button position_start;
//	 private Button position_stop;
//	 
//	 private String userId;
//	 
//	 private boolean locationIsOpen;
//	 private SharedPreferences settings;
//     private SharedPreferences.Editor editor;
	 
//	 public class Receiver extends BroadcastReceiver {
//	        /**
//	         * �չ��캯��,���ڳ�ʼ��Recevier
//	         */
//	        @Override
//	        public void onReceive(Context context, Intent intent) {
//	        	Log.i("tag", "LoactionRecevier:"+intent.getDoubleExtra("latitude", 0)
//	        			+","+intent.getDoubleExtra("latitude", 0)
//	        			+","+intent.getStringExtra("address"));
//	        	latitude.setText(""+intent.getDoubleExtra("latitude", 0));
//	        	longitude.setText(""+intent.getDoubleExtra("longitude", 0));
//	        	address.setText(intent.getStringExtra("address"));
//	        }
//	 };
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onCreateView");
		
		//Context context = new ContextThemeWrapper(getActivity(), R.style.MyTheme);
        // clone the inflater using the ContextThemeWrapper
        //LayoutInflater localInflater = inflater.cloneInContext(context);
		
		View rootView = inflater.inflate(R.layout.shuimian, container,false);
		initView(rootView);


		
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onActivityCreate");
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.e("tag", "position:onAttach");
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onPause");
//		myAdapter.clearData();
//		data.clear();
//		time.clear();
		//onDestroy();
		super.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onStart");
//		receiver=new Receiver();
//		serviceIntent = new Intent(getActivity(),
//                LoactionService.class);
//		IntentFilter filter = new IntentFilter();
//        filter.addAction("LocationBroadcast");
//        getActivity().registerReceiver(receiver, filter);
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onStop");
//		if (receiver != null)
//           getActivity().unregisterReceiver(receiver);
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
//		if (data.size() == 0) {
//			Log.e("tag", "position:onResum");
//			View view = getActivity().getLayoutInflater().inflate(R.layout.shuimian, null);
//			initView(view);
//		}
		super.onResume();

	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		}

	}
	private void initView(View rootView) {
		title = (TextView)rootView.findViewById(R.id.title);
		//mListView = (HorizontalListView)rootView.findViewById(R.id.listview);
		mHorizontalScrollView  = (MyHorizontalScrollView)rootView.findViewById(R.id.listview);
		deepsleep = (TextView)rootView.findViewById(R.id.deepsleep);
		qiansleep = (TextView)rootView.findViewById(R.id.qiansleep);
		sleepTime = (TextView)rootView.findViewById(R.id.sleepTime);
		qingxingTime = (TextView)rootView.findViewById(R.id.qingxingTime);
		rushuiTime = (TextView)rootView.findViewById(R.id.rushuiTime);
		xinglaiTime = (TextView)rootView.findViewById(R.id.xinglaiTime);
		title = (TextView)rootView.findViewById(R.id.title);
		choose_date = (ImageView)rootView.findViewById(R.id.choose_date);
		choose_date.setVisibility(View.GONE);
		choose_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				View v = getActivity().getLayoutInflater().inflate(R.layout.calendar_dialog, null);

				final AlertDialog dlg = new AlertDialog.Builder(getActivity()).setTitle("查找的时间").setView(v)
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.cancel();
							}
						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.cancel();
							}
						}).create();
				
				CalendarView staCalendar = (CalendarView)v.findViewById(R.id.calendar_view);
				Calendar month = Calendar.getInstance();
				month.add(Calendar.MONTH, -1);
				
				Calendar thisMonth = Calendar.getInstance();
				thisMonth.add(Calendar.MONTH, 1);
				
				staCalendar.setMinDate(month.getTimeInMillis());
				staCalendar.setDate(new Date().getTime());
				staCalendar.setMaxDate(new Date().getTime());
				
				
				staCalendar.setOnDateChangeListener(new OnDateChangeListener() {
					
					@Override
					public void onSelectedDayChange(CalendarView view, int year, int month,
							int day) {
						int realMonth=month+1;
						String selectedDateStr=year+"-"+realMonth+"-"+day;
						
						selectedDate = realMonth + "月" + day +"日";
						title.setText(selectedDate +"睡眠情况");
						selectedDate = realMonth + "/"+day;
						myAdapter.setSelectedDate(selectedDate);
						//mHorizontalScrollView.invalidate();
					}
				});
				
				dlg.show();
			}
		});
		
		
		sanjiao = (ImageView)rootView.findViewById(R.id.sanjiao);
		
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		
		//myAdapter = new SleepAdapter(getActivity());
		myAdapter = new HorizontalScrollViewAdapter(getActivity());
		
		myAdapter.setSelectedDate(sdf2.format(new Date()));
		if (data.size() == 0) {
			initData();
		}
		
		deepsleep.setText(data.get(time.size() - 1).getDeepSleepTime() / 20+"小时");
		qiansleep.setText(data.get(time.size() - 1).getQianSleepTime() / 20 +"小时");
		sleepTime.setText(data.get(time.size() - 1).getDeepSleepTime() / 20 + data.get(time.size() - 1).getQianSleepTime() / 20 +"小时");
		qingxingTime.setText(24 - (data.get(time.size() - 1).getDeepSleepTime() / 20 + data.get(time.size() - 1).getQianSleepTime() / 20 )+"小时");
		rushuiTime.setText("20:00");
		int x = 20 + data.get(time.size() - 1).getDeepSleepTime() / 20 + data.get(time.size() - 1).getQianSleepTime() / 20 - 24;
		xinglaiTime.setText(String.valueOf(x)+"点");
		
		myAdapter.setData(data);
		
		mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onClick(View view, int pos) {
				Log.d("size", data.size()+" ");
				Log.d("pos", pos+" ");
				Log.d("tagggg", data.get(data.size() - 5 + pos).getTime()+" ");
				if (initialView.equals(view)) {
					
				} else {
				
					TextView t = (TextView) initialView.findViewById(R.id.textview);
					ImageView triangle = (ImageView) initialView.findViewById(R.id.sanjiao);
					triangle.setVisibility(View.GONE);
					Log.d("adsasd", t.getText()+" ");
					t.setTextColor(Color.rgb(187, 187, 187));
					
					selectedDate = data.get(data.size() - 5 + pos).getTime();
					title.setText(selectedDate +"睡眠情况");
					
					initialView = view;
				}
			}
		});
		mHorizontalScrollView.initDatas(myAdapter);
		initialView = myAdapter.getView(myAdapter.getCount() - 1, null, mHorizontalScrollView.getmContainer());
		
	}
	View initialView;
	
	private void initData() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		String today = sdf.format(new Date());
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		
		
		for (int i =0;i < 5 ;i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, i-4);
			
			time.add(sdf2.format(cal.getTime()));
		}
		
		Log.d("sd", title.getText()+" ");
		title.setText(today+"睡眠情况");
		
		
		
		for (int i = 0;i < time.size();i++) {
			SleepModel model = new SleepModel();
			model.setTime(time.get(i));
			int random1 = (int)(Math.random() * 200) + 50;
			int random2 = (int)(Math.random() * 200) + 50;
			Log.d("tag", random1 + "   "+random2);
			model.setDeepSleepTime(random1);
			model.setQianSleepTime(random2);
			data.add(model);
		}
		
		Log.d("data", data.size()+" ");
	}
}
