package com.bbcc.mobilehealth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class MyDBHelp extends SQLiteOpenHelper {

	private static final String DBNAME = "HealthDataBase.db";
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
			+ "registerTime" + " datetime not null" + ");";
	private static final String CREATE_HEART_RATE = "create table "
			+ HEART_RATE + " (" + "ID" + " integer primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "time" + " date," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ "foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";

	private static final String CREATE_MONTH_RATE = "create table "
			+ MONTH_RATE + " (" + "ID" + " integer primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "day" + " date," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ "foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_DAY_RATE = "create table " + DAY_RATE
			+ " (" + "ID" + " integer primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "day" + " date," + "rate"
			+ " int(3)," + "state" + " int(1),"
			+ "foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
//	private static final String CREATE_SLEEP = "create table " + SLEEP + " ("
//			+ "ID" + " integer primary key autoincrement," + "phoneNumber"
//			+ " char(11)," + "strShallow" + " datetime," + "strDeep"
//			+ " datetime," + "strREM" + " datetime," + "endTime" + " datetime,"
//			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
//			+ ");";
	private static final String CREATE_SLEEP = "create table " + SLEEP + " ("
			+ "ID" + " integer primary key autoincrement," + "phoneNumber"
			+ " char(11)," + "startTime" + " datetime," + "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_SLEEP_DEEP = "create table "
			+ SLEEP_DEEP + " (" + "ID" + " integer primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "startTime" + " datetime,"
			+ "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";
	private static final String CREATE_SLEEP_REM = "create table " + SLEEP_REM
			+ " (" + "ID" + " integer primary key autoincrement,"
			+ "phoneNumber" + " char(11)," + "startTime" + " datetime,"
			+ "endTime" + " datetime,"
			+ " foreign key(phoneNumber) references user_ifo(phoneNumber)"
			+ ");";

	public MyDBHelp(Context context) {
		super(context, DBNAME, null, DATABASE_VERSION);
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
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	
	public static void readDB2Dir(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(context, "have SD card", Toast.LENGTH_SHORT).show();
			String path = "/data/data/com.bbcc.mobilehealth/databases";

			File file = new File(path, "HealthDataBase.db");
			File f = Environment.getExternalStorageDirectory();
			File fileDist = new File(f, "HealthDataBase.db");
			FileOutputStream os = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				os = new FileOutputStream(fileDist);
				byte[] array = new byte[1024];
				int len = -1;
				while ((len = fis.read(array)) != -1) {
					os.write(array, 0, len);
				}
				Toast.makeText(context, "读取成功", Toast.LENGTH_SHORT)
						.show();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					os.close();
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			Toast.makeText(context, "no SD card", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
