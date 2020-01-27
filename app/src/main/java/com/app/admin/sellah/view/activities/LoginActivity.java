package com.app.admin.sellah.view.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.DeviceRegistaration;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.LoginPojo.LoginResult;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.view.CustomDialogs.OTPVerificationDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.utils.Global.deleted_account;
import static com.app.admin.sellah.controller.utils.Global.makeLinks;
import static com.app.admin.sellah.controller.utils.Global.setStatusBarColor;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.ADVERTISEMENTSTATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.DEEP_LINKING;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILESTATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.txt_killer_quote)
    TextView txtKillerQuote;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.signUp_relative)
    RelativeLayout signUpRelative;
    @BindView(R.id.txt_header)
    TextView txtHeader;
    @BindView(R.id.t_email)
    TextView tEmail;
    @BindView(R.id.t_pass)
    TextView tPass;
    @BindView(R.id.text)
    TextView text;
    private String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.etLogin_email)
    EditText loginEmail;
    @BindView(R.id.etLogin_pass)
    EditText loginPass;
    @BindView(R.id.signIn)
    Button loginSignIn;
    @BindView(R.id.signUp)
    TextView loginSignup;
    @BindView(R.id.forgotPass)
    TextView forgot;
    @BindView(R.id.root)
    RelativeLayout rel_root;
    WebService webService;

    String product_id="",product_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        makeTransperantStatusBar(this,false);
        setContentView(R.layout.layout_signin_new);
        setStatusBarColor(this,R.color.colorFormBg);
//        StatusBarLightMode(this);//set status bar light mode
        ButterKnife.bind(LoginActivity.this);// view ids injection with ButterKnife.
        webService = Global.WebServiceConstants.getRetrofitinstance();// Get retrofit instance.


        //------redirect deep link-login-specific item----------
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(DEEP_LINKING))
        {
            product_id = intent.getStringExtra("product_id");
            product_type = intent.getStringExtra("product_type");
        }
        else
        {
            product_id="";
            product_type="";
        }

            //----------------------------------------------------



        forgot.setLinkTextColor(Color.BLACK); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

               /* Global.getResetPasswordDialog(LoginActivity.this, (dialog, input) -> {
                    String email = input.toString().trim();
                    if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(LoginActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    } else {
//-----------------------------Forget password Api--------------------------------------------------
                        hitForgetApi(email, dialog);
//                dialog.dismiss();
                    }
                }).show();*/
                Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
