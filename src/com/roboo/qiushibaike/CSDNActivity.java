package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.CSDNFragment;

public class CSDNActivity extends BaseActivity
{
 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG 
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, CSDNFragment.newInstance()).commit();
		
	}
 
}
