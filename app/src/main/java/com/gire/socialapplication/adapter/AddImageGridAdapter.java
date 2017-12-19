package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.viewHolder.AddNewPostVH;

import java.util.List;

/**
 * Created by girish on 6/22/2017.
 */

public class AddImageGridAdapter extends RecyclerView.Adapter<AddNewPostVH> {

    Activity activity;
    List<Bitmap> imageData;

    public AddImageGridAdapter(Activity activity,
                               List<Bitmap> imageData) {
        this.activity = activity;
        this.imageData = imageData;
    }

    @Override
    public AddNewPostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_image_grid_item, parent, false);
        return new AddNewPostVH(itemView);
    }

    @Override
    public void onBindViewHolder(AddNewPostVH holder, int position) {
        holder.postImage.setImageBitmap(imageData.get(position));
    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

}
