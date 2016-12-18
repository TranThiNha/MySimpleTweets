package com.codepath.apps.mysimpletweets.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.DBHelper.DBHelper;
import com.codepath.apps.mysimpletweets.adapter.ProfileTablayoutAdapter;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.Dialog.ShowImageDialog;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 11/8/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    public static String KEY_AVARTAR ="avatar";
    public static String KEY_COVER ="cover";
    public static String KEY_ID="id";
    public static  String KEY_SCREENNAME ="screenName";
    private DBHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    ImageView ivImage;
    ImageView ivCover;
    TextView tvName;
    TextView tvMail;
    TextView tvNumberTweet;
    TextView tvNumberFollow;
    TextView tvNumberFollower;
    Button btnChangeProfile;
    Button btnFriend;
    LinearLayoutManager mLayoutManager;
    TwitterClient mClient;
    ProfileTablayoutAdapter mAdapter;
    String avatarImage ="", coverImage ="";
    ShowImageDialog mDialog;
    TabLayout mTablayout;
    ViewPager mViewPager;
    public static List<Tweet> mFavouriteList = new ArrayList<>();
    public static List<Tweet> mTweetList = new ArrayList<>();

    long mId;
    int count = 5;
    public static String mScreenName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUp();
        loadDataFromChangeProfileActivity();
        tvNumberFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FollowerListActivity.class);
                intent.putExtra(KEY_ID, mId);
                intent.putExtra(KEY_SCREENNAME, mScreenName);
                startActivity(intent);

            }
        });

    }


    private void setUp() {
        ivImage = (ImageView) findViewById(R.id.image);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMail = (TextView) findViewById(R.id.tvMail);
        tvNumberTweet = (TextView) findViewById(R.id.tvNumberTweet);
        tvNumberFollow = (TextView) findViewById(R.id.tvNumberFollow);
        tvNumberFollower = (TextView) findViewById(R.id.tvNumberFollowers);
        ivCover = (ImageView) findViewById(R.id.ivCoverImage);
        mClient = TwitterApplication.getRestClient();
        btnChangeProfile = (Button) findViewById(R.id.btnChangeProfile);
        btnFriend = (Button) findViewById(R.id.btnFriend);
        mTablayout = (TabLayout) findViewById(R.id.tablayoutProfile);
        mViewPager = (ViewPager) findViewById(R.id.pagerProfile);
        mLayoutManager = new LinearLayoutManager(this);
        final Intent intent = getIntent();
        final long id;
        String name;

        id = intent.getLongExtra("id",0);
        name = intent.getStringExtra("name");
        if (id !=0){
            mDbHelper = new DBHelper(getApplicationContext());
            mDatabase = mDbHelper.getReadableDatabase();
            if(isNetworkAvailable())
            {
                mClient.getAccount(id,name,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new Gson();
                    User user1 = gson.fromJson(response.toString(),User.class);
                    mScreenName = user1.getScreenName();
                    setDataAccount(user1);
                    }
                });
            }else {
                mScreenName = name;
                User user = mDbHelper.parseAccount(mDbHelper.getAccountData(id),mScreenName);
                if (user!=null)
                    setDataAccount(user);
            }
        }

        else {
            mDbHelper = new DBHelper(getApplicationContext());
            mDatabase = mDbHelper.getReadableDatabase();
            if(isNetworkAvailable()) {
                mClient.getProfile(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        mDbHelper.clearProfileTable();
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.toString(), User.class);
                        mScreenName = user.getScreenName();
                        setDataProfile(user);
                    }
                });
            }else {
                mScreenName = name;
                User user = mDbHelper.parseProfile(mDbHelper.getProfileData());
                setDataProfile(user);
            }
        }


        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ShowImageDialog(coverImage);
                mDialog.showDialog(ProfileActivity.this);
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ShowImageDialog(avatarImage);
                mDialog.showDialog(ProfileActivity.this);            }
        });
    }

    private void loadDataFromChangeProfileActivity(){
        Intent intent = getIntent();
        Bitmap bitmapAvatar = (Bitmap) intent.getParcelableExtra(ChangeProfileActivity.KEY_BITMAP_AVATAR);
        Bitmap bitmapCover = (Bitmap) intent.getParcelableExtra(ChangeProfileActivity.KEY_BITMAP_COVER);

        if(!(bitmapAvatar==null))
        {
            ivImage.setImageBitmap(bitmapAvatar);
        }
        if (!(bitmapCover == null))
        {
            ivCover.setImageBitmap(bitmapCover);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void setDataAccount(User user1){
        String n = user1.getScreenName();
        avatarImage = user1.getProfileImageUrl();
        coverImage = user1.getProfileBackgroundImageUrl();
        mId = user1.getUid();
        mAdapter = new ProfileTablayoutAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        btnChangeProfile.setVisibility(View.GONE);

        if (user1.getFollowRequestSent()){
            btnFriend.setText("UNFOLLOW");
        }
        else {
            btnFriend.setText("FOLLOW");
        }
        btnFriend.setVisibility(View.VISIBLE);
        Glide.with(ivImage.getContext())
                .load(user1.getProfileImageUrl())
                .into(ivImage);
        tvName.setText(user1.getName());
        tvMail.setText("@"+user1.getScreenName());
        tvNumberTweet.setText(String.valueOf(user1.getStatusesCount()));
        tvNumberFollow.setText(String.valueOf(user1.getFavouritesCount()));
        tvNumberFollower.setText(String.valueOf(user1.getFollowersCount()));
        if (user1.isProfileUseBackgroundImage()){
            Glide.with(ivCover.getContext())
                    .load(coverImage)
                    .into(ivCover);
        }
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),ChangeProfileActivity.class);
                intent1.putExtra(KEY_AVARTAR,avatarImage);
                intent1.putExtra(KEY_COVER,coverImage);
                startActivity(intent1);
            }
        });
    }

    private void setDataProfile(User user){
        avatarImage = user.getProfileImageUrl();
        coverImage = user.getProfileBackgroundImageUrl();
        mId = user.getUid();
        mAdapter = new ProfileTablayoutAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        btnChangeProfile.setVisibility(View.VISIBLE);
        btnFriend.setVisibility(View.GONE);
        Glide.with(ivImage.getContext())
                .load(user.getProfileImageUrl())
                .into(ivImage);
        tvName.setText(user.getName());
        tvMail.setText(user.getScreenName());
        tvNumberTweet.setText(String.valueOf(user.getStatusesCount()));
        tvNumberFollow.setText(String.valueOf(user.getFavouritesCount()));
        tvNumberFollower.setText(String.valueOf(user.getFollowersCount()));
        if (user.isProfileUseBackgroundImage()){
            Glide.with(ivCover.getContext())
                    .load(coverImage)
                    .into(ivCover);
        }

        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),ChangeProfileActivity.class);
                intent1.putExtra(KEY_AVARTAR,avatarImage);
                intent1.putExtra(KEY_COVER,coverImage);
                startActivity(intent1);
            }
        });
    }
}
