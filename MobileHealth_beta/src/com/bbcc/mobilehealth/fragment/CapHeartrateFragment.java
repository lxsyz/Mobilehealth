package com.bbcc.mobilehealth.fragment;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbcc.mobilehealth.service.HardwareConnectorService;
import com.bbcc.mobilehealth.util.MyDBHelp;
import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.capabilities.Heartrate.Data;
public class CapHeartrateFragment extends CapabilityFragment {

	private final Heartrate.Listener mHeartrateListener = new Heartrate.Listener() {

		@Override
		public void onHeartrateData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onHeartrateDataReset() {
			registerCallbackResult("onHeartrateDataReset", TimeInstant.now());
			refreshView();
		}
	};
	private Heartrate.Data mLastCallbackData;
	private TextView mTextView;
	private LinearLayout layout;
//	private DrawChart view;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		mTextView.setTextSize(20);
		layout=createSimpleLinearLayout(context);
		layout.setBackgroundColor(Color.BLACK);
		LayoutParams params=new LayoutParams(500,450);
		layout.setLayoutParams(params);
//		view = new DrawChart(this.getActivity());
	
		refreshView();
		ll.addView(mTextView);

//		ll.addView(createSimpleButton(context, "resetHeartrateData", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				getHeartrateCap().resetHeartrateData();
//			}
//		}));
		ll.addView(layout);
//		layout.addView(view);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        Log.i("tag", "CapHeartrate ondestroy");
		Heartrate cap = getHeartrateCap();
		if (cap != null) {
			cap.removeListener(mHeartrateListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(
			HardwareConnectorService hardwareConnectorService) {
		super.onHardwareConnectorServiceConnected(hardwareConnectorService);

		getHeartrateCap().addListener(mHeartrateListener);
		refreshView();
	}

	private Heartrate getHeartrateCap() {
		return (Heartrate) getCapability(CapabilityType.Heartrate);
	}

	@Override
	protected void refreshView() {
		Heartrate cap = getHeartrateCap();
		if (cap != null) {
			Heartrate.Data data = cap.getHeartrateData();
			MyDBHelp myDbHelp=new MyDBHelp(getActivity());
			SQLiteDatabase db= myDbHelp.getWritableDatabase();
			Cursor cursor = db.rawQuery("select count( * ) from "+MyDBHelp.HAATABLE,null);
			cursor.moveToFirst();
			int count=cursor.getInt(0);
			mTextView.setText("");
			mTextView.append("Hearterate:\n");
			mTextView.append(summarizeGetters(data));
//			mTextView.append("\n\n");
//			mTextView.append("CALLBACK DATA\n");
//			mTextView.append(summarizeGetters(mLastCallbackData));
//			mTextView.append("\n\n");
//			mTextView.append("CALLBACKS\n");
//			mTextView.append(getCallbackSummary());
//			mTextView.append("\n\n");
//			mTextView.append("count:"+count);
			mTextView.append("\n\n");
//			view.invalidate();

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
