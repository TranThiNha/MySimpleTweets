package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.PostNewTweetDialog;
import com.codepath.apps.mysimpletweets.ReplyDialog;
import com.codepath.apps.mysimpletweets.adapter.FragmentSimpleAdapter;
import com.codepath.apps.mysimpletweets.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Profile;
import com.codepath.apps.mysimpletweets.models.RoundImage;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsArrayAdapter mTweetsArrayAdapter;
    private RecyclerView rvTweets;
    private ProgressBar pbMoreLoading;
    private PostNewTweetDialog mPostNewTweetDialog;
    private ReplyDialog mReplyDialog;
    public static String KEY_TWEET ="tweet";
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeContainer;
    private ViewPager mViewPager;
    private FragmentSimpleAdapter mFragmentSimpleAdapter;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    View headrLayout;
    ImageButton imageButton;
    RoundImage roundImage;
    ImageView ivImage;
    TextView tvName;
    TextView tvMail;
    TwitterClient mClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rvTweets = (RecyclerView) findViewById(R.id.rvResult);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //mViewPager = (ViewPager) findViewById(R.id.viewPager);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        headrLayout = nvDrawer.getHeaderView(0);
        ivImage = (ImageView) headrLayout.findViewById(R.id.ibImage);
        tvName = (TextView)headrLayout.findViewById(R.id.tvName);
        tvMail = (TextView)headrLayout. findViewById(R.id.tvMail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        setSupportActionBar(toolbar);
        mTweetsArrayAdapter = new TweetsArrayAdapter();
        //connect adapter to listview
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(1);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(mLayoutManager);
        rvTweets.setAdapter(mTweetsArrayAdapter);


        pbMoreLoading = (ProgressBar) findViewById(R.id.pdMoreLoading);

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
            public void onItemClick(Tweet tweet)
            {
                Intent intent = new Intent(getApplicationContext(), DetailTweetActivity.class);

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
                replyDialog.shoDialog(TimelineActivity.this);
            }
        });


        //get the client
        client = TwitterApplication.getRestClient();
        populateTimeline(1);
        setHeaderData(client);


    }

    private void setHeaderData(TwitterClient client){
        client = TwitterApplication.getRestClient();
        client.getProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(),User.class);
                if (!user.getProfileImageUrl().isEmpty())
                {
                    Glide.with(TimelineActivity.this)
                            .load(user.getProfileImageUrl())
                            .bitmapTransform(new CropCircleTransformation(TimelineActivity.this))
                            .into(ivImage);
                }

                tvName.setText(user.getName());
                tvMail.setText(user.getScreenName());
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
       // switch (item.getItemId()){
         //   case R.id.menu_action_profile:-
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //fragmentClass = Profile.class;
                //break;
           // default:break;
        //}
        /*try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/


        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        MenuItem postItem = menu.findItem(R.id.miEdit);
        MenuItem profileItem = menu.findItem(R.id.miProfile);
        MenuItem homeItem = menu.findItem(R.id.mihome);
        postItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPostNewTweetDialog = PostNewTweetDialog.newInstance();
               /// mPostNewTweetDialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog_FullScreen);
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

        homeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(),TimelineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });

        profileItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}
