package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.model.MyPostModel;
import com.gire.socialapplication.viewHolder.MyPostVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 6/29/2017.
 */

public class MyPostAdapter extends RecyclerView.Adapter<MyPostVH> {

    Activity activity;
    List<MyPostModel> myPostModels;

    public MyPostAdapter(Activity activity,
                         List<MyPostModel> myPostModels) {
        this.activity = activity;
        this.myPostModels = myPostModels;
    }

    @Override
    public MyPostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new MyPostVH(itemView);
    }

    @Override
    public void onBindViewHolder(MyPostVH holder, int position) {
        if(myPostModels.get(position).getFileList().size() != 0){

            if(myPostModels.get(position).getFileList().size() == 2){
                for(int i=0;i<myPostModels.get(position).getFileList().size();i++){
                    if(i == 0){
                        Picasso.with(activity)
                                .load(myPostModels.get(position).getFileList().get(i))
                                .into(holder.postFileOne);
                    }else {
                        Picasso.with(activity)
                                .load(myPostModels.get(position).getFileList().get(i))
                                .into(holder.postFileTwo);
                    }
                }
            }else if(myPostModels.get(position).getFileList().size() == 1){
                Picasso.with(activity)
                        .load(myPostModels.get(position).getFileList().get(0))
                        .into(holder.postFileOne);

                holder.postFileTwo.setVisibility(View.GONE);
            }
        }else {
            holder.postFileOne.setVisibility(View.GONE);
            holder.postFileTwo.setVisibility(View.GONE);
        }

        holder.postTitle.setText(myPostModels.get(position).getTitle());
        holder.PostDescription.setText(myPostModels.get(position).getDescription());
        holder.likePost.setImageDrawable(activity.getResources().getDrawable(R.drawable.like));
        holder.comment.setImageDrawable(activity.getResources().getDrawable(R.drawable.comment));
    }

    @Override
    public int getItemCount() {
        return myPostModels.size();
    }

}
