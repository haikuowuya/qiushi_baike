package com.roboo.qiushibaike.dao;

import java.util.LinkedList;

import com.roboo.qiushibaike.model.CommentItem;

public interface ICommentItemDao
{
	public int insert(CommentItem item);
	public LinkedList<CommentItem> getItems(String md5,int pageNo);

}
