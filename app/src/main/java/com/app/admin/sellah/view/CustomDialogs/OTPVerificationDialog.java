package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.OTPListener;
import com.app.admin.sellah.controller.utils.PermissionCheckUtil;
import com.app.admin.sellah.controller.utils.SMSListener;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.goodiebag.pinview.Pinview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.deleted_account;
import static com.app.admin.sellah.controller.utils.Global.makeLinks;

public class OTPVerificationDialog extends AlertDialog {

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
    @BindView(R.id.otp_view)
    Pinview otpView;
    @BindView(R.id.btn_verify)
    Button btnVerify;
    @BindView(R.id.txt_sign_in)
    TextView txtSignIn;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    private Context mContext;

    private String mMobileNumber;

    private int mGeneratedOTP;

    private int mEnteredOTP = 0;

    private OTPVerificationListener mOTPVerificationListener;

    private int totalTime = 1 * 60 * 1000;

    private CountDownTimer mCountDownTimer;

    private String countryCode = "";

    private WebService service;

    private String uid;
    private String phone_type;


    public OTPVerificationDialog(Context context)
    {
        super(context);

    }

    private OTPVerificationDialog(@NonNull Context context, String uid, String countryCode, String phone_type, String mobileNumber,
                                  int verificationCode, OTPVerificationListener otpVerificationListener) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.mContext = context;
        this.mOTPVerificationListener = otpVerificationListener;
        this.mGeneratedOTP = verificationCode;
        service = Global.WebServiceConstants.getRetrofitinstance();
        this.uid = uid;
        this.phone_type = phone_type;
    }
    //step 2, required
    private void KeepStatusBar(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
//        Global.setStatusBarColor((Activity) mContext,R.color.colorFormBg);
    }

    public static OTPVerificationDialog create(Context context, String uid, String countryCode, String phone_type, String mobileNumber, int verificationCode,
                                               OTPVerificationListener otpVerificationListener) {
        return new OTPVerificationDialog(context, uid, countryCode,phone_type,mobileNumber, verificationCode, otpVerificationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_otp_vcerification);
//        setCancelable(false);
        ButterKnife.bind(this);
//        setCancelable(false);
        handleKeyBoard();
        setUpPinView();
        KeepStatusBar();
//        generateAndSendOtp();


       /* SMSListener.bindListener(new OTPListener() {
            @Override
            public void onOTPReceived(String otp) {
                Log.e( "onOTPReceived: ",otp );
                otpView.setValue(otp);
                setUpPinView();


            }
        });*/

    }


    /*private void showProgress() {
        pbOTP.setIndeterminate(true);
        relSendingOtp.setVisibility(View.VISIBLE);
        relOTPSent.setVisibility(View.INVISIBLE);
        relVeryfingOtp.setVisibility(View.INVISIBLE);
    }*/

  /*  private void dismissProgress() {
        relOTPSent.setVisibility(View.VISIBLE);
        relSendingOtp.setVisibility(View.INVISIBLE);
        relVeryfingOtp.setVisibility(View.INVISIBLE);
    }*/

/*    public void bindSMSReceiver() {
        PermissionCheckUtil.create(mContext, () -> {
            SMSListener.bindListener(new OTPListener() {
                @Override
                public void onOTPReceived(String extractedOTP) {
//                et_otp.setText(extractedOTP);
                    Log.e("oooooo", extractedOTP);
                }
            });
        });
    }*/

