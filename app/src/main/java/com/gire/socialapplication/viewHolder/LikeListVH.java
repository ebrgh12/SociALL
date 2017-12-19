package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/10/2017.
 */

public class LikeListVH extends RecyclerView.ViewHolder {

    public ImageView userImage;
    public TextView userName;

    public LikeListVH(View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.user_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
    }

}
