package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.NotificationUtils;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;
import static org.webrtc.ContextUtils.getApplicationContext;

public class NotificationActivityAdapter extends RecyclerView.Adapter<NotificationActivityAdapter.NotificationViewHolder> {
    private NotificationListModel notificationList;
    Context context;
    int state;
    String forWhich;
    private NotificationListModel notificationListFilter;
    private NotificationModel object;

    public NotificationActivityAdapter(NotificationListModel notificationMessList, Activity activity, int state, String forWhich) {

        this.notificationList = notificationMessList;
        notificationListFilter = notificationMessList;
        context = activity;
        this.state = state;
        this.forWhich = forWhich;
        Log.e("adapter_state", "working");

    }

    public void filterList(NotificationListModel newMessList) {
        this.notificationList = newMessList;
        notifyDataSetChanged();
    }

    @Override
    public NotificationActivityAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_mess_adapter, parent, false);
        NotificationActivityAdapter.NotificationViewHolder cvh = new NotificationActivityAdapter.NotificationViewHolder(groceryProductView);
        return cvh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final NotificationActivityAdapter.NotificationViewHolder holder, final int position) {


        if (forWhich.equalsIgnoreCase("p")) {
//            holder.imageView.setImageResource(notificationList.getArrPost().get(position).getProfileImage());
            Glide.with(context).load(notificationList.getArrPost().get(position).getImage())
                    .apply(Global.getGlideOptions()).into(holder.imageView);
            holder.headingText.setText(notificationList.getArrPost().get(position).getUsername());
            holder.subHeadingText.setText(notificationList.getArrPost().get(position).getMessage());

            dateFormat(Global.formateDateTo_HHmm(Global.convertUTCToLocal(notificationList.getArrPost().get(position).getMsgArr().getDatetime())));

           // checkDate(Global.formateDateTo_HHmm(Global.convertUTCToLocal(notificationList.getArrPost().get(position).getMsgArr().getDatetime())));


            holder.notiTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(notificationList.getArrPost().get(position).getMsgArr().getDatetime())));
            if(notificationList.getArrPost().get(position).getReadStatus().equalsIgnoreCase("1")){
                holder.imgNew.setVisibility(View.GONE);
            }else{
                holder.imgNew.setVisibility(View.VISIBLE);
            }
        } else if (forWhich.equalsIgnoreCase("a")) {
            Glide.with(context).load(notificationList.getArrAnnouncements().get(position).getImage())
                    .apply(Global.getGlideOptions()).into(holder.imageView);
            holder.headingText.setText(notificationList.getArrAnnouncements().get(position).getUsername());
            holder.subHeadingText.setText(notificationList.getArrAnnouncements().get(position).getMessage());
            holder.notiTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(notificationList.getArrAnnouncements().get(position).getMsgArr().getDatetime())));
            if(notificationList.getArrAnnouncements().get(position).getReadStatus().equalsIgnoreCase("1")){
                holder.imgNew.setVisibility(View.GONE);
            }else{
                holder.imgNew.setVisibility(View.VISIBLE);
            }
        } else {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Log.e("Notification_model", "handleDataMessage: " + object.getMessage());
                Bundle bundle = new Bundle();
                bundle.putParcelable(NT_DATA, object);
                if (notificationList != null) {
                    // app is in foreground, broadcast the push message
                    Intent resultIntent;
                    if (forWhich.equalsIgnoreCase("p")) {
                        switch (notificationList.getArrPost().get(position).getNotiType()) {
                            case NT_ACCEPT_REJECT:
                                resultIntent = new Intent(context, ChatActivity.class);
                                resultIntent.putExtra("otherUserId", notificationList.getArrPost().get(position).getUserId());
                                resultIntent.putExtra("otherUserImage", notificationList.getArrPost().get(position).getImage());
                                resultIntent.putExtra("otherUserName", notificationList.getArrPost().get(position).getUsername());
                                resultIntent.putExtras(bundle);
                                break;
                            case NT_FOLLOW:

                                break;
                            case NT_COMMENT_ADDED:

                                break;
                            case NT_PRODUCT_ADDED:

                                break;
                            case NT_CHAT:
                                resultIntent = new Intent(context, ChatActivity.class);
                                resultIntent.putExtra("otherUserId", notificationList.getArrPost().get(position).getUserId());
                                resultIntent.putExtra("otherUserImage", notificationList.getArrPost().get(position).getImage());
                                resultIntent.putExtra("otherUserName", notificationList.getArrPost().get(position).getUsername());
                                resultIntent.putExtras(bundle);
                                break;
                            case NT_PAYMENT:
                                resultIntent = new Intent(context, ChatActivity.class);
                                resultIntent.putExtra("otherUserId", notificationList.getArrPost().get(position).getUserId());
                                resultIntent.putExtra("otherUserImage", notificationList.getArrPost().get(position).getImage());
                                resultIntent.putExtra("otherUserName", notificationList.getArrPost().get(position).getUsername());
                                resultIntent.putExtras(bundle);
                                break;
                            case NT_OFFER_MAKE:
                                resultIntent = new Intent(context, ChatActivity.class);
                                resultIntent.putExtra("otherUserId", notificationList.getArrPost().get(position).getUserId());
                                resultIntent.putExtra("otherUserImage", notificationList.getArrPost().get(position).getImage());
                                resultIntent.putExtra("otherUserName", notificationList.getArrPost().get(position).getUsername());
                                resultIntent.putExtras(bundle);
                                break;
                        }
                    }
                }
            }
        });
        if (state == 0) {
            if (position == 0) {
                holder.order.setVisibility(View.VISIBLE);
            } else {
                holder.order.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(context, ChatActivity.class);
                    context.startActivity(in);
                }
            });
        } else {

            holder.order.setVisibility(View.GONE);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dateFormat(String date)
    {


        //----------------------------------------
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        // Set the values for the calendar fields YEAR, MONTH, and DAY_OF_MONTH.
        calendar1.set(2012, 2, 12);
        calendar1.set(2012, 2, 11);
        /*
         * Use getTimeInMillis() method to get the Calendar's time value in
         * milliseconds. This method returns the current time as UTC
         * milliseconds from the epoch
         */
        long miliSecondForDate1 = calendar1.getTimeInMillis();
        long miliSecondForDate2 = calendar2.getTimeInMillis();
        // Calculate the difference in millisecond between two dates
        long diffInMilis = miliSecondForDate2 - miliSecondForDate1;
        /*
         * Now we have difference between two date in form of millsecond we can
         * easily convert it Minute / Hour / Days by dividing the difference
         * with appropriate value. 1 Second : 1000 milisecond 1 Hour : 60 * 1000
         * millisecond 1 Day : 24 * 60 * 1000 milisecond
         */
        long diffInSecond = diffInMilis / 1000;
        long diffInMinute = diffInMilis / (60 * 1000);
        long diffInHour = diffInMilis / (60 * 60 * 1000);
        long diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
        System.out.println("Difference in Seconds : " + diffInSecond);
        System.out.println("Difference in Minute : " + diffInMinute);
        System.out.println("Difference in Hours : " + diffInHour);
        System.out.println("Difference in Days : " + diffInDays);

    }


    @Override
    public int getItemCount() {
        if (forWhich.equalsIgnoreCase("p")) {
            return notificationList.getArrPost().size();
        } else if (forWhich.equalsIgnoreCase("a")) {
            return notificationList.getArrAnnouncements().size();
        } else {
            return 0;
        }
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView headingText, subHeadingText, order, notiTime;
        ImageView imgNew;


        public NotificationViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.profileimage);
            headingText = view.findViewById(R.id.personHeading);
            subHeadingText = view.findViewById(R.id.personSubHeading);

            imgNew = view.findViewById(R.id.img_new_entry);
            notiTime = view.findViewById(R.id.txt_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                listener.onNotificationSelected(notificationListFilter.get(getAdapterPosition()));
                }
            });

        }
    }



}
