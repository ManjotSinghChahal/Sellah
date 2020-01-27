package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.TouchDetectableScrollView;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.HomeProductAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.getTotalHeightofGridRecyclerView;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;


public class Show_tag_result_fragment extends Fragment {



    @BindView(R.id.nolivevideo_text)
    LinearLayout nolivevideoText;
    @BindView(R.id.tag_recycler)
    RecyclerView tagRecycler;
    @BindView(R.id.pb_home)
    ProgressBar pbHome;
    @BindView(R.id.txt_no_more_item)
    TextView txtNoMoreItem;
    @BindView(R.id.img_data_error)
    ImageView imgDataError;
    @BindView(R.id.sv_root)
    TouchDetectableScrollView svRoot;
    @BindView(R.id.swipe_container)
    LinearLayout swipeContainer;

    Call<GetProductList> getProductsCall;
    GetProductList productList;
    private Dialog dialog;
    private ArrayList<Result> resultList;
    private int TOTAL_PAGES = 0;
    private boolean isFeedsFetchInProgress = false;
    WebService service;
    HomeProductAdapter mainCategoriesAdapter;
    private int currentPage = PAGE_START;
    private static final int PAGE_START = 1;
    private int previousPage = 0;
    Unbinder unbinder;
    String tag_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aboutUsView = inflater.inflate(R.layout.show_tag_result, container, false);
        unbinder = ButterKnife.bind(this, aboutUsView);

        Bundle bundle = this.getArguments();


        service = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(getActivity());
        hideSearch();
        productList = new GetProductList();
        resultList = new ArrayList<>();
        productList.setResult(resultList);
        pagginationCode();


        setMainProductList();


        Log.e( "bundle: ",bundle.getString("root") );

        if (bundle.getString("root").equals("search"))
        {
            new ApisHelper().getSeaarchResultApi(getActivity(), new ApisHelper.SearchResultCallback() {
                @Override
                public void onProductListSuccess(GetProductList body) {
                    Log.e( "onProductListSuccess: ",body.getStatus() );
                    if (body.getStatus().equals("1")) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            productList.setStatus(body.getStatus());
                            productList.setMessage(body.getMessage());
                            resultList.addAll(body.getResult());
                            productList.setResult(resultList);
                            isFeedsFetchInProgress = false;
                            pbHome.setVisibility(View.GONE);
                            txtNoMoreItem.setVisibility(View.GONE);
                            mainCategoriesAdapter.notifyItemRangeInserted(productList.getResult().size() > 0 ? productList.getResult().size() - 1 : 0, productList.getResult().size());
                            if (productList.getResult().size() > 0) {
                                nolivevideoText.setVisibility(View.GONE);
                                tagRecycler.setVisibility(View.VISIBLE);

                                getTotalHeightofGridRecyclerView(getActivity(), tagRecycler, R.layout.main_categories_adapter, 1);
                            } else {
                                nolivevideoText.setVisibility(View.VISIBLE);
                                tagRecycler.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {

                        }


                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onProductListFailure() {
                    Log.e("searchResult", "onProductListFailure: ");
                }
            },bundle.getString("tag"));

        }
        else
        {
            tag_name = bundle.getString("tag");

            getProductlist();
        }

        return aboutUsView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void pagginationCode() {

        Rect scrollBounds = new Rect();
        svRoot.getHitRect(scrollBounds);

        svRoot.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
            @Override
            public void onScroll() {
            }

            @Override
            public void onScrollUp() {

                pbHome.setVisibility(View.GONE);

            }

            @Override
            public void onScrollDown() {
                pbHome.setVisibility(View.VISIBLE);

            }

            @Override
            public void onBottomReached() {
                Log.e("onBottomReached: ", "vbb");
                if (isFeedsFetchInProgress) {
                    pbHome.setVisibility(View.VISIBLE);
                    return;
                }
                if (TOTAL_PAGES > 1) {

                    //End of list
                    previousPage = currentPage;
                    currentPage++;
                    isFeedsFetchInProgress = true;
                    pbHome.setVisibility(View.VISIBLE);
//                        loader = LOADER.BOTTOM;
                    if ((currentPage) <= TOTAL_PAGES) {
                        getProductlist();
                        Log.e("onBottomReached:", "ff");
                    } else {
                        isFeedsFetchInProgress = false;
                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.VISIBLE);
                    }
//                    }
                } else {
                    isFeedsFetchInProgress = false;
                    pbHome.setVisibility(View.GONE);
                    txtNoMoreItem.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTopReached() {

            }
        });
    }

    private void setMainProductList() {

        try {


            tagRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mainCategoriesAdapter = new HomeProductAdapter(getActivity(), productList);
            tagRecycler.setAdapter(mainCategoriesAdapter);
            imgDataError.setVisibility(View.GONE);
            if (productList.getResult().size() > 0) {
                getTotalHeightofGridRecyclerView(getActivity(), tagRecycler, R.layout.main_categories_adapter, 0);
            }
        } catch (Exception e) {

        }


    }

    public void getProductlist() {
        if (!Global.NetworStatus.isOnline(getActivity()) || Global.NetworStatus.isInternetAvailable()) {



        } else {
            dialog.show();
            getProductsCall = service.searchProductApi(tag_name,"tag");
            getProductsCall.enqueue(new Callback<GetProductList>() {
                @Override
                public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {



                    Gson gson = new GsonBuilder().create();
                    Log.e("Getproducts", "Success" + gson.toJson(response.body().toString()));
                    if (response.isSuccessful()) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }




                     //   TOTAL_PAGES = Integer.parseInt(response.body().getTotalPages());
                        try {
                            productList.setStatus(response.body().getStatus());
                            productList.setMessage(response.body().getMessage());
                            resultList.addAll(response.body().getResult());
                            productList.setResult(resultList);
                            isFeedsFetchInProgress = false;
                            pbHome.setVisibility(View.GONE);
                            txtNoMoreItem.setVisibility(View.GONE);
                            mainCategoriesAdapter.notifyItemRangeInserted(productList.getResult().size() > 0 ? productList.getResult().size() - 1 : 0, productList.getResult().size());
                            if (productList.getResult().size() > 0) {
                                nolivevideoText.setVisibility(View.GONE);
                                tagRecycler.setVisibility(View.VISIBLE);

                                getTotalHeightofGridRecyclerView(getActivity(), tagRecycler, R.layout.main_categories_adapter, 1);
                            }
                            else {
                                nolivevideoText.setVisibility(View.VISIBLE);
                                tagRecycler.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {

                        }
//                        mainCategoriesAdapter.notifyDataSetChanged();
//                        setMainProductList(productList);

                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
//                        productList = new GetProductList();
//                    setMainProductList(productList);
                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.GONE);
//                        txtErrorItem.setVisibility(View.VISIBLE);
                        try {
                            Log.e("Getproducts", "failure" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    /*Snackbar.make(rootTag, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();*/
                    }

                }

                @Override
                public void onFailure(Call<GetProductList> call, Throwable t) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (pbHome != null) {
                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.GONE);
                    }

//                    txtErrorItem.setVisibility(View.VISIBLE);
               /* Snackbar.make(rootTag, "Something went's wrong", Snackbar.LENGTH_SHORT)df
                        .setAction("", null).show();*/
                }
            });
        }
    }

    public void hideSearch() {


        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(0);
     //   ((MainActivity) getActivity()).text_sell.setText("Search ResultFeatured");
        ((MainActivity) getActivity()).text_sell.setText("");
        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);





        /**********************************************************************************************/

    }


}
