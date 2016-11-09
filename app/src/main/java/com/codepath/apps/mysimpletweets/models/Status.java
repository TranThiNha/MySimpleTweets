package com.codepath.apps.mysimpletweets.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MyPC on 11/8/2016.
 */
public class Status {
    @SerializedName("retweet_count")
    private int retweetCount;

    public int getRetweetCount() {
        return retweetCount;
    }
}
