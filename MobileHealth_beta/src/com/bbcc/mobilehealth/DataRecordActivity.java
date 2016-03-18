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
	private int currentPage = 1; //Ĭ���ڵ�һҳ
    private int allRecorders = 0;  //ȫ����¼��
	private int pageSize = 1;  //Ĭ�Ϲ�һҳ
	private int lastItem;
	private MyBaseAdapter baseAdapter;
	private static final int lineSize = 25;    //ÿ����ʾ��
	
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
		userId=BmobUser.getCurrentUser(this).getObjectId();//��ȡ��ǰ�û���Id
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
		loadInfo.setText("Ŭ�������С�����");
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
	    //������ҳ��
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
	    	Toast.makeText(this, "����û�����ݣ�", Toast.LENGTH_LONG).show();
	    }
//	   
   	    
	}
	
	public void appendData(){
		    System.out.println("currentPage:"+currentPage);
		    ArrayList<String> additems = service.getAllItems(userId,currentPage, lineSize);
		    System.out.println("additems.size();"+  additems.size());
		    baseAdapter.setCount(baseAdapter.getCount() + additems.size());
		    //�жϣ����������ĩβ��ȥ�������ڼ��ء�
		   
		    if(allRecorders == baseAdapter.getCount()){
		      listView.removeFooterView(loadLayout);
		    }
		    items.addAll(additems);
		    //֪ͨ��¼�ı�
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
		
		lastItem = firstVisible + visibleCount - 1; //ͳ���Ƿ����
		System.out.println(lastItem);
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scorllState) {
		// TODO Auto-generated method stub
		System.out.println("�������������");
	    //�Ƿ���ײ���������û����
	    if(lastItem == baseAdapter.getCount() 
	        && currentPage < pageSize    //���ٹ���
	        && scorllState == OnScrollListener.SCROLL_STATE_IDLE){
	      currentPage =currentPage+1;
	      //������ʾλ��
	      listView.setSelection(lastItem);
	      //��������
	      appendData();
	      System.out.println("���ص�"+currentPage+"����");
	    }
	    else{
	    	System.out.println("���ݼ��������");
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
