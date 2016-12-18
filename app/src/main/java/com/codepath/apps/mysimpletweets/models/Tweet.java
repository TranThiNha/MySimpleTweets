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


    @SerializedName("retweeted") private boolean retweeted;

    @SerializedName("favorited") private boolean favourited;

    @SerializedName("text")   String body;

    @SerializedName("id")  long uid;

    @SerializedName("location")   String location;

    @SerializedName("retweet_count")   int retweetCount;

    @SerializedName("favorite_count")   int favouritesCount;

    @SerializedName("created_at")   String createAt;

    @SerializedName("user")   private User user;

    @SerializedName("url")
    private String Url;

    String avatarUrl;
    String userScreenName;
    String relativeTimestamp = "";
    String mediaUrl;
    long userId;
    @SerializedName("entities")
    private JsonObject entities;

    private List<Media>medias;

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setFavourited(boolean favourited) {
        this.favourited = favourited;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setRelativeTimestamp(String relativeTimestamp) {
        this.relativeTimestamp = relativeTimestamp;
    }

    public String getLocation() {
        return location;
    }


    public List<Media>getMedias(){
        medias = new ArrayList<>();
        Gson gson = new Gson();
        if(entities!=null)
        {
            medias = gson.fromJson(entities.getAsJsonArray("media"),new TypeToken<List<Media>>(){}.getType());
        }
        if (medias!=null && medias.size()>0){
            mediaUrl = medias.get(0).getMediaUrl();
        }else {
            mediaUrl = "";
        }
        return medias;
    }

    public String getMediaUrl() {
        return mediaUrl;
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

    public boolean isRetweeted() {
        return retweeted;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public User getUser() {
        if(user!=null)
        {
            avatarUrl = user.getProfileImageUrl();
            userScreenName = user.getScreenName();
            userId = user.getUid();
        }
        return user;

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public String getUrl() {
        return Url;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String _getRelativeTimeAgo(){
        return relativeTimestamp;
    }
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {

            if (rawJsonDate!=null) {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
                relativeTimestamp = relativeDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }



}
