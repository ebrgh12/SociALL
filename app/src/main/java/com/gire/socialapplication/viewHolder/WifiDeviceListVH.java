package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/1/2017.
 */

public class WifiDeviceListVH extends RecyclerView.ViewHolder {

    public TextView deviceName,sendFriendRequest;

    public WifiDeviceListVH(View itemView) {
        super(itemView);
        deviceName = (TextView) itemView.findViewById(R.id.device_name);
        sendFriendRequest = (TextView) itemView.findViewById(R.id.send_friend_request);
    }

}
