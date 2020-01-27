package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.CustomDialogs.PromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.fragments.HomeFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ProductFrgament;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.getTotalHeightofGridRecyclerView;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> implements Filterable {

    GetProductList mData, filtedData;
    Context context;
    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;
    Call<GetProductList> getProductsCall;
    WebService service;
    private ArrayList<Result> resultList;

    public HomeProductAdapter(FragmentActivity activity, GetProductList mainList) {
        this.context = activity;
        this.mData = mainList;
        this.filtedData = mData;
        service = Global.WebServiceConstants.getRetrofitinstance();
        resultList = new ArrayList<>();
    }


    public void filterList(GetProductList newMessList) {
        this.filtedData = newMessList;
        notifyDataSetChanged();
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_adapter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Log.e("adpterPosPrint",position+"");

      /* if (position==0)
            getProductlist();*/
        /*   else if (position%10==0)*/
           // getProductlist();



//        holder.imageView.setImageResource(mData.getResult().get(position).getImage());
   /*     RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);
*/
        if (isLogined(context) &&
                mData.getResult().get(position).getUserId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID)) /*&&
                mData.getResult().get(position).getPromoteProduct().equalsIgnoreCase("S")*/
            /*    && mData.getResult().get(position).getPromotes() != null
                && mData.getResult().get(position).getPromotes().size() > 0*/) {
            holder.txtPromoteButton.setVisibility(View.VISIBLE);
        } else {
            holder.txtPromoteButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mData.getResult().get(position).getProductVideo())) {
            holder.imgVideoIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.imgVideoIndicator.setVisibility(View.GONE);
        }
        holder.txtPromoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromoteDialog.create(context, mData.getResult().get(position).getId(), new PromoteDialog.PromoteCallback() {
                    @Override
                    public void onPromoteSuccess() {
                        mData.getResult().get(position).setPromoteProduct("S");
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onPromoteFailure() {

                    }
                }).show();
            }
        });
        String imageUrl = "";
        if (mData.getResult().get(position).getProductImages() != null) {
            imageUrl = !mData.getResult().get(position).getProductImages().isEmpty() ? mData.getResult().get(position).getProductImages().get(0).getImage() : "";
            imageUrl = imageUrl.replace("productimages", "thproductimages");
//            Log.e("imageUrl", "onBindViewHolder: "+imageUrl);
        }
        RequestOptions requestOptions = Global.getGlideOptions();
        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(holder.imageView);

        if (!TextUtils.isEmpty(mData.getResult().get(position).getPromoteProduct()) && mData.getResult().get(position).getPromoteProduct().equalsIgnoreCase("S")) {
            holder.imgFeatured.setVisibility(View.VISIBLE);
        } else {
            holder.imgFeatured.setVisibility(View.GONE);
        }
        holder.txtProductCost.setText(mData.getResult().get(position).getPrice());
        holder.txtProductName.setText(mData.getResult().get(position).getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, mData.getResult().get(position));
                ProductFrgament fragment = new ProductFrgament();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
//                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductFrgament()).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return filtedData.getResult() == null ? 0 : filtedData.getResult().size();
//        return ;
    }

   /* @Override
    public int getItemViewType(int position) {
        return (position == filtedData.getResult().size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }*/

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filtedData = mData;
                } else {
                    List<Result> results = new ArrayList<>();
//
                    for (Result row : filtedData.getResult()) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCategoryName().contains(charSequence) || row.getSubcategoryName().contains(charSequence)) {
                            results.add(row);
                        }
                    }

                    filtedData.setResult(results);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterResults;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filtedData = (GetProductList) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView,imgVideoIndicator;
        TextView txtProductName, txtProductCost, txtPromoteButton;
        LinearLayout linearLayout;
        TextView imgFeatured;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.third_image);
            linearLayout = itemView.findViewById(R.id.main_grid_layout);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtProductCost = itemView.findViewById(R.id.txt_product_cost);
            imgFeatured = itemView.findViewById(R.id.img_featured);
            txtPromoteButton = itemView.findViewById(R.id.txt_promote_button);
            imgVideoIndicator = itemView.findViewById(R.id.img_video_indicator);
        }

    }



    public void getProductlist() {
        if (!Global.NetworStatus.isOnline(context) || Global.NetworStatus.isInternetAvailable()) {
            S_Dialogs.getNetworkErrorDialog(context).show();

        } else {
            Log.e("printID", "getProductlist_ID: " + HelperPreferences.get(context).getString(UID)+"");
            getProductsCall = service.getProductListApi(HelperPreferences.get(context).getString(UID), "", "", String.valueOf(HomeFragment.currentPage));
            getProductsCall.enqueue(new Callback<GetProductList>() {
                @Override
                public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                    if (response.isSuccessful()) {

                        Log.e("printResponse","success");


                    //    HomeFragment.TOTAL_PAGES = Integer.parseInt(response.body().getTotalPages());
                        try {
                            mData.setStatus(response.body().getStatus());
                            mData.setMessage(response.body().getMessage());
                            resultList.addAll(response.body().getResult());
                            mData.setResult(resultList);
                          //  notifyDataSetChanged();

                          //  HomeFragment.mainCategoriesAdapter.notifyItemRangeInserted(mData.getResult().size() > 0 ? mData.getResult().size() - 1 : 0, mData.getResult().size());
                            /*if (mData.getResult().size() > 0) {
                                getTotalHeightofGridRecyclerView(context, rvProducts, R.layout.main_categories_adapter, 1);
                            }*/
                            //--------------------------------------------
                            Log.e("successsss",Global.DEEP_LINKING_PRODUCT_ID+"   outer");







                          //  setSearchData();
                        } catch (Exception e) {

                        }
//                        mainCategoriesAdapter.notifyDataSetChanged();
//                        setMainProductList(productList);
                        Log.e("Getproducts", "Success" + response.body().toString());
                    } else {

                    }

                }

                @Override
                public void onFailure(Call<GetProductList> call, Throwable t) {

                }
            });
        }
    }


}
