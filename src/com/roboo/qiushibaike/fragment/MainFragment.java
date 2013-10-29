package com.roboo.qiushibaike.fragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.adapter.QiuShiListAdapter;
import com.roboo.qiushibaike.dao.IQiuShiItemDao;
import com.roboo.qiushibaike.dao.impl.QiuShiItemDaoImpl;
import com.roboo.qiushibaike.databases.DBHelper;
import com.roboo.qiushibaike.model.QiuShiItem;
import com.roboo.qiushibaike.ptr.PullToRefreshBase;
import com.roboo.qiushibaike.ptr.PullToRefreshBase.OnRefreshListener2;
import com.roboo.qiushibaike.ptr.PullToRefreshListView;
import com.roboo.qiushibaike.utils.CYDBUtils;
import com.roboo.qiushibaike.utils.QSBKUtils;
import com.roboo.qiushibaike.view.RoundProgressBar;

public class MainFragment extends Fragment
{
	private PullToRefreshListView mPTRListView;
	private RoundProgressBar mRoundProgressBar;
	private TextView mTextView;
	private LinkedList<QiuShiItem> mData;
	private QiuShiListAdapter mAdapter;
	private String mType;
	private int mCurrentPageNo = 1;
	private GetDataTask mGetDataTask = new GetDataTask();
	private static final int ONE_MINUTE=60*1000;

	public static MainFragment newInstance(String type)
	{
		Bundle bundle = new Bundle();
		bundle.putString("type", type);
		MainFragment mainFragment = new MainFragment();
		mainFragment.setArguments(bundle);
		return mainFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mType = getArguments().getString("type");
		View view = inflater.inflate(R.layout.ptr_listview, null);
		mPTRListView = (PullToRefreshListView) view.findViewById(R.id.ptr_listview);
		mTextView = (TextView) view.findViewById(R.id.tv_text);
		mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setListener();
		IQiuShiItemDao qiuShiItemDao = new QiuShiItemDaoImpl(new DBHelper(getActivity()));
		mData = qiuShiItemDao.getItems(mType, mCurrentPageNo);	
		if (null != mData)
		{
			mAdapter = new QiuShiListAdapter(getActivity(), mData);
			mPTRListView.setAdapter(mAdapter);
			mRoundProgressBar.setVisibility(View.GONE);
			mTextView.setVisibility(View.GONE);
		}
		SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getPackageName(),Context.MODE_PRIVATE);
		//每隔60秒去获取一次数据
		if((System.currentTimeMillis() - preferences.getLong(mType, System.currentTimeMillis()) ==0 )||(System.currentTimeMillis() - preferences.getLong(mType, System.currentTimeMillis()) >ONE_MINUTE))
		{
			//自动刷新
			mPTRListView.setRefreshing(); //去执行onPullDownToRefresh
			preferences.edit().putLong(mType, System.currentTimeMillis()).commit();
			mPTRListView.setShowViewWhileRefreshing(true);
			System.out.println("应该去获取数据了 " + mGetDataTask.getStatus());
			if( mGetDataTask.getStatus() == Status.PENDING)
			{
			    System.out.println("上一个任务已经执行完毕");
				String[] params = new String[] { mType, "" + mCurrentPageNo };
				mGetDataTask = new GetDataTask();
				mGetDataTask.execute(params);
			
			}
		}
		
	}
	@Override
	public void onPause()
	{
		 if(mGetDataTask.getStatus() == Status.RUNNING)
		 { 
			 mGetDataTask.cancel(true);
			// Toast.makeText(getActivity(), "Fragment发生切换,当前任务应该取消", Toast.LENGTH_SHORT).show());
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
				String[] params = new String[] { mType, "" + mCurrentPageNo };
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
				String[] params = new String[] { mType, "" + mCurrentPageNo };
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
	}

	public class GetDataTask extends AsyncTask<String, Float, Boolean>
	{
		@Override
		protected Boolean doInBackground(String... params)
		{
			System.out.println("mCurrentPageNo = " + mCurrentPageNo);
			Boolean flag = false;
			try
			{
				CYDBUtils.handleChuanYiItems(mCurrentPageNo+"");
				LinkedList<QiuShiItem> data = QSBKUtils.handleQiuSHiData(params[0], params[1]);
				IQiuShiItemDao qiuShiItemDao = new QiuShiItemDaoImpl(new DBHelper(getActivity()));
				if (null != data && data.size() > 0)
				{
					if (mCurrentPageNo == 1)// 下拉刷新获取到数据后删除以前的数据保存最新的数据
					{
						qiuShiItemDao.removePreviousItems(mType);
					}
					int insertCount = 0;
					float progress = 0;
					for (int i = data.size() - 1; i >= 0; i--)
					{
						QiuShiItem item = data.get(i);
						item.time = System.currentTimeMillis();
						insertCount += qiuShiItemDao.insert(item);
						System.out.println(item);
						progress += 100.0 / data.size();
						publishProgress(progress);
					}
					System.out.println("插入 " + insertCount + " 条数据");
					flag = true;
				}
			}
			catch (SocketTimeoutException e)
			{
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "连接超时异常", Toast.LENGTH_SHORT).show();
				Looper.loop();
				return flag;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "发生I/O异常", Toast.LENGTH_SHORT).show();
				Looper.loop();
				return flag;
			}
			catch (Exception e) {
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getActivity(), "发生异常", Toast.LENGTH_SHORT).show();
				Looper.loop();
				return flag;
			}
			return flag;

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
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			mPTRListView.onRefreshComplete();
			if (result.booleanValue())
			{
				if (mCurrentPageNo == 1)
				{
					mData = null;
				}
				IQiuShiItemDao qiuShiItemDao = new QiuShiItemDaoImpl(new DBHelper(getActivity()));
				//注意，就算是上拉加载的数据也会在最上面根据插入数据的数据进行排序的，所以每次获取最上面的数据即为插入的最新数据[可能是上拉加载的数据也可能是下拉刷新的数据]
				LinkedList<QiuShiItem> tmp = qiuShiItemDao.getItems(mType, 1);
				if (null == mData)// 第一次肯定为空
				{
					mData = new LinkedList<QiuShiItem>();
					if (tmp != null)
					{
						mData.addAll(tmp);
						mAdapter = new QiuShiListAdapter(getActivity(), mData);
						mPTRListView.setAdapter(mAdapter);
					}
				}
				else
				{
					if (null != tmp)
					{
						if (mCurrentPageNo == 1)// 下拉刷新有数据应该放在最前面
						{
							for (QiuShiItem item : tmp)
							{
								if (!mData.contains(item))
								{
									mData.addLast(item);
								}
							}
						}
						else
						{
							for (QiuShiItem item : tmp)
							{
								if (!mData.contains(item))
								{
									mData.addLast(item);
								}
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
