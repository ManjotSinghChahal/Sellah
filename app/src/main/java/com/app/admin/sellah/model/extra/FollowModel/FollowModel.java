
package com.app.admin.sellah.model.extra.FollowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("following")
    @Expose
    private Integer following;
    @SerializedName("followers")
    @Expose
    private Integer followers;

    public String getFollowingStatus() {
        return followingStatus;
    }

    public void setFollowingStatus(String followingStatus) {
        this.followingStatus = followingStatus;
    }

    @SerializedName("following_status")
    @Expose
    private String followingStatus;

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

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

}
