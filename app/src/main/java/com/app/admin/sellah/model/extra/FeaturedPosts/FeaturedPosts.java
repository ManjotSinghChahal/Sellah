package com.app.admin.sellah.model.extra.FeaturedPosts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeaturedPosts implements Parcelable,Cloneable
{
         @SerializedName("status")
         @Expose
         private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("product_count")
        @Expose
        private Integer productCount;
        @SerializedName("totalPages")
        @Expose
        private Integer totalPages;
        @SerializedName("result")
        @Expose
        private List<ResultFeatured> result = null;

    public FeaturedPosts(Parcel in) {
        status = in.readString();
        message = in.readString();
        if (in.readByte() == 0) {
            productCount = null;
        } else {
            productCount = in.readInt();
        }
        if (in.readByte() == 0) {
            totalPages = null;
        } else {
            totalPages = in.readInt();
        }
        result = in.createTypedArrayList(ResultFeatured.CREATOR);
    }

    public static final Creator<FeaturedPosts> CREATOR = new Creator<FeaturedPosts>() {
        @Override
        public FeaturedPosts createFromParcel(Parcel in) {
            return new FeaturedPosts(in);
        }

        @Override
        public FeaturedPosts[] newArray(int size) {
            return new FeaturedPosts[size];
        }
    };

    public FeaturedPosts() {

    }

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

        public Integer getProductCount() {
            return productCount;
        }

        public void setProductCount(Integer productCount) {
            this.productCount = productCount;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public List<ResultFeatured> getResult() {
            return result;
        }

        public void setResult(List<ResultFeatured> result) {
            this.result = result;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(message);
        if (productCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(productCount);
        }
        if (totalPages == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(totalPages);
        }
        parcel.writeTypedList(result);
    }
}





