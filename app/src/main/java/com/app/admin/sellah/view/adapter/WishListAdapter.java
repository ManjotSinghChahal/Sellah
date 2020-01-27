package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;

import static com.app.admin.sellah.controller.utils.Global.convertUTCToLocal;
import static com.app.admin.sellah.controller.utils.Global.getTimeAgo;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishHolder> {
    private GetProductList wishList;
    Context context;
    RemoveWishlistCallback callback;

    public WishListAdapter(GetProductList sublist, Context context, RemoveWishlistCallback callback) {
        this.wishList = sublist;
        this.context = context;
        this.callback=callback;
    }

    @Override
    public WishListAdapter.WishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_birth_adapter, parent, false);
        WishListAdapter.WishHolder gvh = new WishListAdapter.WishHolder(view);
        return gvh;
    }



    @Override
    public void onBindViewHolder(WishListAdapter.WishHolder holder, final int position) {
//        holder.profileImg.setImageResource(wishList.get(position).getProfileImage());
       /* RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/
        String imageUrl = "";
        if (wishList.getResult().get(position).getProductImages() != null) {
            imageUrl = !wishList.getResult().get(position).getProductImages().isEmpty() ? wishList.getResult().get(position).getProductImages().get(0).getImage() : "";
//            imageUrl.replace("productimages","thproductimages");
            imageUrl = imageUrl.replace("productimages","thproductimages");
        }
        RequestOptions requestOptions=Global.getGlideOptions();
        Glide.with(context)
                .load(imageUrl).apply(requestOptions)
                .into(holder.profileImg);

        holder.BirthText.setText(wishList.getResult().get(position).getName());
        holder.costText.setText("S$ "+wishList.getResult().get(position).getPrice());
        holder.timeText.setText(getTimeAgo(convertUTCToLocal(wishList.getResult().get(position).getWishlistTime())));
//
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_Dialogs.getRemoveItemDialog(context,((dialog, which) -> {
                    callback.onRemoveWishlist(wishList.getResult().get(position).getId(),position);
                })).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, wishList.getResult().get(position));
                ProductFrgament fragment = new ProductFrgament();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishList.getResult().size();
    }
    public class WishHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView BirthText,costText,timeText;
        ImageButton remove;

        public WishHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.img_product);
            BirthText = view.findViewById(R.id.text_birthday);
            costText = view.findViewById(R.id.cake_cost);
            timeText = view.findViewById(R.id.text_time);
            remove = view.findViewById(R.id.wish_remove_img);
        }

    }
    public interface RemoveWishlistCallback {
        public void onRemoveWishlist(String productId,int postion);
    }

}
