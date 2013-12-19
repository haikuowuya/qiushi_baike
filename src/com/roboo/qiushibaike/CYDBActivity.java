package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.ChuanYiFragment;

public class CYDBActivity extends BaseActivity
{
 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG 
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, ChuanYiFragment.newInstance()).commit();
		
	}
 
}
