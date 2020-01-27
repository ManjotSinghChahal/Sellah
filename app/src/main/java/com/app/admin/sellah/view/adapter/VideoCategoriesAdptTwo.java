package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;

import java.util.List;

/**
 * Created by Admin on 11/19/2018.
 */

public class VideoCategoriesAdptTwo extends RecyclerView.Adapter<VideoCategoriesAdptTwo.ViewHolder> {
    Context context;
    List<VideoList> videoLists;
    public VideoCategoriesAdptTwo(Context context, List<VideoList> videoLists) {
        this.context=context;
        this.videoLists=videoLists;
        Log.e("csdvcsd123",videoLists.size()+"");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_live_videos_adapter_view, parent, false);
        ViewHolder cvh = new ViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        Glide.with(context).load(videoLists.get(position).get)

        holder.Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                intent.putExtra("value", "noLiveStream");
                intent.putExtra("id",videoLists.get(position).getVideoId());
                intent.putExtra("group_id",videoLists.get(position).getGroupId());
                intent.putExtra("product_id",videoLists.get(position).getProductId());
                intent.putExtra("product_name",videoLists.get(position).getProductName());
                intent.putExtra("seller_id",videoLists.get(position).getSellerId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoview_categories;
        RelativeLayout Click;
        public ViewHolder(View itemView) {
            super(itemView);
            videoview_categories = itemView.findViewById(R.id.videoview_categories);
            Click = itemView.findViewById(R.id.Click);
        }
    }
}
