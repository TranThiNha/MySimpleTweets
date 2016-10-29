package com.codepath.apps.mysimpletweets.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ThiNha on 10/29/2016.
 */
public class User {
    //list attributea
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long uid;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

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

    public static User fromJSON(JSONObject json){
        Gson gson = new Gson();
        User result = new User();
        User temp = gson.fromJson(String.valueOf(json),User.class);
        //Extract and fill the values

        result.name = temp.getName();
        result.uid = temp.getUid();
        result.screenName = temp.getScreenName();
        result.profileImageUrl = temp.getProfileImageUrl();

        //return  a user
        return  result;
    }

}
