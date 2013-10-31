package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.HomeFragment;

public class HomeActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
		setContentView(R.layout.activity_show_img);
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, HomeFragment.newInstance()).commit();
		
	}
}
