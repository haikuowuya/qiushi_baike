package com.roboo.qiushibaike.fragment;

import java.util.LinkedList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.UserQiuShiActivity;
import com.roboo.qiushibaike.adapter.CommentListAdapter;
import com.roboo.qiushibaike.dao.ICommentItemDao;
import com.roboo.qiushibaike.dao.impl.CommentItemDaoImpl;
import com.roboo.qiushibaike.databases.DBHelper;
import com.roboo.qiushibaike.model.CommentItem;
import com.roboo.qiushibaike.model.QiuShiItem;
import com.roboo.qiushibaike.ptr.PullToRefreshBase;
import com.roboo.qiushibaike.ptr.PullToRefreshBase.OnRefreshListener2;
import com.roboo.qiushibaike.ptr.PullToRefreshListView;
import com.roboo.qiushibaike.utils.QSBKUtils;
import com.roboo.qiushibaike.view.RoundProgressBar;

public class CommentFragment extends Fragment
{
	private PullToRefreshListView mPTRListView;
	private RoundProgressBar mRoundProgressBar;
	private TextView mTextView;
	private LinkedList<CommentItem> mData;
	private CommentListAdapter mAdapter;
	private QiuShiItem mItem;
	private int mCurrentPageNo = 1;
	private static final String ARG_ITEM = "item";
	private GetDataTask mGetDataTask = new GetDataTask();

	public static CommentFragment newInstance(QiuShiItem item)
	{
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_ITEM, item);
		CommentFragment commentFragment = new CommentFragment();
		commentFragment.setArguments(bundle);
		return commentFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mItem = (QiuShiItem) getArguments().getSerializable(ARG_ITEM);
		View view = inflater.inflate(R.layout.ptr_listview, null);
		view.setPadding(0, 0, 0, 0);
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
		ICommentItemDao commentItemDao = new CommentItemDaoImpl(new DBHelper(getActivity()));
		if (null != mItem)
		{
			mData = commentItemDao.getItems(mItem.md5, mCurrentPageNo);
			mAdapter = new CommentListAdapter(getActivity(), mData,mItem);
			mPTRListView.setAdapter(mAdapter);
			if (null == mData)
			{
				mGetDataTask.execute();
			}
			else
			{
				mRoundProgressBar.setVisibility(View.GONE);
				mTextView.setVisibility(View.GONE);
			}
		}
	}

	private void setListener()
	{
		this.mPTRListView.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				if (mGetDataTask.getStatus() == Status.FINISHED)
				{
					mGetDataTask = new GetDataTask();

				}
				if (mGetDataTask.getStatus() != Status.RUNNING)
				{
					mCurrentPageNo = 1; 
					mGetDataTask.execute();
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				if (mGetDataTask.getStatus() == Status.FINISHED)
				{
					mGetDataTask = new GetDataTask();

				}
				if (mGetDataTask.getStatus() != Status.RUNNING)
				{
					mCurrentPageNo += 1; 
					mGetDataTask.execute();
				}
			}
		});
		this.mPTRListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				 CommentItem item = (CommentItem) parent.getAdapter().getItem(position);
				 Intent intent = new Intent(getActivity(), UserQiuShiActivity.class);				  
					mItem.authorUrl = item.commentAuthorUrl;
					mItem.authorName = item.commentAuthorName;
					intent.putExtra("item", mItem);
					getActivity().startActivity(intent);
			}
		});
	}
	@Override
	public void onPause()
	{
		if(mGetDataTask.getStatus() == Status.RUNNING)
		{
			mGetDataTask.cancel(true);
		}
		super.onPause();
	}
	public class GetDataTask extends AsyncTask<Void, Float, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			System.out.println("mCurrentPageNo = " + mCurrentPageNo);
			if (mCurrentPageNo > 1)
			{
				return Boolean.valueOf(true);
			}
			else
			{
				Boolean flag = false;
				LinkedList<CommentItem> data = QSBKUtils.handleCommentData(mItem);
				ICommentItemDao commentItemDao = new CommentItemDaoImpl(new DBHelper(getActivity()));
				if (null != data && data.size() > 0)
				{
					int insertCount = 0;
					float progress = 0;
					for (int i = data.size() - 1; i >= 0; i--)
					{
						CommentItem item = data.get(i);
						item.time = System.currentTimeMillis();
						insertCount += commentItemDao.insert(item);
						System.out.println(item);
						progress += 100.0 / data.size();
						publishProgress(progress);
					}
					System.out.println("插入 " + insertCount + " 条数据");
					flag = true;
				}
				return flag;
			}
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
			mPTRListView.onRefreshComplete();
			if (result.booleanValue())
			{
				ICommentItemDao commentItemDao = new CommentItemDaoImpl(new DBHelper(getActivity()));
				LinkedList<CommentItem> tmp = commentItemDao.getItems(mItem.md5, mCurrentPageNo);
				if (tmp != null && tmp.size() > 0)
				{
					if (null == mData)
					{
						mRoundProgressBar.setVisibility(View.GONE);
						mTextView.setVisibility(View.GONE);
						mData = new LinkedList<CommentItem>();
						mData.addAll(tmp);
						mAdapter = new CommentListAdapter(getActivity(), mData,mItem);
						mPTRListView.setAdapter(mAdapter);
					}
					else
					{
						if (mCurrentPageNo == 1)
						{
							for (CommentItem item : tmp)
							{
								if (!mData.contains(item))
								{
									mData.addFirst(item);
								}
							}
						}
						else
						{
							for (CommentItem item : tmp)
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
			}
			else
			{
				mCurrentPageNo -= 1;
			}
		}
	}
}
