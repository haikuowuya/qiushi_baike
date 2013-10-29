package com.roboo.qiushibaike.model;

import java.io.Serializable;


public class CommentItem implements Serializable
{
	public String commentAuthorName;
	public String commentAuthorUrl;
	public String commentContent;
	public String commentNote="评论备注";
	public String commentIndex;
	public long time;
	/**是与当前评论对应的糗事数据*/
	public String md5;
	
	public CommentItem()
	{
		super();
	}

	public CommentItem(String commentAuthorName, String commentAuthorUrl, String commentContent, String commentNote, String commentIndex, long time,
			String md5)
	{
		super();
		this.commentAuthorName = commentAuthorName;
		this.commentAuthorUrl = commentAuthorUrl;
		this.commentContent = commentContent;
		this.commentNote = commentNote;
		this.commentIndex = commentIndex;
		this.time = time;
		this.md5 = md5;
	}
	@Override
	public boolean equals(Object o)
	{
		if(null != o && o instanceof CommentItem)
		{
			CommentItem item  = (CommentItem) o;
			return this.commentIndex.equals(item.commentIndex) && this.commentContent.equals(item.commentContent);
		}
		return false;
	}
	@Override
	public String toString()
	{
		 return "评论人 = "+ this.commentAuthorName + " 评论人URL = " + this.commentAuthorUrl
				 +" 评论内容 = " + this.commentContent + " 时间 = " + this.time
				 +" md5 = " + this.md5 + "索引 = " + commentIndex; 
	}
}
