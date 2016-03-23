package com.bbcc.mobilehealth.view;

import com.bbcc.mobilehealth.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

public class PerWeekStepsChart extends View {
	private Context context;
	private int mWidth = 200;
	private int mHeight = 200;
	private int width = 0;
	private int height = 0;
	private float xSpacing = 0;
	private float ySpacing = 0;
	private float allStepsHeight = 0;
	private float yTextSpace = 70;// 纵坐标文字的宽度
	private int monSteps = 0;
	private int tuesSteps = 0;
	private int wedSteps = 0;
	private int thurSteps = 0;
	private int friSteps = 0;
	private int satSteps = 0;
	private int sunSteps = 0;
	private float monStepsHeight = 0;
	private float tuesStepsHeight = 0;
	private float wedStepsHeight = 0;
	private float thurStepsHeight = 0;
	private float friStepsHeight = 0;
	private float satStepsHeight = 0;
	private float sunStepsHeight = 0;
	private boolean isShowDetail1 = false;
	private boolean isShowDetail2 = false;
	private boolean isShowDetail3 = false;
	private boolean isShowDetail4 = false;
	private boolean isShowDetail5 = false;
	private boolean isShowDetail6 = false;
	private boolean isShowDetail7 = false;

	private float xActionDown = 0;
	private float yActionDown = 0;

	public PerWeekStepsChart(Context context) {
		super(context);
	}

	public PerWeekStepsChart(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PerWeekStepsChart(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int marginTop = ((View) getParent()).getPaddingTop();
		Log.v("tag", "getPaddingTop=" + ((View) getParent()).getPaddingTop());
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = this.mWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = this.mHeight;
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setXYSpacing();
		drawTable(canvas);
		drawSteps(canvas);
		isShowDetail1 = isShowDetail2 = isShowDetail3 = isShowDetail4 = isShowDetail5 = isShowDetail6 = isShowDetail7 = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xActionDown = event.getX();
			yActionDown = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			int i = isOnStepsLine(xActionDown, yActionDown);
			if (i != 0) {
				switch (i) {
				case 1:
					isShowDetail1 = true;
					break;
				case 2:
					isShowDetail2 = true;
					break;
				case 3:
					isShowDetail3 = true;
					break;
				case 4:
					isShowDetail4 = true;
					break;
				case 5:
					isShowDetail5 = true;
					break;
				case 6:
					isShowDetail6 = true;
					break;
				case 7:
					isShowDetail7 = true;
					break;

				default:
					break;
				}
				invalidate();
			}
			break;

		default:
			break;
		}

		return true;
	}

	private void setXYSpacing() {
		xSpacing = (width - yTextSpace) / 7f;
		ySpacing = height / 7f;
		allStepsHeight = ySpacing * 4;
	}

