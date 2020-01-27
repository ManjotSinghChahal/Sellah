package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.stripe.StripeApp;
import com.app.admin.sellah.controller.stripe.StripeButton;
import com.app.admin.sellah.controller.stripe.StripeConnectListener;
import com.app.admin.sellah.controller.utils.ChatActivityController;
import com.app.admin.sellah.controller.utils.Prodctfragment_click;
import com.app.admin.sellah.controller.utils.RecyclerViewClickListener;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.ChatModel.ChatMessageModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.ProductDetails.ProductDetailModel;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;
import com.app.admin.sellah.view.CustomDialogs.Stripe_image_verification_dialogfragment;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.ImageViewerActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ChatDetailFragment;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.Chat_User_Data;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ChatAdapter extends RecyclerView.Adapter {


    List<ChatMessageModel> messagelist;
    Context context;
    private RecyclerViewClickListener onBottomReachedListener;
    private WebService service;
    private StripeApp StripeAppmApp;
    private int currentFlag = 0;
    private String user_id= "";
    private String reciever_d= "";
    ChatActivityController showpay;
    String progress_status;
    Dialog dialog;


    String friendname;
    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        LinearLayout ll_sendchat;
        TextView txtMessage, txtMsgTime;
        ImageView read_tick;

        public SendMessageViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.read_tick = itemView.findViewById(R.id.sender_tick);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);
            this.ll_sendchat = itemView.findViewById(R.id.ll_sendmsg);
        }
    }


    public static class RecievedMessageViewHolder extends RecyclerView.ViewHolder {


        CircleImageView userImage;
        TextView txtMessage, txtMsgTime;
        LinearLayout ll_recmsg;
        public RecievedMessageViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);
            this.ll_recmsg = itemView.findViewById(R.id.ll_recmsg);

        }
    }


    public static class TodayViewHolder extends RecyclerView.ViewHolder {

        TextView today;

        public TodayViewHolder(View itemView) {
            super(itemView);

            this.today = itemView.findViewById(R.id.today);


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

    public static class MakeOfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemQuantity,dollarsign, txtItemCost, txtCanceled,aftertext_itemsendername,texitem_sendername,aftertxtItemName, aftertxtItemQuantity, aftertxtItemCost;
        Button btnAccept, btnCanceld, btnPay;
        LinearLayout liOffer, liPay, liOfferStatus,upperchat_layout,beforeaccept,afteraccept;
        ImageView productimage,productimage1;


        public MakeOfferViewHolder(View itemView) {
            super(itemView);
            this.aftertxtItemName = (TextView) itemView.findViewById(R.id.aftertxt_item_name);
            this.dollarsign = (TextView) itemView.findViewById(R.id.dollar_sign);
            this.aftertxtItemQuantity = (TextView) itemView.findViewById(R.id.aftertxt_item_quantity);
            this.aftertxtItemCost = (TextView) itemView.findViewById(R.id.aftertxt_item_cost);
            this.aftertext_itemsendername = (TextView) itemView.findViewById(R.id.aftertxt_item_sender_name);
            this.txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
            this.texitem_sendername = (TextView) itemView.findViewById(R.id.txt_item_sender_name);
            this.txtItemQuantity = (TextView) itemView.findViewById(R.id.txt_item_quantity);
            this.txtItemCost = (TextView) itemView.findViewById(R.id.txt_item_cost);
            this.txtCanceled = (TextView) itemView.findViewById(R.id.txt_offer_status);
            this.btnAccept = itemView.findViewById(R.id.btn_item_accept);
            this.btnPay = itemView.findViewById(R.id.btn_pay);
            this.btnCanceld = itemView.findViewById(R.id.btn_item_cancel);
            this.liOffer = itemView.findViewById(R.id.li_confirm_order);
            this.liPay = itemView.findViewById(R.id.li_pay_order);
            this.liOfferStatus = itemView.findViewById(R.id.li_offer_status);
            this.upperchat_layout = itemView.findViewById(R.id.uppperchatlayout);
            this.beforeaccept = itemView.findViewById(R.id.beforeacpt__offer__layout);
            this.afteraccept = itemView.findViewById(R.id.afteracpt_offerlayout);
            this.productimage = itemView.findViewById(R.id.img_product_image);
            this.productimage1 = itemView.findViewById(R.id.img_product_image1);

        }
    }

    private TextView txtReview;
    public ChatAdapter(List<ChatMessageModel> messageList, Context context,String progressStatus,Dialog dialog, TextView txtReview, ChatActivityController showpay) {

        this.messagelist = messageList;
        this.context = context;
        this.progress_status = progressStatus;
        this.dialog = dialog;
        this.txtReview=txtReview;
        this.showpay = showpay;
        onBottomReachedListener = new ChatActivity();
        service = Global.WebServiceConstants.getRetrofitinstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


/*        if (progress_status.equalsIgnoreCase("progressYes"))
        {
            if (dialog!=null && dialog.isShowing())
                dialog.dismiss();
        }*/

        View view;
        switch (viewType) {
            case SAConstants.Keys.TYPE_SEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_sent, parent, false);
                return new SendMessageViewHolder(view);
            case SAConstants.Keys.TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_recieved, parent, false);
                return new RecievedMessageViewHolder(view);
            case SAConstants.Keys.TYPE_MAKEOFFER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_make_offer, parent, false);  // Crash deteched by Manjot
                return new MakeOfferViewHolder(view);
            case SAConstants.Keys.TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_send_image, parent, false);
                return new ImageSentViewHolder(view);
            case SAConstants.Keys.TYPE_RECIEVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_received_image, parent, false);
                return new ImageReceivedViewHolder(view);

            case 101:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_today_layout, parent, false);
                return new TodayViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {




          friendname = ((ChatActivity)context).txtUserName.getText().toString();
        //  user_id = messagelist.get(position).getSenderId();
          reciever_d = messagelist.get(position).getReceiverId();



          /* Conditon to add date messge  in the chat list  */
        if (messagelist.get(position).isToday_boolean())
        {
            ((TodayViewHolder)holder).today.setText(messagelist.get(position).getToday());
        }
        else
        {

            if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {

                if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                    /*Handle data according to offer */
                    handleOfferData(holder, position);

                } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {
                      /* Handle data according to image */
                    handleSentImageData(holder, position);

                } else {


                    /* Handle messages data */
                    handleSentMessageData(holder, position);
                }
            } else {
                if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                    handleOfferData(holder, position);

                } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                    handleRecievedImageData(holder, position);

                } else {
                    handleRecievMessageData(holder, position);
                }
            }

        }


       /* if (messagelist.get(position).isSender()) {


            if (messagelist.get(position).isOffer()) {

            } else if (messagelist.get(position).isSent()) {
                if (messagelist.get(position).isImage()) {

                } else {

                }
            } else {
                if (messagelist.get(position).isImage()) {
                    handleSentImageData(holder, position);
                } else {
                    handleRecievMessageData(holder, position);
                }

            }
        }
       */
        if (position == messagelist.size() - 1) {

            onBottomReachedListener.onBottomReached(true);

        } else {

            onBottomReachedListener.onBottomReached(false);

        }
