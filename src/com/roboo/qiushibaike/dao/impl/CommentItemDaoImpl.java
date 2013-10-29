package com.roboo.qiushibaike.dao.impl;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roboo.qiushibaike.QiuShiApplication;
import com.roboo.qiushibaike.dao.ICommentItemDao;
import com.roboo.qiushibaike.databases.DBHelper;
import com.roboo.qiushibaike.model.CommentItem;
import com.roboo.qiushibaike.model.QiuShiItem;

public class CommentItemDaoImpl implements ICommentItemDao
{
	private DBHelper helper;

	public CommentItemDaoImpl(DBHelper helper)
	{
		this.helper = helper;
	}

	@Override
	public int insert(CommentItem item)
	{

		int count = 0;
		if (item != null)
		{
			SQLiteDatabase db = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("comment_author_name", item.commentAuthorName);
			values.put("comment_author_url", item.commentAuthorUrl);
			values.put("comment_content", item.commentContent);
			values.put("comment_note", item.commentNote);
			values.put("comment_index", item.commentIndex);
			values.put("time", item.time);
			values.put("md5", item.md5);
			if (db.insert(QiuShiApplication.TABLE_COMMENT, null, values) > 0)
			{
				count++;
			}
			db.close();
		}
		else
		{
			System.out.println("数据库中已经存在 【 " + item + " 】");
		}

		return count;
	}

	@Override
	public LinkedList<CommentItem> getItems(String md5, int pageNo)
	{
		LinkedList<CommentItem> items = null;
		SQLiteDatabase db = this.helper.getReadableDatabase();
		String[] columns = new String[] { "comment_author_name", "comment_author_url", "comment_content", "comment_index", "comment_note", "time"  };
		 
		Cursor cursor = db.query(QiuShiApplication.TABLE_COMMENT, columns, " md5 = ?", new String[] { md5 }, null, null, "time DESC",(pageNo-1)*QiuShiApplication.PAGE_SIZE+","+pageNo*QiuShiApplication.PAGE_SIZE);
	 
		if (cursor != null && cursor.getCount() > 0)
		{
			items = new LinkedList<CommentItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				CommentItem item = new CommentItem();
				item.commentAuthorName = cursor.getString(0);
				item.commentAuthorUrl = cursor.getString(1);
				item.commentContent = cursor.getString(2);
				item.commentIndex = cursor.getString(3);
				item.commentNote = cursor.getString(4);
				item.time = cursor.getLong(5);
				item.md5 = md5;
				items.add(item);			 
			}
			cursor.close();
		}
		db.close();
		return items;
	}

}
