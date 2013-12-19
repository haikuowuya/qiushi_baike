package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.WebViewFragment;

public class WebViewActivity extends BaseActivity
{
 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Object object =  getIntent().getSerializableExtra("item");
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, WebViewFragment.newInstance(object)).commit();
		
	}	 

}
