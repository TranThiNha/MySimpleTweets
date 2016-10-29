package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ThiNha on 10/29/2016.
 */
//taking the tweet object and turning them into View displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context,List<Tweet>tweets) {
        super(context,android.R.layout.simple_list_item_1, tweets);
    }

    //Override and setup custom template


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //GET the tweet
        Tweet tweet = getItem(position);
        //find or inflate the template
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        //find the subviews to fill with data in the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
        //populate data intto the subviews into the list
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.getCreateAt()));
        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);
        return convertView;
    }
}
