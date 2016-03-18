package com.bbcc.mobilehealth.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bbcc.mobilehealth.util.StepDcretor;
import com.bbcc.mobilehealth.util.Constant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener{
	// Ĭ��Ϊ30�����һ�δ洢
	private SensorManager sensorManager;
	private StepDcretor stepDetector;
	private WakeLock mWakeLock;
	private TimeCount time;

	@Override
	public void onCreate() {
		super.onCreate();
		// initBroadcastReceiver();
		new Thread(new Runnable() {
			public void run() {
				startStepDetector();
			}
		}).start();

		startTimeCount();
	}

//	private String getTodayDate() {
//		Date date = new Date(System.currentTimeMillis());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		return sdf.format(date);                                  
//	}

	private void startTimeCount() {
		time = new TimeCount(30000, 1000);
		time.start();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	private void startStepDetector() {
		if (sensorManager != null && stepDetector != null) {
			sensorManager.unregisterListener(stepDetector);
			sensorManager = null;
			stepDetector = null;
		}
		getLock(this);
		if (sensorManager == null) {
			stepDetector = new StepDcretor(this);
			// ��ȡ��������������ʵ��
			sensorManager = (SensorManager) this
					.getSystemService(SENSOR_SERVICE);
			// ��ô����������ͣ������õ������Ǽ��ٶȴ�����
			// �˷�������ע�ᣬֻ��ע����Ż���Ч��������SensorEventListener��ʵ����Sensor��ʵ������������
			Sensor sensor = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			// sensorManager.unregisterListener(stepDetector);
			sensorManager.registerListener(stepDetector, sensor,
					SensorManager.SENSOR_DELAY_UI);
			stepDetector
					.setOnSensorChangeListener(new StepDcretor.OnSensorChangeListener() {

						@Override
						public void onChange() {
							Intent sendIntent = new Intent();
							sendIntent
									.setAction("com.bbcc.mobilehealth.service.UPDATE");
							// sendIntent.putExtra("state", state);
							sendIntent.putExtra("stepCount",
									StepDcretor.CURRENT_SETP);
							Log.e("tag", "step num :"
									+ StepDcretor.CURRENT_SETP);
							sendBroadcast(sendIntent);
						}
					});
		}
		 //android4.4�Ժ����ʹ�üƲ�������
        sensorManager = (SensorManager) this
                    .getSystemService(SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(SensorService.this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.v("xf","Count sensor not available!");
        }
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// �����ʱ��������������ʼ�Ʋ�
			time.cancel();
			startTimeCount();
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}

	}

	@Override
	public void onDestroy() {
		// ȡ��ǰ̨����
		super.onDestroy();
		Log.v("TAG", "onDestroy() has been done");
		stopForeground(true);
		sensorManager.unregisterListener(stepDetector);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	synchronized private PowerManager.WakeLock getLock(Context context) {
		if (mWakeLock != null) {
			if (mWakeLock.isHeld())
				mWakeLock.release();
			mWakeLock = null;
		}

		if (mWakeLock == null) {
			PowerManager mgr = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					SensorService.class.getName());
			mWakeLock.setReferenceCounted(true);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			int hour = c.get(Calendar.HOUR_OF_DAY);
			if (hour >= 23 || hour <= 6) {
				mWakeLock.acquire(5000);
			} else {
				mWakeLock.acquire(300000);
			}
		}

		return (mWakeLock);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}