package com.roboo.qiushibaike;

import android.os.Bundle;
import android.view.Menu;

import com.roboo.qiushibaike.fragment.ShowImgFragment;

public class ShowImgActivity extends BaseActivity
{
	private String mSRC ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
		setContentView(R.layout.activity_show_img);
		mSRC = getIntent().getStringExtra("src");
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, ShowImgFragment.newInstance(mSRC)).commit();
		
	}
}
