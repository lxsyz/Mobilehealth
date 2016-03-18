package com.bbcc.mobilehealth.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;

import com.bbcc.mobilehealth.DeviceDetailsActivity;
import com.bbcc.mobilehealth.R;
import com.bbcc.mobilehealth.service.HardwareConnectorService;

public class DiscoverFragment extends HardwareConnectorFragment {
	
	   private class DiscoveryListAdapter extends ArrayAdapter<ConnectionParams> {

		private List<ConnectionParams> mDiscoveredConnectionParams = new ArrayList<ConnectionParams>();

		public DiscoveryListAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public int getCount() {
			return mDiscoveredConnectionParams.size();
		}

		@Override
		public ConnectionParams getItem(int position) {
			return mDiscoveredConnectionParams.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ConnectionParams params = getItem(position);

			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.discovered_device_item,
						null);
			}

			View background = view.findViewById(R.id.ddi_background);
			TextView name = (TextView) view.findViewById(R.id.ddi_name);
			TextView state = (TextView) view.findViewById(R.id.ddi_connectedstate);
//			Button begin=findViewById(R.id.begin);
			Switch connect = (Switch) view.findViewById(R.id.ddi_connect);
			Switch save = (Switch) view.findViewById(R.id.ddi_save);
			ImageButton about = (ImageButton) view.findViewById(R.id.ddi_about);

			connect.setOnCheckedChangeListener(null);

			background.setBackgroundColor(Color.LTGRAY);

			name.setText(params.getName());

			final SensorConnection sensorConnection = getSensorConnection(params);

			if (sensorConnection != null) {

				switch (sensorConnection.getConnectionState()) {
				case CONNECTED:
					state.setText("Connected");
					connect.setChecked(true);
					background.setBackgroundColor(Color.GREEN);
					break;
				case CONNECTING:
					state.setText("Connecting");
					connect.setChecked(true);
					background.setBackgroundColor(Color.YELLOW);
					break;
				case DISCONNECTED:
					state.setText("Disconnected");
					connect.setChecked(false);
					break;
				case DISCONNECTING:
					state.setText("Disconnecting");
					connect.setChecked(false);
					background.setBackgroundColor(Color.YELLOW);
					break;
				default:
					state.setText("ERROR");
					break;

				}
			} else {

				if (getDiscoveredConnectionParams().contains(params)) {
					state.setText("Discovered");
					connect.setChecked(false);
				} else {
					state.setText("Saved");
					connect.setChecked(false);
				}
			}

			connect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						connectSensor(params);
					} else {
						disconnectSensor(params);
					}
				}
			});

			if (sensorConnection != null) {
				about.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DeviceDetailsActivity.launchActivity(getActivity(), sensorConnection);

					}
				});
				about.setEnabled(true);
			} else {
				about.setOnClickListener(null);
				about.setEnabled(false);
			}

			save.setOnCheckedChangeListener(null);
			save.setChecked(mSavedConnectionParams.contains(params));
			save.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						mSavedConnectionParams.add(params);
					} else {
						mSavedConnectionParams.remove(params);
					}
					saveConnectionParams();
					refreshView();
				}
			});

			return view;
		}

		void refresh() {
			mDiscoveredConnectionParams.clear();

			mDiscoveredConnectionParams.addAll(mSavedConnectionParams);

			for (SensorConnection connectedDevices : getSensorConnections()) {
				ConnectionParams connectedParams = connectedDevices.getConnectionParams();
				if (!mDiscoveredConnectionParams.contains(connectedParams)) {
					mDiscoveredConnectionParams.add(connectedParams);
				}
			}
			for (ConnectionParams discoveredParams : getDiscoveredConnectionParams()) {
				if (!mDiscoveredConnectionParams.contains(discoveredParams)) {
					mDiscoveredConnectionParams.add(discoveredParams);
				}
			}

			Collections.sort(mDiscoveredConnectionParams, new Comparator<ConnectionParams>() {

				@Override
				public int compare(ConnectionParams lhs, ConnectionParams rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			this.notifyDataSetChanged();
		}

	}

	private static final Logger L = new Logger(DiscoverFragment.class);
	private ProgressBar mDiscoveringProgress;

	private Switch mDiscoverSwitch;
	private DiscoveryListAdapter mDiscoveryListAdapter;
	private final Set<ConnectionParams> mSavedConnectionParams = new HashSet<ConnectionParams>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("tag", "Discover:onCreate");
		mSavedConnectionParams.clear();
//		begin=findViewById(R.id.begin);
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
				"PersistentConnectionParams", 0);
		for (Object entry : sharedPreferences.getAll().values()) {
			ConnectionParams params = ConnectionParams.fromString((String) entry);
			mSavedConnectionParams.add(params);
		}
		L.i("onCreate", mSavedConnectionParams.size(), "saved ConnectionParams loaded");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.frag_discover, container,false);

		mDiscoverSwitch = (Switch) view.findViewById(R.id.df_discover);
		mDiscoveringProgress = (ProgressBar) view.findViewById(R.id.df_discovering);

		mDiscoverSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				enableDiscovery(isChecked);

				refreshView();
			}
		});

		mDiscoveryListAdapter = new DiscoveryListAdapter(getActivity());
		ListView list = (ListView) view.findViewById(R.id.df_list);
		list.setAdapter(mDiscoveryListAdapter);
		Log.e("tag", "Discover:onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e("tag", "Discover:onActivityCreate");
		// TODO Auto-generated method stub
//		mDiscoverSwitch = (Switch) getActivity().findViewById(R.id.df_discover);
//		mDiscoveringProgress = (ProgressBar) getActivity().findViewById(R.id.df_discovering);
//
//		mDiscoverSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//				enableDiscovery(isChecked);
//
//				refreshView();
//			}
//		});
//
//		mDiscoveryListAdapter = new DiscoveryListAdapter(getActivity());
//		ListView list = (ListView) getActivity().findViewById(R.id.df_list);
//		list.setAdapter(mDiscoveryListAdapter);
		super.onActivityCreated(savedInstanceState);
		Log.e("tag", "Discover:onActivityCreate  out");
	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {
		refreshView();
	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {
		refreshView();
	}

	@Override
	public void onHardwareConnectorServiceConnected(
			HardwareConnectorService hardwareConnectorService) {
		refreshView();

	}

	@Override
	public void onSensorConnectionStateChanged(SensorConnection sensorConnection,
			SensorConnectionState state) {
		refreshView();
	}

	public void saveConnectionParams() {

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
				"PersistentConnectionParams", 0);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		int count = 0;
		for (ConnectionParams params : mSavedConnectionParams) {
			editor.putString("" + count++, params.serialize());
		}
		editor.commit();
		L.i("onDestroy", mSavedConnectionParams.size(), "ConnectionParams saved");
	}

	private void refreshView() {

		L.i("refreshView");;
		boolean discovering = isDiscovering();
		mDiscoverSwitch.setChecked(discovering);
		mDiscoveringProgress.setVisibility(discovering ? View.VISIBLE : View.INVISIBLE);

		mDiscoveryListAdapter.refresh();
	}

	@Override
	public void onFirmwareUpdateRequired(SensorConnection sensorConnection,
			String currentVersionNumber, String recommendedVersion) {

	}
}
