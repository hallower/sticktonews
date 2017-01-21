package kr.blogspot.charlie0301.stickttonews.view;

import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.blogspot.charlie0301.stickttonews.R;
import kr.blogspot.charlie0301.stickttonews.models.News;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.ViewHolder> {
	private List<News> newsModelData;

	public interface NeedMoreNewsListener
	{
		void OnNeedMoreNews();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView title;
		private TextView desc;
		private TextView author;

		public ViewHolder(View v) {
			super(v);
			title = (TextView) v.findViewById(R.id.card_title);
			desc = (TextView) v.findViewById(R.id.card_desc);
			author = (TextView) v.findViewById(R.id.card_author);

			desc.setMovementMethod(LinkMovementMethod.getInstance());
		}

		public void setNews(News news) {
			title.setText(Html.fromHtml("<h2>" + news.title + "</h2>"));
			desc.setText(Html.fromHtml("<h5>" + news.description + "</h5>"));
			author.setText(news.author);
		}
	}

	public NewsCardAdapter(List<News> news) {
		newsModelData = news;
	}

	@Override
	public NewsCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.card_news_view, parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if(0 > position ||
				position >= newsModelData.size())
			return;

		News news = newsModelData.get(position);
		holder.setNews(news);
	}

	@Override
	public int getItemCount() {
		return newsModelData.size();
	}
}