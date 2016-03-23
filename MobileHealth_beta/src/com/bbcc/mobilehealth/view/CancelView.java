package com.bbcc.mobilehealth.view;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class CancelView extends View {
	private Context context;
	private int width;
	private int height;

	public CancelView(Context context) {
		super(context);
	}

	public CancelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CancelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {

		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		float w=width;
		float h=w/5;
		paint.setAntiAlias(true); // 设置画笔为无锯齿
		paint.setColor(Color.WHITE); // 设置画笔颜色
		RectF r1 = new RectF(0, w/2-h/2, w, w/2+h/2); // RectF对象
		RectF r2 = new RectF(w/2-h/2, 0, w/2+h/2, w); // RectF对象
		canvas.drawRoundRect(r1, 8, 8, paint); // 绘制圆角矩形
		canvas.drawRoundRect(r2, 8, 8, paint);
	}
}
