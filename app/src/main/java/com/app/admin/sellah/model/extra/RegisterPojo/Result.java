
package com.app.admin.sellah.model.extra.RegisterPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("verification_code")
    @Expose
    private Integer verificationCode;
    @SerializedName("code_delivery_status")
    @Expose
    private String codeDeliveryStatus;

    public String getIsProfileCompleted() {
        return isProfileCompleted;
    }

    public void setIsProfileCompleted(String isProfileCompleted) {
        this.isProfileCompleted = isProfileCompleted;
    }

    @SerializedName("is_profile_completed")
    @Expose
    private String isProfileCompleted;
    @SerializedName("id")
    @Expose
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getCodeDeliveryStatus() {
        return codeDeliveryStatus;
    }

    public void setCodeDeliveryStatus(String codeDeliveryStatus) {
        this.codeDeliveryStatus = codeDeliveryStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
