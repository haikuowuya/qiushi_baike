package com.roboo.qiushibaike.model;

import java.io.Serializable;

public class CSDNItem implements Serializable
{
	public String title;
	public String content;
	public String authorImgUrl;
	public String url;
	public CSDNItem()
	{
		 
	}
	public CSDNItem(String title, String content, String authorImgUrl, String url)
	{
		this.title = title;
		this.content = content;
		this.authorImgUrl = authorImgUrl;
		this.url = url;
	}
	@Override
	public String toString()
	{
		 return "title = " +title + " url = " + url + " content = " + content + " authorImgUrl = " + authorImgUrl;
	}
	
}
