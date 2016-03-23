package com.bbcc.mobilehealth.fragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;

import com.bbcc.mobilehealth.HeartRateShowActivity;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.StatisticsActivity;
import com.bbcc.mobilehealth.service.LoactionService;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.DeepSleepModel;
import com.bbcc.mobilehealth.util.HorizontalScrollViewAdapter;
import com.bbcc.mobilehealth.util.MyUser;
import com.bbcc.mobilehealth.util.ShallowSleepModel;
import com.bbcc.mobilehealth.util.SleepModel;
import com.bbcc.mobilehealth.view.MyDialog;
import com.bbcc.mobilehealth.view.MyHorizontalScrollView;
import com.bbcc.mobilehealth.view.MyHorizontalScrollView.CurrentImageChangeListener;
import com.bbcc.mobilehealth.view.MyHorizontalScrollView.OnItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import B.i;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.Handler;
import android.os.Message;
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
	
	private static String year = "2016"; 
	
	private TextView sleepTime,deepsleep,qiansleep,title;
	private TextView rushuiTime,xinglaiTime,qingxingTime;
	private ImageView sanjiao;
	
	
	
	//private HorizontalListView mListView;
	
	private MyHorizontalScrollView mHorizontalScrollView;
	private HorizontalScrollViewAdapter myAdapter;
	
	private List<SleepModel> data = new ArrayList<SleepModel>();
	private ArrayList<String> time = new ArrayList<String>();
	private int pencent[] = { 50, 56, 78, 35, 57, 78, 45, 26, 44, 60, 56, 78,
			35, 57, 78, 45, 26, 44, 60, 56, 78, 35, 57, 78 };
	private ImageView choose_date;

	//private AnimationDrawable animationDrawable;
	
	private Dialog pd;
	
	private String selectedDate;

	private List<ShallowSleepModel> shallowSleepList = new ArrayList<ShallowSleepModel>();
	private List<DeepSleepModel> deepSleepList = new ArrayList<DeepSleepModel>();
	
	private List<ShallowSleepModel> shallowOtherSleepList = new ArrayList<ShallowSleepModel>();
	private List<DeepSleepModel> deepOtherSleepList = new ArrayList<DeepSleepModel>();
	
	
	private int count = 1;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "position:onCreateView");
		
		View rootView = inflater.inflate(R.layout.shuimian, container,false);
		initView(rootView);

		getData();
		
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e("tag", "position:onActivityCreate");
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.e("tag", "position:onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("tag", "position:onCreate");
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onDestroy() {
		Log.e("tag", "position:onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		Log.e("tag", "position:onPause");
//		myAdapter.clearData();
//		data.clear();
//		time.clear();
		//onDestroy();
		super.onPause();
	}

	@Override
	public void onStart() {
		Log.e("tag", "position:onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.e("tag", "position:onStop");
//		if (receiver != null)
//           getActivity().unregisterReceiver(receiver);
		super.onStop();
	}

	@Override
	public void onResume() {
		
//		if (data.size() == 0) {
//			Log.e("tag", "position:onResum");
//			View view = getActivity().getLayoutInflater().inflate(R.layout.shuimian, null);
//			initView(view);
//		}
		
		super.onResume();
	}
	
	@Override
	public void onClick(View view) {
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
//		choose_date.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				View v = getActivity().getLayoutInflater().inflate(R.layout.calendar_dialog, null);
//
//				final AlertDialog dlg = new AlertDialog.Builder(getActivity()).setTitle("查找的时间").setView(v)
//						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								arg0.cancel();
//							}
//						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								arg0.cancel();
//							}
//						}).create();
//				
//				CalendarView staCalendar = (CalendarView)v.findViewById(R.id.calendar_view);
//				Calendar month = Calendar.getInstance();
//				month.add(Calendar.MONTH, -1);
//				
//				Calendar thisMonth = Calendar.getInstance();
//				thisMonth.add(Calendar.MONTH, 1);
//				
//				staCalendar.setMinDate(month.getTimeInMillis());
//				staCalendar.setDate(new Date().getTime());
//				staCalendar.setMaxDate(new Date().getTime());
//				
//				
//				staCalendar.setOnDateChangeListener(new OnDateChangeListener() {
//					
//					@Override
//					public void onSelectedDayChange(CalendarView view, int year, int month,
//							int day) {
//						int realMonth=month+1;
//						String selectedDateStr=year+"-"+realMonth+"-"+day;
//						
//						selectedDate = realMonth + "月" + day +"日";
//						title.setText(selectedDate +"睡眠情况");
//						selectedDate = realMonth + "/"+day;
//						myAdapter.setSelectedDate(selectedDate);
//						//mHorizontalScrollView.invalidate();
//					}
//				});
//				
//				dlg.show();
//			}
//		});
		
		
		sanjiao = (ImageView)rootView.findViewById(R.id.sanjiao);
		
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		
		//myAdapter = new SleepAdapter(getActivity());
		
		
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
		
		title.setText(today+"睡眠情况");
		
		
		
		for (int i = 0;i < time.size();i++) {
			SleepModel model = new SleepModel();
			model.setTime(time.get(i));
			//int random1 = (int)(Math.random() * 200) + 50;
			//int random2 = (int)(Math.random() * 200) + 50;
			//Log.d("tag", random1 + "   "+random2);
			String d = year+"-"+time.get(i).replace("/", "-");
			
			model.setDeepSleepTime((int)getDeepLength(d));
			model.setQianSleepTime((int)getShallowLength(d));
			data.add(model);
		}
		
		Log.d("data", data.size()+" ");
		
		
		
		myAdapter = new HorizontalScrollViewAdapter(getActivity());
		
		myAdapter.setSelectedDate(sdf2.format(new Date()));
//		if (data.size() == 0) {
//			initData();
//		}
		
		
		myAdapter.setData(data);
		
		mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onClick(View view, int pos) {
				xinglaiTimeString = "00:00";
				sleepHour = 0;
				sleepMinute = 0;
				sleepSum = "0分钟";
				rushuiTimeString = "00:00";
				
				Log.d("size", data.size()+" ");
				Log.d("pos", pos+" ");
				Log.d("tagggg", data.get(data.size() - 5 + pos).getTime()+" ");
				if (initialView.equals(view)) {
					
				} else {
				
					TextView t = (TextView) initialView.findViewById(R.id.textview);
					ImageView triangle = (ImageView) initialView.findViewById(R.id.sanjiao);
					triangle.setVisibility(View.GONE);
					t.setTextColor(Color.rgb(187, 187, 187));
					
					selectedDate = data.get(data.size() - 5 + pos).getTime();
					title.setText(selectedDate +"睡眠情况");
					
					//修改点击事件的textView的值
					selectedDate = year+"-"+selectedDate.replace("/", "-");
					
					long qianMinute = getShallowLength(selectedDate);
					long qianHour = 0;
					if (qianMinute >= 60) {
						qianHour += qianMinute / 60;
						qianMinute = qianMinute % 60;
						qiansleep.setText(qianHour+"小时"+qianMinute+"分钟");
					} else {
						qiansleep.setText(qianMinute+"分钟");
					}
					
					//qiansleep.setText(getShallowLength(selectedDate));
					
					long deepMinute = getDeepLength(selectedDate);
					long deepHour = 0;
					if (deepMinute >= 60) {
						deepHour += deepMinute / 60;
						deepMinute = deepMinute % 60;
						deepsleep.setText(deepHour+"小时"+deepMinute+"分钟");
					} else {
						deepsleep.setText(deepMinute+"分钟");
					}
					
					if (sleepMinute > 60) {
						sleepHour += sleepMinute / 60;
						sleepMinute = sleepMinute % 60;
					}
					
					if (sleepHour == 0) {
						sleepSum = sleepMinute+"分钟";
					} else {
						sleepSum = sleepHour+"小时"+sleepMinute+"分钟";
					}
					sleepTime.setText(sleepSum);
					
					rushuiTime.setText(rushuiTimeString);
					long rushuiHour = 0,rushuiMinute = 0;
					if (rushuiTimeString.startsWith("0")) {
						rushuiHour = Long.parseLong(rushuiTimeString.substring(1,2));
					} else 
						rushuiHour = Long.parseLong(rushuiTimeString.substring(0,3));
					if (rushuiTimeString.substring(3).startsWith("0"))
						rushuiMinute = Long.parseLong(rushuiTimeString.substring(4));
					else rushuiMinute = Long.parseLong(rushuiTimeString.substring(3));
					rushuiHour += sleepHour;
					rushuiMinute += sleepMinute;
					
					
					Log.d("rushuiMinute", rushuiMinute+" ");
					if (rushuiMinute > 60) {
						rushuiHour += rushuiMinute / 60;
						rushuiMinute = rushuiMinute % 60;
					}
					
					if (rushuiHour > 24) {
						rushuiHour = rushuiHour - 24; 
					}
					String rushuiHourString = "";
					String rushuiMinuteString = "";
					if (rushuiHour < 10) {
						rushuiHourString = "0"+rushuiHour;
					} else rushuiHourString = rushuiHour+"";
					if (rushuiMinute < 10) {
						rushuiMinuteString = "0"+rushuiMinute;
					} else rushuiMinuteString = rushuiMinute+"";
					xinglaiTimeString = rushuiHourString +":"+ rushuiMinuteString;
					xinglaiTime.setText(xinglaiTimeString);
					
					long qingxingHour = 23 - sleepHour;
					long qingxingMinute = 59 - sleepMinute;
					
					qingxingTime.setText(qingxingHour+"小时"+qingxingMinute+"分钟");
					
					initialView = view;
				}
			}
		});
		
		mHorizontalScrollView.setCurrentImageChangeListener(new CurrentImageChangeListener() {
			
			@Override
			public void onCurrentImgChanged(String startDate,String endDate) {
//				Log.d("dataStrings", dataStrings.size()+" ");
//				handler = new Handler() {
//					public void handleMessage(Message msg) {
//						if (msg.what == 0x11) {
//							
//						}
//							
//					}
//				};
				Log.d("mhorizontaldsa",mHorizontalScrollView.getSleepDataList().size()+" ");
				if (count == 1) {
					getMoreData(startDate, endDate);
				}
				
			}
		});
		
		mHorizontalScrollView.initDatas(myAdapter);
		
		initialView = myAdapter.getView(myAdapter.getCount() - 1, null, mHorizontalScrollView.getmContainer());
		
	}
	
	/*
	 * 获取一段时间睡眠数据
	 */
	private void getData() {
		//pd = new MyDialog(getActivity()).loadDialog1();
		//if (pd != null)
		//	pd.show();
		
		shallowSleepList.clear();
		deepSleepList.clear();
		
		//今天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -4);
		String start = sdf.format(calendar.getTime());
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		RequestParams params = new RequestParams();
		
		params.put("phoneNumber", "18720083869");
		params.put("startDate",start);
		params.put("endDate",today);
		client.post("http://"+Constant.SERVER_URL+"get/getSleep.php",params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				//pd.dismiss();
				String response = new String(arg2);
				
				Log.d("response", response+"'");
				Log.d("status", arg0+" ");
				try {
					JSONObject jsonObject = new JSONObject(response);
					String code = jsonObject.getString("code");
					
					if (code.equals("1")) {
						JSONObject data = jsonObject.getJSONObject("data");
						JSONArray shallowArray = data.getJSONArray("sleepshallow");
						JSONArray deepArray = data.getJSONArray("sleepdeep");
						
						if (shallowArray.length() == 0 && deepArray.length() == 0) {
							Toast.makeText(getActivity(), "没有数据", 2000).show();
							return;
						}
						
						if (shallowArray.length() == 0) {
							Toast.makeText(getActivity(), "无浅度睡眠数据", 2000).show();
						} else if (deepArray.length() == 0) {
							Toast.makeText(getActivity(), "无深度睡眠数据", 2000).show();
						}
						
						int len = shallowArray.length();
						int deeplen = deepArray.length();
						
						for (int i = 0;i < len;i++) {
							ShallowSleepModel shallowModel = new ShallowSleepModel();
							shallowModel.setStartTime(shallowArray.getJSONObject(i).getString("startTime"));
							shallowModel.setEndTime(shallowArray.getJSONObject(i).getString("endTime"));
							shallowSleepList.add(shallowModel);
						}
						
						for (int j = 0;j < deeplen;j++) {
							DeepSleepModel deepModel = new DeepSleepModel();
							deepModel.setStartTime(deepArray.getJSONObject(j).getString("startTime"));
							deepModel.setEndTime(deepArray.getJSONObject(j).getString("endTime"));
							deepSleepList.add(deepModel);
						}
						initData();
						
						Toast.makeText(getActivity(), "获取睡眠数据成功",2000).show();
					} else if (code.equals("0")){
						Toast.makeText(getActivity(), "获取睡眠数据失败",2000).show();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				//pd.dismiss();
				String response = new String(arg2);
				
				Log.d("response", response+"'");
				Log.d("status", arg0+" ");
				Toast.makeText(getActivity(), "连不上服务器，获取睡眠数据失败",2000).show();
			}
		});
	}
	
	
	
	
	//获取选定日期的浅睡眠长度
	private long getShallowLength(String selectedDate) {
		StringBuilder sb = new StringBuilder();
		int len = shallowSleepList.size();
		long sumhour = 0;
		long summinute = 0;
		long shallowSleepMinute = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Log.d("leng", len+"  ");
		if (len != 0) {
			
			for (int i =0;i < len;i++) {
				String date = shallowSleepList.get(i).getStartTime().substring(0,10);
				
				if (date.equals(selectedDate)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					try {
						Date date1 = simpleDateFormat.parse(shallowSleepList.get(i).getStartTime());
					
						Date date2 = simpleDateFormat.parse(shallowSleepList.get(i).getEndTime());
						
						if (i==0) rushuiTimeString = shallowSleepList.get(i).getStartTime().substring(11,16);
						long hour = (date2.getTime() - date1.getTime()) / 1000/3600;
						long minute = (date2.getTime()-date1.getTime())/60/1000 % 60;
						
						
						shallowSleepMinute += (date2.getTime() - date1.getTime()) / 60/1000;
						sumhour += hour;
						summinute += minute;
						
						
					} catch(Exception e) {
					
					}
					
				}
			}
			if (summinute > 60) {
				sumhour += summinute / 60;
				summinute = summinute % 60;
			}
			if (sumhour == 0) {
				sleepMinute += summinute;
				//sb.append(summinute+"分钟");
			} else {
				sleepHour += sumhour;
				sleepMinute += summinute;
				//sb.append(sumhour+"小时"+summinute+"分钟");
			}
			return shallowSleepMinute;
			//return sb.toString();
		} else {
			return 0;
		}
	}
	
	//获取深睡时长
	private long getDeepLength(String selectedDate) {
		StringBuilder sb = new StringBuilder();
		int len = deepSleepList.size();
		long sumhour = 0;
		long summinute = 0;
		long deepSleepMinute = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (len != 0) {
			
			for (int i =0;i < len;i++) {
				
				String date = deepSleepList.get(i).getStartTime().substring(0,10);
				
				if (date.equals(selectedDate)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					try {
				
						Date date1 = simpleDateFormat.parse(deepSleepList.get(i).getStartTime());
					
						Date date2 = simpleDateFormat.parse(deepSleepList.get(i).getEndTime());
						
						
						
						long hour = (date2.getTime() - date1.getTime()) / 1000/3600;
						long minute = (date2.getTime()-date1.getTime())/60/1000 % 60;
						
						deepSleepMinute += (date2.getTime()-date1.getTime())/60/1000;
						sumhour += hour;
						summinute += minute;
						
						
					} catch(Exception e) {
					
					}
					
				}
			}
			
			if (summinute > 60) {
				sumhour += summinute / 60;
				summinute = summinute % 60;
			}
			if (sumhour == 0) {
				sleepMinute += summinute;
				//sb.append(summinute+"分钟");
			} else {
				sleepHour += sumhour;
				sleepMinute += summinute;
				//sb.append(sumhour+"小时"+summinute+"分钟");
			}
			return deepSleepMinute;
//			return sb.toString();
		} else {
			return 0;
		}
	}
	
	
	private String xinglaiTimeString;
	private String rushuiTimeString = "00:00";
	private String sleepSum;
	private long sleepHour;
	private long sleepMinute;
	
	//private long shallowSleepMinute = 0;
	//private long deepSleepMinute = 0;
	
	
	private void getMoreData(String startDate,String endDate) {
		//滑动次数
		count++;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		
		params.put("phoneNumber", "18720083869");
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		client.post("http://"+Constant.SERVER_URL+"get/getSleep.php",params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				//pd.dismiss();
				String response = new String(arg2);
				
				//Log.d("response", response+"'");
				Log.d("status", arg0+" ");
				try {
					JSONObject jsonObject = new JSONObject(response);
					String code = jsonObject.getString("code");
					
					if (code.equals("1")) {
						JSONObject data = jsonObject.getJSONObject("data");
						JSONArray shallowArray = data.getJSONArray("sleepshallow");
						JSONArray deepArray = data.getJSONArray("sleepdeep");
						
						if (shallowArray.length() == 0 && deepArray.length() == 0) {
							Toast.makeText(getActivity(), "没有数据", 2000).show();
							return;
						}
						if (shallowArray.length() == 0) {
							Toast.makeText(getActivity(), "无浅度睡眠数据", 2000).show();
						} else if (deepArray.length() == 0) {
							Toast.makeText(getActivity(), "无深度睡眠数据", 2000).show();
						}
						
						
						
						int len = shallowArray.length();
						int deeplen = deepArray.length();
						
						for (int i = 0;i < len;i++) {
							ShallowSleepModel shallowModel = new ShallowSleepModel();
							shallowModel.setStartTime(shallowArray.getJSONObject(i).getString("startTime"));
							shallowModel.setEndTime(shallowArray.getJSONObject(i).getString("endTime"));
							shallowOtherSleepList.add(shallowModel);
						}
						
						for (int j = 0;j < deeplen;j++) {
							DeepSleepModel deepModel = new DeepSleepModel();
							deepModel.setStartTime(deepArray.getJSONObject(j).getString("startTime"));
							deepModel.setEndTime(deepArray.getJSONObject(j).getString("endTime"));
							deepOtherSleepList.add(deepModel);
						}
						
						//模拟20多天
						for (int k = 5;k <= 30 ;k++) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Calendar cal2 = Calendar.getInstance();
							cal2.add(Calendar.DATE, -k);
							String d = sdf.format(cal2.getTime());
							long qianlength = -1;
							for (int m = 0;m < len;m++) {
								if (d.equals(shallowOtherSleepList.get(m).getStartTime().substring(0,9))) {
									qianlength = getShallowLength(d);
								}
							}
							
							long deepLength= -1;
							for (int n = 0;n < deeplen;n++) {
								if (d.equals(deepOtherSleepList.get(n).getStartTime().substring(0, 9))) {
									deepLength = getShallowLength(d);
								}
							}
							String result = "";
							if (deepLength == -1)
								result = "0,";
							else result = deepLength+",";
							
							if (qianlength == -1) result = result+"0";
							else result = result+qianlength;
							
							//String d = shallowSleepList.get(m).getStartTime().substring(0,9);
							
							dataStrings.add(result);	
						}
						Log.d("dataStringsssss", dataStrings.size()+" ");
						mHorizontalScrollView.setSleepDataList(dataStrings);
						//initData();
						
						//Toast.makeText(getActivity(), "获取睡眠数据成功",2000).show();
						
					} else if (code.equals("0")){
						//Toast.makeText(getActivity(), "获取睡眠数据失败",2000).show();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				//pd.dismiss();
				String response = new String(arg2);
				Toast.makeText(getActivity(), "连不上服务器，获取睡眠数据失败",2000).show();
			}
		});
		
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	private List<String> dataStrings = new ArrayList<String>();
	private Handler handler = new Handler();
}
