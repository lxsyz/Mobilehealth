package com.bbcc.mobilehealth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.listener.UploadBatchListener;

import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.ProvinceHelper;
import com.bbcc.mobilehealth.util.UserModel;
import com.bbcc.mobilehealth.view.MyDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener{
	
	private RelativeLayout edit_name_layout,edit_sex_layout,edit_height_layout,edit_weight_layout,
							edit_province_layout,edit_city_layout,edit_work_layout,edit_birthday_layout,
							edit_phonenumber_layout,edit_registertime_layout;
	
	private TextView edit_name_textview,edit_sex_textview,edit_height_textview,edit_weight_textview,
						edit_province_textview,edit_city_textview,edit_work_textview,edit_birthday_textview,
						edit_phonenumber_textview,edit_registertime_textview;
	
	private TextView bt_right;
	private UserModel model;
	private String provinces;
	private String[] province = new String[34];
	private String[] provinceId = new String[34];
	private String id;
	
	private Dialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		initWindow();
		model = (UserModel)getIntent().getSerializableExtra("user");
		
		initView();
		
		initData();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Log.d("thread", "run");
				provinces = ProvinceHelper.getProvince();
				String[] strArray=provinces.split(",");  
		        for(int i=0;i<strArray.length;i++){  
		            String strArr=strArray[i].split("\\|")[1];
		            String idArr = strArray[i].split("\\|")[0];
		            province[i] = strArr;
		            provinceId[i] = idArr;
		        }
		        
		        if (edit_province_textview.getText()!=null &&(!edit_province_textview.getText().toString().equals(""))) {
	            	for (int j = 0;j < 34;j++) {
	            		//Log.d("province_textview", edit_province_textview.getText().toString()+" ");
	            		if (province[j].equals(edit_province_textview.getText().toString())) {
	            			id = provinceId[j];
	            			Message msg = new Message();
	    					msg.what = 0x11;
	    					handler.sendMessage(msg);
	            		}
	            	}
	            	
	            }
			}
		}).start();
		
	}
	
	private void initData() {
		if (null == model) {
			
		} else {
			if (model.getSex().equals("1")) {
				edit_sex_textview.setText("男");
			} else {
				edit_sex_textview.setText("女");
			}
			if (!model.getName().equals("null")&& model.getName() != null) {
				edit_name_textview.setText(model.getName()+"");
			}
			if (model.getBirthday()!=null && (!model.getBirthday().equals("null") )) {
				edit_birthday_textview.setText(model.getBirthday()+"");
			}
			if (model.getCity() != null &&(!model.getCity().equals("null")))
				//edit_city_textview.setText("");
				edit_city_textview.setText(model.getCity()+"");
			if (model.getHeight() != null && (!model.getHeight().equals("null"))) {
				edit_height_textview.setText(model.getHeight()+"");
			}
			if (model.getWeight() != null && (!model.getWeight().equals("null"))) 
				edit_weight_textview.setText(model.getWeight()+"");
			
			//if (model.getPhone() != null)
			//	edit_phonenumber_textview.setText(model.getPhone()+" ");
			if (model.getProvince() != null && (!model.getProvince().equals("null")))
				edit_province_textview.setText(model.getProvince());
			if (model.getWork() != null && (!model.getWork().equals("null")))
				edit_work_textview.setText(model.getWork()+"");
		}
	}
	
	private void initView () {
		Log.d("user_info", "initview");
		edit_name_layout = (RelativeLayout)findViewById(R.id.edit_userinfo_name);
		edit_sex_layout = (RelativeLayout)findViewById(R.id.edit_sex_name);
		edit_height_layout = (RelativeLayout)findViewById(R.id.edit_height_name);
		edit_weight_layout = (RelativeLayout)findViewById(R.id.edit_weight_name);
		edit_birthday_layout = (RelativeLayout)findViewById(R.id.edit_birthday_name);
		edit_city_layout = (RelativeLayout)findViewById(R.id.edit_city_name);
		edit_province_layout = (RelativeLayout)findViewById(R.id.edit_province_name);
		edit_work_layout = (RelativeLayout)findViewById(R.id.edit_work_name);
		//edit_phonenumber_layout = (RelativeLayout)findViewById(R.id.edit_phonenumber_name);
		
		//edit_phonenumber_textview = (TextView)findViewById(R.id.edit_phonenumber_textview);
		edit_name_textview = (TextView)findViewById(R.id.edit_name_textview);
		edit_sex_textview = (TextView)findViewById(R.id.edit_sex_textview);
		edit_height_textview = (TextView)findViewById(R.id.edit_height_textview);
		edit_weight_textview = (TextView)findViewById(R.id.edit_weight_textview);
		edit_birthday_textview = (TextView)findViewById(R.id.edit_birthday_textview);
		edit_city_textview = (TextView)findViewById(R.id.edit_city_textview);
		edit_province_textview = (TextView)findViewById(R.id.edit_province_textview);
		edit_work_textview = (TextView)findViewById(R.id.edit_work_textview);
		bt_right = (TextView)findViewById(R.id.bt_right);
		
		edit_name_layout.setOnClickListener(this);
		edit_sex_layout.setOnClickListener(this);
		edit_height_layout.setOnClickListener(this);
		edit_weight_layout.setOnClickListener(this);
		edit_birthday_layout.setOnClickListener(this);
		edit_city_layout.setOnClickListener(this);
		edit_province_layout.setOnClickListener(this);
		edit_work_layout.setOnClickListener(this);
		//edit_phonenumber_layout.setOnClickListener(this);
		edit_birthday_layout.setOnClickListener(this);
		bt_right.setOnClickListener(this);
	}

	private void initWindow() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_userinfo_name:
			startActivityForResult(new Intent(UserInfoActivity.this,EditActivity.class).putExtra("title", "用户名")
					.putExtra("tips", "请输入您的真实姓名")
					.putExtra("result", 21), 21);
			break;
			
		case R.id.edit_province_name:
			new AlertDialog.Builder(this).setTitle("请选择省份")
									.setItems(province, new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					edit_province_textview.setText(province[arg1]);
					id = provinceId[arg1];
					Message msg = new Message();
					msg.what = 0x11;
					handler.sendMessage(msg);
				}
			}).show();
			
			edit_city_textview.setText("");
			break;
			
		case R.id.edit_city_name:
			
			if ((edit_province_textview.getText().equals(" "))) {
				Toast.makeText(this, "请先选择所在省份", 2000).show();
			} else {
				
				Log.d("tag", arrayList.size()+" ");
				String[] s = new String[arrayList.size()];
				final String[] s2 = arrayList.toArray(s);
				new AlertDialog.Builder(this).setTitle("请选择城市")
					.setItems(s, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							edit_city_textview.setText(s2[arg1]);
						}
					})
				.show();
			}
			break;
		case R.id.edit_sex_name:
			final String[] sex = {"男","女"};
			new AlertDialog.Builder(this).setTitle("请选择性别")
										.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					edit_sex_textview.setText(sex[arg1]);
					arg0.dismiss();
				}
			}).show();
			break;
		case R.id.edit_work_name:
			startActivityForResult(new Intent(UserInfoActivity.this,EditActivity.class).putExtra("title", "职业")
					.putExtra("tips", "请输入您的职业")
					.putExtra("result", 23), 23);
			break;
		case R.id.edit_height_name:
			startActivityForResult(new Intent(UserInfoActivity.this,EditActivity.class).putExtra("title", "身高")
					.putExtra("tips", "请输入您的身高")
					.putExtra("result", 24), 24);
			break;
		case R.id.edit_weight_name:
			startActivityForResult(new Intent(UserInfoActivity.this,EditActivity.class).putExtra("title", "体重")
					.putExtra("tips", "请输入您的体重")
					.putExtra("result", 25), 25);
			break;
		case R.id.edit_birthday_name:
			View view = getLayoutInflater().inflate(R.layout.date_dialog,null);
			datePicker = (DatePicker)view.findViewById(R.id.date_picker);
			init(datePicker);
			ad = new AlertDialog.Builder(this).setTitle(dateTime)
					.setView(view)
					.setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                        
	                    	edit_birthday_textview.setText(dateTime); 
	                    }  
	                })  
	                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                          edit_birthday_textview.setText("");
	                    }  
	                }).show(); 
			break;
