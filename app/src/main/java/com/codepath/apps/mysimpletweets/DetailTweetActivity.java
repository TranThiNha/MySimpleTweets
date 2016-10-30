package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.activity.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class DetailTweetActivity extends AppCompatActivity {

    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvTimeStamp;
    private TextView tvBody;
    private TextView tvCreatedAt;
    private TextView tvRetweet;
    private TextView tvLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet2);
        Tweet tweet = (Tweet) getIntent().getSerializableExtra(TimelineActivity.KEY_TWEET);
        setUp();
        setData(tweet);


    }
    public void setUp(){
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTimeStamp = (TextView) findViewById(R.id.tvRelativeTimestamp);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvRetweet = (TextView) findViewById(R.id.tvReTweet);
        tvLike = (TextView) findViewById(R.id.tvNumberOfLike);
    }

    public void setData(Tweet tweet)
    {
        Glide.with(getApplicationContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);
        tvUserName.setText(tweet.getUser().getScreenName());
        tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.getCreateAt()));
        tvBody.setText(tweet.getBody());
        tvCreatedAt.setText(tweet.getCreateAt());
        tvRetweet.setText(tweet.getRetweetCount());
        tvLike.setText(tweet.getUser().getFavouritesCount());


    }
}
