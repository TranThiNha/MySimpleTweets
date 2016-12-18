package com.codepath.apps.mysimpletweets.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 12/16/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    //database
    public static final String DATABASE_NAME = "TweetDatabase.db";

    //home time line table
    public static final String TABLE_NAME_HOME_TIME_LINE = "HomeTimeLineOfProfile";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME_USER = "screenName";
    public static final String COLUMN_AVATAR ="avatar";
    public static final String COLUMN_TIME ="time";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_RETWEET_NUMBER = "retweet";
    public static final String COLUMN_FAVOURITE_NUMBER = "favourite";
    public static final String COLUMN_IS_FAVOURITE = "isfavourite";
    public static final String COLUMN_IS_RETWEET = "isretweet";
    public static final String COLUMN_MEDIA ="media";

    //profile table
    public static final String TABLE_PROFILE = "profile";
    public static final String COLUMN_PROFILE_ID ="profileid";
    public static final String COLUMN_PROFILE_NAME = "name";
    public static final String COLUMN_PROFILE_EMAIL = "emial";
    public static final String COLUMN_PROFILE_AVATAR = "avatar";
    public static final String COLUMN_NUMBER_TWEETS = "tweets";
    public static final String COLUMN_FOLLOWING = "following";
    public static final String COLUMN_FOLLWERS = "follwers";
    public static final String COLUMN_COVERURL ="coverUrl";
    public static final String COLUMN_HAVE_COVER = "havecover";

    //account table
    public static final String TABLE_ACCOUNT = " account";

    //tweets table
    public static final String TABLE_TWEET = "profile_tweets";

    //favourite table
    public static final String TABLE_FAVOURITE = "profliefavourites";
    public static final String COLUMN_ISFOLLOWING = "isfollowing";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME_HOME_TIME_LINE+" ("
                +COLUMN_ID+" VARCHAR,"
                +COLUMN_NAME_USER+" VARCHAR,"
                + COLUMN_AVATAR+" VARCHAR,"
                +COLUMN_TIME+" VARCHAR,"
                +COLUMN_BODY+" VARCHAR,"
                +COLUMN_RETWEET_NUMBER+" INTEGER,"
                + COLUMN_FAVOURITE_NUMBER+" INTEGER,"
                + COLUMN_IS_FAVOURITE + " INTEGER,"
                + COLUMN_IS_RETWEET+" INTEGER,"
                +COLUMN_MEDIA+" VARCHAR,"
                +COLUMN_PROFILE_ID+" VARCHAR)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_PROFILE +" ("
                +COLUMN_PROFILE_ID+" VARCHAR,"
                +COLUMN_PROFILE_NAME+" VARCHAR,"
                +COLUMN_PROFILE_AVATAR+" VARCHAR,"
                +COLUMN_HAVE_COVER + " INTEGER,"
                +COLUMN_COVERURL+" VARCHAR,"
                +COLUMN_PROFILE_EMAIL+" VARCHAR,"
                +COLUMN_NUMBER_TWEETS+" INTEGER,"
                +COLUMN_FOLLOWING +" INTEGER,"
                +COLUMN_FOLLWERS+" INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ACCOUNT+" ("
                +COLUMN_PROFILE_ID+" VARCHAR,"
                +COLUMN_PROFILE_NAME+" VARCHAR,"
                +COLUMN_PROFILE_AVATAR+" VARCHAR,"
                +COLUMN_HAVE_COVER + " INTEGER,"
                +COLUMN_COVERURL+" VARCHAR,"
                +COLUMN_PROFILE_EMAIL+" VARCHAR,"
                +COLUMN_NUMBER_TWEETS+" INTEGER,"
                +COLUMN_FOLLOWING +" INTEGER,"
                +COLUMN_FOLLWERS+" INTEGER,"
                +COLUMN_ISFOLLOWING+" INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_TWEET+" ("
                +COLUMN_PROFILE_NAME+" VARCHAR,"
                +COLUMN_ID+" VARCHAR,"
                +COLUMN_NAME_USER+" VARCHAR,"
                + COLUMN_AVATAR+" VARCHAR,"
                +COLUMN_TIME+" VARCHAR,"
                +COLUMN_BODY+" VARCHAR,"
                +COLUMN_RETWEET_NUMBER+" INTEGER,"
                + COLUMN_FAVOURITE_NUMBER+" INTEGER,"
                + COLUMN_IS_FAVOURITE + " INTEGER,"
                + COLUMN_IS_RETWEET+" INTEGER,"
                +COLUMN_MEDIA+" VARCHAR)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_FAVOURITE+" ("
                +COLUMN_PROFILE_NAME+" VARCHAR,"
                +COLUMN_ID+" VARCHAR,"
                +COLUMN_NAME_USER+" VARCHAR,"
                + COLUMN_AVATAR+" VARCHAR,"
                +COLUMN_TIME+" VARCHAR,"
                +COLUMN_BODY+" VARCHAR,"
                +COLUMN_RETWEET_NUMBER+" INTEGER,"
                + COLUMN_FAVOURITE_NUMBER+" INTEGER,"
                + COLUMN_IS_FAVOURITE + " INTEGER,"
                + COLUMN_IS_RETWEET+" INTEGER,"
                +COLUMN_MEDIA+" VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
    public Cursor getHomeTimeLineData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME_HOME_TIME_LINE , null );
        return res;
    }

    public Cursor getProfileData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_PROFILE,null);
        return res;
    }

    public Cursor getAccountData(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_ACCOUNT,null);
        return res;
    }

    public Cursor getTweetsData(String screenName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_TWEET,null);
        return res;
    }

    public Cursor getFavouriteData(String screenName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_FAVOURITE,null);
        return res;
    }

    public User parseProfile(Cursor cursor){
        User user= new User();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            user.setUid(Long.valueOf(cursor.getString(0)));
            user.setScreenName(cursor.getString(1));
            user.setProfileImageUrl(cursor.getString(2));
            if (cursor.getInt(3)==0)
            {
                user.setProfileUseBackgroundImage(false);
            }else {
                user.setProfileUseBackgroundImage(true);
            }
            user.setProfileBackgroundImageUrl(cursor.getString(4));
            user.setName(cursor.getString(5));
            user.setStatusesCount(cursor.getInt(6));
            user.setFavouritesCount(cursor.getInt(7));
            user.setFollowersCount(cursor.getInt(8));
        }
        return user;
    }

    public User parseAccount(Cursor cursor, String screenName){
        User user= new User();
        int count = cursor.getCount();
        int i = 0;
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                i++;
                if(cursor.getString(1)!= null && cursor.getString(1).equals(screenName))
                {
                    user.setUid(Long.valueOf(cursor.getString(0)));
                    user.setScreenName(cursor.getString(1));
                    user.setProfileImageUrl(cursor.getString(2));
                    if (cursor.getInt(3)==0)
                    {
                        user.setProfileUseBackgroundImage(false);
                    }else {
                        user.setProfileUseBackgroundImage(true);
                    }
                    user.setProfileBackgroundImageUrl(cursor.getString(4));
                    user.setName(cursor.getString(5));
                    user.setStatusesCount(cursor.getInt(6));
                    user.setFavouritesCount(cursor.getInt(7));
                    user.setFollowersCount(cursor.getInt(8));
                    if (cursor.getInt(9)==0)
                    {
                        user.setFollowRequestSent(false);
                    }else {
                        user.setFollowRequestSent(true);
                    }
                    return user;
                }
            }while (cursor.moveToNext());
        }
        return null;
    }

    public List<Tweet> parseTweets(Cursor cursor, String screenName){
        List<Tweet> tweets= new ArrayList<>();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                if(cursor.getString(2).equals(screenName))
                {
                    Tweet tweet = new Tweet();
                    tweet.setUid(Long.valueOf(cursor.getString(1)));
                    tweet.setUserScreenName(cursor.getString(2));
                    tweet.setAvatarUrl(cursor.getString(3));
                    tweet.setRelativeTimestamp(cursor.getString(4));
                    tweet.setBody(cursor.getString(5));
                    tweet.setRetweetCount(cursor.getInt(6));
                    tweet.setFavouritesCount(cursor.getInt(7));
                    if (cursor.getInt(8) == 0) {
                        tweet.setFavourited(false);
                    } else {
                        tweet.setFavourited(true);
                    }
                    if (cursor.getInt(9) == 0) {
                        tweet.setRetweeted(false);
                    } else {
                        tweet.setRetweeted(true);
                    }
                    tweet.setMediaUrl(cursor.getString(10));
                    tweets.add(tweet);
                }
            }
            while (cursor.moveToNext());
        }
        return tweets;
    }

    public List<Tweet> parseFavourite(Cursor cursor, String screenName){
        List<Tweet> tweets= new ArrayList<>();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                if (cursor.getString(2).equals(screenName)) {
                    Tweet tweet = new Tweet();
                    tweet.setUid(Long.valueOf(cursor.getString(1)));
                    tweet.setUserScreenName(cursor.getString(2));
                    tweet.setAvatarUrl(cursor.getString(3));
                    tweet.setRelativeTimestamp(cursor.getString(4));
                    tweet.setBody(cursor.getString(5));
                    tweet.setRetweetCount(cursor.getInt(6));
                    tweet.setFavouritesCount(cursor.getInt(7));
                    if (cursor.getInt(8) == 0) {
                        tweet.setFavourited(false);
                    } else {
                        tweet.setFavourited(true);
                    }
                    if (cursor.getInt(9) == 0) {
                        tweet.setRetweeted(false);
                    } else {
                        tweet.setRetweeted(true);
                    }
                    tweet.setMediaUrl(cursor.getString(10));
                    tweets.add(tweet);
                }
            }
            while (cursor.moveToNext());
        }
        return tweets;
    }

    public List<Tweet> parseHomeTimeLine(Cursor cursor){
        List<Tweet> tweets = new ArrayList<>();
        int  len = cursor.getCount();
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                Tweet tweet = new Tweet();
                tweet.setUid(Long.valueOf(cursor.getString(0)));
                tweet.setUserScreenName(cursor.getString(1));
                tweet.setAvatarUrl(cursor.getString(2));
                tweet.setRelativeTimestamp(cursor.getString(3));
                tweet.setBody(cursor.getString(4));
                tweet.setRetweetCount(cursor.getInt(5));
                tweet.setFavouritesCount(cursor.getInt(6));
                if (cursor.getInt(7) == 0) {
                    tweet.setFavourited(false);
                } else {
                    tweet.setFavourited(true);
                }
                if (cursor.getInt(8) == 0) {
                    tweet.setRetweeted(false);
                } else {
                    tweet.setRetweeted(true);
                }
                tweet.setMediaUrl(cursor.getString(9));
                tweet.setUserId(Long.valueOf(cursor.getString(10)));
                tweets.add(tweet);

            } while (cursor.moveToNext());
        }
            return tweets;

    }


    public  void insertProfile(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String id = String.valueOf(user.getUid());
        long _id = user.getUid();
        contentValues.put(COLUMN_PROFILE_ID, String.valueOf(user.getUid()));
        contentValues.put(COLUMN_PROFILE_NAME, user.getScreenName());
        contentValues.put(COLUMN_PROFILE_AVATAR, user.getProfileImageUrl());
        if (user.isProfileUseBackgroundImage())
        {
            contentValues.put(COLUMN_HAVE_COVER,1);
        }else {
            contentValues.put(COLUMN_HAVE_COVER,0);
        }
        contentValues.put(COLUMN_COVERURL, user.getProfileBackgroundImageUrl());
        contentValues.put(COLUMN_PROFILE_EMAIL,user.getName());
        contentValues.put(COLUMN_NUMBER_TWEETS, user.getStatusesCount());
        contentValues.put(COLUMN_FOLLOWING,user.getFavouritesCount());
        contentValues.put(COLUMN_FOLLWERS,user.getFollowersCount());
        db.insert(TABLE_PROFILE,null, contentValues);

    }

    public  void insertAccount(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROFILE_ID, String.valueOf(user.getUid()));
        contentValues.put(COLUMN_PROFILE_NAME, user.getScreenName());
        contentValues.put(COLUMN_PROFILE_AVATAR, user.getProfileImageUrl());
        if (user.isProfileUseBackgroundImage())
        {
            contentValues.put(COLUMN_HAVE_COVER,1);
        }else {
            contentValues.put(COLUMN_HAVE_COVER,0);
        }
        contentValues.put(COLUMN_COVERURL, user.getProfileBackgroundImageUrl());
        contentValues.put(COLUMN_PROFILE_EMAIL,user.getName());
        contentValues.put(COLUMN_NUMBER_TWEETS, user.getStatusesCount());
        contentValues.put(COLUMN_FOLLOWING,user.getFavouritesCount());
        contentValues.put(COLUMN_FOLLWERS,user.getFollowersCount());
        if (user.getFollowRequestSent())
        {
            contentValues.put(COLUMN_ISFOLLOWING,1);
        }else {
            contentValues.put(COLUMN_ISFOLLOWING,0);
        }

        db.insert(TABLE_ACCOUNT,null, contentValues);
    }

    public void insertTweetIntoHomeTimeLine(Tweet tweet){
        tweet.getUser();
        tweet.getMedias();
        tweet.getRelativeTimeAgo(tweet.getCreateAt());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,String.valueOf(tweet.getUid()));
        contentValues.put(COLUMN_AVATAR,tweet.getAvatarUrl());
        contentValues.put(COLUMN_BODY,tweet.getBody());
        contentValues.put(COLUMN_FAVOURITE_NUMBER,tweet.getFavouritesCount());
        if (tweet.isFavourited())
        {
            contentValues.put(COLUMN_IS_FAVOURITE,1);
        }
        else {
            contentValues.put(COLUMN_IS_FAVOURITE,0);
        }
        if (tweet.isRetweeted())
        {
            contentValues.put(COLUMN_IS_RETWEET, 1);
        }
        else {
            contentValues.put(COLUMN_IS_RETWEET, 0);
        }
        if (tweet.getMedias()!=null)
        {
            contentValues.put(COLUMN_MEDIA, tweet.getMedias().get(0).getMediaUrl());
        }
        else contentValues.put(COLUMN_MEDIA,"");
        contentValues.put(COLUMN_NAME_USER,tweet.getUserScreenName());
        contentValues.put(COLUMN_RETWEET_NUMBER,tweet.getRetweetCount());
        contentValues.put(COLUMN_TIME,tweet.getRelativeTimestamp());
        contentValues.put(COLUMN_PROFILE_ID, tweet.getUserId());
        db.insert(TABLE_NAME_HOME_TIME_LINE,null, contentValues);
    }

    public void insertTweets(Tweet tweet, String screenName){
        tweet.getUser();
        tweet.getMedias();
        tweet.getRelativeTimeAgo(tweet.getCreateAt());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROFILE_NAME, screenName);
        contentValues.put(COLUMN_ID,String.valueOf(tweet.getUid()));
        contentValues.put(COLUMN_AVATAR,tweet.getAvatarUrl());
        contentValues.put(COLUMN_BODY,tweet.getBody());
        contentValues.put(COLUMN_FAVOURITE_NUMBER,tweet.getFavouritesCount());
        if (tweet.isFavourited())
        {
            contentValues.put(COLUMN_IS_FAVOURITE,1);
        }
        else {
            contentValues.put(COLUMN_IS_FAVOURITE,0);
        }
        if (tweet.isRetweeted())
        {
            contentValues.put(COLUMN_IS_RETWEET, 1);
        }
        else {
            contentValues.put(COLUMN_IS_RETWEET, 0);
        }
        if (tweet.getMedias()!=null)
        {
            contentValues.put(COLUMN_MEDIA, tweet.getMedias().get(0).getMediaUrl());
        }
        else contentValues.put(COLUMN_MEDIA,"");
        contentValues.put(COLUMN_NAME_USER,tweet.getUserScreenName());
        contentValues.put(COLUMN_RETWEET_NUMBER,tweet.getRetweetCount());
        contentValues.put(COLUMN_TIME,tweet.getRelativeTimestamp());
        db.insert(TABLE_TWEET,null, contentValues);

    }

    public void insertFavourite(Tweet tweet, String screenName){
        tweet.getUser();
        tweet.getMedias();
        tweet.getRelativeTimeAgo(tweet.getCreateAt());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROFILE_NAME, screenName);
        contentValues.put(COLUMN_ID,String.valueOf(tweet.getUid()));
        contentValues.put(COLUMN_AVATAR,tweet.getAvatarUrl());
        contentValues.put(COLUMN_BODY,tweet.getBody());
        contentValues.put(COLUMN_FAVOURITE_NUMBER,tweet.getFavouritesCount());
        if (tweet.isFavourited())
        {
            contentValues.put(COLUMN_IS_FAVOURITE,1);
        }
        else {
            contentValues.put(COLUMN_IS_FAVOURITE,0);
        }
        if (tweet.isRetweeted())
        {
            contentValues.put(COLUMN_IS_RETWEET, 1);
        }
        else {
            contentValues.put(COLUMN_IS_RETWEET, 0);
        }
        if (tweet.getMedias()!=null)
        {
            contentValues.put(COLUMN_MEDIA, tweet.getMedias().get(0).getMediaUrl());
        }
        else contentValues.put(COLUMN_MEDIA,"");
        contentValues.put(COLUMN_NAME_USER,tweet.getUserScreenName());
        contentValues.put(COLUMN_RETWEET_NUMBER,tweet.getRetweetCount());
        contentValues.put(COLUMN_TIME,tweet.getRelativeTimestamp());
        db.insert(TABLE_FAVOURITE,null, contentValues);

    }


    public boolean deleteTweetHomeTimeLine(Tweet tweet){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_HOME_TIME_LINE,COLUMN_ID+ "=" + tweet.getUid(), null)>0;
    }

    public void clearHomeTimeLineTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME_HOME_TIME_LINE;
        db.execSQL(clearDBQuery);

    }

    public void clearProfileTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_PROFILE;
        db.execSQL(clearDBQuery);
    }

    public boolean deleteFavourite(String screenName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVOURITE,COLUMN_PROFILE_NAME+ "=" + screenName, null)>0;

    }

    public boolean deleteTweets(String screenName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TWEET,COLUMN_PROFILE_NAME+ "=" + screenName, null)>0;

    }
}
