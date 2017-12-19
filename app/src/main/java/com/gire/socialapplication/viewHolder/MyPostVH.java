package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 6/29/2017.
 */

public class MyPostVH extends RecyclerView.ViewHolder {

    public ImageView postFileOne,postFileTwo,likePost,comment;
    public TextView postTitle,PostDescription,viewLike,viewComment;
    public LinearLayout imageLayout;

    public MyPostVH(View itemView) {
        super(itemView);
        postFileOne = (ImageView) itemView.findViewById(R.id.image_one);
        postFileTwo = (ImageView) itemView.findViewById(R.id.image_two);
        postTitle = (TextView) itemView.findViewById(R.id.post_title);
        PostDescription = (TextView) itemView.findViewById(R.id.post_description);

        likePost = (ImageView) itemView.findViewById(R.id.like_post);
        comment = (ImageView) itemView.findViewById(R.id.comment_post);
        viewLike = (TextView) itemView.findViewById(R.id.view_like);
        viewComment = (TextView) itemView.findViewById(R.id.view_comments);

        imageLayout = (LinearLayout) itemView.findViewById(R.id.image_layout);
    }

}
