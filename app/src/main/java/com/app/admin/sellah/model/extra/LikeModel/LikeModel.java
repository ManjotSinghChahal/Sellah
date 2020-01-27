package com.app.admin.sellah.model.extra.LikeModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;

    public Integer getLikeCommentCount() {
        return likeCommentCount;
    }

    public void setLikeCommentCount(Integer likeCommentCount) {
        this.likeCommentCount = likeCommentCount;
    }

    @SerializedName("like_comment_count")
    @Expose
    private Integer likeCommentCount;

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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

}