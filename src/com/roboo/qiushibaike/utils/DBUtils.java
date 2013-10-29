package com.roboo.qiushibaike.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class DBUtils
{
	public static String getCreateTableSQL(Context context)
	{
		InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream("db.json");
		if (null != inputStream)
		{
			StringBuffer sb = new StringBuffer();
			byte[] buffer = new byte[1024];
			int len = -1;
			try
			{
				while ((len = inputStream.read(buffer)) != -1)
				{
					sb.append(new String(buffer, 0, len, HTTP.UTF_8));
				}
				return sb.toString().trim();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	public static String[] getCreateTableSql( Context context)
	{
		String[] sqls = null;
		String sqlJson = getCreateTableSQL(context);
		if(null != sqlJson)
		{
			JSONObject jsonObject;
			try
			{
				jsonObject = new JSONObject(sqlJson);
				if(null != jsonObject )
				{
					JSONArray jsonArray = jsonObject.optJSONArray("create_table_sql");
					if(null != jsonArray && jsonArray.length() > 0)
					{
						sqls = new String[jsonArray.length()];
						for(int i = 0; i < jsonArray.length();i++)
						{
							JSONObject jsonObject2 = jsonArray.optJSONObject(i);
							if(null != jsonObject2)
							{
								sqls[i] = jsonObject2.optString("table"); 
							}
						}
					}
				}
				
			}
			catch (JSONException e)
			{
			 
				e.printStackTrace();
			}
		}
		return sqls;
	}
}
