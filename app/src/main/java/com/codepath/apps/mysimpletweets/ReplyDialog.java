package com.codepath.apps.mysimpletweets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ThiNha on 10/31/2016.
 */
public class ReplyDialog {

    private EditText edtStatus;
    private Button btnTweet;
    private TextView tvNumberCharacter;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private ImageView ivProfileImage;
    private TwitterClient mClient;
    private TextView tvReplyTo;
    private Dialog dialog;
    private Tweet mTweet;
    private Button btnClear;


    public ReplyDialog(Tweet tweet)
    {
        mTweet = tweet;
    }

    private void setCharsCounter(EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNumberCharacter.setText(String.valueOf(140 - s.length()));
                if (s.length() > 140)
                {
                    tvNumberCharacter.setTextColor(ContextCompat.getColor(tvNumberCharacter.getContext(),android.R.color.holo_red_light));
                }
                else
                {
                    tvNumberCharacter.setTextColor(Color.parseColor("#9E9E9E"));
                }

                if (s.length() > 0 && s.length() <= 140)
                {
                    //Limit the number of char in one tweet
                    btnTweet.setEnabled(true);
                }
                else
                {
                    btnTweet.setEnabled(false);
                    // mReplyContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void shoDialog(Activity activity) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.avtivity_post_new_tweet);

        edtStatus = (EditText) dialog.findViewById(R.id.edtStatus);
        tvNumberCharacter = (TextView) dialog.findViewById(R.id.tvNumberCharcter);
        btnTweet = (Button) dialog.findViewById(R.id.btnTweet);
        tvProfileName = (TextView) dialog.findViewById(R.id.tvProfileName);
        tvProfileEmail = (TextView) dialog.findViewById(R.id.tvProfileEmail);
        ivProfileImage = (ImageView) dialog.findViewById(R.id.ivProfileImage);
        mClient = TwitterApplication.getRestClient();
        btnClear = (Button) dialog.findViewById(R.id.btnClear);
        tvReplyTo = (TextView) dialog.findViewById(R.id.tvReplyTo);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //mClient.updateReply(mTweet.getBody(), mTweet.getUid(),new JsonHttpResponseHandler());
               dialog.cancel();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtStatus.setText("");
            }
        });
        edtStatus.setHint("");
        mClient.getProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(),User.class);
                tvProfileName.setText(user.getName());
                tvProfileEmail.setText(user.getScreenName());
                Glide.with(ivProfileImage.getContext())
                        .load(user.getProfileImageUrl())
                        .into(ivProfileImage);
            }
        });
	setCharsCounter(edtStatus);
        tvReplyTo.setText("reply to "+ mTweet.getUser().getName());
        edtStatus.requestFocus();
        dialog.show();
    }



}
