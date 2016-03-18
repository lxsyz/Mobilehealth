package com.bbcc.mobilehealth.util;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class HAA extends  BmobObject {
	private BmobDate Time;
	private String bmp;
	private String action;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BmobDate getTime() {
		return Time;
	}
	public void setTime(BmobDate time) {
		Time = time;
	}
	public String getBmp() {
		return bmp;
	}
	public void setBmp(String bmp) {
		this.bmp = bmp;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
