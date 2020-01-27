package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.SellProductInterface;

import java.util.ArrayList;

public class AddProductTagsAdapter extends RecyclerView.Adapter<AddProductTagsAdapter.AddProductTagsViewHolder> {
    private ArrayList<String> tag_list;
    Context context;
    SellProductInterface sellProductInterface;
    public AddProductTagsAdapter(ArrayList<String> addList, Context activity, SellProductInterface sellProductInterface) {

        this.tag_list = addList;
        context = activity;
        this.sellProductInterface=sellProductInterface;

    }

    @Override
    public AddProductTagsAdapter.AddProductTagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_tag_adapter, parent, false);
        AddProductTagsAdapter.AddProductTagsViewHolder cvh = new AddProductTagsAdapter.AddProductTagsViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public int getItemCount() {

        return tag_list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductTagsViewHolder holder, int position) {
        holder.textTag1.setText(tag_list.get(position));
        if(tag_list.size()!=3){
            sellProductInterface.setTagVisiblity(true);
        }else{
            sellProductInterface.setTagVisiblity(false);
        }
       holder.tagRemove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            tag_list.remove(position);
            tag_list.remove(position);
            notifyDataSetChanged();
        }

    });
        /*if(tag_list.size()<=3){
            sellProductInterface.setTagVisiblity(true);
        }else{
            sellProductInterface.setTagVisiblity(false);
        }*/
    }



    public class AddProductTagsViewHolder extends RecyclerView.ViewHolder {
       TextView textTag1;
       ImageView tagRemove;


        public AddProductTagsViewHolder(View view) {
            super(view);
            textTag1 = view.findViewById(R.id.text_tag1);
            tagRemove = view.findViewById(R.id.tagCross);


        }
    }


}
