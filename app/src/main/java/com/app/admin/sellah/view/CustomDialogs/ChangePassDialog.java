package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomAnimations.MyBounceInterpolator;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.goodiebag.pinview.Pinview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ChangePassDialog extends Dialog {
    @BindView(R.id.edt_current_pass)
    EditText edtCurrentPass;
    @BindView(R.id.il_curr_pass)
    NoChangingBackgroundTextInputLayout ilCurrPass;
    @BindView(R.id.edt_new_pass)
    EditText edtNewPass;
    @BindView(R.id.il_new_pass)
    NoChangingBackgroundTextInputLayout ilNewPass;
    @BindView(R.id.edt_confirm_pass)
    EditText edtConfirmPass;
    @BindView(R.id.il_confirm_pass)
    NoChangingBackgroundTextInputLayout ilConfirmPass;
    @BindView(R.id.otp_view)
    Pinview otpView;
    @BindView(R.id.pb_time)
    MaterialProgressBar pbTime;
    @BindView(R.id.txt_time_remaining)
    TextView txtTimeRemaining;
    @BindView(R.id.img_resend)
    ImageView imgResend;
    @BindView(R.id.txt_resend)
    TextView txtResend;
    @BindView(R.id.li_resend)
    LinearLayout liResend;
    @BindView(R.id.btn_change)
    Button btnChange;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    private ArrayList<EditText> allchangePassFields;
    private ArrayList<TextInputLayout> allchangePassInputLayouts;
    private HashMap<EditText, String> changePassMessages;
    private Animation myAnim;
    private Context context;
    private CountDownTimer mCountDownTimer;
    private int totalTime = 1 * 60 * 1000;
    private int mEnteredOTP;
    private int mGeneratedOTP = 0;

    public ChangePassDialog(@NonNull Context context, int genratedOTP) {
        super(context);
        this.context = context;
        this.mGeneratedOTP = genratedOTP;
    }

    public static ChangePassDialog create(Context context, int genratedOTP) {
        return new ChangePassDialog(context, genratedOTP);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.account_password_change);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        setUpData();
        setUpPinView();
        setUpAndStartTimer();
        pbTime.setIndeterminate(false);
        pbTime.setMax(100);
    }

    private void setUpData() {
        allchangePassFields = new ArrayList<>();
        allchangePassFields.add(edtCurrentPass);
        allchangePassFields.add(edtNewPass);
        allchangePassFields.add(edtConfirmPass);

        allchangePassInputLayouts = new ArrayList<>();
        allchangePassInputLayouts.add(ilCurrPass);
        allchangePassInputLayouts.add(ilNewPass);
        allchangePassInputLayouts.add(ilConfirmPass);


        changePassMessages = new HashMap<>();
        changePassMessages.put(edtCurrentPass, "Please enter current password");
        changePassMessages.put(edtNewPass, "Please enter new password");
        changePassMessages.put(edtConfirmPass, "Please enter password");

        myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        backImg.startAnimation(myAnim);
    }

    @OnClick({R.id.btn_change, R.id.back_img, R.id.li_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change:
                if (isChangePassFormValid()) {
                    if (mEnteredOTP == 0) {
                        Toast.makeText(context, "Enter OTP Received", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mEnteredOTP == mGeneratedOTP/*1234*/) {
                        Global.hideKeyboard(getWindow().getDecorView(), context);

                        mCountDownTimer.cancel();
                        verifyOTP();

                    } else {
                        Toast.makeText(context, "Enter correct OTP received.", Toast.LENGTH_SHORT).show();

                    }

                }
                break;
            case R.id.back_img:
                dismiss();
                break;
            case R.id.li_resend:

                new ApisHelper().sendOtpChangePassApi(context, new ApisHelper.OTPSentCallBack() {
                    @Override
                    public void onOTPSentSuccess(ResendCode body) {
                        mGeneratedOTP = body.getResult().getVerificationCode();
                        setUpAndStartTimer();
                    }

                    @Override
                    public void onOTPSentFailure() {

                    }
                });
                break;
        }
    }

    private void verifyOTP() {
        if (Global.getText(edtConfirmPass).equalsIgnoreCase(Global.getText(edtNewPass))) {
            ilConfirmPass.setErrorEnabled(false);
            Dialog loadingdialog = S_Dialogs.getLoadingDialog(context);
            loadingdialog.show();

            Call<Common> changePassCall = Global.WebServiceConstants.getRetrofitinstance().changePasswordApi(HelperPreferences.get(context).getString(UID), Global.getText(edtCurrentPass), Global.getText(edtNewPass), Global.getText(edtConfirmPass));
            changePassCall.enqueue(new Callback<Common>() {
                @Override
                public void onResponse(Call<Common> call, Response<Common> response) {
                    if (response.isSuccessful()) {
                        Common result = response.body();

                        if (loadingdialog != null && loadingdialog.isShowing()) {
                            loadingdialog.dismiss();
                        }
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ChagePassError", " : " + result.getMessage());
                        dismiss();


                    } else {
                        if (loadingdialog != null && loadingdialog.isShowing()) {
                            loadingdialog.dismiss();
                        }
                        Toast.makeText(context, "Password is either same as previous or incorrect ", Toast.LENGTH_SHORT).show();

                        Log.e("ChagePassError", " : " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Common> call, Throwable t) {
                    if (loadingdialog != null && loadingdialog.isShowing()) {
                        loadingdialog.dismiss();
                    }
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            ilConfirmPass.setError("Password not Matched");
        }
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

    private void setUpPinView() {
        otpView.setPinViewEventListener((pinView, fromUser) -> {
            Global.hideKeyboard(getWindow().getDecorView(), context);
            String enteredPin = pinView.getValue();
            mEnteredOTP = Integer.valueOf(enteredPin);
        });
    }

    private void setUpAndStartTimer() {

        liResend.setVisibility(View.INVISIBLE);
        liResend.setClickable(false);
        txtTimeRemaining.setVisibility(View.VISIBLE);
        pbTime.setVisibility(View.VISIBLE);

        mCountDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                txtTimeRemaining.setText(context.getString(R.string.lbl_timer, minutes, seconds));

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
                Toast.makeText(context, "Time finished, OTP expired try again", Toast.LENGTH_SHORT).show();
//                dismiss();
                TransitionManager.beginDelayedTransition(liResend);
                liResend.setVisibility(View.VISIBLE);
                liResend.setClickable(true);
                txtTimeRemaining.setVisibility(View.GONE);
                pbTime.setVisibility(View.GONE);

            }
        };
        mCountDownTimer.start();
    }

}
