package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class Live_featureViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.car_icon,R.drawable.car_icon,R.drawable.car_icon};
    private List<String> bannerList;

    public Live_featureViewPagerAdapter(Context context, List<String> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
            return bannerList.size();


    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_live_videos_adapter_view, null);

        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.videoview_categories);
        imageView.setPadding(0,0,0,0);
//        imageView.setImageResource(images[position]);

        if (bannerList != null && bannerList.size() > 0) {
            Glide.with(context)
                    .load(bannerList.get(position))
                    .apply(Global.getGlideOptions().transform(new RoundedCorners(10))/*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*/)
                    .into(imageView);
        }


        container.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }


}
