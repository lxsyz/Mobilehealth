package com.bbcc.mobilehealth.util;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class StatisticsTable extends BmobObject {
	private int step;
	private int daySit;
	private int dayWalk;
	private int dayRun;
	private int dayUp;
	private int dayDown;
	public int getDayUp() {
		return dayUp;
	}
	public void setDayUp(int dayUp) {
		this.dayUp = dayUp;
	}
	public int getDayDown() {
		return dayDown;
	}
	public void setDayDown(int dayDown) {
		this.dayDown = dayDown;
	}
	private BmobDate date;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getDaySit() {
		return daySit;
	}
	public void setDaySit(int daySit) {
		this.daySit = daySit;
	}
	public int getDayWalk() {
		return dayWalk;
	}
	public void setDayWalk(int dayWalk) {
		this.dayWalk = dayWalk;
	}
	public int getDayRun() {
		return dayRun;
	}
	public void setDayRun(int dayRun) {
		this.dayRun = dayRun;
	}
	public BmobDate getDate() {
		return date;
	}
	public void setDate(BmobDate date) {
		this.date = date;
	}
	
	
	

}
