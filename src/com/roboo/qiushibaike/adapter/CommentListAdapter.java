package com.roboo.qiushibaike.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.UserQiuShiActivity;
import com.roboo.qiushibaike.model.CommentItem;
import com.roboo.qiushibaike.model.QiuShiItem;

public class CommentListAdapter extends BaseAdapter
{
	private Context context;
	private LinkedList<CommentItem> data;
	private QiuShiItem mqItem;

	public CommentListAdapter(Context context, LinkedList<CommentItem> data, QiuShiItem qItem)
	{
		super();
		this.context = context;
		this.data = data;
		this.mqItem = qItem;
	}

	@Override
	public int getCount()
	{

		return null == data ? 0 : data.size();
	}

	@Override
	public Object getItem(int position)
	{

		return null == data ? null : data.get(position);
	}

	@Override
	public long getItemId(int position)
	{

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		CommentItem item = data.get(position);
		if (null != item)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);
				holder = new ViewHolder();
				holder.mTVCommentAuthorName = (TextView) convertView.findViewById(R.id.tv_comment_author_name);
				holder.mTVCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
				holder.mTVCommentIndex = (TextView) convertView.findViewById(R.id.tv_comment_index);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTVCommentAuthorName.setText(item.commentAuthorName);
			holder.mTVCommentContent.setText(item.commentContent);
			holder.mTVCommentIndex.setText(item.commentIndex);
			 
			holder.mTVCommentAuthorName.setOnClickListener(new OnClickListenerImpl(item));
		}
		setViewAnimation(convertView);
		return convertView;
	}

	private void setViewAnimation(View convertView)
	{
	
		AnimatorSet set = new AnimatorSet();
		// set.playTogether(new Animator[]{scaleX,scaleY});
		ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView, "rotationX", 0f,45f);
		set.play(translateX);
		set.setStartDelay(100);
		set.setDuration(3000);
		set.start();
	}

	public class ViewHolder
	{
		public TextView mTVCommentAuthorName;
		public TextView mTVCommentContent;
		public TextView mTVCommentIndex;
	}

	private class OnClickListenerImpl implements OnClickListener
	{
		private CommentItem item;

		public OnClickListenerImpl(CommentItem item)
		{
			this.item = item;
		}

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.tv_comment_author_name:
				if (mqItem != null)
				{
					Intent intent = new Intent(context, UserQiuShiActivity.class);
					mqItem.authorUrl = item.commentAuthorUrl;
					mqItem.authorName = item.commentAuthorName;
					intent.putExtra("item", mqItem);
					context.startActivity(intent);
				}
				else
				{
					// Toast.makeText(context, "无名氏",
					// Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}
	}

}
