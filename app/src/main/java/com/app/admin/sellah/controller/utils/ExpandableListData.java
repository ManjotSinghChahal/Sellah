package com.app.admin.sellah.controller.utils;

import android.app.Activity;
import android.util.Log;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.model.extra.Categories.Result;
import com.app.admin.sellah.model.extra.Categories.Subcategory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {

    public static HashMap<HashMap<String, Integer>, ArrayList<HashMap<String, Integer>>> expandableListDetail = new HashMap<>();
    public static HashMap<String, ArrayList<String>> expandableListDetail1 = new HashMap<>();
    //    public static HashMap<String, ArrayList<String> expandableListDetail=new HashMap<>();
    public static ArrayList<HashMap<String, Integer>> categoriesMain = new ArrayList<>();
    //    public static ArrayList<String> categoriesMain = new ArrayList<>();
    public static GetCategoriesModel mainCatgoryList;


    public static void setData(GetCategoriesModel categoriesModel) {


        if (categoriesModel != null) {
            Log.e("SetCatData_Api", "Done");
//            mainCatgoryList=new GetCategoriesModel();
//            mainCatgoryList.getResult().clear();
            mainCatgoryList = categoriesModel;
        } else {
            Log.e("SetCatData_Api", "unDone");
        }



       /* for (int i = 0; i < categoriesModel.getResult().size(); i++) {
            ArrayList<HashMap<String, Integer>> subCategories = new ArrayList<>();

            Log.e("categoryData", categoriesModel.getResult().get(i).getName());

            HashMap<String, Integer> subcatHash = new HashMap<>();
            HashMap<String, Integer> catHash = new HashMap<>();

            subcatHash.put("View All", 00);
//            subCategories.put("View all",00);
            for (int j = 0; j < categoriesModel.getResult().get(i).getSubcategories().size(); j++) {
                Log.e("  SubcategoryData", categoriesModel.getResult().get(i).getSubcategories().get(j).getName());
                subcatHash.put(categoriesModel.getResult().get(i).getSubcategories().get(j).getName(), categoriesModel.getResult().get(i).getSubcategories().get(j).getId());
                subCategories.add(subcatHash);
//                subCategories.add(categoriesModel.getResult().get(i).getSubcategories().get(j).getName());
            }
            catHash.put(categoriesModel.getResult().get(i).getName(), categoriesModel.getResult().get(i).getCatId());
            categoriesMain.add(catHash);
            expandableListDetail.put(catHash, subCategories);
        }*/

    }

    public static GetCategoriesModel getData() {
        return mainCatgoryList;
    }/*
    public static HashMap<HashMap<String, Integer>, ArrayList<HashMap<String, Integer>>> getData() {
        return expandableListDetail;
    }*/

    public static List<Subcategory> getSubcatgoriesList(String catId) {

        GetCategoriesModel model = ExpandableListData.getData();
        HashMap<String, List<Subcategory>> subCategoryList = new HashMap<>();
        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                subCategoryList.put(model.getResult().get(i).getCatId(), model.getResult().get(i).getSubcategories());
            }
            Log.e("CatId", "getSubcatgoriesList: " + catId);
            return subCategoryList.get(catId);
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public static String getCatId(String catName) {

        GetCategoriesModel model = ExpandableListData.getData();
        HashMap<String, List<Subcategory>> subCategoryList = new HashMap<>();
        String catId = "";
        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                if (model.getResult().get(i).getName().equalsIgnoreCase(catName)) {
                    catId = model.getResult().get(i).getCatId();
                    Log.e("CatId", "getCatId: " + catId);
                }
            }
            return catId;
        } catch (Exception e) {
            return catId;
        }

    }

    public static String getCatName(String catId) {

        GetCategoriesModel model = ExpandableListData.getData();

        String catName = "";
        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                if (model.getResult().get(i).getCatId().equalsIgnoreCase(catId)) {
                    catName = model.getResult().get(i).getName();
                    Log.e("CatId", "getCatId: " + catName);
                }
            }
            return catName;
        } catch (Exception e) {
            return catName;
        }

    }

    public static void setDataOnfaliur() {


        String defaultCategories = "{\n" +
                "    \"message\": \"Get Categories\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"name\": \"Women's Fashion\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/12122018054215_5c109fb73a8ea.png\",\n" +
                "            \"cat_id\": \"5bdadbad07229b3fca570513\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae87307229b3fc867ecda\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018051403_1543986843.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae88c07229b5abb3eabfd\",\n" +
                "                    \"name\": \"Tops\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018051824_1543987104.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae89b07229b5aba11796a\",\n" +
                "                    \"name\": \"Dresses\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018051848_1543987128.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8a107229b3fcb4d32f0\",\n" +
                "                    \"name\": \"Jeans & Pants\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018051934_1543987174.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8aa07229b3fca57051d\",\n" +
                "                    \"name\": \"Shorts & Skirts\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018051951_1543987191.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8b407229b3fcd5fa747\",\n" +
                "                    \"name\": \"Shoes\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052011_1543987211.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8bc07229b43ee05c7ec\",\n" +
                "                    \"name\": \"Bags\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052031_1543987231.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8c307229b43514afdde\",\n" +
                "                    \"name\": \"Accessories\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052045_1543987245.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae8cc07229b3fc867ecdb\",\n" +
                "                    \"name\": \"Lingerie & Sleep\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052100_1543987260.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Men's Fashion\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050337_1543986217.png\",\n" +
                "            \"cat_id\": \"5bdadcb207229b43514afdd6\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae28807229b5abb3eabf6\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052153_1543987313.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae29e07229b3fcb4d32e9\",\n" +
                "                    \"name\": \"Shirts\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052210_1543987330.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae2aa07229b3fca570516\",\n" +
                "                    \"name\": \"Casual Tops\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052232_1543987352.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae2b407229b43ee05c7e6\",\n" +
                "                    \"name\": \"Jeans\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052257_1543987377.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae2da07229b3fcd5fa742\",\n" +
                "                    \"name\": \"Pants\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052329_1543987409.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae2ee07229b3fc867ecd5\",\n" +
                "                    \"name\": \"Sweaters & Cardigans\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052345_1543987425.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae2fd07229b43522013a5\",\n" +
                "                    \"name\": \"Mens's Shoes\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052403_1543987443.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae30807229b5abb3eabf7\",\n" +
                "                    \"name\": \"Mens's Bags\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052422_1543987462.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae31807229b5ac0067435\",\n" +
                "                    \"name\": \"Mens's Accessories\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052441_1543987481.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Electronics\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050406_1543986246.png\",\n" +
                "            \"cat_id\": \"5bdadcbc07229b3fc867ecd4\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae37607229b43522013a6\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052517_1543987517.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae38007229b5abb3eabf8\",\n" +
                "                    \"name\": \"Smartphones\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052535_1543987535.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae38b07229b5ac0067436\",\n" +
                "                    \"name\": \"Televisions\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052553_1543987553.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae39607229b5aba117963\",\n" +
                "                    \"name\": \"Laptops & PCs\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052609_1543987569.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae39f07229b3fcb4d32eb\",\n" +
                "                    \"name\": \"Home Appliances\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052623_1543987583.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae3aa07229b3fca570517\",\n" +
                "                    \"name\": \"Gaming Consoles\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052641_1543987601.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae3b407229b43ee05c7e7\",\n" +
                "                    \"name\": \"Tablets\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052700_1543987620.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae3bc07229b43514afdd9\",\n" +
                "                    \"name\": \"Audio\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052717_1543987637.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae3d307229b3fc867ecd6\",\n" +
                "                    \"name\": \"Cameras\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052808_1543987688.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae3e207229b43522013a7\",\n" +
                "                    \"name\": \"All Other Tech\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052829_1543987709.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Health & Beauty\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050456_1543986296.png\",\n" +
                "            \"cat_id\": \"5bdadcc807229b5abb3eabf2\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4bc07229b5ac0067437\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052932_1543987772.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4ca07229b5aba117964\",\n" +
                "                    \"name\": \"Makeup\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052945_1543987785.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4d207229b43514afdda\",\n" +
                "                    \"name\": \"Hair Care\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018052959_1543987799.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4da07229b3fcd5fa743\",\n" +
                "                    \"name\": \"Skin Care\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053011_1543987811.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4e207229b3fc867ecd7\",\n" +
                "                    \"name\": \"Personal Care\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053026_1543987826.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae4e907229b43522013a8\",\n" +
                "                    \"name\": \"Fragrances\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053044_1543987844.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae50607229b5ac0067438\",\n" +
                "                    \"name\": \"Men's Care\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053100_1543987860.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae50f07229b5aba117965\",\n" +
                "                    \"name\": \"Beauty Tools\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053114_1543987874.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae51607229b3fcb4d32ec\",\n" +
                "                    \"name\": \"Health Supplements\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053126_1543987886.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae51e07229b3fca570518\",\n" +
                "                    \"name\": \"Medical Supplies\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053141_1543987901.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Kids\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050538_1543986338.png\",\n" +
                "            \"cat_id\": \"5bdadce407229b5ac0067433\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae53707229b43ee05c7e8\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053203_1543987923.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae53e07229b43514afddb\",\n" +
                "                    \"name\": \"Toys & Games\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053219_1543987939.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae54707229b3fcd5fa744\",\n" +
                "                    \"name\": \"Girl's Clothing\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053303_1543987983.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae55207229b3fc867ecd8\",\n" +
                "                    \"name\": \"Boy's Clothing\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053323_1543988003.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae55b07229b43522013a9\",\n" +
                "                    \"name\": \"Girl's Shoes\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053342_1543988022.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae56907229b5abb3eabf9\",\n" +
                "                    \"name\": \"Boy's Shoes\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053402_1543988042.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae57307229b5ac0067439\",\n" +
                "                    \"name\": \"Baby Walker\",\n" +
                "                    \"image\": \"\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae57b07229b5aba117966\",\n" +
                "                    \"name\": \"Baby & Toddler\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053510_1543988110.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae59307229b3fca570519\",\n" +
                "                    \"name\": \"Maternity\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053539_1543988139.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae59d07229b43ee05c7e9\",\n" +
                "                    \"name\": \"Outdoor Play & Sports\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053554_1543988154.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Home & Garden\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050556_1543986356.png\",\n" +
                "            \"cat_id\": \"5bdadcef07229b3fcb4d32e7\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5c807229b43522013aa\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053619_1543988179.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5cb07229b43522013ab\",\n" +
                "                    \"name\": \"Small Appliances\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053632_1543988192.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5d607229b5abb3eabfa\",\n" +
                "                    \"name\": \"Bedding\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053643_1543988203.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5e007229b5ac006743a\",\n" +
                "                    \"name\": \"Bath\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053652_1543988212.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5ea07229b5aba117967\",\n" +
                "                    \"name\": \"Kitchen & Dining\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053712_1543988232.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5f107229b3fcb4d32ed\",\n" +
                "                    \"name\": \"Furniture\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053725_1543988245.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae5fb07229b3fca57051a\",\n" +
                "                    \"name\": \"Decor\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053751_1543988271.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae60307229b43ee05c7ea\",\n" +
                "                    \"name\": \"Lighting\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053811_1543988291.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae60b07229b43514afddc\",\n" +
                "                    \"name\": \"Tools & DIY\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053825_1543988305.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae61207229b3fcd5fa745\",\n" +
                "                    \"name\": \"Garden & Pool\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053836_1543988316.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Cars\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050613_1543986373.png\",\n" +
                "            \"cat_id\": \"5bdadcf707229b3fca570514\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae76b07229b43522013ac\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053907_1543988347.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Property\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050626_1543986386.png\",\n" +
                "            \"cat_id\": \"5bdadd0007229b43ee05c7e3\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae78807229b5abb3eabfb\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053939_1543988379.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae79e07229b5ac006743b\",\n" +
                "                    \"name\": \"For Sale\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018053951_1543988391.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7a907229b5aba117968\",\n" +
                "                    \"name\": \"Rentals\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054003_1543988403.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7b107229b3fcb4d32ee\",\n" +
                "                    \"name\": \"Offices\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054014_1543988414.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7ba07229b3fca57051b\",\n" +
                "                    \"name\": \"Warehouse & Storage\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054025_1543988425.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Everything Else\",\n" +
                "            \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018050646_1543986406.png\",\n" +
                "            \"cat_id\": \"5bdadd0807229b43514afdd7\",\n" +
                "            \"subcategories\": [\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7cd07229b43ee05c7eb\",\n" +
                "                    \"name\": \"View All\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054057_1543988457.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7de07229b43514afddd\",\n" +
                "                    \"name\": \"Fresh Food\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054115_1543988475.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7e707229b3fcd5fa746\",\n" +
                "                    \"name\": \"Books & Media\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054128_1543988488.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7f107229b3fc867ecd9\",\n" +
                "                    \"name\": \"Jewellery\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054139_1543988499.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae7f707229b43522013ad\",\n" +
                "                    \"name\": \"Religious Items\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054156_1543988516.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae80007229b5abb3eabfc\",\n" +
                "                    \"name\": \"Art & Hobbies\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054206_1543988526.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae80807229b5ac006743c\",\n" +
                "                    \"name\": \"Business & Industrial\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054218_1543988538.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae81107229b5aba117969\",\n" +
                "                    \"name\": \"Jobs\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054229_1543988549.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae81907229b3fcb4d32ef\",\n" +
                "                    \"name\": \"Health & Beauty Services\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054242_1543988562.jpg\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"_id\": \"5bdae82107229b3fca57051c\",\n" +
                "                    \"name\": \"Repair & Admin Services\",\n" +
                "                    \"image\": \"https://s3.ap-southeast-1.amazonaws.com/sellahmedia/categoriesimages/05122018054337_1543988617.jpg\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        try {
            Gson gson = new Gson();
            GetCategoriesModel catModel = gson.fromJson(defaultCategories, GetCategoriesModel.class);
            mainCatgoryList = catModel;
        } catch (Exception e) {

        }

//        Log.e("CatGoryFailed", categoriesMain + "");
    }
}
