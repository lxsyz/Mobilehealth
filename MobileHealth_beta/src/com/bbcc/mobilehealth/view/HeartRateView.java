package com.bbcc.mobilehealth.view;

import java.util.LinkedList;
import java.util.List;

import com.bbcc.mobilehealth.R;

import android.R.integer;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;

public class HeartRateView extends View{

	private Paint mPaint;
	
	private int mWidth = 480;
	private int mHeight = 400;
	
	private float radius = 5.0f;
	
	private int width;
	private int height;
	
	private Context context;
	
	//上下左右的边距
	private float paddingLeft = 0;
	private float paddingTop = 0;
	
	//y轴间距
	private float xSpacing = 0;
	private float ySpacing = 0;
	
	private float pointSpacing = 0;
	
	private float mScreenWidth = 0;
	private float mScreenHeight = 0;
	
	private List<Point> maxPoints;
	private List<Point> minPoints;
	
	private final int TEXT_SIZE = 15;
	
	private int[] maxData = new int[24];
	private int[] minData = new int[24];
	
	private float textWidth;
	private float textHeight;
	
	//矩形宽度
	private float rectWidth;
	
	public HeartRateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		mPaint = new Paint();
		Log.d("heartRateView", "heartRateView");
		maxPoints = new LinkedList<Point>();
		minPoints = new LinkedList<Point>();
		
		WindowManager wm = (WindowManager)context 
				.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
		mScreenHeight = wm.getDefaultDisplay().getHeight();
		
	}

	public HeartRateView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public HeartRateView(Context context) {
		this(context,null);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = (int)(mScreenHeight / 2.0f);
		}
		
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = this.mWidth;
		}
		

		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("draw", "draw");
		super.onDraw(canvas);
		//disableHardwareRendering(this);
		drawTable(canvas);
		//将数据转换成坐标显示
		initMaxPoints(maxData);
		initMinPoints(minData);
		//initData(maxData, minData);
		//initMinPoints(minData);
		//initMaxPoints(maxData);
		drawRec(canvas);
		//drawPoints(canvas);
	}
	
	
	private void setOffset() {
		
		// float circleLayoutHeight = circleLayout.getBottom();
		// float fragLayoutHeight = fragLayout.getMeasuredHeight();
		paddingTop = getPaddingTop();
		float usefulHeight = height;
		Log.d("height", usefulHeight+" ");
		paddingLeft = 70f;
		
		ySpacing = usefulHeight / 6;
		xSpacing = (mScreenWidth - paddingLeft) / 99;
		rectWidth = ((mScreenWidth - paddingLeft * 2) / 26.0f);
	}
	
	//画表
	private void drawTable(Canvas canvas) {
		setOffset();
		
		
		
		//画y轴坐标值
		Paint textPaint = new Paint();
		textPaint.setColor(Color.rgb(156, 154, 154));
		textPaint.setTextSize(TEXT_SIZE);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setTextAlign(Paint.Align.CENTER);
		 
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		textHeight = fm.descent-fm.ascent;
		 
		
		RectF rectF = new RectF();
		rectF.set(3, paddingTop, 100, paddingTop + textHeight);
		canvas.drawText(String.valueOf(150), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);

		rectF.set(3, paddingTop-textHeight + ySpacing, 100, paddingTop + textHeight
				+ ySpacing);
		canvas.drawText(String.valueOf(120), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);

		rectF.set(3, paddingTop-textHeight / 2 + ySpacing, 100, paddingTop + textHeight + 2
				* ySpacing);
		canvas.drawText(String.valueOf(90), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);

		rectF.set(3, paddingTop-textHeight / 2 + ySpacing, 100, paddingTop + textHeight + 3
				* ySpacing);
		canvas.drawText(String.valueOf(60), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);

		rectF.set(3, paddingTop-textHeight / 2 + ySpacing, 100, paddingTop + textHeight + 4
				* ySpacing);
		canvas.drawText(String.valueOf(30), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);

		rectF.set(3, paddingTop-textHeight / 2 + ySpacing, 100, paddingTop + textHeight + 5
				* ySpacing);
		canvas.drawText(String.valueOf(0), rectF.width() / 4,
				rectF.bottom - fm.descent, textPaint);
		
		
		//画横线
		Paint paint = new Paint();
		paint.setColor(Color.rgb(80, 80, 80));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);
		paint.setAntiAlias(false);
		
		for (int i = 0; i < 6; i++) {
			//path.moveTo(paddingLeft, paddingTop + ySpacing * i);
			//path.lineTo(mScreenWidth, paddingTop + ySpacing * i);
			//canvas.drawPath(path, paint);
			canvas.drawLine(paddingLeft - rectF.width() / 4, paddingTop + ySpacing * i + textHeight / 2, mScreenWidth, paddingTop + ySpacing * i + textHeight / 2, paint);
		}
		
		
		//画x轴坐标值
		//textWidth = (mScreenWidth - paddingLeft) / 9.0f;
		pointSpacing = (mScreenWidth - paddingLeft) / 24.0f;
