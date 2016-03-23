package com.bbcc.mobilehealth.view;

import java.util.Timer;
import java.util.TimerTask;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.util.Constant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class ProcessView extends View {
	private Paint paint;
	private Paint mainPaint;
	private Timer timer;
	private TimerTask task;
	private int state = 1;
	private int mainColor = getResources().getColor(R.color.white);
	private int blueColor = getResources().getColor(R.color.blueviolet);
	private int redColor = getResources().getColor(R.color.red);
	private int greenColor = getResources().getColor(R.color.greenyellow);
	private int purpleColor = getResources().getColor(R.color.purple);
	private float radius = 13;
	private MyHandler handler = new MyHandler();

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.PROCESSVIEW_MSG:
				invalidate();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	public ProcessView(Context context) {
		this(context, null);
	}

	public ProcessView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProcessView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = new Paint();
		mainPaint = new Paint();
		timer = new Timer();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {

			width = (int) (radius * 14);
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = (int) (radius * 2);
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mainPaint.setColor(mainColor);
		switch (state) {
		case 1:
			paint.setColor(blueColor);
			canvas.drawCircle(radius, radius, radius, paint);
			canvas.drawCircle(radius * 5, radius, radius, mainPaint);
			canvas.drawCircle(radius * 9, radius, radius, mainPaint);
			canvas.drawCircle(radius * 13, radius, radius, mainPaint);
			break;
		case 2:
			paint.setColor(redColor);
			canvas.drawCircle(radius, radius, radius, mainPaint);
			canvas.drawCircle(radius * 5, radius, radius, paint);
			canvas.drawCircle(radius * 9, radius, radius, mainPaint);
			canvas.drawCircle(radius * 13, radius, radius, mainPaint);
			break;
		case 3:
			paint.setColor(greenColor);
			canvas.drawCircle(radius, radius, radius, mainPaint);
			canvas.drawCircle(radius * 5, radius, radius, mainPaint);
			canvas.drawCircle(radius * 9, radius, radius, paint);
			canvas.drawCircle(radius * 13, radius, radius, mainPaint);
			break;
		case 4:
			paint.setColor(purpleColor);
			canvas.drawCircle(radius, radius, radius, mainPaint);
			canvas.drawCircle(radius * 5, radius, radius, mainPaint);
			canvas.drawCircle(radius * 9, radius, radius, mainPaint);
			canvas.drawCircle(radius * 13, radius, radius, paint);
			break;

		default:
			break;
		}
		if (task != null) {
			task.cancel(); // 将原任务从队列中移除
		}
		task = new TimerTask() {
			@Override
			public void run() {
				state++;
				if (state == 5) {
					state = 1;
				}
				Message message = Message.obtain();
				message.what = Constant.PROCESSVIEW_MSG;
				handler.sendEmptyMessage(message.what);
			}

		};
		timer.schedule(task, 200);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
