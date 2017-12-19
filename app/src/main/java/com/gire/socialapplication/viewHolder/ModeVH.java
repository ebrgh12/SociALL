package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/7/2017.
 */

public class ModeVH extends RecyclerView.ViewHolder {

    public TextView modeName;
    public LinearLayout viewMain;

    public ModeVH(View itemView) {
        super(itemView);
        modeName = (TextView) itemView.findViewById(R.id.mode_name);
        viewMain = (LinearLayout) itemView.findViewById(R.id.view_main);
    }

}