//		
//		
//		rectF.set(paddingLeft + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth + 5,paddingTop + ySpacing * 5 + textHeight +textHeight);
//		canvas.drawText("12:00", rectF.width() / 4,
//				rectF.bottom + textHeight, textPaint);
//		
//		
//		rectF.set(paddingLeft + textWidth + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*2 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("3:00", rectF.width() / 4,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 2 + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*3 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("6:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 3 + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*4 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("9:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 4 + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*5+ 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("12:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 5 + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*6 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("3:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 6+ 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*7 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("6:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 7+ 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*8 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("9:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);
//		
//		rectF.set(paddingLeft + textWidth * 8 + 5,paddingTop + ySpacing * 5 + textHeight ,paddingLeft + textWidth*9 + 5,paddingTop + ySpacing * 5 + textHeight+textHeight);
//		canvas.drawText("12:00", rectF.left + rectF.width() / 2,
//				rectF.bottom + textHeight, textPaint);

	}
	
	
	
	private void drawRec(Canvas canvas) {
		//float rectWidth = ((mScreenWidth - paddingLeft * 2) / 25.0f);
		Paint paint = new Paint();
		
		paint.setColor(Color.rgb(88, 150, 206));
		
		for (int i = 0;i < 24;i++) {
			if (maxPoints.get(i).y == -1 || minPoints.get(i).y == -1) {
				
			} else 
				canvas.drawRect(maxPoints.get(i).x - rectWidth / 2.0f, maxPoints.get(i).y, minPoints.get(i).x + rectWidth / 2.0f, minPoints.get(i).y, paint);
		}
	}
	
	
