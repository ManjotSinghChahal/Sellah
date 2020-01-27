package com.app.admin.sellah.controller.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.NOTIFICATION_ID_BIG_IMAGE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_LIVE_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_TESTIMONIAL_ADDED;


/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

    private String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;
    String CHANNEL_ID = "";
    CharSequence name = "";
    int importance;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        CHANNEL_ID = "my_channel_01";
        name = "Android";
        importance = NotificationManager.IMPORTANCE_HIGH;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null, "");
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl, String noti_type) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;
        // notification icon
        final int icon = R.drawable.sellah_icon_red;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestCode = (int) System.currentTimeMillis();
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);/*
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");*/
        NotificationModel notiData = intent.getParcelableExtra(NT_DATA);
        if(notiData!=null){
            Log.e("Notification", "hasData");
        }else{
            Log.e("Notification", "don't hasData");
        }
        if (!TextUtils.isEmpty(imageUrl)) {

            Log.e("Notification", "Comes");
            Log.e("Notification_type", " : " + noti_type);

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                if (bitmap != null) {
//                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                    showSmallNotification(bitmap,mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    showNotificationAsType(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound,intent);
                } else {
                    Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_dummy_logo);
//                    showSmallNotification(bm, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    showNotificationAsType(bm, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound,intent);
                }
            }
        } else {

            Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_dummy_logo);
//            showSmallNotification(bm, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            showNotificationAsType(bm, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound,intent);

//            playNotificationSound();
        }
    }

    private void showNotificationAsType(Bitmap userImg, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, Intent dataIntent) {
        Bundle bundle=dataIntent.getExtras();
        if(bundle!=null){
            Log.e(TAG, "showNotificationAsType: hasExtras");
//            NotificationModel notiData= bundle.getParcelable(NT_DATA);
            NotificationModel notiData = dataIntent.getParcelableExtra(NT_DATA);
            if(notiData!=null){

                Log.e(TAG, "showNotificationAsType: "+notiData.getNotiType());
                switch (notiData.getNotiType()){

                    case NT_ACCEPT_REJECT:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_FOLLOW:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_COMMENT_ADDED:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_PRODUCT_ADDED:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_TESTIMONIAL_ADDED:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_CHAT:
                        if(notiData.getMsgType().equalsIgnoreCase("t")){
                            showSmallNotification(mBuilder,icon,title,message,timeStamp,resultPendingIntent,alarmSound);
                        }else if(notiData.getMsgType().equalsIgnoreCase("img")){
                            showSmallNotification(mBuilder,icon,title,message,timeStamp,resultPendingIntent,alarmSound);
                            /*if (notiData.geti != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                                Bitmap bitmap = getBitmapFromURL(imageUrl);
                            showBigNotification();*/
                        }
                        break;
                    case NT_PAYMENT:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_OFFER_MAKE:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    case NT_OFFER_LIVE_MAKE:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;
                    default:
                        showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                        break;

                }

            }else{
                Log.e(TAG, "showNotificationAsType: else" );
                showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            }
        }else{
            Log.e(TAG, "showNotificationAsType: else" );
            showSmallNotification(userImg, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
        }

    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.app_dummy_logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setChannelId(CHANNEL_ID)
                .build();

        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    private void showSmallNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(message);

        Notification notification;
        notification = mBuilder
                .setContentTitle(title)
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setLargeIcon(bitmap)
                .setContentText(message)
                .setChannelId(CHANNEL_ID)
                .setColor(mContext.getResources().getColor(R.color.colorRed))
//                .addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent)
                .addAction(android.R.drawable.ic_menu_send, "Open in App", resultPendingIntent)
                .build();
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.app_dummy_logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setChannelId(CHANNEL_ID)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);/*
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");*/
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
