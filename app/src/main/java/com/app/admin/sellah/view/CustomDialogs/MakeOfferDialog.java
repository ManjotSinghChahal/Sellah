package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CheckOutModel;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.adapter.CheckoutProductAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class MakeOfferDialog extends Dialog {

    OfferController controller;
    Context context;
    @BindView(R.id.rec_product)
    RecyclerView recProduct;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_send_offer)
    Button txtSendOffer;
    @BindView(R.id.txt_status_canceled)
    TextView txtStatusCanceled;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.rl_list)
    RelativeLayout rlList;
    @BindView(R.id.li_sendOffer)
    LinearLayout liSendOffer;
    @BindView(R.id.btn_chat_order_cancel)
    ImageView btnChatOrderCancel;
    private WebService webService;
    private Dialog dialog;
    private ArrayList<CheckOutModel> recordList;
    private CheckoutProductAdapter checkoutProductAdapter;
    private String itemQuantity;
    String otherUserId;
    MakeOfferModel makeOfferModel;
    String productName;
    String productId="";

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    protected MakeOfferDialog(Context context, OfferController controller, String otherUserId) {
        super(context);
        this.controller = controller;
        this.context = context;
        this.otherUserId = otherUserId;
    }

    public static MakeOfferDialog create(Context context, OfferController controller, String otherUserId) {
        return new MakeOfferDialog(context, controller, otherUserId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.layout_chat_order_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);

        ButterKnife.bind(this);
        getForsaleList(otherUserId);


    }

    private void checkOutProductList(GetProductList body, ArrayList<HashMap<String, String>> arrayList_) {


        pbLoading.setVisibility(View.GONE);
        recProduct.setVisibility(View.VISIBLE);
        card1.setVisibility(View.GONE);
        checkoutProductAdapter = new CheckoutProductAdapter(body.getResult(),arrayList_, context,txtSendOffer, new CheckoutProductAdapter.ActionCallback() {
            @Override
            public void onCheckclicked(String name, String id, String subtotal, String quantity) {
                txtSubtotal.setText("S$ " + subtotal);
                itemQuantity = quantity;
                productId = id;
                productName = name;
            }

            @Override
            public void onMakeOffer(MakeOfferModel body, String name) {
//                makeOfferModel=body;
//                productName=name;
                controller.onMakeOffer(body, name);
                dismiss();
            }
        });

        LinearLayoutManager birthHorizontalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recProduct.setLayoutManager(birthHorizontalManager);
        recProduct.setAdapter(checkoutProductAdapter);
    }



    @OnClick({R.id.btn_chat_order_cancel, R.id.txt_send_offer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_order_cancel:
                dismiss();
                break;
            case R.id.txt_send_offer:


                // does not execute
                if (productId.equalsIgnoreCase(""))
                {
                    Toast.makeText(context,  "Please select product", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (txtSubtotal.getText().toString().equalsIgnoreCase("S$ 0")) {
                        controller.onErrorSelection();
                    } else {
                        makeOfferApi(productId, txtSubtotal.getText().toString().replace("S$", ""), productName, "1");
                    }
                }


                break;
        }
    }

    public interface OfferController {
        void onMakeOfferButtonClick(MakeOfferModel offerPrice, String itemQuantity);

        void onMakeOffer(MakeOfferModel body, String productName);

        void onErrorSelection();

    }
    Call<GetProductList> recordsCall;
    private void getForsaleList(String otherUserId) {
     Log.e("idGetHere",otherUserId);
        dialog.show();
         recordsCall = webService.getForSalelistApi(otherUserId);
        recordsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Gson gson = new GsonBuilder().create();
                        Log.e("ForSaleData", gson.toJson(response.body()));

                        //---------------------------------------------------------

                        arrayList.clear();

                        for (int i = 0; i <response.body().getResult().size() ; i++)
                        {
                            HashMap<String,String> hashMap = new HashMap<>();

                            hashMap.put("name",response.body().getResult().get(i).getName());
                            hashMap.put("quantity",response.body().getResult().get(i).getQuantity());
                            hashMap.put("price",response.body().getResult().get(i).getPrice());
                            hashMap.put("id",response.body().getResult().get(i).getId());
                            hashMap.put("user_id",response.body().getResult().get(i).getUserId());

                            if (response.body().getResult().get(i).getProductImages().size()>0)
                                hashMap.put("image",response.body().getResult().get(i).getProductImages().get(0).getImage());
                            else
                                hashMap.put("image","");

                            String quantity = response.body().getResult().get(i).getQuantity();

                            try {

                                if (!quantity.isEmpty())
                                {
                                    int pric = Integer.parseInt(quantity);
                                    if (pric>0)
                                        arrayList.add(hashMap);

                                }

                            }catch (Exception e){

                            }






                        }



                        //----------------------------------------------------------


                        checkOutProductList(response.body(),arrayList);
                        Log.e("sizeGet",response.body().getResult().size()+"");

                    }
                } else {
                    dismiss();
                    Toast.makeText(context, "No product available to show.", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("ForSaleData", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("ForSaleData", e.getLocalizedMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dismiss();
                Toast.makeText(context, "Something went's wrong.", Toast.LENGTH_SHORT).show();
                Log.e("ForSaleData", "failure" + t.getMessage());
            }
        });
    }
    Call<MakeOfferModel> makeOfferCall;
    private void makeOfferApi(String productId, String price, String name, String tt) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(context);
        dialog1.show();
        makeOfferCall = Global.WebServiceConstants.getRetrofitinstance().makeOfferApi(HelperPreferences.get(context).getString(UID), productId, otherUserId, price, "P", name, itemQuantity);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        controller.onMakeOfferButtonClick(response.body(), productName);
                        dismiss();

                    }
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                } else {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    Toast.makeText(context, "Unable to make an offer on this product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {

                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("MakeOfferApi", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
