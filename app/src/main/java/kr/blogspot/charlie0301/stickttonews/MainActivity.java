package kr.blogspot.charlie0301.stickttonews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import kr.blogspot.charlie0301.stickttonews.models.News;

public class MainActivity extends AppCompatActivity {

	private static final String LOG_TAG = "StickToNews";
	private static final String whooingURL = "https://whooing.com";

	private MainHandler handler = new MainHandler();

	public static final int CMD_GET_NEWS_ID = 1111;
	public static final int CMD_GET_NEWS = 2222;

	private ArrayList<String> newsIDs = new ArrayList<>();
	private HashMap<String, News> newsContents = new HashMap<>();

	private int currPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		currPos = 0;
		GetNewsID(null);
	}

	void GetNewsID(String from) {
		GetNewsID getNewsID = new GetNewsID(handler);
		getNewsID.execute();
	}

	void GetNewsDetail(String id){
		GetNewsDetail getNewsDetail = new GetNewsDetail(handler);
		getNewsDetail.execute(id);
	}

	boolean MovePage(boolean prev)
	{
		if(prev) {
			// left
			if(currPos == 0)
				return false;

			currPos -= 1;
		} else {
			if((currPos + 1) == newsIDs.size())
				return false;
			currPos += 1;
		}

		String newsID = newsIDs.get(currPos);
		if(newsContents.containsKey(newsID))
		{
			News news = newsContents.get(newsID);
			TextView tv = (TextView)findViewById(R.id.txt_main);
			tv.setText(news.id + " / " + news.title + " / " + news.description);
		}
		else
		{
			GetNewsDetail(newsID);
		}
		return true;
	}

	private class MainHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {

			int command = msg.what;
			Object obj = msg.obj;

			switch(command) {

				case CMD_GET_NEWS_ID:

					Collection<String> ids = (Collection<String>)obj;

					for(String id : ids)
					{
						newsIDs.add(id);

						Log.d(LOG_TAG, "New ID = " + id);
						GetNewsDetail(id);
					}

					break;

				case CMD_GET_NEWS :
					if(obj instanceof News){
						News news = (News)obj;
						Log.d(LOG_TAG, "News : " + news.title);
						newsContents.put(news.id, news);

						if(0 == newsIDs.get(currPos).compareTo(news.id))
						{
							TextView tv = (TextView)findViewById(R.id.txt_main);
							tv.setText(news.id + " / " + news.title + " / " + news.description);
						}
					}

					break;
			}
			super.handleMessage(msg);
		}
	}


	public void sm(int cmd, Object msg){
		handler.sendMessage(Message.obtain(handler, cmd, 1, 0, msg));
	}

	public void sm(int cmd, int a1, int a2, Object msg){
		handler.sendMessage(Message.obtain(handler, cmd, a1, a2, msg));
	}

	public  void smd(int cmd, Object msg, long ms){
		handler.sendMessageDelayed(Message.obtain(handler, cmd, 1, 0, msg), ms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_go_to_whooing) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(whooingURL));
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
