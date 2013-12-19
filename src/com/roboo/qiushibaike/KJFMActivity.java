package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.ChuanYiFragment;
import com.roboo.qiushibaike.fragment.KJFMFragment;

public class KJFMActivity extends BaseActivity
{
 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG 
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, KJFMFragment.newInstance()).commit();
		
	}
 
}
