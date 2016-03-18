package com.bbcc.mobilehealth.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.TrustManagerFactorySpi;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.service.HardwareConnectorService;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.SystemBarTintManager;
import com.bbcc.mobilehealth.view.BluetoothDialog;
import com.bbcc.mobilehealth.view.BluetoothDialog.BluetoothDialogListener;
import com.bbcc.mobilehealth.view.DrawChart;
import com.bbcc.mobilehealth.view.ProcessView;
import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate.Data;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.data.HeartrateData;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class HeartRateFragment extends HardwareConnectorFragment {
	// private FragmentManager fragmentManager;
	// private FragmentTransaction transaction;
	// private ViewPager fragheartrate;
	// private FragmentPagerAdapter fa;
	private final Heartrate.Listener mHeartrateListener = new Heartrate.Listener() {

		@Override
		public void onHeartrateData(Data data) {
			if (moveState==1) {
				startMove();
				processView.setVisibility(View.GONE);
				drawChart.setVisibility(View.VISIBLE);
			}
			Heartrate heartrate = (Heartrate) getCapability(CapabilityType.Heartrate);
			data = heartrate.getHeartrateData();
			int rate = data.getHeartrateBpm();
			drawChart.startInvalidate(rate);
			heartraTextView.setText(rate+" bmp");
//			circleLayout.clearAnimation();
//			circleLayout.layout(circleLayout_left + xMove, circleLayout_top + yMove, (int) (circleLayout_left
//					+ xMove + circleLayout_width * 0.3),
//					(int) (circleLayout_top + yMove + circleLayout_height * 0.3));
		}

		@Override
		public void onHeartrateDataReset() {
		}
	};
	private View rootView;
	private List<ConnectionParams> mDiscoveredConnectionParams = new ArrayList<ConnectionParams>();
	private static final Logger L = new Logger(DiscoverFragment.class);

	private final Set<ConnectionParams> mSavedConnectionParams = new HashSet<ConnectionParams>();
	private WindowManager wm;
	private LinearLayout textViewLayout;
	private LinearLayout circleLayout;
	private ProcessView processView;
	private TextView circleTextView;
	private TextView titleTextView;
	private TextView heartraTextView;
	
	private ImageView circleImageView;
	private DrawChart drawChart;
	private RelativeLayout mainLayout;
	private int xMove = 300;
	private int yMove = -300;
	private int moveState=1;
	private int circleLayout_width=0;
	private int circleLayout_height=0;
	private int circleLayout_top=0;
	private int circleLayout_left=0;
	private boolean isBluetoothOpen = false;
	private boolean discovering = false;
	private boolean isConnected = false;
	private ConnectionParams mConnectionParams = null;
	private SensorConnection sensorConnection;
	private ActionBar mActionBar;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.HEARTATE_CONNECT_SUCCESS:
				// enableDiscovery(true);
				// refresh();
				// sensorConnection = getSensorConnection(mConnectionParams);
				// refresh();
				// sensorConnection=connectSensor(mConnectionParams);

				// Heartrate heartrate = (Heartrate)
				// getCapability(CapabilityType.Heartrate);
				// Heartrate.Data data = heartrate.getHeartrateData();
				// int rate = data.getHeartrateBpm();
				// circleTextView.setText("心率" + rate);
				// processView.setVisibility(View.GONE);
				// circleTextView.setVisibility(View.VISIBLE);
				
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
		mSavedConnectionParams.clear();
		// begin=findViewById(R.id.begin);
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
		// mActionBar=getActivity().getActionBar();
		// mActionBar.show();
		circleLayout = (LinearLayout) getActivity().findViewById(
				R.id.heartrate_circle_main);
		textViewLayout = (LinearLayout) getActivity().findViewById(
				R.id.heartrate_circle_textview);
		circleTextView = (TextView) getActivity().findViewById(
				R.id.heartrate_circle_textview2);
		titleTextView = (TextView) getActivity().findViewById(
				R.id.heartrate_title_textview);
		heartraTextView = (TextView) getActivity().findViewById(
				R.id.heartrate_show_text);
		processView = (ProcessView) getActivity().findViewById(
				R.id.heartrate_circle_processview);
		mainLayout=(RelativeLayout)getActivity().findViewById(R.id.frag_heartrate_layout);
//		circleImageView = (ImageView) getActivity().findViewById(
//				R.id.heartrate_circle_imageview);
		drawChart=(DrawChart) getActivity().findViewById(
				R.id.heartrate_circle_drawchart);
		WindowManager wm = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
//		drawChart=new DrawChart(getActivity());
//		drawChart.layout(0, 220, screenWidth, 520);
		drawChart.setCircleLayout(circleLayout);
		drawChart.setFragLayout(mainLayout);
		circleLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isBluetoothOn()) {
					BluetoothDialog dialog = new BluetoothDialog(getActivity(),
							R.style.dialog_style);
					dialog.show();
//					// dialog.setBluetoothDialogListener(new
//					// BluetoothDialogListener() {
//					//
//					// @Override
//					// public void callBack(boolean b) {
//					// isBluetoothOpen=b;
//					// }
//					// });
//					// AlertDialog.Builder dialog=new
//					// AlertDialog.Builder(getActivity());
//					// AlertDialog alertDialog=dialog.create();
//					// alertDialog.show();
//					// Window window=alertDialog.getWindow();
//					// window.setContentView(R.layout.bluetooth_dialog);
				}
				if (moveState==1) {
					textViewLayout.setVisibility(View.GONE);
					processView.setVisibility(View.VISIBLE);
					enableDiscovery(true);
//					startMove();
//					drawChart.setVisibility(View.VISIBLE);
				}else {
					startRevsMove();
					
				}
				// startMove();
				// refresh();

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

		// Collection<CapabilityType>
		// heartrate=sensorConnection.getCurrentCapabilities();
		// Log.v("tag",
		// "heartrate.toString()***************"+heartrate.toString());

		// refresh();

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
		Log.e("tag", "top" + "***" + top);
		Log.e("tag", "statusBarHeight" + "***" + statusBarHeight);
		Log.e("tag", "titleHeight" + "***" + titleHeight);
		Log.e("tag", "yMove" + "***" + yMove);
		xMove = screenWidth
				- (int) (100 + circleLayout.getMeasuredWidth() * 0.3) - left;
		Log.e("tag", "screenWidth" + "***" + screenWidth);
		Log.e("tag",
				"circleLayout.getWidth()" + "***" + circleLayout.getWidth());
		Log.e("tag",
				"circleLayout.getMeasuredWidth()" + "***"
						+ circleLayout.getMeasuredWidth());
		Log.e("tag", "left" + "***" + left);
		Log.e("tag", "xMove" + "***" + xMove);
	}

	private void startMove() {
		setXYMove();
		moveState=2;
//		circleLayout.setBackgroundColor(Color.TRANSPARENT);
//		textViewLayout.setVisibility(View.GONE);
//		circleImageView.setVisibility(View.VISIBLE);
		Animation mScaleAnimation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f);
		mScaleAnimation.setDuration(1000);
