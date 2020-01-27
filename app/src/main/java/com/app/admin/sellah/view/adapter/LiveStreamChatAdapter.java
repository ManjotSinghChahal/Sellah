package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.RecyclerViewClickListener;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_PROFILE_PIC;

/**
 * Created by Admin on 11/30/2018.
 */

public class LiveStreamChatAdapter extends RecyclerView.Adapter {


    ArrayList<HashMap<String, String>> messagelist;
    Context context;
    private RecyclerViewClickListener onBottomReachedListener;
    private WebService service;
    private final int[] IMAGES = {R.drawable.unpined_icon, R.drawable.pined_icon};
    private boolean isClicedR;
    private String sellerId="";
    OptionsClickCallBack makeOfferController;
    private String productname_1 = "";
   /* public LiveStreamChatAdapter(Context activity, ArrayList<HashMap<String,String>> models) {


            this.messagelist = models;
            this.context = activity;
           // onBottomReachedListener = new ChatActivity();
          //  service = Global.WebServiceConstants.getRetrofitinstance();
            Log.e("SAcwsedac ",messagelist+"  yeah");

    }*/

    public LiveStreamChatAdapter(ArrayList<HashMap<String, String>> models, Context activity,String sellerId,OptionsClickCallBack controller) {
        this.messagelist = models;
        this.context = activity;
        //  onBottomReachedListener = ;
        this.makeOfferController=controller;
        this.sellerId=sellerId;
        Log.e("liveStreamChatApt_list:", messagelist + "");
        Log.e("seller_id ", sellerId + "  <-");
    }

    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView txtMessage, txtMsgTime;
        ImageView imagePin, imgoptions;

