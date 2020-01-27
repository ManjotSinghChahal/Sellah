package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.FolowModelNew.FollowModelNew;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.NonSwipeableViewPager;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.NonSwipableViewPaggerAdapter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

@SuppressLint("ValidFragment")
public class ViewFollowListFragment extends Fragment {

    @BindView(R.id.btn_Noti)
    TextView btnFolloers;
    @BindView(R.id.notification_img)
    ImageView notificationImg;
    @BindView(R.id.btn_mess)
    TextView btnFollowings;
    @BindView(R.id.message_img)
    ImageView messageImg;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.notification_viewpagger)
    NonSwipeableViewPager viewPager;

    WebService service;
    @BindView(R.id.li_follow_root)
    LinearLayout liFollowRoot;

//    FollowModelNew followModelNew;

    String userId;
    @SuppressLint("ValidFragment")
    public ViewFollowListFragment(String string) {
        // Required empty public constructor
        userId=string;
    }

    View view;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,new IntentFilter(PUSH_NOTIFICATION));
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_view_followings, container, false);
        }
        ((MainActivity) getActivity()).text_sell.setText("Followers");
        unbinder = ButterKnife.bind(this, view);

        service = Global.WebServiceConstants.getRetrofitinstance();

        getFollowList(userId);
        btnFolloers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).text_sell.setText("Followers");
                btnFollowings.setBackgroundResource(R.drawable.noti_button_bg_normal_right);
                btnFolloers.setBackgroundResource(R.drawable.noti_button_bg_selected_left);
                viewPager.setCurrentItem(0);
            }

        });

        btnFollowings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).text_sell.setText("Following");
                btnFolloers.setBackgroundResource(R.drawable.noti_button_bg_normal_left);
                btnFollowings.setBackgroundResource(R.drawable.noti_button_bg_selected_right);
                viewPager.setCurrentItem(1);
            }

        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }


    private void createViewPager(FollowModelNew followModelNew) {
        NonSwipableViewPaggerAdapter adapter = new NonSwipableViewPaggerAdapter(getChildFragmentManager());
        adapter.addFrag(new FollowListFragment(followModelNew, "Followers",userId));
        adapter.addFrag(new FollowListFragment(followModelNew, "Following",userId));
        viewPager.setAdapter(adapter);
    }

    private void getFollowList(String userId) {

        Dialog dialog=S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<FollowModelNew> followListCall = service.getFollowListApi(userId);
        followListCall.enqueue(new Callback<FollowModelNew>() {
            @Override
            public void onResponse(Call<FollowModelNew> call, Response<FollowModelNew> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        createViewPager(response.body());
                    }
                    if(dialog!=null&&dialog.isShowing()){
                        dialog.dismiss();
                    }
                } else {

                    if(dialog!=null&&dialog.isShowing()){
                        dialog.dismiss();
                    }
                    try {
                        Log.e("FollowListApi", "onErrorResponse: "+response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(liFollowRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }

            }

            @Override
            public void onFailure(Call<FollowModelNew> call, Throwable t) {
                Log.e("getFollowList", "onFailure: " + t.getMessage());
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                Snackbar.make(liFollowRoot, "Please try again later", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });

    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                if(message!=null){
                    switch(message.getNotiType()) {
                        case NT_ACCEPT_REJECT:

                            break;
                        case NT_FOLLOW:
                            getFollowList(userId);
                            break;
                        case NT_COMMENT_ADDED:

                            break;
                        case NT_PRODUCT_ADDED:

                            break;
                        case NT_CHAT:

                            break;
                        case NT_PAYMENT:

                            break;
                        case NT_OFFER_MAKE:

                            break;
                        default :

                            break;
                    }
                }
                Log.e("receiver", "Got message: Follow" + message.getMessage());
            } catch (Exception e) {
                Log.e("receiver_failure", "onReceive: " + e.getMessage());
            }
        }
    };
}
