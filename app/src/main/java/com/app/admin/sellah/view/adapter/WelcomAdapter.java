package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;


import java.util.ArrayList;

public class WelcomAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<Integer> viewImagesArray;
    String[] stringArray;
    String[] stringArray2;


    public WelcomAdapter(Context context, ArrayList<Integer> viewImagesArray, String[] stringArray, String[] stringArray2) {
        this.context = context;
        this.viewImagesArray=viewImagesArray;
        this.stringArray=stringArray;
        this.stringArray2=stringArray2;


    }

    @Override
    public int getCount() {
        return viewImagesArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.welcome_dapter, null);
        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.imageView3);

        TextView text_sellah = (TextView) view.findViewById(R.id.txt_sellah);
        TextView text_sellah_main = (TextView) view.findViewById(R.id.txt_sellah_main);
        imageView.setImageResource(viewImagesArray.get(position));
        if (position==0)
        {
            text_sellah.setTextColor(Color.parseColor("#aaaaaa"));
        }
        else
        {
            text_sellah.setTextColor(Color.parseColor("#e35252"));
        }
        text_sellah.setText(stringArray[position]);
        text_sellah_main.setText(stringArray2[position]);

        container.addView(view);


        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

}
