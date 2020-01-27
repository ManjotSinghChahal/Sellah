package com.app.admin.sellah.model.extra;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddProductModel {

    RequestBody product_id;
    RequestBody user_id;
    RequestBody name;
    RequestBody cat_id;
    RequestBody sub_cat_id;
    RequestBody payment_mode;
    RequestBody delivery;
    RequestBody sell_internationally;
    RequestBody price;
    RequestBody location;

    public RequestBody getImage_id() {
        return image_id;
    }

    public void setImage_id(RequestBody image_id) {
        this.image_id = image_id;
    }

    RequestBody image_id;



    public MultipartBody.Part getProductVideo() {
        return productVideo;
    }
    public void setProductVideo(MultipartBody.Part productVideo) {
        this.productVideo = productVideo;
    }
    MultipartBody.Part productVideo ;





    public RequestBody getLocation() {
        return location;
    }

    public void setLocation(RequestBody locatoin) {
        this.location = locatoin;
    }


    public RequestBody getPromoteId() {
        return promoteId;
    }

    public void setPromoteId(RequestBody promoteId) {
        this.promoteId = promoteId;
    }

    RequestBody promoteId;

    public RequestBody getPackageId() {
        return packageId;
    }

    public void setPackageId(RequestBody packageId) {
        this.packageId = packageId;
    }

    RequestBody packageId;

    public RequestBody getProduct_id() {
        return product_id;
    }

    public void setProduct_id(RequestBody product_id) {
        this.product_id = product_id;
    }

    public RequestBody getUser_id() {
        return user_id;
    }

    public void setUser_id(RequestBody user_id) {
        this.user_id = user_id;
    }

    public RequestBody getName() {
        return name;
    }

    public void setName(RequestBody name) {
        this.name = name;
    }

    public RequestBody getCat_id() {
        return cat_id;
    }

    public void setCat_id(RequestBody cat_id) {
        this.cat_id = cat_id;
    }

    public RequestBody getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(RequestBody sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public RequestBody getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(RequestBody payment_mode) {
        this.payment_mode = payment_mode;
    }

    public RequestBody getDelivery() {
        return delivery;
    }

    public void setDelivery(RequestBody delivery) {
        this.delivery = delivery;
    }

    public RequestBody getSell_internationally() {
        return sell_internationally;
    }

    public void setSell_internationally(RequestBody sell_internationally) {
        this.sell_internationally = sell_internationally;
    }

    public RequestBody getPrice() {
        return price;
    }

    public void setPrice(RequestBody price) {
        this.price = price;
    }

    public RequestBody getFixed_price() {
        return fixed_price;
    }

    public void setFixed_price(RequestBody fixed_price) {
        this.fixed_price = fixed_price;
    }

    public RequestBody getProduct_type() {
        return product_type;
    }

    public void setProduct_type(RequestBody product_type) {
        this.product_type = product_type;
    }

    public RequestBody getQuantity() {
        return quantity;
    }

    public void setQuantity(RequestBody quantity) {
        this.quantity = quantity;
    }

    public RequestBody getDescription() {
        return description;
    }

    public void setDescription(RequestBody description) {
        this.description = description;
    }

    public RequestBody getPromote_product() {
        return promote_product;
    }

    public void setPromote_product(RequestBody promote_product) {
        this.promote_product = promote_product;
    }

    public RequestBody getNo_of_clicks() {
        return no_of_clicks;
    }

    public void setNo_of_clicks(RequestBody no_of_clicks) {
        this.no_of_clicks = no_of_clicks;
    }

    public RequestBody getBudget() {
        return budget;
    }

    public void setBudget(RequestBody budget) {
        this.budget = budget;
    }

    public RequestBody getTags() {
        return tags;
    }

    public void setTags(RequestBody tags) {
        this.tags = tags;
    }

    public MultipartBody.Part getImage1() {
        return image1;
    }

    public void setImage1(MultipartBody.Part image1) {
        this.image1 = image1;
    }

    public MultipartBody.Part getImage2() {
        return image2;
    }

    public void setImage2(MultipartBody.Part image2) {
        this.image2 = image2;
    }

    public MultipartBody.Part getImage3() {
        return image3;
    }

    public void setImage3(MultipartBody.Part image3) {
        this.image3 = image3;
    }

    public MultipartBody.Part getImage4() {
        return image4;
    }

    public void setImage4(MultipartBody.Part image4) {
        this.image4 = image4;
    }

    public MultipartBody.Part getImage5() {
        return image5;
    }

    public void setImage5(MultipartBody.Part image5) {
        this.image5 = image5;
    }

    public MultipartBody.Part getImage6() {
        return image6;
    }

    public void setImage6(MultipartBody.Part image6) {
        this.image6 = image6;
    }

    public MultipartBody.Part getImage7() {
        return image7;
    }

    public void setImage7(MultipartBody.Part image7) {
        this.image7 = image7;
    }

    public MultipartBody.Part getImage8() {
        return image8;
    }

    public void setImage8(MultipartBody.Part image8) {
        this.image8 = image8;
    }

    RequestBody fixed_price;
    RequestBody product_type;
    RequestBody quantity;
    RequestBody description;
    RequestBody promote_product;
    RequestBody no_of_clicks;
    RequestBody budget;
    RequestBody tags;
    MultipartBody.Part image1;
    MultipartBody.Part image2;
    MultipartBody.Part image3;
    MultipartBody.Part image4;
    MultipartBody.Part image5;
    MultipartBody.Part image6;

    public MultipartBody.Part getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(MultipartBody.Part thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    MultipartBody.Part image7;
    MultipartBody.Part image8;
    MultipartBody.Part thumbnail_image;


}
