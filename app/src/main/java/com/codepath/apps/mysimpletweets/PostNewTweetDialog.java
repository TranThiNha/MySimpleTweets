package com.codepath.apps.mysimpletweets;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.activity.TimelineActivity;

/**
 * Created by ThiNha on 10/29/2016.
 */
public class PostNewTweetDialog extends DialogFragment  {

    private EditText edtStatus;
    private Button btnTweet;
    private TextView tvNumberCharacter;
    private DialogListener mListener;

    public interface DialogListener{
        void onButtonClickListener(String status);
    }

    public void setOnButtonClickListener(DialogListener dialogListener){
        mListener = dialogListener;
    }

    public PostNewTweetDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.avtivity_post_new_tweet, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtStatus = (EditText) view.findViewById(R.id.edtStatus);
        tvNumberCharacter = (TextView) view.findViewById(R.id.tvNumberCharcter);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = edtStatus.getText().toString();
                mListener.onButtonClickListener(status);
                dismiss();
            }
        });

    }

    public static PostNewTweetDialog newInstance(String title) {
        PostNewTweetDialog frag = new PostNewTweetDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }



}
