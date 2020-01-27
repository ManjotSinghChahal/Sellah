package com.app.admin.sellah.view.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.SeeMoreCategoryAdapter;

import java.util.ArrayList;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;

/**
 * Created by hp on 07-10-2018.
 */

public class SeeMoreFragment extends Fragment {

    RecyclerView mainCategorylistView;
    private SeeMoreCategoryAdapter seeMoreCategoryAdapter;
    GetCategoriesModel categoriesModel;
    public SeeMoreFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.see_more_fragement, container, false);
        hideSearch();
        categoriesModel=ExpandableListData.getData();
        mainCategorylistView =view.findViewById(R.id.recycler);
        mainCategorylistView.setHasFixedSize(true);
        // use a linear layout manager
        mainCategorylistView.setLayoutManager(new LinearLayoutManager(getActivity()));
        seeMoreCategoryAdapter = new SeeMoreCategoryAdapter(getActivity(),categoriesModel,(catId,subCatId,subcategories)->{
            Bundle bundle=new Bundle();
            bundle.putString(SAConstants.Keys.CAT_ID, catId);
            bundle.putString(SAConstants.Keys.SUB_CAT_ID, subCatId);
            bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategories);
            SubCategoryFragment fragment=new SubCategoryFragment();
            fragment.setArguments(bundle);
            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(HOMETAG).commit();
            Log.e("Cat_id",catId);
        });
        mainCategorylistView.setAdapter(seeMoreCategoryAdapter);
        return view;
    }
    public void hideSearch() {
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(0);

    }
}
