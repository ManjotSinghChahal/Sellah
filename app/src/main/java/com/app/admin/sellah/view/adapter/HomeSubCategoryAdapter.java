package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.HomeModelClass;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeSubCategoryAdapter extends RecyclerView.Adapter<HomeSubCategoryAdapter.CategoryViewHolder> {
    private List<HomeModelClass> subCategoriesList;
    Context context;

    public HomeSubCategoryAdapter(List<HomeModelClass> sublist, Context context) {
        this.subCategoriesList = sublist;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_adapter, parent, false);
        CategoryViewHolder gvh = new CategoryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        holder.imageView.setImageResource(subCategoriesList.get(position).getProductImage());
        holder.txtview.setText(subCategoriesList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return subCategoriesList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        TextView txtview;

        public CategoryViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            txtview = view.findViewById(R.id.txt_otheruser_name);
        }
    }
}
