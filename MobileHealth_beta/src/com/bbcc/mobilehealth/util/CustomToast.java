package com.bbcc.mobilehealth.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CustomToast {
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			mToast.cancel();
		}
	};
	
	public static void showToast(Context context,String text,int duration) {
		mHandler.removeCallbacks(runnable);
		
		if (mToast != null) {
			mToast.setText(text);
		} else {
			mToast = Toast.makeText(context, text, duration);
		}
		
		mHandler.postDelayed(runnable, duration);
		mToast.show();
	}
}
