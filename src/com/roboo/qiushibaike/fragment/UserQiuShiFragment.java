package com.roboo.qiushibaike.fragment;

import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.adapter.QiuShiListAdapter;
import com.roboo.qiushibaike.model.QiuShiItem;
import com.roboo.qiushibaike.ptr.PullToRefreshBase.Mode;
import com.roboo.qiushibaike.ptr.PullToRefreshListView;
import com.roboo.qiushibaike.utils.QSBKUtils;
import com.roboo.qiushibaike.view.RoundProgressBar;

public class UserQiuShiFragment extends Fragment
{
	private PullToRefreshListView mPTRListView;
	private RoundProgressBar mRoundProgressBar;
	private TextView mTextView;
	private LinkedList<QiuShiItem> mData;
	private QiuShiListAdapter mAdapter;
	private QiuShiItem mItem;
	private ProgressBar mBar;
	private GetDataTask mGetDataTask = new GetDataTask();

	public static UserQiuShiFragment newInstance(QiuShiItem item)
	{
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", item);
		UserQiuShiFragment mainFragment = new UserQiuShiFragment();
		mainFragment.setArguments(bundle);
		return mainFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mItem = (QiuShiItem) getArguments().getSerializable("item");
		View view = inflater.inflate(R.layout.ptr_listview, null);
		mPTRListView = (PullToRefreshListView) view.findViewById(R.id.ptr_listview);
		mTextView = (TextView) view.findViewById(R.id.tv_text);
		mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		mBar = (ProgressBar) view.findViewById(R.id.pb_progress);
		mBar.setVisibility(View.VISIBLE);
		mRoundProgressBar.setVisibility(View.INVISIBLE);
	
		return view;
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mPTRListView.setMode(Mode.DISABLED);
		if(null == mGetDataTask)
		{
			mGetDataTask = new GetDataTask();
		}
		mGetDataTask.execute();

	}
	@Override
	public void onPause()
	{	 
		super.onPause();
		mGetDataTask.cancel(true);
	}
	public class GetDataTask extends AsyncTask<String, Float, LinkedList<QiuShiItem>>
	{
		@Override
		protected LinkedList<QiuShiItem> doInBackground(String... params)
		{
			LinkedList<QiuShiItem> data = QSBKUtils.handleUserQiuSHiData(mItem);
			return data;

		}
		@Override
		protected void onPostExecute(LinkedList<QiuShiItem> result)
		{
			mPTRListView.onRefreshComplete();
			super.onPostExecute(result);
			if (result != null)
			{
				mBar.setVisibility(View.GONE);
				mTextView.setVisibility(View.GONE);
				mTextView.setVisibility(View.GONE);
				mRoundProgressBar.setVisibility(View.GONE);
				if (null == mData)
				{
					mData = new LinkedList<QiuShiItem>();
					mData.addAll(result);
					mAdapter = new QiuShiListAdapter(getActivity(), mData,true);
					mPTRListView.setAdapter(mAdapter);
				}
			}
		}
	}
}
