package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.LiveVideoDesc.LiveVideoDescModel;
import com.app.admin.sellah.view.activities.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class Stripe_image_verification_dialogfragment extends DialogFragment {


    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.live_imgview)
    ImageView liveImgview;
    @BindView(R.id.live_imgview_name)
    TextView liveImgviewName;
    @BindView(R.id.dummylive1_text)
    TextView dummylive1Text;
    @BindView(R.id.dummylive2_text)
    TextView dummylive2Text;
    @BindView(R.id.editimage_btn_live)
    Button editimageBtnLive;
    @BindView(R.id.li_take_picture)
    RelativeLayout liTakePicture;
    @BindView(R.id.addmore_photos_btn)
    Button addmorePhotosBtn;
    @BindView(R.id.stripeimage_addbtn)
    Button stripeimageAddbtn;
    Unbinder unbinder;
    @BindView(R.id.txt_mediakit_dec)
    TextView txtMediakitDec;
    private int CAMERA_PIC_REQUEST = 102;
    private String imagePath = "";
    WebService service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stripe_image_verification, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        service = Global.WebServiceConstants.getRetrofitinstance();
        unbinder = ButterKnife.bind(this, view);




        ClickableSpan first = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                String url = "https://stripe.com/docs/connect/updating-accounts#tos-acceptance";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }

        };

        ClickableSpan seoond = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                String url = "https://stripe.com/us/connect-account/legal";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }

        };
        makeLinks(txtMediakitDec, new String[] {
                "Services Agreement","Stripe Connected Account Agreement"
        }, new ClickableSpan[] {
                first,seoond
        });

        return view;
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());


        Log.e( "makeLinks: ",""+ links.length);
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void onActivityResult(int requestCode, int code, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && code == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.e("LiveProductDialog", "onActivityResult: " + extras.get("data"));
//            String imgString = Base64.encodeToString(getBytesFromBitmap(imageBitmap),
//                    Base64.NO_WRAP);
//            Log.e("ImageString", "onActivityResult: "+imgString);
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), imageBitmap);
            dummylive1Text.setVisibility(View.GONE);
            dummylive2Text.setVisibility(View.GONE);
            liveImgview.setVisibility(View.VISIBLE);
            liveImgviewName.setVisibility(View.VISIBLE);
            editimageBtnLive.setVisibility(View.VISIBLE);
            liTakePicture.setBackgroundResource(R.drawable.live_product_detail_background);
            stripeimageAddbtn.setEnabled(true);
            stripeimageAddbtn.setBackgroundResource(R.drawable.round_red_border_testimonial);
            Glide.with(getActivity()).applyDefaultRequestOptions(new RequestOptions().transform(new RoundedCorners(10)))
                    .load(tempUri).into(liveImgview);
            liveImgviewName.setText(getFileName(tempUri));
            imagePath = getRealPathFromURI(tempUri);
            Log.e("ImageString", "onActivityResult: " + imagePath);
//            uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.editimage_btn_live, R.id.li_take_picture, R.id.stripeimage_addbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.editimage_btn_live:
                takePhotoFromCamera();
                break;
            case R.id.li_take_picture:
                takePhotoFromCamera();
                break;
            case R.id.stripeimage_addbtn:
                if (imagePath.isEmpty()) {
                    Snackbar.make(getActivity().getWindow().getDecorView(), "Please capture the image.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else {
                    addimage(imagePath);
                }
                break;
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void addimage(String imageUri) {

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(imageUri));
        MultipartBody.Part multipartimage1 = MultipartBody.Part.createFormData("identity_document", "stripedoc.jpeg", image);
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<LiveVideoDescModel> getProfileCall = service.stripe_imageverification(RequestBody.create(MediaType.parse("text/plain"), HelperPreferences.get(getActivity()).getString(UID)), multipartimage1);
        getProfileCall.enqueue(new Callback<LiveVideoDescModel>() {
            @Override
            public void onResponse(Call<LiveVideoDescModel> call, Response<LiveVideoDescModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(getActivity().getWindow().getDecorView(), response.body().getMessage().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    ((MainActivity) getActivity()).getProfileData();
                    dismiss();
                    Log.e("imagever", "onSuccess" + response.body().getMessage().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong.", Snackbar.LENGTH_SHORT)
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
                Snackbar.make(getActivity().getWindow().getDecorView(), "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();

                Log.e("add_desc_api", "onFailure: " + t.getMessage());
            }
        });

    }

    public byte[] bytes(String path) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] by = stream.toByteArray();
        return by;
    }


}
