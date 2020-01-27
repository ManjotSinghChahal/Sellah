package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abc on 6/2/18.
 */

public class SeeMoreCategoryAdapter extends RecyclerView.Adapter<SeeMoreCategoryAdapter.MyViewHolder> {


    RecyclerView recyclerView;
    TextView titleTextView;
    CircleImageView titleImage;
    private Context context;
    private InnerRecyclerAdapter adapter, adapter1;
    GetCategoriesModel getCategoriesModel;
    //    ArrayList<HashMap<String, Object>> subcategoryList;
    InnerRecyclerAdapter.SubcatClickCallback callback;

    public SeeMoreCategoryAdapter(Context mainActivity, GetCategoriesModel title, InnerRecyclerAdapter.SubcatClickCallback callback) {
        context = mainActivity;
        this.getCategoriesModel = title;
        this.callback=callback;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.outer);
            titleImage = view.findViewById(R.id.img_category);
            titleTextView = view.findViewById(R.id.title_text);
//            imageView=view.findViewById(R.id.img_innerview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outerdesign, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        if(position==1){
//            recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        int parentId = R.layout.innerdesign;
        int parentId1 = R.layout.inner_desing_new;

        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.centerInside();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
//        requestOptions.dontAnimate();
        requestOptions.error(R.drawable.glide_error);
        requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

        Glide.with(context)
                .load(getCategoriesModel.getResult().get(position).getImage())
                .apply(requestOptions)
                .into(titleImage);

        adapter = new InnerRecyclerAdapter(context, parentId, parentId1, getCategoriesModel.getResult().get(position).getCatId(), getCategoriesModel.getResult().get(position).getSubcategories(), callback);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case InnerRecyclerAdapter.TYPE_FIRST_ITEM:
                        return 2;
                    case InnerRecyclerAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        titleTextView.setText(getCategoriesModel.getResult().get(position).getName());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public int getItemCount() {
        return getCategoriesModel.getResult().size();
    }
}
