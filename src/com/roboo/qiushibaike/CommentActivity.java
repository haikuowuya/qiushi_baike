package com.roboo.qiushibaike;

import android.os.Bundle;
import android.view.Menu;

import com.roboo.qiushibaike.fragment.CommentFragment;
import com.roboo.qiushibaike.model.QiuShiItem;

public class CommentActivity extends BaseActivity
{
	private QiuShiItem mItem;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
		setContentView(R.layout.activity_show_img);
		mItem = (QiuShiItem) getIntent().getSerializableExtra("item");
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, CommentFragment.newInstance(mItem)).commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
		
	}

}
