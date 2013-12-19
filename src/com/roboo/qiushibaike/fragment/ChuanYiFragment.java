package com.roboo.qiushibaike.fragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.WebViewActivity;
import com.roboo.qiushibaike.adapter.BaseListAdapter;
import com.roboo.qiushibaike.model.ChuanYiItem;
import com.roboo.qiushibaike.ptr.PullToRefreshBase;
import com.roboo.qiushibaike.ptr.PullToRefreshBase.OnRefreshListener2;
import com.roboo.qiushibaike.ptr.PullToRefreshListView;
import com.roboo.qiushibaike.utils.CYDBUtils;
import com.roboo.qiushibaike.view.RoundProgressBar;

public class ChuanYiFragment extends Fragment
{
	private PullToRefreshListView mPTRListView;
	private RoundProgressBar mRoundProgressBar;
	private TextView mTextView;
	private LinkedList<ChuanYiItem> mData;
	private ProgressBar mProgressBar;
	private BaseListAdapter<ChuanYiItem> mAdapter;
	private static final String PREF_UPDATE_TIME_FLAG = "cydb";// 穿衣打扮
	private int mCurrentPageNo = 1;
	private GetDataTask mGetDataTask = new GetDataTask();
	private static final int ONE_MINUTE = 60 * 1000;

	public static ChuanYiFragment newInstance()
	{

		ChuanYiFragment chuanYiFragment = new ChuanYiFragment();

		return chuanYiFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.ptr_listview, null);
		view.setPadding(0, 0, 0, 0);
		mPTRListView = (PullToRefreshListView) view.findViewById(R.id.ptr_listview);
		mTextView = (TextView) view.findViewById(R.id.tv_text);
		mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		mProgressBar = (ProgressBar) view.findViewById(R.id.pb_progress);
		mRoundProgressBar.setVisibility(View.INVISIBLE);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setListener();
		SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
		if (mData == null)
		{
			mProgressBar.setVisibility(View.VISIBLE);
		}
		// 每隔60秒去获取一次数据
		if (mData == null || (System.currentTimeMillis() - preferences.getLong(PREF_UPDATE_TIME_FLAG, System.currentTimeMillis()) == 0)
				|| (System.currentTimeMillis() - preferences.getLong(PREF_UPDATE_TIME_FLAG, System.currentTimeMillis()) > ONE_MINUTE))
		{
			// 自动刷新
			mPTRListView.setRefreshing(); // 去执行onPullDownToRefresh
			preferences.edit().putLong(PREF_UPDATE_TIME_FLAG, System.currentTimeMillis()).commit();
			mPTRListView.setShowViewWhileRefreshing(true);
			System.out.println("应该去获取数据了 " + mGetDataTask.getStatus());
			if (mGetDataTask.getStatus() == Status.PENDING)
			{
				System.out.println("上一个任务已经执行完毕");
				String[] params = new String[] { "" + mCurrentPageNo };
				mGetDataTask = new GetDataTask();
				mGetDataTask.execute(params);

			}
		}

	}

	@Override
	public void onPause()
	{
		if (mGetDataTask.getStatus() == Status.RUNNING)
		{
			mGetDataTask.cancel(true);
			// Toast.makeText(getActivity(), "Fragment发生切换,当前任务应该取消",
			// Toast.LENGTH_SHORT).show());
			System.out.println("Fragment发生切换,当前任务应该取消");

		}
		super.onPause();
	}

	private void setListener()
	{
		this.mPTRListView.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mCurrentPageNo = 1;
				String[] params = new String[] { "" + mCurrentPageNo };
				if (mPTRListView.isRefreshing())
				{
					mGetDataTask = new GetDataTask();
					mGetDataTask.execute(params);
					Toast.makeText(getActivity(), "正在下拉刷新中……", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getActivity(), "已经开始下拉刷新中……", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{

				mCurrentPageNo += 1;
				String[] params = new String[] { "" + mCurrentPageNo };
				if (mPTRListView.isRefreshing())
				{
					mGetDataTask = new GetDataTask();
					mGetDataTask.execute(params);
					Toast.makeText(getActivity(), "正在努力加载中……", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getActivity(), "已经开始努力加载中……", Toast.LENGTH_SHORT).show();
				}
			}
		});
		this.mPTRListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ChuanYiItem item = (ChuanYiItem) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra("item", item);
				startActivity(intent);
			}
		});
	}

	public class GetDataTask extends AsyncTask<String, Float, LinkedList<ChuanYiItem>>
	{
		@Override
		protected LinkedList<ChuanYiItem> doInBackground(String... params)
		{
			System.out.println("mCurrentPageNo = " + mCurrentPageNo);
			LinkedList<ChuanYiItem> data = null;
			try
			{
				data = CYDBUtils.handleChuanYiItems(params[0]);
			}
			catch (SocketTimeoutException e)
			{
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "连接超时异常", Toast.LENGTH_SHORT).show();
				Looper.loop();

			}
			catch (IOException e)
			{
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "发生I/O异常", Toast.LENGTH_SHORT).show();
				Looper.loop();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "发生异常", Toast.LENGTH_SHORT).show();
				Looper.loop();

			}
			return data;

		}

		@Override
		protected void onCancelled()
		{
			mPTRListView.onRefreshComplete();
			System.out.println("onCancelled 执行");
		}

		@Override
		protected void onProgressUpdate(Float... values)
		{
			super.onProgressUpdate(values);
			if (mData == null && mCurrentPageNo == 1)
			{
				mRoundProgressBar.setProgress(values[0].intValue());
				if (values[0] > 99)
				{
					mTextView.setVisibility(View.GONE);
					mRoundProgressBar.setVisibility(View.GONE);
				}
			}
		}

		@Override
		protected void onPostExecute(LinkedList<ChuanYiItem> result)
		{
			super.onPostExecute(result);
			mPTRListView.onRefreshComplete();
			mProgressBar.setVisibility(View.GONE);
			mTextView.setVisibility(View.GONE);
			if (result != null)
			{
				if (mCurrentPageNo == 1)
				{
					mData = null;
				}
				if (null == mData)// 第一次肯定为空
				{
					mData = new LinkedList<ChuanYiItem>();
					mData.addAll(result);
					mAdapter = new BaseListAdapter<ChuanYiItem>(getActivity(), mData);
					mPTRListView.setAdapter(mAdapter);
				}
				else
				{
					if (mCurrentPageNo == 1)// 下拉刷新有数据应该放在最前面
					{
						for (ChuanYiItem item : result)
						{
							if (!mData.contains(item))
							{
								mData.addLast(item);
							}
						}
					}
					else
					{
						for (ChuanYiItem item : result)
						{
							if (!mData.contains(item))
							{
								mData.addLast(item);
							}
						}

					}
					mAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				if (mCurrentPageNo > 1)
				{
					mCurrentPageNo -= 1;
				}
			}
		}
	}

}
