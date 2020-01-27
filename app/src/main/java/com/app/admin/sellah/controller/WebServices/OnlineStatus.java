package com.app.admin.sellah.controller.WebServices;

import android.util.Log;

import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.controller.utils.Global;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineStatus {

    WebService service;

    public OnlineStatus() {
        service = Global.WebServiceConstants.getRetrofitinstance();
    }

    public void changeOnlineStatus(String user_id, String status) {

        Call<Common> categoriesModelCall = service.changeOnlineStatusApi(user_id,status);
        categoriesModelCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    Log.e("Online_status", "Success : " +status);
                    /*if (response.body().getStatus().equalsIgnoreCase("1")) {

                    }*/
                } else {

                    try {
                        Log.e("Online_status", "un-Success : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {

                Log.e("Online_status", "failure : " + t.getMessage());
            }
        });
    }
}
