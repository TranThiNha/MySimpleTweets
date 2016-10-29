package com.codepath.apps.mysimpletweets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private ArrayList<Tweet> mTweets;
    private TweetsArrayAdapter mTweetsArrayAdapter;
    private ListView lvTweets;
    private ProgressBar pbMoreLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        //create the arraylist
        mTweets = new ArrayList<Tweet>();
        //construct the adapter fromdata source
        mTweetsArrayAdapter = new TweetsArrayAdapter(this, mTweets);
        //connect adapter to listview
        lvTweets.setAdapter(mTweetsArrayAdapter);

        pbMoreLoading = (ProgressBar) findViewById(R.id.pdMoreLoading);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                pbMoreLoading.setVisibility(View.VISIBLE);
                populateTimeline(page);
                pbMoreLoading.setVisibility(View.GONE);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
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
                try {
                    mTweetsArrayAdapter.addAll(Tweet.fromJSONArray(json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Debug",mTweetsArrayAdapter.toString());
            }
            //FAILED

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug",errorResponse.toString());
            }
        });
    }

}
