
package com.app.admin.sellah.model.extra.NotificationList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MsgArrPost {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("bidder_id")
    @Expose
    private String bidderId;
    @SerializedName("price_cost")
    @Expose
    private String priceCost;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("msg_id")
    @Expose
    private String msgId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("noti_type")
    @Expose
    private String notiType;
    @SerializedName("datetime")
    @Expose
    private String datetime;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