//		mScaleAnimation.setFillAfter(true);

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
				circleLayout.layout(circleLayout_left + xMove, circleLayout_top + yMove, (int) (circleLayout_left
						+ xMove + circleLayout_width * 0.3),
						(int) (circleLayout_top + yMove + circleLayout_height * 0.3));
//				RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//				layoutParams.setMargins(circleLayout_left + xMove, circleLayout_top + yMove, (int) (circleLayout_left
//						+ xMove + circleLayout_width * 0.3), (int) (circleLayout_top + yMove + circleLayout_height * 0.3));
//				circleLayout.setLayoutParams(layoutParams);
//				circleLayout.setLayoutParams(new RelativeLayout.LayoutParams((circleLayout_left + xMove)-(int) (circleLayout_left
//						+ xMove + circleLayout_width * 0.3), (circleLayout_top + yMove)-(int) (circleLayout_top + yMove + circleLayout_height * 0.3)));
//				drawChart.setL(circleLayout_left + xMove);
//				drawChart.setT(circleLayout_top + yMove);
//				drawChart.setR((int) (circleLayout_left
//						+ xMove + circleLayout_width * 0.3));
//				drawChart.setB((int) (circleLayout_top + yMove + circleLayout_height * 0.3));
				// circleLayout.setBackground(getResources().getDrawable(R.drawable.hearhrate_cancel));
//				circleLayout.clearAnimation();
			}
		});
	}

	private void startRevsMove() {
		xMove=-xMove;
		yMove=-yMove;
		moveState=1;
		Animation mScaleAnimation = new ScaleAnimation(1f, (float) (10f/3), 1f,(float) (10f/3));
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

				int left = circleLayout.getLeft();
				int top = circleLayout.getTop();
				int width = circleLayout.getWidth();
				int height = circleLayout.getHeight();
//				circleLayout.clearAnimation();
				circleLayout.setBackgroundColor(Color.TRANSPARENT);
//				circleImageView.setVisibility(View.GONE);
				circleLayout.layout(left+xMove , top+yMove, (int) (left
						+xMove + width * (10f/3)), (int) (top + yMove + height * (10f/3)) );
				// circleLayout.setBackground(getResources().getDrawable(R.drawable.hearhrate_cancel));
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
		// refresh();
		// Toast.makeText(getActivity(), "onHardwareConnectorServiceConnected",
		// Toast.LENGTH_SHORT).show();
		// if (params != null) {
		// if (sensorConnection != null) {
		//
		// } else {
		//
		// if (getDiscoveredConnectionParams().contains(params)) {
		// Toast.makeText(getActivity(), "Discovered",
		// Toast.LENGTH_SHORT).show();
		// isConnected = true;
		// } else {
		// Toast.makeText(getActivity(), "DISCONNECTED2",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// }
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
			isConnected = true;
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
		// refresh();
		// refresh();
		// if (isConnected) {
		// if (this.params == null) {
		// Log.v("params222", this.params.toString());
		// } else {
		// Heartrate heartrate = (Heartrate) getCapability(
		// CapabilityType.Heartrate, this.params);
		// Heartrate.Data data = heartrate.getHeartrateData();
		// int rate = data.getHeartrateBpm();
		// processView.setVisibility(View.GONE);
		// circleTextView.setText("心率=" + rate);
		// circleTextView.setVisibility(View.VISIBLE);
		// }
		// }
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
		if (isConnected) {
			Message msg = Message.obtain();
			msg.what = Constant.HEARTATE_CONNECT_SUCCESS;
			handler.sendEmptyMessageDelayed(msg.what, 2500);
		}
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
}
