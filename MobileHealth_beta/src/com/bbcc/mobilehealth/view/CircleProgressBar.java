package com.bbcc.mobilehealth.view;

import com.bbcc.mobilehealth.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class CircleProgressBar extends View {
	private final int BackgroundStartAngle = 140;
	private final int BackgroundSweepAngle = 260;
	private int mProgress = 0;
	private float mProgressSweepAngle = 0;
	private int maxProgress = 5000;
	private Context mContext = null;
	private Paint mProgressBarBackgroundPaint = null;
	private Paint mProgressBarPaint = null;
	private Paint mCirclePointPaint = null;
	private int mProgressBackgroundColor = 0;
	private int mProgressColor = 0;
	private float mProgressBarWidth = 25;
	private int mWidth = 250;
	private RectF mBackgroundOval = null;
	private RectF mOval = null;
	private float cx = 0;
	private float cy = 0;
	private float radius=0;
	private OnProgressBarChangeListener mOnProgressBarChangeListener = null;

	public interface OnProgressBarChangeListener {
		void onProgressChanged(int progress);
	}

	public CircleProgressBar(Context context) {
		this(context, null);
	}

	public CircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initViewAttr(attrs);

	}

	private void initViewAttr(AttributeSet attrs) {
		TypedArray localTypedArray = mContext.obtainStyledAttributes(attrs,
				R.styleable.CircleProgressBar);
		mProgressBackgroundColor = localTypedArray.getColor(
				R.styleable.CircleProgressBar_progress_background_color,
				R.color.lightgray);
		mProgressColor = localTypedArray.getColor(
				R.styleable.CircleProgressBar_progress_color, R.color.blue);
		mProgressBarWidth = localTypedArray.getDimension(
				R.styleable.CircleProgressBar_progressbar_width,
				mProgressBarWidth);
		mProgressBarBackgroundPaint = new Paint();
		mProgressBarPaint = new Paint();
		mCirclePointPaint = new Paint();
		mProgressBarBackgroundPaint.setColor(mProgressBackgroundColor);
		mProgressBarPaint.setColor(mProgressColor);
		mCirclePointPaint.setColor(getResources().getColor(R.color.blue));
		mProgressBarBackgroundPaint.setAntiAlias(true);
		mProgressBarPaint.setAntiAlias(true);
		mCirclePointPaint.setAntiAlias(true);
		mProgressBarBackgroundPaint.setStyle(Paint.Style.STROKE);
		mProgressBarPaint.setStyle(Paint.Style.STROKE);
		mProgressBarBackgroundPaint.setStrokeWidth(mProgressBarWidth);
		mProgressBarPaint.setStrokeWidth(mProgressBarWidth);
		mBackgroundOval = new RectF();
		mOval = new RectF();
//		setProgress(mProgress);
//		maxProgress = 1000;// 设置默认最大为1000
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		int marginTop = ((View) getParent()).getPaddingTop();
		Log.v("tag", "getPaddingTop=" + ((View) getParent()).getPaddingTop());
		if (widthMode == MeasureSpec.EXACTLY) {
			Log.v("tag", "widthMode == MeasureSpec.EXACTLY");
			Log.v("tag", "widthSize=" + widthSize);
			width = widthSize;
		} else {
			width = this.mWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			Log.v("tag", "heightMode == MeasureSpec.EXACTLY");
			Log.v("tag", "heightSize=" + heightSize);
			height = heightSize;
		} else {
			height = this.mWidth;
		}
		Log.v("tag", "width=" + width);
		Log.v("tag", "height=" + height);
		if (height > width) {
			height = width;
		} else if (width > height) {
			width = height;
		}
		Log.v("tag", "width=" + width);
		Log.v("tag", "height=" + height);
		setMeasuredDimension(width, height);
		mBackgroundOval.set(30, 30, width - 30, height - 30);
		mOval.set(30, 30, width - 30, height - 30);
		setProgress(mProgress);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(mBackgroundOval, BackgroundStartAngle,
				BackgroundSweepAngle, false, mProgressBarBackgroundPaint);
		canvas.drawArc(mOval, BackgroundStartAngle, mProgressSweepAngle, false,
				mProgressBarPaint);
		canvas.drawCircle(cx, cy, 30, mCirclePointPaint);
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		Log.v("tag", "progress="+progress);
		int width=getMeasuredWidth();
		int height=getHeight();
		radius=(width-60)/2;
		if (progress <= 0) {
			progress = 0;
			mProgressSweepAngle = 0;
		} else {

			if (progress > maxProgress) {
				mProgress = maxProgress;
			}else {
				mProgress = progress;
			}
			Log.v("tag", "mProgress="+mProgress);
			mProgressSweepAngle = 260 * ((float)mProgress / maxProgress);
			Log.v("tag", "mProgress="+mProgress);
		}
		Log.v("tag", "mProgressSweepAngle="+mProgressSweepAngle);
		if (0 <=mProgressSweepAngle && mProgressSweepAngle <= 40) {
			cx=(float) ((width/2)-(Math.cos(Math.PI*(40-mProgressSweepAngle)/180)*radius));
			cy=(float) ((width/2)+(Math.sin(Math.PI*(40-mProgressSweepAngle)/180)*radius));
			
//			Log.v("tag", "radius="+radius);
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)="+Math.cos(40-mProgressSweepAngle));
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)*radius="+Math.cos(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "Math.sin(40-mProgressSweepAngle)*radius="+Math.sin(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "cx="+cx);
//			Log.v("tag", "cy="+cy);
		} else if (40 <mProgressSweepAngle && mProgressSweepAngle <= 130) {
			cx=(float) ((width/2)-(Math.sin(Math.PI*(130-mProgressSweepAngle)/180)*radius));
			cy=(float) ((width/2)-(Math.cos(Math.PI*(130-mProgressSweepAngle)/180)*radius));
//			Log.v("tag", "radius="+radius);
//			Log.v("tag", "Math.sin(Math.PI*(130-mProgressSweepAngle)="+Math.sin(Math.PI*(130-mProgressSweepAngle)));
//			Log.v("tag", "Math.sin(Math.PI*(130-mProgressSweepAngle))*radius="+Math.sin(Math.PI*(130-mProgressSweepAngle)/180)*radius);
//			Log.v("tag", "Math.cos(Math.PI*(130-mProgressSweepAngle))*radius="+Math.cos(Math.PI*(130-mProgressSweepAngle)/180)*radius);
//			Log.v("tag", "cx="+cx);
//			Log.v("tag", "cy="+cy);
		} else if (130 < mProgressSweepAngle && mProgressSweepAngle <= 220) {
			cx=(float) ((width/2)+(Math.cos(Math.PI*(220-mProgressSweepAngle)/180)*radius));
			cy=(float) ((width/2)-(Math.sin(Math.PI*(220-mProgressSweepAngle)/180)*radius));
//			Log.v("tag", "radius="+radius);
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)="+Math.cos(40-mProgressSweepAngle));
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)*radius="+Math.cos(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "Math.sin(40-mProgressSweepAngle)*radius="+Math.sin(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "cx="+cx);
//			Log.v("tag", "cy="+cy);
		} else if (220 < mProgressSweepAngle && mProgressSweepAngle <=260) {
			cx=(float) ((width/2)+(Math.cos(Math.PI*(mProgressSweepAngle-220)/180)*radius));
			cy=(float) ((width/2)+(Math.sin(Math.PI*(mProgressSweepAngle-220)/180)*radius));
//			Log.v("tag", "radius="+radius);
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)="+Math.cos(40-mProgressSweepAngle));
//			Log.v("tag", "Math.cos(40-mProgressSweepAngle)*radius="+Math.cos(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "Math.sin(40-mProgressSweepAngle)*radius="+Math.sin(40-mProgressSweepAngle)*radius);
//			Log.v("tag", "cx="+cx);
//			Log.v("tag", "cy="+cy);
		}
		
		invalidate();
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setOnProgressBarChangeListener(
			OnProgressBarChangeListener onProgressBarChangeListener) {
		mOnProgressBarChangeListener = onProgressBarChangeListener;
	}
}
