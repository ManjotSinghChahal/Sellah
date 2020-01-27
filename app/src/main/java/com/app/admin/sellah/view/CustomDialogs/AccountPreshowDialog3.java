package com.app.admin.sellah.view.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.admin.sellah.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountPreshowDialog3 extends Dialog {
    Context context;
    public AccountPreshowDialog3(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_preshow3);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.gotit_btn_dialog3)
    public void onViewClicked() {
        dismiss();
       /* Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
        stripe_dialogfragment.show(((Activity)context).getFragmentManager(), "");*/
    }
}