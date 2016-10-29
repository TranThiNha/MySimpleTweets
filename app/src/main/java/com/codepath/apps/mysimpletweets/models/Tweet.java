package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.ParseException;

/**
 * Created by ThiNha on 10/29/2016.
 */

public class Tweet implements Serializable {
    //list out the attributes
    @SerializedName("text")
    String body;

    @SerializedName("id")
    long uid; //unique Id for the tweet

    @SerializedName("created_at")
    String createAt;
    String relativeTimestamp;

    private User user;
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreateAt() {
        return createAt;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public Tweet fromJSON(JSONObject jsonObject){
        Gson gson = new Gson();
        Tweet result = new Tweet();
        Tweet tweet = gson.fromJson(String.valueOf(jsonObject),Tweet.class);

        //Extract the values from te json, store then
        try {
            result.relativeTimestamp =String.valueOf(getRelativeTimeAgo(tweet.getCreateAt()));
            result.body = tweet.getBody();
            result.uid = tweet.getUid();
            result.createAt = tweet.getCreateAt();
            result.user = User.fromJSON(jsonObject.getJSONObject("user"));
            //tweet.user

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return the tweet oject
        return result;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        Gson gson = new Gson();
        for (int i =0; i< jsonArray.length(); i++){
            tweets.add(gson.fromJson(String.valueOf(jsonArray.get(i)),Tweet.class));
        }
        return  tweets;
    }


}
