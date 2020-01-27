
package com.app.admin.sellah.model.extra.MakeOffer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable,Cloneable {

    public Result(){

    }
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("product_id")
    @Expose
    private String productId;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @SerializedName("quantity")
    @Expose
    private String quantity;

    protected Result(Parcel in) {
        message = in.readString();
        userId = in.readString();
        username = in.readString();
        productId = in.readString();
        productName = in.readString();
        sellerId = in.readString();
        priceCost = in.readString();
        userimage = in.readString();
        notiType = in.readString();
        datetime = in.readString();
        offerId = in.readString();
        status = in.readString();
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("price_cost")
    @Expose
    private String priceCost;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("noti_type")
    @Expose
    private String notiType;
    @SerializedName("datetime")
    @Expose
    private String datetime;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @SerializedName("offer_id")
    @Expose
    private String offerId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(String priceCost) {
        this.priceCost = priceCost;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(sellerId);
        dest.writeString(priceCost);
        dest.writeString(userimage);
        dest.writeString(notiType);
        dest.writeString(datetime);
        dest.writeString(offerId);
        dest.writeString(status);
    }
}
