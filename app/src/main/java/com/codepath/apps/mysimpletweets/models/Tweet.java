package com.codepath.apps.mysimpletweets.models;

import android.provider.MediaStore;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.ParseException;

/**
 * Created by ThiNha on 10/29/2016.
 */

public class Tweet implements Serializable {


    @SerializedName("retweeted")
    private boolean retweeted;

    public boolean isRetweeted() {
        return retweeted;
    }

    public boolean isFavourited() {
        return favourited;
    }

    @SerializedName("favorited")
    private boolean favourited;

    //list out the attributes
    @SerializedName("text")
    String body;

    @SerializedName("id")
    long uid; //unique Id for the tweet

    @SerializedName("location")
    String location;



    @SerializedName("retweet_count")
    int retweetCount;

    @SerializedName("favorite_count")
    int favouritesCount;


    @SerializedName("created_at")
    String createAt;

    @SerializedName("user")
    private User user;

    @SerializedName("url")
    private String Url;

    public String getUrl() {
        return Url;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    String relativeTimestamp;

    public String getLocation() {
        return location;
    }

    @SerializedName("entities")
    private JsonObject entities;

    private List<Media>medias;

    public List<Media>getMedias(){
        Gson gson = new Gson();
        medias = gson.fromJson(entities.getAsJsonArray("media"),new TypeToken<List<Media>>(){}.getType());
        return medias;
    }


    public int getRetweetCount() {
        return retweetCount;
    }

    public String getRelativeTimestamp() {
        return relativeTimestamp;
    }

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

}
