package com.app.admin.sellah.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Admin on 11/26/2018.
 */

public class VideoPlayerActivity extends AppCompatActivity {
    VideoView video_fullScreen;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.btn_menu)
    ImageButton btnMenu;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.card_top_view)
    CardView cardTopView;
    @BindView(R.id.video_fullScreen)
    VideoView videoFullScreen;
    @BindView(R.id.img_videoPlayer)
    ImageView imgVideoPlayer;
    @BindView(R.id.txt_views)
    TextView txtViews;
    @BindView(R.id.txt_live)
    TextView txtLive;
    @BindView(R.id.videoview_categories_play)
    ImageView videoviewCategoriesPlay;
    @BindView(R.id.img_send_camera)
    ImageView imgSendCamera;
    @BindView(R.id.img_send_gallery)
    ImageView imgSendGallery;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.card_bottom_view)
    CardView cardBottomView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_players);
        Global.StatusBarLightMode(this);
        ButterKnife.bind(this);

      /*  video_fullScreen = findViewById(R.id.video_fullScreen);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("video_url");

        Uri uri_ = Uri.parse(uri);
        video_fullScreen.setVideoURI(uri_);
        video_fullScreen.start();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
//        video_fullScreen.pause();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
