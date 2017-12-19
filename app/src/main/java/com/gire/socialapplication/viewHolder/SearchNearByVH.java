package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 6/15/2017.
 */

public class SearchNearByVH extends RecyclerView.ViewHolder{

    public TextView userName,userDistance,rating;
    public Button sedRequest;
    public LinearLayout mainLayout;
    public ImageView userProfile;

    public SearchNearByVH(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        userDistance = (TextView) itemView.findViewById(R.id.distance);
        rating = (TextView) itemView.findViewById(R.id.rating);
        sedRequest = (Button) itemView.findViewById(R.id.send_friend_request);
        mainLayout = (LinearLayout) itemView.findViewById(R.id.main_layout);
        userProfile = (ImageView) itemView.findViewById(R.id.user_image);
    }
    
}
