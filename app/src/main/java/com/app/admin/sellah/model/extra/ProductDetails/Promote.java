package com.app.admin.sellah.model.extra.ProductDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Promote {
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

    @SerializedName("days_left")
    @Expose
    private String days_left;

    public String getDays_left() {
        return days_left;
    }

    public void setDays_left(String days_left) {
        this.days_left = days_left;
    }

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
