package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.gire.socialapplication.R;

/**
 * Created by girish on 6/22/2017.
 */

public class AddNewPostVH extends RecyclerView.ViewHolder {

    public ImageView postImage;

    public AddNewPostVH(View itemView) {
        super(itemView);
        postImage = (ImageView) itemView.findViewById(R.id.post_image);
    }

}
