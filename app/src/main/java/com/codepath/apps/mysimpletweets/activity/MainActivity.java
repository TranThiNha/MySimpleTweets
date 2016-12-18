package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.Fragment.MainFragment;
import com.codepath.apps.mysimpletweets.Dialog.PostNewTweetDialog;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.TimeLineFragmentAdapter;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by MyPC on 11/10/2016.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnListItemClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TimeLineFragmentAdapter adapter;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    View headerLayout;
    private PostNewTweetDialog mPostNewTweetDialog;

    ImageView ivImage;
    TextView tvName;
    TextView tvMail;
    private TwitterClient client;

    // ý e dang chạy tren dt
    // có vysor ko?a cho e chut :p
    // khoan
    // activity nào bị?cai này luon nek a

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        client = TwitterApplication.getRestClient();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        headerLayout = nvDrawer.getHeaderView(0);
        ivImage = (ImageView) headerLayout.findViewById(R.id.ibImage);
        tvName = (TextView)headerLayout.findViewById(R.id.tvName);
        tvMail = (TextView)headerLayout. findViewById(R.id.tvMail);
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TimeLineFragmentAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        setHeaderData(client);
    }
    private void setHeaderData(TwitterClient _client){
        _client = TwitterApplication.getRestClient();
        _client.getProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(),User.class);
                if (!user.getProfileImageUrl().isEmpty())
                {
                    Glide.with(MainActivity.this)
                            .load(user.getProfileImageUrl())
                            .bitmapTransform(new CropCircleTransformation(MainActivity.this))
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
        intent.putExtra("id",1);
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
         return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        MenuItem postItem = menu.findItem(R.id.miEdit);
        MenuItem profileItem = menu.findItem(R.id.miProfile);
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

        profileItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onStart() {
        super.onStart();
       // EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onListItemClick(int position) {
        /*Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FavouriteDestinationFragment();
                break;

            case 1:
                fragment = new FavouriteDesGridFragment();
                break;
        }*/

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
            return super.onKeyDown(keyCode, event);

    }
}
