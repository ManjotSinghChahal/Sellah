package com.app.admin.sellah.controller.stripe;

import android.util.Log;

import com.app.admin.sellah.BuildConfig;

public class AppLog {
	
	private static boolean DEBUG = BuildConfig.DEBUG;
	
	private static final String APP_TAG = "StripeConnect";

	public static void d(String clazz, String method, String message) {
		if(DEBUG)
			Log.d(APP_TAG, clazz + "." + method + "(): " + message);
	}
	
	public static void i(String clazz, String method, String message) {
		if(DEBUG)
			Log.i(APP_TAG, clazz + "." + method + "(): " + message);
	}
	
	public static void e(String clazz, String method, String message) {
		if(DEBUG)
			Log.e(APP_TAG, clazz + "." + method + "(): " + message);
	}
	
	public static void w(String clazz, String method, String message) {
		if(DEBUG)
			Log.w(APP_TAG, clazz + "." + method + "(): " + message);
	}	
	
}
