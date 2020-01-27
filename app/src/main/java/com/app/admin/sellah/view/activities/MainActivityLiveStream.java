package com.app.admin.sellah.view.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.OnSwipeTouchListener;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ChatModel.ChatMessageModel;
import com.app.admin.sellah.model.extra.LiveVideoDesc.LiveVideoDescModel;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.PinCommentModel.PinCommentModel;
import com.app.admin.sellah.model.extra.UploadChatImage.UploadChatImageModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.LiveProductDetailDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.SendOffer;
import com.app.admin.sellah.view.adapter.LiveStreamChatAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import de.tavendo.autobahn.WebSocket;
import io.antmedia.webrtcandroidframework.IWebRTCClient;
import io.antmedia.webrtcandroidframework.IWebRTCListener;
import io.antmedia.webrtcandroidframework.WebRTCClient;
import io.antmedia.webrtcandroidframework.apprtc.CallActivity;
import io.antmedia.webrtcandroidframework.apprtc.CallFragment;
import io.antmedia.webrtcandroidframework.apprtc.UnhandledExceptionHandler;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.convertUTCToLocal;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.Global.playMessageTone;
import static com.app.admin.sellah.controller.utils.SAConstants.ConstValues.SCREEN_STATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_CLOSE_GROUP;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_COUNTVIEWS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_CREATEGROUP;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_JOIN;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_LIVE_PIN_COMMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_SEND_GROUP_MESSAGE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_PROFILE_PIC;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_STOP_LIVE_VIDEO;
import static io.antmedia.webrtcandroidframework.apprtc.CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED;

public class MainActivityLiveStream extends Activity implements IWebRTCListener, LiveStreamChatAdapter.OptionsClickCallBack, LiveProductDetailDialog.UpdateDetailCallback {


    //  public static final String SERVER_URL = "ws://192.168.43.168:5080/WebRTCAppEE/websocket";
    //   public static final String SERVER_URL = "ws://ec2-13-229-209-35.ap-southeast-1.compute.amazonaws.com/WebRTCAppEE/websocket";
    public static final String SERVER_URL = "ws://ec2-18-136-228-4.ap-southeast-1.compute.amazonaws.com/WebRTCAppEE/websocket";
    //    public static final String SERVER_URL = "ws://ovh36.antmedia.io:5080/WebRTCAppEE/websocket";
    public static final int PERMISSION_REQUEST_CODE = 123;
    @BindView(R.id.img_user_rec)
    CircleImageView imgUserRec;
    @BindView(R.id.txt_category_rec)
    TextView txtCategoryRec;
    @BindView(R.id.txt_description_rec)
    TextView txtDescriptionRec;
    @BindView(R.id.txt_msg_time_rec)
    TextView txtMsgTimeRec;
    @BindView(R.id.li_pinned_received)
    LinearLayout liPinnedReceived;
    @BindView(R.id.txt_msg_time_sent)
    TextView txtMsgTimeSent;
    @BindView(R.id.txt_category_sent)
    TextView txtCategorySent;
    @BindView(R.id.txt_description_sent)
    TextView txtDescriptionSent;
    @BindView(R.id.img_user)
    CircleImageView imgUser;
    @BindView(R.id.li_pinned_sent)
    LinearLayout liPinnedSent;
    @BindView(R.id.camera_view_renderer)
    SurfaceViewRenderer cameraViewRenderer;
    @BindView(R.id.pip_view_renderer)
    SurfaceViewRenderer pipViewRenderer;
    @BindView(R.id.img_chat)
    ImageView imgChat;
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
    @BindView(R.id.img_pin_rec)
    ImageView imgPinRec;
    @BindView(R.id.lin_content)
    LinearLayout linContent;
    @BindView(R.id.card3)
    CardView card3;
    @BindView(R.id.card2)
    CardView card2;
    @BindView(R.id.img_unpin_comment)
    ImageView imgUnpinComment;
    @BindView(R.id.img_pin_sent)
    ImageView imgPinSent;
    @BindView(R.id.lin_content_sent)
    LinearLayout linContentSent;
    @BindView(R.id.card_sent)
    CardView cardSent;
    @BindView(R.id.card2_sent)
    CardView card2Sent;
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
    RelativeLayout linear;
    @BindView(R.id.card_bottom_view)
    RelativeLayout cardBottomView;
    @BindView(R.id.flipCamera)
    ImageButton flipCamera;
    @BindView(R.id.txt_offer_from_seller)
    TextView txtOfferFromSeller;
    @BindView(R.id.share_chat)
    ImageView shareChat;

    @BindView(R.id.liveViewImage)
    ImageView liveViewImage;

