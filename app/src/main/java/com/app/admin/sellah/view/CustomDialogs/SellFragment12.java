package com.app.admin.sellah.view.CustomDialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.controller.utils.SellProductInterface;
import com.app.admin.sellah.model.extra.AddProductModel;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.model.extra.Categories.Subcategory;
import com.app.admin.sellah.model.extra.SpinnerStateCheck;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.ProductImage;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.model.extra.getProductsModel.Tag;
import com.app.admin.sellah.view.CustomDialogs.AddProductPromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.PromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.app.admin.sellah.view.activities.Video_capture_activity;
import com.app.admin.sellah.view.adapter.AddProductTagsAdapter;
import com.app.admin.sellah.view.adapter.Add_Product_Cars_Adapter;
import com.app.admin.sellah.view.adapter.SpinnerStateAdapter;
import com.app.admin.sellah.view.fragments.HomeFragment;
import com.app.admin.sellah.view.fragments.ProductFrgament;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.M;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PRODUCT_DETAIL;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class SellFragment12 extends Fragment implements SellProductInterface {

    //Cars Recycler Initialization
    RecyclerView addImageRecycler;
    List<String> imageList = new ArrayList<>();
    ArrayList<HashMap<String,String>> imageListID = new ArrayList<>();
    Add_Product_Cars_Adapter add_product_cars_adapter;

    //Tags Recycler Initialization
    RecyclerView tagRecycler;
    ArrayList<String> tagList = new ArrayList<>();
    AddProductTagsAdapter addProductTagsAdapter;
    @BindView(R.id.add_product_post)
    Button addProductPost;
    @BindView(R.id.tabs1)
    TabLayout tabs1;
    @BindView(R.id.add_product_car_recycler)
    RecyclerView addProductCarRecycler;
    @BindView(R.id.add_photo)
    CardView addPhoto;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.edt_price)
    EditText edtPrice;
    @BindView(R.id.rl6)
    RelativeLayout rl6;
    @BindView(R.id.add_product_tags_recycler)
    RecyclerView addProductTagsRecycler;
    @BindView(R.id.add_tags)
    ImageView addTags;
    @BindView(R.id.spinner_city)
    TextView spinnerCity;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.rootTag)
    LinearLayout rootTag;
    @BindView(R.id.edt_product_name)
    EditText edtProductName;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.edt_budget)
    EditText edtBudget;
    @BindView(R.id.txt_no_of_clicks)
    TextView txtNoOfClicks;
    @BindView(R.id.edtDescription)
    EditText edtDescription;
    //    private HashMap<String, ArrayList<String>> subcategoryList;
    TextView product, live;
    @BindView(R.id.txt_payment_type)
    TextView txtPaymentType;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.upload_video)
    CardView upload;
    @BindView(R.id.thumbnail_video)
    ImageView thumbnailVideo;
    private TabLayout tabLayout;
    public static final int RESULT_OK = -1;
    final int CAMERA_PIC_REQUEST = 1313;
    ImageView addTag;

    String tag;
    private int GALLERY = 1212;
    Unbinder unbinder;

    @BindView(R.id.spinner_sub_catagory)
    Spinner spinSubcategory;

    @BindView(R.id.spinner_catagory)
    Spinner spinCategory;

    @BindView(R.id.spinner_payment_mode)
    Spinner spinnerPaymentMode;

    @BindView(R.id.spinner_mode_of_delivery)
    Spinner spinnerModeOfDelivery;

    @BindView(R.id.spin_sell_internationally)
    Spinner spinSellInternationally;

    @BindView(R.id.spin_fixed_price)
    Spinner spinFixedPrice;

    @BindView(R.id.spin_condition)
    Spinner spinCondition;

    private ArrayList<String> subCategory;
    TextWatcher priceTextWacher;
    String isPromotingProduct = "N";
    boolean isPromotClicked = false;
    private String catId = "", subCatId = "";
    List<Subcategory> subcategories = new ArrayList<>();
    ArrayList<SpinnerStateCheck> paymentOptions = new ArrayList<>();
    ArrayList<SpinnerStateCheck> deliveryOptions = new ArrayList<>();
    private String paymentType = "";
    private String deliveryType = "";
    private String TAG = SellFragment12.class.getSimpleName();
    private boolean isEditing = false;
    private String productId = "";
    private Result productDetails;
    private String promotepackageId = "NA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sell_product, container, false);
        unbinder = ButterKnife.bind(this, view);

        hideSearch();
        getids(view);
        onClicks();
        setUpTagLayoutAdapter();
        setTextWacher();
        if (getArguments() != null) {
            Log.e(TAG, "onCreateView: have bundle");
            setUpProductData(getArguments().containsKey(PRODUCT_DETAIL) ? getArguments().getParcelable(PRODUCT_DETAIL) : null);
        } else {
            setUpPaymentOptions();
            setUpDeliveryOptions();
            setupSubcategory();
            Log.e(TAG, "onCreateView: don't have bundle");
        }


        return view;
    }

    private void setUpProductData(Result productDetial) {

        if (productDetial != null) {
            Log.e(TAG, "setUpProductData: got Data");

            tabs1.setVisibility(View.GONE);
            productDetails = productDetial;
            isEditing = true;
            productId = productDetial.getId();

            setUpPaymentOptions();
            setUpDeliveryOptions();
//            setUpTagLayoutAdapter();
            setupSubcategory();

//          Setup tags from product detail
            tagList.clear();
            for (Tag tag : productDetial.getTags()) {
                tagList.add(tag.getTagName());
                addProductTagsAdapter.notifyItemRangeInserted(tagList.size() > 0 ? tagList.size() - 1 : 0, tagList.size());
//                addProductTagsAdapter.notifyDataSetChanged();
            }

//            int count=0;

            for (ProductImage imgUrl : productDetial.getProductImages()) {
                if (!(imageList.size() > productDetial.getProductImages().size())) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(imgUrl.getImage())
//                        .apply(Global.getGlideOptions())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imageList.add(getRealPathFromURI(getImageUri(getActivity(), resource)));
                                    add_product_cars_adapter = new Add_Product_Cars_Adapter(getActivity(), imageList, SellFragment12.this);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                    addImageRecycler.setLayoutManager(layoutManager);
                                    addImageRecycler.setAdapter(add_product_cars_adapter);
                                    add_product_cars_adapter.notifyDataSetChanged();
                                }
                            });
                }
