package com.gire.socialapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.HomeFragment;
import com.gire.socialapplication.model.HomePageModeTypes;
import com.gire.socialapplication.viewHolder.ModeVH;

import java.util.List;

/**
 * Created by girish on 7/7/2017.
 */

public class ModeAdapter extends RecyclerView.Adapter<ModeVH> {

    Activity activity;
    HomeFragment homeFragment;
    List<HomePageModeTypes> homePageModeTypes;

    public ModeAdapter(Activity activity,
                       HomeFragment homeFragment,
                       List<HomePageModeTypes> homePageModeTypes) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.homePageModeTypes = homePageModeTypes;
    }

    @Override
    public ModeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mode_list_item, parent, false);
        return new ModeVH(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ModeVH holder, final int position) {
        holder.modeName.setText(homePageModeTypes.get(position).getModeName());
        holder.viewMain.setBackground(homePageModeTypes.get(position).getViewBg());
        if(homePageModeTypes.get(position).getViewBg().equals(activity.getResources().getDrawable(R.drawable.blue_curved_rectangle))){
            holder.modeName.setTextColor(activity.getResources().getColor(android.R.color.white));
        }else {
            holder.modeName.setTextColor(activity.getResources().getColor(android.R.color.black));
        }
        holder.viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.getNewPost(homePageModeTypes.get(position).getModeId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homePageModeTypes.size();
    }

}
