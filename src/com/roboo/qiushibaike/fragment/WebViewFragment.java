package com.roboo.qiushibaike.fragment;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.model.CSDNItem;
import com.roboo.qiushibaike.model.ChuanYiItem;
import com.roboo.qiushibaike.view.RoundProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewFragment extends Fragment
{
	private WebView mWebView;
	private static final String ARG_ITEM = "item";
	private ChuanYiItem mChuanYiItem;
	private CSDNItem mCsdnItem;
	private RoundProgressBar mRoundProgressBar;
	private ProgressBar mProgressBar;
	private TextView mTextView;

	public static WebViewFragment newInstance(Object item)
	{
		Bundle bundle = new Bundle();
		if (null != item)
		{
			if (item instanceof CSDNItem)
			{
				bundle.putSerializable(ARG_ITEM, (CSDNItem) item);
			}
			else if (item instanceof ChuanYiItem)
			{
				bundle.putSerializable(ARG_ITEM, (ChuanYiItem) item);
			}
		}
		WebViewFragment mainFragment = new WebViewFragment();
		mainFragment.setArguments(bundle);
		return mainFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		Object object = getArguments().getSerializable(ARG_ITEM);
		if (object instanceof ChuanYiItem)
		{
			mChuanYiItem = (ChuanYiItem) object;
		}
		else if (object instanceof CSDNItem)
		{
			mCsdnItem = (CSDNItem) object;
		}
		View view = inflater.inflate(R.layout.fragment_web_view, null);
		mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		mWebView = (WebView) view.findViewById(R.id.wv_webview);
		mTextView = (TextView) view.findViewById(R.id.tv_text);
		mProgressBar = (ProgressBar) view.findViewById(R.id.pb_progress_bar);
		mRoundProgressBar.setVisibility(View.INVISIBLE);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initWebView();
		if(mCsdnItem != null)
		{
			mWebView.loadUrl(mCsdnItem.url);
			return;
		}
		 
		new WebContentTask().execute();
	}

	@SuppressWarnings("deprecation")
	private void initWebView()
	{
		this.mWebView.getSettings().setJavaScriptEnabled(true);
		this.mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//		this.mWebView.setInitialScale(100);
		// this.mWebView.getSettings().setUseWideViewPort(true);
		this.mWebView.getSettings().setLoadWithOverviewMode(true);
		this.mWebView.getSettings().setTextSize(TextSize.NORMAL);
		initWebViewClient();

	}

	private void initWebViewClient()
	{
		this.mWebView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{

				super.onPageFinished(view, url);
			}
		});
		this.mWebView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				if (newProgress == 100)
				{
					mProgressBar.setVisibility(View.GONE);
					mTextView.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}

		});
	}

	private class WebContentTask extends AsyncTask<Void, Float, String>
	{
		@Override
		protected String doInBackground(Void... params)
		{
			if (null != mChuanYiItem)
			{
				return getChuanYiHtml();
			}
			else if (null != mCsdnItem)
			{
				return getCsdnHtml();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (null != result)
			{
				mWebView.loadData(result, "text/html; charset=UTF-8", null);
			}

		}
	}

	private String getCsdnHtml()
	{
		String data = null;
		try
		{
			Document document = Jsoup.connect(mCsdnItem.url).timeout(20000).get();
			 
			Elements divTags = document.getElementsByClass("details");
			 if(null != divTags)
			 {
				data = divTags.html();
			 }
			 if(null == data)
			 {
				 data = document.html();
			 }
			data = new String(data.getBytes(), "UTF-8");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	private String getChuanYiHtml()
	{
		String data = null;
		try
		{
			Document document = Jsoup.connect(mChuanYiItem.url).timeout(20000).get();
			Element bodyTag = document.getElementsByTag("body").get(0);
			Elements divTags = bodyTag.getElementsByTag("div");
			if (divTags != null && divTags.size() > 2)
			{
				Element tmp = divTags.get(0);
				Element tmp1 = divTags.get(1);
				tmp.getElementsByTag("img").get(0).attr("src", "http://www.chong4.com.cn/mobile/style/logo.gif");
				tmp1.getElementsByClass("m_more").get(0).html("");
				Elements pTags = tmp1.getElementsByClass("m_content").get(0).getElementsByTag("p");
				for (Element e : pTags)
				{
					if (e.getElementsByTag("a") != null && e.getElementsByTag("a").size() > 0)
					{
						e.getElementsByTag("a").get(0).html("");
					}
					e.attr("style", "text-align:left;");
				}
				data = tmp.html() + tmp1.html();
			}
			data = new String(data.getBytes(), "UTF-8");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

}
