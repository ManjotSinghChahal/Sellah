
package com.app.admin.sellah.model.extra.LiveVideoDesc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("video_title")
    @Expose
    private String videoTitle;
    @SerializedName("video_desc")
    @Expose
    private String videDesc;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("seller_image")
    @Expose
    private String sellerImage;

    @SerializedName("created_at")
    @Expose
    private String createdAt;


    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideDesc() {
        return videDesc;
    }

    public void setVideDesc(String videDesc) {
        this.videDesc = videDesc;
    }

}
