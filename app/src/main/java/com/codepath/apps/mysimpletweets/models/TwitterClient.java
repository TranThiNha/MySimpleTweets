package com.codepath.apps.mysimpletweets.models;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "1eVRXqb04tOpwpDatVgqR3FwD";
	public static final String REST_CONSUMER_SECRET = "bi3adyEJdF57Bi39VnzSReEDEHC8DV8BNtApknnIyFfebJpZfb";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}
	//COMPOSE TWEET

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	public void retweetStatus(long statusId, AsyncHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("statuses/retweet/" + statusId + ".json");
		RequestParams params = new RequestParams();
		params.put("id",statusId);
		getClient().post(apiUrl,params,handler);
	}

	public void unRetweetStatus(long statusId, AsyncHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("statuses/unretweet/" + statusId + ".json");
		RequestParams params = new RequestParams();
		params.put("id",statusId);
		getClient().post(apiUrl,params,handler);
	}
	public void getReTweet(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/retweet/" + String.valueOf(id)+".json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}

	public void favouriteStatus(long statusId, AsyncHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", statusId);
		getClient().post(apiUrl, params, handler);
	}

	public void unFavouriteStatus(long statusId, AsyncHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", statusId);
		getClient().post(apiUrl, params, handler);
	}

	public void getAccount(long id,String username, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("users/show.json?screen_name="+username);
		RequestParams params = new RequestParams();
		params.put("id",id);
		getClient().get(apiUrl,params,handler);
	}


	public void updateReply(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("in_reply_to_status_id",id);
		getClient().post(apiUrl,params,handler);
	}

	public void getProfile (AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		getClient().get(apiUrl,params,handler);
	}

	public void getChatList(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("direct_messages.json?count=1");
		RequestParams params = new RequestParams();
		getClient().get(apiUrl,params,handler);
	}

	public void getFollwerList(String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		//params.put("skip_status",true);
		//params.put("cursor",-1);
		//params.put("include_user_entities",false);
		params.put("screen_name", screenName);
		getClient().get(apiUrl,params,handler);
	}

	public void createFollow(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("friendships/create.json?user_id="+id+"&follow=true");
		RequestParams params = new RequestParams();
		getClient().get(apiUrl,params,handler);
	}

	public void changeProfileImage(String url, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/update_profile_image.json?image="+url);
		RequestParams params = new RequestParams();
		getClient().get(apiUrl,params,handler);
	}

	public void getFavouriteList(String name, int page, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("favorites/list.json");
		RequestParams params = new RequestParams();
		params.put("page",String.valueOf(page));
		params.put("screen_name",name);
		getClient().get(apiUrl, params, handler);
	}


	public void getUserTweet(String screenName, int count, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/user_timeline.json?screen_name="+screenName+"&count="+count);
		RequestParams params = new RequestParams();
		getClient().get(apiUrl,params,handler);
	}

}
