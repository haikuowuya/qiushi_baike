package com.roboo.qiushibaike.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.utils.MD5Utils;
import com.roboo.qiushibaike.view.RoundProgressBar;

public class ShowImgFragment extends Fragment
{
	private String mSRC;
	private ImageLoader mImageLoader;
	private RoundProgressBar mRoundProgressBar;
	private ImageView mImageView;

	public static ShowImgFragment newInstance(String src)
	{
		Bundle bundle = new Bundle();
		bundle.putString("src", src);
		ShowImgFragment mainFragment = new ShowImgFragment();
		mainFragment.setArguments(bundle);
		return mainFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mSRC = getArguments().getString("src");
		System.out.println("mSRC = " + mSRC);
		View view = inflater.inflate(R.layout.fragment_show_img, null);
		mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		mImageView = (ImageView) view.findViewById(R.id.iv_img);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		new BitmapTask().execute(mSRC);
	}

	private class BitmapTask extends AsyncTask<String, Float, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... params)
		{
			Bitmap bitmap = null;
			File file = new File(getActivity().getCacheDir(), MD5Utils.generate(mSRC));
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			try
			{
				fileOutputStream = new FileOutputStream(file);
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				if (!file.exists())
				{
					file.mkdirs();
				}
				if (connection.getResponseCode() == HttpStatus.SC_OK)
				{

					inputStream = connection.getInputStream();
					byte[] buffer = new byte[4096];
					int len = -1;
					int count = 0;
					while ((len = inputStream.read(buffer)) != -1)
					{
						count = count + len;
						fileOutputStream.write(buffer, 0, len);
						float percent = (float) (count * 100.0 / connection.getContentLength());
						System.out.println("count = " + count + " percent = " + percent + " getContentLength = " + connection.getContentLength());
						publishProgress(percent);
					}
					Options options = new Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(file.getAbsolutePath(), options);
					int width = getResources().getDisplayMetrics().widthPixels;
					int height = getResources().getDisplayMetrics().heightPixels;
					int outWidth = options.outWidth;
					int outHeight = options.outHeight;
					options.inSampleSize = (outHeight / height) > (outWidth / width) ? (outHeight / height) : (outWidth / width);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
					System.out.println(" bitmap = " + bitmap + " outWidth = " + outWidth + " outHeight = " + outHeight);

				}
			}
			catch (MalformedURLException e)
			{

				e.printStackTrace();
			}
			catch (SocketTimeoutException e)
			{

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (null != inputStream)
					{
						inputStream.close();
					}
					if (null != fileOutputStream)
					{
						fileOutputStream.close();
					}
				}
				catch (IOException e)
				{

					e.printStackTrace();
				}
			}

			return bitmap;
		}

		@Override
		protected void onProgressUpdate(Float... values)
		{
			super.onProgressUpdate(values);
			if (values[0] > 99)
			{
				mRoundProgressBar.setVisibility(View.GONE);
			}
			int progress = values[0].intValue();
			mRoundProgressBar.setProgress(progress);
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{

			super.onPostExecute(result);
			if (null != result)
			{
				mImageView.setVisibility(View.VISIBLE);
				mImageView.setImageBitmap(result);
			}
		}
	}

	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState)
	// {
	// mSRC = getArguments().getString("src");
	// if (null != mSRC)
	// {
	//
	// NetworkImageView networkImageView = new NetworkImageView(getActivity());
	// mImageLoader = new ImageLoader(Volley.newRequestQueue(getActivity()), new
	// ImageCache()
	// {
	// private static final int MAX_CACHE_SIZE = 4 * 1024 * 1024;
	// private LruCache<String, Bitmap> lruCache = new LruCache<String,
	// Bitmap>(MAX_CACHE_SIZE)
	// {
	// protected int sizeOf(String key, Bitmap value)
	// {
	// return value.getHeight() * value.getRowBytes();
	// };
	// };
	//
	// @Override
	// public void putBitmap(String url, Bitmap bitmap)
	// {
	// lruCache.put(url, bitmap);
	// }
	//
	// @Override
	// public Bitmap getBitmap(String url)
	// {
	// return lruCache.get(url);
	// }
	// });
	// networkImageView.setImageUrl(mSRC, mImageLoader);
	// return networkImageView;
	// }
	// else
	// {
	// return null;
	// }
	//
	// }
}
