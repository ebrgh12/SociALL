package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/11/2017.
 */

public class ViewUserCommentsVH extends RecyclerView.ViewHolder {

    public ImageView userImage;
    public TextView userName,userComment;

    public ViewUserCommentsVH(View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.user_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        userComment = (TextView) itemView.findViewById(R.id.user_comment);
    }

}
