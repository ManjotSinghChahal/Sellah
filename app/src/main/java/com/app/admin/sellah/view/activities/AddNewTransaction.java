package com.app.admin.sellah.view.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.model.extra.AddProductModel;
import com.app.admin.sellah.model.extra.SpinnerStateCheck;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.AddProductPromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SpinnerStateAdapter;
import com.app.admin.sellah.view.fragments.HomeFragment;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class AddNewTransaction extends AppCompatActivity {


    @BindView(R.id.addnewtrans_back)
    ImageView addnewtransBack;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.spinner_payment_mode)
    Spinner spinnerPaymentMode;
    @BindView(R.id.spinner_mode_of_delivery)
    Spinner spinnerModeOfDelivery;
    @BindView(R.id.delivery_img)
    ImageView deliveryImg;
    @BindView(R.id.delivery_rl)
    RelativeLayout deliveryRl;
    @BindView(R.id.spin_sell_internationally)
    Spinner spinSellInternationally;
    @BindView(R.id.currency_img)
    ImageView currencyImg;
    @BindView(R.id.currency_rl)
    RelativeLayout currencyRl;
    @BindView(R.id.featured_sp)
    Spinner featuredSp;
    @BindView(R.id.featured_img)
    ImageView featuredImg;
    @BindView(R.id.feature_rl)
    RelativeLayout featureRl;
    @BindView(R.id.edt_budget)
    EditText edtBudget;
    @BindView(R.id.txt_no_of_clicks)
    TextView txtNoOfClicks;
    @BindView(R.id.add_product_post)
    Button addProductPost;
    ArrayList<SpinnerStateCheck> paymentOptions = new ArrayList<>();
    ArrayList<SpinnerStateCheck> deliveryOptions = new ArrayList<>();
    @BindView(R.id.txt_payment_type)
    TextView txtPaymentType;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.payemt_img)
    ImageView payemtImg;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.rootTag)
    RelativeLayout rootTag;
    @BindView(R.id.spinner_place)
    Spinner spinnerPlace;
    @BindView(R.id.txt_place)
    TextView txtPlace;
    @BindView(R.id.place_img)
    ImageView placeImg;
    @BindView(R.id.place_rl)
    RelativeLayout placeRl;
    private String paymentType = "";
    private String deliveryType = "";
    private Result productDetails;

    private boolean isEditing = false;
    boolean isPromotClicked = false;
    String isPromotingProduct = "N";
    private String promotepackageId = "NA";
    byte[] byteArray;
    GetProductList productList;
    MultipartBody.Part multipartimage;
    MultipartBody.Part multipartimage1;
    MultipartBody.Part multipartimage2;
    MultipartBody.Part multipartimage3;
    MultipartBody.Part multipartimage4;
    MultipartBody.Part multipartimage5;
    MultipartBody.Part mulitpartvideo;
    String productStatus = "add";
    String location = "";
    String image_id = "";
    boolean isVideoUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
