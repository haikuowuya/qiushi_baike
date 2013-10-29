package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.WebViewFragment;

public class WebViewActivity extends BaseActivity
{
	private String mURL ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
		setContentView(R.layout.activity_show_img);
		mURL = getIntent().getStringExtra("url");
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, WebViewFragment.newInstance(mURL)).commit();
		
	}	 

}
