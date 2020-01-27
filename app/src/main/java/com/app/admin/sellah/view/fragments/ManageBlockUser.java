package com.app.admin.sellah.view.fragments;

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
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.BlockedUserModel.BlockListModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.BlockedUserAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;


public class ManageBlockUser extends Fragment {

    @BindView(R.id.rv_blocked_user)
    RecyclerView rvBlockedUser;

    Unbinder unbinder;
    @BindView(R.id.root_mbu)
    FrameLayout rootMbu;
    @BindView(R.id.txt_no_blocked_user)
    TextView txtNoBlockedUser;
    private BlockedUserAdapter adapter;
    private BlockListModel blockedUsers;

    public ManageBlockUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_block_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        blockedUsers = new BlockListModel();

        getBlockeduserList();

        hideSearch();
//        setupData();

        // Inflate the layout for this fragment
        return view;
    }

    private void setupData() {
        if(blockedUsers!=null&&blockedUsers.getRecord().size()>0){
            txtNoBlockedUser.setVisibility(View.GONE);
        }else{
            txtNoBlockedUser.setVisibility(View.VISIBLE);
        }
        adapter = new BlockedUserAdapter(blockedUsers, getActivity(), (pos, userId) -> {
            S_Dialogs.getBlockUnblockConfirmation(getActivity(), getActivity().getResources().getString(R.string.dialog_title_unblock_user), ((dialog, which) -> {
                blockUnBlockUser("unblock", pos, userId);
            })).show();
        });
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBlockedUser.setLayoutManager(horizontalLayoutManager1);
        rvBlockedUser.setAdapter(adapter);
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
        ((MainActivity) getActivity()).text_sell.setText("Blocked Users");
//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);

        ((MainActivity) getActivity()).changeOptionColor(4);

    }

    private void blockUnBlockUser(String blockStatus, int pos, String userId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> manageBlockUserCall = Global.WebServiceConstants.getRetrofitinstance().manageBlockListApi(HelperPreferences.get(getActivity()).getString(UID), userId, blockStatus);
        manageBlockUserCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootMbu, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        blockedUsers.getRecord().remove(pos);
                        adapter.notifyItemRemoved(pos);
                        adapter.notifyItemRangeChanged(pos, 3);
                        if(blockedUsers!=null&&blockedUsers.getRecord().size()>0){
                            txtNoBlockedUser.setVisibility(View.GONE);
                        }else{
                            txtNoBlockedUser.setVisibility(View.VISIBLE);
                        }
                    }
                } else {

                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("bmanageUser", "onFailure: " + t.getMessage());
                Snackbar.make(rootMbu, "Please try again latter.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void getBlockeduserList() {

        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<BlockListModel> manageBlockUserCall = Global.WebServiceConstants.getRetrofitinstance().getBlockList(HelperPreferences.get(getActivity()).getString(UID));
        manageBlockUserCall.enqueue(new Callback<BlockListModel>() {
            @Override
            public void onResponse(Call<BlockListModel> call, Response<BlockListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        /*Snackbar.make(rootMbu, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();*/
                        blockedUsers = response.body();
                        setupData();
                    }
                } else {

                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BlockListModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("BlockedList", "onFailure: " + t.getMessage());
                Snackbar.make(rootMbu, "Please try again latter.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }
}
