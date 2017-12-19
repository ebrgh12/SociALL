package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/13/2017.
 */

public class GroupListVH extends RecyclerView.ViewHolder {

    public TextView groupName,viewGroupDetails;
    public ImageView deleteGroups;

    public GroupListVH(View itemView) {
        super(itemView);
        groupName = (TextView) itemView.findViewById(R.id.group_name);
        viewGroupDetails = (TextView) itemView.findViewById(R.id.view_group_details);
        deleteGroups = (ImageView) itemView.findViewById(R.id.delete_group);
    }

}
