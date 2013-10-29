package com.roboo.qiushibaike.dao.impl;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;

import com.roboo.qiushibaike.QiuShiApplication;
import com.roboo.qiushibaike.dao.IQiuShiItemDao;
import com.roboo.qiushibaike.databases.DBHelper;
import com.roboo.qiushibaike.model.QiuShiItem;

public class QiuShiItemDaoImpl implements IQiuShiItemDao
{
	private DBHelper helper;

	public QiuShiItemDaoImpl(DBHelper helper)
	{
		this.helper = helper;
	}

	@Override
	public int insert(QiuShiItem item)
	{
		int count = 0;
		if (item != null)
		{
			if (!isInserted(item.md5))
			{
				SQLiteDatabase db = helper.getReadableDatabase();
				ContentValues values = new ContentValues();
				values.put("author_name", item.authorName);
				values.put("author_img", item.authorImg);
				values.put("img", item.img);
				values.put("author_url", item.authorUrl);
				values.put("content", item.content);
				values.put("comment_count", item.commentCount);
				values.put("agree_count", item.agreeCount);
				values.put("dis_agree_count", item.disAgreeCount);
				values.put("note", item.note);
				values.put("md5", item.md5);
				values.put("type", item.type);
				values.put("comment_url", item.commentUrl);
				values.put("time", item.time);
				if (db.insert(QiuShiApplication.TABLE_QIU_SHI, null, values) > 0)
				{
					count++;
				}
				db.close();
			}
			else
			{
				System.out.println("数据库中已经存在 【 " + item + " 】");
			}
		}
		return count;
	}

	@Override
	public boolean isInserted(String md5)
	{
		boolean flag = false;
		SQLiteDatabase db = this.helper.getReadableDatabase();
		Cursor cursor = db.query(QiuShiApplication.TABLE_QIU_SHI, null, " md5 = ?", new String[] { md5 }, null, null, null);
		if (null != cursor && cursor.getCount() > 0)
		{
			flag = true;
		}
		db.close();
		cursor.close();
		return flag;
	}

	@Override
	public LinkedList<QiuShiItem> getItems(String type)
	{
		LinkedList<QiuShiItem> items = null;
		SQLiteDatabase db = this.helper.getReadableDatabase();
		String[] columns = new String[] { "author_name", "author_img", "img", "content", "comment_count", "agree_count", "dis_agree_count", "md5",
				"is_agreed", "is_dis_agreed", "time","comment_url","author_url" };
		Cursor cursor = db.query(QiuShiApplication.TABLE_QIU_SHI, columns, " type = ?", new String[] { type }, null, null, "time DESC");
		if (cursor != null && cursor.getCount() > 0)
		{
			items = new LinkedList<QiuShiItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				QiuShiItem item = new QiuShiItem();
				item.authorName = cursor.getString(0);
				item.authorImg = cursor.getString(1);
				item.img = cursor.getString(2);
				item.content = cursor.getString(3);
				item.commentCount = cursor.getString(4);
				item.agreeCount = cursor.getString(5);
				item.disAgreeCount = cursor.getString(6);
				item.md5 = cursor.getString(7);
				item.isAgreed = cursor.getString(8);
				item.isDisAgreed = cursor.getString(9);
				item.time = cursor.getLong(10);
				item.commentUrl = cursor.getString(11);
				item.authorUrl = cursor.getString(12);
				items.add(item);
			}
			cursor.close();
		}
		db.close();
		return items;
	}

	@Override
	public LinkedList<QiuShiItem> getItems(String type, int pageNo)
	{
		LinkedList<QiuShiItem> items = null;
		SQLiteDatabase db = this.helper.getReadableDatabase();
		String[] columns = new String[] { "author_name", "author_img", "img", "content", "comment_count", "agree_count", "dis_agree_count", "md5",
				"is_agreed", "is_dis_agreed", "time","comment_url","author_url" };	 
		Cursor cursor = db.query(QiuShiApplication.TABLE_QIU_SHI, columns, " type = ?", new String[] { type }, null, null, "time DESC",(pageNo-1)*QiuShiApplication.PAGE_SIZE+","+pageNo*QiuShiApplication.PAGE_SIZE);
	 
		if (cursor != null && cursor.getCount() > 0)
		{
			items = new LinkedList<QiuShiItem>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				QiuShiItem item = new QiuShiItem();
				item.authorName = cursor.getString(0);
				item.authorImg = cursor.getString(1);
				item.img = cursor.getString(2);
				item.content = cursor.getString(3);
				item.commentCount = cursor.getString(4);
				item.agreeCount = cursor.getString(5);
				item.disAgreeCount = cursor.getString(6);
				item.md5 = cursor.getString(7);
				item.isAgreed = cursor.getString(8);
				item.isDisAgreed = cursor.getString(9);
				item.time = cursor.getLong(10);
				items.add(item);
				item.commentUrl = cursor.getString(11);
				item.authorUrl = cursor.getString(12);
			}
			cursor.close();
		}
		db.close();
		return items;
	}

	@Override
	public boolean removePreviousItems(String type)
	{
		boolean flag = false;
		SQLiteDatabase db = this.helper.getReadableDatabase();
		if(db.delete(QiuShiApplication.TABLE_QIU_SHI, "type =?", new String[]{type}) > 0)
		{
			flag = true;
		}
		db.close();
		return flag;
	}

}
