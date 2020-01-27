
package com.app.admin.sellah.model.extra.PromotePackages;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromotePackageModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("packages_list")
    @Expose
    private List<PackagesList> packagesList = null;

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

    public List<PackagesList> getPackagesList() {
        return packagesList;
    }

    public void setPackagesList(List<PackagesList> packagesList) {
        this.packagesList = packagesList;
    }

}