    @BindView(R.id.lin_recyclerview)
    LinearLayout linRecyclerview;
    private CallFragment callFragment;
    private WebRTCClient webRTCClient;
    Socket mSocket;
    EditText edt_message;
    private boolean isConnected;
    ImageView btn_send;
    WebService service;
    private LiveStreamChatAdapter adapter;
    private ArrayList<HashMap<String, String>> models;
    private boolean isOffer = true;
    LiveStreamChatAdapter videoCategoriesAdpt;
    RecyclerView recyclerview_livemodule_chat;
    RelativeLayout relLay_back_live;
    String showPublishPlay = "";
    private String streamId = "";
    private String groupId = "";
    private String productName = "";
    private String productId = "";
    private String sellerId = "";
    private int GALLERY = 1213;
    private int CAMERA_PIC_REQUEST = 1212;
    private String imagePath = "", coverImagePath = "";
    String owner = "";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private PopupMenu popup;
    private RelativeLayout relLayOptionLive;
    private String productDescription = "";
    private String productCategory = "";
    public String catId = "";
    private String videoStartTime = "";
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    private String videoViews = "";
    boolean isFliped;
    boolean isbackPressed = false;
    Dialog loadingDialog;
    private LiveProductDetailDialog liveProductDetailDialog;
    private String tokenId = "";
    private boolean isFetchingimage = true;
    private boolean isPermissionGranted;
    private String imageFilePath = "";
    private String isOfferd = "";
    private String ownerImage = "";
    private String ownerId = "";
    private String ownerUserName = "";
    private String productname_1 = "";
    public String deeplinkingStatus = "off";
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main_live);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        service = Global.WebServiceConstants.getRetrofitinstance();
        Intent intent = getIntent();
        Global app = new Global();
        mSocket = app.getSocket();
        loadingDialog = S_Dialogs.getLoadingDialog(this);
        showPublishPlay = intent.getStringExtra("value");
        streamId = intent.getStringExtra("id");
        productName = intent.getStringExtra("product_name");
        productId = intent.hasExtra("product_id") ? intent.getStringExtra("product_id") : "";
        groupId = intent.hasExtra("group_id") ? intent.getStringExtra("group_id") : "";
        sellerId = intent.hasExtra("seller_id") ? intent.getStringExtra("seller_id") : "";
        videoStartTime = intent.hasExtra("start_time") ? intent.getStringExtra("start_time") : "";
        videoViews = intent.hasExtra("views") ? intent.getStringExtra("views") : "";

        if (!groupId.equalsIgnoreCase(""))
            getProductUrlApi(groupId);

        if (Global.DEEP_LINKING_STATUS.equalsIgnoreCase("enable"))
            Global.DEEP_LINKING_STATUS = "disable";


        handler = new Handler();
        try {
            txtViews.setText(videoViews);
            txtDuration.setText(Global.getTimeDuration(Global.convertUTCToLocal(videoStartTime), Global.convertUTCToLocal(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))));
        } catch (Exception e) {

        }

        Thread.setDefaultUncaughtExceptionHandler(new UnhandledExceptionHandler(this));
        Global.setStatusBarColor(MainActivityLiveStream.this, R.color.colorBlack);
        ButterKnife.bind(this);
        relLay_back_live = findViewById(R.id.relLay_back_live);
        txtProductname = findViewById(R.id.txt_productname);
        edt_message = findViewById(R.id.edt_message);
        btn_send = findViewById(R.id.btn_send_live);
        recyclerview_livemodule_chat = findViewById(R.id.recyclerview_livemodule_chat);
        models = new ArrayList<>();
        // videoCode();
        permissionsCheck();
        // setUpMessgae(arrayList);
        setUpSendButton();
        showHideoptions();
        arrayList = new ArrayList<>();
        setupChatList();
        relLayOptionLive = findViewById(R.id.relLay_more_live);

        relLayOptionLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(MainActivityLiveStream.this, relLayOptionLive);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_live_stream, popup.getMenu());
                popup.show();

                if (!sellerId.equalsIgnoreCase(HelperPreferences.get(MainActivityLiveStream.this).getString(UID))) {
                    popup.getMenu().findItem(R.id.menu_Report_user).setVisible(true);
                    popup.getMenu().findItem(R.id.menu_view_pined).setVisible(false);
                } else {
                    popup.getMenu().findItem(R.id.menu_Report_user).setVisible(false);
                    popup.getMenu().findItem(R.id.menu_view_pined).setVisible(true);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (isLogined(MainActivityLiveStream.this)) {
                            switch (menuItem.getItemId()) {
                              /*  case R.id.menu_Report_user:
                                    break;
                                case R.id.menu_block:
                                    break;*/

                                case R.id.menu_Report_user:

                                    Global.addComment(MainActivityLiveStream.this, (dialog, input) -> {
                                        if (TextUtils.isEmpty(input)) {
                                            dialog.dismiss();

                                        } else {
                                            String comment = input.toString().trim();

                                            hitReportApi(comment);
                                            dialog.dismiss();
                                        }
                                    }).show();


                                    //     hitReportApi("ededed");

                                    break;
                                case R.id.menu_view_pined:

                                    Intent intent1 = new Intent(MainActivityLiveStream.this, ViewPinedMessage.class);
                                    intent1.putExtra("group_id", groupId);
                                    startActivity(intent1);
                                    break;
                                case R.id.menu_cancel_comment:
                                    popup.dismiss();
                                    break;
                            }

                        } else {
                            S_Dialogs.getLoginDialog(MainActivityLiveStream.this).show();
                        }

                        return false;
                    }
                });
            }
        });

        shareChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deeplinkingStatus = "on";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = Global.DEEP_LINKING_PRODUCT_URL;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Join me now at this Sellah! Live Video session: ");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));


            }
        });


        relLay_back_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mSocket != null) {


                    if (!isOffer) {
                        if (edt_message.getText().toString().trim().equalsIgnoreCase("")) {
                            Toast.makeText(MainActivityLiveStream.this, "Please enter text.", Toast.LENGTH_SHORT).show();

                        } else
                            attemptSend(edt_message.getText().toString().trim());
                    } else {
                        /*S_Dialogs.getMakeOfferDialog(MainActivityLiveStream.this, "30", (dialog, input) -> {
                            if (TextUtils.isEmpty(input) || input.toString().equalsIgnoreCase("0")) {
                                Toast.makeText(MainActivityLiveStream.this, "Please enter offering amount", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                            } else {
                                makeOfferApi(input.toString().trim(), sellerId, dialog, productId);
                            }
                        }).show();*/
                    }
                }
            }
        });

        recyclerview_livemodule_chat.setOnTouchListener(new OnSwipeTouchListener(MainActivityLiveStream.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();


                recyclerview_livemodule_chat.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                recyclerview_livemodule_chat.setVisibility(View.GONE);
                            }
                        });


            }


            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                recyclerview_livemodule_chat.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                recyclerview_livemodule_chat.setVisibility(View.VISIBLE);
                            }
                        });

                // Put your logic here for text visibility and for timer like progress bar for 5 second and setText
            }
        });


        linRecyclerview.setOnTouchListener(new OnSwipeTouchListener(MainActivityLiveStream.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                recyclerview_livemodule_chat.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                recyclerview_livemodule_chat.setVisibility(View.GONE);
                            }
                        });
            }


            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                recyclerview_livemodule_chat.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                recyclerview_livemodule_chat.setVisibility(View.VISIBLE);
                            }
                        });

                // Put your logic here for text visibility and for timer like progress bar for 5 second and setText
            }
        });


