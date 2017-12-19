package com.gire.socialapplication.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.BlueToothDeviceListAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.SEND_FRIEND_REQUEST;

/**
 * Created by girish on 7/2/2017.
 */

public class BluetoothDetectFragment extends Fragment implements WebInterface {

    View view;
    LinearLayout reloadDevice;
    RecyclerView bluetoothDevice;

    BluetoothHeadset mBluetoothHeadset;
    BluetoothAdapter mBluetoothAdapter;

    List<String> deviceNameList = new ArrayList<String>();
    BlueToothDeviceListAdapter blueToothDeviceListAdapter;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bluetooth_detect_fragment,container,false);

        reloadDevice = (LinearLayout) view.findViewById(R.id.reload_device);
        bluetoothDevice = (RecyclerView) view.findViewById(R.id.blue_tooth_device_list);
        bluetoothDevice.setLayoutManager(new LinearLayoutManager(getActivity()));

        webServiceController = new WebServiceController(getActivity(),BluetoothDetectFragment.this);
        progressIndicator = new ProgressIndicator();

        reloadDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getActivity(), "your phone doesnot support bluetooth", Toast.LENGTH_SHORT).show();
        }else {

            mBluetoothAdapter.setName("SSAPP-2017 "+SharedPreferencesUtil.getInstance(getActivity()).getUserName()
                    +" "+ SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());

            startBluetoothDiscover();

        }

        return view;
    }

    private void startBluetoothDiscover() {
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30000);
        startActivity(discoverableIntent);

        mBluetoothAdapter.startDiscovery();

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode){
            case 100:
                    startBluetoothDiscover();
                break;
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();

                if(deviceName.contains("SSAPP-2017")){
                    deviceNameList.add(deviceName);
                }

                if(deviceNameList.size() != 0){
                    blueToothDeviceListAdapter = new BlueToothDeviceListAdapter(
                            getActivity(),BluetoothDetectFragment.this,deviceNameList);
                    bluetoothDevice.setAdapter(blueToothDeviceListAdapter);
                }

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        mBluetoothAdapter.closeProfileProxy(100,mBluetoothHeadset);
        getActivity().unregisterReceiver(mReceiver);
    }

    public void sendFriendRequest(String userId) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",userId);
        webServiceController.postRequest(SEND_FRIEND_REQUEST,requestParams,2);
    }

    @Override
    public void getResponse(String response, int flag) {
        progressIndicator.DismissProgress();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.optBoolean("status")){
                Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

}
