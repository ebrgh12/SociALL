package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.model.LikeListModel;
import com.gire.socialapplication.viewHolder.LikeListVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/10/2017.
 */

public class LikeListAdapter extends RecyclerView.Adapter<LikeListVH> {

    Activity activity;
    List<LikeListModel> likeListModels;

    public LikeListAdapter(Activity activity,
                           List<LikeListModel> likeListModels) {
        this.activity = activity;
        this.likeListModels = likeListModels;
    }

    @Override
    public LikeListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_list_item, parent, false);
        return new LikeListVH(itemView);
    }

    @Override
    public void onBindViewHolder(LikeListVH holder, int position) {
        Picasso.with(activity).load(likeListModels.get(position).getUserImage()).into(holder.userImage);
        holder.userName.setText(likeListModels.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return likeListModels.size();
    }

}
