
package com.app.admin.sellah.model.extra.GetCardDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("card_holder_name")
    @Expose
    private String cardHolderName;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("exp_date")
    @Expose
    private String expDate;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}
