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
		progressDialog = ProgressDialog.show(HealthPlanActivity.this, "���Ե�...", "��ȡ������...", true);
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
				double   bmi1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  //������λС˵
				String bmiStr="";
				if(bmi1<18.5)
				{
					bmiStr="���ع���,ע����ʳӪ����";
				}
				else if(bmi1>=18.5&&bmi1<24.99)
				{
					bmiStr="��׼���أ�ע�Ᵽ�֡�";
				}
				else if(bmi1>=24.99&&bmi1<28)
				{
					bmiStr="��΢���֣���ע����ʳ����";
				}
				else if(bmi1>=28&&bmi1<32)
				{
					bmiStr="�ǳ����֣���ע����ʳ��������ȡ���ʴ�ʩ��";
				}
				else if(bmi>=32)
				{
					bmiStr="���س��أ����鵽רҵҽԺ�������Ԥ������֢�ĳ��֡�";
				}
				
				sb.append("���ã����Ľ���״��������£�"+"\n");
				sb.append("\n");
				sb.append("��������ָ��(BMI)Ϊ"+bmi1+","+bmiStr+"\n");//���᣺����18.5 ������18.5-24.99 ���أ�25-28 ���֣�28-32 �ǳ����֣�����32
				sb.append("\n");
				sb.append("��ƽ��ÿ�촦�ھ�ֹ��ʱ��Ϊ18Сʱ27��43�룬"+"�ʱ����٣������μ������Ԥ����������"+"\n");
				sb.append("\n");
				sb.append("��ƽ��ÿ���˯��ʱ��Ϊ7Сʱ17��38�룬"+"˯��ʱ�������ע�Ᵽ�֡�"+"\n");
				sb.append("\n");
				sb.append("��ƽ��ÿ����˯ʱ��Ϊ1:23:14,"+"��˯ʱ�������˯������ܱ������彡����"+"\n"+
						"ƽ��ÿ����ʱ��Ϊ8:47:31,"+"��ʱ�����һ��֮�����ڳ�������˯����"+"");
				health_plan.setText(sb);
				progressDialog.dismiss();
			}}).start();
		
	}

}
