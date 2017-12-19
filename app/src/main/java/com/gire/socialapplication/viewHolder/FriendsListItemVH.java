package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/8/2017.
 */

public class FriendsListItemVH extends RecyclerView.ViewHolder {

    public ImageView userImage;
    public TextView userName,updateUserMode,messageFriend,sendFeedback;

    public FriendsListItemVH(View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.user_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        updateUserMode = (TextView) itemView.findViewById(R.id.update_mode);
        messageFriend = (TextView) itemView.findViewById(R.id.message);
        sendFeedback = (TextView) itemView.findViewById(R.id.feedback);
    }

}