	private void drawTable(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.yellow));
		Path path = new Path();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(7);
		paint.setAntiAlias(true);
		path.moveTo(yTextSpace, ySpacing * 1);
		path.lineTo(yTextSpace, ySpacing * 5);
		canvas.drawPath(path, paint);
		paint.setColor(getResources().getColor(R.color.gray));
		paint.setStrokeWidth(1);
		for (int i = 1; i < 6; i++) {
			path.moveTo(yTextSpace, ySpacing * i);
			path.lineTo(width, ySpacing * i);
			canvas.drawPath(path, paint);
		}

		/*********************************************/
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(24);
		/**
		 * 设置绘制文字时起始点X坐标的位置 CENTER:以文字的宽度的中心点为起始点向两边绘制 LEFT:以文字左边为起始点向右边开始绘制
		 * RIGHT:以文字宽度的右边为起始点向左边绘制
		 */
		textPaint.setTextAlign(Paint.Align.CENTER);

		// 获取文字度量信息
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		float textHeight = fm.descent - fm.ascent;
		RectF rectF = new RectF();
		for (int i = 1; i < 6; i++) {
			rectF.set(3, ySpacing * i, yTextSpace - 3, ySpacing * i
					+ textHeight);
			canvas.drawText(String.valueOf(25000 - i * 5000), rectF.left
					+ rectF.width() / 2, rectF.bottom - fm.descent, textPaint);
		}
		for (int i = 0; i < 7; i++) {
			rectF.set(yTextSpace + xSpacing * i, ySpacing * 5, yTextSpace
					+ xSpacing * (i + 1), ySpacing * 5 + textHeight + 30);
			canvas.drawText(String.valueOf(getDayName(i + 1)), rectF.left
					+ rectF.width() / 2, rectF.bottom - fm.descent, textPaint);
		}

		
	}

	private void drawSteps(Canvas canvas) {
		monStepsHeight = (monSteps / 20000f) * allStepsHeight;
		tuesStepsHeight = (tuesSteps / 20000f) * allStepsHeight;
		wedStepsHeight = (wedSteps / 20000f) * allStepsHeight;
		thurStepsHeight = (thurSteps / 20000f) * allStepsHeight;
		friStepsHeight = (friSteps / 20000f) * allStepsHeight;
		satStepsHeight = (satSteps / 20000f) * allStepsHeight;
		sunStepsHeight = (sunSteps / 20000f) * allStepsHeight;
		Paint trianglePaint = new Paint();
		Path trianglePath = new Path();
		trianglePaint.setColor(getResources().getColor(R.color.black));
		trianglePaint.setStyle(Paint.Style.FILL);
		trianglePaint.setAntiAlias(true);
		Paint textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(20);
		textPaint.setTextAlign(Paint.Align.CENTER);
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		float textHeight = fm.descent - fm.ascent;
		RectF rectF = new RectF();
		if (isShowDetail1) {
			rectF.set(yTextSpace + xSpacing / 2 - 45, ySpacing * 5
					- monStepsHeight - 40 - textHeight, yTextSpace + xSpacing
					/ 2 + 45, ySpacing * 5 - monStepsHeight - 40);
			canvas.drawText(String.valueOf(getMonSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing / 2, ySpacing * 5
					- monStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing / 2 - 10, ySpacing * 5
					- monStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing / 2 + 10, ySpacing * 5
					- monStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail2) {
			rectF.set(yTextSpace + xSpacing * 1 + xSpacing / 2 - 45, ySpacing
					* 5 - tuesStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 1 + xSpacing / 2 + 45, ySpacing * 5
					- tuesStepsHeight - 40);
			canvas.drawText(String.valueOf(getTuesSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 1 + xSpacing / 2,
					ySpacing * 5 - tuesStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 1 + xSpacing / 2 - 10,
					ySpacing * 5 - tuesStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 1 + xSpacing / 2 + 10,
					ySpacing * 5 - tuesStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail3) {
			rectF.set(yTextSpace + xSpacing * 2 + xSpacing / 2 - 45, ySpacing
					* 5 - wedStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 2 + xSpacing / 2 + 45, ySpacing * 5
					- wedStepsHeight - 40);
			canvas.drawText(String.valueOf(getWedSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 2 + xSpacing / 2,
					ySpacing * 5 - wedStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 2 + xSpacing / 2 - 10,
					ySpacing * 5 - wedStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 2 + xSpacing / 2 + 10,
					ySpacing * 5 - wedStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail4) {
			rectF.set(yTextSpace + xSpacing * 3 + xSpacing / 2 - 45, ySpacing
					* 5 - thurStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 3 + xSpacing / 2 + 45, ySpacing * 5
					- thurStepsHeight - 40);
			canvas.drawText(String.valueOf(getThurSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 3 + xSpacing / 2,
					ySpacing * 5 - thurStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 3 + xSpacing / 2 - 10,
					ySpacing * 5 - thurStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 3 + xSpacing / 2 + 10,
					ySpacing * 5 - thurStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail5) {
			rectF.set(yTextSpace + xSpacing * 4 + xSpacing / 2 - 45, ySpacing
					* 5 - friStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 4 + xSpacing / 2 + 45, ySpacing * 5
					- friStepsHeight - 40);
			canvas.drawText(String.valueOf(getFriSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 4 + xSpacing / 2,
					ySpacing * 5 - friStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 4 + xSpacing / 2 - 10,
					ySpacing * 5 - friStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 4 + xSpacing / 2 + 10,
					ySpacing * 5 - friStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail6) {
			rectF.set(yTextSpace + xSpacing * 5 + xSpacing / 2 - 45, ySpacing
					* 5 - satStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 5 + xSpacing / 2 + 45, ySpacing * 5
					- satStepsHeight - 40);
			canvas.drawText(String.valueOf(getSatSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 5 + xSpacing / 2,
					ySpacing * 5 - satStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 5 + xSpacing / 2 - 10,
					ySpacing * 5 - satStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 5 + xSpacing / 2 + 10,
					ySpacing * 5 - satStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		} else if (isShowDetail7) {
			rectF.set(yTextSpace + xSpacing * 6 + xSpacing / 2 - 45, ySpacing
					* 5 - sunStepsHeight - 40 - textHeight, yTextSpace
					+ xSpacing * 6 + xSpacing / 2 + 45, ySpacing * 5
					- sunStepsHeight - 40);
			canvas.drawText(String.valueOf(getSunSteps()),
					rectF.left + rectF.width() / 2, rectF.bottom - fm.descent,
					textPaint);
			trianglePath.moveTo(yTextSpace + xSpacing * 6 + xSpacing / 2,
					ySpacing * 5 - sunStepsHeight - 10);
			trianglePath.lineTo(yTextSpace + xSpacing * 6 + xSpacing / 2 - 10,
					ySpacing * 5 - sunStepsHeight - 20);
			trianglePath.lineTo(yTextSpace + xSpacing * 6 + xSpacing / 2 + 10,
					ySpacing * 5 - sunStepsHeight - 20);
			trianglePath.close();
			canvas.drawPath(trianglePath, trianglePaint);
		}

		Paint paint1 = new Paint();
		Path path1 = new Path();
		paint1.setColor(getResources().getColor(R.color.red));
		paint1.setStyle(Paint.Style.STROKE);
		paint1.setStrokeWidth(23);
		paint1.setAntiAlias(true);
		Shader mShader1 = new LinearGradient(yTextSpace + xSpacing / 2,
				ySpacing * 5, yTextSpace + xSpacing / 2, ySpacing * 5
						- monStepsHeight, new int[] { Color.BLUE, Color.GREEN,
						Color.RED }, null, Shader.TileMode.REPEAT);
		paint1.setShader(mShader1);
		path1.moveTo(yTextSpace + xSpacing / 2, ySpacing * 5);
		path1.lineTo(yTextSpace + xSpacing / 2, ySpacing * 5 - monStepsHeight);
		canvas.drawPath(path1, paint1);

		Paint paint2 = new Paint();
		Path path2 = new Path();
		paint2.setColor(getResources().getColor(R.color.red));
		paint2.setStyle(Paint.Style.STROKE);
		paint2.setStrokeWidth(23);
		paint2.setAntiAlias(true);
		Shader mShader2 = new LinearGradient(yTextSpace + xSpacing + xSpacing
				/ 2, ySpacing * 5, yTextSpace + xSpacing + xSpacing / 2,
				ySpacing * 5 - tuesStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null, Shader.TileMode.REPEAT);
		paint2.setShader(mShader2);
		path2.moveTo(yTextSpace + xSpacing + xSpacing / 2, ySpacing * 5);
		path2.lineTo(yTextSpace + xSpacing + xSpacing / 2, ySpacing * 5
				- tuesStepsHeight);
		canvas.drawPath(path2, paint2);

		Paint paint3 = new Paint();
		Path path3 = new Path();
		paint3.setColor(getResources().getColor(R.color.red));
		paint3.setStyle(Paint.Style.STROKE);
		paint3.setStrokeWidth(23);
		paint3.setAntiAlias(true);
		Shader mShader3 = new LinearGradient(yTextSpace + xSpacing * 2
				+ xSpacing / 2, ySpacing * 5, yTextSpace + xSpacing * 2
				+ xSpacing / 2, ySpacing * 5 - wedStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null,
				Shader.TileMode.REPEAT);
		paint3.setShader(mShader3);
		path3.moveTo(yTextSpace + xSpacing * 2 + xSpacing / 2, ySpacing * 5);
		path3.lineTo(yTextSpace + xSpacing * 2 + xSpacing / 2, ySpacing * 5
				- wedStepsHeight);
		canvas.drawPath(path3, paint3);

		Paint paint4 = new Paint();
		Path path4 = new Path();
		paint4.setColor(getResources().getColor(R.color.red));
		paint4.setStyle(Paint.Style.STROKE);
		paint4.setStrokeWidth(23);
		paint4.setAntiAlias(true);
		Shader mShader4 = new LinearGradient(yTextSpace + xSpacing * 3
				+ xSpacing / 2, ySpacing * 5, yTextSpace + xSpacing * 3
				+ xSpacing / 2, ySpacing * 5 - thurStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null,
				Shader.TileMode.REPEAT);
		paint4.setShader(mShader4);
		path4.moveTo(yTextSpace + xSpacing * 3 + xSpacing / 2, ySpacing * 5);
		path4.lineTo(yTextSpace + xSpacing * 3 + xSpacing / 2, ySpacing * 5
				- thurStepsHeight);
		canvas.drawPath(path4, paint4);

		Paint paint5 = new Paint();
		Path path5 = new Path();
		paint5.setColor(getResources().getColor(R.color.red));
		paint5.setStyle(Paint.Style.STROKE);
		paint5.setStrokeWidth(23);
		paint5.setAntiAlias(true);
		Shader mShader5 = new LinearGradient(yTextSpace + xSpacing * 4
				+ xSpacing / 2, ySpacing * 5, yTextSpace + xSpacing * 4
				+ xSpacing / 2, ySpacing * 5 - friStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null,
				Shader.TileMode.REPEAT);
		paint5.setShader(mShader5);
		path5.moveTo(yTextSpace + xSpacing * 4 + xSpacing / 2, ySpacing * 5);
		path5.lineTo(yTextSpace + xSpacing * 4 + xSpacing / 2, ySpacing * 5
				- friStepsHeight);
		canvas.drawPath(path5, paint5);

		Paint paint6 = new Paint();
		Path path6 = new Path();
		paint6.setColor(getResources().getColor(R.color.red));
		paint6.setStyle(Paint.Style.STROKE);
		paint6.setStrokeWidth(23);
		paint6.setAntiAlias(true);
		Shader mShader6 = new LinearGradient(yTextSpace + xSpacing * 5
				+ xSpacing / 2, ySpacing * 5, yTextSpace + xSpacing * 5
				+ xSpacing / 2, ySpacing * 5 - satStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null,
				Shader.TileMode.REPEAT);
		paint6.setShader(mShader6);
		path6.moveTo(yTextSpace + xSpacing * 5 + xSpacing / 2, ySpacing * 5);
		path6.lineTo(yTextSpace + xSpacing * 5 + xSpacing / 2, ySpacing * 5
				- satStepsHeight);
		canvas.drawPath(path6, paint6);

		Paint paint7 = new Paint();
		Path path7 = new Path();
		paint7.setColor(getResources().getColor(R.color.red));
		paint7.setStyle(Paint.Style.STROKE);
		paint7.setStrokeWidth(23);
		paint7.setAntiAlias(true);
		Shader mShader7 = new LinearGradient(yTextSpace + xSpacing * 6
				+ xSpacing / 2, ySpacing * 5, yTextSpace + xSpacing * 6
				+ xSpacing / 2, ySpacing * 5 - sunStepsHeight, new int[] { Color.BLUE, Color.GREEN,
				Color.RED }, null,
				Shader.TileMode.REPEAT);
		paint7.setShader(mShader7);
		path7.moveTo(yTextSpace + xSpacing * 6 + xSpacing / 2, ySpacing * 5);
		path7.lineTo(yTextSpace + xSpacing * 6 + xSpacing / 2, ySpacing * 5
				- sunStepsHeight);
		canvas.drawPath(path7, paint7);
		mShader1 = mShader2 = mShader3 = mShader4 = mShader5 = mShader6 = mShader7 = null;
		path1 = path2 = path3 = path4 = path5 = path6 = path7 = null;
		paint1 = paint2 = paint3 = paint4 = paint5 = paint6 = paint7 = null;
	}

	private int isOnStepsLine(float x, float y) {
		if (((yTextSpace + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - monStepsHeight) && y <= ySpacing * 5) {
			return 1;
		} else if (((yTextSpace + xSpacing + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - tuesStepsHeight) && y <= ySpacing * 5) {
			return 2;
		} else if (((yTextSpace + xSpacing * 2 + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing * 2 + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - wedStepsHeight) && y <= ySpacing * 5) {
			return 3;
		} else if (((yTextSpace + xSpacing * 3 + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing * 3 + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - thurStepsHeight) && y <= ySpacing * 5) {
			return 4;
		} else if (((yTextSpace + xSpacing * 4 + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing * 4 + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - friStepsHeight) && y <= ySpacing * 5) {
			return 5;
		} else if (((yTextSpace + xSpacing * 5 + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing * 5 + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - satStepsHeight) && y <= ySpacing * 5) {
			return 6;
		} else if (((yTextSpace + xSpacing * 6 + xSpacing / 2) - 23 / 2f) <= x
				&& ((yTextSpace + xSpacing * 6 + xSpacing / 2) + 23 / 2f) >= x
				&& y >= (ySpacing * 5 - sunStepsHeight) && y <= ySpacing * 5) {
			return 7;
		} else {
			return 0;
		}
	}

	private String getDayName(int i) {
		String string = null;
		switch (i) {
		case 1:
			string = "周一";
			break;
		case 2:
			string = "周二";
			break;
		case 3:
			string = "周三";
			break;
		case 4:
			string = "周四";
			break;
		case 5:
			string = "周五";
			break;
		case 6:
			string = "周六";
			break;
		case 7:
			string = "周日";
			break;
		default:
			break;
		}
		return string;
	}

	public void setPerWeekSteps(int monSteps, int tuesSteps, int wedSteps,
			int thurSteps, int friSteps, int satSteps, int sunSteps) {
		this.monSteps = monSteps;
		this.tuesSteps = tuesSteps;
		this.wedSteps = wedSteps;
		this.thurSteps = thurSteps;
		this.friSteps = friSteps;
		this.satSteps = satSteps;
		this.sunSteps = sunSteps;
		invalidate();
	}

	public int getMonSteps() {
		return monSteps;
	}

	public void setMonSteps(int monSteps) {
		this.monSteps = monSteps;
		invalidate();
	}

	public int getTuesSteps() {
		return tuesSteps;
	}

	public void setTuesSteps(int tuesSteps) {
		this.tuesSteps = tuesSteps;
		invalidate();
	}

	public int getWedSteps() {
		return wedSteps;
	}

	public void setWedSteps(int wedSteps) {
		this.wedSteps = wedSteps;
		invalidate();
	}

	public int getThurSteps() {
		return thurSteps;
	}

	public void setThurSteps(int thurSteps) {
		this.thurSteps = thurSteps;
		invalidate();
	}

	public int getFriSteps() {
		return friSteps;
	}

	public void setFriSteps(int friSteps) {
		this.friSteps = friSteps;
		invalidate();
	}

	public int getSatSteps() {
		return satSteps;
	}

	public void setSatSteps(int satSteps) {
		this.satSteps = satSteps;
		invalidate();
	}

	public int getSunSteps() {
		return sunSteps;
	}

	public void setSunSteps(int sunSteps) {
		this.sunSteps = sunSteps;
		invalidate();
	}

}
