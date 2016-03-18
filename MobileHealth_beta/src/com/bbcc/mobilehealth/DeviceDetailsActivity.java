package com.bbcc.mobilehealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bmob.v3.Bmob;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.FirmwareUpgrade;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.bbcc.mobilehealth.fragment.CapabilityFragment;
import com.bbcc.mobilehealth.service.HardwareConnectorService;
import com.bbcc.mobilehealth.util.HardwareConnectorServiceConnection;

public class DeviceDetailsActivity extends ActionBarActivity implements ActionBar.TabListener,
		HardwareConnectorServiceConnection.Listener, HardwareConnectorService.Listener {

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			textView.setText("Not supported");
			return rootView;
		}

	}

	private boolean isFirmwareUpgradeInProgress() {
		boolean inProgress = false;
		SensorConnection sc = getSensorConnection();
		if (sc != null && sc.isConnected()) {

			FirmwareUpgrade firmwareUpgrade = (FirmwareUpgrade) sc
					.getCurrentCapability(CapabilityType.FirmwareUpgrade);
			inProgress = (firmwareUpgrade != null && firmwareUpgrade.isFirmwareUpgradeInProgress());
		}
		return inProgress;

	}

	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
	 * sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private final List<CapabilityType> mCapabilityTypes = new ArrayList<CapabilityType>();
		private final Map<CapabilityType, CapabilityFragment> mLookup = new HashMap<CapabilityType, CapabilityFragment>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public int getCount() {
			return mCapabilityTypes.size();
		}

		@Override
		public Fragment getItem(int position) {

			CapabilityFragment fragment = mLookup.get(mCapabilityTypes.get(position));
			if (fragment != null) {
				return fragment;
			} else {
				return new PlaceholderFragment();
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mCapabilityTypes.get(position).toString();
		}

		@Override
		public void notifyDataSetChanged() {

			SensorConnection sensorConnection = getSensorConnection();

			if (sensorConnection != null) {

				for (CapabilityType capabilityType : sensorConnection.getCurrentCapabilities()) {
					if (!mCapabilityTypes.contains(capabilityType)&&capabilityType==CapabilityType.Heartrate) {
						mCapabilityTypes.add(capabilityType);
						mLookup.put(capabilityType,
								CapabilityFragment.create(capabilityType, mConnectionParams));

					}
				}

			} else {
				L.w("notifyDataSetChanged SensorConnection null");
				mCapabilityTypes.clear();
				mLookup.clear();
			}

			L.i("notifyDataSetChanged", mCapabilityTypes.size(), "capabilities supported");
			super.notifyDataSetChanged();
		}

	}

	private static final Logger L = new Logger(DeviceDetailsActivity.class);

	private ConnectionParams mConnectionParams;

	private HardwareConnectorServiceConnection mHardwareConnectorServiceConnection;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
	 * sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded
	 * fragment in memory. If this becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public SensorConnection getSensorConnection() {
		if (mConnectionParams == null) {
			throw new AssertionError("getSensorConnection is null");
		}

		if (mHardwareConnectorServiceConnection != null) {
			return mHardwareConnectorServiceConnection.getSensorConnection(mConnectionParams);
		} else {
			return null;
		}
	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {

	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {

	}

	@Override
	public void onDiscoveredDeviceRssiChanged(ConnectionParams params) {

	}

	@Override
	public void onHardwareConnectorServiceConnected(
			HardwareConnectorService hardwareConnectorService) {
		hardwareConnectorService.addListener(this);
		refreshView();
	}

	@Override
	public void onHardwareConnectorServiceDisconnected() {
	}

	@Override
	public void onNewCapabilityDetected(SensorConnection sensorConnection,
			CapabilityType capabilityType) {

		refreshView();

	}

	@Override
	public void onSensorConnectionStateChanged(SensorConnection sensorConnection,
			SensorConnectionState state) {
		refreshView();
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	private void refreshView() {
		L.i("refreshTabs");
		mSectionsPagerAdapter.notifyDataSetChanged();

		final ActionBar actionBar = getSupportActionBar();

		int tabCount = actionBar.getTabCount();

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = tabCount; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.

			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		SensorConnection sc = this.getSensorConnection();
		if (sc != null) {
			if (sc.isConnected()) {
				mViewPager.setBackgroundColor(Color.TRANSPARENT);
			} else {
				mViewPager.setBackgroundColor(Color.YELLOW);
			}
		} else {
			mViewPager.setBackgroundColor(Color.YELLOW);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_details_activity);
		Bmob.initialize(this, "b51e3eb59f6e65de81096688de413362");
		mConnectionParams = ConnectionParams.fromString(getIntent().getStringExtra("params"));
		this.setTitle(mConnectionParams.getName());

		mHardwareConnectorServiceConnection = new HardwareConnectorServiceConnection(this, this);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		refreshView();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mHardwareConnectorServiceConnection.unbind();
		mHardwareConnectorServiceConnection = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshView();
	}

	public static void launchActivity(Activity activity, SensorConnection sensorConnection) {
		Intent intent = new Intent(activity, DeviceDetailsActivity.class);
		intent.putExtra("params", sensorConnection.getConnectionParams().serialize());
		activity.startActivity(intent);
	}

	@Override
	public void onFirmwareUpdateRequired(SensorConnection sensorConnection,
			String currentVersionNumber, String recommendedVersion) {

	}

	

}
