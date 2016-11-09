package com.codepath.apps.mysimpletweets.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.codepath.apps.mysimpletweets.activity.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.TimeLineFragment;

/**
 * Created by ThiNha on 11/2/2016.
 */
public class FragmentSimpleAdapter extends FragmentPagerAdapter{
    public FragmentSimpleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TimeLineFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
