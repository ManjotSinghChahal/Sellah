
package com.app.admin.sellah.model.extra.wishlistmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
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

    public String getWishlistTime() {
        return wishlistTime;
    }

    public void setWishlistTime(String wishlistTime) {
        this.wishlistTime = wishlistTime;
    }

    @SerializedName("wishlist_time")
    @Expose
    private String wishlistTime;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
