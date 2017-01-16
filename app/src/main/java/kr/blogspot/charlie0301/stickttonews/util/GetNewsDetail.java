package kr.blogspot.charlie0301.stickttonews.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import kr.blogspot.charlie0301.stickttonews.MainActivity;
import kr.blogspot.charlie0301.stickttonews.models.News;

/**
 * Created by csk on 2017-01-15.
 */

public class GetNewsDetail extends AsyncTask<String, Void, News>
{
	static private String LOG_TAG = "GetNewsID";

	private Handler handler;
	public GetNewsDetail(Handler handler)
	{
		this.handler = handler;
	}

	@Override
	protected News doInBackground(String... strings) {
		String URL = "https://whooing.com/api/bbs/moneynews/"+ strings[0] + ".json";

		JSONObject json = RestAPIInvoker.invokeRESTAPI(RestAPIInvoker.HTTP_METHOD.GET, URL, "");
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
			return new News(result);
		}catch (JSONException e)
		{
			Log.e(LOG_TAG, "[User Info] Error response - null returned");
			return null;
		}
	}

	@Override
	protected void onPostExecute(News news) {
		super.onPostExecute(news);

		handler.sendMessage(Message.obtain(handler, MainActivity.CMD_GET_NEWS, 1, 0, news));
	}
}