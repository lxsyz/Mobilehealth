package com.bbcc.mobilehealth.service;

import android.R.string;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bbcc.mobilehealth.MainActivity;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.fragment.ActionFragment;
import com.bbcc.mobilehealth.util.CountDownTimer;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.StepDcretor;

public class StepService extends Service implements SensorEventListener {
	// Ĭ��Ϊ30�����һ�δ洢
	private static int duration = 30000;
	private static String CURRENTDATE = "";
	private SensorManager sensorManager;
	private StepDcretor stepDetector;
	private NotificationManager nm;
	private NotificationCompat.Builder builder;
	private BroadcastReceiver mBatInfoReceiver;
	private WakeLock mWakeLock;
	private TimeCount time;
	private SharedPreferences spf;
	private SharedPreferences.Editor editor;
	private String editorKey = null;
	private Context context=null;
	// ����
	private static int i = 0;

	@Override
	public void onCreate() {
		Log.v("step", "Service creat");
		super.onCreate();
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		editorKey=String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
		spf=getSharedPreferences("step_data", MODE_PRIVATE);
		editor = spf.edit();
		initBroadcastReceiver();
		new Thread(new Runnable() {
			public void run() {
				startStepDetector();
			}
		}).start();

		startTimeCount();
		initTodayData();

//		updateNotification("���ղ���" + StepDcretor.CURRENT_SETP + " ��");
	}

	private void initTodayData() {
		
		spf.getInt(editorKey, 0);
		Log.v("step", "spf.getInt(editorKey, 0)="+spf.getInt(editorKey, 0));
	}

	private void initBroadcastReceiver() {
		final IntentFilter filter = new IntentFilter();
		// 屏幕灭屏广播
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		// 关机广播
		filter.addAction(Intent.ACTION_SHUTDOWN);
		// 亮屏
		filter.addAction(Intent.ACTION_SCREEN_ON);
		// ��Ļ����㲥
		filter.addAction(Intent.ACTION_USER_PRESENT);
		// 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
		// example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
		// 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

		mBatInfoReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				String action = intent.getAction();

				if (Intent.ACTION_SCREEN_ON.equals(action)) {
					Log.d("xf", "screen on");
				} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
					Log.d("xf", "screen off");
					// ��Ϊ60��һ�洢
					duration = 60000;
				} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
					Log.d("xf", "screen unlock");
					 save();
					// ��Ϊ30��һ�洢
					duration = 30000;
				} else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent
						.getAction())) {
					Log.i("xf", " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
					// ����һ��
					 save();
				} else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
					Log.i("xf", " receive ACTION_SHUTDOWN");
					 save();
				}
			}
		};
		registerReceiver(mBatInfoReceiver, filter);
	}

	private void startTimeCount() {
		time = new TimeCount(duration, 1000);
		time.start();
	}

	/**
	 * ����֪ͨ
	 */
//	private void updateNotification(String content) {
//		builder = new NotificationCompat.Builder(this);
//		builder.setPriority(Notification.PRIORITY_MIN);
//
//		// Notification.Builder builder = new Notification.Builder(this);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				new Intent(this, MainActivity.class), 0);
//		builder.setContentIntent(contentIntent);
//		builder.setSmallIcon(R.drawable.ic_action_refresh);
//		builder.setTicker("BasePedo");
//		builder.setContentTitle("BasePedo");
//		// ���ò������
//		builder.setOngoing(true);
//		builder.setContentText(content);
//		Notification notification = builder.build();
//
//		startForeground(0, notification);
//
//		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		nm.notify(R.string.app_name, notification);
//	}

//	@Override
//	public IBinder onBind(Intent intent) {
//
//		return messenger.getBinder();
//	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.v("step", "Service start");
		
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
		// android4.4�Ժ����ʹ�üƲ�������
		int VERSION_CODES = android.os.Build.VERSION.SDK_INT;
		if (VERSION_CODES >= 19) {
			addCountStepListener();
		} else {
			addBasePedoListener();
		}
	}

	private void addCountStepListener() {
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		Sensor countSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		if (countSensor != null) {
			sensorManager.registerListener(StepService.this, countSensor,
					SensorManager.SENSOR_DELAY_UI);
		} else {
			Log.v("xf", "Count sensor not available!");
			addBasePedoListener();
		}
	}

	private void addBasePedoListener() {
		stepDetector = new StepDcretor(this);
		// ��ȡ��������������ʵ��
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// ��ô����������ͣ������õ������Ǽ��ٶȴ�����
		// �˷�������ע�ᣬֻ��ע���Ż���Ч������SensorEventListener��ʵ��Sensor��ʵ���������
		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// sensorManager.unregisterListener(stepDetector);
		sensorManager.registerListener(stepDetector, sensor,
				SensorManager.SENSOR_DELAY_UI);
		stepDetector
				.setOnSensorChangeListener(new StepDcretor.OnSensorChangeListener() {

					@Override
					public void onChange() {
						StepDcretor.CURRENT_SETP++;
						Message message=Message.obtain();
						message.what=Constant.STEP_UPDATE;
						message.arg1=StepDcretor.CURRENT_SETP;
						ActionFragment.handler.sendMessage(message);
//						updateNotification("���ղ���" + StepDcretor.CURRENT_SETP
//								+ " ��");
					}
				});
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// i++;
		StepDcretor.CURRENT_SETP++;
		Message message=Message.obtain();
		message.what=Constant.STEP_UPDATE;
		message.arg1=StepDcretor.CURRENT_SETP;
		ActionFragment.handler.sendMessage(message);
//		updateNotification("���ղ���" + StepDcretor.CURRENT_SETP + " ��");
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// ����ʱ���������ʼ�Ʋ�
			time.cancel();
			 save();
			startTimeCount();
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}

	}

	private void save() {
		Log.v("step", "save");
        int tempStep = StepDcretor.CURRENT_SETP;
        editor.putInt(editorKey, tempStep);
        editor.commit();
    }

	@Override
	public void onDestroy() {
		// ȡ��ǰ̨���
		stopForeground(true);
		// DbUtils.closeDb();
		unregisterReceiver(mBatInfoReceiver);
		Intent intent = new Intent(this, StepService.class);
		startService(intent);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	// private void unlock(){
	// setLockPatternEnabled(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED,false);
	// }
	//
	// private void setLockPatternEnabled(String systemSettingKey, boolean
	// enabled) {
	// //�Ƽ�ʹ��
	// android.provider.Settings.Secure.putInt(getContentResolver(),
	// systemSettingKey,enabled ? 1 : 0);
	// }

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
					StepService.class.getName());
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

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
