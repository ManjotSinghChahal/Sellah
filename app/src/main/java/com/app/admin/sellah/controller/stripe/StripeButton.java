package com.app.admin.sellah.controller.stripe;

import com.app.admin.sellah.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class StripeButton extends android.support.v7.widget.AppCompatTextView {
	
	private StripeApp mStripeApp;
	private Context mContext;
	private StripeConnectListener mStripeConnectListener;
	private StripeApp.CONNECT_MODE mConnectMode = StripeApp.CONNECT_MODE.DIALOG;

	public StripeButton(Context context) {
		super(context);
		mContext = context;
		setupButton();
	}

	public StripeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setupButton();
	}

	public StripeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupButton();
	}

	private void setupButton() {
		
		if(mStripeApp == null) {
			setButtonText(R.string.btnConnectText);
		}
		
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		
		setClickable(true);
//		setBackgroundResource(R.color.coloCreamyWhite);
		/*Drawable img = getContext().getResources().getDrawable(R.drawable.send_48dp);
		img.setBounds( 0, 0, dpToPx(32), dpToPx(32) );
		setCompoundDrawables(img, null, null, null);
		*/
		setPadding(3,0,3,0);
		setGravity(Gravity.CENTER);
		setTextColor(getResources().getColor(R.color.colorRed));
//		setTextSize(mContext.getResources().getDimension(R.dimen._11sdp));
//		setTypeface(Typeface.DEFAULT_BOLD);
		
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(mStripeApp == null) {
					Toast.makeText(mContext, 
							"StripeApp obect needed. Call StripeButton.setStripeApp()", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mStripeApp.isConnected()) {


                    if(mConnectMode == StripeApp.CONNECT_MODE.DIALOG) {
                        mStripeApp.displayDialog();
                    }
                    else {
                        Activity parent = (Activity) mContext;
                        Intent i = new Intent(getContext(), StripeActivity.class);
                        i.putExtra("url", mStripeApp.getAuthUrl());
                        i.putExtra("callbackUrl", mStripeApp.getCallbackUrl());
                        i.putExtra("tokenUrl", mStripeApp.getTokenUrl());
                        i.putExtra("secretKey", mStripeApp.getSecretKey());
                        i.putExtra("accountName", mStripeApp.getAccountName());
                        parent.startActivityForResult(i, StripeApp.STRIPE_CONNECT_REQUEST_CODE);
                    }

					/*final AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(
						getResources().getString(R.string.dialog_action_continue))
						.setCancelable(false)
						.setPositiveButton(getResources().getString(R.string.dialog_action_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									mStripeApp.resetAccessToken();
									setButtonText(R.string.disconnect_call);
									mStripeApp.getOAuthAuthenticationListener().onSuccess();
								}
							})
						.setNegativeButton(getResources().getString(R.string.dialog_action_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					final AlertDialog alert = builder.create();
					alert.show();	*/
				}
				else {
					if(mConnectMode == StripeApp.CONNECT_MODE.DIALOG) {
						mStripeApp.displayDialog();
					}
					else {
						Activity parent = (Activity) mContext;
						Intent i = new Intent(getContext(), StripeActivity.class);
						i.putExtra("url", mStripeApp.getAuthUrl());
						i.putExtra("callbackUrl", mStripeApp.getCallbackUrl());
						i.putExtra("tokenUrl", mStripeApp.getTokenUrl());
						i.putExtra("secretKey", mStripeApp.getSecretKey());
						i.putExtra("accountName", mStripeApp.getAccountName());
						parent.startActivityForResult(i, StripeApp.STRIPE_CONNECT_REQUEST_CODE);
					}
				}

			}
			
		});
		
	}
	
	private void setButtonText(int resourceId) {
		setText(resourceId);
	}
	
	/**
	 * 
	 * @param connectMode
	 */
	public void setConnectMode(StripeApp.CONNECT_MODE connectMode) {
		mConnectMode = connectMode;
	}
	
	/**
	 * 
	 * @param stripeApp
	 */
	public void setStripeApp(StripeApp stripeApp) {
		mStripeApp = stripeApp;
		mStripeApp.setListener(getOAuthAuthenticationListener());
		
		if(mStripeApp.isConnected()) {
			setButtonText(R.string.btnDisconnectText);
//			setClickable(true);
		}
		else {
			setButtonText(R.string.btnConnectText);
//			setClickable(false);
		}
	}
	

	/**
	 * 
	 * @param stripeConnectListener
	 */
	public void addStripeConnectListener(StripeConnectListener stripeConnectListener) {
		mStripeConnectListener = stripeConnectListener;
		if(mStripeApp != null) {
			mStripeApp.setListener(getOAuthAuthenticationListener());
		}
	}
	
	private StripeApp.OAuthAuthenticationListener getOAuthAuthenticationListener() {
		
		return new StripeApp.OAuthAuthenticationListener() {

			@Override
			public void onSuccess() {
				Log.d("StripeButton", "Calling OAuthAuthenticationListener.onSuccess()");
				if(mStripeConnectListener != null) {
					if(mStripeApp.isConnected()) {
						Log.d("StripeButton", "Connected");
						setButtonText(R.string.btnDisconnectText);
						Log.d("StripeButton", "Calling mStripeConnectListener.onConnected()");
						mStripeConnectListener.onConnected();
					}
					else {
						Log.d("StripeButton", "Disconnected");
						Log.d("StripeButton", "Calling mStripeConnectListener.onDisconnected()");
						setButtonText(R.string.btnConnectText);
						mStripeConnectListener.onDisconnected();
					}
				}
				else {
					Log.d("StripeButton", "mStripeConnectListener is null");
				}
			}

			@Override
			public void onFail(String error) {
				Log.i("StripeButton", "Calling OAuthAuthenticationListener.onFail()");
				if(mStripeConnectListener != null) {
					mStripeConnectListener.onError(error);
				}
			}
		};
	}
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}

	public int pxToDp(int px) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}

}
