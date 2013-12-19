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
		 
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, HomeFragment.newInstance()).commit();
		
	}
}
