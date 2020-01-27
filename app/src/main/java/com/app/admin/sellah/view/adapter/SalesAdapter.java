package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.PromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;
import com.app.admin.sellah.view.CustomDialogs.Stripe_image_verification_dialogfragment;
import com.app.admin.sellah.view.activities.AddNewVideos;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.app.admin.sellah.view.fragments.SellFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.ADDPRODUCTTAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PRODUCT_DETAIL;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;


public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    GetProductList saleList;
    LayoutInflater mInflater;
    Context context;
    View view;
    boolean isOwner;
    MarkAsSoldCallback callback;
    OnDeleteItemCallback onDeleteItemCallback;
    OnReportCallBack reportCallBack;

//    TabTextController controller = new ProfileFragment();

    public SalesAdapter(GetProductList saleList, Context context, boolean isOwner, MarkAsSoldCallback callback, OnDeleteItemCallback onDeleteItemCallback,OnReportCallBack reportCallBack) {
        this.saleList = saleList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.isOwner = isOwner;
        this.callback = callback;
        this.onDeleteItemCallback = onDeleteItemCallback;
        this.reportCallBack=reportCallBack;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.sales_adapter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions=Global.getGlideOptions();
        String imageUrl = "";
        if (saleList.getResult().get(position).getProductImages() != null) {
            imageUrl = !saleList.getResult().get(position).getProductImages().isEmpty() ? saleList.getResult().get(position).getProductImages().get(0).getImage() : "";
//            imageUrl.replace("productimages","thproductimages");
            imageUrl = imageUrl.replace("productimages","thproductimages");
        }
        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(holder.imageView);
        Log.e( "onBindViewHolder: ","s"+saleList.getResult().get(position).getQuantity() );
        if (saleList.getResult().get(position).getQuantity().isEmpty()||Integer.parseInt(saleList.getResult().get(position).getQuantity())<=0)
        {

            holder.outofstock.setVisibility(View.VISIBLE);
            holder.imgFeatured.setVisibility(View.GONE);
        }
        else
        {
            if(!TextUtils.isEmpty(saleList.getResult().get(position).getPromoteProduct())&&saleList.getResult().get(position).getPromoteProduct().equalsIgnoreCase("S")/*&&saleList.getResult().get(position).getPromotes()!=null&&saleList.getResult().get(position).getPromotes().size()>0*/){
                holder.

                        imgFeatured.setVisibility(View.VISIBLE);
                holder.outofstock.setVisibility(View.GONE);
            }else{
                holder.imgFeatured.setVisibility(View.GONE);
                holder.outofstock.setVisibility(View.GONE);
            }
        }



        holder.salesText.setText(saleList.getResult().get(position).getName());
        holder.costText.setText(saleList.getResult().get(position).getPrice());
        holder.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOwner) {

                    if (saleList.getResult().get(position).getQuantity().isEmpty()||Integer.parseInt(saleList.getResult().get(position).getQuantity())<=0)
                    {
                        Toast.makeText(context, "Product is out of stock", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        getOwnerMenu(holder.btnOption, position);
                    }

                } else {
                    getUserMenu(holder.btnOption, position);
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, saleList.getResult().get(position));
                ProductFrgament fragment = new ProductFrgament();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

            }
        });
//            controller.tabTextController();
//        controller.tabTextController(position,saleList.getResult().size());

    }


    public void getOwnerMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.personal_item_menu, popup.getMenu());
        popup.show();
        if (saleList.getResult().get(position).getPromoteProduct().equalsIgnoreCase("S")/*&&saleList.getResult().get(position).getPromotes()!=null&&saleList.getResult().get(position).getPromotes().size()>0*/) {
            popup.getMenu().findItem(R.id.menu_promote).setVisible(false);
            popup.getMenu().findItem(R.id.menu_update_promote).setVisible(true);
            if(saleList.getResult().get(position).getPromotes()!=null&&saleList.getResult().get(position).getPromotes().size()>0) {
                popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(true);
            }else{
                popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(false);
            }
        } else {
            popup.getMenu().findItem(R.id.menu_promote).setVisible(true);
            popup.getMenu().findItem(R.id.menu_update_promote).setVisible(false);
            popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_as_sold:
                        callback.onMarkAsSoldClick(saleList.getResult().get(position).getId(), position);
                        break;
                    case R.id.menu_cancel_promote:
                        try {
                            if(saleList.getResult().get(position).getPromotes()!=null&&saleList.getResult().get(position).getPromotes().size()>0){
                            S_Dialogs.getCancelPromotion(context,((dialog, which) -> {
                                cancelPromotionApi(saleList.getResult().get(position).getId(),saleList.getResult().get(position).getPromotes().get(0).getId(),position);
                            })).show();}
                        }catch (Exception e){
                            Toast.makeText(context, "Invalid inputs.", Toast.LENGTH_SHORT).show();
                        }
                       break;
                    case R.id.menu_update_promote:

                        if (HelperPreferences.get(context).getString(STRIPE_VERIFIED).equals("N"))
                        {
                            S_Dialogs.getLiveVideoStopedDialog(context, "You are not currently connected with stripe. Press ok to connect.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
                                stripe_dialogfragment.show(((MainActivity)context).getFragmentManager(),"");

                            })).show();
                        }

                        else if ((HelperPreferences.get(context).getString(STRIPE_VERIFIED).equalsIgnoreCase("P")))
                        {
                            S_Dialogs.getLiveVideoStopedDialog((context), "You have not uploaded you Idenitification Documents. Press ok to upload.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_image_verification_dialogfragment stripe_dialogfragment = new Stripe_image_verification_dialogfragment();
                                stripe_dialogfragment.show(((MainActivity)context).getFragmentManager(),"");

                            })).show();
                        }
                        else
                        {
                            PromoteDialog.create(context,saleList.getResult().get(position).getId(), new PromoteDialog.PromoteCallback() {
                                @Override
                                public void onPromoteSuccess() {
                                    Toast.makeText(context, "Promote package is updated successfully.", Toast.LENGTH_SHORT).show();
                                    saleList.getResult().get(position).setPromoteProduct("S");
                                    notifyDataSetChanged();
                                }
                                @Override
                                public void onPromoteFailure() {
                                    Toast.makeText(context, "Unable to update promote package at this movement.", Toast.LENGTH_SHORT).show();
                                }
                            }).show();

                        }
                        break;

                        case R.id.menu_edit_item:
                            /*Bundle bundle = new Bundle();
                            bundle.putParcelable(PRODUCT_DETAIL, saleList.getResult().get(position));
                            SellFragment fragment = new SellFragment();
                            fragment.setArguments(bundle);
                            loadFragment(fragment, ADDPRODUCTTAG, bundle);*/



                            ArrayList<Result> list = new ArrayList<>();
                            list.addAll(saleList.getResult());



