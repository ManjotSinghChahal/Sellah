package com.app.admin.sellah.controller.utils;

import android.content.Context;
import android.util.Log;

import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.Device;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.controller.utils.DeviceUtility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRegistaration {

    public void registerDevice(Context context,String userID){

//        int user_id=HelperPreferences.get(context).getInt(UID);
        String user_id=userID;
        Device device = DeviceUtility.getInstance(context).createNewDevice();
        WebService service=Global.WebServiceConstants.getRetrofitinstance();

        Log.e("Device_Registration",device.getAndroidId()+" : "+device.getBrand()+" : "+device.getTimeZone());
        Call<Common> deviceRegiterCall= service.deviceTockenUpdateapi(user_id,device.getAndroidToken(),device.getDevice_type(),device.getTimeZone());

        deviceRegiterCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if(response.isSuccessful()){
                    Log.e("DeviceRegister","Success :"+response.body().toString());
                }else{
                    try {
                        Log.e("DeviceRegister","Un-Success :"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                Log.e("DeviceRegister","faild :"+t.getMessage());
            }
        });


    }

}
