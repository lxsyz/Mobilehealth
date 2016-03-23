package com.bbcc.mobilehealth;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.bbcc.mobilehealth.util.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.bmob.v3.listener.UploadBatchListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
    	if (resultType == 24) {
    		ed.setHint("单位：厘米");
    	} else if (resultType == 23) {
    		ed.setHint("单位：千克");
    	}
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
			//判断工作
			Pattern p = Pattern.compile("[0-9]*"); 
		    Matcher m = p.matcher(ed.getText().toString()); 
			if (resultType == 23) {
				
			} else if (resultType == 24){
				if (!m.matches()) {
					
					Toast.makeText(EditActivity.this,"请输入数字", Toast.LENGTH_SHORT).show();
					return;
				}
			} else if (resultType == 25) {
				if (!m.matches()) {
					
					Toast.makeText(EditActivity.this,"请输入数字", Toast.LENGTH_SHORT).show();
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