        public SendMessageViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);
            this.imagePin = itemView.findViewById(R.id.img_pin);
            this.imgoptions = itemView.findViewById(R.id.img_msg_options);

        }
    }


    public static class RecievedMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePin,imgOption;
        CircleImageView userImage;
        TextView txtMessage, txtMsgTime;
        RelativeLayout img_sendOfferIcon;

        public RecievedMessageViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);
            this.imagePin = itemView.findViewById(R.id.img_pin);
            this.imgOption = itemView.findViewById(R.id.img_msg_options);
            this.img_sendOfferIcon = itemView.findViewById(R.id.img_sendOfferIcon);

        }
    }

    public static class ImageSentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        ImageView imgSent;

        public ImageSentViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.imgSent = itemView.findViewById(R.id.img_sent_image);
        }
    }

    public static class ImageReceivedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        ImageView imgSent;

        public ImageReceivedViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.imgSent = itemView.findViewById(R.id.img_received_image);
        }
    }

    public static class PinnedReceivedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView txtTitle, txtCategory, txtDescription;

        public PinnedReceivedViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtTitle = itemView.findViewById(R.id.txt_Title);
            this.txtCategory = itemView.findViewById(R.id.txt_category);
            this.txtDescription = itemView.findViewById(R.id.txt_description);
        }
    }

    public static class PinnedSendViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView txtTitle, txtCategory, txtDescription;

        public PinnedSendViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtTitle = itemView.findViewById(R.id.txt_Title);
            this.txtCategory = itemView.findViewById(R.id.txt_category);
            this.txtDescription = itemView.findViewById(R.id.txt_description);
        }
    }

    public static class MakeOfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemQuantity, txtItemCost, txtCanceled;
        Button btnAccept, btnCanceld, btnPay;
        LinearLayout liOffer, liPay, liOfferStatus;

        public MakeOfferViewHolder(View itemView) {
            super(itemView);
            this.txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
            this.txtItemQuantity = itemView.findViewById(R.id.txt_item_quantity);
            this.txtItemCost = itemView.findViewById(R.id.txt_item_cost);
            this.txtCanceled = (TextView) itemView.findViewById(R.id.txt_offer_status);
            this.btnAccept = itemView.findViewById(R.id.btn_item_accept);
            this.btnPay = itemView.findViewById(R.id.btn_pay);
            this.btnCanceld = itemView.findViewById(R.id.btn_item_cancel);
            this.liOffer = itemView.findViewById(R.id.li_confirm_order);
            this.liPay = itemView.findViewById(R.id.li_pay_order);
            this.liOfferStatus = itemView.findViewById(R.id.li_offer_status);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SAConstants.Keys.TYPE_SEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_message_sent, parent, false);
                return new LiveStreamChatAdapter.SendMessageViewHolder(view);
            case SAConstants.Keys.TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_message_recieved, parent, false);
                return new LiveStreamChatAdapter.RecievedMessageViewHolder(view);
            case SAConstants.Keys.TYPE_MAKEOFFER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_message_sent, parent, false);
                return new LiveStreamChatAdapter.SendMessageViewHolder(view);
            case SAConstants.Keys.TYPE_MAKEOFFER_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_message_recieved, parent, false);
                return new LiveStreamChatAdapter.RecievedMessageViewHolder(view);
            case SAConstants.Keys.TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_send_image, parent, false);
                return new LiveStreamChatAdapter.ImageSentViewHolder(view);
            case SAConstants.Keys.TYPE_RECIEVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_received_image, parent, false);
                return new LiveStreamChatAdapter.ImageReceivedViewHolder(view);
            case SAConstants.Keys.TYPE_PINED_SEND_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_pin_message, parent, false);
                return new LiveStreamChatAdapter.PinnedSendViewHolder(view);
            case SAConstants.Keys.TYPE_PINED_RECEIVED_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_pin_message, parent, false);
                return new LiveStreamChatAdapter.PinnedReceivedViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e("onBindViewHolderLive","count");

        if (messagelist.get(position).get("senderid").equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {

            if (messagelist.get(position).get("type").equalsIgnoreCase("o")) {

                handleOfferData(holder, position);
//                handleSentMessageData(holder, position);
            } else if (messagelist.get(position).get("type").equalsIgnoreCase("img")) {

                handleSentImageData(holder, position);

            }/* else if (messagelist.get(position).get("type").equalsIgnoreCase("p")) {

                handlePinedMessageSent(holder, position);

            }*/ else {
                handleSentMessageData(holder, position);
            }
        } else {

            if (messagelist.get(position).get("type").equalsIgnoreCase("o")) {
//                handleRecievMessageData(holder, position);
                handleOfferDataReceived(holder, position);
//
            } else if (messagelist.get(position).get("type").equalsIgnoreCase("img")) {

                handleRecievedImageData(holder, position);

            }/* else if (messagelist.get(position).get("type").equalsIgnoreCase("p")) {

                handlePinedMessageRecieved(holder, position);

            }*/ else {
                handleRecievMessageData(holder, position);
            }
        }

        if (position == messagelist.size() - 1) {

            //  onBottomReachedListener.onBottomReached(true);

        } else {

            //   onBottomReachedListener.onBottomReached(false);

        }
//        notifyDataSetChanged();
    }

    private void handlePinedMessageRecieved(RecyclerView.ViewHolder holder, int position) {
        ((PinnedReceivedViewHolder) holder).txtTitle.setText("Title :-" + messagelist.get(position).get("product_name"));
        ((PinnedReceivedViewHolder) holder).txtDescription.setText("Description :-" + messagelist.get(position).get("product_description"));
        ((PinnedReceivedViewHolder) holder).txtCategory.setText("Category :-" + messagelist.get(position).get("product_category"));
    }

    private void handlePinedMessageSent(RecyclerView.ViewHolder holder, int position) {
        ((PinnedSendViewHolder) holder).txtTitle.setText("Title :-" + messagelist.get(position).get("product_name"));
        ((PinnedSendViewHolder) holder).txtDescription.setText("Description :-" + messagelist.get(position).get("product_description"));
        ((PinnedSendViewHolder) holder).txtCategory.setText("Category :-" + messagelist.get(position).get("product_category"));
    }

    private void handleOfferDataReceived(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context)
                .load(messagelist.get(position).get("sender_image"))
                .apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).userImage);
        if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
        } else {
            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
        }
        ((RecievedMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).get("message"));
        isClicedR = false;
