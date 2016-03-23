package com.bbcc.mobilehealth.util;

import java.util.LinkedList;

import com.wahoofitness.connector.conn.characteristics.Integrator;

public class HeartrateData {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private LinkedList<Integer> linkedList;

	public HeartrateData(int year,int month,int day,int hour,int minute,LinkedList<Integer> linkedList) {
		this.year=year;
		this.month=month;
		this.day=day;
		this.hour=hour;
		this.minute=minute;
		this.linkedList=linkedList;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public LinkedList<Integer> getLinkedList() {
		return linkedList;
	}

	public void setLinkedList(LinkedList<Integer> linkedList) {
		this.linkedList = linkedList;
	}

}
