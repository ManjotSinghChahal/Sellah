package com.app.admin.sellah.view.CustomDialogs;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.admin.sellah.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Add_New_Product_tutorial_price_screen extends DialogFragment {

    @BindView(R.id.gotit_btn)
    Button gotitBtn;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_product_price_tutorial_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();

        TextView priceDialog = view.findViewById(R.id.price_dialog);
        TextView price_recDialog = view.findViewById(R.id.price_rec_dialog);
        //
        // p.x = getArguments().getInt("x");
        p.y = getArguments().getInt("y") + 80;
        String price = getArguments().getString("price");
        String price_rec = getArguments().getString("price_rec");

        priceDialog.setText("$ "+price);
        price_recDialog.setText("You will recieve S$ "+price_rec);

        getDialog().getWindow().setAttributes(p);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.gotit_btn)
    public void onViewClicked() {
        dismiss();
    }
}