/*        ((RecievedMessageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
//                    ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                   *//* pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isClicedR = true;
                            messagelist.get(position).put("ispined", "n");
                            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });*//*
//                    isClicedR = false;
                } else {
                   *//* pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isClicedR = true;
                            messagelist.get(position).put("ispined", "y");
                            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to remove comment from bookmark at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });*//*

                }
            }
        });*/
    }

    private void handleRecievMessageData(RecyclerView.ViewHolder holder, int position) {
        isClicedR = false;
     /*   if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
        } else {
            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
        }*/
        ((RecievedMessageViewHolder) holder).txtMsgTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(messagelist.get(position).get("created_at"))));

        if (messagelist.get(position).get("owner_id")!=null&&HelperPreferences.get(context).getString(UID).equalsIgnoreCase(messagelist.get(position).get("owner_id")))
        {
//            ((RecievedMessageViewHolder) holder).imgOption.setVisibility(View.VISIBLE);



           ((RecievedMessageViewHolder) holder).img_sendOfferIcon.setVisibility(View.VISIBLE);

             Log.e("LongPress", "handleRecievMessageData: " );
            ((RecievedMessageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    PopupMenu popup = new PopupMenu(context, ((RecievedMessageViewHolder) holder).imgOption);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_livechat, popup.getMenu());
                    popup.getMenu().findItem(R.id.menu_make_offer).setVisible(true);
                    if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(false);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (isLogined(context)) {
                                switch (menuItem.getItemId()) {
                                    case R.id.menu_make_offer:
                                        Log.e("mkoffer", "onMenuItemClick: ");
                                        makeOfferController.onMakeOfferClicked(messagelist.get(position).get("comment_id"), messagelist.get(position).get("senderid"));
                                        break;
                                    case R.id.menu_add_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "y");
                                            }
                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to add comment as bookmark at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_remove_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "n");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to remove comment from bookmark the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_cancel:
                                        popup.dismiss();
                                        break;
                                    case R.id.menu_pin_comment:
                                        Log.e("pinComment", "onMenuItemClick: ");
                                        makeOfferController.onPinLiveCommentClick(messagelist.get(position).get("comment_id"));
                                        break;
                                }
                            } else {
                                S_Dialogs.getLoginDialog(context).show();
                            }
                            return false;
                        }
                    });


                }
            });
           /* ((RecievedMessageViewHolder) holder).imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, ((SendMessageViewHolder) holder).imgoptions);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_livechat, popup.getMenu());
                    if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(false);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (isLogined(context)) {
                                switch (menuItem.getItemId()) {
                                    case R.id.menu_make_offer:
                                        Log.e("mkoffer", "onMenuItemClick: ");
                                        makeOfferController.onMakeOfferClicked(messagelist.get(position).get("comment_id"));
                                        break;
                                    case R.id.menu_add_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "y");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to add comment as bookmark at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_remove_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "n");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to remove comment from bookmark the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_cancel:
                                        popup.dismiss();
                                        break;
                                }
                            } else {
                                S_Dialogs.getLoginDialog(context).show();
                            }

                            return false;
                        }
                    });
                }
            });*/
        } else {
            ((RecievedMessageViewHolder) holder).img_sendOfferIcon.setVisibility(View.GONE);
            Log.e("LongPress", "handleRecievMessageData: negative");
//            ((RecievedMessageViewHolder) holder).imgOption.setVisibility(View.GONE);
        }
        /*((RecievedMessageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
//                    ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
//                    isClicedR = false;
                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isClicedR = true;
                            messagelist.get(position).put("ispined", "n");
                            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isClicedR = true;
                            messagelist.get(position).put("ispined", "y");
                            ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });
                   *//* isClicedR=true;
                    ((RecievedMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);*//*
                }
            }
        });*/
        ((LiveStreamChatAdapter.RecievedMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).get("message"));

        Glide.with(context)
                .load(messagelist.get(position).get("sender_image"))
                .apply(Global.getGlideOptions())
                .into(((RecievedMessageViewHolder) holder).userImage);

