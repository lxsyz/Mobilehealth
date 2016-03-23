package com.bbcc.mobilehealth.fragment;

import java.util.Calendar;

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
import android.graphics.AvoidXfermode.Mode;
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

	private SharedPreferences argsSPF;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("tag", "action:onCreate");
		super.onCreate(savedInstanceState);
		argsSPF=getActivity().getSharedPreferences("step_data", getActivity().MODE_PRIVATE);
		editor=argsSPF.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("tag", "action:onCreateView");

		return inflater.inflate(R.layout.frag_action, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.e("tag", "action:onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
		aimsTextView = (TextView) getActivity().findViewById(R.id.action_aims);
		circleProgressBar = (CircleProgressBar) getActivity().findViewById(
				R.id.action_circleprogressbar);
		startImageView = (ImageView) getActivity().findViewById(
				R.id.action_view_start);
		pauseImageView = (ImageView) getActivity().findViewById(
				R.id.action_view_pause);
		startImageView.setOnClickListener(this);
		pauseImageView.setOnClickListener(this);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		String editorKey=String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
		circleProgressBar.setProgress(argsSPF.getInt(editorKey, 0));
		todaystepTextView.setText(String.valueOf(argsSPF.getInt(editorKey, 0)));
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.STEP_UPDATE:
					circleProgressBar.setProgress(msg.arg1);
					todaystepTextView.setText(String.valueOf(msg.arg1));
					break;

				default:
					break;
				}

			};
		};
	}

	@Override
	public void onStart() {
		Log.e("tag", "action:onStart");
		if (argsSPF.getBoolean("isStepServiceStart", false)) {
			startImageView.setVisibility(View.GONE);
			pauseImageView.setVisibility(View.VISIBLE);
		}
		super.onStart();
	}

	@Override
	public void onDestroy() {
		editor.putInt("allStepCount", allStepCount);
		editor.commit();
		Log.e("tag", "action:onDestroy");

		super.onDestroy();
	}

	@Override
	public void onPause() {
		Log.e("tag", "action:onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.e("tag", "action:onStop");
		super.onStop();
	}

	@Override
	public void onResume() {
		Log.e("tag", "action:onResum");
		super.onResume();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.action_view_start:
			startImageView.setVisibility(View.GONE);
			pauseImageView.setVisibility(View.VISIBLE);
			Intent startIntent = new Intent(getActivity(), StepService.class);
			getActivity().startService(startIntent);
			editor.putBoolean("isStepServiceStart", true);
			editor.commit();
			Log.v("step", "Service creat");
			Toast.makeText(getActivity(), "已开启计步器", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_view_pause:
			pauseImageView.setVisibility(View.GONE);
			startImageView.setVisibility(View.VISIBLE);
			Intent stopIntent = new Intent(getActivity(), StepService.class);
			getActivity().stopService(stopIntent);
			Toast.makeText(getActivity(), "已关闭计步器", Toast.LENGTH_SHORT).show();
			editor.putBoolean("isStepServiceStart", false);
			editor.commit();
			break;
		}
	}

}
