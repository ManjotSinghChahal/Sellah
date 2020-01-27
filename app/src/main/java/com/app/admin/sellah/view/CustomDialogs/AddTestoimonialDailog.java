package com.app.admin.sellah.view.CustomDialogs;


import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.fragments.ChatDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UNAME;

public class AddTestoimonialDailog extends DialogFragment {


    Float givenRating;
    String otherUserId;
    @BindView(R.id.testimonial_cancel)
    ImageView testimonialCancel;
    private WebService webService;
    AddtestimonialListner listner;
    @BindView(R.id.review_txt)
    RelativeLayout reviewTxt;
    @BindView(R.id.rate_txt)
    TextView rateTxt;
    @BindView(R.id.rtB_rating)
    RatingBar rtBRating;
    @BindView(R.id.txt_testimonial)
    TextView txtTestimonial;
    @BindView(R.id.edt_testimonials)
    EditText edtTestimonials;
    @BindView(R.id.btn_submit_review)
    Button btnSubmitReview;
    Unbinder unbinder;
    String order_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_testimonial_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        webService = Global.WebServiceConstants.getRetrofitinstance();
        textwatcher();
        rtBRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("Rating_value", rating + "");
                givenRating = rating;
            }
        });
        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            otherUserId = bundle.getString("other_id");
            order_id = bundle.getString("order_id");


            if (bundle.containsKey("friendName"))
            {
             rateTxt.setText("On a scale of 1 to 5, how would you rate your experience when shopping with "+bundle.getString("friendName"));
            }

        }


        return view;
    }


    private void addTestimonialApi(String otherUserId, String feedBack, String rating) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();


        Call<Common> addTestimonialCall = webService.addTestimonialApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId, feedBack, rating,order_id);
        addTestimonialCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                Log.e("AddTestimonialApi", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("AddTestimonialApi", "onResponse: " + response.body());
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        ChatDetailFragment.chatScrollview.setVisibility(View.GONE);

                     //   listner.onSuccessListner();
                        S_Dialogs.getInformation(getActivity(), response.body().getMessage(), ((dialog1, which) -> {
                            dismiss();
                          //  listner.onSuccessListner();
                        })).show();

                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("AddTestimonialApi", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.testimonial_cancel, R.id.btn_submit_review})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.testimonial_cancel:
                dismiss();
                break;
            case R.id.btn_submit_review:

                if (!Global.getText(edtTestimonials).equalsIgnoreCase("")) {
                    addTestimonialApi(otherUserId, Global.getText(edtTestimonials), String.valueOf(givenRating));
                } else {
                    Toast.makeText(getActivity(), "Please give some review about user.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public interface AddtestimonialListner {
        void onSuccessListner();
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    public void textwatcher(){
        edtTestimonials.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

              btnSubmitReview.setBackgroundResource(R.drawable.round_red_border_testimonial);
            }
        });
    }

}
