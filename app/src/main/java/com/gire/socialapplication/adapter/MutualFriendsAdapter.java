package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.fragments.MutualFriendsFragment;
import com.gire.socialapplication.model.NearByFriendsModel;
import com.gire.socialapplication.viewHolder.SearchNearByVH;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by girish on 7/22/2017.
 */

public class MutualFriendsAdapter extends RecyclerView.Adapter<SearchNearByVH> {

    Activity activity;
    MutualFriendsFragment mutualFriendsFragment;
    List<NearByFriendsModel> nearByFriendsModels;

    public MutualFriendsAdapter(Activity activity,
                                MutualFriendsFragment mutualFriendsFragment,
                                List<NearByFriendsModel> nearByFriendsModels) {
        this.activity = activity;
        this.mutualFriendsFragment = mutualFriendsFragment;
        this.nearByFriendsModels = nearByFriendsModels;
    }

    @Override
    public SearchNearByVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frient_search_list_item, parent, false);
        return new SearchNearByVH(itemView);
    }

    @Override
    public void onBindViewHolder(SearchNearByVH holder, final int position) {
        holder.userDistance.setVisibility(View.GONE);
        holder.rating.setVisibility(View.VISIBLE);
        holder.rating.setText(nearByFriendsModels.get(position).getRating()+" / 5");
        holder.userProfile.bringToFront();
        holder.userName.setText(nearByFriendsModels.get(position).getUserName());
        Picasso.with(activity).load(nearByFriendsModels.get(position).getUserImage()).into(holder.userProfile);
        holder.sedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mutualFriendsFragment.sendFriendRequest(nearByFriendsModels.get(position).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearByFriendsModels.size();
    }

}
