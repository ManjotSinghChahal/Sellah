package com.app.admin.sellah.controller.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.SAConstants.ConstValues.SCREEN_STATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_LIVE_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

/**
 * Created by Rghubeer singh.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "getMessage" + remoteMessage.getMessageId());

        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (isLogined(this)) {

            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//                handleNotification(remoteMessage.getNotification().getBody());

            }

            if (remoteMessage.getData().size() > 0) {


                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                Log.e(TAG, "Data Payload: " + SCREEN_STATUS);


                if (!SCREEN_STATUS.equalsIgnoreCase(ChatActivity.class.getSimpleName())) {

                    Log.e(TAG, "chat: ");
                    try {
                        JSONObject json = new JSONObject(remoteMessage.getData());
                        handleDataMessage(json);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                } else {

                    JSONObject json = new JSONObject(remoteMessage.getData());
                    String mJsonString = String.valueOf(json);
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(mJsonString);
                    Gson gson = new Gson();
                    NotificationModel object = gson.fromJson(mJson, NotificationModel.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(NT_DATA, object);
                    Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                    pushNotification.putExtras(bundle);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                }

            } else {
                if (!SCREEN_STATUS.equalsIgnoreCase(ChatActivity.class.getSimpleName())) {

                    Log.e(TAG, "screeen: ");
                    handleNotification(remoteMessage.getNotification().getBody());
                }
            }
            // Check if message contains a data payload.

        }
    }


    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {


            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            // show notification
            showNotificationMessage(getApplicationContext(), "t", message, "", pushNotification);
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);
            //check for image attachment
            showNotificationMessage(getApplicationContext(), "title", message, "timestamp", resultIntent);
//          notificationUtils.OpenNotificationActivity(message);
            Log.e(TAG, "handleDataMessage_message: foreground");
        } else {
            Log.e(TAG, "handleDataMessage_message:  Background");
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {

        Log.e(TAG, "push json: " + json.toString());

        String mJsonString = String.valueOf(json);
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(mJsonString);
        Gson gson = new Gson();
        NotificationModel object = gson.fromJson(mJson, NotificationModel.class);

        Log.e("Notification_model", "handleDataMessage: " + object.getMessage());

        String title = object.getUsername() != null ? object.getUsername() : "";
        String message = object.getMessage() != null ? object.getMessage() : "";
        String imageUrl = object.getUserimage();
        String timestamp = object.getDatetime() != null ? object.getDatetime() : "";
        String otherUserId = object.getOtherUserId() != null ? object.getOtherUserId() : "";
        String noti_type = object.getNotiType();

        Log.e(TAG, "title: " + title);
        Log.e(TAG, "message: " + message);
        Log.e(TAG, "otherUserId: " + otherUserId);
        Log.e(TAG, "imageUrl: " + imageUrl);
        Log.e(TAG, "timestamp: " + timestamp);
        Log.e(TAG, "notiype: " + noti_type);

        Bundle bundle = new Bundle();
        bundle.putParcelable(NT_DATA, object);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtras(bundle);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Intent resultIntent = null;

            //-------------------to check activity---------------------------------------------
           /*ArrayList<String> runningactivities = new ArrayList<String>();
            ActivityManager activityManager = (ActivityManager)getBaseContext().getSystemService (Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for (int i1 = 0; i1 < services.size(); i1++) {
                runningactivities.add(0,services.get(i1).topActivity.toString());
            }*/
            //if(runningactivities.contains("com.app.admin.sellah.view.activities.MainActivityLiveStream}")==true)


          //  switch (object.getNotiType()) {
            switch (noti_type) {

                case NT_ACCEPT_REJECT:

                    if (SCREEN_STATUS.equalsIgnoreCase("MainActivityLiveStream")) {
                        Toast.makeText(this, "Can't open while you are live.", Toast.LENGTH_SHORT).show();
                    } else {
                        resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("otherUserId", object.getUserId());
                        resultIntent.putExtra("otherUserImage", object.getUserimage());
                        resultIntent.putExtra("otherUserName", object.getUsername());
                        resultIntent.putExtras(bundle);
                    }

                    break;
                case NT_FOLLOW:
                    resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtras(bundle);
                    break;
                case NT_COMMENT_ADDED:
                    resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtras(bundle);
                    break;
                case NT_PRODUCT_ADDED:
                    resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtras(bundle);
                    break;

                //
                case NT_CHAT:

                    if (SCREEN_STATUS.equalsIgnoreCase("MainActivityLiveStream")) {

                        Toast.makeText(this, "Can't open while you are live.", Toast.LENGTH_SHORT).show();


                    } else {
                        resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("otherUserId", object.getUserId());
                        resultIntent.putExtra("otherUserImage", object.getUserimage());
                        resultIntent.putExtra("otherUserName", object.getUsername());
                        resultIntent.putExtras(bundle);
                    }

                    break;

                case NT_PAYMENT:
                    if (SCREEN_STATUS.equalsIgnoreCase("MainActivityLiveStream")) {

                        Toast.makeText(this, "Can't open while you are live.", Toast.LENGTH_SHORT).show();

                    } else {
                        resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("otherUserId", object.getUserId());
                        resultIntent.putExtra("otherUserImage", object.getUserimage());
                        resultIntent.putExtra("otherUserName", object.getUsername());
                        resultIntent.putExtras(bundle);
                    }

                    break;
                case NT_OFFER_MAKE:
                    if (SCREEN_STATUS.equalsIgnoreCase("MainActivityLiveStream")) {

                        Toast.makeText(this, "Can't open while you are live.", Toast.LENGTH_SHORT).show();


                    } else {
                        resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("otherUserId", object.getUserId());
                        resultIntent.putExtra("otherUserImage", object.getUserimage());
                        resultIntent.putExtra("otherUserName", object.getUsername());
                        resultIntent.putExtras(bundle);
                    }

                    break;

                case NT_OFFER_LIVE_MAKE:

                    if (SCREEN_STATUS.equalsIgnoreCase("MainActivityLiveStream")) {

                        Toast.makeText(this, "Can't open while you are live.", Toast.LENGTH_SHORT).show();


                    } else {
                        resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("otherUserId", object.getUserId());
                        resultIntent.putExtra("otherUserImage", object.getUserimage());
                        resultIntent.putExtra("otherUserName", object.getUsername());
                        resultIntent.putExtras(bundle);
                    }
                    break;
                default:
                    resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtras(bundle);
                    break;


            }
            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), message, title, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, noti_type);
            }
            Log.e(TAG, "handleDataMessage: foreground");


            //     }

        } else {
            Log.e(TAG, "handleDataMessage: Background");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
            resultIntent.putExtras(bundle);
            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, noti_type);
            }
        }
//            }
//        }

      /*
        try {
//            JSONObject data = json.getJSONObject("data");

            String title = json.getString("username")!=null?json.getString("username"):"";
            String message = json.getString("message")!=null?json.getString("message"):"";
//            boolean isBackground = json.getBoolean("is_background");
            String imageUrl = json.getString("userimage")!=null?json.getString("userimage"):"";
            String timestamp = json.getString("datetime")!=null?json.getString("datetime"):"";
            String otherUserId = json.getString("other_user_id")!=null?json.getString("other_user_id"):"";
//            JSONObject payload = json.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "otherUserId: " + otherUserId);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//                notificationUtils.OpenNotificationActivity(message);

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
                Log.e(TAG, "handleDataMessage: foreground" );
            } else {
                Log.e(TAG, "handleDataMessage: Background" );
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
*/
    }

    /**
     * Showing notification with text only
     */
    public void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    public void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl, String noti_type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, noti_type);
    }


}