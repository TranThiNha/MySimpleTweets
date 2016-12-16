package com.codepath.apps.mysimpletweets.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapter.ChatAdapter;
import com.codepath.apps.mysimpletweets.models.Chat;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MyPC on 12/13/2016.
 */
public class ChatFragment extends Fragment {

    RecyclerView rvChats;
    ChatAdapter mAdapter;
    LinearLayoutManager layoutManager;
    TwitterClient mClient;

    public static ChatFragment newInstance(){
        Bundle argvs = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(argvs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        rvChats = (RecyclerView) rootView.findViewById(R.id.rvChats);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClient = TwitterApplication.getRestClient();
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ChatAdapter();
        rvChats.setAdapter(mAdapter);
        rvChats.setLayoutManager(layoutManager);

        setData();
    }

    private void setData() {
        mClient.getChatList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                List<Chat> chats = gson.fromJson(response.toString(), new TypeToken<List<Chat>>(){}.getType() );
                mAdapter.setChats(chats);
            }
        });
    }
}
