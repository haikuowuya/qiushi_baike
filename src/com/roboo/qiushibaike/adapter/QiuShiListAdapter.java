package com.roboo.qiushibaike.adapter;

import java.text.NumberFormat;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.roboo.qiushibaike.CommentActivity;
import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.ShowImgActivity;
import com.roboo.qiushibaike.UserQiuShiActivity;
import com.roboo.qiushibaike.model.QiuShiItem;

public class QiuShiListAdapter extends BaseAdapter
{
	private Context context;
	private LinkedList<QiuShiItem> data;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private ImageCache mImageCache;
	private DisplayMetrics mDisplayMetrics;
	private boolean isUserQiuShiList = false;
	public QiuShiListAdapter(Context context, LinkedList<QiuShiItem> data)
	{
		super();
		this.context = context;
		this.data = data;
		this.mQueue = Volley.newRequestQueue(context);
		this.mImageCache = new BitmapImageCache();
		this.mDisplayMetrics = context.getResources().getDisplayMetrics();
		this.mImageLoader = new ImageLoader(mQueue, mImageCache);
	}
	public QiuShiListAdapter(Context context, LinkedList<QiuShiItem> data,boolean isUserQiuShiList)
	{
		super();
		this.context = context;
		this.data = data;
		this.mQueue = Volley.newRequestQueue(context);
		this.mImageCache = new BitmapImageCache();
		this.mDisplayMetrics = context.getResources().getDisplayMetrics();
		this.mImageLoader = new ImageLoader(mQueue, mImageCache);
		this.isUserQiuShiList = isUserQiuShiList;
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
		QiuShiItem item = null == data ? null : data.get(position);
		if (null != item)
		{
			ViewHolder holder;
			final OnClickListenerImpl impl = new OnClickListenerImpl(item);
			// if (convertView == null)
			// {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qiushi_list_item, null);
			holder.mIVAuthorImg = (ImageView) convertView.findViewById(R.id.iv_author_img);
			holder.mIVImg = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.mTVAgreeCount = (TextView) convertView.findViewById(R.id.tv_agree_count);
			holder.mTVAuthorName = (TextView) convertView.findViewById(R.id.tv_author_name);
			holder.mTVCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
			holder.mTVContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.mTVDisAgreeCount = (TextView) convertView.findViewById(R.id.tv_dis_agree_count);
			convertView.setTag(holder);
			// }
			// else
			// {
			// holder = (ViewHolder) convertView.getTag();
			// }
			holder.setListener(impl);
			holder.mTVAgreeCount.setText(item.agreeCount);
			holder.mTVAuthorName.setText(item.authorName);
			holder.mTVCommentCount.setText(item.commentCount);
			holder.mTVContent.setText(item.content);
			holder.mTVDisAgreeCount.setText(item.disAgreeCount);
			if (null != item.authorImg)
			{
				mImageLoader.get(item.authorImg, ImageLoader.getImageListener(holder.mIVAuthorImg, R.drawable.ic_default, R.drawable.ic_default),
						(int) (48 * mDisplayMetrics.density), (int) (48 * mDisplayMetrics.density));
			}
			if (null != item.img)
			{
				mImageLoader.get(item.img, ImageLoader.getImageListener(holder.mIVImg, R.drawable.ic_default, R.drawable.ic_default),
						mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
			}
			else
			{
				holder.mIVImg.setVisibility(View.GONE);
			}
		}
		setViewAnimator(convertView);
		return convertView;
	}

	private void setViewAnimator(View convertView)
	{
		AnimatorSet set = new AnimatorSet();
		// set.playTogether(new Animator[]{scaleX,scaleY});
		ObjectAnimator translateX = ObjectAnimator.ofFloat(convertView, "translationX", context.getResources().getDisplayMetrics().widthPixels, 0f);
		set.play(translateX);
		set.setStartDelay(100);
		set.setDuration(300);
		set.start();
		
	}

	public class ViewHolder
	{
		public TextView mTVAgreeCount;
		public TextView mTVDisAgreeCount;
		public ImageView mIVImg;
		public ImageView mIVAuthorImg;
		public TextView mTVAuthorName;
		public TextView mTVContent;
		public TextView mTVCommentCount;

		public void setListener(OnClickListener listener)
		{
			this.mIVAuthorImg.setOnClickListener(listener);
			this.mTVAgreeCount.setOnClickListener(listener);
			this.mTVCommentCount.setOnClickListener(listener);
			this.mTVContent.setOnClickListener(listener);
			this.mTVDisAgreeCount.setOnClickListener(listener);
			if(!isUserQiuShiList)
			{
			this.mTVAuthorName.setOnClickListener(listener);
			this.mIVImg.setOnClickListener(listener);
			}
		}
	}

	private class BitmapImageCache implements ImageCache
	{
		private static final int MAX_CACHE_SIZE = 4 * 1024 * 1024;
		private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE)
		{
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			};
		};

		@Override
		public Bitmap getBitmap(String url)
		{
			return mLruCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap)
		{
			mLruCache.put(url, bitmap);
		}

	}

	private class OnClickListenerImpl implements OnClickListener
	{
		private QiuShiItem item;

		public OnClickListenerImpl(QiuShiItem item)
		{
			super();
			this.item = item;
		}

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.iv_img:
				Intent intent = new Intent(context, ShowImgActivity.class);
				if (null != item.img && item.img.contains("/small/"))
				{
					String[] tmp = item.img.split("/small/");
					item.img = tmp[0] + "/medium/" + tmp[1];
				}
				intent.putExtra("src", item.img);
				context.startActivity(intent);
				break;
			case R.id.tv_comment_count:
				if(!"0 条评论".equals(item.commentCount))
				{
					intent = new Intent(context, CommentActivity.class);
					intent.putExtra("item", item);
					context.startActivity(intent);				
				}
				else
				{
					Toast.makeText(context, "暂无评论", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.tv_author_name:
			case R.id.iv_author_img:
				if(item.authorUrl !=null)
				{
				intent = new Intent(context, UserQiuShiActivity.class);
				intent.putExtra("item", item);
				context.startActivity(intent);
				}
				else
				{
					Toast.makeText(context, "无名氏", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.tv_agree_count:
				try
				{
					int  text = Integer.parseInt(item.agreeCount.trim());
					text+=1;
					((TextView)v).setText(text+"");
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
				break;
			case R.id.tv_dis_agree_count:
				 try
				{
					  
					int text = Integer.parseInt(item.disAgreeCount.trim());
					 text-=1;
					 ((TextView)v).setText(text+"");
				}
				catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 break;
			default:
				break;
			}
		}
	}

}
