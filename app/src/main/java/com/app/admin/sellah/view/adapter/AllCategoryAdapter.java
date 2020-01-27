package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.BlockedUserModel.BlockListModel;
import com.app.admin.sellah.model.extra.Categories.Result;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.SubCategoryFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder> {

    private List<Result> listData;
    Context context;
    List<Integer> colorList;
    OnItemClickedListner listner;
    public AllCategoryAdapter(List<Result> results, Context activity, List<Integer> colorList,OnItemClickedListner listner) {

        this.listData = results;
        context = activity;
        this.colorList=colorList;
        this.listner=listner;

    }

    @Override
    public AllCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_category_adapter_view, parent, false);
        AllCategoryViewHolder cvh = new AllCategoryViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final AllCategoryViewHolder holder, final int position) {

        holder.imgCatColor.setColorFilter(colorList.get(position));
        holder.imgCatColor.setAlpha(0.5f);
        holder.txtName.setText(listData.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.CAT_ID, listData.get(position).getCatId());
                bundle.putInt(SAConstants.Keys.CAT_POS, position);
                bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) listData.get(position).getSubcategories());
                SubCategoryFragment fragment = new SubCategoryFragment();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(HOMETAG).commit();
                Log.e("Cat_id", listData.get(position).getCatId());
                listner.onItemClicked();
            }
        });
    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public class AllCategoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgCatColor;
        TextView txtName;

        public AllCategoryViewHolder(View view) {
            super(view);
            imgCatColor = view.findViewById(R.id.img_cat_color);
            txtName = view.findViewById(R.id.txt_name);
        }
    }

    public interface OnItemClickedListner{
        void onItemClicked();
    }

}