package com.app.admin.sellah.view.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.adapter.CardListAdapter;
import com.app.admin.sellah.view.fragments.AddCreditCardDetailFragment;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class PaymentDialog extends BottomSheetDialog {

    @BindView(R.id.card_container)
    CardMultilineWidget cardContainer;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.credit_card_view)
    CreditCardView creditCardView;
    @BindView(R.id.edt_cvv)
    EditText edtCvv;
    @BindView(R.id.cv_pin)
    CardView cvPin;
    @BindView(R.id.li_card_detail)
    LinearLayout liCardDetail;
    @BindView(R.id.li_card_detail_error)
    LinearLayout liCardDetailError;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_proceed)
    Button btnProceed;
    @BindView(R.id.li_bottom_root)
    LinearLayout liBottomRoot;
    @BindView(R.id.txt_edit_card)
    TextView txtEditCard;
    @BindView(R.id.tabs1)
    TabLayout tabLayout;
    @BindView(R.id.rec_cards)
    RecyclerView recCards;
    @BindView(R.id.txt_card_number)
    TextView txtCardNumber;
    @BindView(R.id.li_card_list)
    LinearLayout liCardList;
    @BindView(R.id.pager)
    ViewPager pager;
    private Context context;
    private boolean isCardBack;
    private Dialog dialog;
    String CardNumber, Cvc, Currency;
    Integer ExpMonth, ExpYear;
    String offerId, sellerId;
    PaymentCallBack callBack;
    String productId;
    private boolean isAdded;
    List<com.app.admin.sellah.model.extra.CardDetails.Card> cardList;
    private String cardHolderName = "";
    private String cardNumber = "";
    private String cardExp = "";
    private int cardExpMonth = 00;
    private int cardExpYear = 0000;
    String cardId = "";
    String customerId = "";
    int selectedCardPos = 0;
    String packageId="";
    String promoteId="";
    String price;


    public PaymentDialog(Context context,String price, String offerId, String sellerId, String productId, String packageId, String promoteId, PaymentCallBack callBack) {
        super(context, R.style.CustomDialog);
        this.price = price;
        this.context = context;
        dialog = S_Dialogs.getLoadingDialog(context);
        this.sellerId = sellerId;
        this.offerId = offerId;
        this.callBack = callBack;
        this.productId = productId;
        this.packageId=packageId;
        this.promoteId=promoteId;
        this.cardList = new ArrayList<>();
        Log.e( "PaymentDialog: ","comnd" );

    }

    public static PaymentDialog create(Context context,String price, String offerId, String sellerId, String productId,String packageId,String promoteId, PaymentCallBack callBack) {
        return new PaymentDialog(context,price, offerId, sellerId, productId,packageId,promoteId, callBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_payment_bottom);
        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);

        BottomSheetBehavior.from(bottomSheet)
                .setState(BottomSheetBehavior.STATE_EXPANDED);
        BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
        BottomSheetBehavior.from(bottomSheet).setHideable(false);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        ButterKnife.bind(this);
        isCardBack = false;
        getCardApi();

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

    }


    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCardApi();
        }
    };



    @OnClick({R.id.card_container, R.id.pb_loading, R.id.credit_card_view, R.id.edt_cvv, R.id.cv_pin, R.id.li_card_detail, R.id.li_card_detail_error, R.id.btn_cancel})
    public void onViewClicked(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(cardContainer);
        }
        switch (view.getId()) {
            case R.id.card_container:
                break;
            case R.id.pb_loading:
                break;
            case R.id.credit_card_view:
                if (!isCardBack) {
                    creditCardView.showBack();
                    isCardBack = true;
                } else {
                    creditCardView.showFront();
                    isCardBack = false;
                }
                break;
            case R.id.edt_cvv:
                break;
            case R.id.cv_pin:
                break;
            case R.id.li_card_detail:
                break;
            case R.id.li_card_detail_error:
                break;
            case R.id.btn_cancel:
                dismiss();
                callBack.onCancelDialog();
                break;
        }
    }

    @OnClick(R.id.btn_proceed)
    public void onViewClicked() {
//        if (!creditCardView.getCVV().equalsIgnoreCase("")) {

//        dialog.show();
//        List<String> items = Arrays.asList(creditCardView.getExpiry().split("\\s*/\\s*"));
//        Log.e("List", "updateCreditCardApi: "+items.size());
//        String expMonth="",expYear="";
       /* int expMonth = 01;
        int expYear = 2019;
        for(int i=0;i<items.size();i++){
            if(i==0){
                expMonth= Integer.parseInt(items.get(i));
            }if(i==1){
                expYear= Integer.parseInt(items.get(i));
            }
        }
            String cvc = creditCardView.getCVV();
            String acc = creditCardView.getCardNumber();*/

        StripeCreditCard();
//        }
    }

    public void StripeCreditCard() {
       /* CardNumber = acc;
        ExpMonth = month;
        ExpYear = year;
        Cvc = cvc;
        Card card = new Card(
                CardNumber,
                ExpMonth,
                ExpYear,
                Cvc);
        //   card.setCurrency(Currency);
//        boolean validation = card.validateCard();

        if (!card.validateNumber()) {
            callBack.onPaymentFail("Invalid card number.");
        } else if (!card.validateExpiryDate()) {
            callBack.onPaymentFail("Invalid expiry date.");
        } else if (!card.validateCVC()) {
            callBack.onPaymentFail("Invalid CVV code");
        } else {
//            if (validation) {
            Stripe stripe = new Stripe(context, PUBLISHABLE_KEY);
            stripe.createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            Log.e("tokengetHere", "t  " + token.getId() + "");
//                            stripeApiHit(token.getId(), dialog);
                        }

                        public void onError(Exception error) {
                            callBack.onPaymentFail(error.getLocalizedMessage());
                        }
                    });
        }*/
       if(!TextUtils.isEmpty(packageId)){
           promoteProductApi(cardId,packageId,dialog);

       }else{

           Log.e( "user: 1","s"+ HelperPreferences.get(context).getString(UID));
           Log.e( "prdo: 1","s"+productId);
           Log.e( "toke: 1","s"+cardId);
           Log.e( "offer: 1","s"+offerId);
           Log.e( "seller: 1","s"+sellerId);
           Log.e( "customer: 1","s"+customerId);
           Log.e( "price: 1","s"+price);
           stripeApiHit(cardId, dialog);
       }

//        }

    }

    private void stripeApiHit(String token, Dialog dialog) {
        dialog.show();
        WebService webService = Global.WebServiceConstants.getRetrofitinstance();




        Call<JsonObject> stripePaymentApi = webService.stripePayment(HelperPreferences.get(context).getString(UID), productId, price, "sgd", token, offerId, sellerId, customerId,"Y");
        stripePaymentApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("paymentException", "onResponse: " + e.getMessage());
                    }
                    callBack.onPaymentSuccess();
                } else {


                    try {
                        Log.e("errorBodyRes",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callBack.onPaymentFail("Unable to make payment against this product");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                Log.e("payment", "onFailure: " + t.getMessage());
                callBack.onPaymentFail(t.getMessage());
            }
        });

    }

    private void promoteProductApi(String cardId,String packageId, Dialog dialog) {
        dialog.show();
        Log.e("PaymentDialog", "promoteProductApi: "+packageId+" : "+cardId+" : "+customerId+" : "+productId);
        WebService webService = Global.WebServiceConstants.getRetrofitinstance();
        Call<Common> stripePaymentApi = webService.promoteProductApi(HelperPreferences.get(context).getString(UID), productId, packageId,cardId, customerId,promoteId);
        stripePaymentApi.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equalsIgnoreCase("1")){
                        callBack.onPaymentSuccess();
                        dismiss();
                        Log.e("PaymentDialog", "promoteProductApi: success");

                    }else{
                        dismiss();
                        callBack.onPaymentFail(response.body().getMessage());
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        callBack.onPaymentFail(message);
                        Log.e("PaymentDialog", "promoteProductApi: failed"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                Log.e("payment", "onFailure: " + t.getMessage());
                callBack.onPaymentFail(t.getMessage());
            }
        });

    }

    public  void getCardApi() {

//        showCCview();

        new ApisHelper().getCardApi(context, new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {

                Gson gson = new GsonBuilder().create();
                Log.e( "onGetDataSuccess: ", gson.toJson(body.getCards()));
                setTabs();
                cardList.clear();
                cardList.addAll(body.getCards());
                setUpcards(body.getCards());
            }

            @Override
            public void onGetDataFailure() {
                hideCCview();
            }
        });

       /* new ApisHelper().getCardApi(context, new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {
                setTabs();
                setUpcards(body.getCards());
                showCCview();
            }

            @Override
            public void onGetDataFailure() {
                hideCCview();
            }
        });*/

    }

    private void hideCCview() {
        isAdded = false;
        txtEditCard.setText("+ Add Card");
        tabLayout.setVisibility(View.GONE);
        pager.setVisibility(GONE);
        liCardDetailError.setVisibility(View.VISIBLE);
        btnProceed.setVisibility(View.GONE);
        pbLoading.setVisibility(GONE);
    }


    private void showCCview(/*GetCardDetailModel body*/) {

        isAdded = false;
        txtEditCard.setText("+ Add Card");
        pbLoading.setVisibility(GONE);
        liCardDetail.setVisibility(View.VISIBLE);
        liCardDetailError.setVisibility(GONE);
        btnProceed.setVisibility(View.VISIBLE);
//        creditCardView.setCVV(cvv);
        String cardType;
        try {
            if (cardList != null && cardList.size() > selectedCardPos) {
                cardHolderName = cardList.get(selectedCardPos).getName();
                cardNumber = cardList.get(selectedCardPos).getLast4();
                cardExpMonth = cardList.get(selectedCardPos).getExpMonth();
                cardExpYear = cardList.get(selectedCardPos).getExpYear();
                cardExp = (cardExpMonth + "/" + cardExpYear).toString();
                cardId = cardList.get(selectedCardPos).getId();
                customerId = cardList.get(selectedCardPos).getCustomer();
            }
//            CardSelector.selectCard("4242");
            creditCardView.setCardHolderName(cardHolderName);
            creditCardView.setCardExpiry(cardExp);
            creditCardView.setCardNumber("XXXXXXXXXXXX" + cardNumber);
//            creditCardView.setSelectorLogic(null);

//            creditCardView.paintCard();
        } catch (Exception e) {
            Log.e("CardDataSetupException", e.getMessage());
        }
    }

    @OnClick(R.id.txt_edit_card)
    public void onEditCardClicked() {
       /* if (isAdded) {
            Intent intent = new Intent(context, AddCreditCardDetailFragment.class);
            intent.putExtra(SAConstants.Keys.CARDHOLDER_NAME, creditCardView.getCardHolderName());
            intent.putExtra(SAConstants.Keys.CARD_NUMBER, creditCardView.getCardNumber());
            intent.putExtra(SAConstants.Keys.CARD_EXP_YEAR, creditCardView.getExpiry());
            ((Activity) context).startActivityForResult(intent, 2);
        }else{*/
        Intent intent = new Intent(context, AddCreditCardDetailFragment.class);
          dismiss();
        ((Activity) context).startActivityForResult(intent, 2);
//        }
    }



    private void setTabs() {

        pager.setVisibility(View.VISIBLE);
        PaymentPagerAdapter adapter = new PaymentPagerAdapter();
        pager.setAdapter(adapter);

        tabLayout.setVisibility(View.VISIBLE);
        /*tabLayout.addTab(tabLayout.newTab().setText("Selected Card"));
        tabLayout.addTab(tabLayout.newTab().setText("More Cards"));*/
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).select();

        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(liBottomRoot);
                }
                if (tab.getPosition() == 0) {
                    liCardDetail.setVisibility(View.VISIBLE);
                    recCards.setVisibility(GONE);
                } else {
                    liCardDetail.setVisibility(View.GONE);
                    recCards.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    public interface PaymentCallBack {
        void onPaymentSuccess();

        void onPaymentFail(String message);

        void onCancelDialog();
    }

    private void setUpcards(List<com.app.admin.sellah.model.extra.CardDetails.Card> cards) {


        if (cards != null && cards.size() > 0) {
            showCCview();
            txtCardNumber.setVisibility(View.GONE);
            CardListAdapter cardListAdapter = new CardListAdapter(context, cards, new CardListAdapter.OnCardOptionSelection() {
                @Override
                public void onCardSelectionClick(int pos, String card_id) {
                    selectedCardPos = pos;
                    cardId = card_id;
                    tabLayout.getTabAt(0).select();
                    showCCview();
                }

                @Override
                public void onCardRemoveListner(int pos, int updatedSize) {
                    if(pos>0){
                        selectedCardPos = pos-1;
                        cardId = cards.get(pos-1).getId();
                        tabLayout.getTabAt(0).select();
                    }else{
                        hideCCview();
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recCards.setLayoutManager(layoutManager);
            recCards.setAdapter(cardListAdapter);
            Global.getTotalHeightofLinearRecyclerView(context, recCards, R.layout.card_adapter_layout, 0);

        } else {
            hideCCview();
            txtCardNumber.setText("No card detail available");
            txtCardNumber.setVisibility(View.VISIBLE);
        }
//        recCards.setAnimation(new DefaultItemAnimator());


    }

    class PaymentPagerAdapter extends PagerAdapter {

        public Object instantiateItem(ViewGroup collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.li_card_detail;
                    break;
                case 1:
                    resId = R.id.li_card_list;
                    break;
            }
            return findViewById(resId);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Selected card";
            } else {
                return "More Cards";
            }
//            return super.getPageTitle(position);

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // No super
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        new ApisHelper().cancel_striipe_request();
    }
}

