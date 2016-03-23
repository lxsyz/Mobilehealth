package com.bbcc.mobilehealth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.DeepSleepModel;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.bbcc.mobilehealth.util.REMSleepModel;
import com.bbcc.mobilehealth.util.ShallowSleepModel;
import com.bbcc.mobilehealth.view.HeartRateView;
import com.bbcc.mobilehealth.view.MyDialog;
import com.bbcc.mobilehealth.view.WLQQTimePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;

public class HeartRateShowActivity extends Activity implements OnClickListener{
	
	HeartRateView heartRateView;
	ImageView textView;
	CalendarView calendarView;
	ImageView imageView;
	TextView low,high;
	
	private int month;
	private int day;
	private int year;
	private Dialog pd;
	
	//模拟数据
	private int[] maxData = new int[]{60,70,75,80,85,85,90,122,130,125,
										100,95,130,90,85,86,88,80,75,
										85,84,83,84,75};
	private int[] minData = new int[] {
			35,45,55,56,32,21,20,15,18,25,
			24,55,53,52,56,64,68,69,70,
			64,55,56,45,63
			};
	
	//真实数据
	private int[] maxRate = new int[24];
	private int[] minRate = new int[24];
	
	//每分钟心率
	public static List<Integer> list = new ArrayList<Integer>();
	//时间
	private ArrayList<String> timeList = new ArrayList<String>();
	//
	//利用不同睡眠阶段，平均值和标准差的变化情况
	private List<Double> avglist = new ArrayList<Double>();
	private List<Double> stdlist = new ArrayList<Double>();
	
	private List<DeepSleepModel> deepSleepList = new ArrayList<DeepSleepModel>();
	private List<ShallowSleepModel> shallowSleepList = new ArrayList<ShallowSleepModel>();
	private List<REMSleepModel> remSleepList = new ArrayList<REMSleepModel>();
	
