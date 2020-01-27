package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.FollowModel.FollowModel;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.PersonalProfilePagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class PersonalProfileFragment extends Fragment implements PersonalProfileSalesFragment.tabTextController {

    ViewPager vpPager;
    TabLayout tabLayout;
    PersonalProfilePagerAdapter personalProfilePagerAdapter;
    List<String> tags;
    @BindView(R.id.img_user_profile)
    CircleImageView imgUserProfile;
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.tv_proffesion)
    TextView tvProffesion;
    @BindView(R.id.tv_followers)
    TextView tvFollowers;
    @BindView(R.id.tv_following)
    TextView tvFollowing;
    @BindView(R.id.btn_follow)
    Button btnFollow;
    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.personal_profile_TabLayout)
    TabLayout personalProfileTabLayout;
    @BindView(R.id.personl_profile_ViewPager)
    ViewPager personlProfileViewPager;
    Unbinder unbinder;
    @BindView(R.id.li_personal_profile_root)
    LinearLayout liPersonalProfileRoot;
    @BindView(R.id.li_follow)
    LinearLayout liFollow;
    @BindView(R.id.location_profile)
    TextView locationProfile;
    private BottomSheetDialog filterDialog;
    String otherUserId = "";
    private ProfileModel profileData;
    private WebService service;
    View view;
    private boolean followStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        service = Global.WebServiceConstants.getRetrofitinstance();


        Global.DEEP_LINKING_STATUS = "disable";

        if (getArguments() != null) {
            otherUserId = getArguments().containsKey(SAConstants.Keys.OTHER_USER_ID) ? getArguments().getString(SAConstants.Keys.OTHER_USER_ID) : "";
        } else {
            otherUserId = "";
        }
        if (!otherUserId.equalsIgnoreCase("")) {
            getProfiledata(otherUserId);
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (view == null) {
            view = inflater.inflate(R.layout.personal_profile_fragment, container, false);
            getIds(view);
        }
        unbinder = ButterKnife.bind(this, view);
        hideSearch();

        //---------generate url----------------
        getProductUrlApi(otherUserId);


        return view;
    }

    private void getProfiledata(String otherUserId) {

        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<ProfileModel> getProfileCall = service.getProfileApi(otherUserId);
        getProfileCall.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.isSuccessful()) {
                    dismissDialog(dialog);
                    profileData = response.body();
                    Log.e("profile_success", response.body().getMessage());
                    try {
                        setProfileData(profileData);
                        getFollowStatusApi(profileData.getResult().getId());
                    } catch (Exception e) {
                    }

                } else {
                    dismissDialog(dialog);
                    Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("Profile_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                dismissDialog(dialog);
                Log.e("errorPrint", t.getMessage());
                Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void setProfileData(ProfileModel body) {

        if (body != null) {

            if (!TextUtils.isEmpty(body.getResult().getUsername())) {
                tvProfileName.setText(body.getResult().getUsername());
            } else {
                tvProfileName.setText("Sellah! user");
            }

            if (!TextUtils.isEmpty(body.getResult().getRating()))
                textview.setText(body.getResult().getRating());


            if (!TextUtils.isEmpty(body.getResult().getDescription())) {
                tvProffesion.setText(body.getResult().getDescription());
            } else {
                tvProffesion.setText("NA");
            }
            if (!TextUtils.isEmpty(body.getResult().getCity())) {
                locationProfile.setText(body.getResult().getCity());
            } else {
                locationProfile.setText("Singapore");
            }

       /*     RequestOptions requestOptions = new RequestOptions();
            requestOptions.transform(new CircleCrop());
            requestOptions.skipMemoryCache(true);
            requestOptions.centerInside();
            requestOptions.placeholder(R.drawable.glide_error);
            requestOptions.error(R.drawable.glide_error);*/
            RequestOptions requestOptions = Global.getGlideOptions();
            Glide.with(getActivity())
                    .load(body.getResult().getImage())
                    .apply(requestOptions)
                    .into(imgUserProfile);


            try {
                tvFollowers.setText(String.valueOf(profileData.getResult().getFollowers()));
                tvFollowing.setText(String.valueOf(profileData.getResult().getFollowing()));

            } catch (Exception e) {

            }


            setUpViewPagger();
        }
    }

    private void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


    private void followUserApi(String otherUserId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
//        dialog.show();
        Call<FollowModel> getProfileCall = service.followUserApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId);
        getProfileCall.enqueue(new Callback<FollowModel>() {
            @Override
            public void onResponse(Call<FollowModel> call, Response<FollowModel> response) {
                if (response.isSuccessful()) {
                    dismissDialog(dialog);
                    Snackbar.make(liPersonalProfileRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
//                    getProfiledata(otherUserId);
                    tvFollowers.setText(response.body().getFollowers().toString());
                    tvFollowing.setText(response.body().getFollowing().toString());
                    btnFollow.setText("Unfollow");
                    followStatus = true;

                } else {
                    dismissDialog(dialog);
                    Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("Profile_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowModel> call, Throwable t) {
                dismissDialog(dialog);
                Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void unFollowUserApi(String otherUserId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
//        dialog.show();
        Call<FollowModel> getProfileCall = service.unFollowUserApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId);
        getProfileCall.enqueue(new Callback<FollowModel>() {
            @Override
            public void onResponse(Call<FollowModel> call, Response<FollowModel> response) {
                if (response.isSuccessful()) {
                    dismissDialog(dialog);
                    Snackbar.make(liPersonalProfileRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    tvFollowers.setText(response.body().getFollowers().toString());
                    tvFollowing.setText(response.body().getFollowing().toString());
                    btnFollow.setText("Follow");
                    followStatus = false;
                } else {
                    dismissDialog(dialog);
                    Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("Profile_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowModel> call, Throwable t) {
                dismissDialog(dialog);
                Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void getFollowStatusApi(String otherUserId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
//        dialog.show();
        Call<FollowModel> getProfileCall = service.getFollowStatusApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId);
        getProfileCall.enqueue(new Callback<FollowModel>() {
            @Override
            public void onResponse(Call<FollowModel> call, Response<FollowModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        if (response.body().getFollowingStatus().equalsIgnoreCase("Y")) {
                            btnFollow.setText("Unfollow");
                            followStatus = true;
                        } else {
                            btnFollow.setText("Follow");
                            followStatus = false;
                        }
                    } else {
                        btnFollow.setText("Follow");
                        followStatus = false;
                    }

                } else {
                    btnFollow.setText("Follow");
                    followStatus = false;
                    try {
                        Log.e("follow_status_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowModel> call, Throwable t) {
                dismissDialog(dialog);
                Snackbar.make(liPersonalProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void setUpViewPagger() {
        filterDialog = new BottomSheetDialog(getActivity());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        personalProfilePagerAdapter = new PersonalProfilePagerAdapter(getChildFragmentManager(), 3, tags, profileData);
        vpPager.setAdapter(personalProfilePagerAdapter);

/*

        tabLayout.addTab(tabLayout.newTab().setText("FOR SALE(0)"), true);
        tabLayout.addTab(tabLayout.newTab().setText("TESTIMONIALS"), true);
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"), true);
*/

//        tabLayout.getTabAt(0).select();

//        personalProfilePagerAdapter = new PersonalProfilePagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), tags);
//        vpPager.setAdapter(personalProfilePagerAdapter);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(vpPager);

    }

    private void getIds(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.personal_profile_TabLayout);
        vpPager = (ViewPager) view.findViewById(R.id.personl_profile_ViewPager);
    }

    public void hideSearch() {


        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.VISIBLE);
//        ((MainActivity) getActivity()).rlTitle.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).changeOptionColor(0);

        ((MainActivity) getActivity()).rloptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popup = new PopupMenu(getActivity(), ((MainActivity) getActivity()).rloptions);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.user_profile_menu_options, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.menu_report:

                                filterDialog.setContentView(R.layout.user_profile_report_dialog);
                                LinearLayout ll_reporting_item = filterDialog.findViewById(R.id.ll_reporting_item);
                                LinearLayout l2_prohibited = filterDialog.findViewById(R.id.l2_prohibited);
                                LinearLayout l3_offensive = filterDialog.findViewById(R.id.l3_offensiveContent);
                                LinearLayout l4_fakeProfile = filterDialog.findViewById(R.id.l4_fakeProfile);
                                LinearLayout l9_cancel = filterDialog.findViewById(R.id.l9_cancel);
                                filterDialog.show();

                                l2_prohibited.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l2_prohibited);
                                        filterDialog.dismiss();
                                    }
                                });
                                l3_offensive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l3_offensive);
                                        filterDialog.dismiss();
                                    }
                                });
                                l4_fakeProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l4_fakeProfile);
                                        filterDialog.dismiss();
                                    }
                                });
                                l9_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        filterDialog.dismiss();
                                    }
                                });

                                break;
                            case R.id.menu_share:

                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBodyText = Global.DEEP_LINKING_PRODUCT_URL;
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                                break;
                        }
                        return true;
                    }
                });

            }
        });
    }

    private void hitReportApi(LinearLayout layout) {

        new ReportApi().hitReportApi(getActivity(), layout
                , profileData.getResult().getId(), "", (msg) -> {
                    Snackbar.make(liPersonalProfileRoot, msg, Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }, () -> {
                    Snackbar.make(liPersonalProfileRoot, "Please try again later", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemCountChanged(int count) {
        tabLayout.getTabAt(0).setText("For Sale (" + count + ")");
    }

    @OnClick(R.id.btn_follow)
    public void onViewClicked() {
        if (isLogined(getActivity())) {
            if (!followStatus) {
                followUserApi(otherUserId);
            } else {
                unFollowUserApi(otherUserId);
            }
        } else {
            S_Dialogs.getLoginDialog(getActivity()).show();
        }
    }

    @OnClick(R.id.li_follow)
    public void onFollowClicked() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ViewFollowListFragment(profileData.getResult().getId())).addToBackStack(PROFILETAG).commit();
    }

    public void getProductUrlApi(String productId) {
        Call<JsonObject> productUrl;
        productUrl = service.getProductUrl(HelperPreferences.get(getActivity()).getString(UID), productId, "u");
        productUrl.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            Global.DEEP_LINKING_PRODUCT_URL = jsonObject.getString("url");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}