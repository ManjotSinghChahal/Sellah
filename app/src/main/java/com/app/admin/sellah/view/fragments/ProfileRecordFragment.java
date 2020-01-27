package com.app.admin.sellah.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.wishllist_model.Wishlist;
import com.app.admin.sellah.view.adapter.WishRecordAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ProfileRecordFragment extends Fragment {
    View view;
    RecyclerView recordRecycler;
    WishRecordAdapter wishRecordAdapter;
    WebService service;
    @BindView(R.id.txt_heading)
    TextView txtHeading;
    @BindView(R.id.img_no_data)
    ImageView imgNoData;
    Unbinder unbinder;
    Call<Wishlist> recordsCall;
    @BindView(R.id.ll_no_product)
    LinearLayout llNoProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_record_fragment, container, false);
        service = Global.WebServiceConstants.getRetrofitinstance();
        recordRecycler = view.findViewById(R.id.profile_record_recycler);
        getRecordsData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void getRecordsData() {
        recordsCall = service.getRecordsApi(HelperPreferences.get(getActivity()).getString(UID));
        recordsCall.enqueue(new Callback<Wishlist>() {
            @Override
            public void onResponse(Call<Wishlist> call, Response<Wishlist> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        setRecordsData(response.body());
                    }
                } else {
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call<Wishlist> call, Throwable t) {

            }
        });
    }

    private void setRecordsData(Wishlist body) {


        wishRecordAdapter = new WishRecordAdapter(body, getActivity());
        LinearLayoutManager birthHorizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recordRecycler.setLayoutManager(birthHorizontalManager);
        recordRecycler.setAdapter(wishRecordAdapter);

        if (!(body.getResult().size() > 0)) {
            showErrorMsg();
        } else {
            hideErrorMsg();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recordsCall != null) {
            recordsCall.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showErrorMsg() {
        txtHeading.setVisibility(View.GONE);
        llNoProduct.setVisibility(View.VISIBLE);
    }

    public void hideErrorMsg() {
        txtHeading.setVisibility(View.VISIBLE);
        llNoProduct.setVisibility(View.GONE);
    }
}

