package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Add_New_Product_tutorial_firstDialog extends Dialog {

    @BindView(R.id.gotit_btn)
    Button gotitBtn;
    Context context;

    public Add_New_Product_tutorial_firstDialog( Context context) {
        super(context);
        this.context=context;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.add_new_product_first_tutorial_dialog);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }


    @OnClick(R.id.gotit_btn)
    public void onViewClicked() {
        dismiss();
        new Add_New_Product_tutorial_videoDialog(context).show();
    }
}