/*    private void unBindReciever() {
        SMSListener.unbindListener();
    }*/

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
       // unBindReciever();

    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        super.setOnShowListener(listener);
      //  bindSMSReceiver();
    }

    private void setUpPinView() {
        otpView.setPinViewEventListener((pinView, fromUser) -> {
            Global.hideKeyboard(getWindow().getDecorView(), mContext);
            String enteredPin = pinView.getValue();
            mEnteredOTP = Integer.valueOf(enteredPin);
        });

        txtSignIn.setLinkTextColor(mContext.getResources().getColor(R.color.colorRed)); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                reSendCode();
            }

        };
        makeLinks(txtSignIn, new String[]{
                "Resend SMS"
        }, new ClickableSpan[]{
                normalLinkClickSpan
        });

    }

   /* @Override
    public void dismiss() {
        super.dismiss();
        Global.setStatusBarColor((Activity) mContext,R.color.colorFormBg);
    }*/
    /*
    private void generateAndSendOtp() {
//        showProgress();
//        setUpAndStartTimer();
        dismissProgress();
//        mGeneratedOTP = 0;
*//*        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

            }

        }, 3000);*//*
    }*/

    /* private void setUpAndStartTimer() {

         mCountDownTimer = new CountDownTimer(totalTime, 1000) {
             @Override
             public void onTick(long millisUntilFinished) {
                 long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                 long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                 txtTimeRemaining.setText(mContext.getString(R.string.lbl_timer, minutes, seconds));

                 long timePassed = totalTime - millisUntilFinished;
                 double progressFloat = (double) timePassed / totalTime;
                 double progress = progressFloat * 100;
                 long progressInt = Math.round(progress);
                 Log.e("Percent:", "Percentage : " + progressInt + " float " + progressFloat);
                 pbTime.setProgress((int) progressInt);
             }

             @Override
             public void onFinish() {
 //                WMUtility.showToast(mContext, "Time finished, OTP expired try again");
                 Toast.makeText(mContext, "Time finished, OTP expired try again", Toast.LENGTH_SHORT).show();
 //                dismiss();
                 TransitionManager.beginDelayedTransition(liResend);
                 liResend.setVisibility(View.VISIBLE);
                 liResend.setClickable(true);
                 txtTimeRemaining.setVisibility(View.INVISIBLE);
                 pbTime.setVisibility(View.INVISIBLE);

             }
         };
         mCountDownTimer.start();
     }
 */
    @OnClick(R.id.btn_verify)
    void onClickVerify() {
        if (mEnteredOTP == 0) {
            Toast.makeText(mContext, "Enter OTP Received", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mEnteredOTP == mGeneratedOTP/*123456*/) {
            Global.hideKeyboard(getWindow().getDecorView(), mContext);
            if (deleted_account)
            {
               restore_account_otpverification();
            }
            else
            {
                verifyOTP();
            }


        } else {
//            WMUtility.showToast(mContext, "Enter correct OTP");
            Toast.makeText(mContext, "Enter correct OTP", Toast.LENGTH_SHORT).show();

        }
    }

    private void verifyOTP() {
//        String uid= String.valueOf(HelperPreferences.get(mContext).getInt(UID));
//        String vCode= String.valueOf(mEnteredOTP);
        Log.e("Uid_vCode", uid + " : " + mEnteredOTP);
        /*Dialog dialog = S_Dialogs.getLoadingDialog(mContext);
        dialog.show();*/
//        dismissPinView();
        Call<Common> verifyOTPcall = service.varifyOTPApi(uid, mEnteredOTP, phone_type);
        verifyOTPcall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                Log.e("VerfyCode_response", response.isSuccessful() + "");
                Common result = response.body();
                if (response.isSuccessful()) {

                    if (result.getStatus().equalsIgnoreCase("1")) {
                        mOTPVerificationListener.onOTPVerified();
                        dismiss();
                    } else {
                        mOTPVerificationListener.onOTPNotVerified();
//                        dismissProgress();
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
//                    dismissProgress();
                    Toast.makeText(mContext, "Something went's wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
//                dismissProgress();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("verifyOtp_failure", t.getMessage());
            }
        });
    }

    private void reSendCode() {
//        showProgress();
        Dialog dialog=S_Dialogs.getLoadingDialog(mContext);
        dialog.show();
        Call<ResendCode> recendCodeCall = service.reSendOTPApi(uid, phone_type);
        recendCodeCall.enqueue(new Callback<ResendCode>() {
            @Override
            public void onResponse(Call<ResendCode> call, Response<ResendCode> response) {
                Log.e("ResendCode_response", response.isSuccessful() + "");

                if(dialog!=null&&dialog.isShowing()){
                 dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    ResendCode resendCode = response.body();
                    Log.e("Resend_Response", resendCode.getResult().getVerificationCode() + "");
                    if (resendCode.getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(mContext, resendCode.getMessage(), Toast.LENGTH_SHORT).show();
//                        generateAndSendOtp();
                        mGeneratedOTP = resendCode.getResult().getVerificationCode();
                    } else {
                        Toast.makeText(mContext, resendCode.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Something went's wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendCode> call, Throwable t) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
//        Global.hideKeyboard(getWindow().getDecorView(), mContext);
//        dismiss();
        Log.e("Dialog_Canceled", "setOnCancelListener: ");
        mOTPVerificationListener.onOTPNotVerified();
        /*try {
            mCountDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /* @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        Global.hideKeyboard(getWindow().getDecorView(), mContext);
        dismiss();
        mOTPVerificationListener.onOTPNotVerified();
        try {
            mCountDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

   /* @OnClick(R.id.li_mobile)
    public void onViewClicked() {
    }*/

   /* @OnClick(R.id.li_resend)
    public void onRecendClicked() {
        reSendCode();
    }*/

    public interface OTPVerificationListener {
        void onOTPVerified();

        void onOTPNotVerified();
    }

    private void handleKeyBoard() {
        if (getWindow() != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void restore_account_otpverification() {

        Log.e("Uid_vCode", uid + " : " + mEnteredOTP);

        Call<Common> verifyOTPcall = service.restore_account(uid, mEnteredOTP);
        verifyOTPcall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                Log.e("VerfyCode_response", response.isSuccessful() + "");
                Common result = response.body();
                if (response.isSuccessful()) {

                    if (result.getStatus().equalsIgnoreCase("1")) {
                        mOTPVerificationListener.onOTPVerified();
                        dismiss();
                    } else {
                        mOTPVerificationListener.onOTPNotVerified();
//                        dismissProgress();
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
//                    dismissProgress();
                    Toast.makeText(mContext, "Something went's wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
//                dismissProgress();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("verifyOtp_failure", t.getMessage());
            }
        });
    }



}
