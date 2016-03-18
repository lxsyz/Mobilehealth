package com.bbcc.mobilehealth.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.util.HorizontalScrollViewAdapter;
import com.bbcc.mobilehealth.util.SleepModel;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener
{
	

	/**
	 * 图片滚动时的回调接口
	 * 
	 * 
	 */
	public interface CurrentImageChangeListener
	{
		void onCurrentImgChanged(int position, View viewIndicator);
	}

	/**
	 * 条目点击时的回调
	 * 
	 * 
	 */
	public interface OnItemClickListener
	{
		void onClick(View view, int pos);
	}

	private CurrentImageChangeListener mListener;

	private OnItemClickListener mOnClickListener;

	private static final String TAG = "MyHorizontalScrollView";

	/**
	 * HorizontalListView中的LinearLayout
	 */
	private LinearLayout mContainer;

	/**
	 * 子元素的宽度
	 */
	private int mChildWidth;
	/**
	 * 子元素的高度
	 */
	private int mChildHeight;
	/**
	 * 当前最后一张图片的index
	 */
	private int mCurrentIndex;
	/**
	 * 当前第一张图片的下标
	 */
	private int mFristIndex;
	/**
	 * 当前第一个View
	 */
	private View mFirstView;
	/**
	 * 数据适配器
	 */
	private HorizontalScrollViewAdapter mAdapter;
	/**
	 * 每屏幕最多显示的个数
	 */
	private int mCountOneScreen;
	/**
	 * 屏幕的宽度
	 */
	private int mScreenWitdh;


	/**
	 * 保存View与位置的键值对
	 */
	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

	private int time = 5;
	
	//初始索引  根据初始的数据多少来定
	private int initIndex = 4;
	
	private int tempIndex = 0;
	private int rightTempIndex = 0;
	
	private int emptyViewSum;
	
	public MyHorizontalScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 获得屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	/**
	 * 加载右侧数据
	 */
	protected void loadNext()
	{
		
		Log.d("currentindex", mCurrentIndex+" " + emptyViewSum + mAdapter.getCount());
		Log.d("firstindex", mFristIndex+" ");
		Log.d("rightindex", rightTempIndex+" ");
		// 数组边界值计算
		if (mCurrentIndex == mAdapter.getCount() - 2 + emptyViewSum)
		{
			return;
		}

		//移除第一View，且将水平滚动位置置0
		scrollTo(0, 0);
		mViewPos.remove(mContainer.getChildAt(0));
		mContainer.removeViewAt(0);
		tempIndex++;
		//View不够则获取空VIEW
		if (mAdapter.getCount() < mCurrentIndex) {
			//获取下一空View，并且设置onclick事件，且加入容器中
			View view = mAdapter.getEmptyView(++rightTempIndex, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, rightTempIndex);
			//当前第一张图片小标
			mFristIndex++;
			mCurrentIndex++;
		} else  {
			mCurrentIndex++;
			rightTempIndex++;
			if (mAdapter.getCount() <= mCurrentIndex) {
				//获取下一空View，并且设置onclick事件，且加入容器中
				View view = mAdapter.getEmptyView(rightTempIndex, null, mContainer);
				view.setOnClickListener(this);
				mContainer.addView(view);
				mViewPos.put(view, rightTempIndex);
				//当前第一张图片小标
				mFristIndex++;
			} else {
				//获取下一View，并且设置onclick事件，且加入容器中
				View view = mAdapter.getView(rightTempIndex+mAdapter.getCount() - 5, null, mContainer);
				view.setOnClickListener(this);
				mContainer.addView(view);
				mViewPos.put(view, rightTempIndex);
				//当前第一张图片小标
				mFristIndex++;
			}
		}
		//如果设置了滚动监听则触发
		if (mListener != null)
		{
			notifyCurrentImgChanged();
		}

	}
	/**
	 * 加载左侧数据
	 */
	protected void loadPre()
	{
		
		//获得当前应该显示为第一张图片的下标
		int index = mCurrentIndex - mCountOneScreen;
		Log.d("firstindex", mFristIndex+" ");
		Log.d("currentindex", mCurrentIndex+" ");
		Log.d("index", index+" ");
		Log.d("righttemp", rightTempIndex+" ");
		//还没有移到最左侧时的操作
		if (index >= 0)
		{
			//移除最后一条
			int oldViewPos = mContainer.getChildCount() - 1;
			mViewPos.remove(mContainer.getChildAt(oldViewPos));
			mContainer.removeViewAt(oldViewPos);
			tempIndex--;
			//将此View放入第一个位置
			View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, tempIndex);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			//水平滚动位置向左移动view的宽度个像素
			scrollTo(mChildWidth, 0);
			//当前位置--，当前第一个显示的下标--
			
			mCurrentIndex--;
			rightTempIndex--;
			mFristIndex--;
			//回调
			if (mListener != null)
			{
				notifyCurrentImgChanged();

			}	
		} 
		else {
			//添加数据
			SleepModel model = new SleepModel();
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0 - time);
			time++;
			model.setTime(sdf2.format(cal.getTime()));
			model.setDeepSleepTime((int)(Math.random()*200) + 100);
			model.setQianSleepTime((int)(Math.random()*200)+100);
			mAdapter.addData(model);
			
			//移除最后一条
			int oldViewPos = mContainer.getChildCount() - 1;
			mViewPos.remove(mContainer.getChildAt(oldViewPos));
			mContainer.removeViewAt(oldViewPos);
			
			index = 0;
			
			//ViewPos的左边界
			tempIndex--;
			//将此View放入第一个位置
			View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, tempIndex);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			//水平滚动位置向左移动view的宽度个像素
			scrollTo(mChildWidth, 0);
			//当前位置重置--，当前第一个显示的下标重置--
			mCurrentIndex++;
			mFristIndex = 0;
			rightTempIndex--;
			if (mCurrentIndex >= mCountOneScreen - 1) {
				mCurrentIndex--;
			}
			mFristIndex--;
			
		}
	}

	/**
	 * 滑动时的回调
	 */
	public void notifyCurrentImgChanged()
	{
		
		mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

	}

	/**
	 * 初始化数据，设置数据适配器
	 * 
	 * @param mAdapter
	 */
	public void initDatas(HorizontalScrollViewAdapter mAdapter)
	{
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		// 获得适配器中第一个View
		final View view = mAdapter.getView(0, null, mContainer);
		mContainer.addView(view);

		// 强制计算当前View的宽和高
		if (mChildWidth == 0 && mChildHeight == 0)
		{
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			mChildHeight = view.getMeasuredHeight();
			mChildWidth = view.getMeasuredWidth();
			Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
			// 计算每次加载多少个View
			mCountOneScreen = (mScreenWitdh / mChildWidth == 0)?mScreenWitdh / mChildWidth+1:mScreenWitdh / mChildWidth+2;

			Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
					+ " ,mChildWidth = " + mChildWidth);
			

		}
		//初始化第一屏幕的元素
		initFirstScreenChildren(mCountOneScreen);
	}


	/**
	 * 加载第一屏的View
	 * 
	 * @param mCountOneScreen
	 */
	public void initFirstScreenChildren(int mCountOneScreen)
	{
		
		
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();
		Log.d("initfirst", mContainer.getChildCount()+" ");
		//scrollBy(-mScreenWitdh / 2, 0);
		int temp = mCountOneScreen;
		if (mCountOneScreen > mAdapter.getCount()) {
			temp = mAdapter.getCount();
		
			for (int i = 0; i < temp; i++)
			{
				View view = mAdapter.getView(i, null, mContainer);
				view.setOnClickListener(this);
				mContainer.addView(view);
				mViewPos.put(view, i);
				mCurrentIndex = i;
				rightTempIndex = i;
			}
			//初始化后面的空数据
			for (int j = temp;j < mCountOneScreen;j++) {
				View emptyView = mAdapter.getEmptyView(j, null, mContainer);
				//view.setOnClickListener(this);
				mContainer.addView(emptyView);
				mViewPos.put(emptyView, j);
				mCurrentIndex = j;
				rightTempIndex = j;
				emptyViewSum++;
			}
		}

		if (mListener != null)
		{
			notifyCurrentImgChanged();
		}

	}

	public void Invalidate(String selectedDate) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		int chaju = 0;
		try {
			chaju = daysBetween(selectedDate,sdf2.format(new Date()));
			Log.d("chaju", chaju+" ");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (chaju < 5) {
			mAdapter.getView(mAdapter.getCount() -1 - chaju, null, mContainer);
		} else {
			
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				Log.e(TAG, getScrollX() + "");
	
				int scrollX = getScrollX();
				// 如果当前scrollX为view的宽度，加载下一张，移除第一张
				if (scrollX >= mChildWidth)
				{
					Log.d("tag", "right");
					loadNext();
				}
				// 如果当前scrollX = 0，  到了最左边
				if (scrollX == 0)
				{
					Log.d("tag", "left");
					loadPre();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v)
	{
		if (mOnClickListener != null)
		{
			//TextView deep = (TextView) v.findViewById(R.id.deep_values_btn);
			//TextView qian = (TextView) v.findViewById(R.id.qian_values_btn);
			TextView time = (TextView)v.findViewById(R.id.textview);
			time.setTextColor(Color.rgb(126, 166, 191));
			ImageView sanjiao = (ImageView)v.findViewById(R.id.sanjiao);
			sanjiao.setVisibility(View.VISIBLE);
			
			mOnClickListener.onClick(v, mViewPos.get(v));
		}
	}

	
	public void setOnItemClickListener(OnItemClickListener mOnClickListener)
	{
		this.mOnClickListener = mOnClickListener;
	}

	public void setCurrentImageChangeListener(
			CurrentImageChangeListener mListener)
	{
		this.mListener = mListener;
	}
	
	//判断相隔多少天
	public int daysBetween(String smdate,String bdate) throws ParseException{ 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(sdf.parse(smdate)); 
		long time1 = cal.getTimeInMillis(); 
		cal.setTime(sdf.parse(bdate)); 
		long time2 = cal.getTimeInMillis(); 
		long between_days=(time2-time1)/(1000*3600*24); 

		return Integer.parseInt(String.valueOf(between_days)); 
		} 
	
	public LinearLayout getmContainer() {
		return mContainer;
	}
}
