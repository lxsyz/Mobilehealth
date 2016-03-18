package com.bbcc.mobilehealth.fragment;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.BmobUser;

import com.bbcc.mobilehealth.DataRecordActivity;
import com.bbcc.mobilehealth.ExceptionRecordActivity;
import com.bbcc.mobilehealth.HeartRateShowActivity;
import com.bbcc.mobilehealth.MainActivity;
import com.bbcc.mobilehealth.StatisticsActivity;
import com.bbcc.mobilehealth.HealthPlanActivity;
import com.bbcc.mobilehealth.PersonDataActivity;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.SettingActivity;
import com.bbcc.mobilehealth.SignInActivity;
import com.bbcc.mobilehealth.SignUpActivity;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.bbcc.mobilehealth.util.MyUser;
import com.bbcc.mobilehealth.util.SystemBarTintManager;
import com.bbcc.mobilehealth.view.CircleImageView;
import com.bbcc.mobilehealth.view.CircleLayout;
import com.bbcc.mobilehealth.view.CircleLayout.OnItemClickListener;
import com.bbcc.mobilehealth.view.CircleLayout.OnItemSelectedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements  OnClickListener {
	private ViewPager viewPager;
	private LinearLayout item1;
	private LinearLayout item2;
	private LinearLayout item3;
	private LinearLayout item4;
	private LinearLayout item5;
	private TextView login;
	
	private SystemBarTintManager tintManager;
	private ImageHandler handler = new ImageHandler(new WeakReference<HomeFragment>(this));
	private class ImageAdapter extends PagerAdapter{
        
        private ArrayList<ImageView> viewlist;
 
        public ImageAdapter(ArrayList<ImageView> viewlist) {
            this.viewlist = viewlist;
        }
 
        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }
 
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
         @Override  
         public void destroyItem(ViewGroup container, int position,  
                 Object object) {  
             //Warning：不要在这里调用removeView
         }  
         @Override  
         public Object instantiateItem(ViewGroup container, int position) {
             //对ViewPager页号求模取出View列表中要显示的项
             position %= viewlist.size();
             if (position<0){
                 position = viewlist.size()+position;
             }
             ImageView view = viewlist.get(position);
             //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
             ViewParent vp =view.getParent();
             if (vp!=null){
                 ViewGroup parent = (ViewGroup)vp;
                 parent.removeView(view);
             }
             container.addView(view);  
             //add listeners here if necessary
             return view;  
         }  
    }
	private static class ImageHandler extends Handler{
        
        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE  = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT   = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT  = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED  = 4;
         
        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;
         
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<HomeFragment> weakReference;
        private int currentItem = 0;
         
        protected ImageHandler(WeakReference<HomeFragment> wk){
            weakReference = wk;
        }
         
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeFragment homeFragment = weakReference.get();
            if (homeFragment==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
//            检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            switch (msg.what) {
            case MSG_UPDATE_IMAGE:
                currentItem++;
                homeFragment.viewPager.setCurrentItem(currentItem);
                //准备下次播放
                homeFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_KEEP_SILENT:
                //只要不发送消息就暂停了
                break;
            case MSG_BREAK_SILENT:
            	homeFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_PAGE_CHANGED:
                //记录当前的页号，避免播放的时候页面显示不正确。
                currentItem = msg.arg1;
                break;
            default:
                break;
            } 
            if (homeFragment.handler.hasMessages(MSG_UPDATE_IMAGE)){
            	homeFragment.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
        }
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("tag", "Homepage:onCreateView");
		return inflater.inflate(R.layout.frag_homepage, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
//		initWindow();
		ActionBar actionBar=getActivity().getActionBar();
		//初始化iewPager的内容
        viewPager = (ViewPager) getActivity().findViewById(R.id.homepage_viewpager);
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        ImageView view1 = (ImageView) inflater.inflate(R.layout.viewpage_imageview, null);
        ImageView view2 = (ImageView) inflater.inflate(R.layout.viewpage_imageview, null);
        ImageView view3 = (ImageView) inflater.inflate(R.layout.viewpage_imageview, null);
        view1.setImageResource(R.drawable.homeviewpage1);
        view2.setImageResource(R.drawable.homeviewpage2);
        view3.setImageResource(R.drawable.homeviewpage3);
        ArrayList<ImageView> views = new ArrayList<ImageView>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPager.setAdapter(new ImageAdapter(views));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
             
            //配合Adapter的currentItem字段进行设置。
            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
            }
             
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
             
            //覆写该方法实现轮播效果的暂停和恢复
            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                    break;
                default:
                    break;
                }
            }
        });
        viewPager.setCurrentItem(0);//默认在中间，使用户看不到边界
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE,ImageHandler.MSG_DELAY);
	}
	
	private void initData() {
		item1=(LinearLayout) getActivity().findViewById(R.id.homepage_item1);
		item2=(LinearLayout) getActivity().findViewById(R.id.homepage_item2);
		item3=(LinearLayout) getActivity().findViewById(R.id.homepage_item3);
		item4=(LinearLayout) getActivity().findViewById(R.id.homepage_item4);
		item5=(LinearLayout) getActivity().findViewById(R.id.homepage_item5);
		login=(TextView)getActivity().findViewById(R.id.login);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		MyDBHelp myDbHelp;
		SQLiteDatabase db;
		switch(view.getId())
		{
		case R.id.homepage_item1:
			if (login.getText().equals("个人中心")) {
				intent = new Intent(getActivity(),PersonDataActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(),SignInActivity.class).putExtra("result", 20);
				startActivityForResult(intent, 20);
			}
			break;
		case R.id.homepage_item2:
			intent = new Intent(getActivity(),HeartRateShowActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("phonenumber", Constant.PHONE_NUMBER+" '");
		if (!TextUtils.isEmpty(Constant.PHONE_NUMBER)) {
			login.setText("个人中心");
		}
	}

}
