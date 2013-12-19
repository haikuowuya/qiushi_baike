package com.roboo.qiushibaike.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.qiushibaike.model.ChuanYiItem;
import com.roboo.qiushibaike.model.KJFMItem;

/**
 * 获取科技锋芒网站数据的工具类 http://www.phonekr.com/
 * 
 * 
 * @author bo.li
 * 
 */
public class KJFMUtils
{
	private static final String KE_JI_FENG_MANG_URL = "http://www.phonekr.com/page/";

	public static LinkedList<KJFMItem> handleKJFMItems(String pageNo) throws IOException
	{
		LinkedList<KJFMItem> items = null;
		String url = KE_JI_FENG_MANG_URL + pageNo+ "/";
		System.out.println("url = " + url);
		Document document = Jsoup.connect(url).timeout(20000).get();
		Element divTag = document.getElementById("xs-main");
		if (null != divTag)
		{
			Elements entryTags = divTag.getElementsByClass("xs-entry");
			if (null != entryTags && entryTags.size() > 0)
			{
				items = new LinkedList<KJFMItem>();
				for (Element e : entryTags)
				{
					KJFMItem item = new KJFMItem();
					Elements aTags = e.getElementsByTag("a");
					if (null != aTags && aTags.size() > 0)
					{
						String url1 = aTags.get(0).attr("href");
					//	System.out.println("url1 = " + url1);
						item.title = url1;
					}
					Elements imgTags = e.getElementsByTag("img");
					if (null != imgTags && imgTags.size() > 0)
					{
						String img = imgTags.get(0).attr("src");
						String title = imgTags.get(0).attr("alt");
						// System.out.println("img = " + img + " title = " +
						// title);
						item.img = img;
						item.title = title;
					}
					Elements pTags = e.getElementsByTag("p");
					if (null != pTags && pTags.size() > 0)
					{
						String content = pTags.get(0).text();
						// System.out.println("content = " + content);
						item.content = content;
					}
					items.add(item);
				}
			}
		}

		return items;
	}
}
