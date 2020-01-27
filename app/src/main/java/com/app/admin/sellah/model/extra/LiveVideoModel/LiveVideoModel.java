
package com.app.admin.sellah.model.extra.LiveVideoModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveVideoModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    @SerializedName("totalPages")
    @Expose
    private String totalPages;
    @SerializedName("video_list")

    @Expose
    private List<VideoList> list = null;

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

    public List<VideoList> getList() {
        return list;
    }

    public void setList(List<VideoList> list) {
        this.list = list;
    }

}
