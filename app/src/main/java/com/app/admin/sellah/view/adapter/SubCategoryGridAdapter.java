package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;

public class SubCategoryGridAdapter extends RecyclerView.Adapter<SubCategoryGridAdapter.ViewHolder> {

    GetProductList mData;
    LayoutInflater mInflater;
    Context context;


    public SubCategoryGridAdapter(Context context, GetProductList mainList) {
        this.mInflater=LayoutInflater.from(context);
        this.mData=mainList;
        this.context=context;
    }


    @Override
    @NonNull
    public SubCategoryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_categories_adapter, parent, false);
        return new SubCategoryGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubCategoryGridAdapter.ViewHolder holder, int position) {

  /*      RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);
*/
        String imageUrl = "";
        if (mData.getResult().get(position).getProductImages() != null) {
            imageUrl = !mData.getResult().get(position).getProductImages().isEmpty() ? mData.getResult().get(position).getProductImages().get(0).getImage() : "";
//            imageUrl.replace("productimages","thproductimages");
            imageUrl = imageUrl.replace("productimages","thproductimages");
        }
        RequestOptions requestOptions=Global.getGlideOptions();
       try {
           Glide.with(context)
                   .load(imageUrl)
                   .apply(requestOptions)
                   .into(holder.imageView);
       }catch (Exception e){

       }

        if(!TextUtils.isEmpty(mData.getResult().get(position).getPromoteProduct())&&mData.getResult().get(position).getPromoteProduct().equalsIgnoreCase("S")){
            holder.imgFeatured.setVisibility(View.VISIBLE);
        }else{
            holder.imgFeatured.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mData.getResult().get(position).getProductVideo())) {
            holder.imgVideoIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.imgVideoIndicator.setVisibility(View.GONE);
        }
        holder.txtItemName.setText(mData.getResult().get(position).getName());
        holder.txtItemCost.setText(mData.getResult().get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, mData.getResult().get(position));
                ProductFrgament fragment=new ProductFrgament();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
//                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductFrgament()).commit();
            }
        });
    }




    @Override
    public int getItemCount() {
        return mData.getResult().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView,imgVideoIndicator;
        TextView txtItemName,txtItemCost;
        TextView imgFeatured;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.third_image);
            txtItemName = itemView.findViewById(R.id.txt_product_name);
            txtItemCost = itemView.findViewById(R.id.txt_product_cost);
            imgFeatured = itemView.findViewById(R.id.img_featured);
            imgVideoIndicator = itemView.findViewById(R.id.img_video_indicator);
        }

    }

}
