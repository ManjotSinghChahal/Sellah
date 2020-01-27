
package com.app.admin.sellah.model.extra.LoginPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;

    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

    @SerializedName("stripe_id")
    @Expose
    private String stripe_id;

    @SerializedName("stripe_verified")
    @Expose
    private String stripe_verified;

    public String getStripe_verified() {
        return stripe_verified;
    }

    public void setStripe_verified(String stripe_verified) {
        this.stripe_verified = stripe_verified;
    }

    public String getIsProfileCompleted() {
        return isProfileCompleted;
    }

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public void setIsProfileCompleted(String isProfileCompleted) {
        this.isProfileCompleted = isProfileCompleted;
    }

    @SerializedName("is_profile_completed")
    @Expose
    private String isProfileCompleted;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

}
