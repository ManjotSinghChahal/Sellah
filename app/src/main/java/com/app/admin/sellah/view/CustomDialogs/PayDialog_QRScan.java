package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.activities.QrCodeActivity;
import com.app.admin.sellah.view.fragments.PaymentFragment;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class PayDialog_QRScan extends Dialog {
    String availBal;
    Context context;
    @BindView(R.id.edt_availBal)
    TextView edtAvailBal;
    @BindView(R.id.edt_offered_price)
    EditText edtOfferedPrice;

    ProgressDialog paymentDialog;

    WebService service;
    public PayDialog_QRScan(Context con, String availBal) {
        super(con);
        this.context = con;
        this.availBal = availBal;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PaymentFragment.qr_scan_id="";


        setContentView(R.layout.pay_dialog_qrscan);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

        edtAvailBal.setText("S$"+availBal);

        service = Global.WebServiceConstants.getRetrofitinstance();




    }


    @OnClick({R.id.btn_cancel_qrscan, R.id.btn_pay_qrScan,R.id.qr_code_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_qrscan:
                dismiss();
                break;
            case R.id.btn_pay_qrScan:

                if (edtOfferedPrice.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                }
                else if (PaymentFragment.qr_scan_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(context, "Scan your friend QR Code first", Toast.LENGTH_SHORT).show();
                }


                else {

                    float avail = Float.parseFloat(availBal);
                    float offered = Float.parseFloat(edtOfferedPrice.getText().toString().trim());
                    String val = edtOfferedPrice.getText().toString().trim();
                    if (offered<=0)
                    {
                        Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        paymentDialog = new ProgressDialog(context);
                        paymentDialog.setTitle("Please Wait...");
                        paymentDialog.show();

                        pay_api(edtOfferedPrice.getText().toString().trim());
                    }
                    /*if (avail >= offered) {
                        paymentDialog = new ProgressDialog(context);
                        paymentDialog.setTitle("Please Wait...");
                        paymentDialog.show();

                        pay_api(edtOfferedPrice.getText().toString().trim());
                    } else {
                        Toast.makeText(context, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    }*/

                }
                break;


            case R.id.qr_code_black:
                context.startActivity(new Intent(context,QrCodeActivity.class));

                break;
        }
    }


    public void pay_api(String amount) {


        Call<JsonObject> call = service.pay_money_scan(HelperPreferences.get(context).getString(UID),PaymentFragment.qr_scan_id,amount,"sgd",PaymentFragment.card_id, PaymentFragment.customer_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                dismiss();

                if (paymentDialog!=null  && paymentDialog.isShowing())
                    paymentDialog.dismiss();

                if (response.isSuccessful())
                {


                try {
                    JSONObject obj = new JSONObject(response.body().toString());
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(context, obj.getString("message") +"", Toast.LENGTH_SHORT).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("printError_",e.getMessage());
                }
                }
                else
                {
                    try {
                        Log.e("_printError",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (paymentDialog!=null  && paymentDialog.isShowing())
                    paymentDialog.dismiss();

                 Log.e("printError",t.getMessage());



            }
        });

    }





}
