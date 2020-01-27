
package com.app.admin.sellah.model.extra.getProductsModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.app.admin.sellah.model.extra.getProductsModel.Promote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable, Cloneable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
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
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
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
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;

    @SerializedName("product_video")
    @Expose
    private String productVideo;

    @SerializedName("location")
    @Expose
    private String location;



    @SerializedName("product_video_thumbnail")
    @Expose
    private String productVideoThumbnail;








    public Result() {

    }


    protected Result(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        catId = in.readString();
        subCatId = in.readString();
        paymentMode = in.readString();
        delivery = in.readString();
        sellInternationally = in.readString();
        price = in.readString();
        fixedPrice = in.readString();
        productType = in.readString();
        quantity = in.readString();
        description = in.readString();
        noOfClicks = in.readString();
        budget = in.readString();
        promoteProduct = in.readString();
        isLive = in.readString();
        isActive = in.readString();
        isDeleted = in.readString();
        updatedAt = in.readString();
        createdAt = in.readString();
        categoryName = in.readString();
        subcategoryName = in.readString();
        username = in.readString();
        image = in.readString();
        likeCount = in.readString();
        onlineStatus = in.readString();
        productVideo = in.readString();
        location = in.readString();
        productVideoThumbnail = in.readString();
        lastSeenTime = in.readString();
        wishlistTime = in.readString();
        isLiked = in.readString();
        isWishlist = in.readString();
        tags = in.createTypedArrayList(Tag.CREATOR);
        productImages = in.createTypedArrayList(ProductImage.CREATOR);
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
    }





    @SerializedName("promotes")
    @Expose
    private List<Promote> promotes = null;

    public List<Promote> getPromotes() throws NullPointerException {
        return promotes;
    }

    public void setPromotes(List<Promote> promotes) {
        this.promotes = promotes;
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

    @SerializedName("last_seen_time")
    @Expose
    private String lastSeenTime;

    public String getWishlistTime() {
        return wishlistTime;
    }

    public void setWishlistTime(String wishlistTime) {
        this.wishlistTime = wishlistTime;
    }

    @SerializedName("wishlist_time")
    @Expose
    private String wishlistTime;

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    @SerializedName("is_liked")
    @Expose
    private String isLiked;

    public String getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(String isWishlist) {
        this.isWishlist = isWishlist;
    }

    @SerializedName("is_wishlist")
    @Expose
    private String isWishlist;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getProductVideoThumbnail() {
        return productVideoThumbnail;
    }

    public void setProductVideoThumbnail(String productVideoThumbnail) {
        this.productVideoThumbnail = productVideoThumbnail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeString(catId);
        parcel.writeString(subCatId);
        parcel.writeString(paymentMode);
        parcel.writeString(delivery);
        parcel.writeString(sellInternationally);
        parcel.writeString(price);
        parcel.writeString(fixedPrice);
        parcel.writeString(productType);
        parcel.writeString(quantity);
        parcel.writeString(description);
        parcel.writeString(noOfClicks);
        parcel.writeString(budget);
        parcel.writeString(promoteProduct);
        parcel.writeString(isLive);
        parcel.writeString(isActive);
        parcel.writeString(isDeleted);
        parcel.writeString(updatedAt);
        parcel.writeString(createdAt);
        parcel.writeString(categoryName);
        parcel.writeString(subcategoryName);
        parcel.writeString(username);
        parcel.writeString(image);
        parcel.writeString(likeCount);
        parcel.writeString(onlineStatus);
        parcel.writeString(productVideo);
        parcel.writeString(location);
        parcel.writeString(productVideoThumbnail);
        parcel.writeString(lastSeenTime);
        parcel.writeString(wishlistTime);
        parcel.writeString(isLiked);
        parcel.writeString(isWishlist);
        parcel.writeTypedList(tags);
        parcel.writeTypedList(productImages);
    }
}