//                count++;
            }

//          Setup other product details

            edtProductName.setText(productDetial.getName());
//            edtPrice.setText(productDetial.getPrice());
            edtPrice.setText("S$ " + productDetial.getPrice());
            edtBudget.setText(productDetial.getBudget());
            edtQuantity.setText(productDetial.getQuantity());
        } else {
            Log.e(TAG, "setUpProductData: not get Data");
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
                ArrayList<String> selection = new ArrayList<>();
                if (select_paymentoption[i].toString().toLowerCase().contains("credit/debit")) {
                    stateVO.setSelected(true);
                    selection.add(select_paymentoption[i].toString());
//                    paymentType = select_paymentoption[i].toString();
                } else {
                    stateVO.setSelected(false);
                }
                paymentType = TextUtils.join(", ", selection);
            }

            txtPaymentType.setText(paymentType);
            paymentOptions.add(stateVO);
        }

        SpinnerStateAdapter myAdapterPaymentOtions = new SpinnerStateAdapter(getActivity(), 0, paymentOptions, true, "payment", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            paymentType = selection;
            if (TextUtils.isEmpty(selection)) {
                txtPaymentType.setText(select_paymentoption[0]);
            } else {
                txtPaymentType.setText(selection);
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
                ArrayList<String> selction = new ArrayList<>();
                selction.add(selectDeliveryOption[1].toString());
                selction.add(selectDeliveryOption[2].toString());
                deliveryType = TextUtils.join(", ", selction);
//                deliveryType = selectDeliveryOption[i].toString();
                stateVO.setSelected(true);
            }
            txtDelivery.setText(deliveryType);
            deliveryOptions.add(stateVO);
        }
        SpinnerStateAdapter myAdapterDelivery = new SpinnerStateAdapter(getActivity(), 0, deliveryOptions, true, "delivery", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            deliveryType = selection;
            if (TextUtils.isEmpty(selection)) {
                txtDelivery.setText(selectDeliveryOption[0]);
            } else {
                txtDelivery.setText(selection);
            }
        });
        spinnerModeOfDelivery.setAdapter(myAdapterDelivery);
    }

    private void setTextWacher() {
        priceTextWacher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String prefix = "S$";
//                edtPrice.setText("S$" + edtPrice.getText().toString());
                if (!s.toString().startsWith("S$")) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (s.toString().startsWith(deletedPrefix)) {
                        cleanString = s.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = s.toString().replaceAll(prefix, "");
                    }
                    edtPrice.setText(prefix + cleanString);
                    edtPrice.setSelection(Global.getText(edtPrice).length());
                  /*  edtPrice.setText("S$");
                    Selection.setSelection(edtPrice.getText(), edtPrice
                            .getText().length());*/
                } else {
                   /* if(s.toString().equalsIgnoreCase("S$")){
                            edtPrice.setText("");
                    }*/
                }
            }
        };
        edtPrice.addTextChangedListener(priceTextWacher);
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String prefix = "S$";
//                edtPrice.setText("S$" + edtPrice.getText().toString());
                if (!s.toString().startsWith("S$")) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (s.toString().startsWith(deletedPrefix)) {
                        cleanString = s.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = s.toString().replaceAll(prefix, "");
                    }
                    edtBudget.setText(prefix + cleanString);
                    edtBudget.setSelection(Global.getText(edtBudget).length());
                  /*  edtPrice.setText("S$");
                    Selection.setSelection(edtPrice.getText(), edtPrice
                            .getText().length());*/
                } else {
                   /* if(s.toString().equalsIgnoreCase("S$")){
                            edtPrice.setText("");
                    }*/
                }
            /*    if (!s.toString().startsWith("S$")) {
                    edtBudget.setText("S$");
                    Selection.setSelection(edtBudget.getText(), edtBudget
                            .getText().length());
                }*/
            }
        });

           /* checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isPromotingProduct = "Y";
                    } else {
                        isPromotingProduct = "N";
                    }
                }
            });*/
    }

    private void setUpTagLayoutAdapter() {

        tabLayout.addTab(tabLayout.newTab().setText("List a Product"));
        tabLayout.addTab(tabLayout.newTab().setText("Go Live"));
        tabLayout.getTabAt(0).select();

        //tags set Adapter
        addProductTagsAdapter = new AddProductTagsAdapter(tagList, getActivity(), this);
        LinearLayoutManager tagsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        tagRecycler.setLayoutManager(tagsLayoutManager);
        tagRecycler.setAdapter(addProductTagsAdapter);
        addProductTagsAdapter.notifyDataSetChanged();
//        add_product_cars_adapter.notifyDataSetChanged();

    }

    private void onClicks() {



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Video_capture_activity.class);
                startActivity(intent);
            }
        });


        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tagList.size() != 3) {
                    Global.addTag(getActivity(), (dialog, input) -> {
                        if (TextUtils.isEmpty(input)) {
                            dialog.dismiss();

                        } else {
                            tag = input.toString().trim();
                            tagList.add(tag);
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Snackbar.make(rootTag, "Only three tags are allowed per item.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

        });

        tabs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "ggffr", Toast.LENGTH_SHORT).show();

            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("Dcvresdvcd", tab.getPosition() + "");

                if (tab.getPosition() == 1) {

                    Intent intent = new Intent(new Intent(getActivity(), MainActivityLiveStream.class));
                    intent.putExtra("value", "LiveStream");
                    getActivity().startActivity(intent);


                } else {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                       /* if(imageList.size()!=8){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_PIC_REQUEST);
                        }else{
                            Snackbar.make(rootTag, "Only eight images are allowed per item.", Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();
                        }*/
                        showPictureDialog();

                    } else {
                        Log.e("Permission", "Permission is revoked");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    showPictureDialog();
                }

            }
        });

    }

    private void getids(View view) {

        addImageRecycler = view.findViewById(R.id.add_product_car_recycler);
        tagRecycler = view.findViewById(R.id.add_product_tags_recycler);
        tabLayout = view.findViewById(R.id.tabs1);
        addPhoto = view.findViewById(R.id.add_photo);
        addTag = view.findViewById(R.id.add_tags);
        rootTag = view.findViewById(R.id.rootTag);

    }

    String catName = "", subcatname = "", fixedPrice = "", condition = "";

    private void setupSubcategory() {
//        String text = spinCategory.getSelectedItem().toString();

        if (productDetails != null) {
            catId = productDetails.getCatId();
            subCatId = productDetails.getSubCatId();
            catName = productDetails.getCategoryName();
            subcatname = productDetails.getSubcategoryName();
            fixedPrice = productDetails.getFixedPrice();
            condition = productDetails.getProductType();
        }

        GetCategoriesModel model = ExpandableListData.getData();
        List<String> categoryList = new ArrayList<>();
        HashMap<String, ArrayList<String>> subCategoryList = new HashMap<>();
        categoryList.add("Category");
        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                ArrayList<String> subCategories = new ArrayList<>();
                ArrayList<String> categoriesMain = new ArrayList<>();
                Log.e("categoryDataNav", model.getResult().get(i).getName());

                subCategories.add("Subcategory");
                for (int j = 0; j < model.getResult().get(i).getSubcategories().size(); j++) {
                    Log.e("  SubcategoryDataNav", model.getResult().get(i).getSubcategories().get(j).getName());
                    subCategories.add(model.getResult().get(i).getSubcategories().get(j).getName());
                }
                categoryList.add(model.getResult().get(i).getName());
                subCategoryList.put(model.getResult().get(i).getName(), subCategories);
            }
        } catch (Exception e) {

        }

