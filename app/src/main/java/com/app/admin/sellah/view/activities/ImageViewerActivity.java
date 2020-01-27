package com.app.admin.sellah.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.app.admin.sellah.view.CustomViews.TouchImageView;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;

/**
 * Created by rohan on 12/7/17.
 */

public class ImageViewerActivity extends AppCompatActivity {

    @BindView(R.id.img_full_screen)
    TouchImageView imvFullScreen;

    int imgUrl;
    String imageString="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgUrl = bundle.getInt(Global.KEY_IMAGE_URL);

            imageString=bundle.getString(Global.KEY_IMAGE);
            if (imageString!=null&&!imageString.equalsIgnoreCase("")){
                Glide.with(this)
                        .load(imageString).apply(Global.getGlideOptions()).into(imvFullScreen);
            }else{
            /*    File f = new File(imageString);
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                imvFullScreen.setImageDrawable(d);*/
                imvFullScreen.setImageResource(imgUrl);
            }

        }
    }


    @OnClick(R.id.imv_close)
    void onClickClose() {
        finish();
    }


    public static void start(Context context,String url,int status) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(Global.KEY_IMAGE_URL, url);
        context.startActivity(intent);
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(Global.KEY_IMAGE, url);
        context.startActivity(intent);
    }

}
