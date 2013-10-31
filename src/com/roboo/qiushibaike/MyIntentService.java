package com.roboo.qiushibaike;

import android.app.IntentService;
import android.content.Intent;

public class MyIntentService extends IntentService
{

	public MyIntentService()
	{
		super("MyIntentService");
		 
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		 try
		{
			Thread.sleep(20000);
			System.out.println("在IntentService中睡眠20秒钟");
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
