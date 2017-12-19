package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.CreateGroupsFragment;
import com.gire.socialapplication.model.UserForGroupModel;
import com.gire.socialapplication.viewHolder.GroupUserVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/10/2017.
 */

public class UsersForGroupAdapter extends RecyclerView.Adapter<GroupUserVH> {

    Activity activity;
    CreateGroupsFragment createGroupsFragment;
    List<UserForGroupModel> userForGroupModels;

    public UsersForGroupAdapter(Activity activity,
                                CreateGroupsFragment createGroupsFragment,
                                List<UserForGroupModel> userForGroupModels) {
        this.activity = activity;
        this.createGroupsFragment = createGroupsFragment;
        this.userForGroupModels = userForGroupModels;
    }

    @Override
    public GroupUserVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_user_list_item, parent, false);
        return new GroupUserVH(itemView);
    }

    @Override
    public void onBindViewHolder(GroupUserVH holder, final int position) {
        Picasso.with(activity).load(userForGroupModels.get(position).getUserImage()).into(holder.userPic);
        holder.userName.setText(userForGroupModels.get(position).getUserName());
        if(userForGroupModels.get(position).getIsSelected() == 1){
            holder.selectCheck.setChecked(true);
        }else {
            holder.selectCheck.setChecked(false);
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupsFragment.notifyCheck(position,userForGroupModels.get(position).getIsSelected());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userForGroupModels.size();
    }

}
