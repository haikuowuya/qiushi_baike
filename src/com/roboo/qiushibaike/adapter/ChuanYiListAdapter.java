package com.roboo.qiushibaike.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.model.ChuanYiItem;

public class ChuanYiListAdapter extends BaseAdapter
{
	private Context context;
	private LinkedList<ChuanYiItem> data;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private ImageCache mImageCache;
	private DisplayMetrics mDisplayMetrics;
 

	public ChuanYiListAdapter(Context context, LinkedList<ChuanYiItem> data)
	{
		super();
		this.context = context;
		this.data = data;
		this.mQueue = Volley.newRequestQueue(context);
		this.mImageCache = new BitmapImageCache();
		this.mDisplayMetrics = context.getResources().getDisplayMetrics();
		this.mImageLoader = new ImageLoader(mQueue, mImageCache);
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
		ChuanYiItem item = null == data ? null : data.get(position);
		if (null != item)
		{
			ViewHolder holder;

			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chuanyi_list_item, null);
				holder.mIVImg = (ImageView) convertView.findViewById(R.id.iv_img);
				holder.mTVContent = (TextView) convertView.findViewById(R.id.tv_content);
				holder.mTVTitle = (TextView) convertView.findViewById(R.id.tv_title);
			
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTVContent.setText(item.content);
			holder.mTVTitle.setText(item.title);
			mImageLoader.get(item.img, ImageLoader.getImageListener(holder.mIVImg, R.drawable.ic_launcher, R.drawable.ic_launcher), (int)(120*mDisplayMetrics.density), (int)(120*mDisplayMetrics.density));

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
		public TextView mTVTitle;

		public ImageView mIVImg;

		public TextView mTVContent;

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

}
