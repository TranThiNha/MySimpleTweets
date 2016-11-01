package com.codepath.apps.mysimpletweets.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.PostNewTweetDialog;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.ReplyDialog;
import com.codepath.apps.mysimpletweets.activity.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Media;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ThiNha on 10/29/2016.
 */
//taking the tweet object and turning them into View displayed in the list
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.TweetViewHolder>  {

    List<Tweet> mTweets;
    private ListClickListener mListClickListener;
    private ReplyListener mReplyListener;
    public static boolean btnLikeClicked = false;
    public static boolean btnRetweetClicked = false;


    public interface ListClickListener {
        void onItemClick(Tweet tweet);
    }

    public interface ReplyListener{
        void onReplyListener(Tweet tweet);
    }

    public void setListClickListener(ListClickListener listClickListener) {
        mListClickListener = listClickListener;
    }

    public void setReplyListener(ReplyListener replyLisntener){
        mReplyListener = replyLisntener;
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
        List<Media> medias = tweet.getMedias();
        if (medias!= null && medias.size()>0){
            holder.ivMedia.setVisibility(View.VISIBLE);
            Glide.with(holder.ivMedia.getContext())
                    .load(medias.get(0).getMediaUrl())
                    .into(holder.ivMedia);

        }

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
        ImageButton btnRetweet;
        ImageButton btnLike;
        ImageButton btnReply;
        LinearLayout linearLayout;
        TwitterClient mClient;
        ImageView ivMedia;

        public TweetViewHolder( View itemView) {
            super(itemView);
             ivProfileImage= (ImageView)itemView.findViewById(R.id.ivProfileImage);
            tvUserName= (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView)itemView.findViewById(R.id.tvBody);
            tvRelativeTimestamp = (TextView)itemView.findViewById(R.id.tvRelativeTimestamp);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvNumberOfRetweet);
            tvFavorite = (TextView) itemView.findViewById(R.id.tvNumberOfLike);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.tweet_container);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            btnRetweet = (ImageButton) itemView.findViewById(R.id.btnRetweet);
            btnReply = (ImageButton) itemView.findViewById(R.id.btnReply);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
            mClient = TwitterApplication.getRestClient();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);
                    if (mListClickListener != null)
                        mListClickListener.onItemClick(tweet);
                }
            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);

                    mReplyListener.onReplyListener(tweet);
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);

                    if (btnLikeClicked)
                    {

                        btnRetweetClicked = false;
                    }
                    else {
                        btnLikeClicked = true;
                    }

                    if (btnLikeClicked){
                        mClient.updateLike(tweet.getBody().toString(),tweet.getUser().getFavouritesCount()+1,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                            }
                        });
                        tvFavorite.setText(String.valueOf(tweet.getUser().getFavouritesCount()+1));
                        btnLike.setImageResource(R.drawable.ic_liked);
                    }
                    else {
                        tvFavorite.setText(String.valueOf(tweet.getUser().getFavouritesCount()));
                        btnLike.setImageResource(R.drawable.ic_like);
                    }
                }
            });
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btnRetweetClicked){
                        btnRetweetClicked = false;
                    }
                    else{
                        btnRetweetClicked= true;
                    }
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);

                    if (btnRetweetClicked){

                        int count = tweet.getRetweetCount() + 1;
                        tvRetweet.setText(String.valueOf(tweet.getRetweetCount() + 1));
                        btnRetweet.setImageResource(R.drawable.ic_retweeted);
                    }
                    else
                    {
                        tvRetweet.setText(String.valueOf(tweet.getRetweetCount()));
                        btnRetweet.setImageResource(R.drawable.ic_retweet);
                    }

                    mClient.getReTweet(mTweets.get(position).getUid(),new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Gson gson = new Gson();
                            Tweet tweet = gson.fromJson(response.toString(),Tweet.class);
                            addTweet(tweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        }

    }
}
