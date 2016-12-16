package com.codepath.apps.mysimpletweets.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activity.DetailTweetActivity;
import com.codepath.apps.mysimpletweets.activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapter.ProfileTablayoutAdapter;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.helper.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.models.Media;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 12/15/2016.
 */
public class UserFavouriteFragment extends Fragment {

    private String mScreenName;
    private int count = 10;
    private RecyclerView rvTweets;
    LinearLayoutManager mLayoutmanager;
    TweetsArrayAdapter mAdapter;
    TwitterClient mClient;


    public static UserFavouriteFragment newInstance(){
        Bundle args = new Bundle();
        UserFavouriteFragment fragment = new UserFavouriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_timeline,container,false);
        mScreenName = ProfileActivity.mScreenName;
        rvTweets = (RecyclerView) rootView.findViewById(R.id.rvResult);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClient = TwitterApplication.getRestClient();
        mLayoutmanager = new LinearLayoutManager(getContext());
        mAdapter = new TweetsArrayAdapter(getContext());
        rvTweets.setLayoutManager(mLayoutmanager);
        rvTweets.setAdapter(mAdapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(decoration);
        mAdapter.setTweet(ProfileActivity.mFavouriteList);
        setListener();

    }

    /*void setUp(){
        mClient.getFavouriteList(mScreenName,count, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mAdapter.setTweet(mTweetList);
            }
        });
    }*/

    void setListener(){
        mAdapter.setOnProfileImageClickListener(new TweetsArrayAdapter.ProfileImageClickListener() {
            @Override
            public void onProfileImageClick(Tweet tweet) {
                mClient.getAccount(tweet.getUser().getUid(), tweet.getUser().getScreenName(),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.toString(), User.class);
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        intent.putExtra("id",user.getUid());
                        intent.putExtra("name",user.getScreenName());
                        startActivity(intent);
                    }
                });
            }
        });

        mAdapter.setListClickListener(new TweetsArrayAdapter.ListClickListener() {
            @Override
            public void onItemClick(Tweet tweet)
            {
                Intent intent = new Intent(getContext(), DetailTweetActivity.class);

                List<Media> medias = tweet.getMedias();
                if(medias!=null && medias.size() > 0)
                {
                    intent.putExtra("media",tweet.getMedias().get(0).getMediaUrl());
                }
                else
                    intent.putExtra("media","");
                intent.putExtra("id",tweet.getUid());
                intent.putExtra("profileImage",tweet.getUser().getProfileImageUrl());
                intent.putExtra("userName",tweet.getUser().getScreenName());
                intent.putExtra("timeStamp",tweet.getRelativeTimestamp());
                intent.putExtra("body",tweet.getBody());
                intent.putExtra("retweet",tweet.getRetweetCount());
                intent.putExtra("like",tweet.getFavouritesCount());
                intent.putExtra("liked",tweet.isFavourited());
                intent.putExtra("retweeted",tweet.isRetweeted());
                startActivity(intent);
            }
        });
    }
}
