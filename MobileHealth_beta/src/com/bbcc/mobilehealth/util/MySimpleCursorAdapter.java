package com.bbcc.mobilehealth.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;


public class MySimpleCursorAdapter extends SimpleAdapter {

	public MySimpleCursorAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=null;
		if(convertView!=null)
			
		{
			view=convertView;
		}
		else
		{
			view=super.getView(position, convertView, parent);
		}
		int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色 
		   
        view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
		return super.getView(position, convertView, parent);
	}

	

}
