package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class PersonalProfileAboutFragment extends Fragment {

    @BindView(R.id.txt_about_user)
    TextView txtAboutUser;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.txt_shipping_policy)
    TextView txtShippingPolicy;
    @BindView(R.id.txt_return_policy)
    TextView txtReturnPolicy;
   ProfileModel profileModel;
    public PersonalProfileAboutFragment(ProfileModel profileData) {
    profileModel=profileData;
    }

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_profile_about_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(profileModel!=null){
            setdata(profileModel);
        }

        return view;
    }

    private void setdata(ProfileModel profileModel) {

        txtAboutUser.setText(profileModel.getResult().getAbout());
        txtShippingPolicy.setText(profileModel.getResult().getShippingPolicy());
        txtReturnPolicy.setText(profileModel.getResult().getReturnPolicy());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
