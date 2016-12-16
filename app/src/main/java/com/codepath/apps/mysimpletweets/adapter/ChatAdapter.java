package com.codepath.apps.mysimpletweets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 12/13/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    List<Chat> mChats;

    public ChatAdapter(){
        mChats = new ArrayList<>();
    }

    public void setChats(List<Chat>chats){
        mChats.addAll(chats);
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat,parent,false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        Chat chat = mChats.get(position);
        holder.tvName.setText(chat.getRecipientScreenName());
        holder.tvText.setText(chat.getText());
        Glide.with(holder.ivAvatar.getContext())
                .load(chat.getRecipientAvatar())
                .into(holder.ivAvatar);
        holder.tvTime.setText(chat.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView ivAvatar;
        TextView tvName;
        TextView tvText;
        TextView tvTime;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvName = (TextView) itemView.findViewById(R.id.tvChatName);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
