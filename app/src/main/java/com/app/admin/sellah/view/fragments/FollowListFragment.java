package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.FollowModel.FollowModel;
import com.app.admin.sellah.model.extra.FolowModelNew.FollowModelNew;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.FollowListAdpter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class FollowListFragment extends Fragment {

    View view;
    Unbinder unbinder;
    FollowModelNew followList;
    @BindView(R.id.rv_follow_list)
    RecyclerView rvFollowList;
    String title;
    @BindView(R.id.root_follow)
    FrameLayout rootFollow;
    private FollowListAdpter adapter;
    String userId;

    @SuppressLint("ValidFragment")
    public FollowListFragment(FollowModelNew followList, String title, String userId) {
        this.followList = followList;
        this.title = title;
        this.userId=userId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideSearch();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_follow_list, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        setupData();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
//        ((MainActivity) getActivity()).text_sell.setText(title);
//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(4);

    }

    private void setupData() {

        boolean isOwner=false;

        if(userId.equalsIgnoreCase(HelperPreferences.get(getActivity()).getString(UID))){
            isOwner=true;
        }else{
            isOwner=false;
        }
        adapter = new FollowListAdpter(followList, getActivity(),title,isOwner,(userId, pos) -> {
            unFollowUserApi(userId, pos);
        });
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvFollowList.setLayoutManager(horizontalLayoutManager1);
        rvFollowList.setAdapter(adapter);
    }

    private void unFollowUserApi(String otherUserId, int pos) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<FollowModel> getProfileCall = Global.WebServiceConstants.getRetrofitinstance().unFollowUserApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId);
        getProfileCall.enqueue(new Callback<FollowModel>() {
            @Override
            public void onResponse(Call<FollowModel> call, Response<FollowModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(rootFollow, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    followList.getFollowing().remove(pos);
                    adapter.notifyItemRemoved(pos);
                    adapter.notifyItemRangeChanged(pos, followList.getFollowing().size());

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(rootFollow, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("unFollow_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                Snackbar.make(rootFollow, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

}