/*
        webRTCClient = new WebRTCClient( this,this);

        //webRTCClient.setOpenFrontCamera(false);


        //String streamId = "stream" + (int)(Math.random() * 999);
        String streamId = "stream9";
        String tokenId = "tokenId";

        SurfaceViewRenderer cameraViewRenderer = findViewById(R.id.camera_view_renderer);

        SurfaceViewRenderer pipViewRenderer = findViewById(R.id.pip_view_renderer);


        webRTCClient.setVideoRenderers(pipViewRenderer, cameraViewRenderer);

        // Check for mandatory permissions.
        for (String permission : CallActivity.MANDATORY_PERMISSIONS) {
            if (this.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission " + permission + " is not granted", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        this.getIntent().putExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, true);
        webRTCClient.init(SERVER_URL, streamId, IWebRTCClient.MODE_PLAY, tokenId);


        webRTCClient.startStream();*/

    }


    /*private void setUpSendButton() {

        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnSend.setImageResource(R.drawable.cart_icon_96dp);
                    isOffer = true;
                } else {
                    isOffer = false;
                    btnSend.setImageResource(R.drawable.send_50dp);
                }
            }
        });
    }*/
    @Override
    public void onPlayStarted() {
        dismissDialog();

        Log.w(getClass().getSimpleName(), "onPlayStarted");
        Toast.makeText(this, "Play started", Toast.LENGTH_LONG).show();
        webRTCClient.switchVideoScaling(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        owner = "false";


//        try {
//            StartTime = getMilisec(convertUTCToLocal(videoStartTime));
        StartTime = SystemClock.uptimeMillis();
        /*} catch (ParseException e) {
            e.printStackTrace();
            StartTime = SystemClock.uptimeMillis();
        }*/

        handler.postDelayed(runnableViwer, 0);
        ConnectToSocket();

        //todo for animation
        animation = AnimationUtils.loadAnimation(MainActivityLiveStream.this, R.anim.fadeon);


    }

    @Override
    public void onPublishStarted() {
        dismissDialog();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(PUSH_NOTIFICATION));
        Log.w(getClass().getSimpleName(), "onPublishStarted");
        Toast.makeText(this, "Publish started", Toast.LENGTH_LONG).show();
        owner = "true";

        SCREEN_STATUS = MainActivityLiveStream.class.getSimpleName();

        ConnectToSocket();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

    }

    @Override
    public void onPublishFinished() {

        SCREEN_STATUS = "";
        dismissDialog();
        closeGroup();
        disconnectSocket();
        if (animation != null)
            animation.cancel();
        handler.removeCallbacks(runnable);
        Log.w(getClass().getSimpleName(), "onPublishFinished");
        Toast.makeText(this, "Publish finished", Toast.LENGTH_LONG).show();
        LocalBroadcastManager.getInstance(MainActivityLiveStream.this).unregisterReceiver(mMessageReceiver);

    }

    @Override
    public void onPlayFinished() {
        dismissDialog();
        disconnectSocket();
        if (animation != null)
            animation.cancel();
        Log.w(getClass().getSimpleName(), "onPlayFinished");
        Toast.makeText(this, "Play finished", Toast.LENGTH_LONG).show();
        handler.removeCallbacks(runnableViwer);


    }

    @Override
    public void noStreamExistsToPlay() {
        dismissDialog();
        Log.e(getClass().getSimpleName(), "noStreamExistsToPlay");
//        showOfflineLayout();
//        Toast.makeText(this, "No stream exist to play", Toast.LENGTH_LONG).show();
//        onBackPressed();
//        disconnectSocket();
//        disconnectSocketFromJoin();
        isbackPressed = false;
        S_Dialogs.getLiveVideoStopedDialog(MainActivityLiveStream.this, "No video available to play.", ((dialog, which) -> {
            if (webRTCClient != null) {
                webRTCClient.stopStream();
            }
        })).show();

  /*      Intent intent = new Intent(MainActivityLiveStream.this, StreamedVideoDetail.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("productName", productName);
        intent.putExtra("streamId", streamId);
        startActivity(intent);*/
    }

    @Override
    public void onError(String description) {
        dismissDialog();
        Toast.makeText(this, "Error: " + description, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (animation!=null)
            animation.cancel();*/

        if (deeplinkingStatus.equalsIgnoreCase("on")) {
            deeplinkingStatus = "off";
        } else {
            dismissDialog();
            closeGroup();
            disconnectSocket();
            Global.setStatusBarColor(MainActivityLiveStream.this, R.color.colorWhite);
//        Global.makeTransperantStatusBar(this, true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            LocalBroadcastManager.getInstance(MainActivityLiveStream.this).unregisterReceiver(mMessageReceiver1);
            if (webRTCClient != null) {
                webRTCClient.stopStream();
            }
        }


/*        if (!isFetchingimage) {
//            closeGroup();
            dismissDialog();
            disconnectSocket();
            if (webRTCClient != null) {
             //   webRTCClient.stopStream();
            }
        }*/

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                if (message != null) {
                    switch (message.getNotiType()) {
                        case NT_STOP_LIVE_VIDEO:
                            if (webRTCClient != null) {
                                isbackPressed = true;
                                webRTCClient.stopStream();
                                S_Dialogs.getLiveVideoStopedDialog(MainActivityLiveStream.this, "Video has been stopped by admin ", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        finish();
                                    }
                                }).show();
                            }
                            break;
                        default:

                            break;
                    }
                }
                Log.e("receiver", "Got message: mainActivity" + message.getMessage());
            } catch (Exception e) {
                Log.e("receiver_failure", "onReceive: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (animation != null)
            animation.cancel();

//        closeGroup();
        dismissDialog();
        closeGroup();
        disconnectSocket();
        Global.setStatusBarColor(MainActivityLiveStream.this, R.color.colorWhite);
//        Global.makeTransperantStatusBar(this, true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        LocalBroadcastManager.getInstance(MainActivityLiveStream.this).unregisterReceiver(mMessageReceiver1);
        if (webRTCClient != null) {
            webRTCClient.stopStream();
        }
    }

    @Override
    public void onSignalChannelClosed(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code) {
        Toast.makeText(this, "Signal channel closed with code " + code, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {

        if (animation != null)
            animation.cancel();

        Log.w(getClass().getSimpleName(), "disconnected");
        Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show();
//        finish();
        if (!showPublishPlay.equalsIgnoreCase("LiveStream")) {
            if (!isbackPressed) {
                S_Dialogs.getLiveVideoStopedDialog(MainActivityLiveStream.this, "Live Video has ended", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
            } else {
                finish();
            }
        } else {
            if (!isbackPressed) {
                finish();
            }
        }
    }

    private void permissionsCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED &&
                   /* ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                            == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                            == PackageManager.PERMISSION_GRANTED &&*/
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_GRANTED) {

                videoCode();


            } else {
                isPermissionGranted = false;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                Manifest.permission.RECORD_AUDIO,
                                /* Manifest.permission.READ_SMS,*/ Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);

            }
        } else {
            videoCode();
        }
    }

    /* private void initWebrtcClient(){



     }
 */
    public void videoCode() {
        isPermissionGranted = true;
//        initWebrtcClient();

        // Check for mandatory permissions.
        for (String permission : CallActivity.MANDATORY_PERMISSIONS) {
            if (this.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission " + permission + " is not granted", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        this.getIntent().putExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, true);

        if (showPublishPlay.equalsIgnoreCase("LiveStream")) {

            flipCamera.setVisibility(View.VISIBLE);
            liveProductDetailDialog = LiveProductDetailDialog.create(MainActivityLiveStream.this, this);
            liveProductDetailDialog.show();
        } else {
            loadingDialog.show();
            flipCamera.setVisibility(View.GONE);
            txtProductname.setText(productName);
            getChatData(false);
            getVideoDescription(groupId);
            Log.e("StreamId", "videoCode: " + streamId);
            if (streamId != null || !streamId.equalsIgnoreCase("")) {
                webRTCClient = new WebRTCClient(this, MainActivityLiveStream.this);
                webRTCClient.setOpenFrontCamera(false);
                tokenId = "tokenId";
                SurfaceViewRenderer cameraViewRenderer = findViewById(R.id.camera_view_renderer);
                SurfaceViewRenderer pipViewRenderer = findViewById(R.id.pip_view_renderer);
                webRTCClient.setVideoRenderers(pipViewRenderer, cameraViewRenderer);
                webRTCClient.init(SERVER_URL, streamId, IWebRTCClient.MODE_PLAY, tokenId);
                webRTCClient.startStream();
                /*webRTCClient.init(SERVER_URL, "stream438", IWebRTCClient.MODE_PLAY, tokenId);
                webRTCClient.startStream();*/
            } else {
                dismissDialog();
                isbackPressed = false;
                S_Dialogs.getLiveVideoStopedDialog(MainActivityLiveStream.this, "No video available to play.", ((dialog, which) -> {
//                    webRTCClient.stopStream();
                    finish();
                })).show();
            }

        }


    }

    private void getChatData(boolean fromOffer) {
        Call<JsonObject> getChatData = Global.WebServiceConstants.getRetrofitinstance().getChatData(HelperPreferences.get(this).getString(UID), groupId);
        getChatData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (fromOffer) {
                            ownerImage = jsonObject.getString("owner_image");
                            ownerId = jsonObject.getString("owner_id");
                            ownerUserName = jsonObject.getString("owner_username");
                            offerClick();
                        }else {
                            String status = jsonObject.getString("status");
                            isOfferd = jsonObject.getString("is_offered");
                            ownerImage = jsonObject.getString("owner_image");
                            ownerId = jsonObject.getString("owner_id");
                            ownerUserName = jsonObject.getString("owner_username");
                            Log.e("GetGroupChat", "onResponse: " + jsonObject.toString());
                            Log.e("OwnerInfo", "onOfferUIClicked : " + jsonObject.getString("is_offered") + ":" + ownerId + ":" + ownerImage + ":" + ownerUserName);
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
                                    String created_at = jsonObject1.getString("created_at");

                                    hashMap.put("senderid", sender_id);
                                    hashMap.put("image_url", image_url);
                                    hashMap.put("sender_image", sender_image);
                                    hashMap.put("msg_id", msg_id);
                                    hashMap.put("comment_id", commentId);
                                    hashMap.put("message", message);
                                    hashMap.put("group_id", groupId);
                                    hashMap.put("ispined", isPined);
                                    hashMap.put("type", type);
                                    hashMap.put("created_at", created_at);

                                    arrayList.add(hashMap);


//                                arrayList.add(hashMap);

                                }

                                adapter.notifyDataSetChanged();
                                showIsOffer();

                            }
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

        videoCode();
    }


    @Override
    protected void onResume() {

        super.onResume();
        LocalBroadcastManager.getInstance(MainActivityLiveStream.this).registerReceiver(mMessageReceiver1,
                new IntentFilter(PUSH_NOTIFICATION));
      /*  if(webRTCClient==null){
            if(isPermissionGranted){
                initWebrtcClient();
            }
        }*/
    }

    private void showIsOffer() {
        if (isOfferd.equalsIgnoreCase("Y")) {
            txtOfferFromSeller.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        disconnectSocket();
    }

    private void ConnectToSocket() {
        if (!mSocket.connected()) {
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(EVENT_CREATEGROUP, onGroupCreation);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(EVENT_SEND_GROUP_MESSAGE, onNewMessage);
            mSocket.on(EVENT_LIVE_PIN_COMMENT, onLiveCommentPin);
            mSocket.on(EVENT_COUNTVIEWS, onViewCountupdate);
            mSocket.on(EVENT_CLOSE_GROUP, onCloseGroup);
            mSocket.on(EVENT_JOIN, join);
            mSocket.connect();

            // increment count views

            if (!TextUtils.isEmpty(groupId)) {
                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("group_id", groupId);
                    jsonObject1.put("isJoined", "Y");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit(EVENT_COUNTVIEWS, jsonObject1);
            }

        }
    }
    /* private void ConnectToSocketToJoin() {
     *//* Global app = new Global();
        mSocket = app.getSocket();*//*
//        service = Global.WebServiceConstants.getRetrofitinstance();
        if (!mSocket.connected()) {
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.on(EVENT_JOIN, onGroupJoin);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(EVENT_SEND_GROUP_MESSAGE, onNewMessage);
            mSocket.on(EVENT_JOIN, join);
            mSocket.on(EVENT_COUNTVIEWS, onViewCountupdate);
            mSocket.connect();
        }
    }*/

    /*  private void choosePhotoFromGallary() {
          Global.hideKeyboard(relChatRoot, this);
          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

          startActivityForResult(galleryIntent, GALLERY);
      }

      private void takePhotoFromCamera() {
          Global.hideKeyboard(relChatRoot, this);
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, CAMERA_PIC_REQUEST);
      }
  */
    public void disconnectSocket() {
//        try {


        if (mSocket.connected()) {
            //  mSocket.off(EVENT_CREATEROOM, onRoomCreation);
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(EVENT_CREATEGROUP, onGroupCreation);
            mSocket.off(EVENT_SEND_GROUP_MESSAGE, onNewMessage);
            mSocket.off(EVENT_JOIN, join);
            mSocket.off(EVENT_LIVE_PIN_COMMENT, onLiveCommentPin);
            mSocket.off(EVENT_COUNTVIEWS, onViewCountupdate);
            mSocket.off(EVENT_CLOSE_GROUP, onCloseGroup);
            mSocket.disconnect();
//        } catch (Exception e) {
        }
    }

    public void closeGroup() {


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("group_id", groupId);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SAcweasvcdsError", e.getMessage() + "cdswvcsd");
        }
        Log.e("close_params", jsonObject.toString());
        mSocket.emit(EVENT_CLOSE_GROUP, jsonObject);
    }


   /* public void disconnectSocketFromJoin() {
//        try {
        mSocket.disconnect();
        //  mSocket.off(EVENT_CREATEROOM, onRoomCreation);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//            mSocket.off(EVENT_JOIN, onGroupJoin);
        mSocket.off(EVENT_COUNTVIEWS, onViewCountupdate);
        mSocket.off(EVENT_SEND_GROUP_MESSAGE, onNewMessage);
        mSocket.off(EVENT_JOIN, join);
//        } catch (Exception e) {
//        }
    }*/

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    if (!isConnected) {
//                        Log.e("create", "run: ", );
                        if (owner.equalsIgnoreCase("true")) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
                                jsonObject.put("video_id", streamId);
                                jsonObject.put("product_name", productName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("CreateGroup_params", jsonObject.toString());
                            if (TextUtils.isEmpty(groupId)) {
                                mSocket.emit(EVENT_CREATEGROUP, jsonObject);
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
                                jsonObject.put("group_id", groupId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("JoinGroup_params", jsonObject.toString());
//
                            mSocket.emit(EVENT_JOIN, jsonObject);
                        }

                        Log.e("Connection", "connected");
                        isConnected = true;
                    }
                }
            });
        }
    };

   /* private Emitter.Listener onJoinConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
//
                            attemptJoin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.e("CreateGroup_params", jsonObject.toString());
//                        mSocket.emit(EVENT_CREATEROOM, jsonObject);
                        mSocket.emit(EVENT_CREATEGROUP, jsonObject);
                        Log.e("Connection", "connected");
                        isConnected = true;
                    }
                }
            });
        }
    };*/

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Connection", "disconnected");
                    isConnected = false;
                    /*Toast.makeText(getApplicationContext(),
                            "disconnect", Toast.LENGTH_LONG).show();*/
                }
            });
        }
    };
   /* private Emitter.Listener onRoomCreation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Event_createRoom", "run: created");

                }
            });
        }
    };*/

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("connection_error", args[0] + "");
                    Log.e("connection_error", args.toString());


                    Toast.makeText(MainActivityLiveStream.this,
                            "error_connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onGroupCreation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();
                    //JSONObject obj = (JSONObject)args[0];


                    try {
                        JSONObject obj = new JSONObject(data);
                        // Get each element based on it's tag
                        groupId = obj.getString("group_id");
                        productId = obj.getString("product_id");
                        sellerId = HelperPreferences.get(MainActivityLiveStream.this).getString(UID);
                        adapter = new LiveStreamChatAdapter(arrayList, MainActivityLiveStream.this, sellerId, MainActivityLiveStream.this);
                        showHideoptions();
                        //                        adapter.notifyDataSetChanged();
                        Log.e("CreateGroupId", groupId);
//                        attemptJoin();
                        attemptToSendPinMessage();

                        //-----------create video url--------------------
                        if (!groupId.equalsIgnoreCase(""))
                            getProductUrlApi(groupId);

//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    JSONObject jsonObject = args.toString();
                    Log.e("CreateGroup", data);
                    Log.e("CreateGroup", "Done");
                }
            });
        }
    };

    private void attemptToSendPinMessage() {

        if (!TextUtils.isEmpty(coverImagePath)) {
            addVideoDescription(groupId, catId, productName, productDescription, coverImagePath);
        } else {
            Toast.makeText(this, "Unable to start live session.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void addVideoDescription(String groupId, String catId, String title, String desc, String imageUri) {

        Dialog dialog = S_Dialogs.getLoadingDialog(MainActivityLiveStream.this);
        dialog.show();
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(imageUri));
        MultipartBody.Part multipartimage = MultipartBody.Part.createFormData("cover_image", "livecoverimage.jpeg", image);
        Call<LiveVideoDescModel> getProfileCall = service.addLiveVideoDesc(
                RequestBody.create(MediaType.parse("text/plain"), groupId), RequestBody.create(MediaType.parse("text/plain"), catId), RequestBody.create(MediaType.parse("text/plain"), title), RequestBody.create(MediaType.parse("text/plain"), desc), RequestBody.create(MediaType.parse("text/plain"), HelperPreferences.get(MainActivityLiveStream.this).getString(UID)), multipartimage);
        getProfileCall.enqueue(new Callback<LiveVideoDescModel>() {
            @Override
            public void onResponse(Call<LiveVideoDescModel> call, Response<LiveVideoDescModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    /*liPinnedSent.setVisibility(View.VISIBLE);
                    txtCategorySent.setText("Title :- " + productName);
                    txtDescriptionSent.setText("Description :- " + productDescription);
                    Glide.with(MainActivityLiveStream.this).load(HelperPreferences.get(MainActivityLiveStream.this).getString(USER_PROFILE_PIC)).apply(Global.getGlideOptions()).into(imgUser);
                    txtMsgTimeSent.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(response.body().getResult().getCreatedAt())));
                   */
                    Log.e("add_desc_api", "onSuccess" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(getWindow().getDecorView(), "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("add_desc_api", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LiveVideoDescModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(getWindow().getDecorView(), "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();

                Log.e("add_desc_api", "onFailure: " + t.getMessage());
            }
        });

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

                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        if (response.body().getResult().getOwnerId().equals(HelperPreferences.get(MainActivityLiveStream.this).getString(UID))) {
                            liPinnedSent.setVisibility(View.VISIBLE);
                            txtDescriptionSent.setText("" + response.body().getResult().getMessage());
                            Glide.with(MainActivityLiveStream.this).load(HelperPreferences.get(MainActivityLiveStream.this).getString(USER_PROFILE_PIC)).apply(Global.getGlideOptions()).into(imgUser);
                            txtMsgTimeSent.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(response.body().getResult().getCreatedAt())));
                        } else {
                            liPinnedReceived.setVisibility(View.VISIBLE);
                            txtDescriptionRec.setText("" + response.body().getResult().getMessage());
                            Glide.with(MainActivityLiveStream.this).load(response.body().getResult().getSenderImage()).apply(Global.getGlideOptions()).into(imgUserRec);
                            txtMsgTimeRec.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(response.body().getResult().getCreatedAt())));
                        }
                    }

                    Log.e("getLiveVideoDesc", "onSuccess" + response.body().getMessage().toString());
                } else {
                   /* if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }*/
/*                    Snackbar.make(getWindow().getDecorView(), "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();*/
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

    private Emitter.Listener onGroupJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();
                    try {
                        JSONArray jsonarray = new JSONArray(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    JSONObject jsonObject = args.toString();
                    Log.e("CreateGroupJoin", data);
                    Log.e("CreateGroupJoin", "Done");
                }
            });
        }
    };
    private Emitter.Listener onViewCountupdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();

                    try {
                        JSONObject data1 = (JSONObject) args[0];
                        if (data1.getString("group_id").equalsIgnoreCase(groupId)) {
                            if (data1.getString("views") != null) {
                                if (Integer.parseInt(data1.getString("views")) < 1000) {
                                    if (Integer.parseInt(data1.getString("views")) < 10) {
                                        txtViews.setText(data1.getString("views"));
                                    } else {
                                        txtViews.setText(data1.getString("views"));
                                    }
                                } else {
                                    txtViews.setText((Double.parseDouble(data1.getString("views")) / 1000) + "K");
                                }
                            } else {
                                txtViews.setText("00");
                            }
//                            txtViews.setText(data1.getString("views"));
                            Log.e("VideoView", data);
                            Log.e("VideoView", "count");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    JSONObject jsonObject = args.toString();

                }
            });
        }
    };
    private Emitter.Listener onLiveCommentPin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();
                    Log.e("LiveComment_Event", "run: " + data);
                    try {
                        JSONObject data1 = (JSONObject) args[0];
                        if (!TextUtils.isEmpty(data1.getString("_id"))) {
                            if (data1.getString("owner_id").equals(HelperPreferences.get(MainActivityLiveStream.this).getString(UID))) {
                                liPinnedSent.setVisibility(View.VISIBLE);
//                                txtCategorySent.setText("Title :- " + response.body().getResult().getVideoTitle());
                                txtDescriptionSent.setText("" + data1.getString("message"));
                                Glide.with(MainActivityLiveStream.this).load(data1.getString("sender_image")).apply(Global.getGlideOptions()).into(imgUser);
                                txtMsgTimeSent.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(data1.getString("created_at"))));
                            } else {
                                liPinnedReceived.setVisibility(View.VISIBLE);
                                Glide.with(MainActivityLiveStream.this).load(data1.getString("sender_image")).apply(Global.getGlideOptions()).into(imgUserRec);
//                                txtCategoryRec.setText("Title :- " + response.body().getResult().getVideoTitle());
                                txtDescriptionRec.setText("" + data1.getString("message"));
                                txtMsgTimeRec.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(data1.getString("created_at"))));
                            }
                        } else {
                            liPinnedSent.setVisibility(View.GONE);
                            liPinnedReceived.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    JSONObject jsonObject = args.toString();

                }
            });
        }
    };


    //--------------------attempt--------------------------------------------
    private void attemptSend(String msg) {

        //  String message = edt_message.getText().toString().trim();
       /* if (TextUtils.isEmpty(message)) {
            return;
        }*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        edt_message.setText("");
        Log.e("SAcweasvcds2222", "cdswvcsd");
        JSONObject jsonObject = new JSONObject();
        try {

            //  jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
            jsonObject.put("sender_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
            jsonObject.put("type", "t");
            jsonObject.put("message", msg);
            jsonObject.put("group_id", groupId);
            //  jsonObject.put("sender_image", "https://ellahppdiag.blob.core.windows.net/chatimages/21112018015235_1542808355.jpg");
            //   jsonObject.put("created_at", str);
            //   jsonObject.put("image_url", "");
            //  jsonObject.put("type", "t");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SAcweasvcdsError", e.getMessage() + "cdswvcsd");
        }
        Log.e("SAcweasvcds3333", "cdswvcsd");
        Log.e("msg_params", jsonObject.toString());
        //   mSocket.emit("new_message", jsonObject);
        mSocket.emit(EVENT_SEND_GROUP_MESSAGE, jsonObject);
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  /*  String s = args[0].toString();
                    models.add(s);

                    videoCategoriesAdpt.notifyDataSetChanged();
*/
                    HashMap<String, String> hashMap = new HashMap<>();
                    JSONObject data = (JSONObject) args[0];
                    Log.e("onmsgReceive", data.toString());
                    playMessageTone(MainActivityLiveStream.this);
                    try {

                        if (groupId.equalsIgnoreCase(data.getString("group_id"))) {
                            String type = data.getString("type");
                            String message = "";
                            try {
                                message = data.getString("message");
                            } catch (Exception e) {

                            }
                            String senderid = data.getString("sender_id");
                            String imageurl = "";
                            try {
                                imageurl = data.getString("image_url");
                            } catch (Exception e) {

                            }
                            String senderImage = "";
                            try {
                                senderImage = data.getString("sender_image");
                            } catch (Exception e) {

                            }

                            String groiupId = "";
                            try {
                                groiupId = data.getString("group_id");
                            } catch (Exception e) {

                            }
                            String commentId = "";
                            try {
                                commentId = data.getString("comment_id");
                            } catch (Exception e) {

                            }
                            String createdAt = "";
                            try {
                                createdAt = data.getString("created_at");
                            } catch (Exception e) {

                            }

                            hashMap.put("message", message);
                            hashMap.put("senderid", senderid);
                            hashMap.put("image_url", imageurl);
                            hashMap.put("sender_image", senderImage);
                            hashMap.put("comment_id", commentId);
                            hashMap.put("ispined", "n");
                            hashMap.put("group_id", groiupId);
                            hashMap.put("owner_id", sellerId);
                            hashMap.put("type", type);
                            hashMap.put("created_at", createdAt);
                            arrayList.add(hashMap);


                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("message", message);
                            jsonObject.put("senderid", senderid);
                            jsonObject.put("image_url", imageurl);
                            jsonObject.put("sender_image", senderImage);
                            jsonObject.put("comment_id", commentId);
                            jsonObject.put("group_id", groiupId);
                            jsonObject.put("ispined", "n");
                            jsonObject.put("type", type);
                            jsonObject.put("owner_id", sellerId);
                            jsonObject.put("created_at", createdAt);

                            String mJsonString = String.valueOf(args[0]);
                            Log.e("scdwsdvcds", mJsonString + "");
                            JsonParser parser = new JsonParser();
                            JsonElement mJson = parser.parse(mJsonString);
                            Gson gson = new Gson();
                            Log.e("scdwsdvcds", mJson + "");
                            ChatMessageModel object = gson.fromJson(String.valueOf(jsonObject), ChatMessageModel.class);

                            if (object != null) {
                                //   models.add(object);
                                // adapter.notifyDataSetChanged();
                                //  recyclerview_livemodule_chat.smoothScrollToPosition(adapter.getItemCount() - 1);

                                adapter.notifyDataSetChanged();

                                Log.e("msg_object", "Inner");
                            } else {
                                Log.e("msg_object", "Error");
                            }
                        }

                    } catch (JSONException e) {
                        Log.e("JsonExce", "run: " + e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        //  senderid
                        data.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (arrayList.size() > 0) {
                        recyclerview_livemodule_chat.scrollToPosition(arrayList.size() - 1);
                    }
                    if (Build.VERSION.SDK_INT >= 11) {
                        recyclerview_livemodule_chat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                            @Override
                            public void onLayoutChange(View v,
                                                       int left, int top, int right, int bottom,
                                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                if (bottom < oldBottom) {
                                    recyclerview_livemodule_chat.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (arrayList != null && arrayList.size() > 0) {
                                                    recyclerview_livemodule_chat.smoothScrollToPosition(recyclerview_livemodule_chat.getAdapter().getItemCount() - 1);
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                    }, 100);
                                }
                            }
                        });
                    }


                }
            });
        }
    };

    private void attemptJoin() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        edt_message.setText("");
        Log.e("SAcweasvcds2222", "cdswvcsd");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
            jsonObject.put("group_id", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject1 = new JSONObject();
        try {
//            jsonObject.put("userid", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
            jsonObject1.put("group_id", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("join_params", jsonObject.toString() + ":");
        mSocket.emit("join", jsonObject);
        mSocket.emit(EVENT_COUNTVIEWS, jsonObject1);
    }

    //  private Emitter.Listener onNewMessage = new Emitter.Listener() {
    private Emitter.Listener join = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String data = (String) args[0];
//                    Toast.makeText(MainActivityLiveStream.this, data, Toast.LENGTH_SHORT).show();
                    Log.e("JoinGroup", "run: " + data);
                    // Get each element based on it's tag
                    /*JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("group_id", groupId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if (groupIdReturned.equalsIgnoreCase(groupId)) {
                    mSocket.emit(EVENT_COUNTVIEWS, jsonObject1);*/
//                    }

//                    attemptJoin();

                }
            });
        }
    };
    private Emitter.Listener onCloseGroup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   /* String data = (String) args[0];
                    Toast.makeText(MainActivityLiveStream.this, data, Toast.LENGTH_SHORT).show();
                  */

                    Log.e("onCloseGroup", "run:" + args[0].toString());

