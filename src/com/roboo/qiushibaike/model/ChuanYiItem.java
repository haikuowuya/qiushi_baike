package com.roboo.qiushibaike.model;

import java.io.Serializable;

public class ChuanYiItem implements Serializable
{
	public String img;
	public String title;
	public String url;
	public String note;
	public String content;
	public String md5;
	public long time;
	public ChuanYiItem(long time,String img, String title, String url, String note, String content, String md5)
	{
		super();
		this.img = img;
		this.title = title;
		this.time = time;
		this.url = url;
		this.note = note;
		this.content = content;
		this.md5 = md5;
	}
	public ChuanYiItem()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(null != o && o instanceof ChuanYiItem)
		{
			ChuanYiItem item = ((ChuanYiItem)o);
			return md5.equals(item.md5);
		}
		return false;
	}
	@Override
	public String toString()
	{
		 return "标题 = " + title + " 图片 = " + img + " md5 = " + md5 + " URL = " + url
				 +" 内容 = "+ content + " 时间 = "+ time;
	}
}
