package com.roboo.qiushibaike.model;

import java.io.Serializable;

public class BaseItem implements Serializable
{
 
	private static final long serialVersionUID = 15458756L;
	public String img;
	public String url;
	public String title;
	public String content;
	public String note;
	public String md5;
	
	public BaseItem()
	{
	}

	public BaseItem(String img, String url, String title, String content)
	{
		super();
		this.img = img;
		this.url = url;
		this.title = title;
		this.content = content;
	}
	
	

}