//                    attemptJoin();

                }
            });
        }
    };


    //-----------------------------------------------------------------------


    private void setUpMessgae(ArrayList<HashMap<String, String>> models) {

        try {


            adapter = new LiveStreamChatAdapter(models, MainActivityLiveStream.this, sellerId, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivityLiveStream.this, LinearLayout.VERTICAL, false);
            recyclerview_livemodule_chat.setLayoutManager(linearLayoutManager);
            recyclerview_livemodule_chat.setItemAnimator(new DefaultItemAnimator());
            recyclerview_livemodule_chat.setAdapter(adapter);
            Log.e("Dvcfsdvc", models.size() + "");
            if (models.size() > 0) {
                recyclerview_livemodule_chat.scrollToPosition(models.size() - 1);
            }
            if (Build.VERSION.SDK_INT >= 11) {
                recyclerview_livemodule_chat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v,
                                               int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            recyclerview_livemodule_chat.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (models != null && models.size() > 0) {
                                            recyclerview_livemodule_chat.smoothScrollToPosition(recyclerview_livemodule_chat.getAdapter().getItemCount() - 1);
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

    private void sendOfferApi(String name, String price, String recieverId) {
        new ApisHelper().sendOfferApi(MainActivityLiveStream.this, recieverId, groupId, productId, name, price, new ApisHelper.SendOfferCallback() {
            @Override
            public void onSendOfferSuccess(String msg) {

                Toast.makeText(MainActivityLiveStream.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendOfferFailure() {
                Toast.makeText(MainActivityLiveStream.this, "Unable to send an offer on this product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* private void makeOfferApi(String price, String sellerId, MaterialDialog dialog, String productId) {
        Context context = MainActivityLiveStream.this;
        Dialog dialog1 = S_Dialogs.getLoadingDialog(context);
        dialog1.show();
        Call<MakeOfferModel> makeOfferCall = service.makeOfferApi(HelperPreferences.get(context).getString(UID), productId, sellerId, price, "P", productName);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        attemptToSendOfferData(response.body(), response.body().getResult().getProductName());
                    }
                } else {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    try {
                        Log.e("MakeOffer", "onResponse: Faild" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Unable to make an offer on this product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("MakeOfferApi", "onFailure: " + t.getMessage());
            }
        });
    }*/

    private void setUpSendButton() {

        edt_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (showPublishPlay.equalsIgnoreCase("LiveStream")) {
                    if (s.length() == 0) {
                        btn_send.setImageResource(R.drawable.send_50dp);
                        isOffer = false;
                    } else {
                        isOffer = false;
                        btn_send.setImageResource(R.drawable.send_50dp);
                    }
                } else {
                    isOffer = false;
                    btn_send.setImageResource(R.drawable.send_50dp);
                }

            }
        });
    }

    private void attemptToSendOfferData(MakeOfferModel body, String otherUserId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
//            jsonObject.put("receiver/**/_id", otherUserId);
            jsonObject.put("message", "Offer Data");
            jsonObject.put("type", "o");
            jsonObject.put("offer_id", body.getResult().getOfferId());
            jsonObject.put("product_id", body.getResult().getProductId());
            jsonObject.put("group_id", groupId);
            jsonObject.put("product_name", body.getResult().getProductName());
            jsonObject.put("price_cost", body.getResult().getPriceCost());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("msg_params", jsonObject.toString());
        mSocket.emit(EVENT_SEND_GROUP_MESSAGE, jsonObject);
    }

    private void takePhotoFromCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            if (photoFile != null) {
                Uri photoURI = /*FileProvider.getUriForFile(getApplicationContext(),                                                                                                    "com.example.android.provider", photoFile);
//                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);*/FileProvider.getUriForFile(getApplicationContext(), "com.example.admin.fileproviderdemo.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        CAMERA_PIC_REQUEST);
                this.grantUriPermission(
                        "com.google.android.GoogleCamera",
                        photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            }
        }/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);*/
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        Log.e("ImagePath", "createImageFile: " + imageFileName);
        return image;
    }


    private void choosePhotoFromGallary() {
        isFetchingimage = true;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (liveProductDetailDialog != null && liveProductDetailDialog.isShowing()) {
//            initWebrtcClient();
            isFetchingimage = false;
            liveProductDetailDialog.onActivityResult(requestCode, resultCode, data);
            Log.e("MainActivityLive", "onActivityResult: Dialog");
        } else {
            if (requestCode == GALLERY) {
                if (data != null) {
                    isFetchingimage = false;
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivityLiveStream.this.getContentResolver(), contentURI);
                    /*String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                            Base64.NO_WRAP);*/
                        Uri tempUri = getImageUri(MainActivityLiveStream.this, bitmap);
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor cursor = MainActivityLiveStream.this.getContentResolver().query(tempUri, filePath, null, null, null);
                        cursor.moveToFirst();
                        imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                        uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
              /*  Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                String imgString = Base64.encodeToString(getBytesFromBitmap(imageBitmap),
                        Base64.NO_WRAP);
                Uri tempUri = getImageUri(MainActivityLiveStream.this.getApplicationContext(), imageBitmap);

                imagePath = getRealPathFromURI(tempUri);
*/
//                Glide.with(this).load(imageFilePath).into(imageView);
                uploadChatImage(ImageUploadHelper.convertImageTomultipart(imageFilePath, "image"));

          /*  MessageModel msg1 = new MessageModel();
            msg1.setSent(true);
            msg1.setImage(true);
            msg1.setImageId(R.drawable.sellah_icon);
            msg1.setCapturedImage(getRealPathFromURI(tempUri));
            models.add(msg1);
            adapter.notifyDataSetChanged();*/

            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = MainActivityLiveStream.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void uploadChatImage(MultipartBody.Part mPart) {
        Dialog dialog = S_Dialogs.getLoadingDialog(MainActivityLiveStream.this);
        dialog.show();
        Call<UploadChatImageModel> uploadChatImageCall = service.uploadChatimageApi(mPart);
        uploadChatImageCall.enqueue(new Callback<UploadChatImageModel>() {
            @Override
            public void onResponse(Call<UploadChatImageModel> call, Response<UploadChatImageModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sender_id", HelperPreferences.get(MainActivityLiveStream.this).getString(UID));
                            jsonObject.put("image_name", response.body().getImageName());
                            //   jsonObject.put("message", "img");
                            jsonObject.put("image_url", response.body().getImageUrl());
                            jsonObject.put("type", "img");
                            jsonObject.put("group_id", groupId);
                            Log.e("Exception_message123", groupId + "   Emit message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Exception_message", "Emit message");
                        }
                        Log.e("msg_params", jsonObject.toString());
                        mSocket.emit(EVENT_SEND_GROUP_MESSAGE, jsonObject);
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(getWindow().getDecorView(), "Unable to send image at the movement.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<UploadChatImageModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("UploadChatImageApi", "onFailure: " + t.getMessage());
                Snackbar.make(getWindow().getDecorView(), "Please try again later", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    @OnClick(R.id.img_send_gallery)
    public void onViewClicked() {
//        showPictureDialog();
        choosePhotoFromGallary();
    }

  /*  private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(MainActivityLiveStream.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Add from your gallery",
                "Take a photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                *//*if (imageList.size() != 8) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                } else {
                                    Snackbar.make(rootTag, "Please select photo", Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
                                }*//*
                                break;
                        }
                    }

                });
        pictureDialog.show();
    }*/


    /* private void getChatData(String group_id) {
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
                                 Log.e("Dvsdvfdfv", message);

                                 hashMap.put("senderid", sender_id);
                                 hashMap.put("image_url", sender_image);
                                 hashMap.put("msg_id", msg_id);
                                 hashMap.put("message", message);
                                 hashMap.put("type", type);


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
 */
    private void setupChatList() {
        setUpMessgae(arrayList);
    }

    @Override
    public void onMakeOfferClicked(String comment_id, String senderId) {
        Log.e("MakeOffer", "onMakeOfferClicked: ");
        //   alertsendoffer(senderId);

        SendOffer sendOffer = new SendOffer(MainActivityLiveStream.this);
        sendOffer.send(MainActivityLiveStream.this, senderId, new SendOffer.onSendOffer() {
            @Override
            public void onSendOfferSuccess(String name, String price) {

                Log.e("response_sendOffer", "here: " + name + "  " + price);
                sendOfferApi(name, price, senderId);

            }
        });
        sendOffer.show();


    }

    @Override
    public void onPinLiveCommentClick(String commentId) {
        Log.e("CommentPin", "onPinLiveCommentClick: " + commentId);
        if (!TextUtils.isEmpty(groupId)) {

            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("group_id", groupId);
                jsonObject1.put("comment_id", commentId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("CommentPinParams", "onPinLiveCommentClick: " + jsonObject1);
            mSocket.emit(EVENT_LIVE_PIN_COMMENT, jsonObject1);
        }
    }

    private void showOfflineLayout() {
        txtLive.setText("Offline");
        imgChat.setVisibility(View.VISIBLE);
        recyclerviewLivemoduleChat.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.dimen._320sdp));
        linear.setVisibility(View.GONE);
    }

    private void showOnineLayout() {
        linear.setVisibility(View.VISIBLE);
        txtLive.setText("Live");
        imgChat.setVisibility(View.GONE);
        recyclerviewLivemoduleChat.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.dimen._220sdp));
    }

    private void showHideoptions() {
        if (!sellerId.equalsIgnoreCase(HelperPreferences.get(MainActivityLiveStream.this).getString(UID))) {
            //   relLayMoreLive.setVisibility(View.INVISIBLE);
            //  relLayMoreLive.setEnabled(false);
        } else {
            //   relLayMoreLive.setEnabled(true);
            //  relLayMoreLive.setVisibility(View.VISIBLE);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);


            txtDuration.setText("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) /*+ ":"
                    + String.format("%03d", MilliSeconds)*/);

            handler.postDelayed(this, 0);
        }

    };
    public Runnable runnableViwer = new Runnable() {

        public void run() {

         /*   try {
                MillisecondTime = getMilisec(convertUTCToLocal(videoStartTime)) - StartTime;
            } catch (ParseException e) {
                e.printStackTrace();*/
            try {
                MillisecondTime = -StartTime - getMilisec(convertUTCToLocal(videoStartTime));
            } catch (ParseException e) {
                e.printStackTrace();
//                MilliSeconds = SystemClock.uptimeMillis()-StartTime;
            }
//            }
            try {
                txtDuration.setText(Global.getTimeDuration(Global.convertUTCToLocal(videoStartTime), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            /*txtDuration.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) *//*+ ":"
                    + String.format("%03d", MilliSeconds)*//*);
             */
            handler.postDelayed(this, 0);
        }

    };

    private long getMilisec(String time) throws ParseException {

//        Log.e("time_han", "getMilisec: " + time);
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(time);
        long milliseconds = date.getTime();
        long millisecondsFromNow = milliseconds - (new Date()).getTime();
        return milliseconds;
     /*   Toast.makeText(this, "Milliseconds to future date=" + millisecondsFromNow, Toast.LENGTH_SHORT).show();
        StopWatch timer = new StopWatch();*/
    }

    @OnClick(R.id.img_unpin_comment)
    public void onUnPinCommentClicked() {
        Log.e("CommentUnPin", "onPinLiveCommentClick: " + "");
        if (!TextUtils.isEmpty(groupId)) {

            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("group_id", groupId);
                jsonObject1.put("comment_id", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("CommentPinParams", "onPinLiveCommentClick: " + jsonObject1);
            mSocket.emit(EVENT_LIVE_PIN_COMMENT, jsonObject1);
        }
    }

    @OnClick(R.id.flipCamera)
    public void onFlipClicked() {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipping);
        anim.setTarget(flipCamera);
        anim.setDuration(1500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                anim.start();
                if (isFliped) {
                    flipCamera.setBackgroundResource(R.drawable.switch_camera_icon);
                } else {
//                    flipCamera.setBackgroundResource(R.mipmap.front_cam_icon);
                }

            }
        });
        if (isFliped) {
            webRTCClient.switchCamera();
            isFliped = false;
//            anim.start();
        } else {
//            flipCamera.setBackgroundResource(R.mipmap.front_cam_icon);
            webRTCClient.switchCamera();
//            webRTCClient.toggleMic();
            isFliped = true;
//            anim.start();
        }

    }


    @Override
    public void onBackPressed() {
//
        isbackPressed = true;
        if (showPublishPlay.equalsIgnoreCase("LiveStream")) {
            S_Dialogs.getLiveConfirmationVideo(MainActivityLiveStream.this, "Video will no longer be visible.Are you sure you want to stop this live session.", (dialog, which) -> {
                if (webRTCClient != null) {
                    webRTCClient.stopStream();
                }
                finish();
            }).show();
        } else {
            S_Dialogs.getLiveConfirmationVideo(MainActivityLiveStream.this, "Are you sure you want to leave from this live session.", (dialog, which) -> {

                // socket to remove views
                if (!TextUtils.isEmpty(groupId)) {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("group_id", groupId);
                        jsonObject1.put("isJoined", "N");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit(EVENT_COUNTVIEWS, jsonObject1);
                }
                if (webRTCClient != null) {
                    webRTCClient.stopStream();
                }
                finish();
            }).show();
        }
//        super.onBackPressed();
    }

    private void dismissDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onUpdateVideoValues(String productName1, String category, String price, String description, String cat_Id, String imagePath1) {

        coverImagePath = imagePath1;
        productName = productName1;
        txtProductname.setText(productName1);
        productDescription = description;
        productCategory = category;
        catId = cat_Id;
        loadingDialog.show();

        Log.e("onUpdateVideoValues: ", coverImagePath);
        streamId = "stream" + (int) (Math.random() * 999);
        // streamId = "stream438";
        webRTCClient = new WebRTCClient(MainActivityLiveStream.this, MainActivityLiveStream.this);
        webRTCClient.setOpenFrontCamera(false);
        tokenId = "tokenId";
        SurfaceViewRenderer cameraViewRenderer = findViewById(R.id.camera_view_renderer);
        SurfaceViewRenderer pipViewRenderer = findViewById(R.id.pip_view_renderer);
        webRTCClient.setVideoRenderers(pipViewRenderer, cameraViewRenderer);
        webRTCClient.init(SERVER_URL, streamId, IWebRTCClient.MODE_PUBLISH, tokenId);
        webRTCClient.startStream();


    }

    @Override
    public void onCancelDetailDialog() {
        finish();
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                Log.e("receiver", "Got message: ChatFragment" + message.getMessage() + ":" + message.getNotiType());
                if (message.getNotiType().equalsIgnoreCase(SAConstants.NotificationKeys.NT_OFFER_LIVE_MAKE)) {
                    Log.e("MakeOfferData", "onReceive: ");
//                    getChatData();
                    txtOfferFromSeller.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                Log.e("Receiver", "onReceive: " + e.getMessage());
            }
        }
    };

    @OnClick(R.id.txt_offer_from_seller)
    public void onOfferUIClicked() {
        if (ownerId.isEmpty()) {
            getChatData(true);
        } else {
            offerClick();
        }
    }

    private void offerClick() {
        try {

            Log.e("OwnerInfo", "onOfferUIClicked: " + ownerId + ":" + ownerImage + ":" + ownerUserName);
            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("otherUserId", ownerId);
            resultIntent.putExtra("otherUserImage", ownerImage);
            resultIntent.putExtra("otherUserName", ownerUserName);
            startActivity(resultIntent);
        } catch (Exception e) {

        }
    }


    public void alertsendoffer(String senderid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Send Offer");

        final EditText one = new EditText(this);
        one.setHint("Enter Name");//optional
        final EditText two = new EditText(this);
        two.setHint("Enter Price");//optional
        Global.CheckPriceDecimal(two);
        one.setInputType(InputType.TYPE_CLASS_TEXT);
        two.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LinearLayout lay = new LinearLayout(this);
        lay.setPadding(12, 0, 12, 0);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(one);
        lay.addView(two);
        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (TextUtils.isEmpty(two.getText().toString()) || two.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(MainActivityLiveStream.this, "Enter offering amount", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                } else {
//                makeOfferApi(input.toString().trim(), sellerId, dialog, productId);

                    if (one.getText().toString().trim().isEmpty()) {
                        productname_1 = productName;
                    } else {
                        productname_1 = one.getText().toString();
                    }
                    sendOfferApi(productname_1, two.getText().toString().trim(), senderid);
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

    public byte[] bytes(String path) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] by = stream.toByteArray();
        return by;
    }


    //------------------generate live video url---------------------
    public void getProductUrlApi(String productId) {
        Call<JsonObject> productUrl;
        productUrl = service.getProductUrl(HelperPreferences.get(this).getString(UID), productId, "v");
        productUrl.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            Global.DEEP_LINKING_PRODUCT_URL = jsonObject.getString("url");
                            //   Log.e("printVideoUrl",Global.DEEP_LINKING_PRODUCT_URL);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void animImage(final Context context) {
        // Load the animation like this
        //  final Animation animRightToLeft = AnimationUtils.loadAnimation(context, R.anim.anim_move_right_to_left);
        final Animation animRightToLeft = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        recyclerview_livemodule_chat.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        // Start the animation like this
        recyclerview_livemodule_chat.startAnimation(animRightToLeft);
    }

    private void hitReportApi(String str) {

        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Call<Common> reportCall = service.reportApi(HelperPreferences.get(this).getString(UID), str, productId, sellerId);
        reportCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(MainActivityLiveStream.this, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    try {
                        Log.e("errorResponse", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {

            }
        });
    }


}
