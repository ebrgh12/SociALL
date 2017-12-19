package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.BluetoothDetectFragment;
import com.gire.socialapplication.viewHolder.WifiDeviceListVH;

import java.util.List;

/**
 * Created by girish on 7/2/2017.
 */

public class BlueToothDeviceListAdapter extends RecyclerView.Adapter<WifiDeviceListVH> {

    Activity activity;
    BluetoothDetectFragment bluetoothDetectFragment;
    List<String> deviceNameList;

    public BlueToothDeviceListAdapter(Activity activity,
                                      BluetoothDetectFragment bluetoothDetectFragment,
                                      List<String> deviceNameList) {
        this.activity = activity;
        this.bluetoothDetectFragment = bluetoothDetectFragment;
        this.deviceNameList = deviceNameList;
    }

    @Override
    public WifiDeviceListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_list_item, parent, false);
        return new WifiDeviceListVH(itemView);
    }

    @Override
    public void onBindViewHolder(WifiDeviceListVH holder, final int position) {
        holder.deviceName.setText(deviceNameList.get(position));
        holder.sendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] userId = deviceNameList.get(position).split(" ");
                bluetoothDetectFragment.sendFriendRequest(userId[2]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceNameList.size();
    }
}
