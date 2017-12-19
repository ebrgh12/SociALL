package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by tej on 6/20/2017.
 */

public class ReceivedRequestVH extends RecyclerView.ViewHolder {

    public ImageView userProfile,newProfilePic;
    public TextView userName,actionEvent,declainEvent;

    public ReceivedRequestVH(View itemView) {
        super(itemView);
        userProfile = (ImageView) itemView.findViewById(R.id.profile_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        actionEvent = (TextView) itemView.findViewById(R.id.accept_request);
        declainEvent = (TextView) itemView.findViewById(R.id.declain_request);
        newProfilePic = (ImageView) itemView.findViewById(R.id.user_profile);
    }

}
