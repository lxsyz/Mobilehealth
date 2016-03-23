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
import com.bbcc.mobilehealth.util.MyUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.bbcc.mobilehealth.util.ExceptionTable;
import com.bbcc.mobilehealth.util.HAA;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.bbcc.mobilehealth.util.StatisticsTable;

public class SettingActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	private ToggleButton setting_toggleButton1;//wifi自动上传
	private ToggleButton setting_toggleButton2;//上传数据后自动删除
	private ToggleButton setting_toggleButton3;//接收健康方案推送
	private ToggleButton setting_toggleButton4;
	private ToggleButton setting_toggleButton5;
	private Button setting_upload;//上传本机数据
	private Button setting_delete;//删除本机数据
	private ImageButton setting_back;
	private SharedPreferences settings ;
	private SharedPreferences.Editor editor;
	private boolean isAutoDele;
	private boolean isUpLoad;
	private boolean isAcceptPush;
	private boolean isAutoAction;
	private boolean isAutoLocation;
	
	private String userId;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity resume");
		super.onResume();
		
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity Destroy");
		super.onDestroy();
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity Pause");
		super.onPause();
	}



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity Start");
		super.onStart();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity Stop");
		super.onStop();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("tag", "SettingActivity Creat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		Bmob.initialize(this, "b51e3eb59f6e65de81096688de413362");
		settings= getSharedPreferences("setting", 0);
		editor=settings.edit();
		isUpLoad=settings.getBoolean("isUpLoad", false);
		isAutoDele=settings.getBoolean("isAutoDele", false);
		isAcceptPush=settings.getBoolean("isAcceptPush", false);
		isAutoAction=settings.getBoolean("isAutoAction", false);
		isAutoLocation=settings.getBoolean("isAutoLocation", false);
		
		setting_toggleButton1=(ToggleButton) findViewById(R.id.setting_toggleButton1);
		setting_toggleButton2=(ToggleButton) findViewById(R.id.setting_toggleButton2);
		setting_toggleButton3=(ToggleButton) findViewById(R.id.setting_toggleButton3);
		setting_toggleButton4=(ToggleButton) findViewById(R.id.setting_toggleButton4);
		setting_toggleButton5=(ToggleButton) findViewById(R.id.setting_toggleButton5);
		setting_back=(ImageButton) findViewById(R.id.setting_back);
		setting_toggleButton1.setChecked(isUpLoad);
		setting_toggleButton2.setChecked(isAutoDele);
		setting_toggleButton3.setChecked(isAcceptPush);
		setting_toggleButton4.setChecked(isAutoAction);
		setting_toggleButton5.setChecked(isAutoLocation);
		
		setting_back.setOnClickListener(this);
		
		setting_upload=(Button) findViewById(R.id.setting_upload);
		setting_delete=(Button) findViewById(R.id.setting_delete);
		setting_upload.setOnClickListener(this);
		setting_delete.setOnClickListener(this);
		
		
		setting_toggleButton1.setOnCheckedChangeListener(this);
		setting_toggleButton2.setOnCheckedChangeListener(this);
		setting_toggleButton3.setOnCheckedChangeListener(this);
		setting_toggleButton4.setOnCheckedChangeListener(this);
		setting_toggleButton5.setOnCheckedChangeListener(this);
	    if(BmobUser.getCurrentUser(this,MyUser.class)!=null)
	    {
		userId=BmobUser.getCurrentUser(this,MyUser.class).getObjectId();
	    }
	    else
	    {
	    	userId="";
	    }
		
		Log.i("tag",userId);
	
	}
	@Override
	public void onCheckedChanged(CompoundButton bt, boolean arg1) {
		// TODO Auto-generated method stub
		switch(bt.getId())
		{
		case R.id.setting_toggleButton1:
		if(arg1)
		{
			editor.putBoolean("isUpLoad", true);
			Toast.makeText(this, "xiugaichenggong", Toast.LENGTH_LONG).show();
		}
		else
		{
			editor.putBoolean("isUpLoad", false);
		}
		break;
		case R.id.setting_toggleButton2:
			if(arg1)
			{
				editor.putBoolean("isAutoDele", true);
			}
			else
			{
				editor.putBoolean("isAutoDele", false);
			}
		break;
		case R.id.setting_toggleButton3:
			if(arg1)
			{
				editor.putBoolean("isAcceptPush", true);
			}
			else
			{
				editor.putBoolean("isAcceptPush", false);
			}
			break;
		case R.id.setting_toggleButton4:
			if(arg1)
			{
				editor.putBoolean("isAutoAction", true);
			}
			else
			{
				editor.putBoolean("isAutoAction", false);
			}
			break;
		case R.id.setting_toggleButton5:
			if(arg1)
			{
				editor.putBoolean("isAutoLocation", true);
			}
			else
			{
				editor.putBoolean("isAutoLocation", false);
			}
			break;
			default:
				
				break;
		}
		editor.commit();
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.setting_upload:
			ConnectivityManager connectMgr = (ConnectivityManager) this
	        .getSystemService(Context.CONNECTIVITY_SERVICE);
	 
			NetworkInfo info = connectMgr.getActiveNetworkInfo();
			if(info==null)
			{
				Toast.makeText(this, "未联网,请联网后上传数据！", Toast.LENGTH_LONG).show();
			}
			else if(info.getType() == ConnectivityManager.TYPE_WIFI)
			{
				Toast.makeText(this, "当前网络为Wifi，可以放心上传数据", Toast.LENGTH_LONG).show();
				
				//上传数据
				 new upLoadTask().execute();
				
			}
			else if(info.getType() ==ConnectivityManager.TYPE_MOBILE)
			{
				//提示框
				AlertDialog.Builder builder = new Builder(this);
				  builder.setMessage("当前网络为手机网络，是否继续上传数据");
				  builder.setTitle("提示");
				  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int witch) {
					// TODO Auto-generated method stub
					 dialog.dismiss();	 
					 //上传数据
					 new upLoadTask().execute();
				}
				  });
				  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int witch) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}});
			}
			break;
		case R.id.setting_delete:
			  AlertDialog.Builder builder = new Builder(this);
			  System.out.println("删除数据");
			  builder.setMessage("本机数据删除后不可恢复，确认删除？");
			  builder.setTitle("提示");
			  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int witch) {
				// TODO Auto-generated method stub
				 dialog.dismiss();	 
				 //删除数据
				    MyDBHelp myDbHelp=new MyDBHelp(SettingActivity.this);
					SQLiteDatabase db= myDbHelp.getWritableDatabase();
					db.execSQL("delete from "+MyDBHelp.HAATABLE+";");
					db.execSQL("delete from "+MyDBHelp.EXCEPTIONTABLE+";");	
					db.execSQL("delete from "+MyDBHelp.STATISTICSTABLE+";");
				    db.execSQL("DELETE FROM sqlite_sequence;");
					db.close();
			}
			  });
			  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int witch) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}});
			  builder.create().show();
			break;
		case R.id.setting_back:
			finish();
			break;
			default:
				break;
		}
	}
	//上传数据
	private class upLoadTask extends AsyncTask <Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... v) {
			// TODO Auto-generated method stub
			MyDBHelp myDbHelp=new MyDBHelp(SettingActivity.this);
			int count=0;
			BmobObject tempBo=new BmobObject();
			SQLiteDatabase db= myDbHelp.getWritableDatabase();
			Cursor cursor = null;
			SimpleDateFormat sdf;
			Date date = null;
			sdf = new SimpleDateFormat("yyyy-MM-DD ahh:mm:ss");
			
			//上传HAA表数据
			//查询HAA表中所有数据
			cursor = db.rawQuery("select * from "+MyDBHelp.HAATABLE+" where objectId=?",new String[]{userId});
			List<BmobObject> bmobObjects=new ArrayList<BmobObject>();
			if(cursor.getCount()==0)
			{
				//Toast.makeText(SettingActivity.this, "数据已删除！", Toast.LENGTH_LONG).show();
				Log.i("tag",  "HAA表数据已删除！");
			}
			else
			{
				
			cursor.moveToFirst();
			HAA haa1=new HAA();
			do{
			//
				if(count==50)
				{
					tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub
							Log.i("tag","批量添加HAA数据失败:"+code+" "+msg);
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Log.i("tag","批量添加HAA数据成功！");
							
						}});
					bmobObjects.clear();
					count=0;
				}
		    haa1=new HAA();
			try {
				date=sdf.parse(cursor.getString(1)+" "+cursor.getString(2));
				haa1.setTime(new BmobDate(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			haa1.setBmp(""+cursor.getFloat(3));
			haa1.setAction(cursor.getString(4));
			haa1.setUserId(userId);
			bmobObjects.add(haa1);
			count++;
			haa1=null;
			}
			while(cursor.moveToNext());
/*			haa1.save(SettingActivity.this, new SaveListener() {
			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			        Log.i("tag","添加数据成功"); 
			    }
			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			        // 添加失败
			    	Log.i("tag","添加数据失败:"+msg);
			    }
			});*/ 
			}
			//批量插入数据
			tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					Log.i("tag","批量添加数据失败:"+code+" "+msg);
				}

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Log.i("tag","批量添加数据成功！");
					
				}});
			  bmobObjects.clear();
			  count=0;
			  
			//上传异常表
				ExceptionTable execTable=new ExceptionTable();
				
					cursor = db.rawQuery("select * from "+MyDBHelp.EXCEPTIONTABLE+" where objectId=?",new String[]{userId});
					if(cursor.getCount()==0)
					{
						Log.i("tag",  "HAA表数据已删除！");
					}
					else
					{
					cursor.moveToFirst();
					do{
						if(count==50)
						{
							tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

								@Override
								public void onFailure(int code, String msg) {
									// TODO Auto-generated method stub
									Log.i("tag","批量excp添加数据失败:"+code+" "+msg);
								}

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									Log.i("tag","批量excp添加数据成功！");
									
								}});
							bmobObjects.clear();
							count=0;
						}
						sdf = new SimpleDateFormat("yyyy-MM-DD ahh:mm:ss");
				    try {
				    	
				    	date=sdf.parse(cursor.getString(1)+" "+cursor.getString(2));
				    	execTable.setTime(new BmobDate(date));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					execTable.setBmp(""+cursor.getFloat(3));
					execTable.setAction(cursor.getString(4));
					execTable.setReason(cursor.getString(5));
					execTable.setUserId(userId);
					bmobObjects.add(execTable);
					count++;
				/*	execTable.save(SettingActivity.this, new SaveListener() {
					    @Override
					    public void onSuccess() {
					        // TODO Auto-generated method stub
					        //Log.i("tag","添加数据成功"); 
					    }
					    @Override
					    public void onFailure(int code, String msg) {
					        // TODO Auto-generated method stub
					        // 添加失败
					    	Log.i("tag","添加数据失败:"+msg);
					    }
					});*/
					}
					while(cursor.moveToNext());
						
					tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						Log.i("tag","批量excp添加数据失败:"+code+" "+msg);
					}
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.i("tag","批量excp添加数据成功！");
						
					}});
				  bmobObjects.clear();
				  count=0;
					}
				  
			//上传统计表
			    StatisticsTable statisticsTable=new StatisticsTable();
				cursor = db.rawQuery("select * from "+MyDBHelp.STATISTICSTABLE+" where objectId=?",new String[]{userId});
				if(cursor.getCount()==0)
				{
					//Toast.makeText(SettingActivity.this, "数据已删除！", Toast.LENGTH_LONG).show();
					Log.i("tag",  "Statistics表数据已删除！");
				}
				else
				{
				cursor.moveToFirst();
				do{
					if(count==50)
					{
						tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

							@Override
							public void onFailure(int code, String msg) {
								// TODO Auto-generated method stub
								Log.i("tag","批量添加Statistics数据失败:"+code+" "+msg);
							}

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								Log.i("tag","批量添加Statistics数据成功！");
								
							}});
						bmobObjects.clear();
						count=0;
					}
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				statisticsTable.setStep(cursor.getInt(1));
				statisticsTable.setDaySit(cursor.getInt(2));
				statisticsTable.setDayWalk(cursor.getInt(3));
				statisticsTable.setDayRun(cursor.getInt(4));
				statisticsTable.setDayDown(cursor.getInt(6));
				statisticsTable.setDayUp(cursor.getInt(5));
				try {
					date=sdf.parse(cursor.getString(7));
					statisticsTable.setDate(new BmobDate(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				statisticsTable.setUserId(userId);
				bmobObjects.add(statisticsTable);
				count++;
			/*	statisticsTable.save(SettingActivity.this, new SaveListener() {
				    @Override
				    public void onSuccess() {
				        // TODO Auto-generated method stub
				        //Log.i("tag","添加数据成功"); 
				    }
				    @Override
				    public void onFailure(int code, String msg) {
				        // TODO Auto-generated method stub
				        // 添加失败
				    	Log.i("tag","添加数据失败:"+msg);
				    }
				});*/
				}
				while(cursor.moveToNext());
				tempBo.insertBatch(SettingActivity.this, bmobObjects, new SaveListener(){

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						Log.i("tag","批量添加Statistics数据失败:"+code+" "+msg);
					}
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.i("tag","批量添加Statistics数据成功！");
						
					}});
				  bmobObjects.clear();
				  count=0;
				}
				
				
			cursor.close();
			db.close();	
			return null;	
			
		}
	     
	 }

}
