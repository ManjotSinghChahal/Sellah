package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SalesAdapter;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;

@SuppressLint("ValidFragment")
public class PersonalProfileSalesFragment extends Fragment implements SalesAdapter.OnReportCallBack {


    RecyclerView saleRecycler;
    SalesAdapter salesAdapter;

    View view;
    private WebService service;
    Unbinder unbinder;
    private GetProductList forSaleProducts;
    String otherUserId;
    tabTextController controller;
    private BottomSheetDialog filterDialog;

    public PersonalProfileSalesFragment(String otheruserId) {
        this.otherUserId = otheruserId;
        Log.e("otherUserId", otheruserId);
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            controller = (tabTextController) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.person_profile_sales_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        saleRecycler = view.findViewById(R.id.salesRecycler);
        service = Global.WebServiceConstants.getRetrofitinstance();
        getSaledata();
        return view;
    }

    private void getSaledata() {
//        Dialog dialog=S_Dialogs.getLoadingDialog(getActivity());
        Call<GetProductList> recordsCall = service.getForSalelistApi(otherUserId);
        recordsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        forSaleProducts = response.body();
                        Log.e("ForSaleData", response.body().getResult().toString());
                        setSalesData(forSaleProducts);
                    }
                } else {
                    try {
                        Log.e("ForSaleData", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                Log.e("ForSaleData", "failure" + t.getMessage());
            }
        });
    }

    private void setSalesData(GetProductList list) {
        try {


            salesAdapter = new SalesAdapter(list, getActivity(), false, (productid, pos) -> {
            }, (productid, pos) -> {
            }, this);
            LinearLayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(), 2);
            saleRecycler.setLayoutManager(horizontalLayoutManager);
            saleRecycler.setAdapter(salesAdapter);
            controller.onItemCountChanged(forSaleProducts != null ? forSaleProducts.getResult().size() : 0);
        } catch (Exception e) {
            Log.e("Exception", "setSalesData: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReportClick(String productId) {

        if (!Global.NetworStatus.isOnline(getActivity()) || Global.NetworStatus.isInternetAvailable()) {
            S_Dialogs.getNetworkErrorDialog(getActivity()).show();
        } else {
            if (isLogined(getActivity())) {
                filterDialog = new BottomSheetDialog(getActivity());
                filterDialog.setContentView(R.layout.filter_dialog);
                LinearLayout ll_reporting_item = filterDialog.findViewById(R.id.ll_reporting_item);
                LinearLayout l2_prohibited = filterDialog.findViewById(R.id.l2_prohibited);
                LinearLayout l3_mispriced = filterDialog.findViewById(R.id.l3_mispriced);
                LinearLayout l4_wrongCategroy = filterDialog.findViewById(R.id.l4_wrongCategroy);
                LinearLayout l5_duplicate = filterDialog.findViewById(R.id.l5_duplicate);
                LinearLayout l6_offensive = filterDialog.findViewById(R.id.l6_offensive);
                LinearLayout l7_irrelevant = filterDialog.findViewById(R.id.l7_irrelevant);
                LinearLayout l8_counterfeit = filterDialog.findViewById(R.id.l8_counterfeit);
                LinearLayout l9_cancel = filterDialog.findViewById(R.id.l9_cancel);
                filterDialog.show();

                l2_prohibited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l2_prohibited, productId);
                        filterDialog.dismiss();
                    }
                });
                l3_mispriced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l3_mispriced, productId);
                        filterDialog.dismiss();
                    }
                });
                l4_wrongCategroy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l4_wrongCategroy, productId);
                        filterDialog.dismiss();
                    }
                });
                l5_duplicate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l5_duplicate, productId);
                        filterDialog.dismiss();
                    }
                });
                l6_offensive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l6_offensive, productId);
                        filterDialog.dismiss();
                    }
                });
                l7_irrelevant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l7_irrelevant, productId);
                        filterDialog.dismiss();
                    }
                });
                l8_counterfeit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitReportApi(l8_counterfeit, productId);
                        filterDialog.dismiss();
                    }
                });
                l9_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterDialog.dismiss();
                    }
                });

            } else {
                S_Dialogs.getLoginDialog(getActivity());
            }
        }

    }

    public interface tabTextController {
        void onItemCountChanged(int count);
    }

    private void hitReportApi(LinearLayout layout, String prductId) {

        new ReportApi().hitReportApi(getActivity(), layout
                , "", prductId, (msg) -> {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }, () -> {
                    Toast.makeText(getActivity(), "Please try again later", Toast.LENGTH_SHORT).show();
                });
    }
}
