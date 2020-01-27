package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.ChatActivityController;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.BuyerMakeOfferDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.CheckOutItemHolder> {


   // private List<ResultFeatured> recordList;
    Context context;
    boolean[] isCheckList;
    ChatActivityController controller;
    //    Dialog dialog;
    long subTotal = 0;
    WebService service;
    ActionCallback actionCallback;
    Button txtsend_offer;
    int pos = -1;
    boolean check;
    String price, qty;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    public CheckoutProductAdapter(List<Result> itemList,ArrayList<HashMap<String,String>> arrayList_, Context context, Button txt_sendoffer, ActionCallback checkSelection) {
      //  this.recordList = itemList;
        this.context = context;
        this.arrayList = arrayList_;
        this.txtsend_offer = txt_sendoffer;
        isCheckList = new boolean[itemList.size()];
        controller = new ChatActivity();
//        this.dialog = dialog;
        for (int i = 0; i < arrayList.size(); i++) {

            isCheckList[i] = false;
        }
        this.actionCallback = checkSelection;
        service = Global.WebServiceConstants.getRetrofitinstance();



    }

    @Override
    public CheckOutItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout_product, parent, false);
        CheckOutItemHolder gvh = new CheckOutItemHolder(view);
        return gvh;
    }


    @Override
    public void onBindViewHolder(CheckOutItemHolder holder, final int position) {
        holder.chkSelect.setChecked(isCheckList[position]);

        holder.txtProductName.setText(arrayList.get(position).get("name"));
        holder.txtProductCost.setText("S$ " + arrayList.get(position).get("price"));
        holder.qty.setText(arrayList.get(position).get("quantity"));
        Glide.with(context)
                .load(!arrayList.get(position).get("image").isEmpty() ? arrayList.get(position).get("image") : "")
               // .apply(Global.getGlideOptions())
                .into(holder.imgProduct);


/*
        Glide.with(context)
                .load(!arrayList.get(position).get("image").isEmpty() ? arrayList.get(position).get("image") : "")
                .apply(Global.getGlideOptions())
                .into(holder.imgProduct);*/

      /*  if (recordList.get(position).getQuantity().isEmpty()  || Integer.parseInt(recordList.get(position).getQuantity())<=0)
        {
         Log.e("_printEmpty","data is fetched");
            holder.topRelCheckout.setVisibility(View.GONE);
        }
        else
        {
            holder.topRelCheckout.setVisibility(View.VISIBLE);

        holder.txtProductName.setText(arrayList.get(position).get("name"));
        holder.txtProductCost.setText("S$ " + arrayList.get(position).get("price"));
        holder.qty.setText(arrayList.get(position).get("quantity"));
      Glide.with(context)
                .load(!recordList.get(position).getProductImages().isEmpty() ? recordList.get(position).getProductImages().get(0).getImage() : "")
                .apply(Global.getGlideOptions())
                .into(holder.imgProduct);*/
       //    }


        //    holder.chkSelect.setTag(position);

        holder.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (arrayList.get(position).get("quantity").isEmpty() || Integer.parseInt(arrayList.get(position).get("quantity")) <= 0) {
                    Toast.makeText(context, "Product is out of stock", Toast.LENGTH_SHORT).show();
                } else {
                    pos = position;
                    notifyDataSetChanged();
                }


            }
        });

        if (pos == position) {
            actionCallback.onCheckclicked(arrayList.get(pos).get("name"), arrayList.get(pos).get("id"), arrayList.get(position).get("price"), arrayList.get(position).get("quantity"));

            holder.checkout.setBackgroundColor(Color.parseColor("#dbdbdb"));

            price = arrayList.get(position).get("price");
            qty = arrayList.get(position).get("quantity");


        } else {
            holder.checkout.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        txtsend_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (price != null && qty != null)
                    holder.imgMakeOffer.performClick();
                else
                    Toast.makeText(context, "Please select product", Toast.LENGTH_SHORT).show();

            }
        });

        holder.imgMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BuyerMakeOfferDialog.create(context, price, qty, new BuyerMakeOfferDialog.OnmakeOfferClick() {
                    @Override
                    public void onMakeOfferClick(String price, String quantity, BuyerMakeOfferDialog buyerMakeOfferDialog) {
//                        makeOfferApi(position,recordList.get(position).getId(), price, recordList.get(position).getName(), buyerMakeOfferDialog);
                        makeOfferApi(position, price, arrayList.get(position).get("name"), quantity, buyerMakeOfferDialog);

                    }
                }).show();
               /* S_Dialogs.getMakeOfferDialog(context,recordList.get(position).getPrice(),(dialog,input)->{
                    if (TextUtils.isEmpty(input)||input.toString().equalsIgnoreCase("0")) {
                        Toast.makeText(context, "Please enter offering amount", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                    } else {

                        makeOfferApi(position,input.toString().trim(),recordList.get(position).getName(),dialog);
                    }
                }).show();*/
            }
        });

        holder.chkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.chkSelect.getTag();
                try {
                    if (isCheckList[pos]) {
                        isCheckList[pos] = false;
                        subTotal = subTotal - Long.parseLong(arrayList.get(pos).get("price"));
                    } else {

                        subTotal = subTotal + Long.parseLong(arrayList.get(pos).get("price"));
                        isCheckList[pos] = true;
                    }
                    int quantity = 0;
                    for (int i = 0; i < isCheckList.length; i++) {
                        quantity += isCheckList[i] ? 1 : 0;
                    }
                    actionCallback.onCheckclicked(arrayList.get(pos).get("name"), arrayList.get(pos).get("id"), String.valueOf(subTotal), String.valueOf(quantity));
                } catch (Exception e) {

                }

//                txtSubTotal.setText(subTotal);
//                controller.updateSubTotal(String.valueOf(subTotal));
            }

        });

    }

    public interface ActionCallback {
        public void onCheckclicked(String name, String id, String subtotal, String quantity);

        public void onMakeOffer(MakeOfferModel body, String name);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CheckOutItemHolder extends RecyclerView.ViewHolder {
        CheckBox chkSelect;
        TextView txtProductName, txtProductCost, qty;
        ImageView imgProduct, imgMakeOffer;
        RelativeLayout checkout;
        RelativeLayout topRelCheckout;

        public CheckOutItemHolder(View view) {
            super(view);
            chkSelect = view.findViewById(R.id.chk_select);
            txtProductName = view.findViewById(R.id.txt_product_name);
            txtProductCost = view.findViewById(R.id.txt_product_cost);
            imgProduct = view.findViewById(R.id.img_product);
            imgMakeOffer = view.findViewById(R.id.img_btn_make_offer);
            checkout = view.findViewById(R.id.root_checkout);
            qty = view.findViewById(R.id.txt_product_qty);
            topRelCheckout = view.findViewById(R.id.top_rel_checkout);
        }
    }

    private void makeOfferApi(int pos, String price, String name, String quantity, BuyerMakeOfferDialog dialog) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(context);
        dialog1.show();
        Call<MakeOfferModel> makeOfferCall = service.makeOfferApi(HelperPreferences.get(context).getString(UID), arrayList.get(pos).get("id"), arrayList.get(pos).get("user_id"), price, "P", name, quantity);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        actionCallback.onMakeOffer(response.body(), arrayList.get(pos).get("name"));
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
                Log.e("MakeOfferApi", "onFailure: " + t.getCause());
            }
        });
    }

}