/*
                            Gson gson = new Gson();
                            String res = gson.toJson(saleList.getResult().get(position));
                            Log.e("AddNewVideosPrint",res);
*/

                            Gson gson = new GsonBuilder().create();


                            Bundle bundle = new Bundle();
                            bundle.putParcelable("modelProductList",list.get(position));
                            bundle.putString("Product" , gson.toJson(list.get(position)));
                          //  bundle.putParcelableArrayList("modelProductList",list);
                            Intent intent= new Intent(context,AddNewVideos.class);
                            intent.putExtra("way_status","SalesAdapter");
                            intent.putExtra("position",String.valueOf(position));
                         //   intent.putParcelableArrayListExtra("modelProductList",list);
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                        break;
                    case R.id.menu_delete_item:
                        onDeleteItemCallback.onDeleteIteClick(saleList.getResult().get(position).getId(), position);
                        break;
                    case R.id.menu_promote:
                        PromoteDialog.create(context,saleList.getResult().get(position).getId(), new PromoteDialog.PromoteCallback() {
                            @Override
                            public void onPromoteSuccess() {
                                saleList.getResult().get(position).setPromoteProduct("S");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onPromoteFailure() {

                            }
                        }).show();
                        break;
                }

                return false;
            }
        });
    }
    public boolean loadFragment(Fragment fragment, String Tag, Bundle bundle) {
        if (fragment != null) {
            fragment.setArguments(bundle);
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(Tag).commit();
            return true;
        }
        return false;
    }

    public void getUserMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.user_item_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_report:
                        if(reportCallBack!=null){
                            reportCallBack.onReportClick(saleList.getResult().get(position).getId());
                        }
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
//        if (saleList != null) {
            return saleList.getResult().size();
//        } else {
//            return 0;
//        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        TextView salesText, costText,outofstock;
        ImageView btnOption,imgFeatured;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.third_image);
            btnOption = v.findViewById(R.id.btn_option);
            salesText = (TextView) v.findViewById(R.id.txt_product_name);
            outofstock = (TextView) v.findViewById(R.id.txt_out_ofstock);
            costText = (TextView) v.findViewById(R.id.txt_product_cost);
            imgFeatured =  v.findViewById(R.id.img_featured);
        }
    }

    public interface MarkAsSoldCallback {
        public void onMarkAsSoldClick(String productId, int pos);
    }

    public interface OnDeleteItemCallback {
        public void onDeleteIteClick(String productId, int pos);
    }

    public interface TabTextController {
        public void tabTextController(int count);
    }

    public interface OnReportCallBack{
        void onReportClick(String productId);
    }

    private void cancelPromotionApi(String productId, String packageId, int position){
        Dialog dialog= S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> cancelPromotionCall=Global.WebServiceConstants.getRetrofitinstance().cancelPromotionApi(
                HelperPreferences.get(context).getString(UID),productId,packageId
        );
        cancelPromotionCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                if(response.isSuccessful()){
                    if(response.body().getStatus().equalsIgnoreCase("1")){
                                saleList.getResult().get(position).setPromoteProduct("N");
                                notifyDataSetChanged();
                        Toast.makeText(context, "Your promotion against this product has been canceled successfully.", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("CancelPromotionApi", "error: "+response.body().getMessage());
                    }
                }else{
                    try {
                        Log.e("CancelPromotionApi", "error: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                Log.e("CancelPromotionApi", "onFailure: "+t.getMessage());
            }
        });
    }



}
