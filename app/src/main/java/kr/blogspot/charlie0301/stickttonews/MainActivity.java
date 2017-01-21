package kr.blogspot.charlie0301.stickttonews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import kr.blogspot.charlie0301.stickttonews.models.News;
import kr.blogspot.charlie0301.stickttonews.models.GetNewsDetail;
import kr.blogspot.charlie0301.stickttonews.models.GetNewsID;
import kr.blogspot.charlie0301.stickttonews.view.NewsCardAdapter;

public class MainActivity extends AppCompatActivity {

	private static final String LOG_TAG = "StickToNews";
	private static final String whooingURL = "https://whooing.com";

	// TODO : adjust
	public static final int CONTENT_REQUEST_AT_ONCE = 15;

	public static final int CMD_BASE  = 1230;
	public static final int CMD_TOAST_LONG = CMD_BASE + 2;
	public static final int CMD_TOAST_SHORT = CMD_BASE + 4;
	public static final int CMD_GET_NEWS_ID = CMD_BASE + 6;
	public static final int CMD_GET_NEWS = CMD_BASE + 8;

	private MainHandler handler = new MainHandler();
 	private CoordinatorLayout coordinatorLayout;

	private RecyclerView mRecyclerView;
	private NewsCardAdapter mAdapter;
	private LinearLayoutManager mLayoutManager;

	private List<String> newsIDs = new ArrayList<>();
	private List<News> newsContents = new ArrayList<>();
	private HashSet<String> gettingInProgress = new HashSet<>();

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
				sm(CMD_TOAST_LONG, getString(R.string.notice_recommend_news));
			}
		});

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.condinator);

		mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setNestedScrollingEnabled(false);

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mAdapter = new NewsCardAdapter(newsContents);
		mRecyclerView.setAdapter(mAdapter);

		android.support.v4.widget.NestedScrollView scroller = (android.support.v4.widget.NestedScrollView) findViewById(R.id.scrollview);

		if (scroller != null) {

			scroller.setOnScrollChangeListener(new android.support.v4.widget.NestedScrollView.OnScrollChangeListener() {
				@Override
				public void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

					if (scrollY == 0) {
						getNewsID(null);
					}

					if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
						getNewsID(newsIDs.get(newsIDs.size() - 1));
					}
				}
			});
		}

		getNewsID(null);
	}

	void getNewsID(String from) {
		sm(CMD_TOAST_LONG, getString(R.string.notice_getting_more));
		GetNewsID getNewsID = new GetNewsID(handler);
		getNewsID.execute(from);
	}

	void getNewsDetail(String id){
		GetNewsDetail getNewsDetail = new GetNewsDetail(handler);
		getNewsDetail.execute(id);
	}

	private class MainHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {

			int command = msg.what;
			Object obj = msg.obj;

			switch(command) {

				case CMD_TOAST_LONG:
					Snackbar.make(coordinatorLayout, obj.toString(), Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
					break;

				case CMD_TOAST_SHORT:
					Snackbar.make(coordinatorLayout, obj.toString(), Snackbar.LENGTH_SHORT)
							.setAction("Action", null).show();
					break;

				case CMD_GET_NEWS_ID:

					Collection<String> ids = (Collection<String>)obj;

					for(String id : ids)
					{
						if(newsContents.contains(id) ||
								newsIDs.contains(id))
							continue;

						Log.d(LOG_TAG, "New ID = " + id);
						newsIDs.add(id);
						Collections.sort(newsIDs, new Comparator<String>() {
							@Override
							public int compare(String s, String t1) {
								return t1.compareTo(s);
							}
						});

						if(false == gettingInProgress.contains(id)){
							gettingInProgress.add(id);
							getNewsDetail(id);
						}
					}

					break;

				case CMD_GET_NEWS :
					if(obj instanceof News){
						News news = (News)obj;
						Log.d(LOG_TAG, "News : " + news.title);

						gettingInProgress.remove(news.id);
						newsContents.add(news);
						Collections.sort(newsContents, new Comparator<News>() {
							@Override
							public int compare(News s, News t1) {
								return t1.id.compareTo(s.id);
							}
						});
						mAdapter.notifyDataSetChanged();

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
