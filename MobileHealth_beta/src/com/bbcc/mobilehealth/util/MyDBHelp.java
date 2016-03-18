package com.bbcc.mobilehealth.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelp extends SQLiteOpenHelper {

	private static final String DBNAME = "MHealthDataBase";
	private static final String TAG = "DBAdapter";
	private static final int DATABASE_VERSION = 1;
	public static final String HAATABLE="HeartrateAndAction";
	public static final String EXCEPTIONTABLE="Exception";
	public static final String STATISTICSTABLE="Statistics";
	public static final String USER_IFO = "user_ifo";
	public static final String HEART_RATE = "heart_rate";
	public static final String MONTH_RATE = "month_rate";
	public static final String DAY_RATE = "day_rate";
	public static final String SLEEP = "sleep";
	public static final String SLEEP_DEEP = "sleep_deep";
	public static final String SLEEP_REM = "sleep_rem";

	private static final String CREATE_USER_IFO = "create table " + USER_IFO
			+ " (" + "phoneNumber" + " char(11) primary key," + "password"
			+ " varchar(16) not null," + "userName" + " varchar(16),"
			+ "province" + " varchar(16)," + "city" + " varchar(16),"
			+ "birthday" + " date," + "profession" + " varchar(16)," + "gender"
			+ " int(1)," + "height" + " int(3)," + "weight" + " int(3),"
			+ "registerTime" + " datetime not null," + ");";
	private static final String CREATE_HEART_RATE = "create table "
			+ HEART_RATE + " (" + "ID" + " int(12) primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "time" + " datetime," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";

	private static final String CREATE_MONTH_RATE = "create table "
			+ MONTH_RATE + " (" + "ID" + " int(8) primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "day" + " date," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_DAY_RATE = "create table " + DAY_RATE
			+ " (" + "ID" + " int(9) primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "day" + " date," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_SLEEP = "create table " + SLEEP + " ("
			+ "ID" + " int(9) primary key autoincrement," + "phoneNumber"
			+ " char(11)," + "strShallow" + " datetime," + "strDeep"
			+ " datetime," + "strREM" + " datetime," + "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_SLEEP_DEEP = "create table "
			+ SLEEP_DEEP + " (" + "ID" + " int(9) primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "startTime" + " datetime,"
			+ "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_SLEEP_REM = "create table " + SLEEP_REM
			+ " (" + "ID" + " int(9) primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "startTime" + " datetime,"
			+ "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";

	public MyDBHelp(Context context) {
		super(context, DBNAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER_IFO);
		db.execSQL(CREATE_HEART_RATE);
		db.execSQL(CREATE_DAY_RATE);
		db.execSQL(CREATE_MONTH_RATE);
		db.execSQL(CREATE_SLEEP);
		db.execSQL(CREATE_SLEEP_DEEP);
		db.execSQL(CREATE_USER_IFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
}
