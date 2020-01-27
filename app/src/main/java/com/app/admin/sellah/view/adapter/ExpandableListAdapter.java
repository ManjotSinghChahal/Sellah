package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.MainActivityController;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    public Context context;
    GetCategoriesModel model;
    List<String> expandableListTitle;
    HashMap<String, ArrayList<String>> expandableListDetail;
    MainActivityController controller;

    public ExpandableListAdapter(Context mainActivity, GetCategoriesModel expandableListTitle,/*,
                                 HashMap<HashMap<String, Integer>, ArrayList<HashMap<String, Integer>>> expandableListDetail*/MainActivityController controller) {
        this.context = mainActivity;
        this.model = expandableListTitle;
        this.expandableListTitle=new ArrayList<>();
        this.expandableListDetail=new HashMap<>();
        this.controller=controller;
        if(model!=null){
        for (int i = 0; i < model.getResult().size(); i++) {
            ArrayList<String> subCategories = new ArrayList<>();
            ArrayList<String> categoriesMain = new ArrayList<>();
            Log.e("categoryDataNav", model.getResult().get(i).getName());

//            subCategories.add("View All");
            for (int j = 0; j < model.getResult().get(i).getSubcategories().size(); j++) {
                Log.e("  SubcategoryDataNav", model.getResult().get(i).getSubcategories().get(j).getName());
                subCategories.add(model.getResult().get(i).getSubcategories().get(j).getName());
            }
            this.expandableListTitle.add(model.getResult().get(i).getName());
            expandableListDetail.put(model.getResult().get(i).getName(), subCategories);
        }
        }
//        this.expandableListDetail=expandableListDetail;
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandableListDetail.get(this.expandableListTitle.get(i)).get(i1);
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String exapandedListText = (String) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) view.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(exapandedListText);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i1==0){
                    controller.onNavOptionSelect(model.getResult().get(i).getCatId(),"",model.getResult().get(i).getSubcategories(),i);
                }else{
                    controller.onNavOptionSelect(model.getResult().get(i).getCatId(),model.getResult().get(i).getSubcategories().get(i1).getId(),model.getResult().get(i).getSubcategories(),i);

                }

            }
        });
        return view;
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListDetail.get(this.expandableListTitle.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.expandableListTitle.get(i);
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }


    @Override
    public long getGroupId(int i) {
        return i;
    }


    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String listTitle = (String) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }

        ImageView img = (ImageView) view.findViewById(R.id.expandable_icon);

        if (b) {
            img.setImageResource(R.drawable.rotate_image);
        } else {
            img.setImageResource(R.drawable.drop_down_ic);
        }
        TextView listTitleTextView = (TextView) view.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(listTitle);
        return view;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
