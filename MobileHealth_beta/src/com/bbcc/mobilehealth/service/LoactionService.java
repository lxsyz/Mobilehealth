package com.bbcc.mobilehealth.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.bbcc.mobilehealth.util.Position;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LoactionService extends Service implements AMapLocationListener {
	public static final long LOCATION_UPDATE_MIN_TIME = 5*60 * 1000;//5����
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 15;//λ�øı䳬��15��
    private String userId;
    private Context context;
    private SimpleDateFormat sdf;
    
   // private FixedLengthList<AMapLocation> locationList = FixedLengthList
//            .newInstance();
    // λ�÷������
    private LocationManagerProxy locationManagerProxy;

    public LoactionService() {

    }
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
		 context=getBaseContext();
		 sdf=new SimpleDateFormat("yyyy-MM-DD ahh:mm:ss");
		 locationManagerProxy = LocationManagerProxy.getInstance(this);
	        // ��λ��ʽ����Ϊ��϶�λ���������綨λ��GPS��λ
	        locationManagerProxy.requestLocationData(
	                LocationProviderProxy.AMapNetwork, LOCATION_UPDATE_MIN_TIME,
	                LOCATION_UPDATE_MIN_DISTANCE, this);
	        // �����λ��ʽ����GPS��λ��Ҫ�ֶ�����GPS����
	        locationManagerProxy.setGpsEnable(true);

	        Log.v("tag", "locationservicestart");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 if (locationManagerProxy != null) {
	            locationManagerProxy.removeUpdates(this);
	            locationManagerProxy.destory();
	        }
	        //����Ϊnull��Ϊ����������������������Դ
	        locationManagerProxy = null;
	        Log.v("tag", "locationservicestop");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		userId=intent.getStringExtra("userId");
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		// TODO Auto-generated method stub
		   if (aMapLocation == null
	                || aMapLocation.getAMapException().getErrorCode() != 0) {
	            Log.v("locationservice", aMapLocation == null ? "null" : "not null");
	            if (aMapLocation != null) {
	                Log.v("tag", "errorcode"
	                        + aMapLocation.getAMapException().getErrorCode());
	                Log.v("tag", "errormessage"
	                        + aMapLocation.getAMapException().getErrorMessage());
	            }
	            Log.v("tag", "request error");
	           return;
	        }
		    Intent intent = new Intent();
		    double latitude=aMapLocation.getLatitude();
		    double longitude=aMapLocation.getLongitude();
		    String address = null;
	        intent.setAction("LocationBroadcast");
	        intent.putExtra("latitude",
	        		latitude);//γ��
	        intent.putExtra("longitude",
	        		longitude);//����
	        if(aMapLocation.getProvider().equals("lbs"))
	        {
	        	address=aMapLocation.getAddress();
	        }
	        else
	        {
	        	address="GPS";
	        }
	        intent.putExtra("address",address);
	        Log.i("tag","intent extra:"+
	                aMapLocation.getLatitude()+","+ aMapLocation.getLatitude()+","+aMapLocation.getAddress());
            if(!userId.equals(""))
            {
            	//��ǰʱ��
            	Date now=new Date();
				DateFormat d1 = DateFormat.getDateInstance();
		        String nowDate = d1.format(now);
		        DateFormat d2=DateFormat.getTimeInstance();
		        String nowTime = d2.format(now);
		        String nowStr=nowDate+" "+nowTime;
            	Position position =new Position();
            	position.setLongitude(longitude);
            	position.setLatitude(latitude);
            	position.setUserId(userId);
            	try {
					position.setTime(new BmobDate(sdf.parse(nowStr)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                position.save(context, new SaveListener(){

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						
					}});
            }
	        sendBroadcast(intent);
	}

}
