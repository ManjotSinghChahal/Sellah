package com.app.admin.sellah.model.extra.ChatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMessageModel {

    @SerializedName("msg_id")
    @Expose
    private String msgId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("sender_image")
    @Expose
    private String senderImage;
    @SerializedName("receiver_image")
    @Expose
    private String receiverImage;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_cost")
    @Expose
    private String productCost;

    @SerializedName("product_image")
    @Expose
    private String product_image;

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getMessage_read_status() {
        return message_read_status;
    }

    public void setMessage_read_status(String message_read_status) {
        this.message_read_status = message_read_status;
    }

    @SerializedName("message_read_status")
    @Expose
    private String message_read_status;

    @SerializedName("product_name")
    @Expose
    private String productName;

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public boolean isToday_boolean() {
        return today_boolean;
    }

    public void setToday_boolean(boolean today_boolean) {
        this.today_boolean = today_boolean;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose

    private String createdAt;
    private String today;
    private boolean today_boolean;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @SerializedName("offer_id")
    @Expose

    private String offerId;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @SerializedName("quantity")
    @Expose

    private String quantity;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }


}
