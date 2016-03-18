package com.bbcc.mobilehealth.fragment;

import com.bbcc.mobilehealth.R;

import com.bbcc.mobilehealth.service.StepService;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.view.CircleProgressBar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActionFragment extends Fragment implements OnClickListener {

	private TextView state;
	private TextView stepCount;
	private Button start;
	private Button stop;
	private Intent intent;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private boolean isClicked;

	private int allStepCount = 0;

	private TextView allStepCountTv;

	// private MyReceiver receiver;

	private String userId;
	private String contact;
	private String name;

	private TextView carlorieTextView = null;
	private TextView kilometerTextView = null;
	private TextView activetimeTextView = null;
	private TextView aimsTextView = null;
	private TextView todaystepTextView = null;
	private CircleProgressBar circleProgressBar = null;
	private ImageView startImageView = null;
	private ImageView pauseImageView = null;
	public static Handler handler;
	public boolean isStepServiceStart=false;

	// public class MyReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// Log.e("tag", "onreceive:" + intent.getStringExtra("state"));
	// state.setText(intent.getStringExtra("state") + " ...test");
	// stepCount.setText("" + intent.getIntExtra("stepCount", 0));
	// allStepCountTv.setText("" + intent.getIntExtra("stepCount", 0));
	//
	// }
	// };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onCreateView");

		return inflater.inflate(R.layout.frag_action, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.e("tag", "action:onAttach");
		// TODO Auto-generated method stub

		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.e("tag", "action:onActivityCreate");
		carlorieTextView = (TextView) getActivity().findViewById(
				R.id.action_item_calorie);
		kilometerTextView = (TextView) getActivity().findViewById(
				R.id.action_item_kilometer);
		activetimeTextView = (TextView) getActivity().findViewById(
				R.id.action_item_activetime);
		todaystepTextView = (TextView) getActivity().findViewById(
				R.id.action_todaystep);
		aimsTextView = (TextView) getActivity().findViewById(
				R.id.action_aims);
		circleProgressBar = (CircleProgressBar) getActivity().findViewById(
				R.id.action_circleprogressbar);
		startImageView = (ImageView) getActivity().findViewById(
				R.id.action_view_start);
		pauseImageView = (ImageView) getActivity().findViewById(
				R.id.action_view_pause);
		startImageView.setOnClickListener(this);
		pauseImageView.setOnClickListener(this);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.STEP_UPDATE:
					circleProgressBar.setProgress(msg.arg1);
					todaystepTextView.setText(msg.arg1+"");
					carlorieTextView.setText((int)(msg.arg1/50)+"");
					kilometerTextView.setText((int)((msg.arg1*0.7)/1000)+"");
					break;

				default:
					break;
				}

			};
		};
		// state = (TextView) getActivity().findViewById(R.id.action_state);
		// stepCount=(TextView)
		// getActivity().findViewById(R.id.action_stepCount);
		// start = (Button) getActivity().findViewById(R.id.action_start);
		// stop = (Button) getActivity().findViewById(R.id.action_stop);
		//
		// allStepCountTv=(TextView)
		// getActivity().findViewById(R.id.action_allStepCount);
		// settings = getActivity().getSharedPreferences("setting", 0);
		// editor = settings.edit();
		// isClicked = settings.getBoolean("isClicked", true);

		// allStepCount=settings.getInt("allStepCount", 0);
		// allSitCout=settings.getLong("allSitCout", 0);
		// allWalkCount=settings.getLong("allWalkCount", 0);
		// allRunCount=settings.getLong("allRunCount", 0);
		// allDownCount=settings.getLong("allDownCount", 0);
		// allUpCount=settings.getLong("allUpCount", 0);
		// if(isClicked)
		// {
		// stop.setVisibility(View.GONE);
		// start.setVisibility(View.VISIBLE);
		//
		// }
		// else
		// {
		// start.setVisibility(View.GONE);
		// stop.setVisibility(View.VISIBLE);
		// }
		// start.setOnClickListener(this);
		// stop.setOnClickListener(this);
		//

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onCreate");

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		editor.putInt("allStepCount", allStepCount);
		editor.commit();
		Log.e("tag", "action:onDestroy");
		// getActivity().unregisterReceiver(receiver);

		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onPause");
		super.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onStart");
		intent = new Intent(getActivity(), StepService.class);
		if (isStepServiceStart) {
			startImageView.setVisibility(View.GONE);
			pauseImageView.setVisibility(View.VISIBLE);
		}
		// receiver = new MyReceiver();
		// IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		// filter.addAction("com.bbcc.mobilehealth.service.UPDATE");
		// getActivity().registerReceiver(receiver, filter);
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onStop");
		// if (receiver != null) {
		// getActivity().unregisterReceiver(receiver);
		// }
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("tag", "action:onResum");
		super.onResume();

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.action_view_start:
			startImageView.setVisibility(View.GONE);
			pauseImageView.setVisibility(View.VISIBLE);
			setupService();
			isStepServiceStart=true;
			Log.v("step", "Service creat");
			Toast.makeText(getActivity(), "已开启计步器", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_view_pause:
			pauseImageView.setVisibility(View.GONE);
			startImageView.setVisibility(View.VISIBLE);
			Intent intent = new Intent(getActivity(), StepService.class);
			getActivity().stopService(intent);
			isStepServiceStart=false;
			break;
		}
	}

	private void setupService() {
		Intent intent = new Intent(getActivity(), StepService.class);
		// bindService(intent, conn, Context.BIND_AUTO_CREATE);
		getActivity().startService(intent);
	}
}