//        String[] subCatStringList = getResources().getStringArray(R.array.add_product_sub_category);
//        List<String> myResArrayList = Arrays.asList(subCatStringList);


//      *************************************Category Spinner adapter*******************************
        ArrayAdapter<String> categorySpinAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_dropdown,
                        categoryList);
        categorySpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinCategory.setAdapter(categorySpinAdapter);
        if (!TextUtils.isEmpty(catName))
            spinCategory.setSelection(categorySpinAdapter.getPosition(catName));
//      *************************************Sell Method Spinner adapter*******************************
        ArrayAdapter<String> sellModeSpinAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.sell_internationally));
        sellModeSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinSellInternationally.setAdapter(sellModeSpinAdapter);
        spinSellInternationally.setSelection(1);

//      *************************************price Method Spinner adapter*******************************
        ArrayAdapter<String> priceModeSpinAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.add_product_fixed));
        priceModeSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinFixedPrice.setAdapter(priceModeSpinAdapter);
        if (fixedPrice.equalsIgnoreCase("Y")) {
            spinFixedPrice.setSelection(1);
        } else if (!TextUtils.isEmpty(fixedPrice)) {
            spinFixedPrice.setSelection(2);
        }

//      *************************************Condition Spinner adapter*******************************
        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.add_product_condition));
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);


        spinCondition.setAdapter(conditionSpinAdapter);


        if (condition.equalsIgnoreCase("U")) {
            spinCondition.setSelection(2);
        } else {
            spinCondition.setSelection(0);
        }


        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.e("Scategory", item + " ");
