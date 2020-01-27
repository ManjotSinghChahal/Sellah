package com.app.admin.sellah.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;

import java.util.List;

public class NewProductAddvideo_Adapter  extends RecyclerView.Adapter<NewProductAddvideo_Adapter.Viewholder> {
    List<Integer> list;
    public NewProductAddvideo_Adapter(List<Integer> list) {
        this.list = list;
    }
        @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.newproduct_addvideo_recycler_items, viewGroup, false);

            return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
            viewholder.image.setBackgroundResource(list.get(i));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
            ImageView image,cancelImage;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_image);
            cancelImage = itemView.findViewById(R.id.item_delete);
        }
    }
}