package com.adolf.shrewmouses;
/**
 * Project Name: com.zh.android
 * Package Name: com.zh.android
 * File Name: MyApplication.java
 * Create Time: 2014楠烇拷閺堬拷5閺冿拷娑撳﹤�?0:07:48
 * Copyright: Copyright(c) 2012 ailk cuc app group.
 */

import java.io.File;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MyApplication extends Application {

	private static MyApplication instance;
	private SharedPreferences appPreferences;
	

	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		appPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
	}
}
