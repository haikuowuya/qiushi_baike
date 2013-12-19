package com.roboo.qiushibaike.utils;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.qiushibaike.model.ChuanYiItem;

/**
 * 获取穿衣打扮网站数据的工具类 www.cydb.com === www.chong4.com.cn 电脑版
 * http://www.chong4.com.cn/index.php?mode=1&page=5 手机版
 * http://www.chong4.com.cn/mobile/index.php?page=3
 * 
 * @author bo.li
 * 
 */
public class CYDBUtils
{
	private static final String CHUAN_YI_DA_BAN_MOBILE_URL = "http://www.chong4.com.cn/mobile/";

	public static LinkedList<ChuanYiItem> handleChuanYiItems(String pageNo) throws IOException
	{
		LinkedList<ChuanYiItem> items = null;
		String url = CHUAN_YI_DA_BAN_MOBILE_URL + "index.php?page=" + pageNo;
		Document document = Jsoup.connect(url).timeout(20000).get();
		Elements elements = document.getElementsByClass("m_list");
		if (null != elements && elements.size() > 0)
		{
			items = new LinkedList<ChuanYiItem>();
			Element aTag = null;
			String url2 = null;
			String img = null;
			String md5 = null;
			String title = null;
			String content = null;
			for (Element element : elements)
			{
				aTag = element.getElementsByTag("a").get(0);
				if (null != aTag)
				{
					ChuanYiItem item = new ChuanYiItem();
					url2 = CHUAN_YI_DA_BAN_MOBILE_URL + aTag.attr("href");
					img = CHUAN_YI_DA_BAN_MOBILE_URL + aTag.getElementsByTag("img").get(0).attr("src");
					title = aTag.getElementsByTag("h2").get(0).ownText();
					content = aTag.getElementsByTag("p").get(0).ownText();
					md5 = MD5Utils.generate(url2 + img + title + content);
					item.content = content;
					item.img = img;
					item.title = title;
					item.url = url2;
					item.md5 = md5;
					item.time = System.currentTimeMillis();
					System.out.println("穿衣打扮Item =" +item);
					items.add(item);
				}
			}
		}
		return items;
	}
}
