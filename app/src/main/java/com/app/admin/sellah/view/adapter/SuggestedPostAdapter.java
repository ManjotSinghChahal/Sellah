package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;

public class SuggestedPostAdapter extends RecyclerView.Adapter<SuggestedPostAdapter.ViewHolder>{
    GetProductList add_items;
    Context context;
    public SuggestedPostAdapter(Context context,GetProductList items) {
        this.add_items = items;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addition_items_adapter, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_name.setText(add_items.getResult().get(position).getName());

        if (add_items.getResult().get(position).getPromoteProduct().equalsIgnoreCase("Y"))
        {
           holder.feature.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.feature.setVisibility(View.GONE);

        }
        holder.item_cost.setText("S$ "+add_items.getResult().get(position).getPrice());
        Glide.with(context)
                .load(!add_items.getResult().get(position).getProductImages().isEmpty() ? add_items.getResult().get(position).getProductImages().get(0).getImage() : "")
                .apply(Global.getGlideOptions())
                .into(holder.add_image);

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
        public TextView item_name,item_cost,feature;

        public ViewHolder(View itemView) {
            super(itemView);
            add_image = (ImageView) itemView.findViewById(R.id.additional_image);
            item_name = (TextView) itemView.findViewById(R.id.tb_item_name);
            item_cost = (TextView) itemView.findViewById(R.id.tb_item_cost);
            feature = (TextView) itemView.findViewById(R.id.suggest_feature);

        }
    }
}
