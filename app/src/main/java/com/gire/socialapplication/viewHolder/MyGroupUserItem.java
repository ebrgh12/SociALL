package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/20/2017.
 */

public class MyGroupUserItem extends RecyclerView.ViewHolder {
    public ImageView userImage, deleteUser;
    public TextView userName;

    public MyGroupUserItem(View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.user_image);
        deleteUser = (ImageView) itemView.findViewById(R.id.delete_user);
        userName = (TextView) itemView.findViewById(R.id.user_name);
    }

}
