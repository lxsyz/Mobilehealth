package com.bbcc.mobilehealth.util;

import java.util.ArrayList;

import com.google.analytics.tracking.android.Log;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

	private Context mcontext;
	private MyDBHelp myDbHelp;
	
    public DatabaseHelper(Context context)
    {
    	this.mcontext=context;
    	this.myDbHelp=new MyDBHelp(mcontext);
    }
    
   
    //��ѯ��������Ŀ
    public int getCount(String userId)
    {
    	SQLiteDatabase db = myDbHelp.getWritableDatabase();
        String sql = "select count(*) from "+MyDBHelp.HAATABLE+" where  objectId=?";
        Cursor mCursor = db.rawQuery(
            sql,
            new String[] {userId});
        mCursor.moveToFirst();
        int count=mCursor.getInt(0);
        mCursor.close();
        db.close();
		return count;
    	
    }
   //��ҳ��ѯ
    //HAATABLE ��ҳ��ѯ 10��ÿҳ
    public ArrayList<String> getAllItems(String userId,int currentPage, int pageSize) {
    	//System.out.println("��ʼ��ҳ��ѯ");
        int firstResult = (currentPage - 1) * pageSize;
        //System.out.println("firstResult:"+firstResult);
        //int maxResult = currentPage * pageSize;
        System.out.println("lineSize:"+pageSize);
        SQLiteDatabase db = myDbHelp.getWritableDatabase();
        System.out.println("�����ݿ�");
        String sql = "select * from "+MyDBHelp.HAATABLE+" where  objectId=?  order by HAAID limit ? offset ?";
        Cursor mCursor = db.rawQuery(
            sql,
            new String[] {userId, 
            		
            		String.valueOf(pageSize),
            		String.valueOf(firstResult) });
        System.out.println("��ҳ��ѯ���"+mCursor.getCount());
        ArrayList<String> items = new ArrayList<String>();
       
        if(mCursor.getCount()==0)
        {
        	Log.i("û�в鵽����");
        }
        else
        {
        mCursor.moveToFirst();
        do
        {
        	 String item = mCursor.getInt(0)+" | "+mCursor.getString(1)+" "+mCursor.getString(2)+" | "+mCursor.getFloat(3)+" | "+mCursor.getString(4);
             items.add(item);
           //  System.out.println("��ҳ��:"+item);
        }
        while(mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return items;
      }
    
    public int getExcpCount(String userId)
    {
    	SQLiteDatabase db = myDbHelp.getWritableDatabase();
        String sql = "select count(*) from "+MyDBHelp.EXCEPTIONTABLE+" where  objectId=?";
        Cursor mCursor = db.rawQuery(
            sql,
            new String[] {userId});
        mCursor.moveToFirst();
        int count=mCursor.getInt(0);
        mCursor.close();
        db.close();
		return count;
    	
    }
    
    public ArrayList<String> getExcptionItems(String userId,int currentPage, int pageSize) {
    	//System.out.println("��ʼ��ҳ��ѯ");
        int firstResult = (currentPage - 1) * pageSize;
        //System.out.println("firstResult:"+firstResult);
        //int maxResult = currentPage * pageSize;
//        System.out.println("pageSize:"+pageSize);
        SQLiteDatabase db = myDbHelp.getWritableDatabase();
//        System.out.println("�����ݿ�");
        String sql = "select * from "+MyDBHelp.EXCEPTIONTABLE+" where  objectId=?  order by EXCEPTIONID limit ? offset ?";
        Cursor mCursor = db.rawQuery(
            sql,
            new String[] {userId, 
            		
            		String.valueOf(pageSize),
            		String.valueOf(firstResult) });
        System.out.println("��ҳ��ѯ���"+mCursor.getCount());
        ArrayList<String> items = new ArrayList<String>();
       
        if(mCursor.getCount()==0)
        {
        	Log.i("û�в鵽����");
        }
        else
        {
        mCursor.moveToFirst();
        do
        {
        	 String item = mCursor.getInt(0)+" | "+mCursor.getString(1)+" "+mCursor.getString(2)+" | "+mCursor.getFloat(3)+" | "+mCursor.getString(4)+"|"+mCursor.getString(5);
             items.add(item);
           //  System.out.println("��ҳ��:"+item);
        }
        while(mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return items;
      }
    
    
    
    public int[] getSelDateSta(String userId, String selDate)
    {
    	int [] sta_item=new int[]{0,0,0,0,0,0};
    	SQLiteDatabase db = myDbHelp.getWritableDatabase();
        System.out.println("�����ݿ�");
        String sql = "select * from "+MyDBHelp.STATISTICSTABLE+" where  objectId=? and saveDate= ?";
        Cursor mCursor = db.rawQuery(
            sql,
            new String[] {userId, 
            		selDate});

        if(mCursor.getCount()==0)
        {
        	System.out.println("û�в�ѯ������");
        }
        else
        {
        	mCursor.moveToFirst();
        	sta_item[0]=mCursor.getInt(1);
        	sta_item[1]=mCursor.getInt(2);
        	sta_item[2]=mCursor.getInt(3);
        	sta_item[3]=mCursor.getInt(4);
        	sta_item[4]=mCursor.getInt(5);
        	sta_item[5]=mCursor.getInt(6);
        	
        }
    	
		return sta_item;
		
    }
}
