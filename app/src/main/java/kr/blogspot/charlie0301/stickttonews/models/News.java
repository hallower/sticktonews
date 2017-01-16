package kr.blogspot.charlie0301.stickttonews.models;

import org.json.JSONObject;

/**
 * Created by csk on 2017-01-14.
 */

public class News {
	public String id;
	public String title;
	public String description;
	public String url;
	public String author;

	public String prevID;
	public String nextID;

	public News(JSONObject json)
	{
		/*
{
"code": 200,
"message": "",
"error_parameters": [],
"rest_of_api": 0,
"results": {
"bbs_id": 82941,
"category": "moneynews",
"notice": "n",
"group": "none",
"language": "ko",
"writer": {
"user_id": 2402,
"username": "호호호",
"level": "2",
"image_url": "https://s3-ap-northeast-1.amazonaws.com/whooingprofile/p2402.jpg?t=191"
},
"hits": 6,
"comments": 0,
"recommandation": 0,
"subject": "카드론에서 P2P대출로.. '이유있는 갈아타기'",
"latest": "http://m.fnnews.com/news/201701131713061172#cb posted by Wimple (https://whooing.com",
"contents": "<a href="http://m.fnnews.com/news/201701131713061172#cb" target="_blank">http://m.fnnews.com/news/201701131713061172#cb</a> posted by Wimple (<a href="https://whooing.com/zS2h" class="ajax">https://whooing.com/zS2h</a>)",
"attachment": [],
"thumb_path": "",
"etc": "",
"timestamp": 1484397853,
"shorturl": "https://whooing.com/zL9v",
"prev": {
"bbs_id": 82940,
"group": "none",
"subject": ""가벼움은 기본…충전 않고도 종일 쓴다" LG전자 '그램'",
"hits": 14,
"comments": 0,
"is_attachment": "n",
"thumb_path": "",
"timestamp": 1484389233
},
"next": {
"bbs_id": "",
"group": "",
"subject": "",
"hits": "",
"comments": "",
"timestamp": ""
},
"rows": [],
}
}
		 */
		id = json.optString("bbs_id");
		title = json.optString("subject");
		description = json.optString("contents");
		if(false == description.isEmpty()){
			description = description.replaceAll("\\n", "<BR>");
		}

		try {
			JSONObject writer = json.getJSONObject("writer");
			author = writer.optString("username");
		}
		catch (Exception e)
		{
			author = "";
		}

		try {
			JSONObject prev = json.getJSONObject("prev");
			prevID = prev.optString("bbs_id");
		}
		catch (Exception e)
		{
			prevID = "";
		}

		try {
			JSONObject next = json.getJSONObject("next");
			nextID = next.optString("bbs_id");
		}
		catch (Exception e)
		{
			nextID = "";
		}

		url = "";
		int start = description.indexOf("href=") + 6;
		if(0 < start){
			int end = description.indexOf('"', start);
			if(0 < end){
				url = description.substring(start, end);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<h2>");
		sb.append(title);
		sb.append("</h2>");
		sb.append("<br>");
		sb.append("<h3>");
		sb.append(author);
		sb.append("</h3>");
		sb.append("<br>");
		sb.append(description);
		return sb.toString();
	}
}
