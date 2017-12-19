package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/24/2017.
 */

public class SelectGroupPostItem extends RecyclerView.ViewHolder {

    public TextView groupName;

    public SelectGroupPostItem(View itemView) {
        super(itemView);
        groupName = (TextView) itemView.findViewById(R.id.group_name);
    }

}
