package com.bbcc.mobilehealth.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

public class ProvinceHelper {
	
	private ArrayList<String> cities;
	private String id;
	
	public ArrayList<String> getCities() {
		return cities;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/*
	 * 获取所有城市及ID  例如 01|北京
	 */
	public static String getProvince() {
		String ws_url = "http://m.weather.com.cn/data5/city.xml";  
        String str= "";  
        try {  
            URL url = new URL(ws_url);  
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解决乱码问题  
            StringBuffer sb = new StringBuffer();  
            String s = "";  
            while ((s = br.readLine()) != null) {  
                sb.append(s); //将内容读取到StringBuffer中  
            }  
            br.close();  
            //System.out.println(sb.toString());// 屏幕  
            str = new String(sb.toString().getBytes());  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return str;  
	}
	
	/*
	 * 获取所有二级城市  2001|武汉
	 * @param id
	 */
	public static String getCity(String id) {
		String ws_url = "http://m.weather.com.cn/data5/city"+id+".xml";  
        String str= "";  
        try {  
            URL url = new URL(ws_url);  
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解决乱码问题  
            StringBuffer sb = new StringBuffer();  
            String s = "";  
            while ((s = br.readLine()) != null) {  
                sb.append(s); //将内容读取到StringBuffer中  
            }  
            br.close();  
            //System.out.println(sb.toString()); 屏幕  
            str = new String(sb.toString().getBytes());  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return str;
	}
//	
//	public void getSecondCity() {
//		String[] array2 = getCity(id).split(",");
//		for (int i = 0; i < array2.length; i++) {
//			cities.add(array2[i].split("\\|")[1] + "市");
//		}
//		Message msg = new Message();
//		msg.what = 0x11;
//		handler.sendMessage(msg);
//	}
//	
//	public void setHandler(Handler handler) {
//		this.handler = handler;
//	}
//	
//	public ProvinceHelper() {
//		cities = new ArrayList<String>();
//		id = "";
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				getSecondCity();
//			}
//		}).start();
//	}
//	private Handler handler = new Handler();
}
