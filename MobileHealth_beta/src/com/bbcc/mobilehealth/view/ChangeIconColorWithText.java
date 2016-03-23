package com.bbcc.mobilehealth.view;

import com.bbcc.mobilehealth.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeIconColorWithText extends View {
	private int mColor = 0Xff00ff33;
	private Bitmap mIconBitmap;
	private String mText = "mobile health";
	private int mSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	private float mAlpha;
	private Rect mIconRect;//icon閭婄晫
	private Rect mTextBound;//鏂囨湰閭婄晫
	private Paint mTextPaint;//wenben
    private static final String INSTANCE_STATE="instance_state";
    private static final String STATE_ALPHA="STATE_ALPHA";
	public ChangeIconColorWithText(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
//鍙栧��
	public ChangeIconColorWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context
				.obtainStyledAttributes(attrs,R.styleable.ChangeIconColorWithText);//ChangeIconColorWithText鐨勬墍鏈夊睘鎬�
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {//鍙栧嚭灞炴�у��
			case R.styleable.ChangeIconColorWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);//icon
			
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeIconColorWithText_color://color
				mColor = a.getColor(attr, 0Xff00ff33);
				break;
			case R.styleable.ChangeIconColorWithText_text://text
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeIconColorWithText_text_size://textsize
				mSize = (int) a.getDimension(attr, TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources()
								.getDisplayMetrics()));//sp杞寲
				break;
			}
		}
		// TODO Auto-generated constructor stub
		a.recycle();//
		
		mTextBound = new Rect();//鐭╁舰
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mSize);
		mTextPaint.setColor(0xff555555);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}

	@Override
	//璁剧疆瀹藉拰楂�
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()//icon瀵害
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height());
		int left=(getMeasuredWidth()-iconWidth)/2;//宸︿笂瑙�
		int top=(getMeasuredHeight()-iconWidth-mTextBound.height())/2;
		mIconRect=new Rect(left,top,left+iconWidth,top+iconWidth);//juxing
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
	  canvas.drawBitmap(mIconBitmap, null,mIconRect, null);
	  int alpha=(int) Math.ceil(mAlpha*255);
	  //sehzhiICon
	  setupTargetBitmap(alpha);
	 //huizhi wenzi 
	  drawSourceText(canvas, alpha);
	  drawTargetText(canvas, alpha);
	  canvas.drawBitmap(mBitmap, 0, 0,null);
		
	}
@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
	
	   if(state instanceof Bundle)
	   {
		   Bundle bundle=(Bundle) state;
		   mAlpha=bundle.getFloat(STATE_ALPHA);
		   super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
		  
	   }
	   else
	   {
		super.onRestoreInstanceState(state);
	   }
	}
	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
		bundle.putFloat(STATE_ALPHA, mAlpha);
		return bundle;
	}
private void drawTargetText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
	mTextPaint.setColor(mColor);
	mTextPaint.setAlpha(alpha);
	int x=(getMeasuredWidth()-mTextBound.width())/2;
	int y=mIconRect.bottom+mTextBound.height();
	canvas.drawText(mText, x, y, mTextPaint);
	}

private void drawSourceText(Canvas canvas,int alpha) {
		// TODO Auto-generated method stub
		mTextPaint.setColor(0Xff00ff33);
		mTextPaint.setAlpha(255-alpha);
		int x=(getMeasuredWidth()-mTextBound.width())/2;
		int y=mIconRect.bottom+mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}

//缂佹ê鍩楅崣顖氬綁閼硅尙娈慖con
	private void setupTargetBitmap(int alpha) {
		// TODO Auto-generated method stub
		mBitmap=Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas =new Canvas(mBitmap);
		mPaint =new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
	    mCanvas.drawRect(mIconRect, mPaint);
	    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
	    mPaint.setAlpha(255);
	    mCanvas.drawBitmap(mIconBitmap,null, mIconRect, mPaint);
		
	}

	public ChangeIconColorWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public void setIconAlpha(float alpha)
	{
		this.mAlpha = alpha;
//		System.out.println("   setIconAlpha    ");
		invalidateView();
	}

//闁插秶绮�
	private void invalidateView()
	{
		if (Looper.getMainLooper() == Looper.myLooper())
		{
//			System.out.println("   chonghui    ");
			invalidate();
		} else
		{
			postInvalidate();
		}
	}
}
