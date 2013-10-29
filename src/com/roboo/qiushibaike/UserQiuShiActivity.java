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
		setContentView(R.layout.activity_show_img);
		mItem = (QiuShiItem) getIntent().getSerializableExtra("item");
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, UserQiuShiFragment.newInstance(mItem)).commit();
		
	}
 
}
