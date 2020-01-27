package com.app.admin.sellah.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.PinCommentModel.PinCommentModel;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.SendOffer;
import com.app.admin.sellah.view.adapter.LiveStreamChatAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class StreamedVideoDetail extends Activity {

    EditText edt_message;
    ImageView btn_send;
    WebService service;
    @BindView(R.id.relLay_back_live)
    RelativeLayout relLayBackLive;
    @BindView(R.id.txt_productname)
    TextView txtProductname;
    @BindView(R.id.relLay_more_live)
    RelativeLayout relLayMoreLive;
    @BindView(R.id.txt_views)
    TextView txtViews;
    @BindView(R.id.relLay_views)
    RelativeLayout relLayViews;
    @BindView(R.id.txt_duration)
    TextView txtDuration;
    @BindView(R.id.relLay_duration)
    RelativeLayout relLayDuration;
    @BindView(R.id.txt_live)
    TextView txtLive;
    @BindView(R.id.relLay_live)
    RelativeLayout relLayLive;
    @BindView(R.id.relLayoutHeader)
    RelativeLayout relLayoutHeader;
    @BindView(R.id.recyclerview_livemodule_chat)
    RecyclerView recyclerviewLivemoduleChat;
    @BindView(R.id.img_user_rec)
    CircleImageView imgUserRec;
    @BindView(R.id.img_pin_rec)
    ImageView imgPinRec;
    @BindView(R.id.txt_category_rec)
    TextView txtCategoryRec;
    @BindView(R.id.txt_description_rec)
    TextView txtDescriptionRec;
    @BindView(R.id.lin_content)
    LinearLayout linContent;
    @BindView(R.id.card3)
    CardView card3;
    @BindView(R.id.card2)
    CardView card2;
    @BindView(R.id.txt_msg_time_rec)
    TextView txtMsgTimeRec;
    @BindView(R.id.li_pinned_received)
    LinearLayout liPinnedReceived;
    @BindView(R.id.txt_msg_time_sent)
    TextView txtMsgTimeSent;
    @BindView(R.id.img_unpin_comment)
    ImageView imgUnpinComment;
    @BindView(R.id.img_pin_sent)
    ImageView imgPinSent;
    @BindView(R.id.txt_category_sent)
    TextView txtCategorySent;
    @BindView(R.id.txt_description_sent)
    TextView txtDescriptionSent;
    @BindView(R.id.lin_content_sent)
    LinearLayout linContentSent;
    @BindView(R.id.card_sent)
    CardView cardSent;
    @BindView(R.id.card2_sent)
    CardView card2Sent;
    @BindView(R.id.img_user)
    CircleImageView imgUser;
    @BindView(R.id.li_pinned_sent)
    LinearLayout liPinnedSent;
    @BindView(R.id.rel_pinned_comment)
    RelativeLayout relPinnedComment;
    @BindView(R.id.img_send_camera)
    ImageView imgSendCamera;
    @BindView(R.id.img_send_gallery)
    ImageView imgSendGallery;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send_live)
    ImageView btnSendLive;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.card_bottom_view)
    RelativeLayout cardBottomView;

    private LiveStreamChatAdapter adapter;
    private ArrayList<HashMap<String, String>> models;
    private boolean isOffer = true;
    LiveStreamChatAdapter videoCategoriesAdpt;
    String showPublishPlay = "";
    private String streamId = "";
    private String groupId = "";
    private String productName = "";
    private String productId = "";
    private String sellerId = "";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private PopupMenu popup;
    private String videoStartTime = "";
    private String videoEndTime = "";
    private String videoViews = "";
    private String productname_1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_demo_activity);
        ButterKnife.bind(this);
        service = Global.WebServiceConstants.getRetrofitinstance();
        Intent intent = getIntent();
        showPublishPlay = intent.getStringExtra("value");
        streamId = intent.getStringExtra("id");
        productName = intent.getStringExtra("product_name");
        productId = intent.hasExtra("product_id") ? intent.getStringExtra("product_id") : "";
        groupId = intent.hasExtra("group_id") ? intent.getStringExtra("group_id") : "";
        sellerId = intent.hasExtra("seller_id") ? intent.getStringExtra("seller_id") : "";
        videoStartTime = intent.hasExtra("start_time") ? intent.getStringExtra("start_time") : "";
        videoEndTime = intent.hasExtra("end_time") ? intent.getStringExtra("end_time") : "";
        videoViews = intent.hasExtra("views") ? intent.getStringExtra("views") : "";
