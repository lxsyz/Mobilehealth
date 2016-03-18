package com.bbcc.mobilehealth.util;

import android.R.integer;

public class SleepModel {
	
	private int percent;	//占总时间百分比
	
	private int deepSleepTime;
	private int qianSleepTime;
	private int state;		//睡眠状态
	
	private String time;	//睡眠时间

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
