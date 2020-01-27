package com.app.admin.sellah.model.extra;

public class SaleModelClass {

    int image;

    String carText,costText;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCarText() {
        return carText;
    }

    public void setCarText(String carText) {
        this.carText = carText;
    }

    public String getCostText() {
        return costText;
    }

    public void setCostText(String costText) {
        this.costText = costText;
    }


    public SaleModelClass(int image, String carText, String costText) {
        this.image = image;
        this.carText = carText;
        this.costText = costText;
    }


}
