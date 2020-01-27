
package com.app.admin.sellah.model.extra.ChatHeadermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("friendId")
    @Expose
    private String friendId;

    @SerializedName("friendName")
    @Expose
    private String friendName;

    @SerializedName("friendImage")
    @Expose
    private String friendImage;

    @SerializedName("IsRead")
    @Expose
    private String IsRead;

    @SerializedName("isBlocked")
    @Expose
    private String isBlocked;

    @SerializedName("room_name")
    @Expose
    private String roomName;

    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("last_msg_time")
    @Expose
    private String lastMsgTime;

    @SerializedName("online_status")
    @Expose
    private String onlineStatus;

    @SerializedName("blockedBy")
    @Expose
    private String blockedBy;

    @SerializedName("last_seen_time")
    @Expose
    private String lastSeenTime;

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
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


    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
    }

}
