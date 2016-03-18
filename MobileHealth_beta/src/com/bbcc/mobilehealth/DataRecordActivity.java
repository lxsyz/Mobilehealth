package com.bbcc.mobilehealth;

import java.util.ArrayList;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.util.DatabaseHelper;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DataRecordActivity extends Activity implements  OnScrollListener {

	private String userId;
	
	private ImageButton data_back;
	
	private TextView loadInfo;
	private ListView listView;
	private LinearLayout loadLayout;
	private ArrayList<String> items;
	private DatabaseHelper service;
	private int currentPage = 1; //默认在第一页
    private int allRecorders = 0;  //全部记录数
	private int pageSize = 1;  //默认共一页
	private int lastItem;
	private MyBaseAdapter baseAdapter;
	private static final int lineSize = 25;    //每次显示数
	
//	private String selDateString;
//	
//	private Button getCouldInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.data_record);
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "b51e3eb59f6e65de81096688de413362");
		
		data_back=(ImageButton) findViewById(R.id.data_back);
		userId=BmobUser.getCurrentUser(this).getObjectId();//获取当前用户的Id
		Log.i("tag",userId);
		data_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		listView=(ListView)findViewById(R.id.listView1);
		loadLayout=new LinearLayout(this);
		loadLayout.setGravity(Gravity.CENTER);
		loadInfo=new TextView(this);
		loadInfo.setText("努力加载中。。。");
		loadInfo.setGravity(Gravity.CENTER);
		loadLayout.addView(loadInfo,new LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		listView.addFooterView(loadLayout);
		listView.setOnScrollListener(this);
		showAllData();
	}

	public void showAllData() {
		// TODO Auto-generated method stub
		service = new DatabaseHelper(this);
	    allRecorders = service.getCount(userId);
	    //计算总页数
	    pageSize = (allRecorders + lineSize -1) / lineSize;  
	    System.out.println("allRecorders =  " + allRecorders);
	    System.out.println("pageSize  =  " + pageSize);
	    items = service.getAllItems(userId, currentPage, lineSize);
	    if(items.size()>0)
	    {
//	    for(int i=0; i<items.size(); i++){
//	      System.out.println(items.get(i));
//	      
//	    }
	    System.out.println("itmes.size():"+items.size());
	    baseAdapter = new MyBaseAdapter(items.size());
		listView.setAdapter(baseAdapter);
		if(items.size()<lineSize)
		{
			listView.removeFooterView(loadLayout);
		}
	    }
	    else
	    {
	    	Toast.makeText(this, "本机没有数据！", Toast.LENGTH_LONG).show();
	    }
//	   
   	    
	}
	
	public void appendData(){
		    System.out.println("currentPage:"+currentPage);
		    ArrayList<String> additems = service.getAllItems(userId,currentPage, lineSize);
		    System.out.println("additems.size();"+  additems.size());
		    baseAdapter.setCount(baseAdapter.getCount() + additems.size());
		    //判断，如果到了最末尾则去掉“正在加载”
		   
		    if(allRecorders == baseAdapter.getCount()){
		      listView.removeFooterView(loadLayout);
		    }
		    items.addAll(additems);
		    //通知记录改变
		    baseAdapter.notifyDataSetChanged();
		    for(int i=0; i<additems.size(); i++){
			      System.out.println(additems.get(i));
			    }
		    System.out.println("baseAdapter:"+baseAdapter.getCount());
		  }

	

	@Override
	public void onScroll(AbsListView view, int firstVisible, int visibleCount,
		      int totalCount) {
		// TODO Auto-generated method stub
		
		lastItem = firstVisible + visibleCount - 1; //统计是否到最后
		System.out.println(lastItem);
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scorllState) {
		// TODO Auto-generated method stub
		System.out.println("进入滚动界面了");
	    //是否到最底部并且数据没读完
	    if(lastItem == baseAdapter.getCount() 
	        && currentPage < pageSize    //不再滚动
	        && scorllState == OnScrollListener.SCROLL_STATE_IDLE){
	      currentPage =currentPage+1;
	      //设置显示位置
	      listView.setSelection(lastItem);
	      //增加数据
	      appendData();
	      System.out.println("加载第"+currentPage+"数据");
	    }
	    else{
	    	System.out.println("数据加载已完毕");
	    }
	}
	private class MyBaseAdapter extends BaseAdapter {
	    int count = 0; /* starting amount */
	    public MyBaseAdapter(int count)
	    {
	    	this.count=count;
	    }
	    public int getCount() {
	      return count;
	    } 
	    
	    public void setCount(int count){
	      this.count = count;
	    }

	    public Object getItem(int pos) {
	      return pos;
	    }

	    public long getItemId(int pos) {
	      return pos;
	    }

	    public View getView(int pos, View v, ViewGroup p) {
	      TextView view = new TextView(DataRecordActivity.this);
	      view.setTextSize(18);
	      view.setTextColor(Color.BLACK);
	      if(items != null){
	        view.setText(items.get(pos));
	      }else{
	        view.setText(pos);
	      }
	      return view;
	    }
	  }

}
