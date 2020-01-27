package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.model.extra.EditProfileModel;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomAnimations.MyBounceInterpolator;
import com.app.admin.sellah.view.CustomDialogs.ChangePassDialog;
import com.app.admin.sellah.view.CustomDialogs.OTPVerificationDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.app.admin.sellah.view.activities.MainActivity;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILESTATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILE_EDIT_MODE_PROFILE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.view.fragments.MyAccountFragment.profileImagePathEdit;

@SuppressLint("ValidFragment")
public class AccountTabFragment extends Fragment implements View.OnClickListener {

    Dialog dialog;
    View view;
    @BindView(R.id.current_delivery_tv)
    TextView currentDeliveryTv;
    @BindView(R.id.current_delivery_edit)
    TextView currentDeliveryEdit;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.about_text)
    TextView aboutText;
    @BindView(R.id.txt_manage_blockUser)
    TextView txtManageBlockUser;
    @BindView(R.id.passChange)
    TextView passChange;
    @BindView(R.id.btn_delete_account)
    TextView btnDeleteAccount;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_profile_description)
    EditText edtProfileDescription;
    @BindView(R.id.current_delivery_name)
    TextView currentDeliveryName;
    @BindView(R.id.current_delivery_addressLine1)
    TextView currentDeliveryAddressLine1;
    @BindView(R.id.current_delivery_addressLine2)
    TextView currentDeliveryAddressLine2;
    @BindView(R.id.current_delivery_city)
    TextView currentDeliveryCity;
    @BindView(R.id.current_delivery_state)
    TextView currentDeliveryState;
    @BindView(R.id.current_delivery_city_sep)
    TextView currentDeliveryCitySep;
    @BindView(R.id.current_delivery_postCode)
    TextView currentDeliveryPostCode;
    @BindView(R.id.rel_current_address)
    RelativeLayout relCurrentAddress;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.edt_pickup_policy)
    EditText edtPickupPolicy;
    @BindView(R.id.edt_return_policy)
    EditText edtReturnPolicy;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rel_account_root)
    RelativeLayout relAccountRoot;
    @BindView(R.id.img_edit)
    ImageView imgEdit;

    @BindView(R.id.edt_profile_newaddress)
    EditText edtProfileNewaddress;
    @BindView(R.id.about_wordcount)
    TextView aboutWordcount;
    @BindView(R.id.online_wordcount)
    TextView onlineWordcount;
    @BindView(R.id.totoal)
    TextView totoal;
    private Animation myAnim;
    private ArrayList<EditText> allAddressFields;
    private ArrayList<NoChangingBackgroundTextInputLayout> allAddressInputLayouts;
    private HashMap<EditText, String> adressfieldsMessages;
    private ArrayList<EditText> allchangePassFields;

    private ArrayList<TextInputLayout> allchangePassInputLayouts;

    private HashMap<EditText, String> changePassMessages;

    private WebService service;

    Unbinder unbinder;
    View rootview;

    String addressName = "", addressline1 = "", addressline2 = "", addressState = "", addressCity = "", addresspostalCode = "";
    private int phoneLength = 8;

    private ProfileModel profileData;

    public AccountTabFragment() {

    }
    public AccountTabFragment(ProfileModel profileData) {
        this.profileData = profileData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.account_tab, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (profileData != null) {
            setdata(profileData);
        }

        currentDeliveryEdit.setOnClickListener(this);
        passChange.setOnClickListener(this);
        btnDeleteAccount.setOnClickListener(this);
        txtManageBlockUser.setOnClickListener(this);
        service = Global.WebServiceConstants.getRetrofitinstance();
        setAddress();
        setUpCodepicker();
        rootview = ((MainActivity) getActivity()).relRoot;
        validateData();
        focuslisteneres();
        return view;

    }

    private void validateData() {
        if (!TextUtils.isEmpty(edtDescription.getText().toString())) {
            edtDescription.setBackgroundResource(R.drawable.live_product_detail_background);
        }

        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setBackgroundResource(R.drawable.live_product_detail_background);
        }

        if (!TextUtils.isEmpty(edtProfileDescription.getText().toString())) {
            edtProfileDescription.setBackgroundResource(R.drawable.live_product_detail_background);
        }

        if (!TextUtils.isEmpty(edtProfileNewaddress.getText().toString())) {
            edtProfileNewaddress.setBackgroundResource(R.drawable.live_product_detail_background);
        }
        if (!TextUtils.isEmpty(etPhoneNum.getText().toString())) {
            etPhoneNum.setBackgroundResource(R.drawable.live_product_detail_background);
        }
        if (!TextUtils.isEmpty(edtDescription.getText().toString())) {
            edtDescription.setBackgroundResource(R.drawable.live_product_detail_background);
        }
        if (!TextUtils.isEmpty(edtReturnPolicy.getText().toString())) {
            edtReturnPolicy.setBackgroundResource(R.drawable.live_product_detail_background);
        }

        if (!TextUtils.isEmpty(edtPickupPolicy.getText().toString())) {
            edtPickupPolicy.setBackgroundResource(R.drawable.live_product_detail_background);
        }
    }


    private void setdata(ProfileModel profileData) {

        edtName.setText(profileData.getResult().getUsername());
        edtProfileDescription.setText(profileData.getResult().getDescription());
        edtProfileNewaddress.setText(profileData.getResult().getAddressName());
        if (!TextUtils.isEmpty(profileData.getResult().getAddressName())) {
            addressName = (profileData.getResult().getAddressName());
            addressline1 = (profileData.getResult().getAddress1());
            addressline2 = (profileData.getResult().getAddress2());
            addressState = (profileData.getResult().getState());
            addressCity = (profileData.getResult().getAddressCity());
            addresspostalCode = (profileData.getResult().getPostalCode());
            /*  currentDeliveryEdit.setText("EDIT");
            relCurrentAddress.setVisibility(View.VISIBLE);
            currentDeliveryName.setText(profileData.getResult().getAddressName() + ",");
            currentDeliveryAddressLine1.setText(profileData.getResult().getAddress1() + ", ");
            currentDeliveryState.setText(profileData.getResult().getState());
            currentDeliveryCity.setText(profileData.getResult().getAddressCity() + ", ");
            currentDeliveryPostCode.setText(profileData.getResult().getPostalCode());*/
        } else {
           /* currentDeliveryEdit.setText("ADD");
            relCurrentAddress.setVisibility(View.GONE);*/
        }

//      ccp.setcode
        ccp.setCountryForPhoneCode(Integer.parseInt(profileData.getResult().getCountryCode().replace("+", "")));
        ccp.registerCarrierNumberEditText(etPhoneNum);
        etPhoneNum.setText(profileData.getResult().getPhoneNumber());
        edtDescription.setText(profileData.getResult().getAbout());
        edtPickupPolicy.setText(profileData.getResult().getShippingPolicy());
        edtReturnPolicy.setText(profileData.getResult().getReturnPolicy());
        onlineWordcount.setText(String.valueOf(profileData.getResult().getDescription().length()));
        aboutWordcount.setText(String.valueOf(profileData.getResult().getAbout().length()));
    }


    private void setUpCodepicker() {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if (ccp.getSelectedCountryCode().equalsIgnoreCase("91")) {
                    phoneLength = 10;
                } else {
                    phoneLength = 8;
                }
            }
        });
    }

    private void setAddress() {
        if (!TextUtils.isEmpty(addressName)) {

            relCurrentAddress.setVisibility(View.GONE);
            edtProfileNewaddress.setText(addressName);
            currentDeliveryAddressLine1.setText(addressline1 + ", ");
            currentDeliveryAddressLine2.setText(addressline2 + ", ");
            currentDeliveryState.setText(addressState);
            currentDeliveryCity.setText(addressCity + ", ");
            currentDeliveryPostCode.setText(addresspostalCode);
        }
    }

    public void currentDeliveryDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.current_delivery_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        EditText postal = dialog.findViewById(R.id.postal);
        EditText address1 = dialog.findViewById(R.id.edt_add_address1);
        EditText address2 = dialog.findViewById(R.id.edt_add_address2);
        EditText name = dialog.findViewById(R.id.edt_add_name);
        Spinner state = dialog.findViewById(R.id.spin_state);
        Spinner city = dialog.findViewById(R.id.spin_city);

        if (!TextUtils.isEmpty(addressName)) {
            name.setText(addressName);
            address1.setText(addressline1);
            address2.setText(addressline2);
            postal.setText(addresspostalCode);
        }

        NoChangingBackgroundTextInputLayout il_name = dialog.findViewById(R.id.il_name);
        NoChangingBackgroundTextInputLayout il_add1 = dialog.findViewById(R.id.il_add_1);
        NoChangingBackgroundTextInputLayout il_add2 = dialog.findViewById(R.id.il_add_2);
        NoChangingBackgroundTextInputLayout il_postal = dialog.findViewById(R.id.il_postal);

        allAddressFields = new ArrayList<>();
        allAddressFields.add(name);
        allAddressFields.add(address1);
