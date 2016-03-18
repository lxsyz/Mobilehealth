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
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener {
	
	private ClearEditText usernameEditText;
	private ClearEditText passwordEditText;
	private Button signIn_btn;
	private TextView signUp_btn;
	private int result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		
		result = getIntent().getIntExtra("result", -1);
		initView();
	}

	private void initView() {
		usernameEditText = (ClearEditText)findViewById(R.id.in_username);
		passwordEditText = (ClearEditText)findViewById(R.id.in_password);
		signIn_btn = (Button)findViewById(R.id.in_sign_in);
		signUp_btn = (TextView)findViewById(R.id.in_sign_up);
		
		signUp_btn.setOnClickListener(this);
		signIn_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
	
			case R.id.in_sign_in:
				
				if (usernameEditText.getText().toString().equals("")) {
					Toast.makeText(SignInActivity.this, "用户名不能为空", 2000).show();
					return;
				}
				
				if (passwordEditText.getText().toString().equals("")) {
					Toast.makeText(SignInActivity.this, "密码不能为空", 2000).show();
					return;
				}
				signIn_btn.setText("登录中...");
				signIn_btn.setClickable(false);
				signIn_btn.setBackgroundResource(R.drawable.yuanjiao2);
				
				RequestParams params = new RequestParams();
				params.put("phoneNumber", (usernameEditText.getText().toString()));
				params.put("password", MD5.MD5Encode(passwordEditText.getText().toString()));
				AsyncHttpClient client = new AsyncHttpClient();
				String url = "http://" + Constant.SERVER_URL + "login.php";
				client.post(url, params,new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String response = new String(arg2);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							
							String res = jsonObject.getString("code");
							
							//String data = jsonObject.getString("data");
							
							Log.d("status",arg0+" ");
							if (res.equals("1")) {
								JSONObject data = jsonObject.getJSONObject("data");
								Log.d("data", data+" ");
								Toast.makeText(SignInActivity.this, "登录成功", 2000).show();
							
								SharedPreferences userSharedPreferences = getSharedPreferences("mobile_user", Activity.MODE_PRIVATE);
								SharedPreferences.Editor editor = userSharedPreferences.edit();
								Constant.PHONE_NUMBER = usernameEditText.getText().toString();
								
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
								//Intent data = new Intent();
								//data.putExtra("result", result);
								//setResult(, data)
								SignInActivity.this.finish();
							} else { 
								Toast.makeText(SignInActivity.this, "密码错误,登录失败", 2000).show();
								signIn_btn.setText("登录");
								signIn_btn.setClickable(true);
								signIn_btn.setBackgroundResource(R.drawable.yuanjiao);
								passwordEditText.setText("");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable error) {
						Toast.makeText(SignInActivity.this, "登录失败,请检查网络设置,", 2000).show();
						signIn_btn.setText("登录");
						signIn_btn.setClickable(true);
						signIn_btn.setBackgroundResource(R.drawable.yuanjiao);
						error.printStackTrace();
					}
				});
				
				break;
				
			case R.id.in_sign_up:
				startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
				
				break;
			}
		
			
		}
	
	@Override
	public void onBackPressed() {

		super.onBackPressed();
		SignInActivity.this.finish();
	}
}
