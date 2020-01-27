
package com.app.admin.sellah.model.extra.ProductDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("sell_internationally")
    @Expose
    private String sellInternationally;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("fixed_price")
    @Expose
    private String fixedPrice;
    @SerializedName("product_type")
    @Expose
    private String productType;

    @SerializedName("product_view_count")
    @Expose
    private String product_view_count ;

    public String getProduct_view_count() {
        return product_view_count;
    }

    public void setProduct_view_count(String product_view_count) {
        this.product_view_count = product_view_count;
    }

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("no_of_clicks")
    @Expose
    private String noOfClicks;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("promote_product")
    @Expose
    private String promoteProduct;
    @SerializedName("sold")
    @Expose
    private String sold;
    @SerializedName("is_live")
    @Expose
    private String isLive;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("subcategory_name")
    @Expose
    private String subcategoryName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("is_wishlist")
    @Expose
    private String isWishlist;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("is_liked")
    @Expose
    private String isLiked;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;
    @SerializedName("last_seen_time")
    @Expose
    private String lastSeenTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
    }

    @SerializedName("product_video")
    @Expose
    private String productVideo;

    public String getProductVideo_thumbnail() {
        return productVideo_thumbnail;
    }

    public void setProductVideo_thumbnail(String productVideo_thumbnail) {
        this.productVideo_thumbnail = productVideo_thumbnail;
    }

    @SerializedName("product_video_thumbnail")
    @Expose
    private String productVideo_thumbnail;

    @SerializedName("location")
    @Expose
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("promotes")
    @Expose
    private List<Promote> promotes = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getSellInternationally() {
        return sellInternationally;
    }

    public void setSellInternationally(String sellInternationally) {
        this.sellInternationally = sellInternationally;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(String fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoOfClicks() {
        return noOfClicks;
    }

    public void setNoOfClicks(String noOfClicks) {
        this.noOfClicks = noOfClicks;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPromoteProduct() {
        return promoteProduct;
    }

    public void setPromoteProduct(String promoteProduct) {
        this.promoteProduct = promoteProduct;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(String isWishlist) {
        this.isWishlist = isWishlist;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(String lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Promote> getPromotes() {
        return promotes;
    }

    public void setPromotes(List<Promote> promotes) {
        this.promotes = promotes;
    }

}
