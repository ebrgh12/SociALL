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
import com.gire.socialapplication.viewHolder.MyGroupUserItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/20/2017.
 */

public class MyGroupUserAdapter extends RecyclerView.Adapter<MyGroupUserItem> {

    Activity activity;
    GroupUserListFragment groupUserListFragment;
    List<GroupUserList> groupUserLists;

    public MyGroupUserAdapter(Activity activity,
                              GroupUserListFragment groupUserListFragment,
                              List<GroupUserList> groupUserLists) {
        this.activity = activity;
        this.groupUserListFragment = groupUserListFragment;
        this.groupUserLists = groupUserLists;

    }

    @Override
    public MyGroupUserItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_group_user_item, parent, false);
        return new MyGroupUserItem(itemView);
    }

    @Override
    public void onBindViewHolder(MyGroupUserItem holder, final int position) {
        Picasso.with(activity).load(groupUserLists.get(position).getUserProfile()).into(holder.userImage);
        holder.userName.setText(groupUserLists.get(position).getUserName());
        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupUserListFragment.deleteUserFromGroup(groupUserLists.get(position).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupUserLists.size();
    }

}
