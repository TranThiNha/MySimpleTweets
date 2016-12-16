package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.ParseException;

/**
 * Created by MyPC on 12/13/2016.
 */
public class Chat {
    @SerializedName("id")
    long id;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("recipient_screen_name")
    String recipientScreenName;

    @SerializedName("sender_screen_name")
    String senderScreenName;

    @SerializedName("text")
    String text;

    @SerializedName("recipient")
    private JsonObject recipient;

    public String getRecipientAvatar(){
        return recipient.get("profile_image_url").getAsString();
    }

    @SerializedName("sender")
    private JsonObject sender;

    public String getSenderAvatar(){
        return sender.get("profile_image_url").getAsString();
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return getRelativeTimeAgo(createdAt);
    }

    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public String getText() {
        return text;
    }

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
