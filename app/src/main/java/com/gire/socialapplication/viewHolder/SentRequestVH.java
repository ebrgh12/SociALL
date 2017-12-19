package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 6/20/2017.
 */

public class SentRequestVH extends RecyclerView.ViewHolder {

    public ImageView userProfile,newUserProfile;
    public TextView userName,actionEvent;

    public SentRequestVH(View itemView) {
        super(itemView);
        userProfile = (ImageView) itemView.findViewById(R.id.profile_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        actionEvent = (TextView) itemView.findViewById(R.id.cancel_action);
        newUserProfile = (ImageView) itemView.findViewById(R.id.user_profile);
    }

}
