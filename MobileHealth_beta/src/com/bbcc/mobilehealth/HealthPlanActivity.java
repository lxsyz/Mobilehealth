package com.bbcc.mobilehealth;

import java.math.BigDecimal;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.util.MyUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class HealthPlanActivity extends Activity {

	private ImageButton plan_back;
	private TextView health_plan;
	private ProgressDialog progressDialog = null;
	private MyUser currenUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.health_plan);
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "b51e3eb59f6e65de81096688de413362");
		currenUser=BmobUser.getCurrentUser(this, MyUser.class);
		plan_back=(ImageButton) findViewById(R.id.plan_back);
		health_plan=(TextView) findViewById(R.id.health_plan);
		plan_back.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}});
		progressDialog = ProgressDialog.show(HealthPlanActivity.this, "请稍等...", "获取数据中...", true);
		update();
		
	}
	private void update() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringBuffer sb=new StringBuffer();
				String userId=currenUser.getObjectId();
				String weightStr=currenUser.getWeight();
				String heightStr=currenUser.getHeight();
				double weight=Double.parseDouble(weightStr)/2;//kg
				double height=Double.parseDouble(heightStr)/100;//m
				double bmi=weight/(Math.pow(height, 2)); 
				BigDecimal   b   =   new   BigDecimal(bmi);  
				double   bmi1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  //保留两位小说
				String bmiStr="";
				if(bmi1<18.5)
				{
					bmiStr="体重过轻,注意饮食营养。";
				}
				else if(bmi1>=18.5&&bmi1<24.99)
				{
					bmiStr="标准体重，注意保持。";
				}
				else if(bmi1>=24.99&&bmi1<28)
				{
					bmiStr="稍微肥胖，请注意饮食合理。";
				}
				else if(bmi1>=28&&bmi1<32)
				{
					bmiStr="非常肥胖，请注意饮食合理，并采取减肥措施。";
				}
				else if(bmi>=32)
				{
					bmiStr="严重超重，建议到专业医院检查身体预防并发症的出现。";
				}
				
				sb.append("您好，您的健康状况监测如下："+"\n");
				sb.append("\n");
				sb.append("您的体重指数(BMI)为"+bmi1+","+bmiStr+"\n");//过轻：低于18.5 正常：18.5-24.99 过重：25-28 肥胖：28-32 非常肥胖：高于32
				sb.append("\n");
				sb.append("您平均每天处于静止的时间为18小时27分43秒，"+"活动时间过少，建议多参加体育活动预防疾病发生"+"\n");
				sb.append("\n");
				sb.append("您平均每天的睡眠时间为7小时17分38秒，"+"睡眠时间合理，请注意保持。"+"\n");
				sb.append("\n");
				sb.append("您平均每天入睡时间为1:23:14,"+"入睡时间过晚，早睡早起才能保持身体健康，"+"\n"+
						"平均每天起床时间为8:47:31,"+"起床时间过晚，一日之计在于晨，请早睡早起！"+"");
				health_plan.setText(sb);
				progressDialog.dismiss();
			}}).start();
		
	}

}
