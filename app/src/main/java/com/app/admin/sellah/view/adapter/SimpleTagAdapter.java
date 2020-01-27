package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.Show_tag_result_fragment;

import java.util.List;

public class SimpleTagAdapter extends RecyclerView.Adapter<SimpleTagAdapter.MyViewHolder> {


    TextView titleTextView;

    private Context context;

    List<com.app.admin.sellah.model.extra.ProductDetails.Tag> title;
    boolean from_product ;

    public SimpleTagAdapter(Context context, List<com.app.admin.sellah.model.extra.ProductDetails.Tag> title,boolean from_product) {
        this.context = context;
        this.title = title;
        this.from_product = from_product;
    }

    public SimpleTagAdapter(Context context, List<com.app.admin.sellah.model.extra.ProductDetails.Tag> title) {
        this.context = context;
        this.title = title;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

            titleTextView = view.findViewById(R.id.tag_text);
        }
    }

    @Override
    public SimpleTagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_tag_adapter_view, parent, false);

        return new SimpleTagAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleTagAdapter.MyViewHolder holder, int position) {
        titleTextView.setText(title.get(position).getTagName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from_product)
                {

                    Show_tag_result_fragment fragment = new Show_tag_result_fragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("tag",title.get(position).getTagName());
                    bundle.putString("root","tags");
                    fragment.setArguments(bundle);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
                }
                           }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();

    }
}