package com.bbcc.mobilehealth;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbcc.mobilehealth.util.Constant;
import com.bbcc.mobilehealth.util.MD5;
import com.bbcc.mobilehealth.view.ClearEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener{
	
	private ClearEditText srcPwd;
	private ClearEditText newPwd;
	private ClearEditText sure_pwd;
	
	private Button confirmButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepwd);
		initView();
	}
	
	private void initView() {
		srcPwd = (ClearEditText)findViewById(R.id.pwd);
		newPwd = (ClearEditText)findViewById(R.id.new_password);
		sure_pwd = (ClearEditText)findViewById(R.id.sure_new_password);
		confirmButton = (Button)findViewById(R.id.confirm);
		
		confirmButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.confirm:
				if (srcPwd.getText().toString().equals("")) {
					Toast.makeText(ChangePasswordActivity.this, "原密码不能为空", 2000).show();
					return;
				}
				
				if (newPwd.getText().toString().equals("")) {
					Toast.makeText(ChangePasswordActivity.this, "新密码不能为空", 2000).show();
					return;
				}

				if (!newPwd.getText().toString().equals(sure_pwd.getText().toString()))
				{
					Toast.makeText(ChangePasswordActivity.this, "两次密码输入不相符", 2000).show();
					return;
				}
				
				confirmButton.setText("修改中...");
				confirmButton.setClickable(false);
				confirmButton.setBackgroundResource(R.drawable.yuanjiao2);
				RequestParams params = new RequestParams();
				params.put("phoneNumber", "13006152436");
				params.put("password", MD5.MD5Encode(srcPwd.getText().toString()));
				params.put("newPassword", MD5.MD5Encode(newPwd.getText().toString()));
				AsyncHttpClient client = new AsyncHttpClient();
				String url = "http://" + Constant.SERVER_URL + "updatepassword.php";
				client.post(url, params,new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String response = new String(arg2);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							
							String res = jsonObject.getString("code");
							Log.d("status",arg0+" ");
							if (res.equals("2")) {
								Toast.makeText(ChangePasswordActivity.this, "修改成功", 2000).show();
								ChangePasswordActivity.this.finish();
							} else if (res.equals("1")) 
								Toast.makeText(ChangePasswordActivity.this, "原密码错误,修改失败", 2000).show();
								confirmButton.setText("注册");
								confirmButton.setClickable(true);
								confirmButton.setBackgroundResource(R.drawable.yuanjiao);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable error) {
						Toast.makeText(ChangePasswordActivity.this, "修改失败,请检查网络设置,", 2000).show();
						confirmButton.setText("注册");
						confirmButton.setClickable(true);
						confirmButton.setBackgroundResource(R.drawable.yuanjiao);
						error.printStackTrace();
					}
				});
			break;

		default:
			break;
		}
	}
	
	
}
