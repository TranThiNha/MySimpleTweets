package com.codepath.apps.mysimpletweets.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.ReplyDialog;
import com.codepath.apps.mysimpletweets.activity.DetailTweetActivity;
import com.codepath.apps.mysimpletweets.activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.models.Media;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Condition;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 11/10/2016.
 */
public class TimeLineFragment extends Fragment {

    private TwitterClient client;
    private TweetsArrayAdapter mTweetsArrayAdapter;
    private RecyclerView rvTweets;
    private ProgressBar pbMoreLoading;
    private LinearLayoutManager mLayoutManager;

    public TimeLineFragment() {
    }

    public static TimeLineFragment newInstance(){
        Bundle args = new Bundle();
        TimeLineFragment fragment = new TimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootiew =inflater.inflate(R.layout.activity_timeline,container,false);
        rvTweets = (RecyclerView) rootiew.findViewById(R.id.rvResult);
        pbMoreLoading = (ProgressBar) rootiew.findViewById(R.id.pdMoreLoading);
        return rootiew;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTweetsArrayAdapter = new TweetsArrayAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(mLayoutManager);
        rvTweets.setAdapter(mTweetsArrayAdapter);
        client = TwitterApplication.getRestClient();
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        rvTweets.addItemDecoration(decoration);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pbMoreLoading.setVisibility(View.VISIBLE);
                populateTimelineLoadMore(page);
                pbMoreLoading.setVisibility(View.GONE);
            }
        });

        mTweetsArrayAdapter.setOnProfileImageClickListener(new TweetsArrayAdapter.ProfileImageClickListener() {
            @Override
            public void onProfileImageClick(Tweet tweet) {
                client.getAccount(tweet.getUser().getUid(), tweet.getUser().getScreenName(),new JsonHttpResponseHandler(){
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

        mTweetsArrayAdapter.setListClickListener(new TweetsArrayAdapter.ListClickListener() {
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

        mTweetsArrayAdapter.setReplyListener(new TweetsArrayAdapter.ReplyListener() {
            @Override
            public void onReplyListener(Tweet tweet) {
                ReplyDialog replyDialog = new ReplyDialog(tweet);
                replyDialog.shoDialog(getActivity());
            }
        });


        //get the client
        populateTimeline(1);

    }

    private void populateTimeline(int page) {
        client.getHomeTimeline(page,new JsonHttpResponseHandler(){
            //SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug",json.toString());

                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );

                mTweetsArrayAdapter.setTweet(mTweetList);//EventBus.getDefault().post(new FragmentMessageEvent(true));
                Log.d("Debug",mTweetsArrayAdapter.toString());
            }
            //FAILED

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }





    private void populateTimelineLoadMore(int page) {
        client.getHomeTimeline(page,new JsonHttpResponseHandler(){
            //SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug",json.toString());

                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );

                mTweetsArrayAdapter.addTweet(mTweetList);

                Log.d("Debug",mTweetsArrayAdapter.toString());
            }
            //FAILED

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
       // EventBus.getDefault().unregister(this);
    }

    public static class FragmentMessageEvent
    {
        public FragmentMessageEvent(boolean isFinishRefresh)
        {
            this.isFinishRefresh = isFinishRefresh;
        }

        public boolean isFinishRefresh() {
            return isFinishRefresh;
        }

        boolean isFinishRefresh = false;
    }

}
