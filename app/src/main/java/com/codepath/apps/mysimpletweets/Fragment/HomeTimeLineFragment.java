package com.codepath.apps.mysimpletweets.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;

/**
 * Created by MyPC on 11/10/2016.
 */
public class HomeTimeLineFragment extends Fragment {

    public static HomeTimeLineFragment newInstance(){
        Bundle args = new Bundle();
        HomeTimeLineFragment fragment = new HomeTimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_timeline,container,false);
    }


}
