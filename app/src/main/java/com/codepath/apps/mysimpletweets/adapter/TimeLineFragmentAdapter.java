package com.codepath.apps.mysimpletweets.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.Fragment.ChatFragment;
import com.codepath.apps.mysimpletweets.Fragment.FavouriteDestinationFragment;
import com.codepath.apps.mysimpletweets.Fragment.TimeLineFragment;

/**
 * Created by MyPC on 11/10/2016.
 */
public class TimeLineFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public TimeLineFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return TimeLineFragment.newInstance();
            case 1: return ChatFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Home Time Line";
            case 1: return "Mention line";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
