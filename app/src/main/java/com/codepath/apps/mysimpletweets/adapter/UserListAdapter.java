package com.codepath.apps.mysimpletweets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 12/14/2016.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    List<User> mList;
    AvatarClickListener mAvatarListener;
    TwitterClient mClient;

    public interface AvatarClickListener{
        void onClick(User user);
    }


    public void setOnAvatarClickListener(AvatarClickListener listener){
        mAvatarListener = listener;
    }

    public UserListAdapter(){
        mList = new ArrayList<>();
        mClient = TwitterApplication.getRestClient();
    }

    public void setData(List<User> users){
        mList.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user,parent,false);
        return new UserListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserListViewHolder holder, int position) {
        final User user = mList.get(position);
        holder.tvName.setText(user.getScreenName());
        holder.tvMail.setText(user.getName());
        holder.tvDes.setText(user.getDescription());
        if (!user.getFollowRequestSent()){
            holder.ibAddFriend.setVisibility(View.GONE);
        }
        Glide.with(holder.ivAvatar.getContext())
                .load(user.getProfileImageUrl())
                .into(holder.ivAvatar);
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAvatarListener.onClick(user);
            }
        });

        holder.ibAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.createFollow(user.getUid(),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        holder.ibAddFriend.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class UserListViewHolder extends RecyclerView.ViewHolder{

        ImageView ivAvatar;
        TextView tvName;
        TextView tvMail;
        ImageButton ibAddFriend;
        TextView tvDes;

        public UserListViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvMail = (TextView) itemView.findViewById(R.id.tvMail);
            ibAddFriend = (ImageButton) itemView.findViewById(R.id.ibAddFriend);
            tvDes = (TextView) itemView.findViewById(R.id.tvDestination);
        }
    }
}
