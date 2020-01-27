package com.app.admin.sellah.controller.stripe;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.util.Log;

import com.app.admin.sellah.controller.utils.HelperPreferences;

/**
 *
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * 
 */
public class StripeSession {

	private final HelperPreferences helperPreferences;
	//	private SharedPreferences sharedPref;
//	private Editor editor;
	private Context context;

	private static final String SHARED = "_StripeAccount_Preferences";
	public static final String API_ACCESS_TOKEN = "access_token";
	public static final String STRIPE_VERIFIED = "Stripe_accountverifiec";
	public static final String USERCITY = "stripeverifies";
	private static final String API_REFRESH_TOKEN = "refresh_token";
	private static final String API_TOKEN_TYPE = "token_type";
	private static final String API_USER_ID = "user_id";
	private static final String API_PUBLISHABLE_KEY = "publishable_key";
	private static final String API_LIVE_MODE = "live_mode";

	public StripeSession(Context context, String accountName) {
		Log.i("StripeSession", "StripeSession[accountName]:					" + accountName);
//		sharedPref = context.getSharedPreferences(accountName + SHARED, Context.MODE_PRIVATE);
		helperPreferences = HelperPreferences.get(context);
		this.context=context;

	}

	public void storeAccessToken(String accessToken) {
//		editor = sharedPref.edit();
		helperPreferences.saveString(API_ACCESS_TOKEN, accessToken);
//		editor.commit();
	}
	
	public void storeRefreshToken(String refreshToken) {
//		editor = sharedPref.edit();
		helperPreferences.saveString(API_REFRESH_TOKEN, refreshToken);
//		editor.commit();
	}
	
	public void storePublishableKey(String publishableKey) {
//		editor = sharedPref.edit();
		helperPreferences.saveString(API_PUBLISHABLE_KEY, publishableKey);
//		editor.commit();
	}
	
	public void storeUserid(String userId) {
//		editor = sharedPref.edit();
		helperPreferences.saveString(API_USER_ID, userId);
//		editor.commit();
	}	
	
	public void storeTokenType(String tokenType) {
//		editor = sharedPref.edit();
		helperPreferences.saveString(API_TOKEN_TYPE, tokenType);
//		editor.commit();
	}
	
	public void storeLiveMode(boolean liveMode) {
//		editor = sharedPref.edit();
		helperPreferences.saveBoolean(API_LIVE_MODE, liveMode);
//		editor.commit();
	}

	public String getAccessToken() {
		return helperPreferences.getString(API_ACCESS_TOKEN);
	}
	
	public String getRefreshToken() {
		return helperPreferences.getString(API_REFRESH_TOKEN);
	}
	
	public String getPublishableKey() {
		return helperPreferences.getString(API_PUBLISHABLE_KEY);
	}
	
	public String getUserId() {
		return helperPreferences.getString(API_USER_ID);
	}
	
	public String getTokenType() {
		return helperPreferences.getString(API_TOKEN_TYPE);
	}
	
	public Boolean getLiveMode() {
		return helperPreferences.getBoolean(API_LIVE_MODE, false);
	}	

	public void resetAccessToken() {
//		editor = sharedPref.edit();
		helperPreferences.remove(API_ACCESS_TOKEN);
		helperPreferences.remove(API_REFRESH_TOKEN);
		helperPreferences.remove(API_PUBLISHABLE_KEY);
		helperPreferences.remove(API_USER_ID);
		helperPreferences.remove(API_TOKEN_TYPE);
		helperPreferences.remove(API_LIVE_MODE);
//		helperPreferences.clear();
//		helperPreferences.commit();
	}

}