package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.Database.UserSearch;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.FolowModelNew.FollowModelNew;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.PersonalProfileFragment;
import com.app.admin.sellah.view.fragments.Show_tag_result_fragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {


    Context context;
    String title;
    FollowListAdpter.UnlickClickController controller;
    ArrayList<String> arrayList;

    public SearchAdapter(Context activity, ArrayList<String> arrayList) {


        context = activity;
        this.arrayList = arrayList;


    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchview = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_autocomplete_text_design, parent, false);
        SearchAdapter.ViewHolder cvh = new SearchAdapter.ViewHolder(searchview);
        return cvh;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {

        holder.textview_search.setText(arrayList.get(position));

/*        holder.search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });*/

        holder.cross_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSearch userSearch = new UserSearch();
                userSearch.setSearched(arrayList.get(position));
                ((MainActivity)context).myDatabase.myDao().deleteSearch(userSearch);

                MainActivity.arrayListSearched.remove(position);
                ((MainActivity)context).searchAdapter.notifyDataSetChanged();
            }
        });

        holder.rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)context).recyclerviewSearch.setVisibility(View.GONE);
                Show_tag_result_fragment fragment = new Show_tag_result_fragment();

                Bundle bundle = new Bundle();
                bundle.putString("tag", arrayList.get(position));
                bundle.putString("root", "search");
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();


            }
        });


    }



    @Override
    public int getItemCount() {
      return   arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview_search;
        ImageView search_icon,cross_search;
        RelativeLayout rel_search;

        public ViewHolder(View view) {
            super(view);
            textview_search = view.findViewById(R.id.textview_search);
            search_icon = view.findViewById(R.id.search_icon);
            cross_search = view.findViewById(R.id.cross_search);
            rel_search = view.findViewById(R.id.rel_search);
        }
    }


    public interface UnlickClickController{
        void onUnlikeClick(String userId,int pos);
    }
}
