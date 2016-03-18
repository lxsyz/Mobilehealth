package com.bbcc.mobilehealth.util;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class ExceptionTable extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BmobDate Time;
	private String bmp;
	private String action;
	private String reason;
	private String userId;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
