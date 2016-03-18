package com.bbcc.mobilehealth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;
import com.bbcc.mobilehealth.fragment.HomeFragment;
import com.bbcc.mobilehealth.fragment.ActionFragment;
import com.bbcc.mobilehealth.fragment.HeartRateFragment;

import com.bbcc.mobilehealth.fragment.PositionFragment;
import com.bbcc.mobilehealth.fragment.MoreFragment;
import com.bbcc.mobilehealth.service.LoactionService;
import com.bbcc.mobilehealth.service.StepService;
import com.bbcc.mobilehealth.service.StepService;
import com.bbcc.mobilehealth.util.ExceptionTable;
import com.bbcc.mobilehealth.util.HAA;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.bbcc.mobilehealth.util.MyUser;
import com.bbcc.mobilehealth.util.StatisticsTable;
import com.bbcc.mobilehealth.util.SystemBarTintManager;
import com.bbcc.mobilehealth.util.ViewPagerScroller;
import com.bbcc.mobilehealth.view.ChangeIconColorWithText;
import com.bbcc.mobilehealth.viewpagertransform.BackgroundToForegroundTransformer;
import com.bbcc.mobilehealth.viewpagertransform.CubeOutTransformer;
import com.bbcc.mobilehealth.viewpagertransform.DefaultTransformer;
import com.bbcc.mobilehealth.viewpagertransform.FlipHorizontalTransformer;
import com.bbcc.mobilehealth.viewpagertransform.StackTransformer;
import com.bbcc.mobilehealth.viewpagertransform.TabletTransformer;
import com.bbcc.mobilehealth.viewpagertransform.ZoomInTransformer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	private List<Fragment> mTabs = new ArrayList<Fragment>();
	ViewPager viewPager;
	private SystemBarTintManager tintManager;
	private FragmentPagerAdapter mAdapter;
	private ActionFragment tab0;
	private HeartRateFragment tab1;
	private PositionFragment tab2;
	private MoreFragment tab3;
	private HomeFragment home;
	private List<ChangeIconColorWithText> mIndicators = new ArrayList<ChangeIconColorWithText>();
	private ChangeIconColorWithText homeIcon;
	private ChangeIconColorWithText one;
	private ChangeIconColorWithText two;
	private ChangeIconColorWithText three;
	private LinearLayout item1 = null;
	private LinearLayout item2 = null;
	private LinearLayout item3 = null;
	private LinearLayout item4 = null;
	private String userId;
	private String contact;
	private String name;

	// private ChangeIconColorWithText four;
	// private TextView state;

	// public static boolean isClick;
	// public static boolean isConnected;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private boolean isAutoDele;
	private boolean isUpLoad;
	private boolean isAcceptPush;
	private boolean isAutoAction;
	private boolean isAutoLocation;
	private boolean isClicked;
	private boolean locationIsOpen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bmob.initialize(this, "b51e3eb59f6e65de81096688de413362");
		initView();
		initWindow();
		initDatas();
		initEvents();
		MyDBHelp myDBHelp = new MyDBHelp(this);
		SQLiteDatabase db = myDBHelp.getWritableDatabase();
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(mAdapter);
		ViewPagerScroller scroller = new ViewPagerScroller(
				getApplicationContext());
		scroller.setScrollDuration(1500);
		if (isUpLoad) {
			ConnectivityManager connectMgr = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = connectMgr.getActiveNetworkInfo();
			if (info == null) {
				Toast.makeText(this, "未联网,请联网后上传数据！", Toast.LENGTH_LONG).show();
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				Toast.makeText(this, "当前网络为Wifi，可以放心上传数据", Toast.LENGTH_LONG)
						.show();

				// 上传数据
				// new upLoadTask().execute();

			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				// 提示框
				AlertDialog.Builder builder = new Builder(this);
				builder.setMessage("当前网络为手机网络，是否继续上传数据");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int witch) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								// 上传数据
								// new upLoadTask().execute();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int witch) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
			}
		}
		if (isAutoAction) {
			Intent intent = new Intent(this, StepService.class);
			intent.putExtra("userId", userId);
			intent.putExtra("contact", contact);
			intent.putExtra("name", name);
			Toast.makeText(this, "startSevice!", Toast.LENGTH_SHORT).show();
			startService(intent);
			isClicked = false;
		}
		if (isAutoLocation) {

			Intent serviceIntent = new Intent(this, LoactionService.class);
			serviceIntent.putExtra("userId", userId);
			startService(serviceIntent);
			isAutoLocation = false;
		}
		editor.putBoolean("isClicked", isClicked);
		editor.putBoolean("isAutoLocation", isAutoLocation);
		editor.commit();
		scroller.initViewPagerScroll(viewPager);// 这个是设置切换过渡时间为2秒
		// viewPager.setPageTransformer(true,n);
		homeIcon.setIconAlpha(1.0f);

		// 实时上传数据
		// timer.schedule(timerTask, 1000, 2000);

	}

	private void initEvents() {
		// TODO Auto-generated method stub
		homeIcon.setOnClickListener(this);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		// four.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);
//		item1.setOnClickListener(this);
//		item2.setOnClickListener(this);
//		item3.setOnClickListener(this);
//		item4.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub

		settings = getSharedPreferences("setting", 0);
		editor = settings.edit();
		isAutoDele = settings.getBoolean("isAutoDele", false);
		isAcceptPush = settings.getBoolean("isAutoDele", false);
		isUpLoad = settings.getBoolean("isUpLoad", false);
		isAutoAction = settings.getBoolean("isAutoAction", false);
		isAutoLocation = settings.getBoolean("isAutoLocation", false);
		isClicked = settings.getBoolean("isClicked", true);
		locationIsOpen = settings.getBoolean("locationIsOpen", true);
		if (BmobUser.getCurrentUser(this, MyUser.class) != null) {
			userId = BmobUser.getCurrentUser(this, MyUser.class).getObjectId();
			contact = BmobUser.getCurrentUser(this, MyUser.class).getContact();
			name = BmobUser.getCurrentUser(this, MyUser.class).getName();
		} else {
			userId = "";
			contact = "15172323942";
			name = "佚名";
		}
		if (tab0 == null) {
			tab0 = new ActionFragment();

		}
		if (tab1 == null) {
			// tab1 = new DiscoverFragment();
			tab1 = new HeartRateFragment();
			// tab1 = new DiscoverFragment();
		}
		if (tab2 == null) {
			tab2 = new PositionFragment();
		}
		if (tab3 == null) {
			tab3 = new MoreFragment();
		}

		if (home == null) {
			home = new HomeFragment();
		}

		mTabs.add(home);
		mTabs.add(tab0);
		mTabs.add(tab1);
		mTabs.add(tab2);
		// mTabs.add(tab3);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mTabs.get(arg0);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				Long id = super.getItemId(position);
				// tagList.add(id);
				Log.e("tag", id.toString());
				return id;
			}

			@Override
			public void startUpdate(ViewGroup container) {
				// TODO Auto-generated method stub
				super.startUpdate(container);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mTabs.size();
			}
		};

	}

	private void initView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.id_viewpager);
		homeIcon = (ChangeIconColorWithText) findViewById(R.id.id_indicator_home);
		one = (ChangeIconColorWithText) findViewById(R.id.id_indicator_one);
		two = (ChangeIconColorWithText) findViewById(R.id.id_indicator_two);
		three = (ChangeIconColorWithText) findViewById(R.id.id_indicator_three);
		item1= (LinearLayout) findViewById(R.id.homepage_item1);
		item2 = (LinearLayout) findViewById(R.id.homepage_item2);
		item3 = (LinearLayout) findViewById(R.id.homepage_item3);
		item4 = (LinearLayout) findViewById(R.id.homepage_item4);
		mIndicators.add(homeIcon);
		mIndicators.add(one);
		mIndicators.add(two);
		mIndicators.add(three);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetOtherTabsColor();
		// mIndicators.get(viewPager.getCurrentItem()).setIconAlpha(1.0f);
		// viewPager.setCurrentItem(0,false);
		clickTab(v, viewPager);
		Intent intent=null;
		switch (v.getId()) {
		
		case R.id.homepage_item1:
			intent=new Intent(this,SignUpActivity.class);
			break;
		case R.id.homepage_item2:
			intent=new Intent(this, PersonDataActivity.class);
			break;
		case R.id.homepage_item3:
			intent=new Intent(this,StatisticsActivity.class);
			break;
		case R.id.homepage_item4:
			intent=new Intent(this,ExceptionRecordActivity.class);
			break;
			
		default:
			break;
		}

	}

	private void clickTab(View v, ViewPager viewPager) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.id_indicator_home:
			mIndicators.get(0).setIconAlpha(1.0f);
			// bottom.setVisibility(View.GONE);
			viewPager.setCurrentItem(0, false);
			// Iterator<Long> it=tagList.iterator();
			// while(it.hasNext())
			// {
			// Log.e("tag",it.next().toString());
			// }

			break;
		case R.id.id_indicator_one:
			mIndicators.get(1).setIconAlpha(1.0f);
			// bottom.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(1, true);

			break;
		case R.id.id_indicator_two:
			mIndicators.get(2).setIconAlpha(1.0f);
			// bottom.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(2, true);

			break;
		case R.id.id_indicator_three:
			mIndicators.get(3).setIconAlpha(1.0f);
			// bottom.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(3, true);

			break;
		// case R.id.id_indicator_four:
		// mIndicators.get(4).setIconAlpha(1.0f);
		// // bottom.setVisibility(View.VISIBLE);
		// viewPager.setCurrentItem(4, true);
		//
		// break;
		default:
			break;

		}

	}

	private void resetOtherTabsColor() {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "resetTabcolor", 0).show();
		for (int i = 0; i < mIndicators.size(); i++) {
			mIndicators.get(i).setIconAlpha(0);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPiexls) {
		// Log.e("tag", "possion"+position+" positionOffset"+positionOffset);
		// viewPager.setPageTransformer(true,new CubeOutTransformer());
		if (positionOffset > 0) {
			ChangeIconColorWithText left = mIndicators.get(position);
			ChangeIconColorWithText right = mIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

		// if (viewPager.getCurrentItem() == 0) {
		// bottom.setVisibility(View.GONE);
		// } else {
		// bottom.setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder isExit = new AlertDialog.Builder(this);
		isExit.setTitle("系统提示");
		// 设置对话框消息
		isExit.setMessage("确定要退出吗");
		// 添加选择按钮并注册监听
		isExit.setPositiveButton("确定", listener);
		isExit.setNegativeButton("取消", listener);
		// 显示对话框
		isExit.create().show();

		// super.onBackPressed();
	}

	private void initWindow() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			// tintManager = new SystemBarTintManager(this);
			// tintManager.setStatusBarTintColor(R.color.green);
			// tintManager.setStatusBarTintEnabled(true);
		}
	}

	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int button) {
			// TODO Auto-generated method stub
			switch (button) {
			case AlertDialog.BUTTON_POSITIVE:
				dialog.dismiss();
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:
				dialog.dismiss();
				break;
			default:
				break;
			}
		}

	};

}
