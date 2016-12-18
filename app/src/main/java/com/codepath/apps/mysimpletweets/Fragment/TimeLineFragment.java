package com.codepath.apps.mysimpletweets.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.DBHelper.DBHelper;
import com.codepath.apps.mysimpletweets.helper.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Dialog.ReplyDialog;
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
    static int page = 1;
    private DBHelper mDbHelper;
    private SQLiteDatabase mDatabase;
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
        mDbHelper = new DBHelper(getContext());
        mDatabase = mDbHelper.getReadableDatabase();
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
        if (!isNetworkAvailable()){
            mTweetsArrayAdapter.setTweet(mDbHelper.parseHomeTimeLine(mDbHelper.getHomeTimeLineData()));
        }
        else {
            mDbHelper.clearHomeTimeLineTable();
        }
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pbMoreLoading.setVisibility(View.VISIBLE);
                populateTimelineLoadMore(page+=1);
                pbMoreLoading.setVisibility(View.GONE);
            }
        });

        mTweetsArrayAdapter.setOnProfileImageClickListener(new TweetsArrayAdapter.ProfileImageClickListener() {
            @Override
            public void onProfileImageClick(Tweet tweet) {
                if(!isNetworkAvailable()){
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("id",tweet.getUserId());
                    intent.putExtra("name",tweet.getUserScreenName());
                    String  name= tweet.getUserScreenName();
                    startActivity(intent);
                }

                client.getAccount(tweet.getUserId(), tweet.getUserScreenName(),new JsonHttpResponseHandler(){
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
                if(tweet.getMediaUrl()!=null)
                {
                    intent.putExtra("media",tweet.getMediaUrl());
                }
                else
                    intent.putExtra("media","");
                intent.putExtra("id",tweet.getUid());
                intent.putExtra("profileImage",tweet.getAvatarUrl());
                intent.putExtra("userName",tweet.getUserScreenName());
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
        populateTimeline(page);

    }

    private void populateTimeline(int page) {
        client.getHomeTimeline(page,new JsonHttpResponseHandler(){
            //SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug",json.toString());
                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                for (int i =0;i < mTweetList.size();i++){
                    mTweetList.get(i).getUser();
                    mTweetList.get(i).getMedias();
                    mDbHelper.insertTweetIntoHomeTimeLine(mTweetList.get(i));
                    mDbHelper.insertAccount(mTweetList.get(i).getUser());
                }
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
        client.getHomeTimeline(page ,new JsonHttpResponseHandler(){
            //SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug",json.toString());
                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                for (int i =0;i < mTweetList.size();i++){
                    mTweetList.get(i).getUser();
                    mTweetList.get(i).getMedias();
                    mDbHelper.insertTweetIntoHomeTimeLine(mTweetList.get(i));
                    String  n = mTweetList.get(i).getUser().getScreenName();
                    mDbHelper.insertAccount(mTweetList.get(i).getUser());
                }
                mTweetsArrayAdapter.addTweet(mTweetList);
                Log.d("Debug",mTweetsArrayAdapter.toString());
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