//        allAddressFields.add(address2);
        allAddressFields.add(postal);

        allAddressInputLayouts = new ArrayList<>();
        allAddressInputLayouts.add(il_name);
        allAddressInputLayouts.add(il_add1);
//        allAddressInputLayouts.add(il_add2);
        allAddressInputLayouts.add(il_postal);


        adressfieldsMessages = new HashMap<>();
        adressfieldsMessages.put(name, "Please enter your name");
        adressfieldsMessages.put(address1, "Please enter address 1");
//        adressfieldsMessages.put(address1, "Please enter address 2");
        adressfieldsMessages.put(postal, "Please enter postal code");


        TextView submit = dialog.findViewById(R.id.submit);
        ImageView back = dialog.findViewById(R.id.back_img);
//        TextInputEditText back = dialog.findViewById(R.id.back_img);

        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        back.startAnimation(myAnim);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(), "weyqdfueyfuyru", Toast.LENGTH_SHORT).show();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                dialog.dismiss();
            }
        });

       /*back.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               Toast.makeText(getActivity(), "weyqdfueyfuyru", Toast.LENGTH_SHORT).show();

               if(dialog!=null&&dialog.isShowing()){
                   dialog.dismiss();
               }
               return false;
           }
       });
*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAddressFormValid()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    /*if (Global.getText(postal).length() < 6 && Global.getText(postal).length() > 6) {
                        il_postal.setError("Invalid postal code");
                    } else {*/
                    il_postal.setErrorEnabled(false);
                    addressName = Global.getText(name);
                    addressline1 = Global.getText(address1);
                    addressline2 = Global.getText(address2);
                    addressState = state.getSelectedItem().toString();
                    addressCity = city.getSelectedItem().toString();
                    addresspostalCode = Global.getText(postal);
                    setAddress();
