package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendOffer extends Dialog {


    @BindView(R.id.btn_cancel)
    ImageView btnCancel;
    @BindView(R.id.edt_add_product)
    EditText edtAddProduct;
    @BindView(R.id.edt_add_price_send)
    EditText edtAddPriceSend;
    @BindView(R.id.btn_sendOffer)
    Button btnSendOffer;
    onSendOffer callback;

    Context context;
    @BindView(R.id.price_rec_send_dialog)
    TextView priceRecSendDialog;



    public SendOffer(Context con) {
        super(con);


    }

    public void send(Context context, String senderId, onSendOffer callback) {
        this.context = context;
        this.callback = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.send_offer_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);


        edtAddPriceSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                priceRecSendDialog.setText("You will received S$ " + Global.gettotalamount(edtAddPriceSend.getText().toString().trim()));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @OnClick({R.id.btn_cancel, R.id.btn_sendOffer, R.id.lin_send_offer_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sendOffer:


                if (TextUtils.isEmpty(edtAddProduct.getText().toString().trim())) {
                    Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show();
                } else if (edtAddPriceSend.getText().toString().trim().equalsIgnoreCase("") || edtAddPriceSend.getText().toString().trim().equalsIgnoreCase("0")) {
                    Toast.makeText(context, "Please enter offering amount", Toast.LENGTH_SHORT).show();
                } else {

                    callback.onSendOfferSuccess(edtAddProduct.getText().toString().trim(), edtAddPriceSend.getText().toString().trim());
                    dismiss();
                }

                break;

            case R.id.lin_send_offer_dialog:
                priceRecSendDialog.setText("You will received S$ " + Global.gettotalamount(edtAddPriceSend.getText().toString().trim()));
                break;
        }
    }

    @OnClick(R.id.lin_send_offer_dialog)
    public void onViewClicked() {
    }


    public interface onSendOffer {
        void onSendOfferSuccess(String name, String price);
    }

/*    public static SendOffer creates(Context context, String senderId, onSendOffer callback) {
        return new SendOffer(context, senderId, callback);
    }*/


}
