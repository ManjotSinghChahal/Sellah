package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.model.extra.ChatHeadermodel.Record;
import com.app.admin.sellah.model.extra.DeleteChatModel.DeleteChat;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.SwipeHelper;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.MessageFragment;
import com.app.admin.sellah.view.fragments.NotificationFragment;
import com.app.admin.sellah.view.fragments.PersonalProfileFragment;
import com.app.admin.sellah.view.fragments.StreamedVideosFragment;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class NewMessageListAdapter extends RecyclerView.Adapter<NewMessageListAdapter.MessagesViewHolder> {

    private List<Record> chattedListModel;
    Context context;
    FragmentManager manager;
    int state;
    RecyclerView messRecyclers;
    String ongoing;
    MessageListAdapter.onSwipeLeft callback;
    int valNew;
//    private ChattedListModel notificationListFilter;

    public NewMessageListAdapter(int valNew,List<Record> chattedListModel, Activity activity, int state, FragmentManager manager, RecyclerView messRecycler, MessageListAdapter.onSwipeLeft callback) {

        this.valNew = valNew;
        this.chattedListModel = chattedListModel;
//        notificationListFilter = chattedListModel;
        context = activity;
        this.state = state;
        this.manager = manager;
        this.messRecyclers = messRecycler;
        this.callback = callback;
        Log.e("adapter_state", "working");
        Log.e("sizeNewMessage",chattedListModel.size()+"");

    }

    public NewMessageListAdapter(Activity activity,int state) {

//        notificationListFilter = chattedListModel;
        context = activity;
        this.state = state;
        Log.e("adapter_state", "working");

    }

    public void filterList(List<Record> newMessList) {
        this.chattedListModel = newMessList;

        notifyDataSetChanged();
    }

    @Override
    public NewMessageListAdapter.MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_mess_adapter, parent, false);
        NewMessageListAdapter.MessagesViewHolder cvh = new NewMessageListAdapter.MessagesViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final NewMessageListAdapter.MessagesViewHolder holder, final int position) {
      //holder.imageView.setImageResource(chattedListModel.get(position).getProfileImage());

        if (chattedListModel.get(position).getIsDeleted().equalsIgnoreCase("N"))
        {
            holder.linRoot_mess.setVisibility(View.VISIBLE);

            if (state==3)
            {

            }

            else
            {

                try {
                    compareDate(Global.convertUTCToLocal(chattedListModel.get(position).getLastMsgTime()),holder.msgTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.imgNew.setVisibility(View.GONE);
                Glide.with(context)
                        .load(chattedListModel.get(position).getFriendImage())
                        .apply(Global.getGlideOptions()).into(holder.imageView);

                holder.headingText.setText(!chattedListModel.get(position).getFriendName().equalsIgnoreCase("") ?
                        chattedListModel.get(position).getFriendName() : "Sellah! user");
                holder.subHeadingText.setText(chattedListModel.get(position).getMessage());
                //holder.msgTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(chattedListModel.get(position).getLastMsgTime())));

                holder.relDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MessageFragment.post_selId = chattedListModel.get(position).getFriendId();

                        if (!TextUtils.isEmpty(chattedListModel.get(position).getIsBlocked())&&chattedListModel.get(position).getIsBlocked().equalsIgnoreCase("y")) {

                            if(!TextUtils.isEmpty(chattedListModel.get(position).getBlockedBy())&&chattedListModel.get(position).getBlockedBy().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))){
//                        you have blocked user
                                S_Dialogs.getYouBlockedUserDialog(context,context.getString(R.string.dialog_title_you_block_user),(dialog, which)->{
                                    blockUnBlockUser("unblock",position,chattedListModel.get(position).getFriendId());
                                    dialog.dismiss();
                                }).show();
                            }else{
                                S_Dialogs.getUserBlockedYouDialog(context).show();
//                        user has blocked you.
                            }

                        }else{
                            Intent chatIntent = new Intent(context, ChatActivity.class);
                            chatIntent.putExtra("otherUserId", chattedListModel.get(position).getFriendId());
                            chatIntent.putExtra("otherUserImage", chattedListModel.get(position).getFriendImage());
                            chatIntent.putExtra("otherUserName", chattedListModel.get(position).getFriendName());
                            context.startActivity(chatIntent);
                        }
                    }
                });

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!TextUtils.isEmpty(chattedListModel.get(position).getIsBlocked())&&chattedListModel.get(position).getIsBlocked().equalsIgnoreCase("y")) {

                            if(!TextUtils.isEmpty(chattedListModel.get(position).getBlockedBy())&&chattedListModel.get(position).getBlockedBy().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))){
//                        you have blocked user
                                S_Dialogs.getYouBlockedUserDialog(context,context.getString(R.string.dialog_title_you_block_user),(dialog,which)->{
                                    blockUnBlockUser("unblock",position,chattedListModel.get(position).getFriendId());
                                    dialog.dismiss();
                                }).show();
                            }else{
                                S_Dialogs.getUserBlockedYouDialog(context).show();
//                        user has blocked you.
                            }

                        }else{
                            loadFragment(new PersonalProfileFragment(), position);
                        }

                    }
                });

            }

        }
        else
        {

            holder.linRoot_mess.setVisibility(View.GONE);
        }

    }

    private void blockUnBlockUser(String blockStatus, int pos, String userId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> manageBlockUserCall = Global.WebServiceConstants.getRetrofitinstance().manageBlockListApi(HelperPreferences.get(context).getString(UID), userId, blockStatus);
        manageBlockUserCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(context,  response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        chattedListModel.get(pos).setIsBlocked("N");
                        notifyDataSetChanged();
                    }
                } else {

                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("bmanageUser", "onFailure: " + t.getMessage());
            /*    Snackbar.make(rootMbu, "Please try again latter.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();*/
                Toast.makeText(context,  "Please try again latter.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        if (state==3)
        {
            return 1;
        }
        else
        {
            return valNew;
        }

    }


    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView headingText, subHeadingText, msgTime;
        ImageView imgNew;
        RelativeLayout relDetails;
        LinearLayout linRoot_mess;

        public MessagesViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.profileimage);
            headingText = view.findViewById(R.id.personHeading);
            subHeadingText = view.findViewById(R.id.personSubHeading);

            msgTime = view.findViewById(R.id.txt_time);
            imgNew = view.findViewById(R.id.img_new_entry);
            relDetails = view.findViewById(R.id.rel_details);
            linRoot_mess = view.findViewById(R.id.linRoot_mess);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                listener.onNotificationSelected(notificationListFilter.get(getAdapterPosition()));
                }
            });

        }
    }

    public boolean loadFragment(Fragment fragment, int pos) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(SAConstants.Keys.OTHER_USER_ID, chattedListModel.get(pos).getFriendId());
            fragment.setArguments(bundle);
            manager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    public interface onSwipeLeft {
        void onSwipeLeftSuccess(int pos);
        void onSwipeLeftFailure();
    }

    private Date today() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        return cal.getTime();
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }



    public void compareDate(String str,TextView txtviewTime) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String[] dateTemp = str.split(" ");
        if (dateTemp.length>1)
        {
            String dateNew =   dateTemp[0];
            String timeNew =   dateTemp[1];

            if (dateNew.equals(dateFormat.format(today())))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try{
                    Date date3 = sdf.parse(timeNew.substring(0,5));
                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
                    txtviewTime.setText(sdf2.format(date3));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            else if (dateNew.equals(dateFormat.format(yesterday())))
                txtviewTime.setText("Yesterday");
            else
                txtviewTime.setText(dateNew);

        }



    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
    }

}
