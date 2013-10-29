package com.roboo.qiushibaike.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.roboo.qiushibaike.QiuShiApplication;
import com.roboo.qiushibaike.utils.DBUtils;

public class DBHelper extends SQLiteOpenHelper
{
	private Context context;
	public DBHelper(Context context)
	{
		super(context, QiuShiApplication.DB_NAME, null, QiuShiApplication.DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String[] sqls = DBUtils.getCreateTableSql(context);
		if(null != sqls)
		{
			for(String sql : sqls)
			{
				db.execSQL(sql);
			}
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		
	}

}
