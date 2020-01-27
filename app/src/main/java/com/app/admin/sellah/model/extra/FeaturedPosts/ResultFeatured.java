package com.app.admin.sellah.model.extra.FeaturedPosts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ResultFeatured implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("promote_product")
    @Expose
    private String promoteProduct;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("product_video")
    @Expose
    private String productVideo;

    protected ResultFeatured(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        promoteProduct = in.readString();
        price = in.readString();
        productImages = in.createTypedArrayList(ProductImage.CREATOR);
        productVideo = in.readString();
    }

    public static final Creator<ResultFeatured> CREATOR = new Creator<ResultFeatured>() {
        @Override
        public ResultFeatured createFromParcel(Parcel in) {
            return new ResultFeatured(in);
        }

        @Override
        public ResultFeatured[] newArray(int size) {
            return new ResultFeatured[size];
        }
    };

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

    public String getPromoteProduct() {
        return promoteProduct;
    }

    public void setPromoteProduct(String promoteProduct) {
        this.promoteProduct = promoteProduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(String productVideo) {
        this.productVideo = productVideo;
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
        parcel.writeString(promoteProduct);
        parcel.writeString(price);
        parcel.writeTypedList(productImages);
        parcel.writeString(productVideo);
    }
}
