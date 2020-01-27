package com.app.admin.sellah.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.adapter.MyCustomPagerAdapter;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageSlideShowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
//    @BindView(R.id.vp_full_screen)
    ViewPager viewPager;

    private ArrayList<String> images;
    private MyCustomPagerAdapter myCustomPagerAdapter;

    @BindView(R.id.imv_backword)
    ImageView imgBackWord;

    @BindView(R.id.imv_forword)
    ImageView imgForWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.StatusBarLightMode(this);
        setContentView(R.layout.image_slide_show_dialog);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            images = bundle.getStringArrayList("images");
            viewPager=findViewById(R.id.vp_full_screen);
            myCustomPagerAdapter = new MyCustomPagerAdapter(this, images);
            viewPager.setAdapter(myCustomPagerAdapter);
            viewPager.setOnPageChangeListener(this);
        }
    }



    @OnClick(R.id.imv_close)
    void onClickClose() {
        finish();
    }

    @OnClick(R.id.imv_forword)
    void onClickForword() {
        viewPager.arrowScroll(ViewPager.FOCUS_RIGHT);
//        finish();
    }
    @OnClick(R.id.imv_backword)
    void onClickBackword() {
        viewPager.arrowScroll(ViewPager.FOCUS_LEFT);
//        finish();
    }

    public void toggleArrowVisibility(int pos) {

        if(pos==0)
            imgBackWord.setVisibility(View.INVISIBLE);
        else
            imgBackWord.setVisibility(View.VISIBLE);
        if(pos==images.size()-1)
            imgForWord.setVisibility(View.INVISIBLE);
        else
            imgForWord.setVisibility(View.VISIBLE);

    }

    public static void start(Context context, ArrayList<String> images){
        Intent intent = new Intent(context, ImageSlideShowActivity.class);
        intent.putStringArrayListExtra("images", images);
        context.startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        toggleArrowVisibility(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /* @BindView(R.id.vp_full_screen)
    ViewPager viewPager;
    private int[] images;
    private MyCustomPagerAdapter myCustomPagerAdapter;

    @OnClick(R.id.imv_close)
    void onClickClose() {
        finish();
    }


    Context context;
    public ImageSlideShowActivity(Context context,int image[]){
       this.images=image ;
       this.context=context;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slide_show_dialog);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        myCustomPagerAdapter = new MyCustomPagerAdapter(this, images);
        viewPager.setAdapter(myCustomPagerAdapter);
    }*/
}
