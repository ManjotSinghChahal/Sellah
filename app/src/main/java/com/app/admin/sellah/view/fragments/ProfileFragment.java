package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.ProfilePagerAdapter;
import com.app.admin.sellah.view.adapter.SalesAdapter;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.USERCITY;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.AVAILABLE_BALANCE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.DEEP_LINKING;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PENDING_BALANCE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.QRCODE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_EMAIL;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_PROFILE_PIC;

public class    ProfileFragment extends Fragment implements SalesAdapter.TabTextController {

    @BindView(R.id.li_profile_root)
    LinearLayout liProfileRoot;
    @BindView(R.id.img_profile_pic)
    CircleImageView imgProfilePic;
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.tv_proffesion)
    TextView tvProffesion;
    @BindView(R.id.tv_followers_count)
    TextView tvFollowersCount;
    @BindView(R.id.tv_following_count)
    TextView tvFollowingCount;
    @BindView(R.id.btn_edit_profile)
    Button btnEditProfile;
    @BindView(R.id.txt_rationg)
    TextView txtRationg;
    @BindView(R.id.profile_TabLayout)
    TabLayout profileTabLayout;
    @BindView(R.id.profile_ViewPager)
    ViewPager profileViewPager;
    WebService service;
    Unbinder unbinder;
    ProfileModel profileData;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.li_follow_list)
    LinearLayout liFollowList;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.profile_availableBal_txt)
    TextView profileAvailableBalTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

//        btnEditProfile = (Button) view.findViewById(R.id.btn_edit_profile);



        service = Global.WebServiceConstants.getRetrofitinstance();
       // createViewPager(profileViewPager,"0");
        profileTabLayout.setupWithViewPager(profileViewPager);
        hideSearch();
        getProfileData();
        getSaledata();

/*        if (getArguments().containsKey(DEEP_LINKING))
        {
            Log.e("valProduct",getArguments().getString("product_id"));
        }*/



        return view;

    }

//----------------------------------------------------Get profile Api-----------------------------------------

    public void getProfileData() {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<ProfileModel> getProfileCall = service.getProfileApi(HelperPreferences.get(getActivity()).getString(UID));
        getProfileCall.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.isSuccessful()) {
                    dismissDialog(dialog);
                    HelperPreferences.get(getActivity()).saveString(USER_PROFILE_PIC, response.body().getResult().getImage());
                    HelperPreferences.get(getActivity()).saveString(USER_EMAIL, response.body().getResult().getEmail());
                    HelperPreferences.get(getActivity()).saveString(PENDING_BALANCE, response.body().getResult().getPendingBal());
                    HelperPreferences.get(getActivity()).saveString(AVAILABLE_BALANCE, response.body().getResult().getAvailableBal());
                    HelperPreferences.get(getActivity()).saveString(QRCODE, response.body().getResult().getQrCode());
                    HelperPreferences.get(getActivity()).saveString(QRCODE, response.body().getResult().getQrCode());
                    try {
                        profileAvailableBalTxt.setText("S$ " + response.body().getResult().getPendingBal());
                        profileData = response.body();
                        setProfileData(response.body());
                    }catch (Exception e){
                        Log.e("errorPrint",e.getMessage());
                    }

                } else {
                    dismissDialog(dialog);

                    Snackbar.make(liProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
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
                Snackbar.make(liProfileRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void setProfileData(ProfileModel body) {

        if (body != null) {
            try {
                txtRationg.setText(body.getResult().getRating());

                if (!TextUtils.isEmpty((HelperPreferences.get(getActivity()).getString(USERCITY))))
                    tvLocation.setText((HelperPreferences.get(getActivity()).getString(USERCITY)));
                else
                    tvLocation.setText("Singapore");

                if (!TextUtils.isEmpty(body.getResult().getUsername())) {
                    tvProfileName.setText(body.getResult().getUsername());
                } else {
                    tvProfileName.setText("Sellah! user");
                }

                if (!TextUtils.isEmpty(body.getResult().getDescription())) {
                    tvProffesion.setText(body.getResult().getDescription());
                } else {
                    tvProffesion.setText("NA");
                }
                RequestOptions requestOptions = Global.getGlideOptions();
                Picasso.with(getActivity()).load(body.getResult().getImage()).fit().centerCrop().
                        into(imgProfilePic);

             //   tvFollowersCount.setText(profileData.getResult().getFollowers());
             //   tvFollowingCount.setText(profileData.getResult().getFollowing());


                try {
                    tvFollowersCount.setText(String.valueOf(profileData.getResult().getFollowers()));
                    tvFollowingCount.setText(String.valueOf(profileData.getResult().getFollowing()));

                }catch (Exception e){

                }

            } catch (Exception e) {
                Log.e("errorPrint",e.getMessage());
            }
        }
    }

    private void createViewPager(ViewPager viewPager,String saleSize) {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getChildFragmentManager());
        adapter.addFrag(new ProfileSalesFragment(), "For Sale("+saleSize+")");
        adapter.addFrag(new WishListFragment(), "Wishlist");
        adapter.addFrag(new ProfileRecordFragment(), "Transactions");
        viewPager.setAdapter(adapter);
    }

    public void hideSearch() {
        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Profile");
//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }



    @OnClick(R.id.btn_edit_profile)
    public void onEditButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("from","my_account");
        bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, profileData);
        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();
    }

    @Override
    public void tabTextController(int count) {
        try {
            // profileTabLayout.getTabAt(0).setText("For Sale (" + count + ")");
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.li_follow_list)
    public void onFollowListClicked() {

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ViewFollowListFragment(HelperPreferences.get(getActivity()).getString(UID))).addToBackStack(PROFILETAG).commit();

    }

    @Override
    public void onStop() {
        super.onStop();
        new ApisHelper().cancel_striipe_request();
    }

    @OnClick(R.id.profile_availableBal_txt)
    public void onViewClicked() {

        Bundle bundle = new Bundle();
        bundle.putString("from","wallet");
        bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, profileData);
        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();


    }




    private void getSaledata() {
        Call<GetProductList> recordsCall;
        recordsCall = service.getForSalelistApi(HelperPreferences.get(getActivity()).getString(UID));
        recordsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("1"))
                    {

                        createViewPager(profileViewPager,response.body().getResult().size()+"");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                Log.e("successMethod",t.getMessage()+"  error");
            }
        });
    }




}