//        notifyDataSetChanged();
    }



    private void handleRecievMessageData(RecyclerView.ViewHolder holder, int position)
    {

        if (position==0)
        {
            ((RecievedMessageViewHolder) holder).ll_recmsg.setBackgroundResource(R.drawable.recchat1);


        }


        else
        {

            if (position+1<messagelist.size())
            {

                if (!messagelist.get(position-1).getReceiverId().equals(reciever_d))
                {
                    ((RecievedMessageViewHolder) holder).ll_recmsg.setBackgroundResource(R.drawable.recchat1);
                }
                else if (!messagelist.get(position+1).getReceiverId().equals(reciever_d))
                {
                    ((RecievedMessageViewHolder) holder).ll_recmsg.setBackgroundResource(R.drawable.recchat3);
                }
                else
                {
                    ((RecievedMessageViewHolder) holder).ll_recmsg.setBackgroundResource(R.drawable.recchat2);
                }

            }
            else
            {
                ((RecievedMessageViewHolder) holder).ll_recmsg.setBackgroundResource(R.drawable.recchat3);
            }




        }

        ((RecievedMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).getMessage());
        ((RecievedMessageViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultingIntent = new Intent(context, MainActivity.class);
                resultingIntent.putExtra(Chat_User_Data, messagelist.get(position).getSenderId());
                ((ChatActivity) context).startActivity(resultingIntent);
            }
        });