//                subCategory = new ArrayList<>(myResArrayList);
                if (!item.toString().equalsIgnoreCase("category")) {
                    Log.e("Subcategory", subCategoryList.get(item) + " ");

                    catId = model.getResult().get(position - 1).getCatId();
                    subcategories.addAll(model.getResult().get(position - 1).getSubcategories());
                    Log.e("Cat_id", "onItemSelected: " + catId);
                    try {
                        subCategory = new ArrayList<>();
                        subCategory.addAll(subCategoryList.get(item));
                        subCategory.remove(1);
                    } catch (Exception e) {

                    }


                } else {
                    catId = "";
                    subCategory = new ArrayList<>();
                    subCategory.add("Subcategory");
                }
//      *************************************Sub-Category Spinner adapter*******************************
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (getActivity(), R.layout.spinner_dropdown,
                                subCategory);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinSubcategory.setAdapter(spinnerArrayAdapter);
                if (!TextUtils.isEmpty(subcatname)) {
                    spinSubcategory.setSelection(spinnerArrayAdapter.getPosition(subcatname));
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.e("Subcategory", item + " ");
                if (!item.toString().equalsIgnoreCase("Subcategory")) {
//                    Log.e("Subcategory", subCategoryList.get(item) + " ");
                    subCatId = model.getResult().get(spinCategory.getSelectedItemPosition() - 1).getSubcategories().get(position).getId();
                    Log.e("SubCat_id", "onItemSelected: " + subCatId);

                } else {
                    subCatId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);*/

    }


    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Sell");
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(2);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);

