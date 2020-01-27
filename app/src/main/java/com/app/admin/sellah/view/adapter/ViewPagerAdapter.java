package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;
import com.app.admin.sellah.view.CustomDialogs.Stripe_image_verification_dialogfragment;
import com.app.admin.sellah.view.activities.AddNewVideos;
import com.app.admin.sellah.view.activities.ImageSlideShowActivity;
import com.app.admin.sellah.view.activities.SplashScreen;
import com.app.admin.sellah.view.activities.VideoWelcomeActivity;
import com.app.admin.sellah.view.activities.WelcomeActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.car_icon,R.drawable.car_icon,R.drawable.car_icon};
    private ArrayList<HashMap<String, String>> bannerList;
    ArrayList arrayList = new ArrayList();
    int finalHeight = 0;
    int finalWidth = 0;

    public ViewPagerAdapter(Context context, ArrayList<HashMap<String, String>> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
        arrayList.clear();
        arrayList.addAll(bannerList);
    }

    @Override
    public int getCount() {
        if (bannerList != null && bannerList.size() > 0) {
            return bannerList.size();
        } else {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_View);
        //   imageView.setPadding(0,0,0,0);
//        imageView.setImageResource(images[position]);


        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imageView.getMeasuredHeight();
                finalWidth = imageView.getMeasuredWidth();
                return true;
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(finalWidth, finalWidth / 2);
                imageView.setLayoutParams(layoutParams);
                if (bannerList != null && bannerList.size() > 0) {
                    Glide.with(context)
                            .load(bannerList.get(position).get("bannerImage"))
                            .apply(Global.getBannerGlideOptions()/*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*/)
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.logo_new);
                    imageView.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                            , (int) context.getResources().getDimension(R.dimen._10sdp)
                            , (int) context.getResources().getDimension(R.dimen._10sdp)
                            , (int) context.getResources().getDimension(R.dimen._10sdp));
//            imageView.setImageResource(R.drawable.app_dummy_logo);
                }
            }
        }, 100);






/*       if (bannerList != null && bannerList.size() > 0) {
            Glide.with(context)
                    .load(bannerList.get(position))
                    .apply(Global.getGlideOptions()*//*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*//*)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.logo_new);
            imageView.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp));
//            imageView.setImageResource(R.drawable.app_dummy_logo);
        }*/


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //  ImageSlideShowActivity.start(context,arrayList);
                    if (bannerList != null && bannerList.get(position).get("bannerLink") != null && bannerList.get(position).get("bannerLink").equalsIgnoreCase("welcome")) {
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else if (bannerList.get(position).get("bannerLink").equalsIgnoreCase("live_stream")) {
                        if ((HelperPreferences.get(context).getString(STRIPE_VERIFIED).equals("") || HelperPreferences.get(context).getString(STRIPE_VERIFIED).equals("N"))) {
                            S_Dialogs.getLiveVideoStopedDialog(context, "You are not currently connected with stripe. Press ok to connect.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
                                stripe_dialogfragment.show(((Activity) context).getFragmentManager(), "");

                            })).show();
                        } else if ((HelperPreferences.get(context).getString(STRIPE_VERIFIED).equalsIgnoreCase("P"))) {
                            S_Dialogs.getLiveVideoStopedDialog(context, "You have not uploaded you Idenitification Documents. Press ok to upload.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_image_verification_dialogfragment stripe_dialogfragment = new Stripe_image_verification_dialogfragment();
                                stripe_dialogfragment.show(((Activity) context).getFragmentManager(), "");

                            })).show();
                        } else {
                            Intent intent = new Intent(context, VideoWelcomeActivity.class);
                            context.startActivity(intent);
                        }
                    } else if (bannerList.get(position).get("bannerLink").equalsIgnoreCase("add_product")) {
                        Intent intent = new Intent(context, AddNewVideos.class);
                        context.startActivity(intent);
                    }
                } catch (NullPointerException e) {

                }

            }
        });


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
/*    @Override
    public float getPageWidth(int position) {
        return(0.90f);
      //  return(0.60f);
    }*/

}
