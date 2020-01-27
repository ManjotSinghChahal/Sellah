package com.app.admin.sellah.controller.WebServices;

import com.app.admin.sellah.model.extra.BannerModel.BannerModel;
import com.app.admin.sellah.model.extra.BlockedUserModel.BlockListModel;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.model.extra.DeleteChatModel.DeleteChat;
import com.app.admin.sellah.model.extra.FeaturedPosts.FeaturedPosts;
import com.app.admin.sellah.model.extra.FollowModel.FollowModel;
import com.app.admin.sellah.model.extra.FolowModelNew.FollowModelNew;
import com.app.admin.sellah.model.extra.GetCardDetailModel.GetCardDetailModel;
import com.app.admin.sellah.model.extra.LikeModel.LikeModel;
import com.app.admin.sellah.model.extra.LiveVideoDesc.LiveVideoDescModel;
import com.app.admin.sellah.model.extra.LiveVideoModel.LiveVideoModel;
import com.app.admin.sellah.model.extra.LoginPojo.LoginResult;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.MessagesModel.ChatListModel;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.model.extra.PinCommentModel.PinCommentModel;
import com.app.admin.sellah.model.extra.ProductDetails.ProductDetailModel;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.model.extra.PromotePackages.PromotePackageModel;
import com.app.admin.sellah.model.extra.RegisterPojo.RegisterResult;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.model.extra.SendOffer.SendOfferModel;
import com.app.admin.sellah.model.extra.TestimonialModel.TestimonialModel;
import com.app.admin.sellah.model.extra.UploadChatImage.UploadChatImageModel;
import com.app.admin.sellah.model.extra.commentModel.CommentModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.wishllist_model.Wishlist;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebService {

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("register")
    Call<RegisterResult> registrationApi(@Field("username") String username,@Field("email") String email, @Field("password") String password
            , @Field("c_password") String confirm_password, @Field("phone_number") String phone_number
            , @Field("country_code") String country_code, @Field("city") String city,@Field("country") String country);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("login")
    Call<LoginResult> loginApi(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("verify_code")
    Call<Common> varifyOTPApi(@Field("user_id") String userId, @Field("verification_code") int verification_code, @Field("phone_type") String phone_type);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("restore_account")
    Call<Common> restore_account(@Field("user_id") String userId, @Field("verification_code") int verification_code);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("resend_code")
    Call<ResendCode> reSendOTPApi(@Field("user_id") String userId, @Field("phone_type") String phone_type);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("send_otp")
    Call<ResendCode> sendOtpChangePass(@Field("user_id") String userId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("device_update")
    Call<Common> deviceTockenUpdateapi(@Field("user_id") String userId, @Field("device_token") String device_token, @Field("device_type") String device_type, @Field("timezone") String timezone);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("change_password")
    Call<Common> changePasswordApi(@Field("user_id") String userId, @Field("old_password") String old_password, @Field("new_password") String new_password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_category")
    Call<GetCategoriesModel> getCategoryApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("featured_products")
    Call<FeaturedPosts> getFeaturedPostsApi(@Field("page_no") String page_no);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("search_product")
    Call<GetProductList> searchProductApi(@Field("search_term") String user_id, @Field("search_by") String tags);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("delete_chat")
    Call<DeleteChat> deleteChatApi(@Field("user_id") String user_id, @Field("room_id") String room_id);


    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_product")
    Call<GetProductList> addProductApi(@Part("user_id") RequestBody user_id, @Part("name") RequestBody productName, @Part("cat_id") RequestBody categoryId
            , @Part("sub_cat_id") RequestBody subCategoryId, @Part("payment_mode") RequestBody paymentMode, @Part("delivery") RequestBody delivery
            , @Part("sell_internationally") RequestBody sellInternationally, @Part("price") RequestBody price, @Part("fixed_price") RequestBody fixedPrice
            , @Part("product_type") RequestBody productType, @Part("quantity") RequestBody quantity, @Part("description") RequestBody description
            , @Part("promote_product") RequestBody promoteProduct, @Part("no_of_clicks") RequestBody noOfClicks, @Part("budget") RequestBody budget
            , @Part("tags") RequestBody tags, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3
            , @Part MultipartBody.Part image4, @Part MultipartBody.Part image5, @Part("package_id") RequestBody packageId
            , @Part MultipartBody.Part productVideo, @Part MultipartBody.Part productvideo_thimbnail,@Part("location") RequestBody location);



    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("edit_product")
    Call<JsonObject> editProductApi(@Part("product_id") RequestBody productId, @Part("user_id") RequestBody user_id, @Part("name") RequestBody productName
            , @Part("cat_id") RequestBody categoryId
            , @Part("sub_cat_id") RequestBody subCategoryId, @Part("payment_mode") RequestBody paymentMode, @Part("delivery") RequestBody delivery
            , @Part("sell_internationally") RequestBody sellInternationally, @Part("price") RequestBody price, @Part("fixed_price") RequestBody fixedPrice
            , @Part("product_type") RequestBody productType, @Part("quantity") RequestBody quantity, @Part("description") RequestBody description
            , @Part("promote_product") RequestBody promoteProduct, @Part("no_of_clicks") RequestBody noOfClicks, @Part("budget") RequestBody budget
            , @Part("tags") RequestBody tags, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3
            , @Part MultipartBody.Part image4, @Part MultipartBody.Part image5, @Part MultipartBody.Part image6, @Part MultipartBody.Part image7
            , @Part MultipartBody.Part image8,@Part("location") RequestBody location,@Part("image_ids") RequestBody image_ids
            , @Part MultipartBody.Part productVideo, @Part MultipartBody.Part productvideo_thimbnail);


    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("edit_product")
    Call<JsonObject> editProductApi1(@Part("product_id") RequestBody productId, @Part("user_id") RequestBody user_id, @Part("name") RequestBody productName
            , @Part("cat_id") RequestBody categoryId
            , @Part("sub_cat_id") RequestBody subCategoryId, @Part("payment_mode") RequestBody paymentMode, @Part("delivery") RequestBody delivery
            , @Part("sell_internationally") RequestBody sellInternationally, @Part("price") RequestBody price, @Part("fixed_price") RequestBody fixedPrice
            , @Part("product_type") RequestBody productType, @Part("quantity") RequestBody quantity, @Part("description") RequestBody description
            , @Part("promote_product") RequestBody promoteProduct, @Part("no_of_clicks") RequestBody noOfClicks, @Part("budget") RequestBody budget
            , @Part("tags") RequestBody tags, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3
            , @Part MultipartBody.Part image4, @Part MultipartBody.Part image5, @Part MultipartBody.Part image6, @Part MultipartBody.Part image7
            , @Part MultipartBody.Part image8,@Part("location") RequestBody location,@Part("image_ids") RequestBody image_ids
 );

    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("upload_chatimage")
    Call<UploadChatImageModel> uploadChatimageApi(@Part MultipartBody.Part image);

    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("edit_profile")
    Call<Common> editProfileApi(@Part("user_id") RequestBody user_id, @Part("username") RequestBody username, @Part("description") RequestBody description
            , @Part("country_code") RequestBody country_code, @Part("phone_number") RequestBody phone_number, @Part("about") RequestBody about
            , @Part("shipping_policy") RequestBody shipping_policy, @Part("return_policy") RequestBody return_policy, @Part("address_name") RequestBody address_name
            , @Part("address_1") RequestBody address_1, @Part("address_2") RequestBody address_2, @Part("postal_code") RequestBody postal_code
            , @Part("state") RequestBody state, @Part("address_city") RequestBody address_city, @Part MultipartBody.Part image, @Part("edit_mode") RequestBody edit_mode);

    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_comment")
    Call<Common> addImageCommentApi(@Part("user_id") RequestBody user_id, @Part("product_id") RequestBody product_id, @Part("comment") RequestBody comment, @Part("comment_type") RequestBody comment_type, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_profile")
    Call<ProfileModel> getProfileApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_profile")
    Call<JsonObject> getProfileApi1(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("create_stripe_account")
    Call<JsonObject> stripeadd(@Field("user_id") String user_id,@Field("first_name") String firstname,@Field("last_name") String lastname,@Field("dob") String dob,@Field("personal_id_number") String dpersonal_id_numberob,@Field("address_line") String address_line,@Field("country") String country,@Field("state") String state,@Field("city") String city,@Field("postal_code") String postal_code,@Field("currency") String currency,@Field("account_number") String account_number,@Field("routing_number") String routing_number,@Field("ip_address") String ip_address,@Field("country_of_bank") String country_of_bank);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("set_default_card")
    Call<JsonObject> set_default_card(@Field("user_id") String user_id, @Field("card_id") String card_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_product_v2")
  //  @POST("get_product")
    Call<GetProductList> getProductListApi(@Field("user_id") String user_id, @Field("cat_id") String cat_id, @Field("sub_cat_id") String sub_cat_id, @Field("page_no") String page_no);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("filer_product")
    Call<GetProductList> applyFilterApi(@Field("sort") String sort, @Field("product_type") String product_type, @Field("cat_id") String cat_id, @Field("payment_mode") String payment_mode, @Field("fixed_price") String fixed_price, @Field("price_min") String price_min, @Field("price_max") String price_max, @Field("delivery") String delivery);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_to_wishlist")
    Call<Common> addToWishListApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("remove_wishlist")
    Call<Common> removeFromWishListApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_wishlist")
    Call<GetProductList> getWishListApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_sale_list")
    Call<GetProductList> getForSalelistApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_records")
    Call<Wishlist> getRecordsApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("mark_sold")
    Call<Common> markProductAsSoldApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("delete_product")
    Call<Common> deleteProductApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("follow_user")
    Call<FollowModel> followUserApi(@Field("user_id") String user_id, @Field("other_user_id") String other_user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("unfollow_user")
    Call<FollowModel> unFollowUserApi(@Field("user_id") String user_id, @Field("other_user_id") String other_user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_following_status")
    Call<FollowModel> getFollowStatusApi(@Field("user_id") String user_id, @Field("other_user_id") String other_user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("report")
    Call<Common> reportApi(@Field("user_id") String user_id, @Field("reason") String reason, @Field("product_id") String product_id, @Field("other_user_id") String other_user_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("report_comment")
    Call<Common> reportCommentApi(@Field("user_id") String user_id, @Field("reason") String reason, @Field("comment_id") String product_id, @Field("other_user_id") String other_user_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("delete_user")
    Call<Common> deleteAccountApi(@Field("user_id") String user_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("forgot_password")
    Call<Common> forgetPasswordApi(@Field("email") String emailid);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_comment")
    Call<Common> addCommentApi(@Field("user_id") String user_id, @Field("product_id") String product_id, @Field("comment") String comment, @Field("comment_type") String comment_type);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("like_product")
    Call<LikeModel> likeProductApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("unlike_product")
    Call<LikeModel> unLikeProductApi(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_testimonial")
    Call<TestimonialModel> getUserTestimonialsApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_comment")
    Call<CommentModel> getProductComment(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("suggested_products")
    Call<GetProductList> suggestedProductsApi(@Field("cat_id") String catId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("logout")
    Call<Common> logoutApi(@Field("user_id") String userId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("update_phone")
    Call<ResendCode> upDatePhone(@Field("user_id") String user_id, @Field("phone_number") String phone_number, @Field("country_code") String country_code);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("charge")
    Call<JsonObject> stripePayment(@Field("user_id") String user_id, @Field("product_id") String product_id, @Field("amount") String amount, @Field("currency_type") String currency_type, @Field("token") String token, @Field("offer_id") String offerId, @Field("seller_id") String seller_id, @Field("customer_id") String customerId,@Field("chatoffer") String chatfofer);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("set_order_status")
    Call<JsonObject> set_order_status(@Field("user_id") String user_id, @Field("order_id") String order_id, @Field("order_status") String order_status, @Field("other_user_id") String otheruserid);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("addCard")
    Call<Common> addCardApi(@Field("user_id") String user_id, @Field("card_holder_name") String card_holder_name, @Field("card_number") String card_number, @Field("exp_month") String exp_month, @Field("exp_year") String exp_year, @Field("cvc") String cvc);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("getCard")
    Call<CardDetailModel> getCardApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("getGroupChat")
    Call<JsonObject> getChatData(@Field("user_id") String user_id, @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("blockList")
    Call<BlockListModel> getBlockList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("getFollowList")
    Call<FollowModelNew> getFollowListApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("banners")
    Call<BannerModel> getbannersApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("getChat")
    Call<ChattedListModel> getChattedUsersApi(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("notification_list")
    Call<NotificationListModel> getNotificationList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("onlineOffline")
    Call<Common> changeOnlineStatusApi(@Field("user_id") String user_id, @Field("online_status") String online_status);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("manageBlockList")
    Call<Common> manageBlockListApi(@Field("user_id") String user_id, @Field("block_user_id") String block_user_id, @Field("block_status") String block_status);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_testimonial")
    Call<Common> addTestimonialApi(@Field("user_id") String user_id, @Field("other_user_id") String other_user_id, @Field("feedback") String feedback, @Field("rating") String rating,@Field("offer_order_id") String offer_order_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("getChatDetail")
    Call<ChatListModel> getChatDetailApi(@Field("user_id") String user_id, @Field("receiver_id") String receiver_id,@Field("page_no") String page_no);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("chat_offer_list")
    Call<JsonObject> chat_offer_list(@Field("user_id") String user_id, @Field("friend_id") String receiver_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("make_offer")
    Call<MakeOfferModel> makeOfferApi(@Field("user_id") String user_id, @Field("product_id") String product_id, @Field("seller_id") String seller_id, @Field("price_cost") String price_cost, @Field("status") String status, @Field("product_name") String product_name, @Field("quantity") String quantity);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("accept_decline_offer")
    Call<MakeOfferModel> acceptDeclineOfferApi(@Field("user_id") String user_id,@Field("friend_id") String friend_id,@Field("offer_id") String offer_id, @Field("status") String status);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("edit_comment")
    Call<Common> editCommentApi(@Field("user_id") String user_id, @Field("comment_id") String comment_id, @Field("comment") String comment);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("delete_comment")
    Call<Common> deleteCommentApi(@Field("user_id") String user_id, @Field("comment_id") String comment_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("video_stream_list")
    Call<LiveVideoModel> getVideoListApi(@Field("user_id") String user_id, @Field("page_no") String pageNo, @Field("cat_id") String cat_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("pinComment")
    Call<Common> pinCommentApi(@Field("user_id") String userId, @Field("group_id") String groupId, @Field("comment_id") String commentId, @Field("pin_status") String pinStatus);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("pinnedCommentList")
    Call<JsonObject> getPinCommentApi(@Field("user_id") String userId, @Field("group_id") String groupId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("read_notify")
    Call<Common> readNotificationApi(@Field("user_id") String userId, @Field("noti_id") String notiId, @Field("read_status") String readStatus ,@Field("read_type") String type);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("like_comment")
    Call<LikeModel> likeCommentApi(@Field("user_id") String userId, @Field("comment_id") String commentId, @Field("like_status") String likeStatus);

    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("add_video_desc")
    Call<LiveVideoDescModel> addLiveVideoDesc(@Part("group_id") RequestBody groupId, @Part("cat_id") RequestBody catId, @Part("video_title") RequestBody videoTitle, @Part("video_desc") RequestBody videoDesc, @Part("seller_id") RequestBody sellerId, @Part MultipartBody.Part coberImage);

    @Multipart
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("upload_document")
    Call<LiveVideoDescModel> stripe_imageverification(@Part("user_id") RequestBody sellerId, @Part MultipartBody.Part coberImage);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_video_desc")
    Call<PinCommentModel> getLiveVideoDesc(@Field("group_id") String groupId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_my_streams")
    Call<LiveVideoModel> getStreamedVideoList(@Field("user_id") String userId, @Field("page_no") String pageNo);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("send_live_offer")
    Call<SendOfferModel> sendOfferApi(@Field("seller_id") String sellerId, @Field("receiver_id") String receiverId, @Field("group_id") String groupId, @Field("product_id") String productId, @Field("product_name") String productName, @Field("product_price") String productPrice);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("link_stripe")
    Call<Common> linkStripAccount(@Field("user_id") String userId, @Field("stripe_id") String stripeId);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_product_detail")
    Call<ProductDetailModel> getProductDetail(@Field("user_id") String userId, @Field("product_id") String productId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("get_product_detail")
    Call<JsonObject> getProductDetail1(@Field("user_id") String userId, @Field("product_id") String productId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("live_video_filter")
    Call<LiveVideoModel> getliveVideoFilter(@Field("cat_id") String catId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("removeCard")
    Call<Common> removeCardApi(@Field("user_id") String userId, @Field("card_id") String cardId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("promote_packages")
    Call<PromotePackageModel> getPromotePackagesApi(@Field("user_id") String userId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("promote_product")
    Call<Common> promoteProductApi(@Field("user_id") String userId, @Field("product_id") String productId, @Field("package_id") String package_id, @Field("card_id") String card_id, @Field("customer_id") String customer_id, @Field("promote_id") String promoteId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("cancel_promote")
    Call<Common> cancelPromotionApi(@Field("user_id") String userId, @Field("product_id") String productId, @Field("package_id") String package_id);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("disputeOffer")
    Call<JsonObject> disputeOffer(@Field("user_id") String userId, @Field("friend_id") String friend_id, @Field("order_id") String order_id,@Field("reason") String reason);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("manage_inventory")
    Call<JsonObject> manage_inventory(@Field("user_id") String userId, @Field("product_id") String p_id, @Field("quantity") String qty);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("withdrawal_money")
    Call<JsonObject> withdrawal_money(@Field("user_id") String userId);

    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("qr_payment")
    Call<JsonObject> pay_money_scan(@Field("user_id") String userId,@Field("other_user_id") String other_user_id,@Field("amount") String amount,@Field("currency_type") String currency_type,@Field("token") String token,@Field("customer_id") String customer_id);


    @FormUrlEncoded
    @Headers("authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh")
    @POST("generate_url")
    Call<JsonObject> getProductUrl(@Field("user_id") String userId, @Field("url_id") String url_id,@Field("url_type") String url_type);

   @FormUrlEncoded
   @GET
   Call<JsonObject> getAccounts(@Field("user_id") String userId);

}
