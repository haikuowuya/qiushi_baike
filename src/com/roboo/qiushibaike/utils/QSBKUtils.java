package com.roboo.qiushibaike.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.roboo.qiushibaike.model.CommentItem;
import com.roboo.qiushibaike.model.QiuShiItem;
/***
 * 获取糗事百科网站中数据的工具类
 * @author bo.li
 *
 */
public class QSBKUtils
{
 
	private static final String WAP3_QIU_SHI_BAI_KE_COM = "http://wap3.qiushibaike.com";
	/** 糗事百科热门笑话对应的url  8小时最糗*/
	private static final String H_R_QIU_SHI_URL = "http://wap3.qiushibaike.com/8hr/page/2";
	/** 最新糗事*/
	private static final String LASTEST_QIU_SHI_URL="http://wap3.qiushibaike.com/late/page/2";
	/** 今日糗事*/
	private static final String TODAY_QIU_SHI_URL="http://wap3.qiushibaike.com/hot/page/2";
	/** 真相糗事*/
	private static final String TRUE_QIU_SHI_URL="http://wap3.qiushibaike.com/imgrank/page/2";
	private static final String COMMENT_URL = "http://wap3.qiushibaike.com/article/47466997";

	public static LinkedList<QiuShiItem> handleQiuSHiData(String type, String pageNo) throws SocketTimeoutException,IOException
	{
		String url = WAP3_QIU_SHI_BAI_KE_COM +"/"+type+"/page/" +pageNo;
		LinkedList<QiuShiItem> items = null;
		Document document = Jsoup.connect(url).timeout(30000).get();
		Elements elements = document.getElementsByClass("qiushi");
		if (null != elements && elements.size() > 0)
		{
			items = new LinkedList<QiuShiItem>();
			for (Element element : elements)
			{
				QiuShiItem item = new QiuShiItem();
				String content = element.ownText();
				String authorName = "无名氏";
				String img = null;
				String authorImg = null;
				String agreeCount = "0";
				String disAgreeCount = "0";
				String commentCount = "0";
				String commentUrl = null;
				String authorUrl = null;
				Elements tmp = element.getElementsByClass("user");
				if (tmp != null && tmp.size() > 0)
				{
					Element pUserTag = tmp.get(0);
					authorName = pUserTag.text();
					authorUrl = WAP3_QIU_SHI_BAI_KE_COM + pUserTag.getElementsByTag("a").get(0).attr("href");
					tmp = pUserTag.getElementsByTag("img");
					if (null != tmp && tmp.size() > 0)
					{
						authorImg = tmp.get(0).attr("src");
					}
				}
				tmp = element.getElementsByAttributeValueStarting("src", "http://pic.qiushibaike.com/system/pictures");
				if (tmp != null && tmp.size() > 0)
				{
					Element aImgTag = tmp.get(0);
					tmp = aImgTag.getElementsByTag("img");
					if (null != tmp && tmp.size() > 0)
					{
						img = tmp.get(0).attr("src");

					}
				}
				tmp = element.getElementsByClass("vote");
				if (tmp != null && tmp.size() > 0)
				{
					Element aImgTag = tmp.get(0);
					tmp = aImgTag.getElementsByTag("a");
					if (tmp != null && tmp.size() > 2)
					{
						agreeCount = tmp.get(0).ownText();
						disAgreeCount = tmp.get(1).ownText();
						commentCount = tmp.get(2).getElementsByTag("strong").get(0).ownText()+" 条评论";
						commentUrl = WAP3_QIU_SHI_BAI_KE_COM + tmp.get(2).attr("href");
					}
				}
				String md5 = MD5Utils.generate(content + authorName + authorImg);
				item.agreeCount = agreeCount;
				item.authorImg = authorImg;
				item.authorName = authorName;
				item.commentCount = commentCount;
				item.disAgreeCount = disAgreeCount;
				item.content = content;
				item.img = img;
				item.md5 = md5;
				item.type = type;
				item.authorUrl = authorUrl;
				item.commentUrl = commentUrl;
				items.add(item);
			}
		}
		return items;
	}

