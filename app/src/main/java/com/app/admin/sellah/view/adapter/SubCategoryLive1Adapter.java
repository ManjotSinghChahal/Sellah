package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.SubCategoryController;
import com.app.admin.sellah.model.extra.Categories.Subcategory;

import java.util.List;

public class SubCategoryLive1Adapter extends RecyclerView.Adapter<SubCategoryLive1Adapter.SubCategoryHolder> {
    private List<String> subcategoryList;
    Context context;
    SubCategoryController controller;
    private int row_index = 0;
    private String subCatId="";

    public SubCategoryLive1Adapter(List<String> textList, Context context, SubCategoryController controller) {
        this.subcategoryList = textList;
        this.context = context;
        this.controller = controller;

    }

    @NonNull
    @Override
    public SubCategoryLive1Adapter.SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_sale_adapter, parent, false);
        SubCategoryLive1Adapter.SubCategoryHolder scvh = new SubCategoryLive1Adapter.SubCategoryHolder(view);
        return scvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryLive1Adapter.SubCategoryHolder holder, int position) {
        holder.textName.setTextColor(Color.parseColor("#a3a3a3"));
        holder.textName.setText(subcategoryList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                if (position == 0) {
                    controller.onSubCategotyClick("");
                } else {
                    controller.onSubCategotyClick(subcategoryList.get(position));

                }
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.view.setVisibility(View.VISIBLE);
            holder.textName.setTextColor(Color.parseColor("#000000"));
            holder.textName.setTypeface(null,Typeface.BOLD);
           /* holder.rlSubCatRoot.setBackgroundResource(R.drawable.selected_subcat_drawable);
            holder.textName.setTextColor(context.getResources().getColor(R.color.colorWhite));*/
        } else {
            holder.view.setVisibility(View.INVISIBLE);
           /* holder.rlSubCatRoot.setBackgroundResource(R.drawable.textview_back);
            holder.textName.setTextColor(context.getResources().getColor(R.color.colorBlack));*/
        }

    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {
        TextView textName;
        RelativeLayout rlSubCatRoot;
        View view;

        public SubCategoryHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_tag1);
            rlSubCatRoot = itemView.findViewById(R.id.rl_subcat_root);
            view = itemView.findViewById(R.id.view_selected);
        }
    }
}
