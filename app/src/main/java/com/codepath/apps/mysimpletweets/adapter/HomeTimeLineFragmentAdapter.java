package com.codepath.apps.mysimpletweets.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.Fragment.HomeTimeLineFragment;

/**
 * Created by MyPC on 11/10/2016.
 */
public class HomeTimeLineFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    public HomeTimeLineFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return HomeTimeLineFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Home Time Line";
            default: return "Mention line";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
