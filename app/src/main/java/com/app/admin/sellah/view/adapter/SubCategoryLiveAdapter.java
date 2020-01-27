package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.model.extra.SubCatImageModel;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.bumptech.glide.Glide;

import java.util.List;

public class SubCategoryLiveAdapter extends RecyclerView.Adapter<SubCategoryLiveAdapter.LiveHolder> {
    private List<VideoList> imageList;

    Context context;

    public SubCategoryLiveAdapter(List<VideoList> sublist, Context context) {
        this.imageList = sublist;
        this.context = context;
    }

    @Override
    public SubCategoryLiveAdapter.LiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_live_adapter, parent, false);
        SubCategoryLiveAdapter.LiveHolder gvh = new SubCategoryLiveAdapter.LiveHolder(groceryProductView);
        return gvh;
    }



    @Override
    public void onBindViewHolder(LiveHolder holder, final int position) {

//        holder.imageView.setImageResource(imageList.get(position).getProductImage());
        Glide.with(context)
                .load(imageList.get(position).getUserImage())
                .apply(Global.getGlideOptions())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                intent.putExtra("value", "noLiveStream");
                intent.putExtra("id",imageList.get(position).getVideoId());
                intent.putExtra("group_id",imageList.get(position).getGroupId());
                intent.putExtra("product_id",imageList.get(position).getProductId());
                intent.putExtra("product_name",imageList.get(position).getProductName());
                intent.putExtra("seller_id",imageList.get(position).getSellerId());
                intent.putExtra("start_time",imageList.get(position).getStartTime());
                intent.putExtra("views",imageList.get(position).getViews());
                context.startActivity(intent);
//                context.startActivity(new Intent(context,VideoPlayerActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class LiveHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public LiveHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.live_image);
        }

    }
}
