package com.bbcc.mobilehealth.util;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Position extends BmobObject {
	private BmobDate time;
	private double latitude;//纬度
	private double longitude;//经度
	private String address;//地址
	private String userId;//userId
	public BmobDate getTime() {
		return time;
	}
	public void setTime(BmobDate time) {
		this.time = time;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
