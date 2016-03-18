package com.bbcc.mobilehealth.fragment;


import com.bbcc.mobilehealth.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onCreateView");
		return inflater.inflate(R.layout.more, container, false);
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onActivityCreate");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.e("tag", "more:onAttach");
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onPause");
		super.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onStop");
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("tag", "more:onResum");
		super.onResume();

	}
}
