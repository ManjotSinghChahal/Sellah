package com.app.admin.sellah.view.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SuggestedPostAdapter;
import com.app.admin.sellah.view.adapter.WishListAdapter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class WishListFragment extends Fragment {

    RecyclerView wishRecyler;
    WishListAdapter wishListAdapterBirth;
    View view;
    WebService service;
    @BindView(R.id.wishListRecycler)
    RecyclerView wishListRecycler;
    @BindView(R.id.wishlist_root)
    LinearLayout wishlistRoot;
    Unbinder unbinder;
    GetProductList wishlistData;
    @BindView(R.id.rv_suggested_post)
    RecyclerView rvSuggestedPost;
    private SuggestedPostAdapter suggestedPostAdapter;
    Call<GetProductList> addCommentCall;;
    @BindView(R.id.ll_no_product)
    LinearLayout llNoProduct;
    @BindView(R.id.ll_no_network)
    LinearLayout llNoNetwork;
    Call<GetProductList> wishlistrCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wish_list_fragment, container, false);

        wishRecyler = view.findViewById(R.id.wishListRecycler);
        service = Global.WebServiceConstants.getRetrofitinstance();

        unbinder = ButterKnife.bind(this, view);


        if (Global.NetworStatus.isOnline(getActivity())) {
            getWishListData();
            getSuggestedPostList();
        } else {
            wishlistRoot.setVisibility(View.GONE);
            llNoNetwork.setVisibility(View.VISIBLE);
            llNoProduct.setVisibility(View.GONE);
        }

        return view;
    }

    public void getWishListData() {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();

         wishlistrCall = service.getWishListApi(HelperPreferences.get(getActivity()).getString(UID));
        wishlistrCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    wishlistData = response.body();
                    initWishlist(wishlistData);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("getWishlist", "onErrorResponse: "+response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("GetWishList", "onFailure: "+t.getMessage() );

            }
        });
    }

    /* private void likeList() {
         HomeModelClass like1 = new HomeModelClass("S$60",R.drawable.product_icon);
         likeList.add(like1);
         likeList.add(like1);
         likeList.add(like1);

     }*/
    private void initWishlist(GetProductList wishListModel) {
        wishListAdapterBirth = new WishListAdapter(wishListModel, getActivity(), (productId, pos) -> {
            removeItemFromWishlist(productId, pos);
        });
        LinearLayoutManager birthHorizontalManager = new GridLayoutManager(getActivity(), 2);
        wishRecyler.setLayoutManager(birthHorizontalManager);
        wishRecyler.setAdapter(wishListAdapterBirth);
        wishListAdapterBirth.notifyDataSetChanged();

        if (wishListModel.getResult().size()>0)
               hideErrorMsg();
            else
               showErrorMsg();


    }

    private void removeItemFromWishlist(String productId, int pos) {
        /*Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();*/

        Call<Common> removeItemFromWishlist = service.removeFromWishListApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        removeItemFromWishlist.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
//                        getWishListData();
                        wishlistData.getResult().remove(pos);
//                        wishListAdapterBirth.notifyDataSetChanged();
                        wishListAdapterBirth.notifyItemRemoved(pos);
                        wishListAdapterBirth.notifyItemRangeChanged(pos, wishlistData.getResult().size());
                        Log.e("remove_Wishlist", "onResponse: removed");
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                Snackbar.make(wishlistRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getSuggestedPostList() {
         addCommentCall = service.suggestedProductsApi("");
        addCommentCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {

                    try {



                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        setUpSuggestedPosts(response.body());
                    }
                    }catch (Exception e) { }

                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                Log.e("suggested_post_failure", "onResponse: " + t.getMessage());
            }
        });
    }

    private void setUpSuggestedPosts(GetProductList list) {
        suggestedPostAdapter = new SuggestedPostAdapter(getActivity(), list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //    LinearLayoutManager addLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSuggestedPost.setLayoutManager(layoutManager);
        rvSuggestedPost.setAdapter(suggestedPostAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (addCommentCall!=null)
        {addCommentCall.cancel();}
        if (wishlistrCall!=null)
        {wishlistrCall.cancel();}
    }

    public void showErrorMsg() {
        if (view != null) {
           if (wishlistRoot!=null)wishlistRoot.setVisibility(View.GONE);
            if (llNoNetwork!=null) llNoNetwork.setVisibility(View.GONE);
            if (llNoProduct!=null) llNoProduct.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorMsg() {
        if (view != null) {
            try {
                wishlistRoot.setVisibility(View.VISIBLE);
                llNoNetwork.setVisibility(View.GONE);
                llNoProduct.setVisibility(View.GONE);
            }catch (Exception e){

            }


        }
    }

    @OnClick(R.id.ll_no_network)
    public void onViewClicked() {

        if (Global.NetworStatus.isOnline(getActivity())) {
            getWishListData();
            getSuggestedPostList();
        } else {
            wishlistRoot.setVisibility(View.GONE);
            llNoNetwork.setVisibility(View.VISIBLE);
            llNoProduct.setVisibility(View.GONE);
        }

    }

}
