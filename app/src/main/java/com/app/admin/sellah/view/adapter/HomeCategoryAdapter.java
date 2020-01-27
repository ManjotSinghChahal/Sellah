package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.view.fragments.SubCategoryFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;
import static com.app.admin.sellah.view.adapter.InnerRecyclerAdapter.getDominantColor;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder> {
    //    private List<HomeModelClass> categoriesList;
    Context context;
    private GetCategoriesModel categoriesModel;

    public HomeCategoryAdapter(/*List<HomeModelClass> categoriesList,*/ FragmentActivity activity, GetCategoriesModel categoriesModel) {

//        this.categoriesList = categoriesList;
        context = activity;
        this.categoriesModel = categoriesModel;

    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_row_view, parent, false);
        CategoryViewHolder cvh = new CategoryViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
//        holder.imageView.setImageResource(R.drawable.category_icon);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
//        requestOptions.dontAnimate();
        requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        /*requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/
        Glide.with(context)
                .asBitmap()
                .load(categoriesModel.getResult().get(position).getSubcategories().get(0).getImage())
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.imgInnerview.setImageBitmap(resource);
                        holder.txtView.setBackgroundColor(0xff000000 + getDominantColor(resource));
                        holder.txtView.getBackground().setAlpha(128);
                    }
                });
       /* Glide.with(context)
                .load(categoriesModel.getResult().get(position).getImage())
                .apply(requestOptions)
                .into(holder.imgInnerview);*/

        holder.txtView.setText(categoriesModel.getResult().get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.CAT_ID, categoriesModel.getResult().get(position).getCatId());
                bundle.putInt(SAConstants.Keys.CAT_POS, position);
                bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) categoriesModel.getResult().get(position).getSubcategories());
                SubCategoryFragment fragment = new SubCategoryFragment();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(HOMETAG).commit();
                Log.e("Cat_id", categoriesModel.getResult().get(position).getCatId());
            }
        });

    }


    @Override
    public int getItemCount() {
        try {
            return categoriesModel.getResult().size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgInnerview;
        TextView txtView;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            imgInnerview=itemView.findViewById(R.id.img_innerview);
            txtView=itemView.findViewById(R.id.innerview);
        }
        /*AppCompatImageView imageView;
        TextView txtview;
        LinearLayout linearLayout;

        public CategoryViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            txtview = view.findViewById(R.id.txt_otheruser_name);
            linearLayout = view.findViewById(R.id.homeCategorylayout);
        }*/
    }

}
