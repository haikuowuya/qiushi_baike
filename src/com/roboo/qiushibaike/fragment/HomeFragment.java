package com.roboo.qiushibaike.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.roboo.qiushibaike.CSDNActivity;
import com.roboo.qiushibaike.CYDBActivity;
import com.roboo.qiushibaike.KJFMActivity;
import com.roboo.qiushibaike.MainActivity;
import com.roboo.qiushibaike.MyIntentService;
import com.roboo.qiushibaike.MyService;
import com.roboo.qiushibaike.R;
import com.roboo.qiushibaike.adapter.GridViewAdapter;

public class HomeFragment extends Fragment implements View.OnClickListener
{
	 
	private GridView mGridView;
	private GridViewAdapter mAdapter;
	private Button mBtnStartService;
	private Button mBtnStopService;
	private Button mBtnBindService;
	private Button mBtnUnbindService;
	private Button mBtnRebindService;
	private Intent mServiceIntent ;
	private Button mBtnFrontService;
	private Button mBtnANRService;
	private Button mBtnNoANRService;
	private boolean isBind = false;
	private ServiceConnection  conn = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			 System.out.println("服务断开连接");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			 System.out.println("服务连接");
			 isBind = true;
		}
	};
	public static HomeFragment newInstance()
	{

		HomeFragment homeFragment = new HomeFragment();

		return homeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_home, null);
		mGridView = (GridView) view.findViewById(R.id.gv_gridview);
		mBtnBindService = (Button) view.findViewById(R.id.btn_bind_service);
		mBtnRebindService = (Button) view.findViewById(R.id.btn_rebind_service);
		mBtnStartService = (Button) view.findViewById(R.id.btn_start_service);
		mBtnStopService = (Button) view.findViewById(R.id.btn_stop_service);
		mBtnUnbindService = (Button) view.findViewById(R.id.btn_unbind_service);
		mBtnFrontService = (Button) view.findViewById(R.id.btn_front_service);
		mBtnANRService = (Button) view.findViewById(R.id.btn_anr_service);
		mBtnNoANRService = (Button) view.findViewById(R.id.btn_no_anr_service);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{		 
		super.onActivityCreated(savedInstanceState);
		 
		mAdapter = new GridViewAdapter(getActivity());
		mServiceIntent = new Intent(getActivity(), MyService.class);
		mGridView.setAdapter(mAdapter);
		mBtnBindService.setOnClickListener(this);
		mBtnRebindService.setOnClickListener(this);
		mBtnStartService.setOnClickListener(this);
		mBtnStopService.setOnClickListener(this);
		mBtnUnbindService.setOnClickListener(this);
		mBtnFrontService.setOnClickListener(this);
		mBtnANRService.setOnClickListener(this);
		mBtnNoANRService.setOnClickListener(this);
		mGridView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = null;
				switch (position)
				{
				case 0://糗事百科
					intent = new Intent(getActivity(),MainActivity.class);			
					break;
				case 1://穿衣打扮
					intent =new Intent(getActivity(),CYDBActivity.class);
					break;
				case 2://csdn
					intent = new Intent(getActivity(),CSDNActivity.class);
					break;
				case 3://科技锋芒
					intent = new Intent(getActivity(),KJFMActivity.class);
				break;
				default:
					break;
				}
				startActivity(intent);
			}

		
		});
	}

 

	@Override
	public void onClick(View v)
	{
		mServiceIntent.putExtra("front", false);
		mServiceIntent.putExtra("anr", false);
		switch (v.getId())
		{
		case R.id.btn_start_service://开启服务
				startService();
			break;
		case R.id.btn_stop_service://停止服务
				stopService();
				break;
		case R.id.btn_bind_service://绑定服务
				bindService();
				break;
		case R.id.btn_unbind_service://解除绑定
			 unbindService();
			 break;
		case R.id.btn_rebind_service://重新绑定服务
			rebindService();
			break;
		case R.id.btn_front_service://前台服务
			frontService();
			break;
		case R.id.btn_anr_service://在继承Service的服务中进行耗时操作
			anrService();
			break;
		case R.id.btn_no_anr_service://在继承IntentService的服务中进行耗时操作
			noAnrService();
		default:
			break;
		}
	}

	private void anrService()
	{
		mServiceIntent.putExtra("anr", true);
		getActivity().startService(mServiceIntent);
	}

	private void noAnrService()
	{
		Intent intent = new Intent(getActivity(),MyIntentService.class);
		getActivity().startService(intent);
	}

	private void frontService()
	{
		mServiceIntent.putExtra("front", true);
		getActivity().startService(mServiceIntent);
		 
	}

	private void rebindService()
	{
		 
	}
	private void unbindService()
	{
		isBind = false;
		getActivity().unbindService(conn);
		
	}
	/**要想绑定服务起作用，onBind方法中的返回值不能为空*/
	private void bindService()
	{	
		getActivity().bindService(mServiceIntent, conn, Service.BIND_AUTO_CREATE);
	}

	private void stopService()
	{
		 getActivity().stopService(mServiceIntent);
	}

	private void startService()
	{
		 
		getActivity().startService(mServiceIntent);
	}
	@Override
	public void onPause()
	{
		 getActivity().stopService(mServiceIntent);
		 if(isBind)
		 {
			 getActivity().unbindService(conn);
		 }
		super.onPause();
	}
	 
}
