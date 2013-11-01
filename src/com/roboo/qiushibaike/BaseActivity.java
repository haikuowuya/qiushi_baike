package com.roboo.qiushibaike;

import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateCrtKeySpec;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 
	}
	

}