*/
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_transaction);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("data"));
        ButterKnife.bind(this);
        setUpPaymentOptions();
        setUpDeliveryOptions();
        getdata();

        if (getIntent() != null && getIntent().hasExtra("productStatus")) {
            if (getIntent().getStringExtra("productStatus").equalsIgnoreCase("add"))
                productStatus = "add";
            else
                productStatus = "update";
        }


    }


    private void setUpPaymentOptions() {


        List<String> items = null;
        if (productDetails != null) {
            items = Arrays.asList(productDetails.getPaymentMode().split("\\s*,\\s*"));
        }
        final CharSequence[] select_paymentoption = getResources().getTextArray(R.array.add_product_payment_method);
        for (int i = 0; i < select_paymentoption.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_paymentoption[i].toString());
            if (items != null && items.size() > 0) {
                for (String option : items) {
                    if (select_paymentoption[i].toString().toLowerCase().contains(option)) {
                        stateVO.setSelected(true);
                    } else {
                        stateVO.setSelected(false);
                    }
                }

                paymentType = productDetails.getPaymentMode();


            } else {
                Log.e("come: ", AddProductDatabase.payment_method);

                if (!AddProductDatabase.payment_method.isEmpty()) {
                    paymentType = AddProductDatabase.payment_method;
                    txtPaymentType.setTextColor(getResources().getColor(R.color.colorBlack));
                    payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    rl3.setBackgroundResource(R.drawable.live_product_detail_background);

                } else {
                    paymentType = "Credit Card";
                    txtPaymentType.setTextColor(getResources().getColor(R.color.colorBlack));
                }


               /* ArrayList<String> selection = new ArrayList<>();
                if (select_paymentoption[i].toString().toLowerCase().contains("credit/debit")) {
                    stateVO.setSelected(true);

                    selection.add(select_paymentoption[i].toString());
//                    paymentType = select_paymentoption[i].toString();
                } else {
                    stateVO.setSelected(false);
                }
                paymentType = TextUtils.join(", ", selection);*/
            }

            txtPaymentType.setText(paymentType);
            paymentOptions.add(stateVO);
        }

        SpinnerStateAdapter myAdapterPaymentOtions = new SpinnerStateAdapter(this, 0, paymentOptions, true, "payment", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            paymentType = selection;


            if (TextUtils.isEmpty(selection)) {

                txtPaymentType.setText(select_paymentoption[0]);
                txtPaymentType.setTextColor(getResources().getColor(R.color.light_grey));
                payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                rl3.setBackgroundResource(R.drawable.live_product_detail_grey_background);

            } else {

                txtPaymentType.setText(selection);
                txtPaymentType.setTextColor(getResources().getColor(R.color.colorBlack));
                payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                rl3.setBackgroundResource(R.drawable.live_product_detail_background);


            }
        });
        spinnerPaymentMode.setAdapter(myAdapterPaymentOtions);

    }


    private void setUpDeliveryOptions() {


        List<String> items = null;
        if (productDetails != null) {
            items = Arrays.asList(productDetails.getDelivery().split("\\s*,\\s*"));

        }
//        List<String> items = Arrays.asList(productDetails.getDelivery().split("\\s*,\\s*"));
        final CharSequence[] selectDeliveryOption = getResources().getTextArray(R.array.add_product_delivery);

        for (int i = 0; i < selectDeliveryOption.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setSelected(true);
            stateVO.setTitle(selectDeliveryOption[i].toString());
            if (items != null && items.size() > 0) {
                for (String option : items) {
                    if (selectDeliveryOption[i].toString().toLowerCase().contains(option)) {
                        stateVO.setSelected(true);
                    } else {
                        stateVO.setSelected(false);
                    }
                }
                deliveryType = productDetails.getDelivery();

            } else {

                if (!AddProductDatabase.modes_of_delivery.isEmpty()) {
                    deliveryType = AddProductDatabase.modes_of_delivery;
                    txtDelivery.setTextColor(getResources().getColor(R.color.colorBlack));
                    deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    deliveryRl.setBackgroundResource(R.drawable.live_product_detail_background);
                } else {
                    ArrayList<String> selction = new ArrayList<>();
                    selction.add(selectDeliveryOption[0].toString());
                    deliveryType = TextUtils.join(", ", selction);
                    txtDelivery.setTextColor(getResources().getColor(R.color.light_grey));
                }


//                deliveryType = selectDeliveryOption[i].toString();
                // stateVO.setSelected(true);
            }

            txtDelivery.setText("Meet-up,Mail Item To Customer");
            txtDelivery.setTextColor(getResources().getColor(R.color.colorBlack));
            deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
            deliveryRl.setBackgroundResource(R.drawable.live_product_detail_background);
            deliveryType = txtDelivery.getText().toString();

            deliveryOptions.add(stateVO);
        }
        SpinnerStateAdapter myAdapterDelivery = new SpinnerStateAdapter(this, 0, deliveryOptions, true, "delivery", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            deliveryType = selection;
            if (TextUtils.isEmpty(selection)) {
                txtDelivery.setText(selectDeliveryOption[0]);
                txtDelivery.setTextColor(getResources().getColor(R.color.light_grey));
                deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                deliveryRl.setBackgroundResource(R.drawable.live_product_detail_dash_background);

            } else {
                txtDelivery.setText(selection);
                txtDelivery.setTextColor(getResources().getColor(R.color.colorBlack));
                deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                deliveryRl.setBackgroundResource(R.drawable.live_product_detail_background);


            }
        });
        spinnerModeOfDelivery.setAdapter(myAdapterDelivery);


        //----------------------------------------------------------------------

        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.currency));
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinSellInternationally.setAdapter(conditionSpinAdapter);

        spinSellInternationally.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.e("category", item + " ");
                if (!item.toString().equalsIgnoreCase(
                        "Select Currency")) {
                    ((TextView) spinSellInternationally.getSelectedView()).setTextColor(Color.BLACK);
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_background);

                } else {
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    ((TextView) spinSellInternationally.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //------places spinner--------------------------------------------------------------

        ArrayAdapter<String> placeSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.add_places));
        placeSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinnerPlace.setAdapter(placeSpinAdapter);

        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    Object item = parent.getItemAtPosition(position);
                String item = parent.getItemAtPosition(position).toString();

                location = item;

                Log.e("category_places", item + " ");
                txtPlace.setText(item);
               /* if (!item.toString().equalsIgnoreCase(
                        "Select Currency")) {
                    ((TextView) spinnerPlace.getSelectedView()).setTextColor(Color.BLACK);
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_background);

                } else {
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    ((TextView) spinnerPlace.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                }*/
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtPlace.setText("North");
        txtPlace.setTextColor(getResources().getColor(R.color.colorBlack));
        placeImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
        placeRl.setBackgroundResource(R.drawable.live_product_detail_background);


        //----------------------------------------------------------------------------


        ArrayAdapter<String> featureSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.feature));
        featureSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        featuredSp.setAdapter(featureSpinAdapter);

        featuredSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.e("category", item + " ");
                if (!item.toString().equalsIgnoreCase(
                        "Select option")) {
                    ((TextView) featuredSp.getSelectedView()).setTextColor(Color.BLACK);
                    featuredImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    featureRl.setBackgroundResource(R.drawable.live_product_detail_background);
                } else {
                    featuredImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    featureRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                    ((TextView) featuredSp.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    @OnClick({R.id.addnewtrans_back, R.id.rl3, R.id.delivery_rl, R.id.currency_rl, R.id.feature_rl, R.id.add_product_post, R.id.place_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewtrans_back:
                adddata();
                onBackPressed();
                break;
            case R.id.rl3:
                //  spinnerPaymentMode.performClick();
                break;
            case R.id.delivery_rl:
                spinnerModeOfDelivery.performClick();
                break;

            case R.id.place_rl:
                spinnerPlace.performClick();
                break;


            case R.id.currency_rl:
                //  spinSellInternationally.performClick();
                break;
            case R.id.feature_rl:
                featuredSp.performClick();
                break;
            case R.id.add_product_post:


                Log.e("onViewClicked---->: ", "" + AddProductDatabase.imageListG);
                Global.hideKeyboard(rootTag, this);

                if (AddProductDatabase.imageListG.size() == 0) {
                    Snackbar.make(rootTag, "Please add at-least one image for product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.name.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please enter product name", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.category == 0) {
                    Snackbar.make(rootTag, "Please select an category", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.sub_category == 0) {
                    Snackbar.make(rootTag, "Please select an subcategory", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (deliveryType.equalsIgnoreCase("") || deliveryType.equalsIgnoreCase("Select delivery mode")) {
                    Snackbar.make(rootTag, "Please select mode of delivery", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (spinSellInternationally.getSelectedItem().toString().equalsIgnoreCase("sell internationally")) {
            Snackbar.make(rootTag, "Please select selling strategy", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (AddProductDatabase.price.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (Global.getText(edtPrice).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (AddProductDatabase.condition == 0) {
                    Snackbar.make(rootTag, "Please select condition of product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.quantity.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please select quantity of product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (Global.getText(edtBudget).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter budget", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else {


                    AddProductModel model = new AddProductModel();
                    model.setUser_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(this).getString(UID))));
                    model.setName(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.name));
                    model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AddProductDatabase.catid)));
//            model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(subCatId)));
                    model.setSub_cat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AddProductDatabase.subcatid)));

                    model.setPayment_mode(RequestBody.create(MediaType.parse("text/plain"), "Credit Card"));
                    model.setDelivery(RequestBody.create(MediaType.parse("text/plain"), deliveryType));
                    model.setTags((RequestBody.create(MediaType.parse("text/plain"), TextUtils.join("|", AddProductDatabase.tagListG))));
                    model.setSell_internationally(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    model.setPrice(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.price.replace("S$", "")));

                    if (AddProductDatabase.type == 2) {
                        model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "Y"));
                    } else {
                        model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    }
                    if (AddProductDatabase.condition == 2) {
                        model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    } else {
                        model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "U"));
                    }
                    model.setQuantity(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.quantity));
                    model.setDescription(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.description));
                    model.setBudget(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtBudget).replace("S$", "")));
                    model.setNo_of_clicks(RequestBody.create(MediaType.parse("text/plain"), "0"));
                    model.setLocation(RequestBody.create(MediaType.parse("text/plain"), location));


                    Log.e("getAllImageList", AddProductDatabase.imageListG + "   Done");

                    //---------------for  editing photos-----------------
                    if (productStatus.equalsIgnoreCase("add")) {
                        Log.e("productStatus", "Add");
                    } else {
                        Log.e("productStatus", "Update");

                        ArrayList<String> arrayListFinal = new ArrayList<>();


                        for (int i = 0; i < AddProductDatabase.imageListGStatus.size(); i++) {

                            if (AddProductDatabase.imageListGStatus.get(i).get("image_status").equalsIgnoreCase("true")) {

                                if (image_id.equalsIgnoreCase(""))
                                    image_id = AddProductDatabase.imageListGStatus.get(i).get("image_id");
                                else
                                    image_id = image_id + "," + AddProductDatabase.imageListGStatus.get(i).get("image_id");
                            } else {

                            }

                        }


                        for (int j = 0; j < AddProductDatabase.imageListG.size(); j++) {

                            if (AddProductDatabase.imageListG.get(j).startsWith("http")) {

                            } else {
                                arrayListFinal.add(AddProductDatabase.imageListG.get(j));
                            }
                        }

                        AddProductDatabase.imageListG.clear();
                        AddProductDatabase.imageListG.addAll(arrayListFinal);

                    }
                    //-----------------------------------------------------------

                    Log.e("arrayListSize", AddProductDatabase.imageListG + "");
                    for (int i = 0; i < AddProductDatabase.imageListG.size(); i++) {

                        if (i == 0) {


                           /*try {
                                URL url = new URL(AddProductDatabase.imageListG.get(0));
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                                byte[] by= stream.toByteArray();
                               // return stream.toByteArray();

                                RequestBody images = RequestBody.create(MediaType.parse("image/*"), by);
                                multipartimage1 = MultipartBody.Part.createFormData("image1", "imageA.jpeg", images);

                            } catch(IOException e) {
                                System.out.println(e);
                            }*/

                           /* if (AddProductDatabase.imageListG.get(0).contains("http"))
                            {

                                RequestBody image = null;
                                try {
                                    image = RequestBody.create(MediaType.parse("image/*"), getByteArray(AddProductDatabase.imageListG.get(0)));
                                    multipartimage1 = MultipartBody.Part.createFormData("image1", "imageA.jpeg", image);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            else
                             {
                                RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(0)));
                                multipartimage1 = MultipartBody.Part.createFormData("image1", "imageA.jpeg", image);

                            }*/


                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(0)));
                            multipartimage1 = MultipartBody.Part.createFormData("image1", "imageA.jpeg", image);

                            model.setImage1(ImageUploadHelper.convertImageTomultipart(AddProductDatabase.imageListG.get(i), "image1"));

                        } else if (i == 1) {
                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(1)));
                            multipartimage2 = MultipartBody.Part.createFormData("image2", "imageB.jpeg", image);
                            model.setImage2(ImageUploadHelper.convertImageTomultipart(AddProductDatabase.imageListG.get(i), "image2"));
                        } else if (i == 2) {

                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(2)));
                            multipartimage3 = MultipartBody.Part.createFormData("image3", "imageC.jpeg", image);
                            model.setImage3(ImageUploadHelper.convertImageTomultipart(AddProductDatabase.imageListG.get(i), "image3"));
                        } else if (i == 3) {
                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(3)));
                            multipartimage4 = MultipartBody.Part.createFormData("image4", "imageD.jpeg", image);
                            model.setImage4(ImageUploadHelper.convertImageTomultipart(AddProductDatabase.imageListG.get(i), "image4"));
                        } else if (i == 4) {

                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes(AddProductDatabase.imageListG.get(4)));
                            multipartimage5 = MultipartBody.Part.createFormData("image5", "imageE.jpeg", image);
                            model.setImage5(ImageUploadHelper.convertImageTomultipart(AddProductDatabase.imageListG.get(i), "image5"));
                        }


                    }


                    if (AddProductDatabase.videoUrl.equals("")) {

                        //Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("videoUrlPrint", "Hii" + AddProductDatabase.videoUrl);
                        // Log.e("videoUrlPrint",AddProductDatabase.videoThumbnail);
                           /* Bitmap thumb = ThumbnailUtils.createVideoThumbnail(Global.videopath, MediaStore.Images.Thumbnails.MINI_KIND);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();*/

                        if (AddProductDatabase.videoThumbnail.equalsIgnoreCase("")) {
                            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(AddProductDatabase.videoUrl, MediaStore.Images.Thumbnails.MINI_KIND);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            thumb.compress(Bitmap.CompressFormat.PNG, 30, stream);
                            byteArray = stream.toByteArray();
                        } else if (AddProductDatabase.videoThumbnail.contains("http")) {
                            try {
                                InputStream inputStream = (InputStream) new URL(AddProductDatabase.videoThumbnail).getContent();
                                byteArray = IOUtils.toByteArray(inputStream);
                            } catch (Exception e) {
                            }

                        } else {
                            Bitmap src = BitmapFactory.decodeFile(AddProductDatabase.videoThumbnail);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            src.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                            byteArray = baos.toByteArray();
                            // byte[] by = baos.toByteArray();
                        }
                        //------------------------------------------------------------

                        if (byteArray != null) {

                            RequestBody image = RequestBody.create(MediaType.parse("image/*"), byteArray);
                            multipartimage = MultipartBody.Part.createFormData("product_video_thumbnail", "thumb.jpeg", image);
                            model.setThumbnail_image(multipartimage);
                        }


                        if (AddProductDatabase.videoUrl.contains("http")) {
                            isVideoUpdate = false;
                               /* Log.e("printFile",AddProductDatabase.videoUrl+"");
                                String urlPath = getRealPathFromURI(Uri.parse(AddProductDatabase.videoUrl));

                                Log.e("printFile",urlPath+"");
                                File file = new File(urlPath);

                                Log.e("printFile",file+"");
                                RequestBody image = RequestBody.create(MediaType.parse("video/*"), file);
                                mulitpartvideo = MultipartBody.Part.createFormData("product_video", "video.mp4", image);
                                model.setProductVideo(mulitpartvideo);*/

                        } else {
                            File file = new File(AddProductDatabase.videoUrl);
                            RequestBody video = RequestBody.create(MediaType.parse("video/*"), file);
                            mulitpartvideo = MultipartBody.Part.createFormData("product_video", "video.mp4", video);
                            model.setProductVideo(mulitpartvideo);
                            isVideoUpdate = true;
                        }


                    }


                    if (isEditing) {

                        // model.setProduct_id(RequestBody.create(MediaType.parse("text/plain"), productId));
                        // editProduct(model);
                        Log.e("EditProduct", "allDone");


                    } else {

              /*  if (isPromotClicked) {
                    if (!promotepackageId.equalsIgnoreCase("NA")) {
                        isPromotingProduct="Y";
                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), promotepackageId));
                    } else {
                        isPromotingProduct="N";
                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), ""));
                    }
                    model.setPromote_product(RequestBody.create(MediaType.parse("text/plain"), isPromotingProduct));

                    addProduct(model);
                    Log.e("addProduct", "allDone");
                } else {*/


                        if (productStatus.equalsIgnoreCase("add")) {

                            AddProductPromoteDialog.create(this, new AddProductPromoteDialog.PromoteCallback() {
                                @Override
                                public void onPackageSelected(String id) {
                                    isPromotClicked = true;
                                    promotepackageId = id;
                                    if (!promotepackageId.equalsIgnoreCase("NA")) {
                                        isPromotingProduct = "Y";
                                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), promotepackageId));
                                    } else {
                                        isPromotingProduct = "N";
                                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), ""));
                                    }
                                    model.setPromote_product(RequestBody.create(MediaType.parse("text/plain"), isPromotingProduct));


                                    Gson gson = new GsonBuilder().create();
                                    Log.e("onViewClicked: ", "" + AddProductDatabase.imageListG);
                                    Log.e("addProduct", "allDone" + gson.toJson(model));
                                    addProduct(model);

                                }
                            }).show();

                        } else {

                            model.setProduct_id(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.productid));
                            model.setPromote_product(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.promoteproduct));
                            model.setImage_id(RequestBody.create(MediaType.parse("text/plain"), image_id));
                            model.setLocation(RequestBody.create(MediaType.parse("text/plain"), location));

                            Gson gson = new GsonBuilder().create();
                            Log.e("onViewClicked: ", "" + AddProductDatabase.imageListG);
                            Log.e("addProductNewTrans", "allDone" + gson.toJson(model));
                            updateProduct(model);
                        }
                    }

                }


                break;
        }
    }

    private void adddata() {


        AddProductDatabase data = new AddProductDatabase();


        if (!spinnerPaymentMode.getSelectedItem().toString().isEmpty())
            data.payment_method = paymentType;

        if (!spinnerModeOfDelivery.getSelectedItem().toString().isEmpty())
            data.modes_of_delivery = txtDelivery.getText().toString();


        if (!spinSellInternationally.getSelectedItem().toString().isEmpty())
            data.currency = spinSellInternationally.getSelectedItemPosition();

        if (!featuredSp.getSelectedItem().toString().isEmpty())
            data.feature = featuredSp.getSelectedItemPosition();


    }


    private void getdata() {

        if (AddProductDatabase.currency >= 0)
            spinSellInternationally.setSelection(AddProductDatabase.currency);

        if (AddProductDatabase.feature >= 0)
            featuredSp.setSelection(AddProductDatabase.feature);

    }


    public void addProduct(AddProductModel addProductModel) {


        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Dialog dialog = S_Dialogs.getLoadingDialog(AddNewTransaction.this);
        dialog.show();
        Call<GetProductList> addProductCall = service.addProductApi(addProductModel.getUser_id(), addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                , addProductModel.getPayment_mode(), addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                , addProductModel.getFixed_price(), addProductModel.getProduct_type(), addProductModel.getQuantity(), addProductModel.getDescription()
                , addProductModel.getPromote_product(), addProductModel.getNo_of_clicks(), addProductModel.getBudget(), addProductModel.getTags()
                , multipartimage1, multipartimage2, multipartimage3, multipartimage4, multipartimage5, addProductModel.getPackageId(), addProductModel.getProductVideo(), multipartimage, addProductModel.getLocation());
        addProductCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                Log.e("onResponseMethod", "addProduct");
                if (response.isSuccessful()) {

                    removedata();
                    Global.videopath = "no_image";
                    AddProductDatabase.imageListG.clear();
                    AddProductDatabase.tagListG.clear();

                    productList = response.body();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();


                        if (isPromotingProduct.equalsIgnoreCase("Y")) {
                            try {
                                PaymentDialog.create(AddNewTransaction.this, response.body().getResult().get(0).getPrice(), "", HelperPreferences.get(AddNewTransaction.this).getString(UID), response.body().getResult().get(0).getId(), response.body().getPackage_id(), response.body().getPromote_id(), new PaymentDialog.PaymentCallBack() {
                                    @Override
                                    public void onPaymentSuccess() {

                                        Toast.makeText(AddNewTransaction.this, "Product promoted successfully.", Toast.LENGTH_SHORT).show();
                                        Log.e("onPaymentSuccess: ", response.body().getResult().get(0).getId());
                                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onPaymentFail(String message) {
                                        Toast.makeText(AddNewTransaction.this, "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();

                                    }

                                    @Override
                                    public void onCancelDialog() {
                                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                               /* PromoteDialog.create(getActivity(), response.body().getResult().get(0).getId(), new PromoteDialog.PromoteCallback() {
                                    @Override
                                    public void onPromoteSuccess() {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        ProductFrgament fragment = new ProductFrgament();
                                        fragment.setArguments(bundle);
                                        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

                                    }

                                    @Override
                                    public void onPromoteFailure() {
                                        Toast.makeText(getActivity(), "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);
                                    }
                                }).show();*/
                            } catch (Exception e) {

                            }
                        } else {
                            Log.e("addproduct","okkk");
                            //Toast.makeText(AddNewTransaction.this,  "Something went's wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddNewTransaction.this, "Product added successfully.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                            intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                        }
                       /*Bundle bundle=new Bundle();
                        bundle.putString(SAConstants.Keys.CAT_ID, catId);
                        bundle.putString(SAConstants.Keys.SUB_CAT_ID, subCatId);
                        bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategories);
                        SubCategoryFragment fragment=new SubCategoryFragment();
                        fragment.setArguments(bundle);*/

                    } else {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } else {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    try {

                        Log.e("printErrorBody", response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("printErrorBody", "message");
                    }

                    Toast.makeText(AddNewTransaction.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                   /* try {
                        Log.e("AddProductError", response.message());
                        Log.e("AddProductError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                   /* Snackbar.make(rootTag, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();*/
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                // Toast.makeText(AddNewTransaction.this,  "Something went's wrong. Please try again later.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Log.e("onFailure: ", t.getLocalizedMessage());
                //  Log.e("onFailure: ", "" + t.getCause().toString());
                Snackbar.make(rootTag, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    public byte[] bytes(String path) {
        byte[] by = new byte[0];
        if (path.contains("http")) {
            try {
                InputStream inputStream = (InputStream) new URL(path).getContent();
                by = IOUtils.toByteArray(inputStream);
            } catch (Exception e) {
            }

        } else {
            Bitmap src = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            by = baos.toByteArray();
            // byte[] by = baos.toByteArray();

        }



       /* BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] by= stream.toByteArray();*/


        return by;
    }

    public byte[] getByteArray(String url) throws Exception {
        InputStream inputStream = (InputStream) new URL(url).getContent();
        return IOUtils.toByteArray(inputStream);
    }

    private void removedata() {

        AddProductDatabase.name = "";
        AddProductDatabase.category = 0;
        AddProductDatabase.sub_category = 0;
        AddProductDatabase.price = "";
        AddProductDatabase.type = 0;
        AddProductDatabase.condition = 0;
        AddProductDatabase.quantity = "";
        AddProductDatabase.tags = "";
        AddProductDatabase.description = "";
        AddProductDatabase.productid = "";
        AddProductDatabase.promoteproduct = "";


    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                PaymentDialog.create(AddNewTransaction.this, productList.getResult().get(0).getPrice(), "", HelperPreferences.get(AddNewTransaction.this).getString(UID), productList.getResult().get(0).getId(), productList.getPackage_id(), productList.getPromote_id(), new PaymentDialog.PaymentCallBack() {
                    @Override
                    public void onPaymentSuccess() {

                        Toast.makeText(AddNewTransaction.this, "Product promoted successfully.", Toast.LENGTH_SHORT).show();
                        Log.e("onPaymentSuccess: ", productList.getResult().get(0).getId());
                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, productList.getResult().get(0));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    @Override
                    public void onPaymentFail(String message) {
                        Toast.makeText(AddNewTransaction.this, "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();

                    }

                    @Override
                    public void onCancelDialog() {
                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, productList.getResult().get(0));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).show();

            } catch (Exception e) {

            }

        }
    };

    public void updateProduct(AddProductModel addProductModel) {
        Call<JsonObject> addProductCall = null;

        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Dialog dialog = S_Dialogs.getLoadingDialog(AddNewTransaction.this);
        dialog.show();

        if (isVideoUpdate) {

            addProductCall = service.editProductApi(addProductModel.getProduct_id()
                    , addProductModel.getUser_id()
                    , addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                    , addProductModel.getPayment_mode()
                    , addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                    , addProductModel.getFixed_price()
                    , addProductModel.getProduct_type()
                    , addProductModel.getQuantity()
                    , addProductModel.getDescription()
                    , addProductModel.getPromote_product()
                    , addProductModel.getNo_of_clicks()
                    , addProductModel.getBudget(), addProductModel.getTags()
                    , addProductModel.getImage1(), addProductModel.getImage2()
                    , addProductModel.getImage3(), addProductModel.getImage4()
                    , addProductModel.getImage5(), addProductModel.getImage6()
                    , addProductModel.getImage7(), addProductModel.getImage8()
                    , addProductModel.getLocation(), addProductModel.getImage_id()
                    , addProductModel.getProductVideo()
                    , addProductModel.getThumbnail_image());

        } else {
            addProductCall = service.editProductApi1(addProductModel.getProduct_id()
                    , addProductModel.getUser_id()
                    , addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                    , addProductModel.getPayment_mode()
                    , addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                    , addProductModel.getFixed_price()
                    , addProductModel.getProduct_type()
                    , addProductModel.getQuantity()
                    , addProductModel.getDescription()
                    , addProductModel.getPromote_product()
                    , addProductModel.getNo_of_clicks()
                    , addProductModel.getBudget(), addProductModel.getTags()
                    , addProductModel.getImage1(), addProductModel.getImage2()
                    , addProductModel.getImage3(), addProductModel.getImage4()
                    , addProductModel.getImage5(), addProductModel.getImage6()
                    , addProductModel.getImage7(), addProductModel.getImage8()
                    , addProductModel.getLocation(), addProductModel.getImage_id());

            Log.e("updateProductStatus", "pre" + addProductModel.getProductVideo());

        }


        addProductCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("updateProductStatus", "updated");

                if (response.isSuccessful()) {
                    Log.e("updateProductStatus", "added");
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());


                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(AddNewTransaction.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            removedata();
                            Global.videopath = "no_image";
                            AddProductDatabase.imageListG.clear();
                            AddProductDatabase.tagListG.clear();

                            Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ErrorException", e.getMessage());

                    }

                } else {
                    Toast.makeText(AddNewTransaction.this, "There is some network error. Please try again.", Toast.LENGTH_SHORT).show();

                    try {
                        Log.e("errorResponse", response.errorBody().string());
                    } catch (Exception e) {
                    }

                }



/*                if (response.isSuccessful()) {

                    removedata();
                    Global.videopath = "no_image";
                    AddProductDatabase.imageListG.clear();
                    AddProductDatabase.tagListG.clear();



                   productList = response.body();
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();


                        if (isPromotingProduct.equalsIgnoreCase("Y")) {
                            try {
                                PaymentDialog.create(AddNewTransaction.this, response.body().getResult().get(0).getPrice(), "", HelperPreferences.get(AddNewTransaction.this).getString(UID), response.body().getResult().get(0).getId(), response.body().getPackage_id(), response.body().getPromote_id(), new PaymentDialog.PaymentCallBack() {
                                    @Override
                                    public void onPaymentSuccess() {

                                        Toast.makeText(AddNewTransaction.this, "Product promoted successfully.", Toast.LENGTH_SHORT).show();
                                        Log.e("onPaymentSuccess: ", response.body().getResult().get(0).getId());
                                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onPaymentFail(String message) {
                                        Toast.makeText(AddNewTransaction.this, "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();

                                    }

                                    @Override
                                    public void onCancelDialog() {
                                        Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();

                            } catch (Exception e) {

                            }
                        } else {

                            Intent intent = new Intent(AddNewTransaction.this, MainActivity.class);
                            intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                        }


                    } else {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                } else {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("AddProductError", response.message());
                        Log.e("AddProductError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Snackbar.make(rootTag, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();

                }
                else
                {
                    try {
                        Log.e("responseError",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                Log.e("onFailure: ", t.getMessage());
                Snackbar.make(rootTag, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {

        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }




      /*  // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);*/
        }


    }

}
