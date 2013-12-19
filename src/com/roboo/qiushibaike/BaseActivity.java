package com.roboo.qiushibaike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.roboo.qiushibaike.utils.CSDNUtils;

public class BaseActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		test();
	}

	private void test()
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
	

}
