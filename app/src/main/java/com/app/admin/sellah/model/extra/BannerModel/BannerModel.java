
package com.app.admin.sellah.model.extra.BannerModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BannerModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("homebanners")
    @Expose
    private List<Homebanner> homebanners = null;
    @SerializedName("categorybanners")
    @Expose
    private List<Categorybanner> categorybanners = null;
    @SerializedName("subcategorybanners")
    @Expose
    private List<Subcategorybanner> subcategorybanners = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Homebanner> getHomebanners() {
        return homebanners;
    }

    public void setHomebanners(List<Homebanner> homebanners) {
        this.homebanners = homebanners;
    }

    public List<Categorybanner> getCategorybanners() {
        return categorybanners;
    }

    public void setCategorybanners(List<Categorybanner> categorybanners) {
        this.categorybanners = categorybanners;
    }

    public List<Subcategorybanner> getSubcategorybanners() {
        return subcategorybanners;
    }

    public void setSubcategorybanners(List<Subcategorybanner> subcategorybanners) {
        this.subcategorybanners = subcategorybanners;
    }

}


