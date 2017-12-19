package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.AddNewPostFragment;
import com.gire.socialapplication.viewHolder.SelectGroupPostItem;

import java.util.List;

/**
 * Created by girish on 7/24/2017.
 */

public class SelectGroupPostAdapter extends RecyclerView.Adapter<SelectGroupPostItem> {

    Activity activity;
    AddNewPostFragment addNewPostFragment;
    List<String> groupName;
    List<String> groupId;

    public SelectGroupPostAdapter(Activity activity,
                                  AddNewPostFragment addNewPostFragment,
                                  List<String> groupName,
                                  List<String> groupId) {
        this.activity = activity;
        this.addNewPostFragment = addNewPostFragment;
        this.groupName = groupName;
        this.groupId = groupId;
    }

    @Override
    public SelectGroupPostItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_group_post_item, parent, false);
        return new SelectGroupPostItem(itemView);
    }

    @Override
    public void onBindViewHolder(SelectGroupPostItem holder, final int position) {
        holder.groupName.setText(groupName.get(position));
        holder.groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPostFragment.selectUserPostGroup(groupId.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupName.size();
    }

}
