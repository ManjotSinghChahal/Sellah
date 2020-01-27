package com.app.admin.sellah.controller.WebServices;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.app.admin.sellah.model.extra.BannerModel.BannerModel;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.model.extra.DeleteChatModel.DeleteChat;
import com.app.admin.sellah.model.extra.EditProfileModel;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.FeaturedPosts.FeaturedPosts;
import com.app.admin.sellah.model.extra.LikeModel.LikeModel;
import com.app.admin.sellah.model.extra.LiveVideoModel.LiveVideoModel;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.model.extra.ResendCode.ResendCode;
import com.app.admin.sellah.model.extra.SendOffer.SendOfferModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_EMAIL;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_PROFILE_PIC;

public class ApisHelper {

    private WebService service;

    public ApisHelper() {
        service = Global.WebServiceConstants.getRetrofitinstance();
    }



    Call<DeleteChat> deleteChatCall;
    public void getDeleteChat(String user_id,String room_id,OnDeleteChat call_back_deletechat) {
        deleteChatCall = service.deleteChatApi(user_id,room_id);
        deleteChatCall.enqueue(new Callback<DeleteChat>() {
            @Override
            public void onResponse(Call<DeleteChat> call, Response<DeleteChat> response) {


                try {


                if (response.isSuccessful()) {
                    call_back_deletechat.onDeleteChatSuccess(response.body());

                } else {

                    try {
                        Log.e("deletechat", "un-Success : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                }catch (Exception e){
                }
            }

            @Override
            public void onFailure(Call<DeleteChat> call, Throwable t) {
                call_back_deletechat.onDeleteChatFailure();
                Log.e("deletechat", "failure : " + t.getMessage());

            }
        });

    }





    Call<com.app.admin.sellah.model.extra.FeaturedPosts.FeaturedPosts> featuredPostsCall;
    public void getFeaturedPosts(String page,OnFeaturedPosts callback_featrured) {
        featuredPostsCall = service.getFeaturedPostsApi(page);
        featuredPostsCall.enqueue(new Callback<FeaturedPosts>() {
            @Override
            public void onResponse(Call<FeaturedPosts> call, Response<FeaturedPosts> response) {
                if (response.isSuccessful()) {
                    callback_featrured.onFeaturedPostsSuccess(response.body());

                } else {

                    try {
                        Log.e("categories", "un-Success : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FeaturedPosts> call, Throwable t) {
                callback_featrured.onFeaturedPostsFailure();
                Log.e("categories", "failure : " + t.getMessage());
            }
        });

    }












    Call<GetCategoriesModel> categoriesModelCall1;
    public void getCategories(String user_id,GetCategoryCallback callback) {
//        ExpandableListData.setDataOnfaliur();
        categoriesModelCall1 = service.getCategoryApi(user_id);
        categoriesModelCall1.enqueue(new Callback<GetCategoriesModel>() {
            @Override
            public void onResponse(Call<GetCategoriesModel> call, Response<GetCategoriesModel> response) {
                if (response.isSuccessful()) {
                    Log.e("categories_", "Success : " + response.body().getMessage());
                    callback.onGetCategorySuccess();
                    ExpandableListData.setData(response.body());
                } else {
                    ExpandableListData.setDataOnfaliur();
                    try {
                        Log.e("categories", "un-Success : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCategoriesModel> call, Throwable t) {
                ExpandableListData.setDataOnfaliur();
                callback.onGetCategoryFailure();
                Log.e("categories", "failure : " + t.getMessage());
            }
        });

    }
    Call<BannerModel> categoriesModelCall;
    public void getBanner(String user_id, OnGetDataListners listners) {

         categoriesModelCall = service.getbannersApi(user_id);
        categoriesModelCall.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                if (response.isSuccessful()) {
                    Log.e("getBannerApi", "Success : " + response.body().getMessage());
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        listners.onGetDataSuccess(response.body());
                    }
                } else {
                    listners.onGetDataFailure();
                    try {
                        Log.e("getBannerApi", "un-Success : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                listners.onGetDataFailure();
                Log.e("categories", "failure : " + t.getMessage());
            }
        });

    }
    Call<CardDetailModel> stripePaymentApi;

    public void getCardApi(Context context, OnGetCardDataListners listners) {
         stripePaymentApi = service.getCardApi(HelperPreferences.get(context).getString(UID));
         stripePaymentApi.enqueue(new Callback<CardDetailModel>() {
            @Override
            public void onResponse(Call<CardDetailModel> call, Response<CardDetailModel> response) {


                try {
                Gson gson = new GsonBuilder().create();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        listners.onGetDataSuccess(response.body());
                    }
//                    showCCview(response.body());
                } else {
                    listners.onGetDataFailure();
//                    hideCCview();
                    try {
                        Log.e("GetCard_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){ }

            }

            @Override
            public void onFailure(Call<CardDetailModel> call, Throwable t) {
               /* if (dialog != null && dialog.isShowing())
                    dialog.dismiss();*/
                try {
                listners.onGetDataFailure();
                Log.e("GetCard_failure", t.getMessage());
                }catch (Exception e){ }

            }
        });

    }
    Call<ChattedListModel> chatedListCall;
    public void getChattedUsersListApi(Context context, OnGetChatedListDataListners listDataListners) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
//        dialog.show();
        chatedListCall = service.getChattedUsersApi(HelperPreferences.get(context).getString(UID));
        chatedListCall.enqueue(new Callback<ChattedListModel>() {
            @Override
            public void onResponse(Call<ChattedListModel> call, Response<ChattedListModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        listDataListners.onGetChattedListSuccess(response.body(), dialog);
                    }
                } else {
                    listDataListners.onGetChattedListFailure();
                }
            }

            @Override
            public void onFailure(Call<ChattedListModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                listDataListners.onGetChattedListFailure();
                Log.e("GetChatListApi", "onFailure: " + t.getMessage());
            }
        });
    }


    public void getSeaarchResultApi(Context context, SearchResultCallback listDataListners, String search_keyword) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<GetProductList> chatedListCall = service.searchProductApi(search_keyword,"name");
        chatedListCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        listDataListners.onProductListSuccess(response.body());
                    }
                } else {
                    listDataListners.onProductListFailure();
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                listDataListners.onProductListFailure();
                Log.e("GetChatListApi", "onFailure: " + t.getMessage());
            }
        });
    }

    public void updateProfileApi(EditProfileModel model, Context context, UpdateProfileCallback callback) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();

        Call<Common> editProfileCall = service.editProfileApi(model.getUser_id(), model.getUsername()
                , model.getDescription(), model.getCountry_code(), model.getPhone_number(), model.getAbout(), model.getShipping_policy()
                , model.getReturn_policy(), model.getAddress_name(), model.getAddress_1(), model.getAddress_2(), model.getPostal_code(), model.getState()
                , model.getAddress_city(), model.getImage(), model.getEdit_mode());

        editProfileCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onProfileUpdateSuccess(response.body().getMessage());

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("EditProfile", "onFailure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onProfileUpdateFailure();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("EditProfile", "onFailure: " + t.getMessage());
                callback.onProfileUpdateFailure();
            }
        });
    }

    public void editCommentApi(Context context, String commentId, String content, EditCommentCallback callback) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> editProfileCall = service.editCommentApi(HelperPreferences.get(context).getString(UID), commentId, content);
        editProfileCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onCommentEditionSuccess(response.body().getMessage());

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onCommentEditionFailure();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onCommentEditionFailure();
            }
        });
    }

    public void deleteCommentApi(Context context, String commentId, DeleteCommentCallback callback) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> editProfileCall = service.deleteCommentApi(HelperPreferences.get(context).getString(UID), commentId);
        editProfileCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {

                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onCommentdeletionSuccess(response.body().getMessage());

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onCommentdeletionFailure();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onCommentdeletionFailure();
            }
        });
    }

    public void reportCommentApi(Context context, String reason, String otherUserId, String commentId, ReportApi.ReportCallback callback, ReportApi.ReportErrorCallBack errorCallBack) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> reportCall = service.reportCommentApi(HelperPreferences.get(context).getString(UID), reason, commentId, otherUserId);
        reportCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onReportSubmit(response.body().getMessage());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    errorCallBack.onReportError();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                errorCallBack.onReportError();
            }
        });
    }

    public void getProfileData(Context context, GetProfileCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Log.e("user", "" + HelperPreferences.get(context).getString(UID));
        Call<JsonObject> getProfileCall = service.getProfileApi1(HelperPreferences.get(context).getString(UID));
        getProfileCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    callback.onGetProfileSuccess(response.body());



                    //Log.e("profile_success1", "" + response.body().toString());

                  /*  HelperPreferences.get(context).saveString(API_ACCESS_TOKEN, response.body().getResult().getStripeId());
                    callback.onGetProfileSuccess(response.body());
                    Log.e("profile_success", "" + response.body().getResult().getImage());;*/
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onGetProfileFailure();
                    try {
                        Log.e("Profile_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onGetProfileFailure();
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }
    Call<LiveVideoModel> getVideodataCall;
    public void getLiveVideoData(Context context, String pageNo, String catId, GetLiveVideoCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
         getVideodataCall = service.getVideoListApi(HelperPreferences.get(context).getString(UID), pageNo, catId);
        getVideodataCall.enqueue(new Callback<LiveVideoModel>() {
            @Override
            public void onResponse(Call<LiveVideoModel> call, Response<LiveVideoModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onGetLiveVideoSuccess(response.body());
                    Log.e("LiveVideo_success", "" + response.body().getList().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onGetLiveVideoFailure();
                    try {
                        Log.e("LiveVideo_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveVideoModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onGetLiveVideoFailure();
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }
    Call<LiveVideoModel> streamedVideoListCall;
    public void getStreamedVideoData(Context context, String pageNo, GetLiveVideoCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        streamedVideoListCall = service.getStreamedVideoList(HelperPreferences.get(context).getString(UID), pageNo);
        streamedVideoListCall.enqueue(new Callback<LiveVideoModel>() {
            @Override
            public void onResponse(Call<LiveVideoModel> call, Response<LiveVideoModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onGetLiveVideoSuccess(response.body());
                    Log.e("LiveVideo_success", "" + response.body().getList().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onGetLiveVideoFailure();
                    try {
                        Log.e("LiveVideo_error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveVideoModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onGetLiveVideoFailure();
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }

    public void sendOfferApi(Context context, String receiverId, String groupId, String productid, String productName, String productPrice, SendOfferCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<SendOfferModel> sendOfferApiCall = service.sendOfferApi(HelperPreferences.get(context).getString(UID), receiverId, groupId, productid, productName, productPrice);
        sendOfferApiCall.enqueue(new Callback<SendOfferModel>() {
            @Override
            public void onResponse(Call<SendOfferModel> call, Response<SendOfferModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onSendOfferSuccess(response.body().getMessage());
                    Log.e("SendOffer", "" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onSendOfferFailure();
                    try {
                        Log.e("SendOffer", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendOfferModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onSendOfferFailure();
                Log.e("SendOffer", "onFailure: " + t.getMessage());
            }
        });
    }

    public void readNotificationApi(Context context, String notiId,String type, ReadNotificationCallback callback) {
        Log.e("Notification_id", "readNotificationApi: " + notiId);
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> readNotificationApiCall = service.readNotificationApi(HelperPreferences.get(context).getString(UID), notiId, "1",type);
        readNotificationApiCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onReadNotificationSuccess(response.body().getMessage());
                    Log.e("ReadNotification", "" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onReadNotificationFailure();
                    try {
                        Log.e("ReadNotification", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onReadNotificationFailure();
                Log.e("ReadNotification", "onFailure: " + t.getMessage());
            }
        });
    }

    public void likeCommentApi(Context context, String commentId, String likeStatus, LikeCommentCallback callback) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<LikeModel> likeCommentApiCall = service.likeCommentApi(HelperPreferences.get(context).getString(UID), commentId, likeStatus.toUpperCase());
        likeCommentApiCall.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onLikeCommentSuccess(response.body().getMessage());
                    Log.e("Like_Comment", "onSuccess" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onLikeCommentFailure();
                    try {
                        Log.e("Like_Comment", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onLikeCommentFailure();
                Log.e("Like_Comment", "onFailure: " + t.getMessage());
            }
        });
    }

    public void LogoutApi(Context context, LogOutCallback callback) {

        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> logoutApiCall = service.logoutApi(HelperPreferences.get(context).getString(UID));
        logoutApiCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onLogOutSuccess(response.body().getMessage());
                    Log.e("logout", "onSuccess" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onLogOutFailure();
                    try {
                        Log.e("logout", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onLogOutFailure();
                Log.e("logout", "onFailure: " + t.getMessage());
            }
        });
    }

    public void linkStripApi(Context context, String stripeId, StripeConnectCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
//        dialog.show();
        Call<Common> linkStripAccountCall = service.linkStripAccount(HelperPreferences.get(context).getString(UID), stripeId);
        linkStripAccountCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onStripeConnectSuccess(response.body().getMessage());
                    Log.e("StripeConnect", "" + response.body().getMessage());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onStripeConnectFailure();
                    try {
                        Log.e("StripeConnect", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onStripeConnectFailure();
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getCategoryLiveVideo(Context context, String catId, SortLiveVideo callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
//        dialog.show();
        Call<LiveVideoModel> linkStripAccountCall = service.getliveVideoFilter(catId);
        linkStripAccountCall.enqueue(new Callback<LiveVideoModel>() {
            @Override
            public void onResponse(Call<LiveVideoModel> call, Response<LiveVideoModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onSortLiveVideoSuccess(response.body());
                    Log.e("StripeConnect", "" + response.body().getMessage());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callback.onSortLiveVideoFailure();
                    try {
                        Log.e("StripeConnect", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveVideoModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                callback.onSortLiveVideoFailure();
                Log.e("Profile_error", "onFailure: " + t.getMessage());
            }
        });
    }

    public void sendOtpChangePassApi(Context context, OTPSentCallBack callBack) {
        Dialog loadingdialog = S_Dialogs.getLoadingDialog(context);
        loadingdialog.show();

        Call<ResendCode> changePassCall = service.sendOtpChangePass(HelperPreferences.get(context).getString(UID));
        changePassCall.enqueue(new Callback<ResendCode>() {
            @Override
            public void onResponse(Call<ResendCode> call, Response<ResendCode> response) {
                if (response.isSuccessful()) {

                    if (loadingdialog != null && loadingdialog.isShowing()) {
                        loadingdialog.dismiss();
                    }
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    callBack.onOTPSentSuccess(response.body());

                } else {
                    if (loadingdialog != null && loadingdialog.isShowing()) {
                        loadingdialog.dismiss();
                    }
                    Toast.makeText(context, "Unable to send OTP on your registered mobile number at the movement.", Toast.LENGTH_SHORT).show();
                    callBack.onOTPSentFailure();
                    try {
                        Log.e("ChagePassErrorOTP", " : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResendCode> call, Throwable t) {
                if (loadingdialog != null && loadingdialog.isShowing()) {
                    loadingdialog.dismiss();
                }
                callBack.onOTPSentFailure();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnGetDataListners {
        void onGetDataSuccess(BannerModel body);

        void onGetDataFailure();
    }

    public interface OTPSentCallBack {
        void onOTPSentSuccess(ResendCode body);

        void onOTPSentFailure();
    }

    public interface OnGetCardDataListners {
        void onGetDataSuccess(CardDetailModel body);

        void onGetDataFailure();
    }

    public interface OnDeleteChat {
        void onDeleteChatSuccess(DeleteChat body);

        void onDeleteChatFailure();
    }

    public interface OnGetChatedListDataListners {
        void onGetChattedListSuccess(ChattedListModel body, Dialog dialog);

        void onGetChattedListFailure();
    }

    public interface SearchResultCallback {
        void onProductListSuccess(GetProductList body);

        void onProductListFailure();
    }

    public interface UpdateProfileCallback {
        void onProfileUpdateSuccess(String msg);

        void onProfileUpdateFailure();
    }

    public interface EditCommentCallback {
        void onCommentEditionSuccess(String msg);

        void onCommentEditionFailure();
    }

    public interface DeleteCommentCallback {
        void onCommentdeletionSuccess(String msg);

        void onCommentdeletionFailure();
    }

    public interface GetProfileCallback {
        void onGetProfileSuccess(JsonObject msg);

        void onGetProfileFailure();
    }

    public interface GetCategoryCallback{
        void onGetCategorySuccess();

        void onGetCategoryFailure();
    }

    public interface GetLiveVideoCallback {
        void onGetLiveVideoSuccess(LiveVideoModel msg);

        void onGetLiveVideoFailure();
    }

    public interface ReadNotificationCallback {
        void onReadNotificationSuccess(String msg);

        void onReadNotificationFailure();
    }

    public interface LikeCommentCallback {
        void onLikeCommentSuccess(String msg);

        void onLikeCommentFailure();
    }

    public interface SendOfferCallback {
        void onSendOfferSuccess(String msg);

        void onSendOfferFailure();
    }

    public interface LogOutCallback {
        void onLogOutSuccess(String msg);

        void onLogOutFailure();
    }

    public interface StripeConnectCallback {
        void onStripeConnectSuccess(String msg);

        void onStripeConnectFailure();
    }

    public interface SortLiveVideo {
        void onSortLiveVideoSuccess(LiveVideoModel videoList);

        void onSortLiveVideoFailure();
    }

    public interface OnFeaturedPosts {
        void onFeaturedPostsSuccess(FeaturedPosts videoList);

        void onFeaturedPostsFailure();
    }


    public void cancel_banner_request()
    {
        if (categoriesModelCall!=null)
        {
            categoriesModelCall.cancel();
        }

    }
    public void cancel_chatlist()
    {
        if (chatedListCall!=null)
        {
            chatedListCall.cancel();
        }

    }

    public void getcategoriesmodel1_cancel()
    {
        if (categoriesModelCall1!=null)
        {categoriesModelCall1.cancel();}
    }
    public void cancel_striipe_request()
    {if (stripePaymentApi!=null)
    {stripePaymentApi.cancel();}}
    public void getvideodata_cancel()
    {
        if (getVideodataCall!=null)
        {getVideodataCall.cancel();}
    }

    public void streameedvideolist_cancel()
    {if (streamedVideoListCall!=null)
    {streamedVideoListCall.cancel();}}

}
