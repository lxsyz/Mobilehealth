package com.bbcc.mobilehealth.fragment;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.util.Convert;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.ResultActivity;
import com.bbcc.mobilehealth.fragment.HardwareConnectorFragment;
import com.bbcc.mobilehealth.util.MyDBHelp;

public abstract class CapabilityFragment extends HardwareConnectorFragment {

	private static final Logger L = new Logger(CapabilityFragment.class);
	private Map<String, String> mCallbackResults = new TreeMap<String, String>();
	private ConnectionParams mConnectionParams;
	private MyDBHelp myDbHelp;
	private static SQLiteDatabase db;
    private static String actionState="";
    private static ContentValues cv= new ContentValues();
    
    private static String userId;
    private static int heartCount=0;
    private static double heartSum=0;
    private static int sitCount=0;
    private static int walkCount=0;
    private static int runCount=0;
    private static int downCount=0;
    private static int upCount=0;
    private static String tempState="";
    private static Context context;
    private static Handler handler;
	private static Runnable runnable;
    private MyReceiver receiver;
    
    private static int heartrateBpm=70;
    public static int getHeartrateBpm(){
    	return heartrateBpm;
    }

	public ConnectionParams getConnectionParams() {
		return mConnectionParams;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
//		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//		filter.addAction("com.bbcc.mobilehealth.service.UPDATE");
//		Log.i("tag", "注册监听成功！");	
//		activity.registerReceiver(receiver, filter);
		context=activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("tag", "oncreat！");
		super.onCreate(savedInstanceState);
		
		
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.capability_fragment_menu, menu);
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myDbHelp=new MyDBHelp(getActivity());
		db= myDbHelp.getWritableDatabase();
		userId="";
		if(BmobUser.getCurrentUser(getActivity())!=null)
		{
		userId=BmobUser.getCurrentUser(getActivity()).getObjectId();
		}
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction("com.bbcc.mobilehealth.service.UPDATE");
		Log.i("tag", "注册监听成功！");
		receiver=new MyReceiver();
		getActivity().registerReceiver(receiver, filter);
		Log.i("tag", "capability oncreatview！");
		Context context = inflater.getContext();
		ScrollView sv = createSimpleScrollView(context);
		LinearLayout ll = (LinearLayout) sv.getChildAt(0);
		initView(context, ll);
		handler=new Handler();
		runnable=new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tempState="";
			}};
		return sv;
	}

	@Override
	public void onFirmwareUpdateRequired(SensorConnection sensorConnection,
			String currentVersionNumber, String recommendedVersion) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.capability_fragment_menu_refresh) {
			refreshView();
			return true;
		} else {
			return false;
		}
	}

	public void setConnectionParams(ConnectionParams connectionParams) {
		if (connectionParams == null) {
			throw new IllegalArgumentException("ConnectionParams cannot be null");
		}
		this.mConnectionParams = connectionParams;
	}
	protected String getCallbackSummary() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : mCallbackResults.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		return sb.toString().trim();
	}

	protected Capability getCapability(CapabilityType capabilityType) {
		SensorConnection sensorConnection = getSensorConnection();
		if (sensorConnection != null) {
			return sensorConnection.getCurrentCapability(capabilityType);
		} else {
			return null;
		}
	}

	protected SensorConnection getSensorConnection() {
		return getSensorConnection(mConnectionParams);
	}

	protected abstract void initView(Context context, LinearLayout ll);

	protected abstract void refreshView();

	protected SimpleDateFormat mFormatHHMMSS = new SimpleDateFormat("HH:mm:ss", Locale.US);

	protected void registerCallbackResult(String key, Object... values) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(mFormatHHMMSS.format(Calendar.getInstance().getTime())).append("] ");
		for (Object value : values)
			sb.append(value).append(" ");
		mCallbackResults.put(key, sb.toString().trim());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("tag", "ondestroy！");
		
		super.onDestroy();
	}

	protected void toast(Object result) {
		Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
	}

	public static CapabilityFragment create(CapabilityType capabilityType, ConnectionParams params) {
		CapabilityFragment fragment = null;
		switch (capabilityType) {
		case Heartrate: {
			fragment = new CapHeartrateFragment();
			break;
		}
		default:
			break;

		}

		if (fragment != null) {
			fragment.setConnectionParams(params);
			return fragment;
		} else {
			return null;
		}
	}

	protected static Button createSimpleButton(Context context, String text, OnClickListener ocl) {
		Button but = new Button(context);
		but.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		but.setTextAppearance(context, android.R.style.TextAppearance_Small);
		but.setText(text);
		but.setOnClickListener(ocl);
		return but;
	}

	protected static ScrollView createSimpleScrollView(Context context) {
		ScrollView sv = new ScrollView(context);
		sv.setPadding(20, 20, 20, 20);
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		return sv;
	}

	protected static LinearLayout createSimpleLinearLayout(Context context) {
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		return ll;
	}

	protected static TextView createSimpleTextView(Context context) {
		TextView tv = new TextView(context);
		tv.setTextAppearance(context, android.R.style.TextAppearance_Small);
		tv.setPadding(20, 20, 20, 20);
		return tv;
	}

	protected static final DecimalFormat sDecimalFormat = new DecimalFormat("#.00");

	protected static String summarizeGetters(Object object) {
		if (object != null) {
			return summarizeGetters(object.getClass(), object);
		} else {
			return "null";
		}
	}

	protected static String summarizeGetters(Class<?> clazz, Object object) {
		StringBuilder sb = new StringBuilder();
		StringBuilder mySb=new StringBuilder();
		if (object != null) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
					if (method.getTypeParameters().length > 0)
						continue;
					if (!method.getDeclaringClass().equals(clazz))
						continue;
					try {
						Object result = method.invoke(object);
						if (result instanceof byte[]) {
							byte[] bytes = (byte[]) result;
							sb.append(method.getName()).append(": ")
									.append(Convert.bytesToHexString(bytes)).append("\n");
						} else if (result instanceof int[]) {
							int[] bytes = (int[]) result;
							sb.append(method.getName()).append(": ").append(Arrays.toString(bytes))
									.append("\n");
						} else if (result instanceof Double) {
							sb.append(method.getName()).append(": ")
									.append(sDecimalFormat.format(result)).append("\n");
						} else if (result instanceof Calendar) {
							Calendar cal = (Calendar) result;
							sb.append(method.getName()).append(": ").append(toString(cal))
									.append("\n");
						} else {
							sb.append(method.getName()).append(": ").append(result).append("\n");
						}
					} catch (Exception e) {
						L.e("summarizeGetters", object.getClass().getSimpleName(),
								method.getName(), e.getMessage());
					}
				}
			}
			if(object instanceof Heartrate.Data)
			{
//				 FileWriter fw = null;
//				 PrintWriter pw=null;
//				 File sdCard= Environment.getExternalStorageDirectory();
//                 File directory=new File(sdCard.getAbsolutePath()+"/HeartRateFiles");
//                 if(!directory.exists())
//                 	directory.mkdir();
//                 File file=new File(directory,"heartRate.txt");
//                 try {
//					 fw=new FileWriter(file,true);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                  pw=new PrintWriter(fw);
//                  Heartrate.Data data=(com.wahoofitness.connector.capabilities.Heartrate.Data)object;
//                  pw.println("time: "+data.getTime());
//                  pw.println("AvgHeartrate: " +data.getAvgHeartrate());
//                  pw.println("getAccumBeatCount: "+data.getAccumBeatCount());
//                  pw.println("Heartrate:"+data.getHeartrate());
//                  pw.println("HeartrateBpm: "+data.getHeartrateBpm());
//                  pw.close();
//                  try {
//					fw.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//             
				
			    Heartrate.Data data=(com.wahoofitness.connector.capabilities.Heartrate.Data)object;
			    sb.append(data.getHeartrateBpm());
			    mySb.append(data.getHeartrateBpm());
			    heartrateBpm=data.getHeartrateBpm();
			    if(heartCount==120)
			    {
			    	 //获取当前行为
			         String currenAction ="";
			         if(sitCount==walkCount
			        		 &&walkCount==runCount
			        		 &&runCount==downCount
			        		 &&downCount==upCount
			        		 &&sitCount==0)
			         {
			        	 currenAction ="";
			         }
			         else{
			         int[] a = new int[5]; 
			         a[0] = sitCount; 
			         a[1] = walkCount; 
			         a[2] = runCount; 
			         a[3] = downCount; 
			         a[4] = upCount; 
			         int maxindex=0;
			         int max = a[0]; 
			         for (int i = 1; i < 5; i++) { 
			             if (a[i] >max) { 
			                 max = a[i]; 
			                 maxindex=i;
			             } 
			         } 
			         switch (maxindex)
			         {
			         case 0:
			        	 currenAction="static";
			        	 break;
			         case 1:
			        	 currenAction="walk";
			        	 break;
			         case 2:
			        	 currenAction="run";
			        	 break;
			         case 3:
			        	 currenAction="down";
			        	 break;
			         case 4:
			        	 currenAction="up";
			        	 break;
			        	 default:
			        		 break;
			         }
			         }
			         //一分钟内的平均心率
			         double bmpeverymin=heartSum/heartCount;
			         
			         //时间日期
			         Date now = new Date(); 
//			         Calendar cal = Calendar.getInstance(); 
			         DateFormat d1 = DateFormat.getDateInstance();
			         String nowDate = d1.format(now);
			         DateFormat d2=DateFormat.getTimeInstance();
			         String nowTime = d2.format(now);
			         cv.clear();
			         cv.put("saveDate",nowDate);
			         cv.put("saveTime", nowTime);
			         cv.put("bmpeverymin", bmpeverymin);
			         cv.put("action", currenAction);
			         cv.put("objectId",userId);
			         db.insert(MyDBHelp.HAATABLE, null, cv);
			         heartCount=0;
			         heartSum=0;
			         sitCount=0;
			         walkCount=0;
			         runCount=0;
			         downCount=0;
			         upCount=0;
			         
			    }
			    else
			    {
			    	heartCount++;
			    	double bmp=Double.parseDouble(data.getHeartrate().toString().substring(10, 15));
			    	if(bmp<40)
			    	{
						//心率过低异常
			    		NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context);
						notificationBuilder.setSmallIcon(com.bbcc.mobilehealth.R.drawable.app_icon);
						notificationBuilder.setContentTitle("心率异常");
						notificationBuilder.setContentText("瞬时心率为"+bmp+"/分，"+"属于心率过低异常，请注意！");
						notificationBuilder.setTicker("心率过低异常");//第一次提示消息的时候显示在通知栏上
						notificationBuilder.setNumber(12);
						notificationBuilder.setAutoCancel(true);//自己维护通知的消失
						//构建一个Intent
						Intent resultIntent = new Intent(context,
						ResultActivity.class);
						//封装一个Intent
						PendingIntent resultPendingIntent = PendingIntent.getActivity(
								context, 0, resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
						// 设置通知主题的意图
						notificationBuilder.setContentIntent(resultPendingIntent);
						//获取通知管理器对象
						NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.notify(0, notificationBuilder.build());
			    	}
			    	else if(bmp>160)
			    	{
			    		if(!actionState.equals("run"))
			    		{
			    			if(!tempState.equals("run"))
			    			{
			    				//心率过高异常
			    				NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context);
								notificationBuilder.setSmallIcon(com.bbcc.mobilehealth.R.drawable.app_icon);
								notificationBuilder.setContentTitle("心率异常");
								notificationBuilder.setContentText("瞬时心率为"+bmp+"/分，"+"属于心率过高异常，请注意！");
								notificationBuilder.setTicker("心率过低异常");//第一次提示消息的时候显示在通知栏上
								notificationBuilder.setNumber(12);
								notificationBuilder.setAutoCancel(true);//自己维护通知的消失
								//构建一个Intent
								Intent resultIntent = new Intent(context,
								ResultActivity.class);
								//封装一个Intent
								PendingIntent resultPendingIntent = PendingIntent.getActivity(
										context, 0, resultIntent,
								PendingIntent.FLAG_UPDATE_CURRENT);
								// 设置通知主题的意图
								notificationBuilder.setContentIntent(resultPendingIntent);
								//获取通知管理器对象
								NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
								mNotificationManager.notify(0, notificationBuilder.build());
			    			}
			    		}
			    	}
			        heartSum+=bmp;
			        if(actionState.equals("static"))
			        {
			        sitCount++;
			        }
			        else if(actionState.equals("walk"))
			        {
			        walkCount++;
			        }
			        else if(actionState.equals("run"))
			        {
			        runCount++;
			        tempState="run";
			        handler.removeCallbacks(runnable);
			        handler.postDelayed(runnable, 60*1000);//tempstate 保存1分钟
			        }
			        else if(actionState.equals("down"))
			        {
			        downCount++;
			        }
			        else if(actionState.equals("up")){
			        upCount++;
			        }
			        else
			        {
			        	
			        }
			    }
				
			}
			
		} else {
			sb.append(actionState);
		}
		return mySb.toString().trim();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		db.close();
		Log.i("tag", "取消监听！");
		super.onDestroyView();
	}

	protected static String toString(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);
		return sdf.format(cal.getTime()) + " " + cal.getTimeZone().getDisplayName();
	}
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("tag", "onreceive:" + intent.getStringExtra("state"));
			// getActivity().getSupportFragmentManager().findFragmentById(1).;
			actionState=intent.getStringExtra("state");

		}
	};
}