//        txtViews.setText(videoViews);
        getChatData(groupId);
        getVideoDescription(groupId);
        txtProductname.setText(productName);

        if (!TextUtils.isEmpty(videoViews)) {
            if (Integer.parseInt(videoViews) < 1000) {
                if (Integer.parseInt(videoViews) < 10) {
                    txtViews.setText(videoViews);
                } else {
                    txtViews.setText(videoViews);
                }
            } else {
                txtViews.setText((Double.parseDouble(videoViews) / 1000) + "K");
            }
        }
        try {
            txtDuration.setText(Global.getTimeDuration(Global.convertUTCToLocal(videoStartTime), Global.convertUTCToLocal(videoEndTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Global.setStatusBarColor(StreamedVideoDetail.this, R.color.colorBlack);
        models = new ArrayList<>();

        relLayMoreLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(StreamedVideoDetail.this, relLayMoreLive);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_live_stream, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (isLogined(StreamedVideoDetail.this)) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_view_pined:

                                    Intent intent1 = new Intent(StreamedVideoDetail.this, ViewPinedMessage.class);
                                    intent1.putExtra("group_id", groupId);
                                    startActivity(intent1);
                                    break;
                                case R.id.menu_cancel_comment:
                                    popup.dismiss();
                                    break;
                            }

                        } else {
                            S_Dialogs.getLoginDialog(StreamedVideoDetail.this).show();
                        }

                        return false;
                    }
                });
            }
        });

        relLayBackLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        disconnectSocketFromJoin();
        Global.setStatusBarColor(StreamedVideoDetail.this, R.color.colorWhite);

    }


    private void getChatData(String group_id) {
        Call<JsonObject> getChatData = Global.WebServiceConstants.getRetrofitinstance().getChatData(HelperPreferences.get(this).getString(UID), group_id);
        getChatData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {

                    if (response.isSuccessful()) {


                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("record");
                            for (int g = 0; g < jsonArray.length(); g++) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(g);
                                String message = jsonObject1.getString("message");
                                String sender_id = jsonObject1.getString("sender_id");
                                String sender_image = jsonObject1.getString("sender_image");
                                String msg_id = jsonObject1.getString("msg_id");
                                String type = jsonObject1.getString("type");
                                String image_url = jsonObject1.getString("image_url");
                                String groupId = jsonObject1.getString("group_id");
                                String commentId = jsonObject1.getString("comment_id");
                                String isPined = jsonObject1.getString("ispined");
                                String createdAt = jsonObject1.getString("created_at");
//                                Log.e("Dvsdvfdfv", message);
                                hashMap.put("senderid", sender_id);
                                hashMap.put("owner_id", sellerId);
                                hashMap.put("image_url", image_url);
                                hashMap.put("sender_image", sender_image);
                                hashMap.put("msg_id", msg_id);
                                hashMap.put("comment_id", commentId);
                                hashMap.put("message", message);
                                hashMap.put("group_id", groupId);
                                hashMap.put("ispined", isPined);
                                hashMap.put("type", type);
                                hashMap.put("created_at", createdAt);
                                arrayList.add(hashMap);

                            }

                            setUpMessgae(arrayList);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }


    @Override
    protected void onResume() {

        super.onResume();
//        ConnectToSocketToJoin();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //-----------------------------------------------------------------------

    private void sendOfferApi(String name, String price, String recieverId) {
        Log.e( "sendOfferApi: ",name );
        new ApisHelper().sendOfferApi(StreamedVideoDetail.this, recieverId, groupId, productId, name, price, new ApisHelper.SendOfferCallback() {
            @Override
            public void onSendOfferSuccess(String msg) {

                Toast.makeText(StreamedVideoDetail.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendOfferFailure() {
                Toast.makeText(StreamedVideoDetail.this, "Unable to send an offer on this product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpMessgae(ArrayList<HashMap<String, String>> models) {

        try {
            adapter = new LiveStreamChatAdapter(models, StreamedVideoDetail.this, "", /*(commentId, senderId) -> {

            }*/new LiveStreamChatAdapter.OptionsClickCallBack() {
                @Override
                public void onMakeOfferClicked(String comment_id, String senderId) {
                 //  alertsendoffer(senderId);

                     SendOffer sendOffer = new SendOffer(StreamedVideoDetail.this);
                     sendOffer.send(StreamedVideoDetail.this, senderId, new SendOffer.onSendOffer() {
                        @Override
                        public void onSendOfferSuccess(String name, String price) {

                            Log.e("response_sendOffer","here: "+name+"  "+price);
                            sendOfferApi(name,price, senderId);

                        }
                    });
                     sendOffer.show();
                }

                @Override
                public void onPinLiveCommentClick(String commentId) {

                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StreamedVideoDetail.this, LinearLayout.VERTICAL, false);
            recyclerviewLivemoduleChat.setLayoutManager(linearLayoutManager);
            recyclerviewLivemoduleChat.setItemAnimator(new DefaultItemAnimator());
            recyclerviewLivemoduleChat.setAdapter(adapter);
            Log.e("Dvcfsdvc", models.size() + "");
            if (models.size() > 0) {
                recyclerviewLivemoduleChat.scrollToPosition(models.size() - 1);
            }
            if (Build.VERSION.SDK_INT >= 11) {
                recyclerviewLivemoduleChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v,
                                               int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            recyclerviewLivemoduleChat.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (models != null && models.size() > 0) {
                                            recyclerviewLivemoduleChat.smoothScrollToPosition(recyclerviewLivemoduleChat.getAdapter().getItemCount() - 1);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }, 100);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("setup_error", "setUpMessgae: ");
        }


    }

    private void getVideoDescription(String groupId) {

//        Dialog dialog = S_Dialogs.getLoadingDialog(MainActivityLiveStream.this);
//        dialog.show();
        Call<PinCommentModel> getProfileCall = service.getLiveVideoDesc(groupId);
        getProfileCall.enqueue(new Callback<PinCommentModel>() {
            @Override
            public void onResponse(Call<PinCommentModel> call, Response<PinCommentModel> response) {
                if (response.isSuccessful()) {
                   /* if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }*/

                    if (response.body().getResult().getOwnerId().equals(HelperPreferences.get(StreamedVideoDetail.this).getString(UID))) {
                        liPinnedSent.setVisibility(View.VISIBLE);
                        txtDescriptionSent.setText("" + response.body().getResult().getMessage());
                        Glide.with(StreamedVideoDetail.this).load(response.body().getResult().getSenderImage()).apply(Global.getGlideOptions()).into(imgUser);
                        txtMsgTimeSent.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(response.body().getResult().getCreatedAt())));
                    } else {
                        liPinnedReceived.setVisibility(View.VISIBLE);
                        txtDescriptionRec.setText("" + response.body().getResult().getMessage());
                        Glide.with(StreamedVideoDetail.this).load(response.body().getResult().getSenderImage()).apply(Global.getGlideOptions()).into(imgUserRec);
                        txtMsgTimeRec.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(response.body().getResult().getCreatedAt())));
                    }

                    Log.e("getLiveVideoDesc", "onSuccess" + response.body().getMessage().toString());
                } else {
                    try {
                        Log.e("getLiveVideoDesc", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PinCommentModel> call, Throwable t) {
              /*  if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }*//*
                Snackbar.make(getWindow().getDecorView(), "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();*/

                Log.e("getLiveVideoDesc", "onFailure: " + t.getMessage());
            }
        });

    }

    public void alertsendoffer(String senderid)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Send Offer");

        final EditText one = new EditText(this);
        one.setHint("Enter Name");//optional
        final EditText two = new EditText(this);
        two.setHint("Enter Price");//optional

        one.setInputType(InputType.TYPE_CLASS_TEXT);
        two.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout lay = new LinearLayout(this);
        lay.setPadding(12,0,12,0);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(one);
        lay.addView(two);
        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (TextUtils.isEmpty(two.getText().toString()) || two.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(StreamedVideoDetail.this, "Enter offering amount", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                } else {
//                makeOfferApi(input.toString().trim(), sellerId, dialog, productId);

                    if (one.getText().toString().trim().isEmpty())
                    {
                        productname_1 = productName;
                    }
                    else
                    {productname_1= one.getText().toString();}
                    sendOfferApi(productname_1,two.getText().toString().trim(), senderid);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
