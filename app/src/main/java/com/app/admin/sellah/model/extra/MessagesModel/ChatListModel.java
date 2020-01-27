package com.app.admin.sellah.model.extra.MessagesModel;

import com.app.admin.sellah.model.extra.ChatModel.ChatMessageModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;

    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
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

    public String getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    @SerializedName("is_reviewed")
    @Expose
    private String is_reviewed;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChatMessageModel> getRecord() {
        return record;
    }

    public void setRecord(List<ChatMessageModel> record) {
        this.record = record;
    }

    @SerializedName("record")
    @Expose
    private List<ChatMessageModel> record = null;
}
