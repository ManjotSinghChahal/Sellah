package com.app.admin.sellah.view.activities;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.AddProductDatabase;

import java.io.File;

import static com.app.admin.sellah.controller.utils.Global.makeTransperantStatusBar;

public class Previewvideo extends AppCompatActivity {


  VideoView prevewvideo;
  Button delete,check,play,pause;
  int stopposition = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeTransperantStatusBar(this, true);
        setContentView(R.layout.activity_videopreview);
        prevewvideo = findViewById(R.id.videoview);
        check = findViewById(R.id.check);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        delete = findViewById(R.id.delete);

        if (getIntent().getExtras()!=null)
        {
            Uri uri = Uri.parse(getIntent().getStringExtra("video"));
            prevewvideo.setVideoURI(uri);
            prevewvideo.start();
            check.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);

        }
        else
        {
            Uri uri = Uri.parse(Global.videopath);
            prevewvideo.setVideoURI(uri);
            prevewvideo.start();
        }





         play.setVisibility(View.GONE);
         pause.setVisibility(View.VISIBLE);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File dir = getFilesDir();
                File file = new File(Global.videopath);
                boolean deleted = file.delete();
                Log.e( "onClick: ",""+deleted );
                Global.videopath = "no_image";
                finish();

            }
        });

        prevewvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                stopposition=0;

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                stopposition = prevewvideo.getCurrentPosition();
                prevewvideo.pause();

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                if (stopposition==0)
                {
                    if (getIntent().getExtras()!=null)
                    {
                        Uri uri = Uri.parse(getIntent().getStringExtra("video"));
                        prevewvideo.setVideoURI(uri);
                        prevewvideo.start();


                    }
                    else
                    {
                        Uri uri = Uri.parse(Global.videopath);
                        prevewvideo.setVideoURI(uri);
                        prevewvideo.start();
                    }

                }
                else
                {
                    prevewvideo.seekTo(stopposition);
                    prevewvideo.start();
                }

            }
        });



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductDatabase.videoUrl = Global.videopath;
                 finish();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
