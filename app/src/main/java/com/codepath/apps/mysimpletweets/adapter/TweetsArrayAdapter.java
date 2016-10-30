package com.codepath.apps.mysimpletweets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThiNha on 10/29/2016.
 */
//taking the tweet object and turning them into View displayed in the list
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.TweetViewHolder> {

    List<Tweet> mTweets;
    private ListClickListener mListClickListener;


    public interface ListClickListener {
        void onArticleItemClick(Tweet tweet);
    }

    public void setListClickListener(ListClickListener listClickListener) {
        mListClickListener = listClickListener;
    }

    public TweetsArrayAdapter()
    {
        mTweets = new ArrayList<>();
    }

    public void setTweet(List<Tweet> articles)
    {
        mTweets.clear();
        mTweets.addAll(articles);
        notifyDataSetChanged();
    }

    public void addTweet(List<Tweet> articles)
    {
        int position = mTweets.size();
        mTweets.addAll(articles);
        notifyItemRangeInserted(position, articles.size());
    }

    Tweet getItem(int position)
    {
        return mTweets.get(position);
    }

    public void addTweet(Tweet tweet)
    {
        int position = mTweets.size();
        mTweets.add(tweet);
        notifyItemRangeInserted(position, 1);
    }


    //Override and setup custom template


    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position)
    {
        final Tweet tweet = mTweets.get(position);

        holder.tvUserName.setText(tweet.getUser().getScreenName());
        holder.tvBody.setText(tweet.getBody());
        holder.ivProfileImage.setImageResource(android.R.color.transparent);
        holder.tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.getCreateAt()));
        holder.tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
        holder.tvFavorite.setText(String.valueOf(tweet.getUser().getFavouritesCount()));
        Glide.with(holder.ivProfileImage.getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.ivProfileImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListClickListener != null)
                    mListClickListener.onArticleItemClick(tweet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    class TweetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvRelativeTimestamp;
        TextView tvRetweet;
        TextView tvFavorite;
        LinearLayout linearLayout;

        public TweetViewHolder(final View itemView) {
            super(itemView);
             ivProfileImage= (ImageView)itemView.findViewById(R.id.ivProfileImage);
            tvUserName= (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView)itemView.findViewById(R.id.tvBody);
            tvRelativeTimestamp = (TextView)itemView.findViewById(R.id.tvRelativeTimestamp);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvNumberOfRetweet);
            tvFavorite = (TextView) itemView.findViewById(R.id.tvNumberOfLike);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.tweet_container);
        }

    }
}
