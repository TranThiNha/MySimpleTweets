package com.codepath.apps.mysimpletweets.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThiNha on 10/31/2016.
 */
public class Media {
    @SerializedName("id")
    private long id;

    @SerializedName("media_url")
    private String mediaUrl;

    @SerializedName("type")
    private String type;

    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getType() {
        return type;
    }
}