//            Toast.makeText(getActivity(), "Here " + getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();

            imageList.add(getRealPathFromURI(tempUri));
            add_product_cars_adapter = new Add_Product_Cars_Adapter(getActivity(), imageList,this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            addImageRecycler.setLayoutManager(layoutManager);
            addImageRecycler.setAdapter(add_product_cars_adapter);

        }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
//                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                    imageList.add(getRealPathFromURI(tempUri));
                    add_product_cars_adapter = new Add_Product_Cars_Adapter(getActivity(), imageList, this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    addImageRecycler.setLayoutManager(layoutManager);
                    addImageRecycler.setAdapter(add_product_cars_adapter);
                    add_product_cars_adapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);

//            Toast.makeText(getActivity(), "Here " + getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();

            imageList.add(getRealPathFromURI(tempUri));
            add_product_cars_adapter = new Add_Product_Cars_Adapter(getActivity(), imageList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            addImageRecycler.setLayoutManager(layoutManager);
            addImageRecycler.setAdapter(add_product_cars_adapter);
            add_product_cars_adapter.notifyDataSetChanged();

        }

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
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
    public void setTagVisiblity(boolean visible) {
        if (visible) {
            addTag.setVisibility(View.VISIBLE);
        } else {
            addTag.setVisibility(View.GONE);
        }
    }

    @Override
    public void setImageCaptureVisiblty(boolean visible) {
        if (visible) {
            addPhoto.setVisibility(View.VISIBLE);
        } else {
            addPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 100:
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (readExternalStorage && camera) {
                   /* if(imageList.size()!=8){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                    }else{
                        Snackbar.make(rootTag, "Only eight images are allowed per item.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }*/
                    showPictureDialog();
                } else {
                    Snackbar.make(rootTag, "Unable to access camera", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_product_post)
    public void onViewClicked() {
        Global.hideKeyboard(rootTag, getActivity());
        if (imageList.size() == 0) {
            Snackbar.make(rootTag, "Please add at-least one image for product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (Global.getText(edtProductName).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter product name", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (spinCategory.getSelectedItem().toString().equalsIgnoreCase("category")) {
            Snackbar.make(rootTag, "Please select an category", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (spinSubcategory.getSelectedItem().toString().equalsIgnoreCase("subcategory")) {
            Snackbar.make(rootTag, "Please select an subcategory", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (paymentType.equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please select mode of payment", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (deliveryType.equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please select mode of delivery", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }/* else if (spinSellInternationally.getSelectedItem().toString().equalsIgnoreCase("sell internationally")) {
            Snackbar.make(rootTag, "Please select selling strategy", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (Global.getText(edtPrice).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }/* else if (Global.getText(edtPrice).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (spinCondition.getSelectedItem().toString().equalsIgnoreCase("condition")) {
            Snackbar.make(rootTag, "Please select condition of product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        } else if (Global.getText(edtQuantity).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please select quantity of product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }/* else if (Global.getText(edtBudget).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter budget", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else {

            AddProductModel model = new AddProductModel();
            model.setUser_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(getActivity()).getString(UID))));
            model.setName(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Global.getText(edtProductName))));
            model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catId)));
//            model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(subCatId)));
            model.setSub_cat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(subCatId)));
            model.setPayment_mode(RequestBody.create(MediaType.parse("text/plain"), paymentType));
            model.setDelivery(RequestBody.create(MediaType.parse("text/plain"), deliveryType));
            model.setTags((RequestBody.create(MediaType.parse("text/plain"), TextUtils.join("|", tagList))));
            if (spinSellInternationally.getSelectedItem().toString().equalsIgnoreCase("sell internationally")) {
                model.setSell_internationally(RequestBody.create(MediaType.parse("text/plain"), "Y"));
            } else {
                model.setSell_internationally(RequestBody.create(MediaType.parse("text/plain"), "N"));
            }
            model.setPrice(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtPrice).replace("S$", "")));
            if (spinFixedPrice.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "Y"));
            } else {
                model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "N"));
            }
            if (!spinCondition.getSelectedItem().toString().equalsIgnoreCase("used")) {
                model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "N"));
            } else {
                model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "U"));
            }
            model.setQuantity(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtQuantity)));
            model.setDescription(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtDescription)));
            model.setBudget(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtBudget).replace("S$", "")));
            model.setNo_of_clicks(RequestBody.create(MediaType.parse("text/plain"), "0"));

            for (int i = 0; i < imageList.size(); i++) {

                if (i == 0) {
                    model.setImage1(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image1"));
                    Log.e("image1", model.getImage1() + "");
                } else if (i == 1) {
                    model.setImage2(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image2"));
                } else if (i == 2) {
                    model.setImage3(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image3"));
                } else if (i == 3) {
                    model.setImage4(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image4"));
                } else if (i == 4) {
                    model.setImage5(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image5"));
                } else if (i == 5) {
                    model.setImage6(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image6"));
                } else if (i == 6) {
                    model.setImage7(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image7"));
                } else if (i == 7) {
                    model.setImage8(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image8"));
                }
                if (Global.videopath.equals("no_image"))
                {

                }
                else
                {
                    model.setProductVideo(ImageUploadHelper.convertImageTomultipart(Global.videopath, "product_video"));
                }
            }
            if (isEditing) {
                model.setProduct_id(RequestBody.create(MediaType.parse("text/plain"), productId));
                editProduct(model);
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
                AddProductPromoteDialog.create(getActivity(), new AddProductPromoteDialog.PromoteCallback() {
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
                        ///addProduct(model);
                        Log.e("addProduct", "allDone");
                    }
                }).show();
            }

        }

//        }

    }

/*
    public void addProduct(AddProductModel addProductModel) {

        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<GetProductList> addProductCall = service.addProductApi(addProductModel.getUser_id(), addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                , addProductModel.getPayment_mode(), addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                , addProductModel.getFixed_price(), addProductModel.getProduct_type(), addProductModel.getQuantity(), addProductModel.getDescription()
                , addProductModel.getPromote_product(), addProductModel.getNo_of_clicks(), addProductModel.getBudget(), addProductModel.getTags()
                , addProductModel.getImage1(), addProductModel.getImage2(),  addProductModel.getPackageId(), addProductModel.getProductVideo(), addProductModel.getProductVideo());
        addProductCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        Global.videopath="no_image";

                        if (isPromotingProduct.equalsIgnoreCase("Y")) {
                            try {
                                PaymentDialog.create(getActivity(), "", HelperPreferences.get(getActivity()).getString(UID), response.body().getResult().get(0).getId(), response.body().getPackage_id(), response.body().getPromote_id(), new PaymentDialog.PaymentCallBack() {
                                    @Override
                                    public void onPaymentSuccess() {

                                        Toast.makeText(getActivity(), "Product promoted successfully.", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);


                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        ProductFrgament fragment = new ProductFrgament();
                                        fragment.setArguments(bundle);
                                        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

                                    }

                                    @Override
                                    public void onPaymentFail(String message) {
                                        Toast.makeText(getActivity(), "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);
                                    }

                                    @Override
                                    public void onCancelDialog() {
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        ProductFrgament fragment = new ProductFrgament();
                                        fragment.setArguments(bundle);
                                        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
                                    }
                                }).show();
                               */
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
                                }).show();*//*

                            } catch (Exception e) {

                            }
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                            ProductFrgament fragment = new ProductFrgament();
                            fragment.setArguments(bundle);
                            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                            clearForm(rootTag);
                        }
                       */
/* Bundle bundle=new Bundle();
                        bundle.putString(SAConstants.Keys.CAT_ID, catId);
                        bundle.putString(SAConstants.Keys.SUB_CAT_ID, subCatId);
                        bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategories);
                        SubCategoryFragment fragment=new SubCategoryFragment();
                        fragment.setArguments(bundle);*//*


                    } else {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("AddProductError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(rootTag, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e( "onFailure: ",t.getLocalizedMessage());
                Log.e( "onFailure: ",""+t.getCause().toString());
                Snackbar.make(rootTag, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }
*/

    public void editProduct(AddProductModel addProductModel) {

/*        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<GetProductList> addProductCall = service.editProductApi(addProductModel.getProduct_id()
                , addProductModel.getUser_id(), addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                , addProductModel.getPayment_mode(), addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                , addProductModel.getFixed_price(), addProductModel.getProduct_type(), addProductModel.getQuantity(), addProductModel.getDescription()
                , addProductModel.getPromote_product(), addProductModel.getNo_of_clicks(), addProductModel.getBudget(), addProductModel.getTags()
                , addProductModel.getImage1(), addProductModel.getImage2(), addProductModel.getImage3(), addProductModel.getImage4(), addProductModel.getImage5(), addProductModel.getImage6()
                , addProductModel.getImage7(), addProductModel.getImage8());
        addProductCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {

                    Log.e("onResponseMethod","editProduct");

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

                        if (isPromotingProduct.equalsIgnoreCase("Y")) {

                            try {
                                PromoteDialog.create(getActivity(), response.body().getResult().get(0).getId(), new PromoteDialog.PromoteCallback() {
                                    @Override
                                    public void onPromoteSuccess() {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);
*//*

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                    ProductFrgament fragment = new ProductFrgament();
                                    fragment.setArguments(bundle);
                                    ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
*//*

                                    }

                                    @Override
                                    public void onPromoteFailure() {
                                        Toast.makeText(getActivity(), "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);
                                    }
                                }).show();
                            } catch (Exception e) {

                            }
                        } else {
                           *//* Bundle bundle = new Bundle();
                            bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                            ProductFrgament fragment = new ProductFrgament();
                            fragment.setArguments(bundle);
                            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();*//*
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                            clearForm(rootTag);
                        }
                       *//* Bundle bundle=new Bundle();
                        bundle.putString(SAConstants.Keys.CAT_ID, catId);
                        bundle.putString(SAConstants.Keys.SUB_CAT_ID, subCatId);
                        bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategories);
                        SubCategoryFragment fragment=new SubCategoryFragment();
                        fragment.setArguments(bundle);*//*

                    } else {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("AddProductError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(rootTag, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(rootTag, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });*/
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            } else if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            } else if (view instanceof CheckBox) {
                ((CheckBox) view).setSelected(false);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
      /*  final int size = imageList.size();
        if (size > 0) {
            for (int j = 0; j < size; j++) {
                imageList.remove(0);
            }
            add_product_cars_adapter.notifyItemRangeRemoved(0, size);
        }*/
        imageList.clear();
        add_product_cars_adapter.notifyDataSetChanged();
        tagList.clear();
        addProductTagsAdapter.notifyDataSetChanged();
        /*final int tagsize = tagList.size();
        if (tagsize > 0) {
            for (int j = 0; j < size; j++) {
                tagList.remove(0);
            }
            addProductTagsAdapter.notifyItemRangeRemoved(0, tagsize);
        }*/
    }
    @Override
    public void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();

        if (Global.videopath.equals("no_image")) {

            thumbnailVideo.setImageDrawable(getResources().getDrawable(R.drawable.rec_icon));

        }
        else {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(Global.videopath, MediaStore.Images.Thumbnails.MICRO_KIND);
            thumbnailVideo.setImageBitmap(thumb);
            thumbnailVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);


        }
    }

    @OnClick({R.id.rl3, R.id.rl4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl3:
                spinnerPaymentMode.performClick();
                break;
            case R.id.rl4:
                spinnerModeOfDelivery.performClick();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.videopath="no_image";
    }
}
