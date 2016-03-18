package com.bbcc.mobilehealth;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbcc.mobilehealth.fragment.HomeFragment;
import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.MD5;
import com.bbcc.mobilehealth.util.MyUser;
import com.bbcc.mobilehealth.view.ClearEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnClickListener {

	private Button up_sign_up;
	private ClearEditText up_username;
	private ClearEditText up_password;
	private ClearEditText sure_up_password;
	private ClearEditText up_name;
	private Intent resultIntent;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		
		initView();
	}

	public void initView() {
		up_username = (ClearEditText)findViewById(R.id.up_username);
		up_password = (ClearEditText)findViewById(R.id.up_password);
		sure_up_password = (ClearEditText)findViewById(R.id.up_confirm_password);
		up_sign_up = (Button)findViewById(R.id.up_sign_up);
		up_name = (ClearEditText)findViewById(R.id.up_name);
		
		up_sign_up.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.up_sign_up:
			
			if (up_username.getText().toString().equals("")) {
				Toast.makeText(SignUpActivity.this, "用户名不能为空", 2000).show();
				return;
			}
			
			if (up_password.getText().toString().equals("")) {
				Toast.makeText(SignUpActivity.this, "密码不能为空", 2000).show();
				return;
			}
			if (up_name.getText().toString().equals("")) {
				Toast.makeText(SignUpActivity.this, "名字不能为空", 2000).show();
				return;
			}
			if (!up_password.getText().toString().equals(sure_up_password.getText().toString()))
			{
				Toast.makeText(SignUpActivity.this, "两次密码输入不相符", 2000).show();
				return;
			}
			up_sign_up.setText("注册中...");
			up_sign_up.setClickable(false);
			up_sign_up.setBackgroundResource(R.drawable.yuanjiao2);
			RequestParams params = new RequestParams();
			params.put("phoneNumber", (up_username.getText().toString()));
			params.put("password", MD5.MD5Encode(up_password.getText().toString()));
			params.put("userName", up_name.getText().toString());
			AsyncHttpClient client = new AsyncHttpClient();
			String url = "http://" + Constant.SERVER_URL + "register.php";
			client.post(url, params,new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					String response = new String(arg2);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(response);
						
						String res = jsonObject.getString("code");
						Log.d("status",arg0+" ");
						Log.d("res", res);
						if (res.equals("2")) {
							Toast.makeText(SignUpActivity.this, "注册成功", 2000).show();
							SignUpActivity.this.finish();
						} else if (res.equals("1")) {
							up_sign_up.setText("注册");
							up_sign_up.setClickable(true);
							up_sign_up.setBackgroundResource(R.drawable.yuanjiao);
							Toast.makeText(SignUpActivity.this, "手机号已注册，注册失败", 2000).show();
						} else {
							Toast.makeText(SignUpActivity.this, "服务器异常，注册失败", 2000).show();
							up_sign_up.setText("注册");
							up_sign_up.setClickable(true);
							up_sign_up.setBackgroundResource(R.drawable.yuanjiao);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable error) {
					Toast.makeText(SignUpActivity.this, "注册失败,请检查网络设置,", 2000).show();
					up_sign_up.setText("注册");
					up_sign_up.setClickable(true);
					up_sign_up.setBackgroundResource(R.drawable.yuanjiao);
					error.printStackTrace();
				}
			});
			
			break;

		default:
			break;
		}
	}
		
	
}
