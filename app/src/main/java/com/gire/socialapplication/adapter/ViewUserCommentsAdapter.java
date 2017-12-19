package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.model.CommentListModel;
import com.gire.socialapplication.viewHolder.ViewUserCommentsVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/11/2017.
 */

public class ViewUserCommentsAdapter extends RecyclerView.Adapter<ViewUserCommentsVH> {

    Activity activity;
    List<CommentListModel> commentListModels;

    public ViewUserCommentsAdapter(Activity activity,
                                   List<CommentListModel> commentListModels) {
        this.activity = activity;
        this.commentListModels = commentListModels;
    }

    @Override
    public ViewUserCommentsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_commnet_list_item, parent, false);
        return new ViewUserCommentsVH(itemView);
    }

    @Override
    public void onBindViewHolder(ViewUserCommentsVH holder, int position) {
        Picasso.with(activity).load(commentListModels.get(position).getUserImage()).into(holder.userImage);
        holder.userName.setText(commentListModels.get(position).getUserName());
        holder.userComment.setText(commentListModels.get(position).getUserComment());
    }

    @Override
    public int getItemCount() {
        return commentListModels.size();
    }

}
