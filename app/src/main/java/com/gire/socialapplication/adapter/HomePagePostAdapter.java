package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.HomeFragment;
import com.gire.socialapplication.model.HomePagePostModel;
import com.gire.socialapplication.viewHolder.MyPostVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/6/2017.
 */

public class HomePagePostAdapter extends RecyclerView.Adapter<MyPostVH> {

    Activity activity;
    HomeFragment homeFragmentClass;
    List<HomePagePostModel> homePagePostModels;

    public HomePagePostAdapter(Activity activity,
                               HomeFragment homeFragmentClass,
                               List<HomePagePostModel> homePagePostModels) {
        this.activity = activity;
        this.homeFragmentClass = homeFragmentClass;
        this.homePagePostModels = homePagePostModels;
    }

    @Override
    public MyPostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new MyPostVH(itemView);
    }

    @Override
    public void onBindViewHolder(MyPostVH holder, final int position) {
        if(homePagePostModels.get(position).getPostMediaFile().size() != 0){

            if(homePagePostModels.get(position).getPostMediaFile().size() == 2){
                for(int i=0;i<homePagePostModels.get(position).getPostMediaFile().size();i++){
                    if(i == 0){
                        Picasso.with(activity)
                                .load(homePagePostModels.get(position).getPostMediaFile().get(i))
                                .into(holder.postFileOne);
                    }else {
                        Picasso.with(activity)
                                .load(homePagePostModels.get(position).getPostMediaFile().get(i))
                                .into(holder.postFileTwo);
                    }
                }
            }else if(homePagePostModels.get(position).getPostMediaFile().size() == 1){
                Picasso.with(activity)
                        .load(homePagePostModels.get(position).getPostMediaFile().get(0))
                        .into(holder.postFileOne);

                holder.postFileTwo.setVisibility(View.GONE);
            }
        }else {
            holder.imageLayout.setVisibility(View.GONE);
            holder.postFileOne.setVisibility(View.GONE);
            holder.postFileTwo.setVisibility(View.GONE);
        }

        if(homePagePostModels.get(position).getLikeStatus() == 1){
            holder.likePost.setImageDrawable(activity.getResources().getDrawable(R.drawable.like_done));
        }else {
            holder.likePost.setImageDrawable(activity.getResources().getDrawable(R.drawable.like));
        }

        if(homePagePostModels.get(position).getCommentStatus() == 1){
            holder.comment.setImageDrawable(activity.getResources().getDrawable(R.drawable.comment_done));
        }else {
            holder.comment.setImageDrawable(activity.getResources().getDrawable(R.drawable.comment));
        }

        holder.postTitle.setText(homePagePostModels.get(position).getPostTitle());
        holder.PostDescription.setText(homePagePostModels.get(position).getPostDescription());
        holder.viewLike.setText("( " +homePagePostModels.get(position).getTotalLikes() +" )");
        holder.viewComment.setText("( " +homePagePostModels.get(position).getTotalComments() +" )");

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentClass.addComment(homePagePostModels.get(position).getPostId());
            }
        });

        holder.likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentClass.likeThePost(homePagePostModels.get(position).getPostId());
            }
        });

        holder.viewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentClass.viewLikeList(homePagePostModels.get(position).getPostId());
            }
        });

        holder.viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentClass.viewUserComments(homePagePostModels.get(position).getPostId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return homePagePostModels.size();
    }

}
