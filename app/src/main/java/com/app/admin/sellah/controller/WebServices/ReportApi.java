package com.app.admin.sellah.controller.WebServices;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ReportApi {

    //    ReportCallback callback;
    public void reportItemApi(Context context, String reason, String otherUserId, String itemProductId, ReportCallback callback,ReportErrorCallBack errorCallBack) {

        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Call<Common> reportCall = service.reportApi(HelperPreferences.get(context).getString(UID), reason, itemProductId, otherUserId);
        reportCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {

                    callback.onReportSubmit(response.body().getMessage());
                }else{
                    errorCallBack.onReportError();

                }
            }
            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                errorCallBack.onReportError();

            }
        });
    }
    public void hitReportApi(Context context,LinearLayout layout, String otherUserId, String itemProductId, ReportCallback callback,ReportErrorCallBack errorCallBack) {
        reportItemApi(context, getTextFromView(layout)
                , otherUserId, itemProductId,callback, errorCallBack);
    }

    private String getTextFromView(LinearLayout layout) {
        String value = "";
        for (int i = 0, count = layout.getChildCount(); i < count; ++i) {
            View view = layout.getChildAt(i);
            if (view instanceof TextView) {
                value = ((TextView) view).getText().toString().trim();
            } else {
                value = "";
            }
        }
        return value;
    }
    public interface ReportCallback {
        void onReportSubmit(String msg);
    }
    public interface ReportErrorCallBack{
        void onReportError();
    }

}
