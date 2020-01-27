package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SellProductInterface;
import com.app.admin.sellah.model.AddProductDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Add_Product_Cars_Adapter  extends RecyclerView.Adapter<Add_Product_Cars_Adapter.AddProductViewHolder> {
    private List<String> add_list;
    Context context;
    SellProductInterface sellProductInterface;



    public Add_Product_Cars_Adapter(FragmentActivity activity, List<String> imageList, SellProductInterface sellProductInterface) {

        this.add_list = imageList;
        context = activity;
        this.sellProductInterface=sellProductInterface;
    }

    @Override
    public Add_Product_Cars_Adapter.AddProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_car_adapter, parent, false);
//        Add_Product_Cars_Adapter.AddProductViewHolder cvh = new Add_Product_Cars_Adapter.AddProductViewHolder(groceryProductView);
        return new AddProductViewHolder(groceryProductView);
    }

    @Override
    public void onBindViewHolder(final Add_Product_Cars_Adapter.AddProductViewHolder holder, final int position) {

        Log.e( "onBindViewHolderAdd: ",add_list.get(position) );


        File f = new File(getRealPathFromURI(Uri.parse(add_list.get(position))));

        if (add_list.size()>0)
        {
            Glide.with(context)
                    .load(add_list.get(position))
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.image)
                                    .centerCrop()
                    )
                    .into(holder.add_car);
        }
        else
        {


        Glide.with(context)
                .load(add_list.get(position))
                .apply(
                        new RequestOptions()
                                .error(R.drawable.image)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Log.e( "onLoadFailed: ",e.getLocalizedMessage() );
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(holder.add_car);

    }


        if(position==0){
            holder.addCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }else{
            holder.addCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        if(add_list.size()!=5){
            sellProductInterface.setImageCaptureVisiblty(true);
        }else{
            sellProductInterface.setImageCaptureVisiblty(false);
        }

        Log.e("items",""+add_list.get(position));


        holder.crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("printImageUrl---->",AddProductDatabase.imageListGStatus.size()+"");
                //-----------------To remove id and url------------
                for (int i = 0; i <AddProductDatabase.imageListGStatus.size() ; i++)
                {
                    if (AddProductDatabase.imageListGStatus.get(i).get("image_url").equalsIgnoreCase(add_list.get(position)))
                    {
                        AddProductDatabase.imageListGStatus.get(i).put("image_status","true");
                        break;
                    }
                }



                add_list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, add_list.size());




            }
        });

    }


    @Override
    public int getItemCount() {
        return add_list.size();
    }

    public class AddProductViewHolder extends RecyclerView.ViewHolder {
        ImageView crossImage,add_car;
        CardView addCardView;
        public AddProductViewHolder(View view) {
            super(view);
            crossImage = view.findViewById(R.id.crossImg);
            add_car = view.findViewById(R.id.add_car_Img);
            addCardView = view.findViewById(R.id.add_car_photo);

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
