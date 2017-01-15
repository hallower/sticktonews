package kr.blogspot.charlie0301.stickttonews;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by csk on 2017-01-15.
 */

public class GetNewsID extends AsyncTask<Void, Void, Collection<String>>
{
	static private String LOG_TAG = "GetNewsID";
	private Handler handler;

	GetNewsID(Handler handler)
	{
		super();
		this.handler = handler;
	}

	@Override
	protected Collection<String> doInBackground(Void... voids) {
		String URL = "https://whooing.com/api/bbs/moneynews.json?limit=5";
		Collection<String> newIDs = new ArrayList<>();

		JSONObject json = RestAPIInvoker.invokeRESTAPI(RestAPIInvoker.HTTP_METHOD.GET, URL, "");
		if(null == json){
			Log.e(LOG_TAG, "Error response - null returned");
			return newIDs;
		}

		try
		{
			if(false == json.getString("code").startsWith("2")){
				Log.e(LOG_TAG, "Error response - " + json.getString("message"));
				return newIDs;
			}

			JSONArray result = json.getJSONArray("results");

			for(int i = 0; i < result.length();i++){
				JSONObject newsObj = result.getJSONObject(i);
				if(false == newsObj.optString("bbs_id").isEmpty())
					newIDs.add(newsObj.optString("bbs_id"));
			}
			return newIDs;
		}catch (JSONException e)
		{
			Log.e(LOG_TAG, "[User Info] Error response - null returned");
			return newIDs;
		}
	}

	@Override
	protected void onPostExecute(Collection<String> news) {
		super.onPostExecute(news);

		handler.sendMessage(Message.obtain(handler, MainActivity.CMD_GET_NEWS_ID, 1, 0, news));
	}
}