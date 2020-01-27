package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.Card_VP_adapter;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ShowCreditCardDetailFragment extends AppCompatActivity {


    String lastInput = "";
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.title_sell)
    TextView titleSell;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.btn_Addcard)
    Button btnAddcard;
    @BindView(R.id.selectcard)
    Button selectcard;
    @BindView(R.id.li_bottom_root)
    RelativeLayout liBottomRoot;
    @BindView(R.id.cardpager)
    ViewPager cardpager;
    @BindView(R.id.addcard_beforenotdata)
    Button addcardBeforenotdata;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;
    static PaymentDialog.PaymentCallBack callBack;
    static String packageId;
    static String productId;
    public static String cardId;
    static String customer_id;
    Dialog dialog;
    int count = 0;
    @BindView(R.id.card_buttons_layout)
    LinearLayout cardButtonsLayout;
    private WebService service;

    public static void paymentCallBack(Context context, PaymentDialog.PaymentCallBack call, String produc, String packag) {
        Intent intent = new Intent(context, ShowCreditCardDetailFragment.class);
        context.startActivity(intent);
        callBack = call;
        productId = produc;
        packageId = packag;


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_credit_card_detail);
        ButterKnife.bind(this);
        Global.StatusBarLightMode(this);
        service = Global.WebServiceConstants.getRetrofitinstance();


        dialog = S_Dialogs.getLoadingDialog(this);
        dialog.show();
        new ApisHelper().getCardApi(this, new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }


                if (body.getStatus().equals("1")) {
                    cardpager.setVisibility(View.VISIBLE);
                    llNodata.setVisibility(View.GONE);
                    cardButtonsLayout.setVisibility(View.VISIBLE);
                    setUpcards(body.getCards());
                } else {
                    cardButtonsLayout.setVisibility(View.GONE);
                    cardpager.setVisibility(View.GONE);
                    llNodata.setVisibility(View.VISIBLE);
                }
                //set card list adapter
            }

            @Override
            public void onGetDataFailure() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                cardpager.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();




        new ApisHelper().getCardApi(this, new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {


                if (body.getStatus().equals("1")) {
                    cardpager.setVisibility(View.VISIBLE);
                    llNodata.setVisibility(View.GONE);
                    cardButtonsLayout.setVisibility(View.VISIBLE);
                    setUpcards(body.getCards());
                } else {
                    cardButtonsLayout.setVisibility(View.GONE);
                    cardpager.setVisibility(View.GONE);
                    llNodata.setVisibility(View.VISIBLE);
                }

                setUpcards(body.getCards());//set card list adapter
            }

            @Override
            public void onGetDataFailure() {


                cardpager.setVisibility(View.GONE);
                llNodata.setVisibility(View.VISIBLE);


            }
        });

    }

    @OnClick({R.id.rl_menu, R.id.btn_Addcard, R.id.selectcard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_menu:
                onBackPressed();
                break;
            case R.id.btn_Addcard:

                Intent intent = new Intent(ShowCreditCardDetailFragment.this, AddCreditCardDetailFragment.class);
                startActivity(intent);
                break;
            case R.id.selectcard:

                if (getIntent().hasExtra("payment")) {
                    setdefaultcard(ShowCreditCardDetailFragment.this);
                    Log.e( "onViewClicked: ","payment" );
                } else {
                    StripeCreditCard();

                }


                break;
        }
    }


    public void StripeCreditCard() {


        Log.e( "onViewClicked: ",packageId );
        if (!TextUtils.isEmpty(packageId)) {
            promoteProductApi(cardId, packageId);

        } else {
            stripeApiHit(cardId, dialog);
            callBack.onCancelDialog();
             finish();
        }


    }


    private void stripeApiHit(String token, Dialog dialog) {
        dialog.show();
        WebService webService = Global.WebServiceConstants.getRetrofitinstance();
        Call<JsonObject> stripePaymentApi = webService.stripePayment(HelperPreferences.get(ShowCreditCardDetailFragment.this).getString(UID), productId, "50", "usd", token, "", "", customer_id, "Y");
        stripePaymentApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(ShowCreditCardDetailFragment.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("paymentException", "onResponse: " + e.getMessage());
                    }
                    callBack.onPaymentSuccess();
                    finish();
                } else {
                    callBack.onPaymentFail("Unable to make payment against this product");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                Log.e("payment", "onFailure: " + t.getMessage());
                callBack.onPaymentFail(t.getMessage());
                finish();
            }
        });

    }


    private void promoteProductApi(String cardId, String packageId) {

        Log.e("PaymentDialog", "promoteProductApi: " + packageId + " : " + cardId + " : " + " : " + productId);
        WebService webService = Global.WebServiceConstants.getRetrofitinstance();
        Call<Common> stripePaymentApi = webService.promoteProductApi(HelperPreferences.get(ShowCreditCardDetailFragment.this).getString(UID), productId, packageId, cardId, customer_id, "");
        stripePaymentApi.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        Log.e("PaymentDialog", "promoteProductApi: success");
                        callBack.onPaymentSuccess();
                        onBackPressed();
                    } else {

                        callBack.onPaymentFail(response.body().getMessage());
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        callBack.onPaymentFail(message);
                        Log.e("PaymentDialog", "promoteProductApi: failed" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {


                Log.e("payment", "onFailure: " + t.getMessage());
                callBack.onPaymentFail(t.getMessage());
            }
        });

    }


    private void setUpcards(List<Card> cards) {
        if (cards != null && cards.size() > 0) {
//            txtCardNumber.setText("No card detail available");

            Card_VP_adapter adapter = new Card_VP_adapter(this, cards);
            cardpager.setAdapter(adapter);

            cardId = cards.get(count).getId();
            customer_id = cards.get(count).getCustomer();
            cardpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    count = position;
                    Log.e("onPageScrolled:zz ", "" + position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {

        }

    }


    @OnClick(R.id.addcard_beforenotdata)
    public void onViewClicked() {

        Intent intent = new Intent(ShowCreditCardDetailFragment.this, AddCreditCardDetailFragment.class);
        startActivity(intent);
    }


    public void setdefaultcard(Context context) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Log.e("user: ", HelperPreferences.get(context).getString(UID));
        Log.e("card: ", cardId);
        Call<JsonObject> getProfileCall = service.set_default_card(HelperPreferences.get(context).getString(UID), cardId);
        getProfileCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                Log.e("onGetDataSuccess: ", response.body().toString());
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    finish();

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("Profile_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        new ApisHelper().cancel_striipe_request();
    }
}
