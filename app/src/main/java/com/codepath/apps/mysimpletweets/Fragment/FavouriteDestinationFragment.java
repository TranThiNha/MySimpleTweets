package com.codepath.apps.mysimpletweets.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.FavouriteDesAdapter;
import com.codepath.apps.mysimpletweets.helper.OnStartDragListener;
import com.codepath.apps.mysimpletweets.helper.SimpleItemTouchHelperCallback;
import com.codepath.apps.mysimpletweets.models.Destination;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 11/16/2016.
 */
public class FavouriteDestinationFragment extends Fragment implements OnStartDragListener {
    private RecyclerView rvDes;
    private FavouriteDesAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private TwitterClient client;
    private ItemTouchHelper mItemTouchHelper;

    public FavouriteDestinationFragment() {
    }

    public static FavouriteDestinationFragment newInstance(){
        Bundle args = new Bundle();
        FavouriteDestinationFragment fragment = new FavouriteDestinationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favourite_destination,container,false);
        rvDes = (RecyclerView) rootView.findViewById(R.id.rvFavouriteDes);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        adapter = new FavouriteDesAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext()) ;
        rvDes.setLayoutManager(linearLayoutManager);
        rvDes.setAdapter(adapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        rvDes.addItemDecoration(decoration);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvDes);
        client = TwitterApplication.getRestClient();
        populateTimeline(1);
    }

    private void populateTimeline(int page) {
        client.getHomeTimeline(page,new JsonHttpResponseHandler(){
            //SUCCESS

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug",json.toString());

                Gson gson = new Gson();
                List<Tweet> mTweetList = gson.fromJson(json.toString(), new TypeToken<List<Tweet>>(){}.getType() );

                adapter.setDesS(mTweetList);//EventBus.getDefault().post(new FragmentMessageEvent(true));
                Log.d("Debug",adapter.toString());
            }
            //FAILED

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
