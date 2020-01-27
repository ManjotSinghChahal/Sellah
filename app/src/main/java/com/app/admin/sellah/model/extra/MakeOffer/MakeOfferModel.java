
package com.app.admin.sellah.model.extra.MakeOffer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeOfferModel implements Parcelable,Cloneable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;


    public MakeOfferModel(){

    }

    protected MakeOfferModel(Parcel in) {
        status = in.readString();
        message = in.readString();
        result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Creator<MakeOfferModel> CREATOR = new Creator<MakeOfferModel>() {
        @Override
        public MakeOfferModel createFromParcel(Parcel in) {
            return new MakeOfferModel(in);
        }

        @Override
        public MakeOfferModel[] newArray(int size) {
            return new MakeOfferModel[size];
        }
    };

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeParcelable(result, flags);
    }
}
