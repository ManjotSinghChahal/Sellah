package com.app.admin.sellah.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.PermissionCheckUtil;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.model.extra.EditProfileModel;
import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.ProfilePagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.stripe.StripeSession.USERCITY;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.Global.rotateImage;
import static com.app.admin.sellah.controller.utils.ImageUploadHelper.REQUEST_CAPTURE_IMAGE;
import static com.app.admin.sellah.controller.utils.ImageUploadHelper.createImageFile;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILE_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILE_EDIT_MODE_IMAGE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_EMAIL;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_PROFILE_PIC;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static org.webrtc.ContextUtils.getApplicationContext;

public class MyAccountFragment extends Fragment {


    ViewPager vpPager;
    TabLayout tabLayout;

    Unbinder unbinder;

    @BindView(R.id.txt_change_profile_pic)
    TextView txtChangePic;


    @BindView(R.id.li_myAccountRoot)
    LinearLayout li_myAccountRoot;


    @BindView(R.id.img_user_profile)
    ImageView img_user_profile;
    @BindView(R.id.edtprofile_back)
    ImageView edtprofileBack;
    @BindView(R.id.txt_displatpicture)
    TextView txtDisplatpicture;
    @BindView(R.id.rl_displaypicture)
    RelativeLayout rlDisplaypicture;
    MultipartBody.Part multipartimage;

    private int CAMERA_PIC_REQUEST = 1213;
    private int GALLERY = 1214;

    public static String profileImagePathEdit;

    ProfileModel profileData;
    private WebService service;
    private String cameraImageFilepath = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);


        profileData = getArguments().containsKey(PROFILE_DATA) ? getArguments().getParcelable(PROFILE_DATA) : null;
        service = Global.WebServiceConstants.getRetrofitinstance();

        if (profileData != null) {
            setProfileData(profileData);
        }



        tabLayout = (TabLayout) view.findViewById(R.id.account_tabLayout);
        vpPager = (ViewPager) view.findViewById(R.id.account_viewPager);
        createViewPager(vpPager);
        tabLayout.setupWithViewPager(vpPager);
        if(getArguments().getString("from").equalsIgnoreCase("wallet"))
        {
            vpPager.setCurrentItem(1);
        }
        else
        {
            vpPager.setCurrentItem(0);
        }
//        createTabIcons();
        hideSearch();
        return view;

    }

    private void setProfileData(ProfileModel profileData) {
      /*  RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/
        RequestOptions requestOptions = Global.getGlideOptions();
//        requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);


        try {
            Picasso.with(getActivity()).load(profileData.getResult().getImage()).fit().centerCrop().
                    into(img_user_profile);
        } catch (Exception e) {
            Log.e("Exception", "setProfileData: " + e.getMessage());
        }
    }

    @OnClick(R.id.txt_change_profile_pic)
    public void onPicChange() {

        PermissionCheckUtil.create(getActivity(), new PermissionCheckUtil.onPermissionCheckCallback() {
            @Override
            public void onPermissionSuccess() {
                showPictureDialog();
            }
        });
        /*if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                       *//* if(imageList.size()!=8){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_PIC_REQUEST);
                        }else{
                            Snackbar.make(rootTag, "Only eight images are allowed per item.", Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();
                        }*//*
                showPictureDialog();

            } else {
                Log.e("Permission", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        } else {
            showPictureDialog();
        }*/
    }

    //custom tab layout inflate

   /* private void createTabIcons() {
        TextView saleTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_account, null);
        saleTab.setText("Account");
        tabLayout.getTabAt(0).setCustomView(saleTab);

        TextView wishTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_account, null);
        wishTab.setText("Payment");
        tabLayout.getTabAt(1).setCustomView(wishTab);

        TextView recordTab = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_account, null);
        recordTab.setText("Social");
        tabLayout.getTabAt(2).setCustomView(recordTab);
    }*/

    private void createViewPager(ViewPager viewPager) {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getChildFragmentManager());
        if (profileData!=null)
        {


        adapter.addFrag(new AccountTabFragment(profileData), "Account");
        try {


        adapter.addFrag(new PaymentFragment(profileData.getResult().getStripeId()), "Wallet");/* no need now */
        }catch (Exception e){
        }
       /* adapter.addFrag(new SocialFragment(), "Social");*/
        viewPager.setAdapter(adapter);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {

                    txtDisplatpicture.setVisibility(View.VISIBLE);
                    rlDisplaypicture.setVisibility(View.VISIBLE);

                } else {
                    txtDisplatpicture.setVisibility(View.GONE);
                    rlDisplaypicture.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).relativeLayout.setVisibility(View.GONE);

        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).title_account.setVisibility(View.VISIBLE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(4);

       /* Global.ProfileStatusCheck.checkProfileStatus(getActivity(), new Global.ProfileStatusCheck.ProfileStatusCallback() {
            @Override
            public void onIfProfileUpdated() {
                ((MainActivity) getActivity()).bottomNavigation.setVisibility(View.VISIBLE);
            }

            @Override
            public void onIfProfileNotUpdated() {
                ((MainActivity) getActivity()).bottomNavigation.setVisibility(View.GONE);
            }
        });*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
                                /*if (imageList.size() != 8) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                } else {
                                    Snackbar.make(rootTag, "Please select photo", Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
                                }*/
                                break;
                        }
                    }

                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
      /*  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);*/
        openCameraIntent();
//        if(imageFilepath!=null){

