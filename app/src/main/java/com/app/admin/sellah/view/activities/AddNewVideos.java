package com.app.admin.sellah.view.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SellProductInterface;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.ProductImage;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.NewVideo_Sell_Better_bottom_Dialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.Add_Product_Cars_Adapter;
import com.app.admin.sellah.view.adapter.NewProductAddvideo_Adapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.adapter.ImagePickAdapter;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Build.VERSION_CODES.M;
import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.ImageFilePath.getPath;
import static com.app.admin.sellah.controller.utils.ImageUploadHelper.createImageFile;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.ADVERTISEMENTSTATUS;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;
import static org.webrtc.ContextUtils.getApplicationContext;

public class AddNewVideos extends AppCompatActivity implements SellProductInterface {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    List<Integer> list;
    @BindView(R.id.addnewvide_back)
    ImageView addnewvideBack;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.dummylive1_text)
    TextView dummylive1Text;
    @BindView(R.id.dummylive2_text)
    TextView dummylive2Text;
    @BindView(R.id.rl_addnewvideo)
    RelativeLayout rlAddnewvideo;
    @BindView(R.id.addmore_photos_btn)
    Button addmorePhotosBtn;
    @BindView(R.id.addnewvideo_btn_nxt)
    Button addnewvideoBtnNxt;
    final int CAMERA_PIC_REQUEST = 1313;
    @BindView(R.id.thumbnail_video)
    ImageView thumbnailVideo;
    @BindView(R.id.dummylive2_text1)
    TextView dummylive2Text1;
    @BindView(R.id.rl_addnewvideo1)
    RelativeLayout rlAddnewvideo1;
    @BindView(R.id.plus)
    TextView plus;
    @BindView(R.id.deleteimg)
    ImageView deleteimg;

    private int GALLERY = 1212;
    List<String> imageList = new ArrayList<>();
    NewProductAddvideo_Adapter newProductAdapter;
    Add_Product_Cars_Adapter add_product_cars_adapter;
    ArrayList<String> filepaths = new ArrayList<>();
    ArrayList<HashMap<String,String>> filepathsID = new ArrayList<>();
    Context context;
    private String cameraImageFilepath = "";
    String productStatus="" , thumbnail = "" , productVideo= "";
    int pos;
    JSONArray productImages; //----------Added by Arvind 17_5_2019---------
    JSONObject productJson;
    String product   = "";
    Result sales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // makeTransperantStatusBar(this, false);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_videos);
        ButterKnife.bind(this);
        context = this;

        //--------------------editing product(to update product data)------------------------------

        if (getIntent()!=null && getIntent().hasExtra("way_status"))
        {
          String _pos =  getIntent().getStringExtra("position");
          pos = Integer.parseInt(_pos);

        //    sales = getIntent().getParcelableArrayListExtra("modelProductList");
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
         //   sales = bundle.getParcelableArrayList("modelProductList");


            sales = bundle.getParcelable("modelProductList");

           // Gson gson = new Gson();
           // String res = gson.toJson(sales);
          //  Log.e("AddNewVideosPrint",res);

            AddProductDatabase.imageListGStatus.clear();

            try {

                product = bundle.getString("Product");
                productJson  = new JSONObject(product);
                productImages = productJson.getJSONArray("product_images");
                thumbnail = productJson.getString("product_video_thumbnail");
                productVideo = productJson.getString("product_video");

            for (int i = 0; i <productImages.length() ; i++)
            {
                JSONObject obj = productImages.getJSONObject(i);
                filepaths.add(obj.getString("image"));
              //  filepathsID.add();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("image_url",obj.getString("image"));
                hashMap.put("image_id",obj.getString("_id"));
                hashMap.put("image_status","false");
                AddProductDatabase.imageListGStatus.add(hashMap);

            }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("printImageUrl",filepathsID+"");
            Log.e("printImageUrl---",AddProductDatabase.imageListGStatus.size()+"");

            if (filepaths.size()>0)
            {

                //------------setting visibility of images visible and other text gone on getting res frm prev screen---
                dummylive1Text.setVisibility(View.GONE);
                dummylive2Text.setVisibility(View.GONE);
                rlAddnewvideo.setBackgroundResource(R.drawable.live_product_detail_background);
                recycler.setVisibility(View.VISIBLE);
                addmorePhotosBtn.setVisibility(View.VISIBLE);
            }

            add_product_cars_adapter = new Add_Product_Cars_Adapter(this, filepaths, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(add_product_cars_adapter);



            //------------showing videoThumbnail on getting video from prev screen----------------------------
            if (thumbnail!=null && !thumbnail.equalsIgnoreCase(""))
            {
               Global.videopath = thumbnail;
                thumbnailVideo.setVisibility(View.VISIBLE);
                rlAddnewvideo1.setVisibility(View.VISIBLE);
                plus.setVisibility(View.GONE);
                dummylive2Text1.setVisibility(View.GONE);
                deleteimg.setVisibility(View.VISIBLE);
                thumbnailVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(thumbnail).into(thumbnailVideo);

                AddProductDatabase.videoUrl = productVideo;
                AddProductDatabase.videoThumbnail = thumbnail;



                }
            else
            {
                Global.videopath = "no_image";

                AddProductDatabase.videoUrl = "";
                AddProductDatabase.videoThumbnail = "";
            }
            productStatus="update";
        }

        else
        {
            productStatus = "add";
            Global.videopath = "no_image";


            if (HelperPreferences.get(this).getString(ADVERTISEMENTSTATUS).equalsIgnoreCase("show"))
            {
                new NewVideo_Sell_Better_bottom_Dialog(this).show();
                HelperPreferences.get(this).saveString(ADVERTISEMENTSTATUS, "hide");
            }




            add_product_cars_adapter = new Add_Product_Cars_Adapter(this, filepaths, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(add_product_cars_adapter);
        }





    }
    @OnClick({/*R.id.deleteimg,*/ R.id.addnewvide_back, R.id.rl_addnewvideo, R.id.addmore_photos_btn, R.id.addnewvideo_btn_nxt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
/*            case R.id.deleteimg:

                break;*/
            case R.id.addnewvide_back:
                onBackPressed();
                break;
            case R.id.rl_addnewvideo:
                rlAddnewvideo.setBackgroundResource(R.drawable.live_product_detail_background);
                recycler.setVisibility(View.VISIBLE);
                addmorePhotosBtn.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= M) {

                    if (ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        showPictureDialog();

                    } else {

                        ActivityCompat.requestPermissions(AddNewVideos.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    showPictureDialog();
                }

                break;
            case R.id.addmore_photos_btn:
                if (Build.VERSION.SDK_INT >= M) {
                    if (ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        showPictureDialog();

                    } else {

                        ActivityCompat.requestPermissions(AddNewVideos.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    showPictureDialog();
                }
                break;
            case R.id.addnewvideo_btn_nxt:

                Log.e("filepaths",AddProductDatabase.videoUrl);

                if (productStatus.equalsIgnoreCase("add"))
                {
                    if (filepaths == null || filepaths.size()==0) {
                        Toast.makeText(this, "Please select atleast one image!", Toast.LENGTH_SHORT).show();
                    } else {
                        AddProductDatabase.imageListG.clear();
                        AddProductDatabase.imageListG.addAll(filepaths);
                        Intent intent = new Intent(AddNewVideos.this, AddNewInfo.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    AddProductDatabase.imageListG.clear();
                    AddProductDatabase.imageListG.addAll(filepaths);


                    Intent intent = new Intent(AddNewVideos.this, AddNewInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("modelProductList", sales);
                    intent.putExtra("wayStatus","addNewVideo");
                    intent.putExtra("Product",product);
                    intent.putExtra("position",String.valueOf(pos));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }



                break;
        }


    }

    private void showPictureDialog() {


        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Add from your gallery",
                "Take a photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dummylive1Text.setVisibility(View.GONE);
                                dummylive2Text.setVisibility(View.GONE);

                                choosePhotoFromGallary();
                                break;
                            case 1:
                                dummylive1Text.setVisibility(View.GONE);
                                dummylive2Text.setVisibility(View.GONE);
                                takePhotoFromCamera();

                                break;
                        }
                    }

                });
        pictureDialog.show();
        /*ImagePicker imagePicker = ImagePicker.create(this)
                .language("in") // Set image picker language
                .folderMode(false) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .toolbarArrowColor(Color.RED) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarDoneButtonText("DONE"); // done button text

        imagePicker.limit(5)

                .language("English")// max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()) // can be full path
                .start();*/

    }


    private void takePhotoFromCamera() {

        openCameraIntent();

    }


    private void choosePhotoFromGallary() {

        /*Intent intent1 = new Intent(AddNewVideos.this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, false);
        intent1.putExtra(Constant.MAX_NUMBER, 5);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);*/


        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY);

        // startActivityForResult(galleryIntent, GALLERY);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {





        if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<ImageFile> photoPaths = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);

                for (int i = 0; i < photoPaths.size(); i++) {
                    if (!filepaths.contains(photoPaths.get(i).getPath())) {
                        filepaths.add(photoPaths.get(i).getPath());
                    }

                }

               /* add_product_cars_adapter = new Add_Product_Cars_Adapter(this, filepaths, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recycler.setLayoutManager(layoutManager);
                recycler.setAdapter(add_product_cars_adapter);*/
                add_product_cars_adapter.notifyDataSetChanged();

            }
        }


        if (requestCode == GALLERY) {

               //------------------------------
                if ( null != data && null != data.getClipData()) {

                    ClipData mClipData = data.getClipData();

                   /* Toast.makeText(this, "You picked " +
                                    (mClipData.getItemCount() > 1 ? mClipData.getItemCount() + " Images" :
                                            mClipData.getItemCount() + " Image"),
                            Toast.LENGTH_LONG).show();*/


                   if (filepaths.size()+mClipData.getItemCount()<=5)
                   {
                       int pickedImageCount;
                       for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount(); pickedImageCount++)
                       {

                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                               String path=    getPath(this,mClipData.getItemAt(pickedImageCount).getUri());
                               imageList.add(path);
                               filepaths.add(path);
                           }
                       }
                       add_product_cars_adapter.notifyDataSetChanged();
                   }
                   else
                   {
                     int val = 5-filepaths.size();

                     if (val==0)
                       Toast.makeText(this, "You can add max 5 images", Toast.LENGTH_LONG).show();

                     else
                       Toast.makeText(this, "You can add only "+ val +" more images", Toast.LENGTH_LONG).show();
                   }

                }

                else
                    {

                        if (data!=null)
                        {
                            if (filepaths.size()<=4)
                            {
                            Uri uri = data.getData();
                            String path= getPath(this,uri);
                            imageList.add(path);
                            filepaths.add(path);
                            add_product_cars_adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(this, "You can add only 5 images", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                            Toast.makeText(this, "You haven't picked any Image", Toast.LENGTH_LONG).show();
                }


                //--------------------------------



        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {


            Log.e( "onActivityResult2: ",""+cameraImageFilepath );

            if (filepaths.size()<=4)
            {
                filepaths.add(cameraImageFilepath);
                add_product_cars_adapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(this, "You can add only 5 images", Toast.LENGTH_LONG).show();
            }


            /*add_product_cars_adapter = new Add_Product_Cars_Adapter(this, filepaths, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(add_product_cars_adapter);*/


        }

    }


    @Override
    public void setTagVisiblity(boolean visible) {

    }

    @Override
    public void setImageCaptureVisiblty(boolean visible) {

    }

    @OnClick(R.id.rl_addnewvideo1)
    public void onViewClicked() {

        if (Global.videopath.equals("no_image")) {
            Intent intent = new Intent(AddNewVideos.this, Video_capture_activity.class);
            startActivity(intent);
        } else {
            S_Dialogs.getLiveConfirmationVideo(AddNewVideos.this, "Are you sure you want to delete your short video?", (dialog, which) -> {
                thumbnailVideo.setVisibility(View.GONE);
                rlAddnewvideo1.setVisibility(View.VISIBLE);
                plus.setVisibility(View.VISIBLE);
                dummylive2Text1.setVisibility(View.VISIBLE);
                deleteimg.setVisibility(View.GONE);
                Global.videopath = "no_image";
            }).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.videopath.equals("no_image")) {

            thumbnailVideo.setVisibility(View.GONE);
            rlAddnewvideo1.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
            dummylive2Text1.setVisibility(View.VISIBLE);
            deleteimg.setVisibility(View.GONE);

        } else {

            thumbnailVideo.setVisibility(View.VISIBLE);
            rlAddnewvideo1.setVisibility(View.VISIBLE);
            plus.setVisibility(View.GONE);
            dummylive2Text1.setVisibility(View.GONE);
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(Global.videopath, MediaStore.Images.Thumbnails.MINI_KIND);
            thumbnailVideo.setImageBitmap(thumb);
            deleteimg.setVisibility(View.VISIBLE);
            thumbnailVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);


        }
    }

    /*camera code*/
    public void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            try {
                photoFile = createImageFile(this);
                cameraImageFilepath = photoFile.getAbsolutePath();
                Log.e("ImageUrl", "takePhotoFromCamera: " + cameraImageFilepath);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = /*FileProvider.getUriForFile(getApplicationContext(),                                                                                                    "com.example.android.provider", photoFile);
//                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);*/FileProvider.getUriForFile(getApplicationContext(), "com.app.admin.sellah.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        CAMERA_PIC_REQUEST);
                grantUriPermission(
                        "com.google.android.GoogleCamera",
                        photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            }
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


}
