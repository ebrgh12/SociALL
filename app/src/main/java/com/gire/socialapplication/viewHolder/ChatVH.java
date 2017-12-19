package com.gire.socialapplication.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gire.socialapplication.R;

/**
 * Created by tej on 7/22/2017.
 */

public class ChatVH extends RecyclerView.ViewHolder {
    public TextView receiverText,senderText;

    public ChatVH(View itemView) {
        super(itemView);
        receiverText = (TextView) itemView.findViewById(R.id.receiver_message);
        senderText = (TextView) itemView.findViewById(R.id.sender_message);
    }
}