//                    }

                }

            }
        });
        dialog.show();

    }


    public void passChange() {
      /*  dialog = new Dialog(getActivity());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.account_password_change);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        EditText edt_current_pass = dialog.findViewById(R.id.edt_current_pass);
        EditText edt_new_pass = dialog.findViewById(R.id.edt_new_pass);
        EditText edt_confirm_pass = dialog.findViewById(R.id.edt_confirm_pass);

        NoChangingBackgroundTextInputLayout il_curr_pass = dialog.findViewById(R.id.il_curr_pass);
        NoChangingBackgroundTextInputLayout il_new_pass = dialog.findViewById(R.id.il_new_pass);
        NoChangingBackgroundTextInputLayout il_confirm_pass = dialog.findViewById(R.id.il_confirm_pass);
//        NoChangingBackgroundTextInputLayout il_postal= dialog.findViewById(R.id.il_postal);

        allchangePassFields = new ArrayList<>();
        allchangePassFields.add(edt_current_pass);
        allchangePassFields.add(edt_new_pass);
        allchangePassFields.add(edt_confirm_pass);

        allchangePassInputLayouts = new ArrayList<>();
        allchangePassInputLayouts.add(il_curr_pass);
        allchangePassInputLayouts.add(il_new_pass);
        allchangePassInputLayouts.add(il_confirm_pass);


        changePassMessages = new HashMap<>();
        changePassMessages.put(edt_current_pass, "Please enter current password");
        changePassMessages.put(edt_new_pass, "Please enter new password");
        changePassMessages.put(edt_confirm_pass, "Please enter password");

        Button btn_change = dialog.findViewById(R.id.btn_change);

        ImageView back = dialog.findViewById(R.id.back_img);

        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        back.startAnimation(myAnim);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(), "weyqdfueyfuyru", Toast.LENGTH_SHORT).show();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                dialog.dismiss();
            }
        });


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               *//* if (edt_current_pass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter your cu", Toast.LENGTH_SHORT).show();
                } else {

                }*//*
                if (isChangePassFormValid()) {

                    if (Global.getText(edt_confirm_pass).equalsIgnoreCase(Global.getText(edt_new_pass))) {
                        il_confirm_pass.setErrorEnabled(false);
                        Dialog loadingdialog = S_Dialogs.getLoadingDialog(getActivity());
                        loadingdialog.show();

                        Call<Common> changePassCall = service.changePasswordApi(HelperPreferences.get(getActivity()).getString(UID), Global.getText(edt_current_pass), Global.getText(edt_new_pass), Global.getText(edt_confirm_pass));
                        changePassCall.enqueue(new Callback<Common>() {
                            @Override
                            public void onResponse(Call<Common> call, Response<Common> response) {
                                if (response.isSuccessful()) {
                                    Common result = response.body();

                                    if (loadingdialog != null && loadingdialog.isShowing()) {
                                        loadingdialog.dismiss();
                                    }
                                    Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("ChagePassError", " : " + result.getMessage());
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }

                                } else {
                                    if (loadingdialog != null && loadingdialog.isShowing()) {
                                        loadingdialog.dismiss();
                                    }
                                    Toast.makeText(getActivity(), "Password is either same as previous or incorrect ", Toast.LENGTH_SHORT).show();

                                    Log.e("ChagePassError", " : " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Common> call, Throwable t) {
                                if (loadingdialog != null && loadingdialog.isShowing()) {
                                    loadingdialog.dismiss();
                                }
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        il_confirm_pass.setError("Password not Matched");
                    }
                }

            }
        });
        dialog.show();*/
        new ApisHelper().sendOtpChangePassApi(getActivity(), new ApisHelper.OTPSentCallBack() {
            @Override
            public void onOTPSentSuccess(ResendCode body) {
                ChangePassDialog.create(getActivity(), body.getResult().getVerificationCode()).show();
            }

            @Override
            public void onOTPSentFailure() {

            }
        });
//        ChangePassDialog.create(getActivity()).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_delivery_edit:
                currentDeliveryDialog();
                break;

            case R.id.passChange:
                passChange();
                break;
            case R.id.btn_delete_account:
                S_Dialogs.getDeleteAccountDilaog(getActivity(), (dialog, which) -> {
                    deleteAccountApi();
                }).show();
                break;
            case R.id.txt_manage_blockUser:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ManageBlockUser()).addToBackStack(null).commit();
                break;
        }
    }

    private void deleteAccountApi() {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> deleteAccountCall = service.deleteAccountApi(HelperPreferences.get(getActivity()).getString(UID));
        deleteAccountCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        HelperPreferences.get(getActivity()).clear();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(rootview, "Please try again later.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(rootview, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    protected boolean isAddressFormValid() {
        for (EditText editText : allAddressFields) {
            if (editText.getText().toString().trim().isEmpty()) {
                allAddressInputLayouts.get(allAddressFields.indexOf(editText))
                        .setError(adressfieldsMessages.get(editText));
//                requestFocus(editText);
                return false;
            } else {
                allAddressInputLayouts.get(allAddressFields.indexOf(editText)).setErrorEnabled(false);
            }
        }
        return true;
    }

    protected boolean isChangePassFormValid() {

        for (EditText editText : allchangePassFields) {
            if (editText.getText().toString().trim().isEmpty()) {
                allchangePassInputLayouts.get(allchangePassFields.indexOf(editText))
                        .setError(changePassMessages.get(editText));
//                requestFocus(editText);
                return false;
            } else {
                allchangePassInputLayouts.get(allchangePassFields.indexOf(editText)).setErrorEnabled(false);
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {

        if (TextUtils.isEmpty(Global.getText(edtName))) {
            Snackbar.make(rootview, "Please enter your name.", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtName);
        } else if (TextUtils.isEmpty(Global.getText(edtProfileDescription))) {
            Snackbar.make(rootview, "Please enter one line description.", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtProfileDescription);
        } else if (TextUtils.isEmpty(edtProfileNewaddress.getText().toString())) {
            Snackbar.make(rootview, "Please add address.", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtProfileDescription);
        } /*else if (Global.getText(etPhoneNum).length() == 0 || Global.getText(etPhoneNum).length() < phoneLength || Global.getText(etPhoneNum).length() > phoneLength) {
            Snackbar.make(rootview, "Number must be " + phoneLength + " digit", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), etPhoneNum);
        }*/ /*else if (TextUtils.isEmpty(Global.getText(edtDescription))) {
            Snackbar.make(rootview, "Please describe yourself", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtDescription);
        } else if (TextUtils.isEmpty(Global.getText(edtPickupPolicy))) {
            Snackbar.make(rootview, "Please enter shipping/pickup polices", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtPickupPolicy);
        } else if (TextUtils.isEmpty(Global.getText(edtReturnPolicy))) {
            Snackbar.make(rootview, "Please enter return/refund polices", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
            Global.requestFocus(getActivity(), edtReturnPolicy);
        }*/ else {
            /*String actualPhoneNo= ccp.getFullNumberWithPlus().substring(ccp.getSelectedCountryCodeWithPlus().length());
            Log.e("ccp_number", "signupClick: "+actualPhoneNo);
*/
            clearFocus(relAccountRoot);
            Log.e("EditProfileValidations", "validate");
            EditProfileModel model = new EditProfileModel();
            model.setUser_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(getActivity()).getString(UID))));
            model.setUsername(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtName)));
            model.setDescription(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtProfileDescription)));
            model.setCountry_code(RequestBody.create(MediaType.parse("text/plain"), ccp.getSelectedCountryCodeWithPlus()));
            model.setPhone_number(RequestBody.create(MediaType.parse("text/plain"), ""));
            model.setAbout(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtDescription)));
            model.setShipping_policy(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtPickupPolicy)));
            model.setReturn_policy(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtReturnPolicy)));
            model.setAddress_name(RequestBody.create(MediaType.parse("text/plain"), edtProfileNewaddress.getText().toString()));
            model.setAddress_1(RequestBody.create(MediaType.parse("text/plain"), addressline1));
            model.setAddress_2(RequestBody.create(MediaType.parse("text/plain"), addressline2));
            model.setPostal_code(RequestBody.create(MediaType.parse("text/plain"), addresspostalCode));
            model.setState(RequestBody.create(MediaType.parse("text/plain"), addressState));
            model.setAddress_city(RequestBody.create(MediaType.parse("text/plain"), addressCity));
            model.setEdit_mode(RequestBody.create(MediaType.parse("text/plain"), PROFILE_EDIT_MODE_PROFILE));

            if (!TextUtils.isEmpty(profileImagePathEdit)) {
                model.setImage(ImageUploadHelper.convertImageTomultipart(profileImagePathEdit, "image"));
            }
            updateProfile(model);
        }
    }

    private void updateProfile(EditProfileModel model) {

        Log.e("model", model.getAbout() + ":" + model.getShipping_policy() + ":" + model.getReturn_policy());

        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();

        Call<Common> editProfileCall = service.editProfileApi(model.getUser_id(), model.getUsername()
                , model.getDescription(), model.getCountry_code(), model.getPhone_number(), model.getAbout(), model.getShipping_policy()
                , model.getReturn_policy(), model.getAddress_name(), model.getAddress_1(), model.getAddress_2(), model.getPostal_code(), model.getState()
                , model.getAddress_city(), model.getImage(), model.getEdit_mode());

        editProfileCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Global.ProfileStatusCheck.checkProfileStatus(getActivity(), new Global.ProfileStatusCheck.ProfileStatusCallback() {
                        @Override
                        public void onIfProfileUpdated() {
                            Snackbar.make(rootview, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();
                        }

                        @Override
                        public void onIfProfileNotUpdated() {
                            HelperPreferences.get(getActivity()).saveString(PROFILESTATUS, "Y");
                            ((MainActivity) getActivity()).loadFragment(new HomeFragment(), HOMETAG);
                            ((MainActivity) getActivity()).changeOptionColor(0);
                        }
                    });


                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(rootview, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(rootview, t.getMessage(), Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void clearFocus(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
//                ((EditText) view).setText("");
                Global.clearFocus(getActivity(), view);
            } else if (view instanceof Spinner) {
//                ((Spinner)view).setSelection(0);
            } else if (view instanceof CheckBox) {
//                ((CheckBox)view).setSelected(false);
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearFocus((ViewGroup) view);
        }
    }

    @OnClick(R.id.img_edit)
    public void onEditPhoneNumberClicked() {
        String actualPhoneNo = ccp.getFullNumberWithPlus().substring(ccp.getSelectedCountryCodeWithPlus().length());
        Log.e("ccp_number", "signupClick: " + actualPhoneNo);

        if (ccp.isValidFullNumber()) {
            updatePhoneApi(actualPhoneNo, ccp.getSelectedCountryCodeWithPlus());
        } else {
            Snackbar.make(rootview, "Invalid phone number.", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }

    }

    private void updatePhoneApi(String phNumber, String selectedCountryCode) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();

        Call<ResendCode> updatePhoneNoCall = service.upDatePhone(HelperPreferences.get(getActivity()).getString(UID), phNumber, selectedCountryCode);
        updatePhoneNoCall.enqueue(new Callback<ResendCode>() {
            @Override
            public void onResponse(Call<ResendCode> call, Response<ResendCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                       /* OTPVerificationDialog.create(getActivity(), HelperPreferences.get(getActivity()).getString(UID), "update", response.body().getResult().getVerificationCode(),
                                new OTPVerificationDialog.OTPVerificationListener() {
                                    @Override
                                    public void onOTPVerified() {
                                        Snackbar.make(rootview, "Phone number updated successfully", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }

                                    @Override
                                    public void onOTPNotVerified() {
                                        etPhoneNum.setText(profileData.getResult().getPhoneNumber());
                                        ccp.setCountryForPhoneCode(Integer.parseInt(profileData.getResult().getCountryCode().replace("+", "")));
                                    }
                                }).show();*/
                        OTPVerificationDialog.create(getActivity(), HelperPreferences.get(getActivity()).getString(UID), selectedCountryCode, "update", phNumber, response.body().getResult().getVerificationCode(),
                                new OTPVerificationDialog.OTPVerificationListener() {
                                    @Override
                                    public void onOTPVerified() {
                                        Snackbar.make(rootview, "Phone number updated successfully", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }

                                    @Override
                                    public void onOTPNotVerified() {
                                        etPhoneNum.setText(profileData.getResult().getPhoneNumber());
                                        ccp.setCountryForPhoneCode(Integer.parseInt(profileData.getResult().getCountryCode().replace("+", "")));
                                    }
                                }).show();
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("Update_phone_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(rootview, "Please try another number", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<ResendCode> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(rootview, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
                Log.e("Update_phone_failure", t.getMessage());
            }
        });
    }

    private void focuslisteneres() {

        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    edtName.setBackgroundResource(R.drawable.live_product_detail_red_background);
                } else {
                    if (edtName != null)
                        edtName.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });


        edtProfileDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    edtProfileDescription.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    if (edtProfileDescription != null)
                        edtProfileDescription.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });

        edtProfileNewaddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    edtProfileNewaddress.setBackgroundResource(R.drawable.live_product_detail_red_background);

                } else {
                    if (edtProfileNewaddress != null)
                        edtProfileNewaddress.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });

        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {


                if (b) {
                    etPhoneNum.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    if (etPhoneNum != null)
                        etPhoneNum.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });


        edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {


                if (b) {
                    edtDescription.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    if (edtDescription != null)
                        edtDescription.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });

        edtPickupPolicy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    edtPickupPolicy.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    if (edtPickupPolicy != null)

                        edtPickupPolicy.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });


        edtReturnPolicy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    edtReturnPolicy.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    if (edtPickupPolicy != null)
                        edtReturnPolicy.setBackgroundResource(R.drawable.live_product_detail_background);

                }
            }
        });

        edtProfileDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onlineWordcount.setText(String.valueOf(charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aboutWordcount.setText(String.valueOf(charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    public void check() {

        if (TextUtils.isEmpty(edtDescription.getText().toString())) {
            edtDescription.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtProfileDescription.getText().toString())) {
            edtProfileDescription.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtProfileNewaddress.getText().toString())) {
            edtProfileNewaddress.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }
        if (TextUtils.isEmpty(etPhoneNum.getText().toString())) {
            etPhoneNum.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }
        if (TextUtils.isEmpty(edtDescription.getText().toString())) {
            edtDescription.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtPickupPolicy.getText().toString())) {
            edtPickupPolicy.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }
        if (TextUtils.isEmpty(edtReturnPolicy.getText().toString())) {
            edtReturnPolicy.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }
    }


}

