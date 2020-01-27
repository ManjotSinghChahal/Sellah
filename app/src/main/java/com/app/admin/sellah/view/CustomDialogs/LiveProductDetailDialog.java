package com.app.admin.sellah.view.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.Extras.ImageFilePath;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class LiveProductDetailDialog extends Dialog {

    UpdateDetailCallback callback;

    @BindView(R.id.downArrow)
    ImageView downArrow;
    @BindView(R.id.edt_add_name)
    EditText edtAddName;


    @BindView(R.id.edt_add_price)
    EditText edtAddPrice;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.filter_dialog_root)
    LinearLayout filterDialogRoot;
    @BindView(R.id.spinner_catagory)
    Spinner spinnerCatagory;
    @BindView(R.id.root_live)
    RelativeLayout rootLive;
    @BindView(R.id.relative_spinnerrl)
    RelativeLayout relativeSpinnerrl;
    @BindView(R.id.cancel_detail)
    ImageView cancelDetail;
    @BindView(R.id.il_add_price)
    NoChangingBackgroundTextInputLayout ilAddPrice;
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

    private ArrayList<EditText> allAddressFields;
    private ArrayList<TextInputLayout> allAddressInputLayouts;
    private HashMap<EditText, String> adressfieldsMessages;
    Context context;
    private String catId = "";
    private int CAMERA_PIC_REQUEST = 1212;
    private String imagePath = "";
    private int GALLERY=2025;

    public LiveProductDetailDialog(@NonNull Context context, UpdateDetailCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
    }

    public static LiveProductDetailDialog create(Context con, UpdateDetailCallback callback) {
        return new LiveProductDetailDialog(con, callback);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }
    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(galleryIntent, GALLERY);

    }


        private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.product_lay_details);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
         touchlistener();
        allAddressFields = new ArrayList<>();
        allAddressFields.add(edtAddName);
//        allAddressFields.add(edtAddCategory);
        allAddressFields.add(edtDescription);

        allAddressInputLayouts = new ArrayList<>();


        focuslisteneres();
        submit.setEnabled(false);
        spinnerCatagory.setGravity(Gravity.RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootLive.setElevation(9);
        }

        adressfieldsMessages = new HashMap<>();
        adressfieldsMessages.put(edtAddName, "Please enter your product name");


        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (context, R.layout.spinner_dropdown,
                        getCategoryList());
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinnerCatagory.setAdapter(conditionSpinAdapter);

        spinnerCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.e("category", item + " ");

                if (!item.toString().equalsIgnoreCase(
                        "Select Category")) {

                    catId = ExpandableListData.getCatId(item.toString());

                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);

                } else {

                    catId = "";
                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private List<String> getCategoryList() {
        GetCategoriesModel model = ExpandableListData.getData();
        List<String> catList = new ArrayList<>();
        catList.add("Select Category");
        for (int i = 0; i < model.getResult().size(); i++) {
            catList.add(model.getResult().get(i).getName());
        }
        return catList;
    }


    protected boolean isFormValid() {
        for (EditText editText : allAddressFields) {
            if (editText.getText().toString().trim().isEmpty()) {

                editText.requestFocus();

                return false;
            } else {

            }
        }
        return true;
    }

    @OnClick(R.id.cancel_detail)
    public void onCrossClicked() {
        onBackPressed();
    }

    @OnClick(R.id.li_take_picture)
    public void onCaptureViewClicked() {
        showPictureDialog();
        remove_focus();
    }

    @OnClick({R.id.editimage_btn_live, R.id.relative_spinnerrl, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.editimage_btn_live:
                showPictureDialog();
                break;

            case R.id.relative_spinnerrl:
                spinnerCatagory.performClick();

                break;
            case R.id.submit:

                if (isFormValid()) {
                    if (!spinnerCatagory.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
                        if (!TextUtils.isEmpty(imagePath)) {
                            Log.e("vvvvvvv","111111");
                            callback.onUpdateVideoValues(Global.getText(edtAddName), spinnerCatagory.getSelectedItem().toString(), Global.getText(edtAddPrice), Global.getText(edtDescription), catId, imagePath);
                            dismiss();
                        } else {
                            Toast.makeText(context, "Capture image for video cover", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
    }

    public interface UpdateDetailCallback {
        void onUpdateVideoValues(String productName, String category, String price, String description, String catId, String imagePath);

        void onCancelDetailDialog();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callback.onCancelDetailDialog();
    }

    public void onActivityResult(int requestCode, int code, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && code == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.e("LiveProductDialog", "onActivityResult: " + extras.get("data"));
//            String imgString = Base64.encodeToString(getBytesFromBitmap(imageBitmap),
//                    Base64.NO_WRAP);
//            Log.e("ImageString", "onActivityResult: "+imgString);
            Uri tempUri = getImageUri(context.getApplicationContext(), imageBitmap);
            dummylive1Text.setVisibility(View.GONE);
            dummylive2Text.setVisibility(View.GONE);
            liveImgview.setVisibility(View.VISIBLE);
            liveImgviewName.setVisibility(View.VISIBLE);
            editimageBtnLive.setVisibility(View.VISIBLE);
            liTakePicture.setBackgroundResource(R.drawable.live_product_detail_background);
            submit.setEnabled(true);
            submit.setBackgroundResource(R.drawable.round_red_border_testimonial);
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions().transform(new RoundedCorners(10)))
                    .load(tempUri).into(liveImgview);
            liveImgviewName.setText(getFileName(tempUri));
            imagePath = getRealPathFromURI(tempUri);
            Log.e("ImageString", "onActivityResult: " + imagePath);
//            uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));

        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri pickedImage = data.getData();

                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(filePath[0]));

                dummylive1Text.setVisibility(View.GONE);
                dummylive2Text.setVisibility(View.GONE);
                liveImgview.setVisibility(View.VISIBLE);
                liveImgviewName.setVisibility(View.VISIBLE);
                editimageBtnLive.setVisibility(View.VISIBLE);
                liTakePicture.setBackgroundResource(R.drawable.live_product_detail_background);
                submit.setEnabled(true);
                submit.setBackgroundResource(R.drawable.round_red_border_testimonial);
                Glide.with(context).applyDefaultRequestOptions(new RequestOptions().transform(new RoundedCorners(10)))
                        .load(path).into(liveImgview);
                liveImgviewName.setText(getFileName(pickedImage));


               imagePath = path;
                Log.e( "myactivity: ", ""+imagePath);






                cursor.close();

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
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void focuslisteneres() {

        edtAddName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtAddName.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {

                    if (TextUtils.isEmpty(edtAddName.getText().toString()))
                        edtAddName.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                        else
                    edtAddName.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });


        edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtDescription.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {

                    if (TextUtils.isEmpty(edtDescription.getText().toString()))
                        edtDescription.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                        else
                        edtDescription.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
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


    public void remove_focus()
    {
        edtDescription.clearFocus();
        edtAddName.clearFocus();
    }

    public void touchlistener()
    {
        spinnerCatagory.setOnTouchListener((view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                remove_focus();
            }
            return false;
        });
    }








}
