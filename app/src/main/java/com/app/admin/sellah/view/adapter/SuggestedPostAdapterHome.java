package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.app.admin.sellah.view.adapter.InnerRecyclerAdapter.getDominantColor;

public class SuggestedPostAdapterHome extends RecyclerView.Adapter<SuggestedPostAdapterHome.ViewHolder> {
    GetProductList add_items;
    Context context;

    public SuggestedPostAdapterHome(Context context, GetProductList items) {
        this.add_items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggested_post_view, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.item_name.setText(add_items.getResult().get(position).getName());
        holder.item_cost.setText("S$ " + add_items.getResult().get(position).getPrice());

        String time = Global.getTimeAgo(Global.convertUTCToLocal(add_items.getResult().get(position).getCreatedAt()));

        if (time.contains("1 days"))
        {
            holder.posted.setText("Posted 1 day ago" );
        }
        else
        {
            holder.posted.setText("Posted "  +time);
        }

       /* Glide.with(context)
                .asBitmap()
                .load(!add_items.getResult().get(position).getProductImages().isEmpty() ? add_items.getResult().get(position).getProductImages().get(0).getImage() : "")
                .apply(Global.getGlideOptions())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.add_image.setImageBitmap(resource);
                        holder.rootLayout.setBackgroundColor(0xff000000 + getDominantColor(resource));
                        holder.rootLayout.getBackground().setAlpha(128);
                    }
                });*/

        if (position % 2 == 0) {
            holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.colorEvenCardColor));
        } else {
            holder.rootLayout.setBackgroundColor(context.getResources().getColor(R.color.colorOddCardColor));

        }
/*        Glide.with(context)
                .load(!add_items.getResult().get(position).getProductImages().isEmpty() ? add_items.getResult().get(position).getProductImages().get(0).getImage() : "")
                .apply(Global.getGlideOptions())
                .into(holder.add_image);*/

        if (add_items.getResult().get(position).getProductImages().size()>0)
        {

            Glide.with(context)
                .load(add_items.getResult().get(position).getProductImages().get(0).getImage())
              //  .apply(Global.getGlideOptions())
                .into(holder.add_image);


        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, add_items.getResult().get(position));
                ProductFrgament fragment = new ProductFrgament();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

            }
        });

    }


    @Override
    public int getItemCount() {
        return add_items.getResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView add_image;
        public TextView item_name, item_cost,posted;
        public LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            add_image = (ImageView) itemView.findViewById(R.id.additional_image);
            item_name = (TextView) itemView.findViewById(R.id.tb_item_name);
            item_cost = (TextView) itemView.findViewById(R.id.tb_item_cost);
            posted = (TextView) itemView.findViewById(R.id.recommend_posted);
            rootLayout = itemView.findViewById(R.id.li_root_view);

        }
    }
}
