package com.app.admin.sellah.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class AddProductDatabase  {
  public static int category;
  public static int sub_category;
  public static int type;
  public static int condition;
  public static int currency;
  public static int feature;
  public static List<String> imageListG = new ArrayList<>();
  public static ArrayList<HashMap<String,String>> imageListGStatus = new ArrayList<>();
  public static ArrayList<String> tagListG = new ArrayList<>();;
  public static String name="",price="",quantity="",tags="",description="",payment_method="",modes_of_delivery="",catid="",subcatid="",videoThumbnail="",videoUrl="";
  public static String productid="",promoteproduct="" , thumbnail;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSub_category() {
        return sub_category;
    }

    public void setSub_category(int sub_category) {
        this.sub_category = sub_category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
