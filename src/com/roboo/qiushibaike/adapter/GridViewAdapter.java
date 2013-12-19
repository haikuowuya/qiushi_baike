package com.roboo.qiushibaike.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roboo.qiushibaike.R;

public class GridViewAdapter extends BaseAdapter
{
	private Context context;
	private String[] texts ={"糗事百科","穿衣打扮"," CSDN "," 科技锋芒 "};
	private int[] imgs = {R.drawable.ic_qsbk,R.drawable.ic_cydb,R.drawable.ic_csdn,R.drawable.ic_kjfm};
	public GridViewAdapter(Context context)
	{
		super();
		this.context = context;
	}

	@Override
	public int getCount()
	{
		
		return imgs.length;
	}

	@Override
	public Object getItem(int position)
	{
		
		return imgs[position]+"@"+texts[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.grid_list_item, null);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img);
		TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
		imageView.setImageResource(imgs[position]);
		textView.setText(texts[position]);
		return convertView;
	}
	
}
