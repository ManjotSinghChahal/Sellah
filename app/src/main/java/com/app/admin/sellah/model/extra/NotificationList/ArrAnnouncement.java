
package com.app.admin.sellah.model.extra.NotificationList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArrAnnouncement {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("msg_arr")
    @Expose
    private MsgArrAnnouncement msgArr;
    @SerializedName("noti_type")
    @Expose
    private String notiType;

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    @SerializedName("noti_id")
    @Expose
    private String notiId;
    @SerializedName("read_status")
    @Expose
    private String readStatus;

    @SerializedName("product_id")
    @Expose
    private String productId;
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MsgArrAnnouncement getMsgArr() {
        return msgArr;
    }

    public void setMsgArr(MsgArrAnnouncement msgArr) {
        this.msgArr = msgArr;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

}
