package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.ManageUserGroupFragment;
import com.gire.socialapplication.model.groups.GroupsMain;
import com.gire.socialapplication.viewHolder.GroupListVH;

import java.util.List;

/**
 * Created by girish on 7/13/2017.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListVH> {

    Activity activity;
    ManageUserGroupFragment manageUserGroupFragment;
    List<GroupsMain> groupsMains;

    public GroupListAdapter(Activity activity,
                            ManageUserGroupFragment manageUserGroupFragment,
                            List<GroupsMain> groupsMains) {
        this.activity = activity;
        this.manageUserGroupFragment = manageUserGroupFragment;
        this.groupsMains = groupsMains;
    }

    @Override
    public GroupListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_group_list_item, parent, false);
        return new GroupListVH(itemView);
    }

    @Override
    public void onBindViewHolder(GroupListVH holder, final int position) {
        holder.groupName.setText(groupsMains.get(position).getGroupName());

        holder.deleteGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageUserGroupFragment.deleteUserGroup(groupsMains.get(position).getGroupId());
            }
        });

        holder.viewGroupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageUserGroupFragment.viewGroupDetails(groupsMains.get(position).getGroupId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsMains.size();
    }

}
