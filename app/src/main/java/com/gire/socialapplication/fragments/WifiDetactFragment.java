package com.gire.socialapplication.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.WifiDeviceListAdapter;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.gire.socialapplication.constants.APIConstants.SEND_FRIEND_REQUEST;

/**
 * Created by girish on 7/1/2017.
 */

public class WifiDetactFragment extends Fragment implements WebInterface{

    View view;
    LinearLayout reloadDevice;
    RecyclerView wifiList;

    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    WiFiDirectBroadcastReceiver receiver;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    List<String> deviceName = new ArrayList<String>();
    WifiDeviceListAdapter wifiDeviceListAdapter;

    WebServiceController webServiceController;
    ProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wifi_fragment,container,false);

        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getActivity(), getMainLooper(), null);

        webServiceController = new WebServiceController(getActivity(),WifiDetactFragment.this);
        progressIndicator = new ProgressIndicator();

        wifiList = (RecyclerView) view.findViewById(R.id.wifi_list);
        wifiList.setLayoutManager(new LinearLayoutManager(getActivity()));

        reloadDevice = (LinearLayout) view.findViewById(R.id.reload_device);
        reloadDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receiver != null){
                    getActivity().unregisterReceiver(receiver);
                }
                receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, WifiDetactFragment.this);
                getActivity().registerReceiver(receiver, intentFilter);
            }
        });

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });

        try {
            Method method = mManager.getClass().getMethod("setDeviceName",
                    WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);

            method.invoke(mManager, mChannel, "SSAPP-2017 "+SharedPreferencesUtil.getInstance(getActivity()).getUserName()
                    +" "+SharedPreferencesUtil.getInstance(getActivity()).getCustomerID(), new WifiP2pManager.ActionListener() {
                public void onSuccess() {}

                public void onFailure(int reason) {}
            });
        } catch (Exception e){

        }

        return view;
    }


    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    // Other methods omitted for brevity
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {

    }

    /**
     * send friend request
     * */
    public void sendFriendRequest(String userID) {
        progressIndicator.showProgress(getActivity());
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
        requestParams.put("friend_id",userID);
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

    private class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        WifiDetactFragment activity;
        WifiP2pManager mManager;
        WifiP2pManager.Channel mChannel;
        public WiFiDirectBroadcastReceiver(WifiP2pManager mManager,
                                           WifiP2pManager.Channel mChannel,
                                           WifiDetactFragment activity) {
            super();
            this.activity = activity;
            this.mManager = mManager;
            this.mChannel = mChannel;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity.
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    activity.setIsWifiP2pEnabled(true);
                } else {
                    activity.setIsWifiP2pEnabled(false);
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                if (mManager != null) {
                    mManager.requestPeers(mChannel, peerListListener);
                }
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                if (mManager != null) {
                    mManager.requestPeers(mChannel, peerListListener);
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                activity.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
                WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            }
        }
    }

    private void updateThisDevice(WifiP2pDevice parcelableExtra) {
        Log.e("data",parcelableExtra.deviceName);
    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            Collection<WifiP2pDevice> dataList = new ArrayList<WifiP2pDevice>();
            dataList = peerList.getDeviceList();
            peers.clear();
            peers.addAll(dataList);
            deviceName.clear();

            for(int i=0;i<peers.size();i++){
                WifiP2pDevice device = peers.get(i);
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                if(device.deviceName.contains("SSAPP-2017")){
                    deviceName.add(device.deviceName);
                }
            }

            if(deviceName.size() != 0){
                wifiDeviceListAdapter = new WifiDeviceListAdapter(getActivity(),WifiDetactFragment.this,deviceName);
                wifiList.setAdapter(wifiDeviceListAdapter);
            }else {
                Toast.makeText(getActivity(), "No device Found", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
