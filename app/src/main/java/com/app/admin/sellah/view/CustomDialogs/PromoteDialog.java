package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.GetCardDetailModel.GetCardDetailModel;
import com.app.admin.sellah.model.extra.PromotePackages.PackagesList;
import com.app.admin.sellah.model.extra.PromotePackages.PromotePackageModel;
import com.app.admin.sellah.view.adapter.PromoteOfferAdapter;
import com.app.admin.sellah.view.fragments.ShowCreditCardDetailFragment;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class PromoteDialog extends AlertDialog implements PaymentDialog.PaymentCallBack {

    @BindView(R.id.img_tick_1)
    ImageView imgTick1;
    @BindView(R.id.rv_offer_list)
    RecyclerView rvOfferList;

    Context context;
    Button btnProceed;

    String CardNumber, Cvc, Currency;
    Integer ExpMonth, ExpYear;
    public static final String PUBLISHABLE_KEY = "pk_test_3eq8eJ4CcA0kgn0JN9AG0fHQ";

    String cardNumberStr;

    CardMultilineWidget cardMultilineWidget;
    CreditCardView creditCardView;
    EditText edtCvv;
    Button btnCancel;
    @BindView(R.id.pb_root)
    ProgressBar pbRoot;
    @BindView(R.id.sv_root)
    ScrollView svRoot;
    @BindView(R.id.cd_root)
    LinearLayout cdRoot;
    @BindView(R.id.btn_promote_cancel)
    ImageView btnPromoteCancel;
    @BindView(R.id.promote_btn)
    Button promoteBtn;
    @BindView(R.id.promote_laterbtn)
    Button promoteLaterbtn;
    private BottomSheetDialog bottomSheetDialog;
    WebService webService;
    private Dialog dialog;
    private boolean isCardBack;
    private LinearLayout liCardDetail;
    private LinearLayout liCardDetailError;
    private ProgressBar pbLoading;
    private ViewGroup transitionsContainer;
    PromoteCallback callback;
    String productId = "";
    String selected_id="";

    public static String promote_selected_id="";

    public PromoteDialog(Context context, String productId, PromoteCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.productId = productId;
    }

    public static PromoteDialog create(Context context, String productId, PromoteCallback callback) {
        return new PromoteDialog(context, productId, callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);
        setContentView(R.layout.layout_promote_product);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);

        getPromotePackages();
/*
        ArrayList<String> priceList = new ArrayList<>();
        priceList.add("S$ 10");
        priceList.add("S$ 15");
        priceList.add("S$ 45");
        ArrayList<String> noOfClick = new ArrayList<>();
        noOfClick.add("200 clicks");
        noOfClick.add("500 clicks");
        noOfClick.add("1000 clicks");*/

        /*bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setContentView(R.layout.layout_payment_bottom);
        cardMultilineWidget = bottomSheetDialog.findViewById(R.id.card_container);
        creditCardView = bottomSheetDialog.findViewById(R.id.credit_card_view);
        edtCvv = bottomSheetDialog.findViewById(R.id.edt_cvv);
        btnCancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
        liCardDetail = bottomSheetDialog.findViewById(R.id.li_card_detail);
        liCardDetailError = bottomSheetDialog.findViewById(R.id.li_card_detail_error);
        pbLoading = bottomSheetDialog.findViewById(R.id.pb_loading);
        transitionsContainer =  bottomSheetDialog.findViewById(R.id.li_bottom_root);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transitionsContainer);
        }
        isCardBack = false;
      *//*  String name = "Android Developer";
        String cvv = "522";
        String expiry = "01/27";
        String cardNumber = "4242424242424242";

//        creditCardView.setCVV(cvv);
        creditCardView.setCardHolderName(name);
        creditCardView.setCardExpiry(expiry);
        creditCardView.setCardNumber(cardNumber);
*//*
        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCardBack) {
                    creditCardView.showBack();
                    isCardBack = true;
                } else {
                    creditCardView.showFront();
                    isCardBack = false;
                }
            }
        });
        edtCvv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isCardBack) {
                    creditCardView.showBack();
                    isCardBack = true;
                }

                return false;

            }
        });
        edtCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                creditCardView.setCVV(s.toString());
                if (s.toString().length() == 3) {
                    creditCardView.showFront();
                    isCardBack = false;
                    Global.hideKeyboard(edtCvv, context);
                } else {
                    isCardBack = true;
                    if (!isCardBack) {
                        creditCardView.showBack();
                        isCardBack = true;
                    }
//                    creditCardView.showBack();
                }

            }
        });

        btnProceed = bottomSheetDialog.findViewById(R.id.btn_proceed);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*//*                if (cardMultilineWidget.validateAllFields()) {

                    dialog.show();

                    int expMonth = cardMultilineWidget.getCard().getExpMonth();
                    int expYear = cardMultilineWidget.getCard().getExpYear();
                    String cvc = cardMultilineWidget.getCard().getCVC();
                    String acc = cardMultilineWidget.getCard().getNumber();
                    StripeCreditCard(acc, expMonth, expYear, cvc);


                }  *//*
                if (!creditCardView.getCVV().equalsIgnoreCase("")) {

                    dialog.show();

                    int expMonth = 01;
                    int expYear = 27;
                    String cvc = creditCardView.getCVV();
                    String acc = creditCardView.getCardNumber();
                    StripeCreditCard(acc, expMonth, expYear, cvc);


                }
            }
        });*/


    }

    private void hideProgress(List<PackagesList> packagesList) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                svRoot.setVisibility(View.VISIBLE);
                pbRoot.setVisibility(GONE);

                PromoteOfferAdapter adapter = new PromoteOfferAdapter(context, packagesList, (id) -> {

                    selected_id = id;


         /*   bottomSheetDialog.show();
            getCardApi();*/
                });

                LinearLayoutManager horizontalLayoutManager1 = new GridLayoutManager(context, 2);
                rvOfferList.setLayoutManager(horizontalLayoutManager1);
                rvOfferList.setAdapter(adapter);
                Global.getTotalHeightofLinearRecyclerView(context, rvOfferList, R.layout.layout_promote_offer_list_design, 0);
            }

        }, 1000);
    }

    @OnClick({R.id.btn_promote_cancel, R.id.promote_btn, R.id.promote_laterbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_promote_cancel:
                dismiss();
                break;
            case R.id.promote_btn:

                if (selected_id.equals(""))
                {
                    Toast.makeText(context, "Please select a promote plan.", Toast.LENGTH_SHORT).show();
                }
                else
                {



                    ShowCreditCardDetailFragment.paymentCallBack(context,this,productId, selected_id);
                 //   ShowCreditCardDetailFragment.paymentCallBack(context,this,productId, promote_selected_id);
                    dismiss();

                    /*Intent intent = new Intent(context,ShowCreditCardDetailFragment.class);
                    context.startActivity(intent);*/
                   /*PaymentDialog.create(context, "", HelperPreferences.get(context).getString(UID), productId, selected_id, "", new PaymentDialog.PaymentCallBack() {
                        @Override
                        public void onPaymentSuccess() {

                            dismiss();
                        }

                        @Override
                        public void onPaymentFail(String message) {

                        }

                        @Override
                        public void onCancelDialog() {

                        }
                    }).show();*/

                }


                break;
            case R.id.promote_laterbtn:
                dismiss();
                break;
        }
    }

    @Override
    public void onPaymentSuccess() {

        callback.onPromoteSuccess();
        Toast.makeText(context, "Product promoted successfully.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentFail(String message) {

        callback.onPromoteFailure();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancelDialog() {

        Log.e( "onCancelDialog: ", "cancel");

    }

   /* public void StripeCreditCard(String acc, int month, int year, String cvc) {
        CardNumber = acc;
        ExpMonth = month;
        ExpYear = year;
        Cvc = cvc;
        //  Currency = "inr";
        //   Currency = "cad";
        Card card = new Card(
                CardNumber,
                ExpMonth,
                ExpYear,
                Cvc);
        //   card.setCurrency(Currency);

        boolean validation = card.validateCard();
        if (validation) {

            Stripe stripe = new Stripe(context, PUBLISHABLE_KEY);
            stripe.createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {

                            Log.e("tokengetHere", "t  " + token.getId() + "");


                            stripeApiHit(token.getId(), dialog);


                        }

                        public void onError(Exception error) {
                            Toast.makeText(context, error.getLocalizedMessage() + "", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else if (!card.validateNumber()) {

            Toast.makeText(context, "The card number that you entered is invalid", Toast.LENGTH_SHORT).show();
        } else if (!card.validateExpiryDate()) {

            Toast.makeText(context, "The expiration date that you entered is invalid", Toast.LENGTH_SHORT).show();
        } else if (!card.validateCVC()) {

            Toast.makeText(context, "The CVC code that you entered is invalid", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, "The card details that you entered are invalid", Toast.LENGTH_SHORT).show();
        }

    }*/

   /* private void stripeApiHit(String token, Dialog dialog) {


        Call<JsonObject> stripePaymentApi = webService.stripePayment(HelperPreferences.get(context).getString(UID), "123", "50", "usd", token,"","");
        stripePaymentApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();

                }
                dismiss();


                if (response.isSuccessful()) {
                    callback.onPromoteSuccess();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    callback.onPromoteFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                callback.onPromoteFailure();
            }
        });

    }*/

    public interface PromoteCallback {
        void onPromoteSuccess();


    void onPromoteFailure();
}
    private void getCardApi() {

     /*   new ApisHelper().getCardApi(context, new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(GetCardDetailModel body) {
                showCCview(body);
            }
            @Override
            public void onGetDataFailure() {
                hideCCview();
            }
        });*/

    }

    private void hideCCview() {
        liCardDetail.setVisibility(View.GONE);
        liCardDetailError.setVisibility(View.VISIBLE);
        btnProceed.setVisibility(View.GONE);
        pbLoading.setVisibility(GONE);

    }


    private void showCCview(GetCardDetailModel body) {

        pbLoading.setVisibility(GONE);
        liCardDetail.setVisibility(View.VISIBLE);
        liCardDetailError.setVisibility(GONE);
        btnProceed.setVisibility(View.VISIBLE);
//        creditCardView.setCVV(cvv);
        try {
            creditCardView.setCardHolderName(body.getRecord().get(body.getRecord().size() - 1).getCardHolderName());
            creditCardView.setCardExpiry(body.getRecord().get(body.getRecord().size() - 1).getExpDate());
            creditCardView.setCardNumber(body.getRecord().get(body.getRecord().size() - 1).getCardNumber());
        } catch (Exception e) {
            Log.e("CardDataSetupException", e.getMessage());
        }
    }

    private void getPromotePackages() {
        Call<PromotePackageModel> promotePackageCall = webService.getPromotePackagesApi(HelperPreferences.get(context).getString(UID));
        promotePackageCall.enqueue(new Callback<PromotePackageModel>() {
            @Override
            public void onResponse(Call<PromotePackageModel> call, Response<PromotePackageModel> response) {
                if (response.isSuccessful()) {
                    Log.e("PromoteProductApi", "onResponse: success" + response.body().getStatus());
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        hideProgress(response.body().getPackagesList());
                    } else {
                        Toast.makeText(context, "Unable to get promote packages at this movement.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                } else {
                    Toast.makeText(context, "Unable to get promote packages at this movement.", Toast.LENGTH_SHORT).show();
                    dismiss();
                    try {
                        Log.e("PromoteProductApi_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PromotePackageModel> call, Throwable t) {
                Toast.makeText(context, "Something went's wrong.", Toast.LENGTH_SHORT).show();
                dismiss();
                Log.e("PromoteProductApi", "onFailure: " + t.getMessage());
            }
        });
    }
}