//                finish();
//                Toast.makeText(getActivity(), "Normal Link", Toast.LENGTH_SHORT).show();
            }

        };
        makeLinks(forgot, new String[] {
                "I forgot my password?"
        }, new ClickableSpan[] {
                normalLinkClickSpan
        });
        loginSignup.setLinkTextColor(Color.BLACK); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan loginSignUp = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                if (!product_id.equalsIgnoreCase("")  && !product_type.equalsIgnoreCase(""))
                {
                    intent.putExtra(DEEP_LINKING,"deeplink");
                    intent.putExtra("product_id",product_id);
                    intent.putExtra("product_type",product_type);
                }
                startActivity(intent);
            }

        };
        makeLinks(loginSignup, new String[] {
                "Register"
        }, new ClickableSpan[] {
                loginSignUp
        });
    }

    @OnClick({R.id.signIn})
    void signinClick() {
//        Global.hideKeyboard(rel_root,this);
        if (getText(loginEmail).equalsIgnoreCase("")) {
            Snackbar.make(rel_root, "Please Enter E-mail", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();

        } else if (getText(loginPass).equals("")) {
            Snackbar.make(rel_root, "Please Enter Password ", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();


        } else if ((!Patterns.EMAIL_ADDRESS.matcher(getText(loginEmail)).matches())) {
            Snackbar.make(rel_root, "Invalid E-mail Address", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();

        }/*else if (getText(loginEmail).length() == 0 && getText(loginPass).length() < 6) {
            Snackbar.make(rel_root, "Password must have at-least 6 characters", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();

        }*/ else if (Patterns.EMAIL_ADDRESS.matcher(getText(loginEmail)).matches() && getText(loginPass).length() > 0) {
            /*Snackbar.make(rel_root, "Login Successfully", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();


            editor.putString("email", getText(loginEmail));
            editor.commit();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/

//            new DeviceRegistaration().registerDevice(this,3);

            Dialog dialog = S_Dialogs.getLoadingDialog(this);
            dialog.show();

            Call<LoginResult> loginCall = webService.loginApi(getText(loginEmail), getText(loginPass));
            loginCall.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    LoginResult loginResult = response.body();
                    if (response.isSuccessful()) {



                        if (loginResult.getStatus().equalsIgnoreCase("1")) {
                            Snackbar.make(rel_root, loginResult.getMessage(), Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();





                            if (loginResult.getResult().getIsDeleted().equalsIgnoreCase("Y"))
                            {


                                S_Dialogs.getLiveVideoStopedDialog(LoginActivity.this, "You have deleted your account. \n" +
                                        "To recover your account. \n" +
                                        "Please verify your phone number.", ((dialog, which) -> {
                                    //--------------openHere-----------------

                                    otpsendapi(loginResult);
                                    deleted_account = true;

                                })).show();

                            }

                           else if (loginResult.getResult().getIsVerified().equalsIgnoreCase("N")) {

                                /**
                                 * Api to complete OTP verification Process if OTP is not verified.
                                 * */

                                otpsendapi(loginResult);
                                deleted_account = false;



                            } else {

                                /**
                                 *Do: Redirect user to main screen is OTP verification process is already completed.
                                 *Checks: Is profile updated by user or not.
                                 *Decisions: Redirect to edit profile mode in case profile is not updated.
                                 * */
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();*/

                                        HelperPreferences.get(LoginActivity.this).saveString(UID, loginResult.getResult().getUserId());
                                        HelperPreferences.get(LoginActivity.this).saveString(PROFILESTATUS, loginResult.getResult().getIsProfileCompleted());
                                        HelperPreferences.get(LoginActivity.this).saveString(API_ACCESS_TOKEN, loginResult.getResult().getStripe_id());
                                        HelperPreferences.get(LoginActivity.this).saveString(STRIPE_VERIFIED, loginResult.getResult().getStripe_verified());
                                        HelperPreferences.get(LoginActivity.this).saveString(ADVERTISEMENTSTATUS, "show");
                                        Global.ProfileStatusCheck.checkProfileStatus(LoginActivity.this, new Global.ProfileStatusCheck.ProfileStatusCallback() {
                                            @Override
                                            public void onIfProfileUpdated() {


                                                if (!product_id.equalsIgnoreCase("")  && !product_type.equalsIgnoreCase(""))
                                                {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(DEEP_LINKING, "deeplink");
                                                    bundle.putString("product_id", product_id);
                                                    bundle.putString("product_type", product_type);

                                                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                    resultIntent.putExtras(bundle);
                                                    startActivity(resultIntent);
                                                    finish();

                                                }
                                                else
                                                {
                                                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(resultIntent);
                                                    finish();
                                                }
                                             //    onBackPressed();

                                                new DeviceRegistaration().registerDevice(LoginActivity.this, loginResult.getResult().getUserId());
                                            }

                                            @Override
                                            public void onIfProfileNotUpdated() {
                                                Log.e(TAG, "onIfProfileNotUpdated:Profile not updated");


                                                if (!product_type.equalsIgnoreCase("") && !product_id.equalsIgnoreCase(""))
                                                {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra(DEEP_LINKING,"deeplink");
                                                    intent.putExtra("product_id",product_id);
                                                    intent.putExtra("product_type",product_type);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                   // finish();
                                                 //   finishAffinity();
                                                }
                                                else
                                                {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra(PROFILESTATUS, "abc");
                                                startActivity(intent);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                //    finishAffinity();
                                                }
                                               /* new ApisHelper().getProfileData(LoginActivity.this, new ApisHelper.GetProfileCallback() {
                                                    @Override
                                                    public void onGetProfileSuccess(ProfileModel data) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.putExtra(PROFILESTATUS,"abc");
                                                        startActivity(intent);
                                                        finish();
                                                       *//* Bundle bundle = new Bundle();
                                                        bundle.putParcelable(SAConstants.Keys.PROFILE_DATA, data);
                                                        Intent in =new Intent(LoginActivity.this,MainActivity.class);
                                                        in.putExtras(bundle);
                                                        startActivity(in);
                                                        finishAffinity();*//*
                                                    }
                                                    @Override
                                                    public void onGetProfileFailure() {

                                                    }
                                                });*/

                                            }
                                        });
                                    }

                                }, Snackbar.LENGTH_SHORT);
                            }
                           /* new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    HelperPreferences.get(LoginActivity.this).saveInt(UID,loginResult.getResult().getUserId());
                                }

                            }, Snackbar.LENGTH_SHORT);*/
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Snackbar.make(rel_root, loginResult.getMessage(), Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();
                        }
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            Log.e(TAG, "onResponse: "+response.errorBody().string() );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(rel_root, "Invalid email or password", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("errorPrint",t.getMessage());
                    Snackbar.make(rel_root, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            });
        } else {
            Snackbar.make(rel_root, "Invalid field data", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }


    }

  /*  @OnClick({R.id.signUp})
    void signUpClick() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }*/

    /**
     * Method to get text from EditText view
     */
    public String getText(EditText view) {
        String result = view.getText().toString().trim();
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void otpsendapi(LoginResult loginResult)
    {
        Call<ResendCode> recendCodeCall = webService.reSendOTPApi(loginResult.getResult().getUserId(), "");
        recendCodeCall.enqueue(new Callback<ResendCode>() {
            @Override
            public void onResponse(Call<ResendCode> call, Response<ResendCode> response) {
                Log.e("ResendCode_response", response.isSuccessful() + "");

                if (response.isSuccessful()) {
                    ResendCode resendCode = response.body();
                    if (resendCode.getStatus().equalsIgnoreCase("1")) {
                        Log.e("ResendCode_response", resendCode.getResult().getVerificationCode() + "");
                        Toast.makeText(LoginActivity.this, resendCode.getMessage(), Toast.LENGTH_SHORT).show();

                        /**
                         * OTPVerificationDialog
                         * By: Raghubeer singh Virk
                         * To: verify otp from api and entered otp from user.
                         * Provide: user interface to show resend otp options,timer, Pin view to enter otp.
                         * */
                        OTPVerificationDialog.create(LoginActivity.this, loginResult.getResult().getUserId(), loginResult.getResult().getCountryCode(), "", loginResult.getResult().getPhoneNumber(), resendCode.getResult().getVerificationCode(),
                                new OTPVerificationDialog.OTPVerificationListener() {
                                    @Override
                                    public void onOTPVerified() {

                                        /*
                                         * OTP verification success listener
                                         * */
                                        HelperPreferences.get(LoginActivity.this).saveString(STRIPE_VERIFIED, loginResult.getResult().getStripe_verified());
                                        HelperPreferences.get(LoginActivity.this).saveString(API_ACCESS_TOKEN, loginResult.getResult().getStripe_id());

                                        HelperPreferences.get(LoginActivity.this).saveString(UID, resendCode.getResult().getUserId());
                                        HelperPreferences.get(LoginActivity.this).saveString(PROFILESTATUS, loginResult.getResult().getIsProfileCompleted());
                                        onBackPressed();
                                        new DeviceRegistaration().registerDevice(LoginActivity.this, resendCode.getResult().getUserId());

                                                                /*Global.ProfileStatusCheck.checkProfileStatus(LoginActivity.this, new Global.ProfileStatusCheck.ProfileStatusCallback() {
                                                                    @Override
                                                                    public void onIfProfileUpdated() {
                                                                        onBackPressed();
                                                                        new DeviceRegistaration().registerDevice(LoginActivity.this, resendCode.getResult().getUserId());
                                                                    }

                                                                    @Override
                                                                    public void onIfProfileNotUpdated() {
                                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                        intent.putExtra(PROFILESTATUS,"abc");
                                                                        startActivity(intent);
                                                                        finish();
                                                                        Log.e(TAG, "onIfProfileNotUpdated:Profile not updated");

                                                                    }
                                                                });*/
                                    }

                                    @Override
                                    public void onOTPNotVerified() {

                                    }
                                }
                                /*() -> {
                                 *//* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finishAffinity();*//*
                                                            onBackPressed();
                                                            new DeviceRegistaration().registerDevice(LoginActivity.this,resendCode.getResult().getUserId());
                                                            HelperPreferences.get(LoginActivity.this).saveString(UID, resendCode.getResult().getUserId());
                                                        }*/).show();
                    } else {
                        Toast.makeText(LoginActivity.this, resendCode.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Something went's wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendCode> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

   /* @OnClick(R.id.forgotPass)
    public void onViewClicked() {

        *//*
         * Material dialog for forget password process*//*

        Global.getResetPasswordDialog(this, (dialog, input) -> {
            String email = input.toString().trim();
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            } else {
//-----------------------------Forget password Api--------------------------------------------------
                hitForgetApi(email, dialog);
//                dialog.dismiss();
            }
        }).show();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatusBarColor(this,R.color.colorWhite);
    }

 /*   private void hitForgetApi(String email, MaterialDialog dialog) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(LoginActivity.this);
        dialog1.show();
        Call<Common> forgetApiCall = webService.forgetPasswordApi(email);
        forgetApiCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rel_root, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Snackbar.make(rel_root, "No account found linked with this email-Id", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                dialog.dismiss();
                Snackbar.make(rel_root, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }*/

}


