package com.app.admin.sellah.controller.utils;

import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;

public interface SubCategoryController {

    public void onSubCategotyClick(String subCatId);

    public void onFilterResponse(GetProductList productList);
}
