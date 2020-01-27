package com.app.admin.sellah.view.CustomDialogs;

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

public class AccountPreshowDialog2 extends Dialog {
    Context context;
    public AccountPreshowDialog2(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_preshow2);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.gotit_btn_dialog2)
    public void onViewClicked() {
        dismiss();
        AccountPreshowDialog3 accountPreshowDialog3 = new AccountPreshowDialog3(context);
        accountPreshowDialog3.show();
    }
}