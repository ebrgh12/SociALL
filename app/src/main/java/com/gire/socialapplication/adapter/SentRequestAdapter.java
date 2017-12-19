package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.FriendRequestFragment;
import com.gire.socialapplication.model.SentRequestModel;
import com.gire.socialapplication.viewHolder.SentRequestVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 6/20/2017.
 */

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestVH> {

    Activity activity;
    FriendRequestFragment friendRequestFragment;
    List<SentRequestModel> sentRequestModelList;

    public SentRequestAdapter(Activity activity,
                              FriendRequestFragment friendRequestFragment,
                              List<SentRequestModel> sentRequestModelList) {
        this.activity = activity;
        this.friendRequestFragment = friendRequestFragment;
        this.sentRequestModelList = sentRequestModelList;
    }

    @Override
    public SentRequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_request_item, parent, false);
        return new SentRequestVH(itemView);
    }

    @Override
    public void onBindViewHolder(SentRequestVH holder, int position) {
        //holder.userProfile.setImageDrawable(sentRequestModelList.get(position).getTestDrawable());
        holder.userName.setText(sentRequestModelList.get(position).getUserName());
        Picasso.with(activity).load(sentRequestModelList.get(position).getUserImage()).into(holder.newUserProfile);
    }

    @Override
    public int getItemCount() {
        return sentRequestModelList.size();
    }

}
