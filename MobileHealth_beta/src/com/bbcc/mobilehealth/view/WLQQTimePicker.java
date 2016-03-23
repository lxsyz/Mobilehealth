package com.bbcc.mobilehealth.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.bbcc.mobilehealth.R;





public class WLQQTimePicker extends LinearLayout {
    public static final String PICKED_TIME_EXT = "picked_time";
    private static final int UPDATE_TITLE_MSG = 0x111;
    private static final int UPDATE_WHEEL = 0x112;
    private static final int UPDATE_UpdateDay_MSG = 0x113;
    private final int START_YEAR = 1949;
    private final int END_YEAR = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
    private TextView mPickerTitle;
    private WheelView mWheelYear;
    private WheelView mWheelMonth;
    private WheelView mWheelDay;
//    private WheelView mWheelHour;
    private TextView mCancelBtn;
    private TextView mConfirmBtn;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;

    public int getmYear() {
		return mYear;
	}



	public void setmYear(int mYear) {
		this.mYear = mYear;
	}



	public int getmMonth() {
		return mMonth;
	}



	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}



	public int getmDay() {
		return mDay;
	}



	public void setmDay(int mDay) {
		this.mDay = mDay;
	}

	private Calendar mCalendar;
    private int mDefaultDayWhellIndex = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TITLE_MSG: {
                    updateTitle();
                }
                break;
                case UPDATE_WHEEL: {
                    updateWheel();
                }
                break;
                case UPDATE_UpdateDay_MSG: {
                    updateDay(mMonth);
                }
                break;
            }

        }
    };
    private WheelView.OnSelectListener mYearListener = new WheelView.OnSelectListener() {
        public void endSelect(int year, String text) {
            mYear = START_YEAR + year;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        public void selecting(int id, String text) {
        }
    };

    private WheelView.OnSelectListener mMonthListener = new WheelView.OnSelectListener() {
        public void endSelect(int month, String text) {
            mMonth = month;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
            mHandler.sendEmptyMessage(UPDATE_UpdateDay_MSG);
        }

        public void selecting(int id, String text) {
        }
    };

    private WheelView.OnSelectListener mDayListener = new WheelView.OnSelectListener() {
        public void endSelect(int day, String text) {
            mDay = day + 1;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        public void selecting(int day, String text) {
        }
    };
    private WheelView.OnSelectListener mHourListener = new WheelView.OnSelectListener() {
        public void endSelect(int hour, String text) {
            mHour = hour;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        public void selecting(int day, String text) {
        }
    };
    private Activity mContext;

    public WLQQTimePicker(Context context) {
        this(context, null);
    }

    
    
    public WLQQTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=(Activity) context;
        LayoutInflater.from(context).inflate(R.layout.time_picker, this);
        mPickerTitle = (TextView) findViewById(R.id.picker_title);
        mWheelYear = (WheelView) findViewById(R.id.year);
        mWheelMonth = (WheelView) findViewById(R.id.month);
        mWheelDay = (WheelView) findViewById(R.id.day);
//        mWheelHour = (WheelView) findViewById(R.id.hour);
        mCancelBtn = (TextView) findViewById(R.id.cancel);
        mConfirmBtn = (TextView) findViewById(R.id.confirm);
        mWheelYear.setOnSelectListener(mYearListener);
        mWheelMonth.setOnSelectListener(mMonthListener);
        mWheelDay.setOnSelectListener(mDayListener);
//        mWheelHour.setOnSelectListener(mHourListener);
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d(WLQQTimePicker.class.getSimpleName(), mContext.getString(R.string.app_name, mYear, mMonth + 1, mDay, mHour));
//                mCalendar.set(Calendar.YEAR, mYear);
//                mCalendar.set(Calendar.MONTH, mMonth);
//                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
//                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
//                Intent intent = new Intent();
//                intent.putExtra(PICKED_TIME_EXT, mCalendar.getTimeInMillis());
//                mContext.setResult(WuliuQQConstants.RESULT_CODE_TIME_PICKER, intent);
//                mContext.finish();
            }
        });
        mCancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.finish();
                
            }
        });
    }


    private void updateDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mWheelDay.resetData(getDayData(maxDay));
        if (mDay > maxDay) {
            mWheelDay.setDefault(mDefaultDayWhellIndex);
            mDay = mDefaultDayWhellIndex + 1;
        } else {
            mWheelDay.setDefault(mDay - 1);
        }
    }

    /**
     * set WLQQTimePicker date
     * @param date
     */
    public void setDate(long date) {
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar.setTimeInMillis(date);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mWheelYear.setData(getYearData());
        mWheelMonth.setData(getMonthData());
        mWheelDay.setData(getDayData(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));

        mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        mHandler.sendEmptyMessage(UPDATE_WHEEL);
    }

    private void updateTitle() {
        mPickerTitle.setText("请选择时间");
    }

    private void updateWheel() {
        mWheelYear.setDefault(mYear - START_YEAR);
        mWheelMonth.setDefault(mMonth);
        mWheelDay.setDefault(mDay - 1);
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            list.add(i + "年");
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    private ArrayList<String> getDayData(int endDay) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= endDay; i++) {
            list.add(i + "日");
        }
        return list;
    }

}