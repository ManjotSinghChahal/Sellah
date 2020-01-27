package com.app.admin.sellah.view.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.PermissionCheckUtil;
import com.app.admin.sellah.model.AddProductDatabase;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.admin.sellah.controller.utils.Global.makeTransperantStatusBar;

public class Video_capture_activity extends AppCompatActivity implements SurfaceHolder.Callback {
    @BindView(R.id.surface_camera)
    SurfaceView surfaceCamera;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.anim_ciricleimage)
    ImageView animCiricleimage;
    @BindView(R.id.video_back)
    Button videoBack;
    @BindView(R.id.rel_instructions)
    RelativeLayout relInstructions;
    @BindView(R.id.img_corner)
    ImageView imgCorner;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    Button mbutton;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    int i = 0;
    File video;
    private Camera mCamera;
    boolean isActionDown, isstopped;
    private int mOrientation = 0;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeTransperantStatusBar(this, true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_videocapture);
        ButterKnife.bind(this);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mbutton = (Button) findViewById(R.id.video);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);

        PermissionCheckUtil.create(this, new PermissionCheckUtil.onPermissionCheckCallback() {
            @Override
            public void onPermissionSuccess() {
                Log.i(null, "Video starting");

                mCamera = Camera.open();
                int cameraId = -1;
                int numberOfCameras = Camera.getNumberOfCameras();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        cameraId = i;
                        break;
                    }
                }

                setCameraDisplayOrientation(Video_capture_activity.this, cameraId, mCamera);
                surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
                surfaceHolder = surfaceView.getHolder();

                surfaceHolder.addCallback(Video_capture_activity.this);
                surfaceHolder.setSizeFromLayout();
//                surfaceHolder.l;
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


            }
        });




        mbutton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                relInstructions.setVisibility(View.GONE);

                // start recording..............
                try {

                    isActionDown = true;
                    animCiricleimage.setVisibility(View.VISIBLE);
                    animCiricleimage.setAnimation(anim);
                    startRecording();
                } catch (Exception e) {
                    String message = e.getMessage();
                    Log.i(null, "Problem Start" + message);
                    mrec.release();
                }
                mCountDownTimer = new CountDownTimer(15000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                        isstopped = false;
                        i++;
                        mProgressBar.setProgress((int) i * 50 / (15000 / 1000));
                        Log.v("Log_tag", "" + (int) i * 50 / (15000 / 1000));
                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        isActionDown = false;
                        i++;
                        mProgressBar.setProgress(100);
                        if (!isstopped) {
                            stopRecording();
                            isstopped = true;
                        }

                        Log.v("Log_tag", "stop");

                    }
                };
                mCountDownTimer.start();
                return false;
            }
        });


        mbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (isActionDown) {
                            ;

                            mCountDownTimer.cancel();
                            if (!isstopped) {
                                stopRecording();
                                isstopped = true;
                            }

                        }

                        break;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "StartRecording");
        menu.add(0, 1, 0, "StopRecording");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                try {
                    startRecording();
                } catch (Exception e) {
                    String message = e.getMessage();
                    Log.i(null, "Problem Start" + message);
                    mrec.release();
                }
                break;

            case 1: //GoToAllNotes
                /*mrec.stop();
                mrec.release();
                mrec = null;*/
                stopRecording();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startRecording() throws IOException {
        mrec = new MediaRecorder();  // Works well
        mCamera.unlock();

        mrec.setCamera(mCamera);
        mrec.setOrientationHint(mOrientation);
        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));

        //  mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //  mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        //  mrec.setVideoEncodingBitRate(690000 );

        /*mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mrec.setVideoEncodingBitRate(512 * 1000);
        mrec.setVideoFrameRate(30);
        mrec.setVideoSize(640, 480);
*/

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        Global.videopath = Environment.getExternalStorageDirectory().getPath().concat("/" + String.valueOf(System.currentTimeMillis()) + ".3gp");
        mrec.setOutputFile(Global.videopath);

        AddProductDatabase.videoUrl = Global.videopath;

        // mrec.setOutputFile("/sdcard/zzzz.3gp");
        Log.e("startRecording: ", Global.videopath);
        mrec.prepare();
        mrec.start();

    }

    protected void stopRecording() {
        if (mrec != null) {
            mrec.stop();
            mrec.release();
        }

        mCamera.release();
        Intent intent = new Intent(this, Previewvideo.class);
        startActivity(intent);
        finish();


    }

    private void releaseMediaRecorder() {
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
               /*mCamera.setPreviewCallback(null);
               mCamera.stopPreview();
               mCamera.release();*/


    }

    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, Camera camera) {

        Camera.CameraInfo info =
                new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        mOrientation = result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Global.videopath = "no_image";
        AddProductDatabase.videoUrl = "";
    }

    @OnClick(R.id.anim_ciricleimage)
    public void onViewClicked() {
    }

    @OnClick({R.id.video_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_back:
                finish();
                break;

        }
    }
}
