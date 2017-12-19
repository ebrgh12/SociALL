package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.WifiDetactFragment;
import com.gire.socialapplication.viewHolder.WifiDeviceListVH;

import java.util.List;

/**
 * Created by girish on 7/1/2017.
 */

public class WifiDeviceListAdapter extends RecyclerView.Adapter<WifiDeviceListVH> {

    Activity activity;
    List<String> deviceName;
    WifiDetactFragment wifiDetactFragment;

    public WifiDeviceListAdapter(Activity activity,
                                 WifiDetactFragment wifiDetactFragment,
                                 List<String> deviceName) {
        this.activity = activity;
        this.wifiDetactFragment = wifiDetactFragment;
        this.deviceName = deviceName;
    }

    @Override
    public WifiDeviceListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_list_item, parent, false);
        return new WifiDeviceListVH(itemView);
    }

    @Override
    public void onBindViewHolder(WifiDeviceListVH holder, final int position) {
        holder.deviceName.setText(deviceName.get(position));
        holder.sendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] userId = deviceName.get(position).split(" ");
                if(userId.length == 4){
                    wifiDetactFragment.sendFriendRequest(userId[3]);
                }else {
                    wifiDetactFragment.sendFriendRequest(userId[2]);
                }
                Log.e("user",userId[0]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceName.size();
    }

}
