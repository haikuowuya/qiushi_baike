package com.roboo.qiushibaike;

import android.os.Bundle;

import com.roboo.qiushibaike.fragment.UserQiuShiFragment;
import com.roboo.qiushibaike.model.QiuShiItem;

public class UserQiuShiActivity extends BaseActivity
{
	private QiuShiItem mItem;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
 
		mItem = (QiuShiItem) getIntent().getSerializableExtra("item");
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, UserQiuShiFragment.newInstance(mItem)).commit();
		
	}
 
}
