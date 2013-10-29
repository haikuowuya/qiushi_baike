package com.roboo.qiushibaike.model;

import java.io.Serializable;

public class QiuShiItem implements Serializable
{
	public String md5;
	public String authorName ="无名氏";
	public String authorImg;
	public String content;
	public String authorUrl;
	public String img;
	public String commentCount;
	public String agreeCount ;
	public String disAgreeCount;
	public String type;
	public String isAgreed = "0";
	public String isDisAgreed = "0";
	public String commentUrl ;
	public long time =  System.currentTimeMillis();
	
	public String note;
	public QiuShiItem()
	{
		super();
	}
	public QiuShiItem(String authorName, String authorImg, String content, String img, String commentCount, String agreeCount, String disAgreeCount,
			 String note,String md5,String type,long time,String commentUrl,String authorUrl)
	{
		super();
		this.commentUrl = commentUrl;
		this.authorName = authorName;
		this.authorImg = authorImg;
		this.content = content;
		this.type = type;
		this.img = img;
		this.commentCount = commentCount;
		this.agreeCount = agreeCount;
		this.authorUrl = authorUrl;
		this.disAgreeCount = disAgreeCount;
		this.note = note;
		this.time  = time;
		this.md5 = md5;
	}
	 @Override
	public boolean equals(Object obj)
	{
		if(null != obj && obj instanceof QiuShiItem)
		{
			QiuShiItem item = (QiuShiItem) obj;
			return content.equals(item.content);
		}
		return false;
	}
	 @Override
	public String toString()
	{
		 return "作者 = "+ authorName + " 标题 = " + content + " 顶 = " + agreeCount + " 踩 = " + disAgreeCount + " 评论 = " + commentCount
				 +" 图片 = "+ img +" 头像 = "+ authorImg +" 类型 = " + type +" md = " +md5 
				 +" 时间 = "+ time + " 评论URL = " + commentUrl+" 作者URL = "+authorUrl;
		 
	}	 
}
