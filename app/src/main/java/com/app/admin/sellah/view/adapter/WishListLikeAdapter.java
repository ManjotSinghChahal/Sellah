package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.HomeModelClass;

import java.util.ArrayList;

public class WishListLikeAdapter extends RecyclerView.Adapter<WishListLikeAdapter.ViewHolder>{
    ArrayList<HomeModelClass> add_items;
    Context context;

    public WishListLikeAdapter(ArrayList<HomeModelClass> wishList, FragmentActivity activity) {
        add_items=wishList;
        context=activity;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_like_adapter, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productCost.setText(add_items.get(position).getProductName());
        holder.productImage.setImageResource(add_items.get(position).getProductImage());
    }


    @Override
    public int getItemCount() {
        return add_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName,productCost;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.wish_product_image);
            productCost = (TextView) itemView.findViewById(R.id.wish_product_cost);

        }
    }
}
