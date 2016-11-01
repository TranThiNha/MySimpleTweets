package com.codepath.apps.mysimpletweets;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.activity.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ThiNha on 10/29/2016.
 */
public class PostNewTweetDialog extends DialogFragment  {

    private EditText edtStatus;
    private Button btnTweet;
    private TextView tvNumberCharacter;
    private DialogListener mListener;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private ImageView ivProfileImage;
    private TwitterClient mClient;
    private Button btnClear;

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
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //request
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void setCharsCounter(EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNumberCharacter.setText(String.valueOf(140 - s.size()));
                if (s.size() > 140)
                {
                    tvNumberCharacter.setTextColor(ContextCompat.getColor(tvNumberCharacter.getContext(),android.R.color.holo_red_light));
                }
                else
                {
                    tvNumberCharacter.setTextColor(Color.parseColor("#9E9E9E"));
                }

                if (s.size() > 0 && s.size() <= 140)
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

    @Override
    public void onPause() {
        //Get existing layout params of window
        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();

        //Assign window properties to layout params width and height
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        //Set the layout params back to the dialog
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams)layoutParams);

        // Call super onResume after sizing
        super.onResume();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtStatus = (EditText) view.findViewById(R.id.edtStatus);
        tvNumberCharacter = (TextView) view.findViewById(R.id.tvNumberCharcter);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        tvProfileName = (TextView) view.findViewById(R.id.tvProfileName);
        tvProfileEmail = (TextView) view.findViewById(R.id.tvProfileEmail);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        btnClear = (Button) view .findViewById(R.id.btnClear);
        mClient = TwitterApplication.getRestClient();
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = edtStatus.getText().toString();
                mListener.onButtonClickListener(status);
                dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtStatus.setText("");
            }
        });
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
        edtStatus.requestFocus();
    }

    public static PostNewTweetDialog newInstance() {
        PostNewTweetDialog frag = new PostNewTweetDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }



}
