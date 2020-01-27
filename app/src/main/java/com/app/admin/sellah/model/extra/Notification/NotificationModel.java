package com.app.admin.sellah.model.extra.Notification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel implements Parcelable,Cloneable {

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("offer_id")
    @Expose
    String offerId;
    @SerializedName("user_id")
    @Expose
    String userId;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("bidder_id")
    @Expose
    String bidderId;
    @SerializedName("price_cost")
    @Expose
    String priceCost;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @SerializedName("msg_type")
    @Expose
    String msgType;
    @SerializedName("userimage")
    @Expose
    String userimage;
    @SerializedName("msg_id")
    @Expose
    String msgId;
    @SerializedName("noti_type")
    @Expose
    String notiType;

    protected NotificationModel(Parcel in) {
        message = in.readString();
        offerId = in.readString();
        userId = in.readString();
        username = in.readString();
        productId = in.readString();
        bidderId = in.readString();
        priceCost = in.readString();
        userimage = in.readString();
        msgId = in.readString();
        notiType = in.readString();
        otherUserId = in.readString();
        datetime = in.readString();
        status = in.readString();
    }

    public NotificationModel(){

    }
    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    @SerializedName("other_user_id'")
    @Expose
    String otherUserId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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

    @SerializedName("datetime'")
    @Expose
    String datetime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    String status;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(offerId);
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(productId);
        dest.writeString(bidderId);
        dest.writeString(priceCost);
        dest.writeString(userimage);
        dest.writeString(msgId);
        dest.writeString(notiType);
        dest.writeString(otherUserId);
        dest.writeString(datetime);
        dest.writeString(status);
    }
}
