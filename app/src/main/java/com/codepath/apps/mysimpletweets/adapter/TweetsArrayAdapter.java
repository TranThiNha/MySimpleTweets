package com.codepath.apps.mysimpletweets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
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
    private ProfileImageClickListener profileImageClickListener;
    private ReplyListener mReplyListener;
    public static boolean btnLikeClicked = false;
    public static boolean btnRetweetClicked = false;
    private Gson mGson;
    Context mContext;

    public interface ListClickListener {
        void onItemClick(Tweet tweet);
    }

    public  interface ProfileImageClickListener{
        void onProfileImageClick(Tweet tweet);
    }
    public interface ReplyListener{
        void onReplyListener(Tweet tweet);
    }

    public void setListClickListener(ListClickListener listClickListener) {
        mListClickListener = listClickListener;
    }

    public void setOnProfileImageClickListener(ProfileImageClickListener listener){
        profileImageClickListener = listener;
    }

    public void setReplyListener(ReplyListener replyLisntener){
        mReplyListener = replyLisntener;
    }
    public TweetsArrayAdapter(Context context)
    {
        mContext = context;
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
        return new TweetViewHolder(itemView, mContext);
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

        holder.tvFavorite.setText(String.valueOf(tweet.getFavouritesCount()));

        if(tweet.isFavourited()){
            holder.btnLike.setImageResource(R.drawable.ic_liked);
        }
        else{
            holder.btnLike.setImageResource(R.drawable.ic_like);
        }

        if (tweet.isRetweeted()){
            holder.btnRetweet.setImageResource(R.drawable.ic_retweeted);
        }
        else {
            holder.btnRetweet.setImageResource(R.drawable.ic_retweet);
        }

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
        else holder.ivMedia.setVisibility(View.GONE);

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
        Context mContext;


        public TweetViewHolder(final View itemView, Context context) {
            super(itemView);
            mContext = context;
             ivProfileImage= (ImageView) itemView.findViewById(R.id.ivProfileImage);
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
            mGson = new Gson();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);
                    if (mListClickListener != null)
                    {
                        mListClickListener.onItemClick(tweet);
                    }

                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);
                    profileImageClickListener.onProfileImageClick(tweet);

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
                    final int position = getAdapterPosition();
                    final Tweet tweet = mTweets.get(position);
                    if (!tweet.isFavourited()){
                        mClient.favouriteStatus(tweet.getUid(),new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Tweet tweet1 = mGson.fromJson(response.toString(),Tweet.class);
                                mTweets.set(position,tweet1);
                                tvFavorite.setText(String.valueOf(tweet1.getFavouritesCount()));
                                btnLike.setImageResource(R.drawable.ic_liked);
                            }
                        });

                    }
                    else {
                        mClient.unFavouriteStatus(tweet.getUid(),new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Tweet tweet1 = mGson.fromJson(response.toString(),Tweet.class);
                                mTweets.set(position,tweet1);
                                tvFavorite.setText(String.valueOf(tweet1.getFavouritesCount()));
                                btnLike.setImageResource(R.drawable.ic_like);
                            }
                        });

                    }
                }
            });
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);

                    if (!tweet.isRetweeted()){

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
