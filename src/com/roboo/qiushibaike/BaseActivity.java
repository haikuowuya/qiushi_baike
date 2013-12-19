package com.roboo.qiushibaike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.roboo.qiushibaike.utils.CSDNUtils;
import com.roboo.qiushibaike.utils.KJFMUtils;

public class BaseActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		testCSDN();
//		testKJFM();
	}

	private void testCSDN()
	{
		 new Thread(){
			 @Override
			public void run()
			{
				 try
				{
					CSDNUtils.handleCSDNBlogData("1");
				}
				catch (Exception e)
				{
				 
					e.printStackTrace();
				}
			}
		 }.start();
	}
	
	private void testKJFM()
	{
		new Thread(){
			@Override
			public void run()
			{
				try
				{
					KJFMUtils.handleKJFMItems("1");
				}
				catch (Exception e)
				{
					
					e.printStackTrace();
				}
			}
		}.start();
	}
	

}
