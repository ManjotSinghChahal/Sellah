package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Categories.Subcategory;
import com.app.admin.sellah.controller.utils.Global;

import java.util.List;

/**
 * Created by abc on 6/2/18.
 */

public class InnerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_FIRST_ITEM = 0;
    public static final int TYPE_ITEM = 1;
    private Context context;
    int prar;
    int prar1;
    List<Subcategory> subcategoryList;
    SubcatClickCallback callback;
    String catid;
//    ArrayList<HashMap<String, Object>> subcategoryList;
    public InnerRecyclerAdapter(Context context, int parentId, int parentId1, String catId,List<Subcategory> subcategories,SubcatClickCallback callback){
        this.callback=callback;
        prar=parentId;
        prar1=parentId1;
        this.context =context;
        this.subcategoryList=subcategories;
        this.catid=catId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FIRST_ITEM:
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(prar, parent, false);
                return new BigViewHolder(view);
            case TYPE_ITEM:
                final View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(prar1, parent, false);
                return new NormalViewHolder(view2);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestOptions requestOptions=Global.getGlideOptions();
        switch (holder.getItemViewType()) {
            case TYPE_FIRST_ITEM:
                BigViewHolder bigViewHolder = (BigViewHolder) holder;
                // Do what you need for the first item
                bigViewHolder.textView.setText(subcategoryList.get(position).getName());

                Glide.with(context)
                        .asBitmap()
                        .load(subcategoryList.get(position).getImage())
                        .apply(requestOptions)
                        .into(new SimpleTarget<Bitmap>() {
                                @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    bigViewHolder.imageView.setImageBitmap(resource);
                                    bigViewHolder.textView.setBackgroundColor(0xff000000 + getDominantColor(resource));
                                    bigViewHolder.textView.getBackground().setAlpha(128);
                                }
                        });

                bigViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onSubCatClick(catid, subcategoryList.get(position).getId(),subcategoryList);
                    }
                });

                break;
            case TYPE_ITEM:
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                // Do what you for the other items
                normalViewHolder.textView.setText(subcategoryList.get(position).getName().toString());
                Glide.with(context)
                        .asBitmap()
                        .load(subcategoryList.get(position).getImage())
                        .apply(requestOptions)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                normalViewHolder.imageView.setImageBitmap(resource);
                                normalViewHolder.textView.setBackgroundColor(0xff000000 + getDominantColor(resource));
                                normalViewHolder.textView.getBackground().setAlpha(128);
                            }
                        });
                normalViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onSubCatClick(catid, subcategoryList.get(position).getId(),subcategoryList);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIRST_ITEM;
        } else return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    final class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.innerview);
            imageView=itemView.findViewById(R.id.img_innerview);

        }
        // find your views here
    }

    final class BigViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public BigViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.innerview);
            imageView=itemView.findViewById(R.id.img_innerview);
        }
        // find your views here
    }
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public interface SubcatClickCallback{
        void onSubCatClick(String catId,String subCatId, List<Subcategory> subcategoryList);
    }

}