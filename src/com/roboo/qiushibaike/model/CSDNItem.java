package com.roboo.qiushibaike.model;


public class CSDNItem  extends BaseItem
{
	public CSDNItem()
	{
		 
	}
	public CSDNItem(String title, String content, String img, String url)
	{
		this.title = title;
		this.content = content;
		this.img = img;
		this.url = url;
	}
	@Override
	public String toString()
	{
		 return "title = " +title + " url = " + url + " content = " + content + " img = " + img;
	}
	
}