	private TextView title;
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heartrate_show);
		
		
		initView();
		
	}
	
	private void initView () {
		Log.d("initview", "initview");
		heartRateView = (HeartRateView)findViewById(R.id.myview);
		textView = (ImageView)findViewById(R.id.bt_right);
		imageView = (ImageView)findViewById(R.id.bt_left);
		low = (TextView)findViewById(R.id.low);
		high = (TextView)findViewById(R.id.high);
		title = (TextView)findViewById(R.id.title);
		btn = (Button)findViewById(R.id.upload);
		
		btn.setOnClickListener(this);
		textView.setOnClickListener(this);
		imageView.setOnClickListener(this);
	}
	
	private void initData() {
		heartRateView.initData(maxRate,minRate);
		
		
		low.setText("最低心率: "+getMin(minRate));
		high.setText("最高心率: "+getMax(maxRate));
		
	}
	
	@Override
	protected void onPause() {

		Log.d("showpause", "ada");
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Log.d("showresume", "showresume");
		
		
		//获取数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
		String today2 = sdf2.format(new Date());
		
		
		title.setText(today2+"心率");
		getData(today);
		getMinRate();
		super.onResume();
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_right:
//			View view = getLayoutInflater().inflate(R.layout.calendar_dialog, null);
//			calendarView = (CalendarView)view.findViewById(R.id.calendar_view);
//			final AlertDialog dlg = new AlertDialog.Builder(HeartRateShowActivity.this).setTitle("选择时间").setView(view)
//									.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					                    public void onClick(DialogInterface dialog, int whichButton) {  
//					                        
//					                    	//edit_birthday_textview.setText(dateTime); 
//					                    }  
//					                })  
//					                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
//					                    public void onClick(DialogInterface dialog, int whichButton) {  
//					                          //edit_birthday_textview.setText("");
//					                    }  
//					                }).show();;
//			
//			Calendar month = Calendar.getInstance();
//			month.add(Calendar.MONTH,-1);
//			
//			Calendar tomorrow = Calendar.getInstance();
//			tomorrow.add(Calendar.MONTH, 1);
//			
//			Calendar today = Calendar.getInstance();
//			today.add(Calendar.DAY_OF_YEAR, 0);
//			calendarView.setMinDate(month.getTimeInMillis());
//			
//			calendarView.setDate(new Date().getTime());
//			calendarView.setFocusable(true);
//			
//			calendarView.setMaxDate(tomorrow.getTimeInMillis());
//			//calendarView.set
//			Log.d("tag", today.getTimeInMillis()+" ");
//			dlg.show();
//			calendarView.setOnDateChangeListener(new OnDateChangeListener() {
//				
//				@Override
//				public void onSelectedDayChange(CalendarView arg0, int arg1, int arg2,
//						int arg3) {
//					
//				}
//			});
			final WLQQTimePicker timePicker = new WLQQTimePicker(HeartRateShowActivity.this);
			timePicker.setDate(new Date().getTime());
			final AlertDialog dlg2 = new AlertDialog.Builder(HeartRateShowActivity.this).create();
			dlg2.show();
			final Window window2 = dlg2.getWindow();
			window2.setContentView(timePicker);
			window2.findViewById(R.id.cancel).setOnClickListener(
					new OnClickListener() {

						public void onClick(View v) {
							dlg2.cancel();
						}
					});
			
			window2.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dlg2.cancel();
					year = timePicker.getmYear();
					month = timePicker.getmMonth() + 1;
					day = timePicker.getmDay();
					title.setText(month+"月"+day+"日心率");
					String date;
					if (month < 10) {
						date = year +"-"+"0"+month+"-"+day;
					} else {
						date = year + "-"+month+"-"+day;
					}
					getData(date);
					
				}
			});
			
			break;
		case R.id.bt_left:
			HeartRateShowActivity.this.finish();
			break;
		case R.id.upload:
			uploadSleepData();
			break;
		default:
			break;
		}
	}
	
	/*
	 * 获取一天24个小时每小时的心率数据
	 */
	private void getData(String date) {
		pd = new MyDialog(this).loadDialog1();
		if (pd != null)
			pd.show();
		//今天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		RequestParams params = new RequestParams();
		
		params.put("phoneNumber", "18720083869");
		params.put("date", date);
		client.post("http://"+Constant.SERVER_URL+"get/getrateDay.php",params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				pd.dismiss();
				String response = new String(arg2);
				
				Log.d("response", response+"'");
				Log.d("status", arg0+" ");
				try {
					JSONObject jsonObject = new JSONObject(response);
					String code = jsonObject.getString("code");
					
					if (code.equals("1")) {
						JSONArray array = jsonObject.getJSONArray("data");
						
						Log.d("data", array.getJSONArray(1).getJSONObject(1).getString("MIN(rate)") + " ");
						for (int i = 0;i < 24;i++) {
							if (array.getJSONArray(i).getJSONObject(0).getString("MAX(rate)").equals("null")) {
								maxRate[i] = 0;
							}
							else {
								maxRate[i] = Integer.parseInt(array.getJSONArray(i).getJSONObject(0).getString("MAX(rate)"));
							}
							
							if (array.getJSONArray(i).getJSONObject(1).getString("MIN(rate)").equals("null")) {
								minRate[i] = 0;
							}
							else {
								minRate[i] = Integer.parseInt(array.getJSONArray(i).getJSONObject(1).getString("MIN(rate)"));
							}
							
							
						}
						
						initData();
						Toast.makeText(HeartRateShowActivity.this, "获取成功",2000).show();
					} else if (code.equals("0")){
						Toast.makeText(HeartRateShowActivity.this, "获取失败",2000).show();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				pd.dismiss();
				Toast.makeText(HeartRateShowActivity.this, "连不上服务器，获取失败",2000).show();
			}
		});
	}
	
	/*
	 * 心率转换成睡眠
	 */
	private void heartRate2Sleep() {
			int len=list.size();  //获取数据的总个数
			int shengyu = len % 3;
			
			for (int j = 0;j < len - shengyu;j = j + 3) {
				double avg = (list.get(j) + list.get(j+1)+list.get(j+2)) / 3.0;
				double std = Math.sqrt(((list.get(j)-avg)*(list.get(j)-avg)+(list.get(j+1)-avg)*(list.get(j+1)-avg)+(list.get(j+2)-avg)*(list.get(j+2)-avg))/3);
				//System.out.println(avg+"    "+std);
				avglist.add(avg);
				stdlist.add(std);
			}
			
			//将每一组的标准差,平均值，和设定好的阈值相对比进而判断睡眠分期
			int MIN_std=1;
			int MIN_avg=61;
			int MAX_std=2;
			int MAX_avg=64;
			
			String startTime=timeList.get(0),endTime="";
			int shuxing=-1;
			for(int j=0;j<avglist.size();j++){
			
				if (avglist.get(j) < MIN_avg && stdlist.get(j) < MIN_std) {
					if  ((shuxing==-1)||(shuxing==0)){
					    endTime=timeList.get(j*3+3);
					    shuxing=0;
					}
					else{
						if (shuxing == 1) {
							ShallowSleepModel shallowSleepModel = new ShallowSleepModel();
							shallowSleepModel.setStartTime(startTime);
							shallowSleepModel.setEndTime(endTime);
							shallowSleepList.add(shallowSleepModel);
						} else if (shuxing == 2){
							REMSleepModel remSleepModel = new REMSleepModel();
							remSleepModel.setStartTime(startTime);
							remSleepModel.setEndTime(endTime);
							remSleepList.add(remSleepModel);
						}
						
					    //System.out.println("#####"+startTime+" "+endTime);
					    shuxing=0;
					    startTime=timeList.get(j*3);
					    endTime=timeList.get(j*3+3);
					}
					//System.out.println("第" + (j + 1) + "组正处于深度睡眠阶段");
					
					
				}
				if (stdlist.get(j) > MAX_std && avglist.get(j) < MAX_avg) {
					if  ((shuxing==-1)||(shuxing==1)){
					    endTime=timeList.get(j*3+3);
					    shuxing=1;
					}
					else{
						if (shuxing == 0) {
							DeepSleepModel deepSleepModel = new DeepSleepModel();
							deepSleepModel.setEndTime(endTime);
							deepSleepModel.setStartTime(startTime);
							deepSleepList.add(deepSleepModel);
						} else if (shuxing == 2) {
							REMSleepModel remSleepModel = new REMSleepModel();
							remSleepModel.setStartTime(startTime);
							remSleepModel.setEndTime(endTime);
							remSleepList.add(remSleepModel);
						}
					    //System.out.println("##### "+startTime+" "+endTime);
					    shuxing=1;
					    startTime=timeList.get(j*3);
					    endTime=timeList.get(j*3+3);
					}
					//System.out.println("第" + (j + 1) + "组正处浅度睡眠阶段");
				}
				if (avglist.get(j) > MAX_avg) {
					if  ((shuxing==-1)||(shuxing==2)){
					    endTime=timeList.get(j*3+3);
					    shuxing=2;
					}
					else{
						if (shuxing == 0) {
							DeepSleepModel deepSleepModel = new DeepSleepModel();
							deepSleepModel.setEndTime(endTime);
							deepSleepModel.setStartTime(startTime);
							deepSleepList.add(deepSleepModel);
						} else if (shuxing == 1) {
							ShallowSleepModel shallowSleepModel = new ShallowSleepModel();
							shallowSleepModel.setStartTime(startTime);
							shallowSleepModel.setEndTime(endTime);
							shallowSleepList.add(shallowSleepModel);
						}
						
					    //System.out.println("##### "+startTime+" "+endTime);
					    shuxing=2;
					    startTime=timeList.get(j*3);
					    endTime=timeList.get(j*3+3);
					}
					//System.out.println("第" + (j + 1) + "组正处于REM睡眠阶段");
				}
			}
			
	}
	
	/*
	 * 获取每分钟心率
	 */
	private void getMinRate() {
		
		timeList.clear();
		list.clear();
		
		//今天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());

		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();

		//测试代码
		params.put("phoneNumber", "18720083869");
		params.put("date", "2016-03-18");
		
		
		client.post("http://" + Constant.SERVER_URL + "get/getrate.php",
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						//pd.dismiss();
						String response = new String(arg2);

						Log.d("response", response + "'");
						Log.d("status", arg0 + " ");
						try {
							JSONObject jsonObject = new JSONObject(response);
							String code = jsonObject.getString("code");
							
							if (code.equals("1")) {
								JSONArray array = jsonObject.getJSONArray("data");

								int len = array.length();
								
								for (int i = 0;i < len;i++) {
									timeList.add(array.getJSONObject(i).getString("time"));
									list.add(Integer.parseInt(array.getJSONObject(i).getString("rate")));
								}

								
								//获取完心率数据后 转换成睡眠
								heartRate2Sleep();
								
								
								
								
//								Toast.makeText(HeartRateShowActivity.this,
//										"获取成功", 2000).show();
							} else if (code.equals("0")) {
//								Toast.makeText(HeartRateShowActivity.this,
//										"获取失败", 2000).show();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						//pd.dismiss();
						Toast.makeText(HeartRateShowActivity.this,
								"连不上服务器，获取失败", 2000).show();
					}
				});
	}
	
	
	//上传睡眠数据
	private void uploadSleepData() {
		AsyncHttpClient client = new AsyncHttpClient();
		
		RequestParams params = new RequestParams();
		int deepLength = deepSleepList.size();
		int shallowLength = shallowSleepList.size();
		
		if (deepLength > 0) {
			for (int i = 0;i < deepLength;i++) {
				params.put("phoneNumber", "18720083869");
				params.put("startTime", deepSleepList.get(i).getStartTime());
				params.put("endTime", deepSleepList.get(i).getEndTime());
				params.put("type", 2);
				
				client.post("http://" + Constant.SERVER_URL + "uploadsleep.php",
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								String response = new String(arg2);

								Log.d("response", response + "'");
								Log.d("status", arg0 + " ");
								try {
									JSONObject jsonObject = new JSONObject(response);
									String code = jsonObject.getString("code");
									if (code.equals("1")) {
										
									} else if (code.equals("0")) {
										
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2,
									Throwable arg3) {
								Toast.makeText(HeartRateShowActivity.this,
										"连不上服务器，获取失败", 2000).show();
							}
						});
			}
		}
		Log.d("shallowlenghththt", shallowLength+" ");
		if (shallowLength > 0) {
			for (int j = 0;j < shallowLength;j++) {
				params.put("phoneNumber", "18720083869");
				params.put("startTime", shallowSleepList.get(j).getStartTime());
				params.put("endTime", shallowSleepList.get(j).getEndTime());
				params.put("type", 1);
				
				client.post("http://" + Constant.SERVER_URL + "uploadsleep.php",
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								String response = new String(arg2);

								Log.d("response", response + "'");
								Log.d("status", arg0 + " ");
								try {
									JSONObject jsonObject = new JSONObject(response);
									String code = jsonObject.getString("code");
									if (code.equals("1")) {
										
									} else if (code.equals("0")) {
										
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2,
									Throwable arg3) {
								Toast.makeText(HeartRateShowActivity.this,
										"连不上服务器，获取失败", 2000).show();
							}
						});
			}
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
			if (a[i] < min && a[i] != 0) min = a[i];
		}
		return min;
	}
	
	
}
