package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.wishllist_model.Wishlist;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;

import static com.app.admin.sellah.controller.utils.Global.clearFocus;
import static com.app.admin.sellah.controller.utils.Global.convertUTCToLocal;
import static com.app.admin.sellah.controller.utils.Global.getTimeAgo;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class WishRecordAdapter  extends RecyclerView.Adapter<WishRecordAdapter.WishHolder> {
    private Wishlist recordList;
    Context context;

    public WishRecordAdapter(Wishlist sublist, Context context) {
        this.recordList = sublist;
        this.context = context;
    }

    @Override
    public WishRecordAdapter.WishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profiile_record_adapter, parent, false);
        WishRecordAdapter.WishHolder gvh = new WishRecordAdapter.WishHolder(view);
        return gvh;
    }



    @Override
    public void onBindViewHolder(WishRecordAdapter.WishHolder holder, final int position) {
      /*  RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/
        String imageUrl = "";
        if (recordList.getResult().get(position).getProductImages() != null) {
            imageUrl = !recordList.getResult().get(position).getProductImages().isEmpty() ? recordList.getResult().get(position).getProductImages().get(0).getImage() : "";
            imageUrl = imageUrl.replace("productimages","thproductimages");
        }
        RequestOptions requestOptions=Global.getGlideOptions();
        Glide.with(context)
                .load(imageUrl).apply(requestOptions)
                .into(holder.profileImg);
        holder.BirthText.setText(recordList.getResult().get(position).getProductName());
        holder.costText.setText("S$ "+recordList.getResult().get(position).getPrice());

        holder.messimage.setOnClickListener(view -> {


            if (HelperPreferences.get(context).getString(UID).equalsIgnoreCase(recordList.getResult().get(position).getSellerId()))
            {
                Intent chatIntent = new Intent(context, ChatActivity.class);
                chatIntent.putExtra("otherUserId",recordList.getResult().get(position).getBuyerId());
                chatIntent.putExtra("otherUserImage", recordList.getResult().get(position).getBuyer_image());
                chatIntent.putExtra("otherUserName", recordList.getResult().get(position).getBuyer_name());
                context.startActivity(chatIntent);
            }
            else
            {
                Intent chatIntent = new Intent(context, ChatActivity.class);
                chatIntent.putExtra("otherUserId",recordList.getResult().get(position).getSellerId());
                chatIntent.putExtra("otherUserImage", recordList.getResult().get(position).getSeller_image());
                chatIntent.putExtra("otherUserName", recordList.getResult().get(position).getSeller_name());
                context.startActivity(chatIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recordList.getResult().size();
    }

    public class WishHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView BirthText,costText;
        Button messimage;



        public WishHolder(View view) {
            super(view);

            profileImg = view.findViewById(R.id.img_product);
            BirthText = view.findViewById(R.id.text_birthday);
            costText = view.findViewById(R.id.cake_cost);
            messimage = view.findViewById(R.id.mess_image);
        }



    }
}