//	private void drawPoints(Canvas canvas) {
//		Paint paint = new Paint();
//		paint.setColor(Color.rgb(242, 168, 117));
//		paint.setAntiAlias(true);
//		paint.setStrokeWidth(2);
//		
//		//粗线为圆发光
//		Paint paint2 = new Paint();
//		paint2.setColor(Color.rgb(242, 168, 117));
//		paint2.setAntiAlias(true);
//		paint2.setStrokeWidth(8);
//		paint2.setAlpha(40);
//		
//		//划线
//		Paint linePaint = new Paint();
//		linePaint.setColor(Color.rgb(202, 112, 50));
//		linePaint.setStrokeWidth(4);
//		linePaint.setAntiAlias(true);
//		//粗线做发光
//		Paint linePaint2 = new Paint();
//		linePaint2.setColor(Color.rgb(202, 112, 50));
//		linePaint2.setStrokeWidth(20);
//		linePaint2.setAntiAlias(true);
//		linePaint2.setAlpha(70);
//		
//		if (maxPoints.size() >= 2) {
//			canvas.drawCircle(maxPoints.get(maxPoints.size() - 1).x, maxPoints.get(maxPoints.size() - 1).y, radius, paint);
//			canvas.drawCircle(maxPoints.get(maxPoints.size() - 1).x, maxPoints.get(maxPoints.size() - 1).y, radius*2, paint2);
//			for (int i = 0; i < maxPoints.size() - 1; i++) {
//				
//				canvas.drawCircle(maxPoints.get(i).x, maxPoints.get(i).y, radius , paint);
//				canvas.drawCircle(maxPoints.get(i).x, maxPoints.get(i).y, radius*2, paint2);
//				
//				canvas.drawLine(maxPoints.get(i).x, maxPoints.get(i).y,
//						maxPoints.get(i + 1).x, maxPoints.get(i + 1).y, linePaint);
//				canvas.drawLine(maxPoints.get(i).x, maxPoints.get(i).y,
//						maxPoints.get(i + 1).x, maxPoints.get(i + 1).y, linePaint2);
//			}
//			
//		}
//		
//		paint.setColor(Color.rgb(142, 186, 226));
//		paint2.setColor(Color.rgb(142, 186, 226));
//		paint2.setAlpha(40);
//		linePaint.setColor(Color.rgb(88, 150, 206));
//		linePaint2.setColor(Color.rgb(88, 150, 206));
//		linePaint2.setAlpha(50);
//		if (minPoints.size() >= 2) {
//			canvas.drawCircle(minPoints.get(minPoints.size() - 1).x, minPoints.get(minPoints.size() - 1).y, radius, paint);
//			canvas.drawCircle(minPoints.get(minPoints.size() - 1).x, minPoints.get(minPoints.size() - 1).y, radius*2, paint2);
//			for (int i = 0; i < minPoints.size() - 1; i++) {
//				canvas.drawCircle(minPoints.get(i).x, minPoints.get(i).y, radius , paint);
//				canvas.drawCircle(minPoints.get(i).x, minPoints.get(i).y, radius*2, paint2);
//				Log.d("i", i+" ");
//				canvas.drawLine(minPoints.get(i).x, minPoints.get(i).y,
//						minPoints.get(i + 1).x, minPoints.get(i + 1).y, linePaint);
//				canvas.drawLine(minPoints.get(i).x, minPoints.get(i).y,
//						minPoints.get(i + 1).x, minPoints.get(i + 1).y, linePaint2);
//			}
//			
//		}
//	}
	
	
	private void initMaxPoints(int[] maxData) {
		maxPoints.clear();
		
		//点的坐标
		float x = paddingLeft + 5 + textWidth / 2;
		Log.d("paddingleft",paddingLeft+" ");
		float y = 0;
		float firstY = 0;	//记录第一个点
		for (int i = 0;i < 24;i++) {
			
			
			if (maxData[i] == 0) {
				y = -1;
				Point point = new Point((int)x,(int)y);
				maxPoints.add(point);
			}
			else if (maxData[i] > 10 && maxData[i] <= 150) {
				y = paddingTop + ((((150 - maxData[i]) / 30.0f)) * ySpacing + textHeight / 2.0f);
				if (i== 0) firstY = y;
				Point point = new Point((int)x,(int)y);
				if (y >= 0) {
					maxPoints.add(point);
				}
			}
			x += pointSpacing;
		}
		//最后一个 跟第一个点数据一样
		//x += pointSpacing;
		//Log.d("x,y",maxPoints.get(0).x+" "+maxPoints.get(0).y+" ");
		//maxPoints.add(new Point((int)x,(int)firstY));
	}
	
	private void initMinPoints(int[] minData) {
		//点的坐标
		minPoints.clear();
		float x = paddingLeft + 5 + textWidth / 2;
		float y = 0;
		float firstY = 0; // 记录第一个点
		for (int i = 0; i < 24; i++) {
			if (maxData[i] == 0) {
				y = -1;
				Point point = new Point((int)x,(int)y);
				maxPoints.add(point);
			}
			else if (minData[i] > 0 && minData[i] <= 150) {
				y = paddingTop
						+ ((((150 - minData[i]) / 30.0f)) * ySpacing + textHeight / 2.0f);
				if (i == 0)
					firstY = y;
				Point point = new Point((int) x, (int) y);
				if (y >= 0) {
					minPoints.add(point);
				}
			}
			x += pointSpacing;
		}
		// 最后一个 跟第一个点数据一样
		// x += pointSpacing;

		//minPoints.add(new Point((int) x, (int) firstY));
	}
	
	public void initData(int[] maxData,int[] minData) {
		
		this.maxData = (int[])maxData.clone();
		this.minData = (int[])minData.clone();
		invalidate();
	}
	
}
