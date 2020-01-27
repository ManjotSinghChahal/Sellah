
package com.app.admin.sellah.model.extra.getProductsModel;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProductList  implements Parcelable, Cloneable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("promote_id")
    @Expose
    private String promote_id;

    public String getPromote_id() {
        return promote_id;
    }

    public void setPromote_id(String promote_id) {
        this.promote_id = promote_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    @SerializedName("package_id")
    @Expose
    private String package_id;

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    @SerializedName("totalPages")
    @Expose
    private String totalPages;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public GetProductList() {
    }

    protected GetProductList(Parcel in) {
        status = in.readString();
        message = in.readString();
        result = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Creator<GetProductList> CREATOR = new Creator<GetProductList>() {
        @Override
        public GetProductList createFromParcel(Parcel in) {
            return new GetProductList(in);
        }

        @Override
        public GetProductList[] newArray(int size) {
            return new GetProductList[size];
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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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
        dest.writeTypedList(result);
    }
}
