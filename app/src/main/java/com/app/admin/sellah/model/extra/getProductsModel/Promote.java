package com.app.admin.sellah.model.extra.getProductsModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Promote implements Parcelable,Cloneable {
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

    protected Promote(Parcel in) {
        id = in.readString();
        clicks = in.readString();
        amount = in.readString();
        validity = in.readString();
        days_left = in.readString();
    }

    public static final Creator<Promote> CREATOR = new Creator<Promote>() {
        @Override
        public Promote createFromParcel(Parcel in) {
            return new Promote(in);
        }

        @Override
        public Promote[] newArray(int size) {
            return new Promote[size];
        }
    };

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

    public String  getClicks() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(clicks);
        parcel.writeString(amount);
        parcel.writeString(validity);
        parcel.writeString(days_left);
    }
}
