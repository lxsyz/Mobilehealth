package com.bbcc.mobilehealth;



import org.apache.http.Header;

import com.bbcc.mobilehealth.util.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.bmob.v3.listener.UploadBatchListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity implements OnClickListener{
	private TextView textView;
	private TextView bt_right;
	private ImageView bt_left;
	private EditText ed;
	private Intent intent;
	private int resultType;
	
    private String content;
    private TextView title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_edit);
    
    	intent = getIntent();
    	
    	initView();
    	
    	title.setText(intent.getStringExtra("title"));
    	
    	textView.setText(intent.getStringExtra("tips"));
    	
    	resultType = intent.getIntExtra("result",-1);
    }
    
    private void initView() {
    	title = (TextView)findViewById(R.id.title);
    	textView = (TextView)findViewById(R.id.edit_tv);
    	ed = (EditText)findViewById(R.id.edit_ed);
    	
    	bt_right = (TextView)findViewById(R.id.bt_right);
    	bt_left = (ImageView)findViewById(R.id.bt_left);
    	
    	bt_left.setOnClickListener(this);
    	bt_right.setOnClickListener(this);
    	
    }

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_left:
			this.finish();
			break;
		case R.id.bt_right:
			if (resultType == 26) {
				if (ed.getText().toString().trim().length() != 11) {
					Toast.makeText(this, "请输入正确的手机号码", 2000).show();
					return;
				}
			}
			Intent data=new Intent();
	        data.putExtra("result", ed.getText().toString().trim()); 
	        
	        //请求代码可以自己设置，这里设置成20  
	        setResult(resultType, data);
	        
	        EditActivity.this.finish();
			break;
		default:
			break;
		}
	}
    
	
	
	
}
