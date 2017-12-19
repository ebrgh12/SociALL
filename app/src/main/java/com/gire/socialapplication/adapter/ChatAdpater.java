package com.gire.socialapplication.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gire.socialapplication.R;
import com.gire.socialapplication.model.ChatMessageModel;
import com.gire.socialapplication.viewHolder.ChatVH;

import java.util.List;

/**
 * Created by girish on 7/22/2017.
 */

public class ChatAdpater extends RecyclerView.Adapter<ChatVH> {

    Activity activity;
    List<ChatMessageModel> chatMessageModels;

    public ChatAdpater(Activity activity,
                       List<ChatMessageModel> chatMessageModels) {
        this.activity = activity;
        this.chatMessageModels = chatMessageModels;
    }

    @Override
    public ChatVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_item, parent, false);
        return new ChatVH(itemView);
    }

    @Override
    public void onBindViewHolder(ChatVH holder, int position) {
        if(chatMessageModels.get(position).getFlagType() == 1){
            holder.receiverText.setVisibility(View.VISIBLE);
            holder.senderText.setVisibility(View.GONE);
            holder.receiverText.setText(chatMessageModels.get(position).getMessage());
        }else {
            holder.senderText.setVisibility(View.VISIBLE);
            holder.receiverText.setVisibility(View.GONE);
            holder.senderText.setText(chatMessageModels.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageModels.size();
    }

}
