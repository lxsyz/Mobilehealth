 package com.bbcc.mobilehealth.fragment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.core.e;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.service.HardwareConnectorService;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.DeepSleepModel;
import com.bbcc.mobilehealth.util.HeartrateData;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.bbcc.mobilehealth.util.REMSleepModel;
import com.bbcc.mobilehealth.util.ShallowSleepModel;
import com.bbcc.mobilehealth.util.SleepModel;
import com.bbcc.mobilehealth.view.BluetoothDialog;
import com.bbcc.mobilehealth.view.CancelView;
import com.bbcc.mobilehealth.view.DrawChart;
import com.bbcc.mobilehealth.view.ProcessView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate.Data;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class HeartRateFragment extends HardwareConnectorFragment {
	private final Heartrate.Listener mHeartrateListener = new Heartrate.Listener() {

		@Override
		public void onHeartrateData(Data data) {
			if (moveState == 1) {
				startMove();
				processView.setVisibility(View.GONE);
				cancelView.setVisibility(View.VISIBLE);
				RotateAnimation animation = new RotateAnimation(0f, 45f,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(1000);// 设置动画持续时间
				animation.setFillAfter(true);
				cancelView.setAnimation(animation);
				animation.startNow();
				drawChart.setVisibility(View.VISIBLE);
				heartraTextView.setVisibility(View.VISIBLE);
			}
			Heartrate heartrate = (Heartrate) getCapability(CapabilityType.Heartrate);
			data = heartrate.getHeartrateData();
			int rate = data.getHeartrateBpm();
			drawChart.startInvalidate(rate);
			heartraTextView.setText(rate + " bmp");
			time.setToNow();
			if (tempMinute == -1) {
				tempYear = time.year;
				tempMonth = time.month + 1;
				tempDay = time.monthDay;
				tempHour = time.hour;
				tempMinute = time.minute;
				Log.v("sql", "TTTTTTTTTTTTTTT****************");
			}
			Log.v("sql", "time.minute-->" + time.minute);
			Log.v("sql", "tempTime.minute-->" + tempMinute);
			
			
			
			if (tempMinute == time.minute) {
				linkedList.add(rate);
				Log.v("sql", "tempTime.minute == time.minute   &&  add");
			} else {
				Log.v("sql", "sendmsg");
				// new RateUploadTask().execute();
				heartrateData = new HeartrateData(tempYear, tempMonth, tempDay,
						tempHour, tempMinute, new LinkedList<Integer>(
								linkedList));
				Message msg = Message.obtain();
				msg.what = Constant.HEARTATE_DB_SAVE;
				msg.obj = heartrateData;
				dbHandler.sendMessage(msg);
				tempYear = time.year;
				tempMonth = time.month + 1;
				tempDay = time.monthDay;
				tempHour = time.hour;
				tempMinute = time.minute;
				linkedList.clear();
				linkedList.add(rate);
			}
			if (linkedList2 != null) {
				Log.d("linkedlist2", linkedList2.size()+" ");
				if (linkedList2.size() == 3) {
					Message msg = Message.obtain();
					msg.what = Constant.SLEEPDATA_DB_SAVE;
					dbHandler.sendMessage(msg);
					
					//linkedList2.clear();
					
				}
			}
		}

		@Override
		public void onHeartrateDataReset() {
		}
	};
	private MyDBHelp myDBHelp = null;
	private ContentValues contentValues = null;
	private LinkedList<Integer> linkedList = null;
	private LinkedList<SleepModel> linkedList2 = new LinkedList<SleepModel>();
	private Time time = null;
	private int tempYear = -1;
	private int tempMonth = -1;
	private int tempDay = -1;
	private int tempHour = -1;
	private int tempMinute = -1;
	
	private HeartrateData heartrateData;
	private View rootView;
	private List<ConnectionParams> mDiscoveredConnectionParams = new ArrayList<ConnectionParams>();
	private static final Logger L = new Logger(DiscoverFragment.class);

	private final Set<ConnectionParams> mSavedConnectionParams = new HashSet<ConnectionParams>();
	private WindowManager wm;
	private LinearLayout textViewLayout;
	private LinearLayout circleLayout;
	private ProcessView processView;
	private TextView titleTextView;
	private TextView heartraTextView;
	private DrawChart drawChart;
	private CancelView cancelView;
	private RelativeLayout mainLayout;
	private Button button;
	private RelativeLayout.LayoutParams layoutParams;
	private int rules = RelativeLayout.CENTER_IN_PARENT;
	private int xMove = 300;
	private int yMove = -300;
	private int moveState = 1;

	private int circleLayout_width = 0;
	private int circleLayout_height = 0;
	private int circleLayout_top = 0;
	private int circleLayout_left = 0;
	private int l = 0;
	private int t = 0;
	private int r = 0;
	private int b = 0;

	private boolean discovering = false;
	private ConnectionParams mConnectionParams = null;
	private SensorConnection sensorConnection;
	private Handler dbHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.HEARTATE_DB_SAVE:
				Log.v("sql", "handler begin");
				HeartrateData hData = (HeartrateData) msg.obj;
				String phoneNumber = "18720083869";
				int averageRate = 0;
				int state = 0;
				String dateTime = hData.getYear() + "-" + hData.getMonth()
						+ "-" + hData.getDay() + " " + hData.getHour() + ":"
						+ hData.getMinute();
				Log.v("sql", "hData.getLinkedList().size()"
						+ hData.getLinkedList().size());
				if (hData.getLinkedList().size() != 0) {
					Iterator<Integer> iterator = hData.getLinkedList()
							.iterator();
					int i = 0;
					int sumRate = 0;
					while (iterator.hasNext()) {
						i++;
						sumRate += iterator.next();
					}
					averageRate = sumRate / i;
					if (averageRate < 30 || averageRate > 150) {
						state = 1;
					}
					
					//添加到睡眠list中
					SleepModel model = new SleepModel();
					model.setTime(dateTime);
					model.setRate(averageRate);
					linkedList2.add(model);
					Log.d("linkedlist2", linkedList2.size()+" ");
					String insertString = "insert into heart_rate(phoneNumber,time,rate,state) values ("
							+ phoneNumber
							+ ","
							+ "'"
							+ dateTime
							+ "'"
							+ ","
							+ averageRate + "," + state + ");";
					SQLiteDatabase db = myDBHelp.getWritableDatabase();
					db.execSQL(insertString);
					Log.v("sql", insertString);
				}

				break;
			case Constant.SLEEPDATA_DB_SAVE:
				Log.d("linklist2", linkedList2.size()+" ");
				phoneNumber = "18720083869";
				//int result = 1;
				int result = heartRate2Sleep(linkedList2);
				String insertString = "";
				SQLiteDatabase db = myDBHelp.getWritableDatabase();
				switch (result) {
					case 1:
						insertString = "insert into SLEEP_DEEP(phoneNumber,startTime,endTime) values ("
								+ phoneNumber
								+ ","
								+ "'"
								+ linkedList2.get(0).getTime()
								+ "'"
								+ ","
								+ "'"
								+ linkedList2.get(2).getTime()
								+ "'"
								+ ");";
						Log.d("linklist2", insertString+" ");
						db.execSQL(insertString);
						break;
					case 2:
						insertString = "insert into SLEEP(phoneNumber,startTime,endTime) values ("
								+ phoneNumber
								+ ","
								+ "'"
								+ linkedList2.get(0).getTime()
								+ "'"
								+ ","
								+ "'"
								+ linkedList2.get(2).getTime()
								+ "'"
								+ ");"; 
						Log.d("linklist2", insertString+" ");
						db.execSQL(insertString);
						break;
					case 3:
//						insertString = "insert into sleep_rem(phoneNumber,startTime,endTime) values ("
//								+ phoneNumber
//								+ ","
//								+ "'"
//								+ linkedList2.get(0).getTime()
//								+ "'"
//								+ ","
//								+ "'"
//								+ linkedList2.get(2).getTime()
//								+ "'"
//								+ ");";
//						Log.d("linklist2", insertString+" ");
//						db.execSQL(insertString);
						break;
					default:
						break;
				} 
				
				MyDBHelp.readDB2Dir(getActivity());
				linkedList2.clear();
				break;
			default:
				break;
			}

		};
	};
	private Handler listenerHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.HEARTATE_CONNECT_SUCCESS:
				getHeartrateCap().addListener(mHeartrateListener);
				break;

			default:
				break;
			}

		};

	};

	@Override
	public void onAttach(Activity activity) {
		Log.v("tag", "HeartRate:onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("tag", "HeartRates:onCreate");
		myDBHelp = new MyDBHelp(getActivity());
		linkedList = new LinkedList<Integer>();
		time = new Time();
		mSavedConnectionParams.clear();
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("PersistentConnectionParams", 0);
		for (Object entry : sharedPreferences.getAll().values()) {
			ConnectionParams params = ConnectionParams
					.fromString((String) entry);
			mSavedConnectionParams.add(params);
		}
		L.i("onCreate", mSavedConnectionParams.size(),
				"saved ConnectionParams loaded");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("tag", "HeartRate:onCreateView");
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.frag_heartrate, container,
					false);
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.v("tag", "HeartRate:onActivityCreate");
		LayoutInflater inflater = getActivity().getLayoutInflater();

		circleLayout = (LinearLayout) inflater.inflate(
				R.layout.frag_heartrate_centeritem, null);
		textViewLayout = (LinearLayout) circleLayout
				.findViewById(R.id.heartrate_circle_textview);
		processView = (ProcessView) circleLayout
				.findViewById(R.id.heartrate_circle_processview);
		titleTextView = (TextView) getActivity().findViewById(
				R.id.heartrate_title_textview);
		heartraTextView = (TextView) getActivity().findViewById(
				R.id.heartrate_show_text);
		cancelView = (CancelView) circleLayout
				.findViewById(R.id.heartrate_circle_cancelview);
		mainLayout = (RelativeLayout) getActivity().findViewById(
				R.id.frag_heartrate_layout);
		drawChart = (DrawChart) getActivity().findViewById(
				R.id.heartrate_circle_drawchart);
		layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button = (Button) getActivity().findViewById(R.id.upload);
		layoutParams.addRule(rules);
		circleLayout.setLayoutParams(layoutParams);
		mainLayout.addView(circleLayout);
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
		drawChart.setCircleLayout(circleLayout);
		drawChart.setFragLayout(mainLayout);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		circleLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isBluetoothOn()) {
					BluetoothDialog dialog = new BluetoothDialog(getActivity(),
							R.style.dialog_style);
					dialog.show();
				}
				if (moveState == 1) {
					textViewLayout.setVisibility(View.GONE);
					processView.setVisibility(View.VISIBLE);
					enableDiscovery(true);
				} else {
					getHeartrateCap().removeListener(mHeartrateListener);
					disconnectSensor(mConnectionParams);
					enableDiscovery(false);
					startRevsMove();

				}

			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	private void refresh() {
		Log.v("tag", "refresh()");
		discovering = isDiscovering();
		Log.v("tag", "discovering=" + discovering);
		mDiscoveredConnectionParams.clear();
		mDiscoveredConnectionParams.addAll(mSavedConnectionParams);

		for (SensorConnection connectedDevices : getSensorConnections()) {
			ConnectionParams connectedParams = connectedDevices
					.getConnectionParams();
			Log.v("tag", " connectedDevices.getConnectionParams()1");
			if (!mDiscoveredConnectionParams.contains(connectedParams)) {
				mDiscoveredConnectionParams.add(connectedParams);
				Log.v("tag",
						"mDiscoveredConnectionParams.add(connectedParams)-->"
								+ connectedParams);
			}
		}
		for (ConnectionParams discoveredParams : getDiscoveredConnectionParams()) {
			Log.v("tag", "getDiscoveredConnectionParams()");
			if (!mDiscoveredConnectionParams.contains(discoveredParams)) {
				mDiscoveredConnectionParams.add(discoveredParams);
				Log.v("tag",
						"mDiscoveredConnectionParams.add(connectedParams)2-->"
								+ discoveredParams);
			}
		}

		Collections.sort(mDiscoveredConnectionParams,
				new Comparator<ConnectionParams>() {

					@Override
					public int compare(ConnectionParams lhs,
							ConnectionParams rhs) {
						return lhs.getName().compareTo(rhs.getName());
					}
				});

	}

	public void saveConnectionParams() {

		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("PersistentConnectionParams", 0);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		int count = 0;
		for (ConnectionParams params : mSavedConnectionParams) {
			editor.putString("" + count++, params.serialize());
		}
		editor.commit();
		L.i("onDestroy", mSavedConnectionParams.size(),
				"ConnectionParams saved");
	}

	private boolean isBluetoothOn() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			// 如果蓝牙设备未开启
			if (!adapter.isEnabled()) {
				return false;
			}
			return true;
		}
		return false;

	}

	public ConnectionParams getItem(int position) {
		return mDiscoveredConnectionParams.get(position);
	}

	private void setXYMove() {
		wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		int screenHeigth = wm.getDefaultDisplay().getHeight();
		int screenWidth = wm.getDefaultDisplay().getWidth();
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		titleTextView.measure(w, h);
		circleLayout.measure(w, h);
		int titleHeight = titleTextView.getMeasuredHeight();
		int titleWidth = titleTextView.getMeasuredWidth();
		Rect rect = new Rect();
		getActivity().getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		int left = circleLayout.getLeft();
		int top = circleLayout.getTop();
		yMove = -(top - statusBarHeight - titleHeight);
		xMove = screenWidth
				- (int) (100 + circleLayout.getMeasuredWidth() * 0.3) - left;
	}

	private void startMove() {
		setXYMove();
		moveState = 2;
		// circleLayout.setBackgroundColor(Color.TRANSPARENT);
		// textViewLayout.setVisibility(View.GONE);
		// circleImageView.setVisibility(View.VISIBLE);
		Animation mScaleAnimation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f);
		mScaleAnimation.setDuration(1000);

		Animation mTranslateAnimation = new TranslateAnimation(0, xMove, 0,
				yMove);
		mTranslateAnimation.setDuration(1000);
		AnimationSet mAnimationSet = new AnimationSet(true);
		mAnimationSet.addAnimation(mScaleAnimation);
		mAnimationSet.setFillAfter(true);
		mAnimationSet.setInterpolator(new DecelerateInterpolator());
		mAnimationSet.addAnimation(mTranslateAnimation);
		circleLayout.startAnimation(mAnimationSet);
		mTranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {

				circleLayout_left = circleLayout.getLeft();
				circleLayout_top = circleLayout.getTop();
				circleLayout_width = circleLayout.getWidth();
				circleLayout_height = circleLayout.getHeight();
				circleLayout.clearAnimation();
				l = circleLayout_left + xMove;
				t = circleLayout_top + yMove;
				r = (int) (circleLayout_left + xMove + circleLayout_width * 0.3);
				b = (int) (circleLayout_top + yMove + circleLayout_height * 0.3);
				layoutParams.removeRule(rules);
				layoutParams.height = b - t;
				layoutParams.width = r - l;
				layoutParams.leftMargin = l;
				layoutParams.topMargin = t;
				circleLayout.setLayoutParams(layoutParams);
			}
		});
	}

	private void startRevsMove() {
		xMove = -xMove;
		yMove = -yMove;
		moveState = 1;
		cancelView.clearAnimation();
		cancelView.setVisibility(View.GONE);
		Animation mScaleAnimation = new ScaleAnimation(1f, (float) (10f / 3),
				1f, (float) (10f / 3));
		mScaleAnimation.setDuration(1000);
		mScaleAnimation.setFillAfter(true);

		Animation mTranslateAnimation = new TranslateAnimation(0, xMove, 0,
				yMove);
		mTranslateAnimation.setDuration(1000);
		AnimationSet mAnimationSet = new AnimationSet(false);
		mAnimationSet.addAnimation(mScaleAnimation);
		mAnimationSet.setFillAfter(true);
		mAnimationSet.setInterpolator(new BounceInterpolator());
		mAnimationSet.addAnimation(mTranslateAnimation);
		circleLayout.startAnimation(mAnimationSet);
		mTranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				drawChart.setVisibility(View.GONE);
				heartraTextView.setVisibility(View.GONE);
				int left = circleLayout.getLeft();
				int top = circleLayout.getTop();
				int right = circleLayout.getRight();
				int bottom = circleLayout.getBottom();
				circleLayout.clearAnimation();
				layoutParams.width = LayoutParams.WRAP_CONTENT;
				layoutParams.height = LayoutParams.WRAP_CONTENT;
				layoutParams.setMargins(left, top, right, bottom);
				layoutParams.addRule(rules);
				circleLayout.setLayoutParams(layoutParams);
				textViewLayout.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("tag", "HeartRate:onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("tag", "HeartRate:onPause");
		super.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.e("tag", "HeartRate:onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.e("tag", "HeartRate:onStop");
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("tag", "HeartRate:onResum");
		super.onResume();

	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {
		refresh();
		if (discovering) {
			refresh();
			Toast.makeText(getActivity(), "发现MIO手环，正在连接请稍后...",
					Toast.LENGTH_SHORT).show();
			// params = getItem(0);
			this.mConnectionParams = getItem(0);
			Log.v("params", "param11" + this.mConnectionParams.toString());
			mSavedConnectionParams.add(params);
			sensorConnection = getSensorConnection(params);
			refresh();
			sensorConnection = connectSensor(params);

		} else {
			Toast.makeText(getActivity(), "抱歉，未发现MIO手环！！！", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {
		refresh();
		Toast.makeText(getActivity(), "onDiscoveredDeviceLost",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onHardwareConnectorServiceConnected(
			HardwareConnectorService hardwareConnectorService) {
		refresh();

	}

	@Override
	public void onSensorConnectionStateChanged(
			SensorConnection sensorConnection, SensorConnectionState state) {
		Toast.makeText(getActivity(), "onSensorConnectionStateChanged",
				Toast.LENGTH_SHORT).show();
		Log.v("params", "333sensorConnection.getConnectionParams()="
				+ sensorConnection);
		Log.v("params", "333sensorConnection.getConnectionParams()="
				+ getSensorConnection(mConnectionParams));
		Log.v("params", "333sensorConnection.getConnectionParams()="
				+ (getSensorConnection(mConnectionParams) == sensorConnection));
		Log.v("params", "333sensorConnection.getConnectionParams()="
				+ sensorConnection.getConnectionParams());
		Log.v("params", "333mConnectionParams=" + mConnectionParams);
		refresh();
		switch (sensorConnection.getConnectionState()) {
		case CONNECTED:
			Toast.makeText(getActivity(), "CONNECTED", Toast.LENGTH_SHORT)
					.show();
			// getHeartrateCap().addListener(mHeartrateListener);
			Message msg = Message.obtain();
			msg.what = Constant.HEARTATE_CONNECT_SUCCESS;
			listenerHandler.sendEmptyMessageDelayed(msg.what, 2500);
			break;
		case CONNECTING:
			Toast.makeText(getActivity(), "CONNECTING", Toast.LENGTH_SHORT)
					.show();
			break;
		case DISCONNECTED:
			Toast.makeText(getActivity(), "DISCONNECTED", Toast.LENGTH_SHORT)
					.show();
			break;
		case DISCONNECTING:
			Toast.makeText(getActivity(), "DISCONNECTING", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();

			break;

		}
	}

	@Override
	public void onFirmwareUpdateRequired(SensorConnection sensorConnection,
			String currentVersionNumber, String recommendedVersion) {
		refresh();

	}

	public void onNewCapabilityDetected(SensorConnection sensorConnection,
			CapabilityType capabilityType) {
		Toast.makeText(getActivity(), "onNewCapabilityDetected",
				Toast.LENGTH_SHORT).show();
		// if (isConnected) {
		// Message msg = Message.obtain();
		// msg.what = Constant.HEARTATE_CONNECT_SUCCESS;
		// handler.sendEmptyMessageDelayed(msg.what, 2500);
		// }
	}

	private Heartrate getHeartrateCap() {
		return (Heartrate) getCapability(CapabilityType.Heartrate);
	}

	protected Capability getCapability(CapabilityType capabilityType) {
		sensorConnection = getSensorConnection(mConnectionParams);
		if (sensorConnection != null) {
			Log.v("params", "111sensorConnection not null");
			if (sensorConnection.getCurrentCapability(capabilityType) != null) {
				Log.v("params",
						"222sensorConnection.getCurrentCapability(capabilityType) not null");
				return sensorConnection.getCurrentCapability(capabilityType);
			} else {
				Log.v("params",
						"222sensorConnection.getCurrentCapability(capabilityType) null");
				return null;
			}
		} else {
			Log.v("params", "111sensorConnection not null");
			return null;
		}
	}

	protected SensorConnection getSensorConnection() {
		return getSensorConnection(mConnectionParams);
	}

	/*
	 * 睡眠转换成心率
	 * @return 
	 * 1    深度睡眠
	 * 2    浅度睡眠
	 * 3  REM
	 */
	private int heartRate2Sleep(LinkedList<SleepModel> link) {
		double avg = (link.get(0).getRate() + link.get(1).getRate()+link.get(2).getRate()) / 3.0;
		double std = Math.sqrt(((link.get(0).getRate()-avg)*(link.get(1).getRate()-avg)+(link.get(2).getRate()-avg)*(link.get(1).getRate()-avg)+(link.get(2).getRate()-avg)*(link.get(2).getRate()-avg))/3);
		
		//将每一组的标准差,平均值，和设定好的阈值相对比进而判断睡眠分期
		int MIN_std=1;
		int MIN_avg=61;
		int MAX_std=2;
		int MAX_avg=64;
		
		String startTime=link.get(0).getTime(),endTime=link.get(2).getTime();
		
		if (avg < MIN_avg && std < MIN_std) {
			
			//System.out.println("第" + (j + 1) + "组正处于深度睡眠阶段");
			return 1;
			
		}
		if (std > MAX_std && avg < MAX_avg) {
			return 2;
		}
		if (avg > MAX_avg) {
			return 3;
		}
		
		return 0;
	}
}
