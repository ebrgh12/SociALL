package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/10/2017.
 */

public class GroupUserVH extends RecyclerView.ViewHolder {

    public ImageView userPic;
    public TextView userName;
    public CheckBox selectCheck;
    public LinearLayout mainLayout;

    public GroupUserVH(View itemView) {
        super(itemView);
        userPic = (ImageView) itemView.findViewById(R.id.user_image);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        selectCheck = (CheckBox) itemView.findViewById(R.id.select_state);
        mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);
    }

}
