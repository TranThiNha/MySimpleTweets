package com.codepath.apps.mysimpletweets.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ThiNha on 10/29/2016.
 */
public class User implements Serializable {
    //list attributea
    @SerializedName("description")
    String description;

    public String getDescription() {
        return description;
    }

    @SerializedName("following")
    boolean followRequestSent;

    public boolean getFollowRequestSent() {
        return followRequestSent;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long uid;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

    @SerializedName("favourites_count")
    int favouritesCount;

    @SerializedName("followers_count")
    int followersCount;

    @SerializedName("profile_background_image_url")
    private String profileBackgroundImageUrl;

    @SerializedName("profile_use_background_image")
    private boolean profileUseBackgroundImage;

    public boolean isProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    @SerializedName("status")
    JsonObject status;

    @SerializedName("statuses_count")
    int statusesCount;

    public int getStatusesCount(){
        return statusesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }


    public int getRetweetCount(){
        Gson gson = new Gson();
        int retweetCount = Integer.valueOf(status.get("retweet_count").toString());
        return retweetCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }



}
