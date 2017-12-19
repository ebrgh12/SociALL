package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.GroupUserListFragment;
import com.gire.socialapplication.model.groups.GroupUserList;
import com.gire.socialapplication.viewHolder.GroupUserVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/22/2017.
 */

public class UserListPopupAdapter extends RecyclerView.Adapter<GroupUserVH> {

    Activity activity;
    GroupUserListFragment groupUserListFragment;
    List<GroupUserList> userListData;

    public UserListPopupAdapter(Activity activity,
                                GroupUserListFragment groupUserListFragment,
                                List<GroupUserList> userListData) {
        this.activity = activity;
        this.groupUserListFragment = groupUserListFragment;
        this.userListData = userListData;
    }

    @Override
    public GroupUserVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_user_list_item, parent, false);
        return new GroupUserVH(itemView);
    }

    @Override
    public void onBindViewHolder(GroupUserVH holder, final int position) {
        if(userListData.get(position).getFlag()){
            holder.selectCheck.setChecked(true);
        }else {
            holder.selectCheck.setChecked(false);
        }

        holder.userName.setText(userListData.get(position).getUserName());
        Picasso.with(activity).load(userListData.get(position).getUserProfile()).into(holder.userPic);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupUserListFragment.selectUnSelectUser(position,userListData.get(position).getFlag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userListData.size();
    }

}
