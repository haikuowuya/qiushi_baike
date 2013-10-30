package com.roboo.qiushibaike;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.roboo.qiushibaike.fragment.ChuanYiFragment;
import com.roboo.qiushibaike.fragment.MainFragment;

public class MainActivity extends BaseActivity implements OnClickListener
{
	private String mType = "late";// late 代表最新
	private Button mBtnToday;
	private Button mBtnTrue;
	private Button mBtnLatest;
	private int mBtnId;
	// 投稿 先用8小时最糗来代替
	private Button mBtnContribute;
	// 穿衣打扮
	private Button mBtnCYDB;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// TODO setContentView TAG
		setContentView(R.layout.activity_main);
		getSupportFragmentManager().beginTransaction().add(R.id.frame_container, MainFragment.newInstance(mType)).commit();
		initView();
		setListener();
	}

	private void setListener()
	{
		this.mBtnContribute.setOnClickListener(this);
		this.mBtnLatest.setOnClickListener(this);
		this.mBtnTrue.setOnClickListener(this);
		this.mBtnToday.setOnClickListener(this);
		this.mBtnCYDB.setOnClickListener(this);
	}

	private void initView()
	{
		this.mBtnContribute = (Button) findViewById(R.id.btn_contribute);
		this.mBtnLatest = (Button) findViewById(R.id.btn_latest);
		this.mBtnToday = (Button) findViewById(R.id.btn_today);
		this.mBtnTrue = (Button) findViewById(R.id.btn_true);
		this.mBtnCYDB = (Button) findViewById(R.id.btn_cydb);
		this.mBtnId = this.mBtnLatest.getId();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;

	}

	private void defaultBtnTextColor()
	{
		this.mBtnContribute.setTextColor(Color.parseColor("#FFFFFF"));
		this.mBtnLatest.setTextColor(Color.parseColor("#FFFFFF"));
		this.mBtnToday.setTextColor(Color.parseColor("#FFFFFF"));
		this.mBtnTrue.setTextColor(Color.parseColor("#FFFFFF"));
		this.mBtnCYDB.setTextColor(Color.parseColor("#FFFFFF"));
	}

	@Override
	public void onClick(View v)
	{
		if (mBtnId != v.getId())
		{
			defaultBtnTextColor();
			((Button) v).setTextColor(Color.parseColor("#FFFF66"));
			mBtnId = v.getId();
			switch (v.getId())
			{
			case R.id.btn_latest:
				mType = "late";
				break;
			case R.id.btn_contribute:
				mType = "8hr";
				break;
			case R.id.btn_today:
				mType = "hot";
				break;
			case R.id.btn_true:
				mType = "imgrank";
				break;
			case R.id.btn_cydb:// 穿衣打扮

				getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ChuanYiFragment.newInstance()).commit();
				return;
			default:
				break;
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, MainFragment.newInstance(mType)).commit();
		}
	}

}