	public static LinkedList<QiuShiItem> handleUserQiuSHiData(QiuShiItem qiuShiItem)
	{
		LinkedList<QiuShiItem> items = null;
		String url = qiuShiItem.authorUrl;
		try
		{
			Document document = Jsoup.connect(url).timeout(20000).get();
			Elements elements = document.getElementsByAttributeValueContaining("id", "article");
			Elements elements2 = document.getElementsByClass("vote");
			if (null != elements && elements.size() > 0)
			{
				System.out.println("element = " + elements.toString());
				items = new LinkedList<QiuShiItem>();
				for (int i = 0; i < elements.size(); i++)
				{
					QiuShiItem item = new QiuShiItem();
					Element element = elements.get(i);
					String content = element.ownText();
					String img = null;
					String agreeCount = "0";
					String disAgreeCount = "0";
					String commentCount = "0";
					String commentUrl = null;
					String authorImg = null;
					if(element.getElementsByClass("avatar")!= null &&element.getElementsByClass("avatar").size() > 0)
					{
						authorImg = element.getElementsByClass("avatar").get(0).attr("src");
					}
					Elements imgTag = elements2.get(i).getElementsByTag("img");					
					if (null != imgTag && imgTag.size() > 1)
					{
						agreeCount = imgTag.get(0).nextSibling().toString().split("&nbsp;")[0];
						disAgreeCount = imgTag.get(1).nextSibling().toString().split("&nbsp;")[0];
					}
					commentCount = elements2.get(i).getElementsByTag("a").get(0).getElementsByTag("strong").get(0).ownText()
							+" 条评论";
					commentUrl = WAP3_QIU_SHI_BAI_KE_COM + elements2.get(i).getElementsByTag("a").get(0).attr("href");
					String md5 = MD5Utils.generate(content + qiuShiItem.authorName + qiuShiItem.authorImg);
					item.agreeCount = agreeCount;
					item.authorImg = authorImg;
					item.authorName = qiuShiItem.authorName;
					item.commentCount = commentCount;
					item.disAgreeCount = disAgreeCount;
					item.content = content;
					item.img = img;
					item.md5 = md5;
					item.commentUrl = commentUrl;
					item.authorUrl = qiuShiItem.authorUrl;
					items.add(item);
					System.out.println("item = " + item);

				}
			}
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
		return items;
	}

	public static LinkedList<CommentItem> handleCommentData(QiuShiItem qiuShiItem)
	{

		LinkedList<CommentItem> items = null;

		if (null != qiuShiItem)
		{
			String url = qiuShiItem.commentUrl;
			System.out.println("评论URL = "+ url);
			try
			{
				Document document = Jsoup.connect(url).timeout(20000).get();
				Elements tmp = document.getElementsByClass("comments-list");
				if (null != tmp && tmp.size() > 0)
				{
					Elements elements = tmp.get(0).getElementsByClass("qiushi");
					if (null != elements && elements.size() > 0)
					{
						items = new LinkedList<CommentItem>();
						for (Element element : elements)
						{
							CommentItem item = new CommentItem();
							String index = null;
							String commentAuthorName = null;
							String commentContent = null;
							String commentAuthorUrl = null;
							index = element.ownText();
							Elements aTag = element.getElementsByTag("a");
							if (null != aTag && aTag.size() > 0)
							{
								commentAuthorName = aTag.get(0).ownText();
								commentAuthorUrl = "http://wap3.qiushibaike.com" + aTag.get(0).attr("href");

							}
							Elements spanTag = element.getElementsByTag("span");
							if (null != spanTag && spanTag.size() > 0)
							{
								commentContent = spanTag.get(0).ownText();
							}
							item.md5 = qiuShiItem.md5;
							item.commentIndex = index;
							item.commentAuthorName = commentAuthorName;
							item.commentAuthorUrl = commentAuthorUrl;
							item.commentContent = commentContent;
							item.time = System.currentTimeMillis();
							items.add(item);
							System.out.println("item = " + item);
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return items;
	}
}
