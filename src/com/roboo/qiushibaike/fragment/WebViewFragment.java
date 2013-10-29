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
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.view.RoundProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewFragment extends Fragment
{
	private WebView mWebView;
	private String mURL;
	private static final String ARG_URL = "url"	;
	private RoundProgressBar mRoundProgressBar;
	private ProgressBar mProgressBar;
	private TextView mTextView;

	public static WebViewFragment newInstance(String url)
	{
		Bundle bundle = new Bundle();
		bundle.putString(ARG_URL, url);
		WebViewFragment mainFragment = new WebViewFragment();
		mainFragment.setArguments(bundle);
		return mainFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mURL = getArguments().getString(ARG_URL);
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
		new WebContentTask().execute(mURL);
	}

	private void initWebView()
	{
		this.mWebView.getSettings().setJavaScriptEnabled(true);
		this.mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
		this.mWebView.setInitialScale(100);
		this.mWebView.getSettings().setUseWideViewPort(true);
		this.mWebView.getSettings().setLoadWithOverviewMode(true);
		
	}

	private class WebContentTask extends AsyncTask<String, Float, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			String data = null;
			try
			{			
				Document document = Jsoup.connect(params[0]).timeout(20000).get();
				Element bodyTag = document.getElementsByTag("body").get(0);
				Elements divTags = bodyTag.getElementsByTag("div");
				if(divTags != null && divTags.size() >2)
				{
					Element tmp = divTags.get(0);
					Element tmp1 = divTags.get(1);
					tmp.getElementsByTag("img").get(0).attr("src", "http://www.chong4.com.cn/mobile/style/logo.gif");
					tmp1.getElementsByClass("m_more").get(0).html("");
					Elements pTags = tmp1.getElementsByClass("m_content").get(0).getElementsByTag("p");
					for(Element e:pTags)
					{
						if(e.getElementsByTag("a") != null && e.getElementsByTag("a").size()>0)
						{
							e.getElementsByTag("a").get(0).html("");
						}
						e.attr("style", "text-align:left;");
					}
					data = tmp.html()+ tmp1.html();
				}		
				data = new String(data.getBytes(),"UTF-8");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return data;
		}
		@Override
		protected void onPostExecute(String result)
		{
		 
			if (null != result)
			{
				mProgressBar.setVisibility(View.GONE);
				mTextView.setVisibility(View.GONE);
 				mWebView.loadData(result, "text/html; charset=UTF-8",null);				
			}
		}
	}
}
