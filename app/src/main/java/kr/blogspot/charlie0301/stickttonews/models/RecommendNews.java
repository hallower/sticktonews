package kr.blogspot.charlie0301.stickttonews.models;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import kr.blogspot.charlie0301.stickttonews.MainActivity;
import kr.blogspot.charlie0301.stickttonews.models.News;
import kr.blogspot.charlie0301.stickttonews.util.RestAPIInvoker;


public class RecommendNews extends AsyncTask<String, Void, Void>
{
	static private String LOG_TAG = "RecommendNews";

	public RecommendNews()
	{
	}

	@Override
	protected Void doInBackground(String... strings) {
		if(null == strings[0])
			return null;

		// TODO : login
		String URL = "https://whooing.com/api/bbs/recommandation.json?bbs_id="+ strings[0];

		JSONObject json = RestAPIInvoker.invokeRESTAPI(RestAPIInvoker.HTTP_METHOD.PUT, URL, "");
		if(null == json){
			Log.e(LOG_TAG, "Error response - null returned");
			return null;
		}

		try
		{
			if(false == json.getString("code").startsWith("2")){
				Log.e(LOG_TAG, "Error response - " + json.getString("message"));
				return null;
			}

			JSONObject result = json.getJSONObject("results");
		}catch (JSONException e)
		{
			Log.e(LOG_TAG, "[User Info] Error response - null returned");
		}
		return null;
	}
}