//        }
    }

    public void openCameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            try {
                photoFile = createImageFile(getActivity());
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
                        1005);
                getActivity().grantUriPermission(
                        "com.google.android.GoogleCamera",
                        photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            }
        }
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 100:
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (readExternalStorage && camera) {
                    showPictureDialog();
                } else {
                    Snackbar.make(li_myAccountRoot, "Unable to access camera", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((MainActivity) getActivity()).relativeLayout.setVisibility(View.GONE);
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri pickedImage = data.getData();

                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                profileImagePathEdit = imagePath;
                Log.e( "onActivityResult: ", ""+imagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap rotatedBitmap;
                /*switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }*/


                updateProfile();
                // Do something with the bitmap


                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();
               /* Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
//                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    img_user_profile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }*/
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);

            profileImagePathEdit = getRealPathFromURI(tempUri);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(getRealPathFromURI(tempUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(getRealPathFromURI(tempUri), options);
            Bitmap rotatedBitmap;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            Log.e( "onActivityResult1: ",profileImagePathEdit );
            updateProfile();
//            img_user_profile.setImageURI(tempUri) ;
            img_user_profile.setImageBitmap(rotatedBitmap);
//            Toast.makeText(getActivity(), "Here " + getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();
        }
        if (requestCode == 1005 && resultCode == RESULT_OK) {

           /* Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);

            profileImagePathEdit=getRealPathFromURI(tempUri);*/
            profileImagePathEdit = cameraImageFilepath;
            Uri uri = Uri.parse(profileImagePathEdit);
            Log.e( "onActivityResult2: ",""+uri );



      /*      ExifInterface ei = null;
            try {
                ei = new ExifInterface(cameraImageFilepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(cameraImageFilepath, options);
            Bitmap rotatedBitmap;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }*/


          updateProfile();
//            img_user_profile.setImageURI(tempUri);

//            img_user_profile.setImageBitmap(rotatedBitmap);
//            Toast.makeText(getActivity(), "Here " + getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();
        }
    }

    private void updateProfile() {
        EditProfileModel model = new EditProfileModel();
        model.setUser_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(getActivity()).getString(UID))));
        model.setUsername(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setDescription(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setCountry_code(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setPhone_number(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setAbout(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setShipping_policy(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setReturn_policy(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setAddress_name(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setAddress_1(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setAddress_2(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setPostal_code(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setState(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setAddress_city(RequestBody.create(MediaType.parse("text/plain"), ""));
        model.setEdit_mode(RequestBody.create(MediaType.parse("text/plain"), PROFILE_EDIT_MODE_IMAGE));
        if (!TextUtils.isEmpty(profileImagePathEdit)) {
            Log.e("ImageUrl", "updateProfile: " + profileImagePathEdit);

            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(profileImagePathEdit));
            multipartimage = MultipartBody.Part.createFormData("image", "profileimage.jpeg", image);
            model.setImage(multipartimage);
        }
        new ApisHelper().updateProfileApi(model, getActivity(), new ApisHelper.UpdateProfileCallback() {
            @Override
            public void onProfileUpdateSuccess(String msg) {

                Snackbar.make(li_myAccountRoot, "Profile picture updated.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
                getProfileData();

            }

            @Override
            public void onProfileUpdateFailure() {
                Snackbar.make(li_myAccountRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).relativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).relativeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.edtprofile_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
    public byte[] bytes(String path)
    {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] by= stream.toByteArray();
        return by;
    }

    public void getProfileData() {

        if (isLogined(getActivity()))// isLogined is a global method to get login status in app
        {
            //----------------------------------getProfileData API call------------------------------------


            new ApisHelper().getProfileData(getActivity(), new ApisHelper.GetProfileCallback() {
                @Override
                public void onGetProfileSuccess(JsonObject msg) {
                    Log.e("onGetProfileSuccess: ", msg
                            .toString());

                    try {
                        JSONObject jsonObject = new JSONObject(msg.toString());
                        String status = jsonObject.getString("status");


                        if (status.equalsIgnoreCase("1")) {
                            JSONObject result = jsonObject.getJSONObject("result");
                            HelperPreferences.get(getActivity()).saveString(STRIPE_VERIFIED, result.getString("stripe_verified"));
                            HelperPreferences.get(getActivity()).saveString(USERCITY, result.getString("city"));
                            HelperPreferences.get(getActivity()).saveString(USER_PROFILE_PIC, result.getString("image"));
                            HelperPreferences.get(getActivity()).saveString(USER_EMAIL, result.getString("email"));
                            ((MainActivity)getActivity()).navUsername.setText(result.getString("username"));
                            RequestOptions requestOptions = Global.getGlideOptions();

                            Picasso.with(getActivity()).load(result.getString("image")).fit().centerCrop().
                                    into(((MainActivity)getActivity()).navHeader);

                            Picasso.with(getActivity()).load(result.getString("image")).fit().centerCrop().
                                    into(img_user_profile);

                            Log.e( "stripe: ","d"+HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED) );
                            if (result.optString("stripe_id").isEmpty()) {
                                Log.e("onGetProfileSuccess: ", "yes");
                                HelperPreferences.get(getActivity()).saveString(API_ACCESS_TOKEN, "");
                            } else {
                                HelperPreferences.get(getActivity()).saveString(API_ACCESS_TOKEN, result.getString("stripe_id"));
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*Log.e( "onGetProfileSuccess: ",msg.getResult().getStripeId() );
                    HelperPreferences.get(MainActivity.this).saveString(USER_PROFILE_PIC, msg.getResult().getImage());
                    HelperPreferences.get(MainActivity.this).saveString(USER_EMAIL, msg.getResult().getEmail());
                    HelperPreferences.get(MainActivity.this).saveString(API_ACCESS_TOKEN, msg.getResult().getStripeId());
                    Log.e( "after: ",  HelperPreferences.get(MainActivity.this).getString(API_ACCESS_TOKEN ));*/
                }

                @Override
                public void onGetProfileFailure() {

                }
            });
        }

    }


}







