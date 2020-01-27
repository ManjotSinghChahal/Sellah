package com.app.admin.sellah.view.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.app.admin.sellah.view.activities.VideoPlayerActivity;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 11/26/2018.
 */


public class  VideoSubcategoriesAdpt extends RecyclerView.Adapter<VideoSubcategoriesAdpt.ViewHolder> {
    Context context;
    List<VideoList> arrayList = new ArrayList<>();

    public VideoSubcategoriesAdpt(Context context, List<VideoList> arrayList) {
        this.context=context;
        this.arrayList=arrayList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_sub_live_video_adapter, parent, false);
        ViewHolder cvh1 = new ViewHolder(v);
        return cvh1;
        }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtVideoTitle.setText(arrayList.get(position).getProductName()+"");
        holder.userName.setText(arrayList.get(position).getUsername()+"");
        try {
            holder.time.setText(Global.getTimeDuration(Global.convertUTCToLocal(arrayList.get(position).getStartTime()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(arrayList.get(position).getViews()!=null){
            if(Integer.parseInt(arrayList.get(position).getViews())<1000){
                if (Integer.parseInt(arrayList.get(position).getViews()) < 10) {
                    holder.txtVideoViewers.setText(arrayList.get(position).getViews());
                } else {
                    holder.txtVideoViewers.setText(arrayList.get(position).getViews());
                }
            }else{
                holder.txtVideoViewers.setText((Double.parseDouble(arrayList.get(position).getViews())/1000)+"K");
            }
        }else{
            holder.txtVideoViewers.setText("00");
        }

        Glide.with(context)
                .load(arrayList.get(position).getCoverImage())
                .apply(Global.getGlideOptions())
                .into(holder.userProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                intent.putExtra("value", "noLiveStream");
                intent.putExtra("id",arrayList.get(position).getVideoId());
                intent.putExtra("group_id",arrayList.get(position).getGroupId());
                intent.putExtra("product_id",arrayList.get(position).getProductId());
                intent.putExtra("product_name",arrayList.get(position).getProductName());
                intent.putExtra("seller_id",arrayList.get(position).getSellerId());
                intent.putExtra("start_time",arrayList.get(position).getStartTime());
                intent.putExtra("views",arrayList.get(position).getViews());
                context.startActivity(intent);
//                context.startActivity(new Intent(context,VideoPlayerActivity.class));
            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Click,userProfile;
        TextView userName,txtVideoTitle,txtVideoViewers,time;
        public ViewHolder(View itemView) {
            super(itemView);
            Click = itemView.findViewById(R.id.videoview_categories_play);
            userProfile = itemView.findViewById(R.id.img_user_profile);
            userName = itemView.findViewById(R.id.txt_user_name);
            txtVideoTitle = itemView.findViewById(R.id.txt_title);
            txtVideoViewers = itemView.findViewById(R.id.txt_views);
            time = itemView.findViewById(R.id.live_time_textview);
        }
    }
}
