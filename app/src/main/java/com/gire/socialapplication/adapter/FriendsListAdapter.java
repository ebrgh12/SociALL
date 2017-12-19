package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.MyFriendsListFragment;
import com.gire.socialapplication.model.FriendsListModel;
import com.gire.socialapplication.viewHolder.FriendsListItemVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/8/2017.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListItemVH> {

    Activity activity;
    MyFriendsListFragment myFriendsListFragment;
    List<FriendsListModel> friendsListModels;

    public FriendsListAdapter(Activity activity,
                              MyFriendsListFragment myFriendsListFragment,
                              List<FriendsListModel> friendsListModels) {
        this.activity = activity;
        this.myFriendsListFragment = myFriendsListFragment;
        this.friendsListModels = friendsListModels;
    }

    @Override
    public FriendsListItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent, false);
        return new FriendsListItemVH(itemView);
    }

    @Override
    public void onBindViewHolder(FriendsListItemVH holder, final int position) {
        if(friendsListModels.get(position).getActionAvailable() == 0){
            holder.updateUserMode.setVisibility(View.GONE);
        }else {
            holder.updateUserMode.setVisibility(View.VISIBLE);
        }
        holder.userName.setText(friendsListModels.get(position).getFriendName());
        Picasso.with(activity).load(friendsListModels.get(position).getUserImage()).into(holder.userImage);
        holder.updateUserMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFriendsListFragment.modeSelection(friendsListModels.get(position).getFriendId());
            }
        });
        holder.messageFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFriendsListFragment.sendMesage(friendsListModels.get(position).getFriendId());
            }
        });
        holder.sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFriendsListFragment.sendFeedbackUser(friendsListModels.get(position).getFriendId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsListModels.size();
    }

}
