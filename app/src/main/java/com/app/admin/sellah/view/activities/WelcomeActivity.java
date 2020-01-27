package com.app.admin.sellah.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.admin.sellah.R;
import com.app.admin.sellah.view.adapter.WelcomAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import static com.app.admin.sellah.controller.utils.Global.makeTransperantStatusBar;

public class WelcomeActivity extends AppCompatActivity {
    ViewPager mPager;
    @BindView(R.id.wlcm_skip)
    TextView wlcmSkip;
    @BindView(R.id.wlcm_next)
    TextView wlcmNext;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;

    ArrayList<Integer> viewImagesArray = new ArrayList<Integer>();
    String[] stringArray = new String[]{"What is Sellah?", "Marketplace", "Sell Effectively", "Easy to shop", "Secure Payment"};
    String[] stringArray2 = new String[]{"   Sellah! is the revolutionary vCommerce \n platform where e-commerce meets video!", "Buy and sell everything for free, \n          in less than a minute", "Sell more effectively with \n    short and live videos", "Shop with confidence in this v-Commerce, \n       where you only buy what you see!", "Send and receive payments \n    safely with Sellah Wallet"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeTransperantStatusBar(this, true);
        setContentView(R.layout.welcome_layout);
        ButterKnife.bind(this);

        viewImagesArray.add(R.drawable.welcome1);
        viewImagesArray.add(R.drawable.welcome2);
        viewImagesArray.add(R.drawable.welcome3);
        viewImagesArray.add(R.drawable.welcome4);
        viewImagesArray.add(R.drawable.welcome5);
        init();

        wlcmSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("run: ", "" + mPager.getCurrentItem());
//                if (mPager.getCurrentItem() >= 3) {
//                    wlcmNext.setVisibility(View.VISIBLE);
//                    wlcmSkip.setVisibility(View.GONE);
//
//
//                }
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        wlcmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
//      sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
//      mPager.setAdapter(new WelcomAdapter(this, viewImagesArray));

        WelcomAdapter welcomAdapter = new WelcomAdapter(this, viewImagesArray, stringArray, stringArray2);
        mPager.setAdapter(welcomAdapter);

        dotscount = welcomAdapter.getCount();
        dots = new ImageView[dotscount];

        //automatic slide
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mPager.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("run: ", "" + mPager.getCurrentItem());
                        if (mPager.getCurrentItem() == 3) {

                            wlcmNext.setVisibility(View.VISIBLE);
                            wlcmSkip.setVisibility(View.GONE);

                        }

                        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);

                    }

                });

            }

        };

        timer = new Timer();
        timer.schedule(timerTask, 4000, 4000);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

       /*for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(WelcomeActivity.this);
            dots[i].setImageResource(R.drawable.dot_icon);
        }*/

        //dots[0].setImageDrawable(ContextCompat.getDrawable(WelcomeActivity.this, R.drawable.dot_icon2));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.e("onPageScrolled: ", "" + position);
                if (position == 4) {
                    wlcmNext.setVisibility(View.VISIBLE);
                    wlcmSkip.setVisibility(View.GONE);
                } else {
                    wlcmNext.setVisibility(View.GONE);
                    wlcmSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

}
