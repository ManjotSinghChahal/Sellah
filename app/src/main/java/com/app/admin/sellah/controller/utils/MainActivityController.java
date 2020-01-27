package com.app.admin.sellah.controller.utils;

import com.app.admin.sellah.model.extra.Categories.Subcategory;

import java.util.List;

public interface MainActivityController {

    public void onNavOptionSelect(String cat_id, String sub_cat_id, List<Subcategory> sub_cat_List, int i);
}
