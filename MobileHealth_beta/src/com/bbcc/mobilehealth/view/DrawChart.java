package com.bbcc.mobilehealth.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bbcc.mobilehealth.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DrawChart extends View {
	private List<Point> points = null;
	private Context context = null;
	private LinearLayout circleLayout = null;
	private RelativeLayout fragLayout = null;
	private float offsetLeft = 0;
	private float offsetTop = 0;
	private float ySpaceing = 0;
	private float xSpaceing = 0;
	private int l;
	private int t;
	private int r;
	private int b;
	
	private int mWidth = 300;
	private int mHeight = 400;
	int width;
	int height;
	private float screenWidth = 0;

	// private int heartRate = 0;

	public DrawChart(Context context) {
		this(context, null);
	}

	public DrawChart(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		points = new LinkedList<Point>();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

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
			height = this.mHeight;
		}
		Log.v("tag", "width=" + width);
		Log.v("tag", "height=" + height);
		Log.v("tag", "width=" + width);
		Log.v("tag", "height=" + height);
		setMeasuredDimension(width, height);
	}
@Override
protected void onLayout(boolean changed, int left, int top, int right,
		int bottom) {
	super.onLayout(changed, left, top, right, bottom);
//	circleLayout.layout(l, t, r, b);
	
}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		drawCurve(canvas);
	}

	private void setOffset() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		// float circleLayoutHeight = circleLayout.getBottom();
		// float fragLayoutHeight = fragLayout.getMeasuredHeight();
		float usefulHeight = height;
		offsetLeft = 120f;
		offsetTop = getPaddingTop();
		ySpaceing = usefulHeight / 6;
		xSpaceing = (screenWidth-offsetLeft) / 99;
	}

	private void drawTable(Canvas canvas) {
		Log.v("DrawChart", "drawTable3");
		setOffset();
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.main_color2));
		Path path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 2, 4, 2, 4 }, 4);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setAntiAlias(false);
		paint.setPathEffect(effects);
		for (int i = 0; i < 6; i++) {
			path.moveTo(offsetLeft, offsetTop + ySpaceing * i);
			path.lineTo(screenWidth, offsetTop + ySpaceing * i);
			canvas.drawPath(path, paint);
		}
		// 画外框
		// paint.setStyle(Paint.Style.STROKE);
		// Rect chartRec = new Rect(OFFSET_LEFT, OFFSET_TOP,CHARTW+OFFSET_LEFT,
		// CHARTH+OFFSET_TOP);
		// canvas.drawRect(chartRec, paint);

		// 画左边的文字
//		 Path textPath = new Path();
//		 paint.setStyle(Paint.Style.FILL);
//		 paint.setTextSize(15);
//		 paint.setAntiAlias(true);
//		 for (int i = 0; i < 6; i++) {
//			 textPath.moveTo(5, offsetTop + ySpaceing * i);
//			 textPath.lineTo(95, offsetTop + ySpaceing * i);
//			 canvas.drawTextOnPath(""+(150-(1*20)), textPath, 0, 0, paint);
//		}
		Paint textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(60);
		/**
		 * 设置绘制文字时起始点X坐标的位置
		 * CENTER:以文字的宽度的中心点为起始点向两边绘制
		 * LEFT:以文字左边为起始点向右边开始绘制
		 * RIGHT:以文字宽度的右边为起始点向左边绘制
		 */
		textPaint.setTextAlign(Paint.Align.CENTER);
		 
		//获取文字度量信息
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		float textHeight = fm.descent-fm.ascent;
		 
		//绘制文字的矩形框范围
		
			RectF rectF = new RectF();
			rectF.set(3, offsetTop, 117, offsetTop+textHeight);
			canvas.drawText(String.valueOf(150), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
			
			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+ySpaceing);
			canvas.drawText(String.valueOf(130), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
			
			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+2*ySpaceing);
			canvas.drawText(String.valueOf(110), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
			
			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+3*ySpaceing);
			canvas.drawText(String.valueOf(90), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
			
			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+4*ySpaceing);
			canvas.drawText(String.valueOf(70), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
			
			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+5*ySpaceing);
			canvas.drawText(String.valueOf(50), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
//			rectF.set(3, offsetTop+ySpaceing, 117, offsetTop+textHeight+ySpaceing);
//			canvas.drawText(String.valueOf(30), rectF.left + rectF.width() / 2, rectF.bottom-fm.descent, textPaint);
	}

	private void drawCurve(Canvas canvas) {
		Log.v("DrawChart", "drawCurve4");
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getResources().getColor(R.color.heartrate_curve));
		paint.setStrokeWidth(7);
		paint.setAntiAlias(true);
		if (points.size() >= 2) {
			for (int i = 0; i < points.size() - 1; i++) {
				canvas.drawLine(points.get(i).x, points.get(i).y,
						points.get(i + 1).x, points.get(i + 1).y, paint);
			}
		}
	}

	public void startInvalidate(int rate) {
		// this.heartRate = rate;
		float x = offsetLeft;
		float y = 0;
		if (rate >= 30 && rate <= 150) {
			y = offsetTop + ((((150f - rate) / 20)-1) * ySpaceing);
			Point point = new Point((int) x, (int) y);
			if (points.size() > 100) {
				points.remove(0);
				for (int i = 0; i < 99; i++) {
					points.get(i).x += xSpaceing;
				}
				if (y!=0) {
					points.add(point);
				}
			} else {
				for (int i = 0; i < points.size() - 1; i++) {
					points.get(i).x += xSpaceing;
				}
				if (y!=0) {
					points.add(point);
				}
			}
		}
		invalidate();
		circleLayout.layout(l, t, r, b);
	}

	public void setCircleLayout(LinearLayout circleLayout) {
		this.circleLayout = circleLayout;
	}

	public void setFragLayout(RelativeLayout fragLayout) {
		this.fragLayout = fragLayout;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

}
