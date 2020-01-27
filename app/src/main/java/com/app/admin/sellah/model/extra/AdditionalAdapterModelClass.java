package com.app.admin.sellah.model.extra;

public class AdditionalAdapterModelClass {
    int product_image ;
    String product_name;
    String product_cost;

    public AdditionalAdapterModelClass(int product_image, String product_name, String product_cost) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_cost = product_cost;
    }

    public int getProduct_image() {
        return product_image;
    }

    public void setProduct_image(int product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }
}
