package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.app.admin.sellah.view.fragments.LiveVideosFragment;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.LIVETAG;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;

public class LiveVideoPaggerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.car_icon,R.drawable.car_icon,R.drawable.car_icon};
    private List<VideoList> videoList;

    public LiveVideoPaggerAdapter(Context context, List<VideoList> videoLists) {
        this.context = context;
        this.videoList = videoLists;
    }

    @Override
    public int getCount() {
        if (videoList != null && videoList.size() > 0) {
            if (videoList.size() > 3) {
                return 3;
            } else {
                return videoList.size();
            }
        } else {
            return 0;
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_live_pagger_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView time = (TextView) view.findViewById(R.id.home_livetime);
        imageView.setPadding(0, 0, 0, 0);
//      imageView.setImageResource(images[position]);

        try {
            time.setText(Global.getTimeDuration(Global.convertUTCToLocal(videoList.get(position).getCreatedAt()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (videoList != null && videoList.size() > 0) {
            Glide.with(context)
                    .load(videoList.get(position).getCoverImage())
                    //    .apply(Global.getGlideOptions()/*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*/)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.logo_new);
            imageView.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp));
            //imageView.setImageResource(R.drawable.app_dummy_logo);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.getUser.isLogined(context)) {
                    //new MainActivity().changeOptionColor(1);
                    //new MainActivity().loadFragment(new LiveVideosFragment(), LIVETAG);
                   // ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LiveVideosFragment()).addToBackStack(HOMETAG).commit();

                    Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                    intent.putExtra("value", "noLiveStream");
                    intent.putExtra("id",videoList.get(position).getVideoId());
                    intent.putExtra("group_id",videoList.get(position).getGroupId());
                    intent.putExtra("product_id",videoList.get(position).getProductId());
                    intent.putExtra("product_name",videoList.get(position).getProductName());
                    intent.putExtra("seller_id",videoList.get(position).getSellerId());
                    intent.putExtra("start_time",videoList.get(position).getStartTime());
                    intent.putExtra("views",videoList.get(position).getViews());
                    context.startActivity(intent);

                } else {
                    S_Dialogs.getLoginDialog(context).show();
                }
               /*if(Global.getUser.isLogined(context))
                {
                    Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                    intent.putExtra("value", "noLiveStream");
                    intent.putExtra("id",videoList.get(position).getVideoId());
                    intent.putExtra("group_id",videoList.get(position).getGroupId());
                    intent.putExtra("product_id",videoList.get(position).getProductId());
                    intent.putExtra("product_name",videoList.get(position).getProductName());
                    intent.putExtra("seller_id",videoList.get(position).getSellerId());
                    intent.putExtra("start_time",videoList.get(position).getStartTime());
                    intent.putExtra("views",videoList.get(position).getViews());
                    context.startActivity(intent);
                }else{
                    S_Dialogs.getLoginDialog(context).show();
                }*/

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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return LiveVideoPaggerAdapter.POSITION_NONE;
    }
}