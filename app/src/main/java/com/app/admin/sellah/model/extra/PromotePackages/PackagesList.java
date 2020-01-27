
package com.app.admin.sellah.model.extra.PromotePackages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackagesList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("clicks")
    @Expose
    private String clicks;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("validity")
    @Expose
    private String validity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClicks() {
        return clicks;
    }

    public void setClicks(String clicks) {
        this.clicks = clicks;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

}