//		case R.id.edit_phonenumber_name:
//			startActivityForResult(new Intent(UserInfoActivity.this,EditActivity.class).putExtra("title", "手机号")
//					.putExtra("tips", "请输入您的电话号码")
//					.putExtra("result", 26), 26);
//			break;
			
		case R.id.bt_right:
			pd = new MyDialog(UserInfoActivity.this).loadDialog1();
			if (pd != null) pd.show();
			upload();

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String result = "";
		switch (resultCode) {
		case 21:
			result = data.getStringExtra("result");
			edit_name_textview.setText(result);
			break;
		case 23:
			result = data.getStringExtra("result");
			edit_work_textview.setText(result);
			break;
		case 24:
			result = data.getStringExtra("result");
			edit_height_textview.setText(result);
			break;
		case 25:
			result = data.getStringExtra("result");
			edit_weight_textview.setText(result);
			break;
		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//上传至服务器
	private void upload() {
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams reqParam = new RequestParams();
		try {
			reqParam.put("phoneNumber", Constant.PHONE_NUMBER);
			reqParam.put("userName", edit_name_textview.getText().toString());
			reqParam.put("province", edit_province_textview.getText().toString());
			reqParam.put("city", edit_city_textview.getText().toString());
			reqParam.put("birthday", edit_birthday_textview.getText().toString());
			reqParam.put("profession", edit_work_textview.getText().toString());
			if (edit_sex_textview.getText().toString().equals("男"))
				reqParam.put("gender", 1);
			else reqParam.put("gender", 2);
			reqParam.put("height",Integer.parseInt(edit_height_textview.getText().toString()));
			reqParam.put("weight", Integer.parseInt(edit_weight_textview.getText().toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpClient.post("http://"+Constant.SERVER_URL+"updateuserinfo.php", reqParam, new AsyncHttpResponseHandler() {


			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable error) {
				pd.dismiss();
				Toast.makeText(UserInfoActivity.this, "连不上服务器，修改失败",2000).show();
				error.printStackTrace();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				pd.dismiss();
				String response = new String(arg2);
				JSONObject jsonObject;
				try {
					Log.d("status",arg0+" ");
					Log.d("",response+" ");
					jsonObject = new JSONObject(response);
					
					String res = jsonObject.getString("code");
					
					//String data = jsonObject.getString("data");
					
					
					if (res.equals("1")) {
						JSONObject data = jsonObject.getJSONObject("data");
						Log.d("data", data+" ");
						Toast.makeText(UserInfoActivity.this, "修改成功", 2000).show();
					
						SharedPreferences userSharedPreferences = getSharedPreferences("mobile_user", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = userSharedPreferences.edit();
						
						editor.putString("birthday",data.getString("birthday"));
						editor.putString("weight", data.getString("weight"));
						editor.putString("height", data.getString("height"));
						editor.putString("phoneNumber", data.getString("phoneNumber"));
						editor.putString("profession", data.getString("profession"));
						editor.putString("province", data.getString("province"));
						editor.putString("gender", data.getString("gender"));
						editor.putString("userName", data.getString("userName"));
						editor.putString("city", data.getString("city"));
						
						editor.commit();
						UserInfoActivity.this.finish();
						//Intent data = new Intent();
						//data.putExtra("result", result);
						//setResult(, data)
						
					} else if(res.equals("0")){
						Toast.makeText(UserInfoActivity.this, "您没有修改信息",2000).show();
						UserInfoActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
			
	}
	
	//private String[] array2;
	private ArrayList<String> arrayList = new ArrayList<String>();
	
	private DatePicker datePicker;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			arrayList.clear();
			if (msg.what == 0x11) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Log.d("id",id+" ");
						String[] array2 = ProvinceHelper.getCity(id).split(",");
						for (int i = 0; i < array2.length; i++) {
							arrayList.add(array2[i].split("\\|")[1] + "市");
						}
					}
				}).start();
			}
		};
	};
	
	private void init(DatePicker datePicker) {
		Calendar calendar = Calendar.getInstance();
		String initDateTime = calendar.get(Calendar.YEAR) + "年"
								+ calendar.get(Calendar.MONTH) + "月"
								+ calendar.get(Calendar.DAY_OF_MONTH) + "日";
		
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar calendar2 = Calendar.getInstance();
				calendar2.set(view.getYear(),view.getMonth(),  
						view.getDayOfMonth());  
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		  
		        dateTime = sdf.format(calendar2.getTime());
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		UserInfoActivity.this.finish();
		super.onBackPressed();
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();

		Log.d("userinfo", "resume");
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				provinces = ProvinceHelper.getProvince();
//				String[] strArray=provinces.split(",");  
//		        for(int i=0;i<strArray.length;i++){  
//		            String strArr=strArray[i].split("\\|")[1];
//		            String idArr = strArray[i].split("\\|")[0];
//		            province[i] = strArr;
//		            provinceId[i] = idArr;
		            
//		        }
//			}
//		}).start();
	
	}
	private String dateTime;
	private AlertDialog ad;
}
