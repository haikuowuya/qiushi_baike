package com.roboo.qiushibaike.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.qiushibaike.model.CSDNItem;

public class CSDNUtils
{
	private static final String CSDN_ANDROID_TAG_BLOG_URL = "http://www.csdn.net/tag/android/blog-";
	private static final String CSDN_ANDROID_TAG_BASE_URL = "http://blog.csdn.net/tag/details.html?tag=Android&page=";
	private static final String AUTHOR_IMG_URL = "http://avatar.csdn.net/3/5/D/1_u011382076.jpg";

	public static LinkedList<CSDNItem> handleCSDNData(String pageNo) throws IOException, JSONException
	{
		LinkedList<CSDNItem> items = null;
		String url = CSDN_ANDROID_TAG_BASE_URL + pageNo;

		Document document = Jsoup.connect(url).timeout(20000).get();

		Elements elements = document.getElementsByTag("script");
		if (null != elements && elements.size() > 0)
		{
			for (Element e : elements)
			{
				if (e.toString().contains("var data") && e.toString().contains("var th_url"))
				{
					String data = e.toString().trim();
					int start = data.indexOf("var data");
					int end = data.indexOf("var th_url");
					// System.out.println("start = " + start + " end = " + end);
					String jsonString = data.substring(start + 11, end - 1);
					// System.out.println("jsonString = "+jsonString);
					JSONObject jsonObject = new JSONObject(jsonString);
					if (null != jsonObject)
					{
						JSONArray jsonArray = jsonObject.optJSONArray("result");
						if (null != jsonArray && jsonArray.length() > 0)
						{
							items = new LinkedList<CSDNItem>();
							for (int i = 0; i < jsonArray.length(); i++)
							{
								JSONObject tmpJsonObject = jsonArray.getJSONObject(i);
								if (null != tmpJsonObject)
								{
									CSDNItem item = new CSDNItem();
									item.title = tmpJsonObject.optString("title", null);
									item.content = tmpJsonObject.optString("description", null);
									item.url = tmpJsonObject.optString("url", null);
									item.img = AUTHOR_IMG_URL;
									System.out.println("item = 【" + item + " 】");
									items.add(item);
								}
							}
						}
					}
				}
			}
		}
		return items;
	}

	public static LinkedList<CSDNItem> handleCSDNBlogData(String pageNo) throws IOException, JSONException
	{
		LinkedList<CSDNItem> items = null;
		String url = CSDN_ANDROID_TAG_BLOG_URL + pageNo;

		Document document = Jsoup.connect(url).timeout(20000).get();

		Elements elements = document.getElementsByTag("li");
		if (null != elements && elements.size() > 0)
		{
			items = new LinkedList<CSDNItem>();
			for (Element e : elements)
			{
				CSDNItem item = new CSDNItem();
				Elements titleElements = e.getElementsByClass("tit_list");
				if (null != titleElements && titleElements.size() > 0)
				{
					item.title = titleElements.get(0).text();
				}
				Elements contentElements = e.getElementsByClass("tag_summary");
				if (null != contentElements && contentElements.size() > 0)
				{
					item.content = contentElements.get(0).text();
					// System.out.println("content = "+item.content);
				}
				Elements aElements = e.getElementsByClass("user_p");
				if (null != aElements && aElements.size() > 0)
				{
					Element aTag = aElements.get(0);
					item.url = aTag.attr("href");
//					 System.out.println("url = "+item.url);
					Elements imgElements = aTag.getElementsByTag("img");
					if (imgElements != null && imgElements.size() > 0)
					{
						String tmpImg = imgElements.get(0).attr("src");
						tmpImg  = tmpImg.replaceFirst("/4_", "/1_");
						item.img = tmpImg;
//						System.out.println("imgUrl = " + item.authorImgUrl  );
					}
				}
				items.add(item);
			}
		}
		return items;
	}
}
