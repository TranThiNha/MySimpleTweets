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
	public static final String REST_CONSUMER_KEY = "SQFDSv2i2gGW1W3Nb8yCjUAiD";
	public static final String REST_CONSUMER_SECRET = "UiRlq6pwIJKnkEMtiPF1IEiOGaRJ9cJv9LRiedeIuLKjLmkTbH";
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
		params.put("page", page);
		params.put("since_id", "1");
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
}
