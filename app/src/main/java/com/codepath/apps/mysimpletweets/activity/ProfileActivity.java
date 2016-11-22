package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.ProfileAdapter;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 11/8/2016.
 */
public class ProfileActivity extends AppCompatActivity {
    ImageView ivImage;
    TextView tvName;
    TextView tvMail;
    TextView tvNumberTweet;
    TextView tvNumberFollow;
    TextView tvNumberFollower;
    RecyclerView rvTweets;
    LinearLayoutManager mLayoutManager;
    ProfileAdapter mProfileAdapter;
    TwitterClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUp();
        mLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(mLayoutManager);
        mProfileAdapter = new ProfileAdapter();
        //rvTweets.setAdapter(mProfileAdapter);

    }

    private void setUp() {
        ivImage = (ImageView) findViewById(R.id.image);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMail = (TextView) findViewById(R.id.tvMail);
        tvNumberTweet = (TextView) findViewById(R.id.tvNumberTweet);
        tvNumberFollow = (TextView) findViewById(R.id.tvNumberFollow);
        tvNumberFollower = (TextView) findViewById(R.id.tvNumberFollowers);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        mClient = TwitterApplication.getRestClient();

        Intent intent = getIntent();
        long id;
        String name;
        id = intent.getLongExtra("id",0);
        name = intent.getStringExtra("name");
        if (id !=0){
         mClient.getAccount(id,name,new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 Gson gson = new Gson();
                 User user1 = gson.fromJson(response.toString(),User.class);
                 Glide.with(ivImage.getContext())
                         .load(user1.getProfileImageUrl())
                         .into(ivImage);
                 tvName.setText(user1.getName());
                 tvMail.setText("@"+user1.getScreenName());
                 tvNumberTweet.setText(String.valueOf(user1.getStatusesCount()));
                 tvNumberFollow.setText(String.valueOf(user1.getFavouritesCount()));
                 tvNumberFollower.setText(String.valueOf(user1.getFollowersCount()));
             }
         });
        }

        else {
            mClient.getProfile(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.toString(), User.class);
                    Glide.with(ivImage.getContext())
                            .load(user.getProfileImageUrl())
                            .into(ivImage);
                    tvName.setText(user.getName());
                    tvMail.setText(user.getScreenName());

                    tvNumberTweet.setText(String.valueOf(user.getStatusesCount()));
                    tvNumberFollow.setText(String.valueOf(user.getFavouritesCount()));
                    tvNumberFollower.setText(String.valueOf(user.getFollowersCount()));
                }
            });
        }
    }
}
