package com.bbcc.mobilehealth.util;

import android.R.integer;

public class SleepModel {
	
	private int percent;	//
	private int deepSleepTime;
	private int qianSleepTime;
	private int state;		//
	
	private String time;	//
	private int rate;	//心率

	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public void setDeepSleepTime(int deepSleepTime) {
		this.deepSleepTime = deepSleepTime;
	}
	public int getDeepSleepTime() {
		return deepSleepTime;
	}
	public void setQianSleepTime(int qianSleepTime) {
		this.qianSleepTime = qianSleepTime;
	}
	public int getQianSleepTime() {
		return qianSleepTime;
	}
}
