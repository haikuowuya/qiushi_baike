package com.roboo.qiushibaike;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class MyService extends Service
{

	 
	@Override
	public IBinder onBind(Intent intent)
	{
		System.out.println("服务绑定");
//		return null;
		return new Binder();
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		System.out.println("服务创建");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		System.out.println("服务开启");
		if(intent.getBooleanExtra("front",false))
		{
			createNotification();
		}
		if(intent.getBooleanExtra("anr", false))
		{
			try
			{
				Thread.sleep(20000);
				System.out.println("在Service中睡眠20秒钟");
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private void createNotification()
	{
		Notification notification  = new NotificationCompat.Builder(getApplicationContext()).build();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText ="这是一个前台服务";
		notification.flags |= Notification.FLAG_ONGOING_EVENT;//前台通知具有onGoing标志
		notification.when = System.currentTimeMillis();
		notification.contentView = new RemoteViews(getPackageName(), R.layout.notification);
		 notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0	, new Intent(this,CYDBActivity.class), 0);
		 startForeground(1, notification);
		 
	}
	@Override
	public void onDestroy()
	{
		System.out.println("服务销毁");
		super.onDestroy();
	}
	@Override
	public boolean onUnbind(Intent intent)
	{
		System.out.println("服务解除绑定");
		return true;
	}
	@Override
	public void onRebind(Intent intent)
	{
		 System.out.println("服务重新绑定");
		super.onRebind(intent);
	}
}