//        ((RecievedMessageViewHolder) holder).userImage.setImageResource(messagelist.get(position).getImageId());
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((RecievedMessageViewHolder) holder).userImage);
        ((RecievedMessageViewHolder) holder).txtMsgTime.setText(formateDate(Global.convertUTCToLocal(messagelist.get(position).getCreatedAt())));
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

    private void handleSentMessageData(RecyclerView.ViewHolder holder, int position) {

          /* Condition to show read unread ticks images*/
        if (messagelist.get(position).getMessage_read_status()!=null)
        {

            if (messagelist.get(position).getMessage_read_status().equalsIgnoreCase("Y"))
            {
                ((SendMessageViewHolder) holder).read_tick.setImageResource(R.drawable.red_tick);
            }
            else
            {
                ((SendMessageViewHolder) holder).read_tick.setImageResource(R.drawable.grey_tick);
            }
        }

            if (position==0)
            {
                ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat1);

            }


            else
            {
                /* condition to check the last message */
                if (position+1<messagelist.size())
                {
                    if (!messagelist.get(position-1).getSenderId().equals(user_id))
                    {
                        ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat1);
                    }
                    else if (!messagelist.get(position+1).getSenderId().equals(user_id))
                    {
                        ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat3);
                    }
                    else
                    {
                        ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat2);
                    }

                }
                else
                {
                    ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat3);
                }




            }
      /*  if(currentFlag==0)
        {
            ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat1);
        }
        else if(currentFlag==2)
        {
            ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat3);

        }
        else
        {
            ((SendMessageViewHolder) holder).ll_sendchat.setBackgroundResource(R.drawable.sendchat2);


        }*/



       // if (messagelist.get(position).getSenderId().equals(messagelist.get(getItemCount())))



        ((SendMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).getMessage());
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((SendMessageViewHolder) holder).userImage);
//        ((SendMessageViewHolder) holder).userImage.setImageResource(messagelist.get(position).getImageId());
        ((SendMessageViewHolder) holder).txtMsgTime.setText(formateDate(Global.convertUTCToLocal(messagelist.get(position).getCreatedAt())));

    }

     private void handleSentImageData(RecyclerView.ViewHolder holder, int position) {


        Glide.with(context).load(messagelist.get(position).getImageUrl()).apply(Global.getGlideOptions())
                .into(((ImageSentViewHolder) holder).imgSent);
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((ImageSentViewHolder) holder).userImage);
        ((ImageSentViewHolder) holder).imgSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewerActivity.start(context, messagelist.get(position).getImageUrl());
            }
        });
    }

    private void handleRecievedImageData(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(messagelist.get(position).getImageUrl()).apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).imgSent);
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).userImage);
        ((ImageReceivedViewHolder) holder).imgSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewerActivity.start(context, messagelist.get(position).getImageUrl());
            }
        });
        ((ImageReceivedViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultingIntent = new Intent(context, MainActivity.class);
                resultingIntent.putExtra(Chat_User_Data, messagelist.get(position).getSenderId());
                ((ChatActivity) context).startActivity(resultingIntent);
            }
        });
    }

    private void StripConnect(MakeOfferViewHolder holder, int position) {
        StripeAppmApp = new StripeApp(context, "geniusAppDeveloper", "ca_EWK1BYRqruSX1X92DbtLY8UiV46ADGoC",
                "sk_test_HDkDbhty58uz3aaJi2TDllrR", "https://developer.android.com", "read_write");
//        mStripeButton = (StripeButton) findViewById(R.id.btnStripeConnect);
        StripeButton btnStripeConnect = new StripeButton(context);
        btnStripeConnect.setStripeApp(StripeAppmApp);
        btnStripeConnect.setConnectMode(StripeApp.CONNECT_MODE.DIALOG);
        btnStripeConnect.addStripeConnectListener(new StripeConnectListener() {

            @Override
            public void onConnected() {
                Log.e("Connected_as", "onConnected: " + StripeAppmApp.getUserId());
                if (StripeAppmApp.getUserId() != null) {

                    new ApisHelper().linkStripApi(context, StripeAppmApp.getUserId(), new ApisHelper.StripeConnectCallback() {
                        @Override
                        public void onStripeConnectSuccess(String msg) {
                            showpay.updateSubTotal("fg");

                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            HelperPreferences.get(context).saveString(API_ACCESS_TOKEN, StripeAppmApp.getUserId());
                            acceptDeclineApi((holder), messagelist.get(position).getOfferId(), "a", position);
//                            txtConnectionStatus.setVisibility(View.GONE);
                        }

                        @Override
                        public void onStripeConnectFailure() {
                            Toast.makeText(context, "Unable to link account with stripe at this movements", Toast.LENGTH_SHORT).show();
            /*                Snackbar.make(context.getWindow().getDecorView(), , Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();*/

                        }
                    });
                }
//                tvSummary.setText("Connected as " + StripeAppmApp.getAccessToken());
            }

            @Override
            public void onDisconnected() {
                Log.e("Disconnected", "Disconnected: ");
//                tvSummary.setText("Disconnected");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }

        });
        btnStripeConnect.performClick();

    }

    private void handleOfferData(RecyclerView.ViewHolder holder, int position) {

        ((MakeOfferViewHolder) holder).txtItemName.setText(messagelist.get(position).getProductName());



        try {
            if (!TextUtils.isEmpty(messagelist.get(position).getQuantity())) {
                ((MakeOfferViewHolder) holder).txtItemQuantity.setText(messagelist.get(position).getQuantity());
            } else {
                ((MakeOfferViewHolder) holder).txtItemQuantity.setText("1");
            }
        } catch (Exception e) {
            ((MakeOfferViewHolder) holder).txtItemQuantity.setText("1");
        }

         ((MakeOfferViewHolder) holder).txtItemCost.setText(messagelist.get(position).getProductCost());


        Log.e("status: ",  messagelist.get(position).getStatus());
        if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            showOfferStatusSenderSide(((MakeOfferViewHolder) holder), messagelist.get(position).getStatus(), position);
            ((MakeOfferViewHolder) holder).btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentDialog.create(context,messagelist.get(position).getProductCost(), messagelist.get(position).getOfferId(), messagelist.get(position).getReceiverId(), messagelist.get(position).getProductId(), "", "", new PaymentDialog.PaymentCallBack() {
                        @Override
                        public void onPaymentSuccess() {
                            messagelist.get(position).setStatus("s");
                            notifyDataSetChanged();
                            if(txtReview!=null){
                                txtReview.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onPaymentFail(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelDialog() {

                        }
                    }).show();
                }
            });

        } else {
            showOfferStatusReceiverSide(((MakeOfferViewHolder) holder), messagelist.get(position).getStatus(), position);
            ((MakeOfferViewHolder) holder).btnCanceld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptDeclineApi(((MakeOfferViewHolder) holder), messagelist.get(position).getOfferId(), "r", position);
                }
            });
            ((MakeOfferViewHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e( "onClick: ","ff"+ HelperPreferences.get(context).getString(API_ACCESS_TOKEN));



                    if (HelperPreferences.get(context).getString(STRIPE_VERIFIED).equals("")||HelperPreferences.get(context).getString(STRIPE_VERIFIED).equals("N")) {

                        S_Dialogs.getStipeConnectDialog(context, ((dialog, which) -> {

                            Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
                            stripe_dialogfragment.show(((ChatActivity)context).getFragmentManager(),"");
                          //  StripConnect((MakeOfferViewHolder) holder, position);
                        })).show();

                    }
                    else if ((HelperPreferences.get(context).getString(STRIPE_VERIFIED).equalsIgnoreCase("P")))
                    {

                        S_Dialogs.getLiveVideoStopedDialog(context, "You have not uploaded you Idenitification Documents. Press ok to upload.", ((dialog, which) -> {
                            //--------------openHere-----------------

                            Stripe_image_verification_dialogfragment stripe_dialogfragment = new Stripe_image_verification_dialogfragment();
                            stripe_dialogfragment.show(((ChatActivity)context).getFragmentManager(),"");

                        })).show();
                    }
                    else {


                        acceptDeclineApi(((MakeOfferViewHolder) holder), messagelist.get(position).getOfferId(), "a", position);

                }
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {


            if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {


                if (messagelist.get(position).isToday_boolean())
                {
                    return 101;
                }

                if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                    return SAConstants.Keys.TYPE_MAKEOFFER;

                } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                    return SAConstants.Keys.TYPE_IMAGE;

                } else {
                    return SAConstants.Keys.TYPE_SEND;
                }
            } else {

                if (messagelist.get(position).isToday_boolean())
                {
                    return 101;
                }

                if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                    return SAConstants.Keys.TYPE_MAKEOFFER;

                } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                    return SAConstants.Keys.TYPE_RECIEVED_IMAGE;

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




    public void showAcceptRejectoption(MakeOfferViewHolder holder) {

        // new changes vishal
        holder.beforeaccept.setVisibility(View.VISIBLE);
        holder.afteraccept.setVisibility(View.GONE);
        (holder).upperchat_layout.setBackgroundResource(R.drawable.recchat2);
        (holder).texitem_sendername.setVisibility(View.VISIBLE);
        (holder).texitem_sendername.setText(friendname +" has sent you an offer");
        (holder).dollarsign.setTextColor(context.getResources().getColor(R.color.colorWhite));
        (holder).texitem_sendername.setTextColor(context.getResources().getColor(R.color.colorWhite));
        (holder).txtItemName.setTextColor(context.getResources().getColor(R.color.colorWhite));
        (holder).txtItemCost.setTextColor(context.getResources().getColor(R.color.colorWhite));
        (holder).txtItemQuantity.setTextColor(context.getResources().getColor(R.color.colorWhite));

        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.VISIBLE);
        (holder).liPay.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.beforeaccept.getLayoutParams();
        params.setMargins(23, 0, 100, 0);
        holder.beforeaccept.setLayoutParams(params);

    }

    public void showStatus(MakeOfferViewHolder holder, String status, int colorId) {


        holder.beforeaccept.setVisibility(View.GONE);
        holder.afteraccept.setVisibility(View.VISIBLE);
        holder.aftertxtItemName.setText(messagelist.get(holder.getAdapterPosition()).getProductName());
        holder.aftertxtItemQuantity.setText(messagelist.get(holder.getAdapterPosition()).getQuantity());
        (holder).txtCanceled.setVisibility(View.VISIBLE);
        (holder).txtCanceled.setText(status);
        (holder).txtCanceled.setTextColor(context.getResources().getColor(colorId));
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.GONE);

     //   holder.aftertxtItemCost.setText(messagelist.get(holder.getAdapterPosition()).getProductCost());

        try {

            if (messagelist.get(holder.getAdapterPosition()).getProductCost().contains("$"))
            {

                String amt = messagelist.get(holder.getAdapterPosition()).getProductCost().replace("$","").trim();
                Double prc = Double.parseDouble(amt);
                String formattedStr = String.format("%.2f",prc);
                ((MakeOfferViewHolder) holder).aftertxtItemCost.setText(formattedStr);

            }
            else
            {

                Double prc = Double.parseDouble(messagelist.get(holder.getAdapterPosition()).getProductCost());
                String formattedStr = String.format("%.2f",prc);
                ((MakeOfferViewHolder) holder).aftertxtItemCost.setText(formattedStr);
            }
        }catch (Exception e){

        }
    }

    public void showPayOptions(MakeOfferViewHolder holder) {



         holder.texitem_sendername.setVisibility(View.GONE);
        holder.beforeaccept.setVisibility(View.VISIBLE);
        holder.afteraccept.setVisibility(View.GONE);
        (holder).upperchat_layout.setBackgroundResource(R.drawable.chat_upper_layout);
        (holder).dollarsign.setTextColor(context.getResources().getColor(R.color.colorRed));
        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.beforeaccept.getLayoutParams();
        params.setMargins(50, 0, 50, 0);
        holder.beforeaccept.setLayoutParams(params);


    }

    public void showOfferStatusSenderSide(MakeOfferViewHolder holder, String status, int pos) {

        Log.e( "productimage: ",messagelist.get(pos).getProduct_image() );
        Glide.with(context).load(messagelist.get(pos).getProduct_image()).apply(Global.getGlideOptions())
                .into(((MakeOfferViewHolder) holder).productimage);
        Glide.with(context).load(messagelist.get(pos).getProduct_image()).apply(Global.getGlideOptions())
                .into(((MakeOfferViewHolder) holder).productimage1);

        if (status.equalsIgnoreCase("p")) {

            holder.aftertext_itemsendername.setText("Sending offer to "+friendname);

            showStatus(holder, "Pending", R.color.colorWhite);
        } else if (status.equalsIgnoreCase("a")) {

            holder.aftertext_itemsendername.setText("Your offer has been accepted by "+friendname);
            showStatus(holder, "accepted", R.color.colorWhite);
           // showPayOptions(holder);
        } else if (status.equalsIgnoreCase("s")) {

            holder.aftertext_itemsendername.setText("Product successfully purchased from "+friendname);
            showStatus(holder, "Purchased", R.color.colorWhite);
        }

        else if (status.equalsIgnoreCase("YRF")) {

            holder.aftertext_itemsendername.setText("Amount successfully refunded in sellah wallet");
            showStatus(holder, "Purchased", R.color.colorWhite);
        }
        else if (status.equalsIgnoreCase("NRF")) {

            holder.aftertext_itemsendername.setText("Dispute request declined by the admin");
            showStatus(holder, "Purchased", R.color.colorWhite);
        }


        else {
            holder.aftertext_itemsendername.setText("Offer rejected "+friendname);
            showStatus(holder, "Rejected", R.color.colorWhite);
        }
    }

    public void showOfferStatusReceiverSide(MakeOfferViewHolder holder, String status, int pos) {

        Glide.with(context).load(messagelist.get(pos).getProduct_image()).apply(Global.getGlideOptions())
                .into(((MakeOfferViewHolder) holder).productimage);
        Glide.with(context).load(messagelist.get(pos).getProduct_image()).apply(Global.getGlideOptions())
                .into(((MakeOfferViewHolder) holder).productimage1);
        Log.e( "recieveeroffer: ",status );
        if (status.equalsIgnoreCase("p")) {
            showAcceptRejectoption(holder);
        } else if (status.equalsIgnoreCase("a")) {
            showpay.updateSubTotal("offer");
            holder.aftertext_itemsendername.setText("You accepted "+friendname+"'s"+" offer");
            showStatus(holder, "Accepted", R.color.colorWhite);
        } else if (status.equalsIgnoreCase("s")) {
            holder.aftertext_itemsendername.setText("Product successfully sold to "+friendname);
            showStatus(holder, "Sold", R.color.colorWhite);
        } else {
            holder.aftertext_itemsendername.setText("You rejected the offer from "+friendname);
            showStatus(holder, "Rejected", R.color.colorGrey);
        }
    }

    /* Api for accept offers and declined offers*/
    private void acceptDeclineApi(MakeOfferViewHolder holder, String offerId, String status, int pos) {


        Log.e("OfferId", "acceptDeclineApi: " + user_id + " : " + HelperPreferences.get(context).getString(UID));
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<MakeOfferModel> acceptDeclineCall = service.acceptDeclineOfferApi(HelperPreferences.get(context).getString(UID), messagelist.get(pos).getSenderId(), offerId, status);
        acceptDeclineCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    Log.e("AcceptDecline", "onResponse: " + response.body().getResult().getStatus());
                    messagelist.get(pos).setStatus(status);
                    notifyDataSetChanged();
//                    showOfferStatusReceiverSide(holder, response.body().getResult().getStatus(), pos);
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
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("AcceptDecline", "onResponsefaild: " + t.getMessage());
            }
        });
    }




}