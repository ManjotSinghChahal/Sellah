package com.app.admin.sellah.model.extra;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileModel {

    RequestBody user_id;
    RequestBody username;
    RequestBody description;
    RequestBody country_code;
    RequestBody phone_number;
    RequestBody about;
    RequestBody shipping_policy;
    RequestBody return_policy;
    RequestBody address_name;
    RequestBody address_1;
    RequestBody address_2;
    RequestBody postal_code;
    RequestBody state;

    public RequestBody getEdit_mode() {
        return edit_mode;
    }

    public void setEdit_mode(RequestBody edit_mode) {
        this.edit_mode = edit_mode;
    }

    RequestBody edit_mode;
    RequestBody address_city;
    MultipartBody.Part image;

    public RequestBody getUser_id() {
        return user_id;
    }

    public void setUser_id(RequestBody user_id) {
        this.user_id = user_id;
    }

    public RequestBody getUsername() {
        return username;
    }

    public void setUsername(RequestBody username) {
        this.username = username;
    }

    public RequestBody getDescription() {
        return description;
    }

    public void setDescription(RequestBody description) {
        this.description = description;
    }

    public RequestBody getCountry_code() {
        return country_code;
    }

    public void setCountry_code(RequestBody country_code) {
        this.country_code = country_code;
    }

    public RequestBody getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(RequestBody phone_number) {
        this.phone_number = phone_number;
    }

    public RequestBody getAbout() {
        return about;
    }

    public void setAbout(RequestBody about) {
        this.about = about;
    }

    public RequestBody getShipping_policy() {
        return shipping_policy;
    }

    public void setShipping_policy(RequestBody shipping_policy) {
        this.shipping_policy = shipping_policy;
    }

    public RequestBody getReturn_policy() {
        return return_policy;
    }

    public void setReturn_policy(RequestBody return_policy) {
        this.return_policy = return_policy;
    }

    public RequestBody getAddress_name() {
        return address_name;
    }

    public void setAddress_name(RequestBody address_name) {
        this.address_name = address_name;
    }

    public RequestBody getAddress_1() {
        return address_1;
    }

    public void setAddress_1(RequestBody address_1) {
        this.address_1 = address_1;
    }

    public RequestBody getAddress_2() {
        return address_2;
    }

    public void setAddress_2(RequestBody address_2) {
        this.address_2 = address_2;
    }

    public RequestBody getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(RequestBody postal_code) {
        this.postal_code = postal_code;
    }

    public RequestBody getState() {
        return state;
    }

    public void setState(RequestBody state) {
        this.state = state;
    }

    public RequestBody getAddress_city() {
        return address_city;
    }

    public void setAddress_city(RequestBody address_city) {
        this.address_city = address_city;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }


}