//        ((RecievedMessageViewHolder) holder).userImage.setImageResource(messagelist.get(position).getImageId());
       /* ((RecievedMessageViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.OTHER_USER_ID, messagelist.get(position).getSenderId());
                PersonalProfileFragment fragment=new PersonalProfileFragment();
                fragment.setArguments(bundle);
                ((ChatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();
            }
        });*/

    }

    boolean isCliced = false;

    private void handleSentMessageData(RecyclerView.ViewHolder holder, int position) {

        isCliced = false;

        if (messagelist.get(position).get("owner_id")!=null&&messagelist.get(position).get("owner_id").equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
//            ((SendMessageViewHolder) holder).imgoptions.setVisibility(View.VISIBLE);
            Log.e("longSent", "handleSentMessageData: " );
            ((SendMessageViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popup = new PopupMenu(context, ((SendMessageViewHolder) holder).imgoptions);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_livechat, popup.getMenu());
                    popup.getMenu().findItem(R.id.menu_make_offer).setVisible(false);
                    if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(false);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (isLogined(context)) {
                                switch (menuItem.getItemId()) {
                                    case R.id.menu_pin_comment:
                                        Log.e("pinComment", "onMenuItemClick: ");
                                        makeOfferController.onPinLiveCommentClick(messagelist.get(position).get("comment_id"));
                                        break;
                                    case R.id.menu_make_offer:
                                        makeOfferController.onMakeOfferClicked(messagelist.get(position).get("comment_id"),messagelist.get(position).get("senderid"));
                                        break;
                                    case R.id.menu_add_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "y");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_remove_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "n");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_cancel:
                                        popup.dismiss();
                                        break;
                                }
                            } else {
                                S_Dialogs.getLoginDialog(context).show();
                            }

                            return false;
                        }
                    });
                    return false;
                }
            });
           /* ((SendMessageViewHolder) holder).imgoptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, ((SendMessageViewHolder) holder).imgoptions);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_livechat, popup.getMenu());
                    if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.menu_add_bookmark).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_remove_bookmark).setVisible(false);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (isLogined(context)) {
                                switch (menuItem.getItemId()) {
                                    case R.id.menu_make_offer:
                                        makeOfferController.onMakeOfferClicked(messagelist.get(position).get("comment_id"));
                                        break;
                                    case R.id.menu_add_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "y");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_remove_bookmark:
                                        pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                                            @Override
                                            public void onCommentPinSuccess() {
                                                isCliced = true;
                                                messagelist.get(position).put("ispined", "n");
                                            }

                                            @Override
                                            public void onCommentPinUnSuccess() {
                                                Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case R.id.menu_cancel:
                                        popup.dismiss();
                                        break;
                                }
                            } else {
                                S_Dialogs.getLoginDialog(context).show();
                            }

                            return false;
                        }
                    });
                }
            });*/
        } else {
            Log.e("longSent", "handleSentMessageData: negative" );
//            ((SendMessageViewHolder) holder).imgoptions.setVisibility(View.VISIBLE);
        }


       /* ((SendMessageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
//                    isCliced = false;
//                    ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isCliced = true;
                            messagelist.get(position).put("ispined", "n");
                            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
                            isCliced = true;
                            messagelist.get(position).put("ispined", "y");
                            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });*/
        ((LiveStreamChatAdapter.SendMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).get("message"));
        ((SendMessageViewHolder) holder).txtMsgTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(messagelist.get(position).get("created_at"))));

        Glide.with(context).load(HelperPreferences.get(context).getString(USER_PROFILE_PIC))
                .apply(Global.getGlideOptions())
                .into(((SendMessageViewHolder) holder).userImage);/*       */

        /*((SendMessageViewHolder) holder).imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(context);
                return imageView;
            }
        });
        ((SendMessageViewHolder) holder).imageSwitcher.setInAnimation(context, android.R.anim.fade_in);
        ((SendMessageViewHolder) holder).imageSwitcher.setOutAnimation(context, android.R.anim.fade_out);

        ((SendMessageViewHolder) holder).imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCliced){
                    ((SendMessageViewHolder) holder).imageSwitcher.setBackgroundResource(IMAGES[1]);
                    isCliced=false;
                }else{
                    isCliced=true;
                    ((SendMessageViewHolder) holder).imageSwitcher.setBackgroundResource(IMAGES[0]);
                }

            }
        });*/

    }
    private void handleSentImageData(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(messagelist.get(position).get("image_url")).apply(Global.getGlideOptions()).into(((ImageSentViewHolder) holder).imgSent);
        Glide.with(context).load(messagelist.get(position).get("sender_image"))
                .apply(Global.getGlideOptions())
                .into(((ImageSentViewHolder) holder).userImage);
    }
    private void handleRecievedImageData(RecyclerView.ViewHolder holder, int position) {

        Glide.with(context).load(messagelist.get(position).get("image_url")).apply(Global.getGlideOptions()).into(((ImageReceivedViewHolder) holder).imgSent);
        Glide.with(context)
                .load(messagelist.get(position).get("sender_image"))
                .apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).userImage);
        ((LiveStreamChatAdapter.ImageReceivedViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.OTHER_USER_ID, messagelist.get(position).get("senderid"));
                PersonalProfileFragment fragment=new PersonalProfileFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();
          */
            }
        });

    }

    private void handleOfferData(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(HelperPreferences.get(context).getString(USER_PROFILE_PIC))
                .apply(Global.getGlideOptions())
                .into(((SendMessageViewHolder) holder).userImage);
        ((SendMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).get("message"));
        if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
        } else {
            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
        }
        isCliced = false;
        ((SendMessageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messagelist.get(position).get("ispined") != null && messagelist.get(position).get("ispined").equalsIgnoreCase("y")) {
//                     isCliced = false;
//                    ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "n", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
//                            isCliced = true;
                            messagelist.get(position).put("ispined", "n");
                            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[0]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    pinCommentApi(messagelist.get(position).get("comment_id"), messagelist.get(position).get("group_id"), "y", new PinUnPinCommentCallback() {
                        @Override
                        public void onCommentPinSuccess() {
//                            isCliced = true;
                            messagelist.get(position).put("ispined", "y");
                            ((SendMessageViewHolder) holder).imagePin.setImageResource(IMAGES[1]);
                        }

                        @Override
                        public void onCommentPinUnSuccess() {
                            Toast.makeText(context, "Unable to pin comment at the movement.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

/*
        if (messagelist.get(position).get("senderid").equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {

        } else {

        }
        */
    }

    @Override
    public int getItemViewType(int position) {
//        Log.e("Dscvdsfvc ", messagelist + "");
//        Log.e("Dscvdsfvc ", HelperPreferences.get(context).getString(UID) + "");
        if (messagelist.get(position).get("senderid").equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            if (messagelist.get(position).get("type").equalsIgnoreCase("o")) {

                return SAConstants.Keys.TYPE_MAKEOFFER;

            } else if (messagelist.get(position).get("type").equalsIgnoreCase("img")) {

                return SAConstants.Keys.TYPE_IMAGE;

            } else if (messagelist.get(position).get("type").equalsIgnoreCase("p")) {
                return SAConstants.Keys.TYPE_PINED_SEND_MSG;
            } else {
                return SAConstants.Keys.TYPE_SEND;
            }
        } else {
            if (messagelist.get(position).get("type").equalsIgnoreCase("o")) {

                return SAConstants.Keys.TYPE_MAKEOFFER_RECEIVED;

            } else if (messagelist.get(position).get("type").equalsIgnoreCase("img")) {

                return SAConstants.Keys.TYPE_RECIEVED_IMAGE;

            } else if (messagelist.get(position).get("type").equalsIgnoreCase("p")) {
                return SAConstants.Keys.TYPE_PINED_RECEIVED_MSG;
            } else {
                return SAConstants.Keys.TYPE_RECEIVED;
            }
        }

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    private String formateDate(String dateToChange) {

        String formattedDate = "";
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = readFormat.parse(dateToChange);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }

   /* public void showAcceptRejectoption(LiveStreamChatAdapter.MakeOfferViewHolder holder) {

        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.VISIBLE);
        (holder).liPay.setVisibility(View.GONE);

    }

    public void showStatus(LiveStreamChatAdapter.MakeOfferViewHolder holder, String status, int colorId) {
        (holder).txtCanceled.setVisibility(View.VISIBLE);
        (holder).txtCanceled.setText(status);
        (holder).txtCanceled.setTextColor(context.getResources().getColor(colorId));
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.GONE);
    }

    public void showPayOptions(LiveStreamChatAdapter.MakeOfferViewHolder holder) {
        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.VISIBLE);
    }*/

   /* public void showOfferStatusSenderSide(LiveStreamChatAdapter.MakeOfferViewHolder holder, String status, int pos) {
        if (status.equalsIgnoreCase("p")) {
            showStatus(holder, "Pending", R.color.colorGrey);
        } else if (status.equalsIgnoreCase("a")) {
            showPayOptions(holder);
        } else if (status.equalsIgnoreCase("s")) {
            showStatus(holder, "Purchased", R.color.colorRed);
        } else {
            showStatus(holder, "Rejected", R.color.colorGrey);
        }
    }*/

    /*public void showOfferStatusReceiverSide(LiveStreamChatAdapter.MakeOfferViewHolder holder, String status, int pos) {
        if (status.equalsIgnoreCase("p")) {
            showAcceptRejectoption(holder);
        } else if (status.equalsIgnoreCase("a")) {
            showStatus(holder, "Accepted", R.color.colorRed);
        } else if (status.equalsIgnoreCase("s")) {
            showStatus(holder, "Sold", R.color.colorRed);
        } else {
            showStatus(holder, "Rejected", R.color.colorGrey);
        }
    }*/

    /*private void acceptDeclineApi(LiveStreamChatAdapter.MakeOfferViewHolder holder, String offerId, String status, int pos) {
        Log.e("OfferId", "acceptDeclineApi: " + offerId + " : ");
        Call<MakeOfferModel> acceptDeclineCall = service.acceptDeclineOfferApi(HelperPreferences.get(context).getString(UID), offerId, status);
        acceptDeclineCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    Log.e("AcceptDecline", "onResponse: " + response.body().getResult().getStatus());
                    showOfferStatusReceiverSide(holder, response.body().getResult().getStatus(), pos);
                } else {
                    try {
                        Log.e("AcceptDecline", "onResponsefaild: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {
                Log.e("AcceptDecline", "onResponsefaild: " + t.getMessage());
            }
        });
    }
*/
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
        }
    }

    private void pinCommentApi(String commentId, String groupId, String pinStatus, PinUnPinCommentCallback callback) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> pinCommentApi = Global.WebServiceConstants.getRetrofitinstance().pinCommentApi(HelperPreferences.get(context).getString(UID),
                groupId, commentId, pinStatus.toUpperCase());
        pinCommentApi.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    callback.onCommentPinSuccess();
                    Log.e("pinCommentApi", "onResponse: " + response.body().toString());
                } else {
                    callback.onCommentPinUnSuccess();
                    try {
                        Log.e("pinCommentApiError", "onResponse: " + response.errorBody().string());
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
                callback.onCommentPinUnSuccess();
                Log.e("pinCommentApiFailure", "onFailure: " + t.getMessage());
            }
        });
    }

    private interface PinUnPinCommentCallback {
        void onCommentPinSuccess();
        void onCommentPinUnSuccess();
    }
    public interface OptionsClickCallBack {
        void onMakeOfferClicked(String comment_id, String senderId);
        void onPinLiveCommentClick(String commentId);
    }
}
