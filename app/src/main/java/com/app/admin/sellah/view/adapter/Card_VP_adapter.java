package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.view.fragments.ShowCreditCardDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class Card_VP_adapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<Card> data;



    public Card_VP_adapter(Context context, List<Card> data) {
        this.context = context;
        this.data=data;


    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.card_vp_adapter, null);

        TextView name = (TextView) view.findViewById(R.id.vp_onnewcardholdername);
        TextView number = (TextView) view.findViewById(R.id.vp_onnewcardnumber);
        TextView exp = (TextView) view.findViewById(R.id.vp_onnewcard_expire);
        TextView cvv = (TextView) view.findViewById(R.id.vp_onnewcard_cvv);
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.vp_root);
        name.setText(data.get(position).getName());
        number.setText("**** **** **** "+data.get(position).getLast4());
        exp.setText(data.get(position).getExpMonth()+"/"+data.get(position).getExpYear());
        cvv.setText("***");



        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowCreditCardDetailFragment.cardId = data.get(position).getId();
            }
        });

        root.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                ,(int) context.getResources().getDimension(R.dimen._10sdp)
                ,(int) context.getResources().getDimension(R.dimen._10sdp)
                ,(int) context.getResources().getDimension(R.dimen._10sdp));

        //container.addView(view);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    @Override
    public float getPageWidth(int position) {
        return(0.85f);
    }
}
