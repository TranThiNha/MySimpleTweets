package com.codepath.apps.mysimpletweets.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.Fragment.UserFavouriteFragment;
import com.codepath.apps.mysimpletweets.Fragment.UserTweetFragment;

/**
 * Created by MyPC on 12/15/2016.
 */
public class ProfileTablayoutAdapter extends FragmentPagerAdapter {

    public static String KEY_NAME = "screenName";

    public ProfileTablayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return UserTweetFragment.newInstance();
            case 1: return UserFavouriteFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "TWEET";
            case 1: return "FAVOURITE";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
