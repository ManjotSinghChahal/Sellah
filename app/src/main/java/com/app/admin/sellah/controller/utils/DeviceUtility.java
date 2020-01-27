package com.app.admin.sellah.controller.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;


import com.app.admin.sellah.model.extra.Device;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.TimeZone;

/**
 * Created by dell on 15/3/18.
 */

public class DeviceUtility {

    private Context mContext;

    //create instance of DeviceUtility class
    private static DeviceUtility instance;

    private String mBrand;

    private String mModel;

    private String mAndroidId;

    private String mAndroidToken;

    private String mAndroidTimeZone;

    private DeviceUtility(Context context) {
        mContext = context;
        getValues();
    }

    private void getValues() {
        mBrand = Build.BRAND;
        mModel = Build.MODEL;
        mAndroidId = generateAndroidID();
        mAndroidToken=generateAndroidToken();
        mAndroidTimeZone=generateAndroidTimeZone();
    }

    public static DeviceUtility getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceUtility(context);
        }
        return instance;
    }

    public String getAndroidId() {
        return mAndroidId;
    }

    private String generateAndroidID() {
        try {
            return Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    private String generateAndroidToken() {
        try {
            Log.e("Device_token", FirebaseInstanceId.getInstance().getToken());
            return FirebaseInstanceId.getInstance().getToken();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    private String generateAndroidTimeZone() {
        try {
            Log.e("Device_time_zone", TimeZone.getDefault().getID());
            return TimeZone.getDefault().getID();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public Device createNewDevice() {
        Device device = new Device();
        device.setBrand(mBrand);
        device.setModel(mModel);
        device.setTimeZone(TimeZone.getDefault().getDisplayName());
        device.setAndroidId(mAndroidId);
        device.setTimeZone(mAndroidTimeZone);
        device.setAndroidToken(mAndroidToken);
        device.setDevice_type("A");
        return device;
    }


    /*public static Drawable getDevicesRoleBackground(Context context, int role) {
        switch (role) {
            case RolesInt.ADMIN:
                return ContextCompat.getDrawable(context, R.drawable.background_circle_green);

            case RolesInt.READONLY:
                return ContextCompat.getDrawable(context, R.drawable.background_circle_yellow);

            case RolesInt.LIMITED:
                return ContextCompat.getDrawable(context, R.drawable.background_circle_orange);

            default:
                return ContextCompat.getDrawable(context, R.drawable.background_circle_green);
        }
    }*/


}
