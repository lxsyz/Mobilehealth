package com.bbcc.mobilehealth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.UserModel;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PersonDataActivity extends Activity implements OnClickListener{
	
	private ImageView edit;
	//private Map<String, String> datalist = new HashMap<String,String>();
	private TextView edit_name_textview,edit_sex_textview,edit_height_textview,edit_weight_textview,
		edit_province_textview,edit_city_textview,edit_work_textview,edit_birthday_textview,
		edit_phonenumber_textview,edit_registertime_textview;
	private TextView changePwd;
	
	private Button bt_out;
	
	UserModel model = new UserModel();
	
	String phoneNumber,birthday,weight,height,profession,gender,userName,city,province;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user);
		initWindow();
		initView();
		
		
		
		
		//SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.user_list, new String[]{"text","data"},new int[]{R.id.text,R.id.data});
		//datalist.put("用户名", model.getName());
		
		//user_list.setAdapter(adapter);
	}
	
	
	
	private void initWindow() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
	
	private void initView() {
		edit_name_textview = (TextView)findViewById(R.id.userinfo_name);
		edit_sex_textview = (TextView)findViewById(R.id.userinfo_sex);
		edit_height_textview = (TextView)findViewById(R.id.userinfo_height);
		edit_weight_textview = (TextView)findViewById(R.id.userinfo_weight);
		edit_birthday_textview = (TextView)findViewById(R.id.userinfo_birthday);
		edit_city_textview = (TextView)findViewById(R.id.userinfo_city);
		edit_province_textview = (TextView)findViewById(R.id.userinfo_province);
		edit_work_textview = (TextView)findViewById(R.id.userinfo_work);
		edit_phonenumber_textview = (TextView)findViewById(R.id.userinfo_phonenum);
		changePwd = (TextView)findViewById(R.id.changePwd);
		bt_out = (Button)findViewById(R.id.bt_out);
		
		edit = (ImageView)findViewById(R.id.edit_userinfo);
		edit.setOnClickListener(this);
		changePwd.setOnClickListener(this);
		bt_out.setOnClickListener(this);
	}
	
	private void initData() {
		//queryUserInfo();
			//
			if (userName != null) {
				edit_name_textview.setText(userName);
				model.setName(userName);
			}
			if (birthday!= null && !birthday.equals("null") && !birthday.equals("0000-00-00")) {
				edit_birthday_textview.setText(birthday);
				model.setBirthday(birthday);
			}
			if (city != null && !city.equals("null")) {
				edit_city_textview.setText(city);
				model.setCity(city);
			}
			
			if (height != null && !height.equals("null") && !height.equals("0")) {
				edit_height_textview.setText(height);
				model.setHeight(height);
				
			}
			if (weight != null && !weight.equals("null") && !weight.equals("0")) {
			
				edit_weight_textview.setText(weight);
				model.setWeight(weight);
				
			}
			
			if (phoneNumber!=null &&!phoneNumber.equals("null")) {
				edit_phonenumber_textview.setText(phoneNumber+" ");
				model.setPhone(phoneNumber);
				
			
			}
			if (!province.equals("null")) {
				edit_province_textview.setText(province);
				model.setProvince(province);
			
			}
			if (!profession.equals("null")) {
				edit_work_textview.setText(profession);
				model.setWork(profession);
			}
			
			if (!gender.equals("null")) {
				if (gender.equals("1"))
					edit_sex_textview.setText("男");
				else edit_sex_textview.setText("女");
				model.setSex(gender);
			}
	}
	
	private void queryUserInfo() {
		SharedPreferences userSharedPreferences = getSharedPreferences("mobile_user", Activity.MODE_PRIVATE);
		
		//SharedPreferences.Editor  editor = userSharedPreferences.edit();
		
		birthday = userSharedPreferences.getString("birthday",null);
		weight = userSharedPreferences.getString("weight", null);
		height = userSharedPreferences.getString("height", null);
		phoneNumber = userSharedPreferences.getString("phoneNumber", Constant.PHONE_NUMBER);
		profession = userSharedPreferences.getString("profession", null);
		gender = userSharedPreferences.getString("gender", null);
		province = userSharedPreferences.getString("province", null);
		city = userSharedPreferences.getString("city", null);
		userName = userSharedPreferences.getString("userName", null);
		
		Log.d("birthday", "birthday" + birthday);
		//Date date = new Date();
		
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_userinfo:
			Intent intent = new Intent(PersonDataActivity.this,UserInfoActivity.class);
			intent.putExtra("user",model);
			startActivityForResult(intent, 20);
			break;
			
		case R.id.changePwd:
			Intent intent2 = new Intent(PersonDataActivity.this,ChangePasswordActivity.class);
			startActivity(intent2);
			break;
			
		case R.id.bt_out:
			new AlertDialog.Builder(PersonDataActivity.this).setTitle("确定要退出吗")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						Constant.PHONE_NUMBER = null;
						PersonDataActivity.this.finish();
					}
				}).show();
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		Log.d("onresume", "resume");
		queryUserInfo();
		initData();
		super.onResume();
	}
}
