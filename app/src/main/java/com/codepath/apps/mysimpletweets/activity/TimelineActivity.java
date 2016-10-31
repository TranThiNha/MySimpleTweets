package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.DetailTweetActivity;
import com.codepath.apps.mysimpletweets.PostNewTweetDialog;
import com.codepath.apps.mysimpletweets.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsArrayAdapter mTweetsArrayAdapter;
    private RecyclerView rvTweets;
    private ProgressBar pbMoreLoading;
    private PostNewTweetDialog mPostNewTweetDialog;
    public static String KEY_TWEET ="tweet";
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rvTweets = (RecyclerView) findViewById(R.id.rvResult);
        //create the arraylist

        //construct the adapter fromdata source
        mTweetsArrayAdapter = new TweetsArrayAdapter();
        //connect adapter to listview

        mLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(mLayoutManager);
        rvTweets.setAdapter(mTweetsArrayAdapter);

        pbMoreLoading = (ProgressBar) findViewById(R.id.pdMoreLoading);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                pbMoreLoading.setVisibility(View.VISIBLE);
                populateTimelineLoadMore(page);
                pbMoreLoading.setVisibility(View.GONE);
            }
        });

        mTweetsArrayAdapter.setListClickListener(new TweetsArrayAdapter.ListClickListener() {
            @Override
            public void onArticleItemClick(Tweet tweet)
            {
                Intent intent = new Intent(getApplicationContext(), DetailTweetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_TWEET,tweet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //get the client
        client = TwitterApplication.getRestClient();
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

                mTweetsArrayAdapter.setTweet(mTweetList);

                Log.d("Debug",mTweetsArrayAdapter.toString());
            }
            //FAILED

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug",errorResponse.toString());
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
                Log.d("Debug",errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        MenuItem postItem = menu.findItem(R.id.miEdit);
        postItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPostNewTweetDialog = PostNewTweetDialog.newInstance("");
                mPostNewTweetDialog.show(getFragmentManager(),"");
                mPostNewTweetDialog.setOnButtonClickListener(new PostNewTweetDialog.DialogListener() {
                    @Override
                    public void onButtonClickListener(String status) {
                        client.postTweet(status, new JsonHttpResponseHandler());

                    }
                });
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
}