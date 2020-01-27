package com.app.admin.sellah.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.makeLinks;
import static com.app.admin.sellah.controller.utils.Global.setStatusBarColor;

public class ForgetPasswordActivity extends AppCompatActivity {

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
    @BindView(R.id.etSignup_email)
    EditText etSignupEmail;
    @BindView(R.id.btn_forget_pass)
    Button btnForgetPass;
    @BindView(R.id.txt_sign_in)
    TextView txtSignIn;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;
    @BindView(R.id.il_email)
    NoChangingBackgroundTextInputLayout ilEmail;
    @BindView(R.id.txt_line_2)
    TextView txtLine2;
    @BindView(R.id.txt_error_send)
    TextView txtErrorSend;
    @BindView(R.id.lin_success_send)
    LinearLayout linSuccessSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password_new);
        ButterKnife.bind(this);
        setStatusBarColor(this,R.color.colorFormBg);
        txtSignIn.setLinkTextColor(Color.BLACK); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        };
        makeLinks(txtSignIn, new String[]{
                "Sign In"
        }, new ClickableSpan[]{
                normalLinkClickSpan
        });

        String first = getResources().getString(R.string.forget_pass_line_2);
        String next = "<font color='#EE0000'>So rest assured that we will never send your password via email.</font>";
        txtLine2.setText(Html.fromHtml(first + next));
    }

    @OnClick(R.id.btn_forget_pass)
    public void onViewClicked() {
        if (TextUtils.isEmpty(Global.getText(etSignupEmail)) && (!Patterns.EMAIL_ADDRESS.matcher(Global.getText(etSignupEmail)).matches())) {
            ilEmail.setError("Invalid Email");
        } else {
            ilEmail.setErrorEnabled(false);
            hitForgetApi(Global.getText(etSignupEmail));
        }
    }

    private void hitForgetApi(String email) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(ForgetPasswordActivity.this);
        dialog1.show();
        Call<Common> forgetApiCall = Global.WebServiceConstants.getRetrofitinstance().forgetPasswordApi(email);
        forgetApiCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        /*Snackbar.make(rootLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

                        */
                        showSuccessLayout();
//                        dialog.dismiss();
                    } else {
//                        dialog.dismiss();
                        hideSuccessLayout();
                        Snackbar.make(rootLayout, "No account found linked with this email-Id", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                }else{
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    Snackbar.make(rootLayout, "No account found linked with this email-Id", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("ForgetPassError", "onResponse: "+response.errorBody().string());
                        Converter<ResponseBody, Common> converter
                                = Global.WebServiceConstants.getRetrofit().responseBodyConverter(
                                Common.class, new Annotation[0]);

//                        Gson gson = new Gson();
//                        Common error=gson.fromJson(response.errorBody().charStream(),Common.class);

                        Common error = converter.convert(response.errorBody());
                        String message = error.getMessage();
                        Log.e("message", "onResponse: "+error.getMessage());
                        Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        Log.e("jsonException", "onResponse: "+e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                hideSuccessLayout();
//                dialog.dismiss();
                Snackbar.make(rootLayout, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });


    }

    private void showSuccessLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(rootLayout);
        }
        etSignupEmail.setText("");
        linSuccessSend.setVisibility(View.VISIBLE);
        btnForgetPass.setVisibility(View.GONE);
    }

    private void hideSuccessLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(rootLayout);
        }
        linSuccessSend.setVisibility(View.GONE);
        btnForgetPass.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.lin_success_send)
    public void onSentSuccessClicked() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatusBarColor(this,R.color.colorWhite);
    }
}
