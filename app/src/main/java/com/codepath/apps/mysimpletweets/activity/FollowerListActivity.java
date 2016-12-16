package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.UserListAdapter;
import com.codepath.apps.mysimpletweets.models.Profile;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Condition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 12/14/2016.
 */
public class FollowerListActivity extends AppCompatActivity {

    RecyclerView rvFollowerList;
    LinearLayoutManager mLayoutManager;
    UserListAdapter mAdapter;
    TwitterClient mClient;
    long id;
    String screenName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        loadData();
        rvFollowerList = (RecyclerView) findViewById(R.id.rvUserList);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new UserListAdapter();
        mClient = TwitterApplication.getRestClient();
        getdata();
        mAdapter.setOnAvatarClickListener(new UserListAdapter.AvatarClickListener() {
            @Override
            public void onClick(User user) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("id",user.getUid());
                intent.putExtra("name",user.getScreenName());
                startActivity(intent);
            }
        });

    }

    private void loadData(){
        Intent intent = getIntent();
        id = intent.getLongExtra(ProfileActivity.KEY_ID,0);
        screenName = intent.getStringExtra(ProfileActivity.KEY_SCREENNAME);
    }

    private void getdata() {

        mClient.getFollwerList(screenName,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                List<User> list = gson.fromJson(response.toString(), new TypeToken<List<Tweet>>(){}.getType() );
                mAdapter.setData(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(),responseString,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
