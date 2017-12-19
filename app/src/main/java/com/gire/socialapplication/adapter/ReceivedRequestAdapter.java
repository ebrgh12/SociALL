package com.gire.socialapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.FriendRequestFragment;
import com.gire.socialapplication.model.ReceivedRequestModel;
import com.gire.socialapplication.viewHolder.ReceivedRequestVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 6/20/2017.
 */

public class ReceivedRequestAdapter extends RecyclerView.Adapter<ReceivedRequestVH> {

    Activity activity;
    FriendRequestFragment friendRequestFragment;
    List<ReceivedRequestModel> receivedRequestModelList;

    public ReceivedRequestAdapter(Activity activity,
                                  FriendRequestFragment friendRequestFragment,
                                  List<ReceivedRequestModel> receivedRequestModelList) {
        this.activity = activity;
        this.friendRequestFragment = friendRequestFragment;
        this.receivedRequestModelList = receivedRequestModelList;
    }

    @Override
    public ReceivedRequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_request_item, parent, false);
        return new ReceivedRequestVH(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ReceivedRequestVH holder, final int position) {
        //holder.userProfile.setImageDrawable(receivedRequestModelList.get(position).getTestDrawable());
        holder.userName.setText(receivedRequestModelList.get(position).getUserName());
        Picasso.with(activity).load(receivedRequestModelList.get(position).getUserImage()).into(holder.newProfilePic);

        holder.actionEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendRequestFragment.sendAcceptRequest(receivedRequestModelList.get(position).getUserId());
            }
        });

        holder.declainEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendRequestFragment.declainRequest(receivedRequestModelList.get(position).getUserId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return receivedRequestModelList.size();
    }

}