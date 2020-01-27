package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewVideo_Sell_Better_bottom_Dialog extends Dialog {


    @BindView(R.id.linn)
    RelativeLayout linn;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.text_better)
    TextView textBetter;
    @BindView(R.id.excited_btn)
    Button excitedBtn;
    Context context;

    public NewVideo_Sell_Better_bottom_Dialog( Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newvideo_sell_better_bottom_dialog);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.excited_btn)
    public void onViewClicked() {

        dismiss();
        new Add_New_Product_tutorial_firstDialog(context).show();
    }
}
