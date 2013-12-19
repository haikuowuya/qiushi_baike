package com.roboo.qiushibaike.model;


public class ChuanYiItem extends BaseItem 
{
	private static final long serialVersionUID = 1345634636346L;
	public long time;
	public ChuanYiItem(long time, String img, String title, String url, String note, String content, String md5)
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
	}

	@Override
	public boolean equals(Object o)
	{
		if (null != o && o instanceof ChuanYiItem)
		{
			ChuanYiItem item = ((ChuanYiItem) o);
			return md5.equals(item.md5);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "标题 = " + title + " 图片 = " + img + " md5 = " + md5 + " URL = " + url + " 内容 = " + content + " 时间 = " + time;
	}
}
