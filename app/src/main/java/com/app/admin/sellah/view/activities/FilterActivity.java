package com.app.admin.sellah.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.controller.utils.SubCategoryController;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.model.extra.SpinnerStateCheck;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SpinnerStateAdapter;
import com.app.admin.sellah.view.fragments.SubCategoryFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.Global.hideKeyboard;

public class FilterActivity extends AppCompatActivity {

    Spinner spCondition;
    Spinner spDelivery;
    ImageView imageView1, imageView2, imageView3, imageView4;
    @BindView(R.id.cross_icon)
    ImageView crossIcon;
    LinearLayout filter_price_drop_down;
    RelativeLayout price;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.tv_price_lower_to_high)
    TextView tvPriceLowerToHigh;
    @BindView(R.id.iv_price_lower_to_high)
    ImageView ivPriceLowerToHigh;
    @BindView(R.id.tv_price_HtoL)
    TextView tvPriceHtoL;
    @BindView(R.id.iv_price_high_t0_low)
    ImageView ivPriceHighT0Low;
    @BindView(R.id.tv_recent)
    TextView tvRecent;
    @BindView(R.id.iv_recent)
    ImageView ivRecent;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(R.id.iv_popularity)
    ImageView ivPopularity;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.spinner_condition)
    Spinner spinnerCondition;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.spinner_price)
    RelativeLayout spinnerPrice;
    @BindView(R.id.edt_minimum_price)
    EditText edtMinimumPrice;
    @BindView(R.id.txt_maximum_price)
    EditText txtMaximumPrice;
    @BindView(R.id.filter_price_drop_down)
    LinearLayout filterPriceDropDown;
    @BindView(R.id.rl6)
    RelativeLayout rl6;
    @BindView(R.id.lin_root)
    LinearLayout linRoot;
    @BindView(R.id.filter_root)
    ScrollView filterRoot;
    @BindView(R.id.txt_condition)
    TextView txtCondition;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.txt_category)
    TextView txtCategory;
    @BindView(R.id.txt_payment_type)
    TextView txtPaymentType;
    @BindView(R.id.txt_payment_negotiable)
    TextView txtPaymentNegotiable;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    private ViewGroup transitionsContainer;
    @BindView(R.id.li_price_LtoH)
    LinearLayout priceLtoH;
    @BindView(R.id.li_price_HtoL)
    LinearLayout priceHtoL;
    @BindView(R.id.li_recent)
    LinearLayout filterRecent;
    @BindView(R.id.li_populer)
    LinearLayout filterPopuler;
    @BindView(R.id.sp_category)
    Spinner spinnerCategory;

    @BindView(R.id.spinner_payment_mode)
    Spinner spinnerPaymentMode;

    @BindView(R.id.spinner_nagotiable)
    Spinner spinnerNagotiable;

    @BindView(R.id.spinner_mode_of_delivery)
    Spinner spinnerModeOfDelivery;


    private ArrayList<LinearLayout> filterLayouts;

    SubCategoryController controller;
    String filterByPrice = SAConstants.Keys.Filter_recent;
    String condition = "", paymentType = "", priceNegotiabilty = "", minimumPrice = "", maximumprice = "", delivery = "";
    String categoryId;
    private ArrayList<String> catList;
    private String filterCondition = "";
    private String filterCategory = "";
    private String fltrSort = "";
    private String fltrCondition = "";
    private String fltrCategory = "";
    private String fltrPaymentType = "";
    private String fltrPriceNego = "";
    private String fltrPriceMin = "";
    private String fltrPriceMax = "";
    private String fltrDelivery = "";
    private String TAG = FilterActivity.class.getSimpleName();
    int count = 0;
    private SpinnerStateAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        StatusBarLightMode(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(SAConstants.Keys.CAT_ID)) {
            categoryId = intent.getStringExtra(SAConstants.Keys.CAT_ID);
            count = intent.getIntExtra(SAConstants.Keys.FLTR_COUNT, 0);
        } else {
        }
        if (count > 0) {
            checkValuePresistance();
        }
        filterInit();
        getCategoryList();
        controller = new SubCategoryFragment();
        spCondition = findViewById(R.id.spinner_condition);
        spDelivery = (Spinner) findViewById(R.id.spinner_mode_of_delivery);
        transitionsContainer = findViewById(R.id.lin_root);
        filter_price_drop_down = findViewById(R.id.filter_price_drop_down);
        imageView1 = findViewById(R.id.iv_price_lower_to_high);
        imageView2 = findViewById(R.id.iv_price_high_t0_low);
        imageView3 = findViewById(R.id.iv_recent);
        imageView4 = findViewById(R.id.iv_popularity);
        price = findViewById(R.id.spinner_price);

        ArrayList<SpinnerStateCheck> conditionList = new ArrayList<>();
        ArrayList<SpinnerStateCheck> categoryList = new ArrayList<>();
        ArrayList<SpinnerStateCheck> deliveryList = new ArrayList<>();
        ArrayList<SpinnerStateCheck> paymentOptions = new ArrayList<>();
        ArrayList<SpinnerStateCheck> priceNegitiable = new ArrayList<>();


        // custom checkbox for delivery
        final String[] select_delivery = {"Delivery", "Free Postal Deliver", "Collection Only"};

        for (int j = 0; j < select_delivery.length; j++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_delivery[j]);
            if (!TextUtils.isEmpty(fltrDelivery)) {
//                Log.e(TAG, "onCreate: " + fltrDelivery);
                delivery = fltrDelivery;
                List<String> items = Arrays.asList(fltrDelivery.split("\\s*,\\s*"));
                for (String option : items) {
                    Log.e(TAG, "onCreate: " + option);
                    try {
                        if (stateVO.getTitle().equalsIgnoreCase(option.trim())) {
                            stateVO.setSelected(true);
                        }/* else {
                            stateVO.setSelected(false);
                        }*/
                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }

            } else {
//                stateVO.setSelected(false);
            }

            deliveryList.add(stateVO);
        }

//for condition
        //custom checkbox for condition
        final String[] select_condition = {"Condition", "New", "Used"};

        for (int i = 0; i < select_condition.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_condition[i]);
            if (!TextUtils.isEmpty(fltrCondition)) {
                condition = fltrCondition;
                List<String> items = Arrays.asList(fltrCondition.split("\\s*,\\s*"));
                for (String option : items) {
                    try {
                        if (option.equalsIgnoreCase("N")) {
                            if (i == 1) {
                                stateVO.setSelected(true);
                            }
                        } else {
                            if (i == 2) {
                                stateVO.setSelected(true);
                            }
                        }/* else {
                            stateVO.setSelected(false);
                        }*/
                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }

            } else {
//                stateVO.setSelected(false);
            }

//            stateVO.setSelected(false);
            conditionList.add(stateVO);
        }
        //custom checkbox for condition
//        final  select_category = catList;

        changeSort();
        for (int i = 0; i < catList.size(); i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(catList.get(i).toString());
            if (!TextUtils.isEmpty(filterCategory)) {
//                categoryId = filterCategory;
                categoryId = ExpandableListData.getCatId(filterCategory);
                if(stateVO.getTitle().equalsIgnoreCase(filterCategory)){
                    stateVO.setSelected(true);
                }else{
                    stateVO.setSelected(false);
                }
               /* List<String> items = Arrays.asList(filterCategory.split("\\s*,\\s*"));
                for (String option : items) {
                    try {
                        if (select_delivery[i].equalsIgnoreCase(option)) {
                            stateVO.setSelected(true);
                        } else {
                            stateVO.setSelected(false);
                        }
                    } catch (Exception e) {
                        stateVO.setSelected(false);
                    }
                }*/

            } else {
//                stateVO.setSelected(false);
            }

//            stateVO.setSelected(false);
            categoryList.add(stateVO);
        }

        final CharSequence[] select_paymentoption = getResources().getTextArray(R.array.payment_accepted);

        for (int i = 0; i < select_paymentoption.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_paymentoption[i].toString());
            if (!TextUtils.isEmpty(fltrPaymentType)) {
                paymentType = fltrPaymentType;
                List<String> items = Arrays.asList(fltrPaymentType.split("\\s*,\\s*"));
                for (String option : items) {
                    try {
                        if (stateVO.getTitle().equalsIgnoreCase(option.trim())) {
                            stateVO.setSelected(true);
                        } /*else {
//                            stateVO.setSelected(false);
                        }*/
                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }

            } else {
//                stateVO.setSelected(false);
            }

//            stateVO.setSelected(false);
            paymentOptions.add(stateVO);
        }
        final CharSequence[] select_Negotiation = getResources().getTextArray(R.array.Nagotiable);

        for (int i = 0; i < select_Negotiation.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_Negotiation[i].toString());
            if (!TextUtils.isEmpty(fltrPriceNego)) {
                priceNegotiabilty = fltrPriceNego;
                List<String> items = Arrays.asList(fltrPriceNego.split("\\s*,\\s*"));
                for (String option : items) {
                    try {
                        if (option.equalsIgnoreCase("Y")) {
                            if (i == 1) {
                                stateVO.setSelected(true);
                            }
                        } else {
                            if (i == 2) {
                                stateVO.setSelected(true);
                            }
//                            stateVO.setSelected(false);
                        }
                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }

            } else {
//                stateVO.setSelected(false);
            }
//            stateVO.setSelected(false);
            priceNegitiable.add(stateVO);
        }
        myAdapter = new SpinnerStateAdapter(FilterActivity.this, 0, conditionList, true, "condition", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            condition = selection;
            if (TextUtils.isEmpty(selection)) {
                txtCondition.setText("Condition");
            } else {
//                String value="";
                ArrayList<String> value=new ArrayList<>();
                 List<String> items = Arrays.asList(selection.split("\\s*,\\s*"));
                for (String option : items) {
                    try {
                        if (option.equalsIgnoreCase("U")) {
                            if(!value.contains("Used"))
                                value.add("Used");

                        } else if(option.equalsIgnoreCase("N")) {
                            if(!value.contains("New"))
                                value.add("New");
//                            value.add("New");
                        }else {

                        }

                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }
//                selection = selection.replace("N", "New");
//                selection = selection.replace("U", "Used");
                txtCondition.setText(TextUtils.join(",", value));
            }
        });
        SpinnerStateAdapter myAdapterDelivery = new SpinnerStateAdapter(FilterActivity.this, 0, deliveryList, true, "delivery", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            delivery = selection;
            if (TextUtils.isEmpty(selection)) {
                txtDelivery.setText(select_delivery[0]);
            } else {
                txtDelivery.setText(selection);
            }
        });
        SpinnerStateAdapter myAdapterCategory = new SpinnerStateAdapter(FilterActivity.this, 0, categoryList, false, "category", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
//            filterCategory = selection;
            categoryId = ExpandableListData.getCatId(selection);
            if (TextUtils.isEmpty(selection)) {
                txtCategory.setText("Category");
            } else {
                txtCategory.setText(selection);
            }
            saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_CATEGORY, selection);
        });
        SpinnerStateAdapter myAdapterPaymentOtions = new SpinnerStateAdapter(FilterActivity.this, 0, paymentOptions, true, "payment", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            paymentType = selection;
            if (TextUtils.isEmpty(selection)) {
                txtPaymentType.setText(select_paymentoption[0]);
            } else {
                txtPaymentType.setText(selection);
            }
        });

        SpinnerStateAdapter myAdapterNegotiable = new SpinnerStateAdapter(FilterActivity.this, 0, priceNegitiable, false, "fixedPrice", (selection, instanceOf) -> {
//            Log.e("Selection", "onCreate: " + selection.substring(0, 1) + " : " + instanceOf);
            try{
            priceNegotiabilty = selection.substring(0, 1);
            if (TextUtils.isEmpty(selection)) {
                txtPaymentNegotiable.setText(select_Negotiation[0]);
            } else {
                txtPaymentNegotiable.setText(selection);
            }}catch (Exception e){}
        });

        spCondition.setAdapter(myAdapter);
        spDelivery.setAdapter(myAdapterDelivery);
        spinnerCategory.setAdapter(myAdapterCategory);
        spinnerPaymentMode.setAdapter(myAdapterPaymentOtions);
        spinnerNagotiable.setAdapter(myAdapterNegotiable);

        /*****************************************************************/


       /* price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                TransitionManager.beginDelayedTransition(transitionsContainer);
                if (filter_price_drop_down.getVisibility() == View.VISIBLE) {
                    filter_price_drop_down.setVisibility(View.GONE);
                } else {
                    filter_price_drop_down.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });*/

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(transitionsContainer);
                }
                if (filter_price_drop_down.getVisibility() == View.VISIBLE) {
                    filter_price_drop_down.setVisibility(View.GONE);
                    hideKeyboard(price, FilterActivity.this);
                } else {
                    filter_price_drop_down.setVisibility(View.VISIBLE);
                }
            }
        });
/********************************************************************************************/


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView1.isClickable()) {
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    imageView4.setVisibility(View.GONE);
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView2.isClickable()) {
                    imageView1.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    imageView4.setVisibility(View.GONE);
                }
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView3.isClickable()) {
                    imageView2.setVisibility(View.GONE);
                    imageView1.setVisibility(View.GONE);
                    imageView4.setVisibility(View.GONE);
                }
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView4.isClickable()) {
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    imageView1.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkValuePresistance() {
//        if (Global.getUser.isLogined(this)) {
        try {

            fltrSort = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_SORT);
            fltrCondition = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_CONDITION);
            fltrCategory = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_CATEGORY);
            fltrPaymentType = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PAYMENT_TYPE);
            fltrPriceMin = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PRICE_MIN);
            fltrPriceMax = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PRICE_MAX);
            fltrDelivery = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_DELIVERY);
            fltrPriceNego = HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PRICE_NEGO);

            if(!TextUtils.isEmpty(fltrCondition)){
                List<String> items = Arrays.asList(fltrCondition.split("\\s*,\\s*"));
                String condition= filterCondition;
                ArrayList<String> value =new ArrayList<>();
                for (String option : items) {
                    try {
                        if (option.equalsIgnoreCase("U")) {
                           if(!value.contains("Used"))
                           value.add("Used");
                        } else if(option.equalsIgnoreCase("N")){
                            if(!value.contains("New"))
                            value.add("New");
                        }
                    } catch (Exception e) {
//                        stateVO.setSelected(false);
                    }
                }
                txtCondition.setText(TextUtils.join(",", value));
            }else{
                txtCondition.setText("Condition");
            }
            if(!TextUtils.isEmpty(fltrPriceNego)){
                txtPaymentNegotiable.setText(fltrPriceNego);
            }else{
                txtPaymentNegotiable.setText("Price Negotiable");
            }
            if(!TextUtils.isEmpty(fltrCategory)){
                txtCategory.setText(fltrCategory);
            }
            if(!TextUtils.isEmpty(fltrPaymentType)){
                txtPaymentType.setText(fltrPaymentType);
            }
            if(!TextUtils.isEmpty(fltrDelivery)){
                txtDelivery.setText(fltrDelivery);
            }
//        }
        } catch (Exception e) {

        }
    }

    private void saveFilterValueToPresist(String key, String value) {
        HelperPreferences.get(this).saveString(key, value);
        /*switch (key){
            case SAConstants.FilterKeys.FLTR_SORT:
                HelperPreferences.get(this).saveString(key,value);
                break;
            case SAConstants.FilterKeys.FLTR_CONDITION:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_CONDITION);
                break;
            case SAConstants.FilterKeys.FLTR_CATEGORY:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_CATEGORY);
                break;
            case SAConstants.FilterKeys.FLTR_PAYMENT_TYPE:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PAYMENT_TYPE);
                break;
            case SAConstants.FilterKeys.FLTR_PRICE_MIN:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PRICE_MIN);
                break;
            case SAConstants.FilterKeys.FLTR_PRICE_MAX:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_PRICE_MAX);
                break;
            case SAConstants.FilterKeys.FLTR_DELIVERY:
                HelperPreferences.get(this).getString(SAConstants.FilterKeys.FLTR_DELIVERY);
                break;
                default:
                    Log.e(TAG, "saveFilterValueToPresist: default" );
                    break;*/
    }


    private void changeSort() {
        try {
            switch (fltrSort) {
                case SAConstants.Keys.Price_ltOH:
                    filterSelection(0);
                    break;
                case SAConstants.Keys.Price_hTol:
                    filterSelection(1);
                    break;
                case SAConstants.Keys.Filter_recent:
                    filterSelection(2);
                    break;
                case SAConstants.Keys.Filter_popuarity:
                    filterSelection(3);
                    break;
                default:
                    filterSelection(0);
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void getCategoryList() {
        GetCategoriesModel model = ExpandableListData.getData();
        catList = new ArrayList<>();
        catList.add("Category");
        for (int i = 0; i < model.getResult().size(); i++) {
            catList.add(model.getResult().get(i).getName());
        }
    }

   /* private String getCategoryName(String catId) {
        GetCategoriesModel model = ExpandableListData.getData();
       String catName="";
        for(ResultFeatured result : model.getResult()){
            if(result.getCatId().equals(catId)){
                catName=result.getName();
            }
        }
        return catName;
    }*/

    private void filterInit() {
        filterLayouts = new ArrayList<>();
        filterLayouts.add(priceLtoH);
        filterLayouts.add(priceHtoL);
        filterLayouts.add(filterRecent);
        filterLayouts.add(filterPopuler);
    }

    @OnClick({R.id.cross_icon})
    public void onViewClicked() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    @OnClick({R.id.li_price_LtoH})
    public void onPriceLtoH() {

        filterByPrice = SAConstants.Keys.Price_ltOH;
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_SORT, filterByPrice);
        filterSelection(0);
    }

    @OnClick({R.id.li_price_HtoL})
    public void onPriceHtoL() {

        filterByPrice = SAConstants.Keys.Price_hTol;
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_SORT, filterByPrice);
        filterSelection(1);
    }

    @OnClick({R.id.li_recent})
    public void onFilterRecent() {

        filterByPrice = SAConstants.Keys.Filter_recent;
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_SORT, filterByPrice);
        filterSelection(2);
    }

    @OnClick({R.id.li_populer})
    public void onFilterpopuler() {

        filterByPrice = SAConstants.Keys.Filter_popuarity;
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_SORT, filterByPrice);
        filterSelection(3);
    }

    @OnClick({R.id.btn_apply})
    public void onApplyClick() {
        filterApi();
    }

    public void filterApi() {

        Dialog dialog = S_Dialogs.getLoadingDialog(FilterActivity.this);
        dialog.show();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_DELIVERY, delivery);
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_PAYMENT_TYPE, paymentType);
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_PRICE_NEGO, priceNegotiabilty);
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_CONDITION, condition);
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_PRICE_MIN, Global.getText(edtMinimumPrice));
        saveFilterValueToPresist(SAConstants.FilterKeys.FLTR_PRICE_MAX, Global.getText(txtMaximumPrice));
        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Call<GetProductList> fillterCall = service.applyFilterApi(filterByPrice, condition, categoryId, paymentType, priceNegotiabilty, Global.getText(edtMinimumPrice), Global.getText(txtMaximumPrice), delivery);
        fillterCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Intent resultIntent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putParcelable("productList", response.body());
                    extras.putString(SAConstants.Keys.CAT_ID, categoryId);
                    extras.putInt(SAConstants.Keys.CAT_POS, spinnerCategory.getSelectedItemPosition());
                    extras.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) ExpandableListData.getSubcatgoriesList(categoryId));
                    resultIntent.putExtras(extras);
                    setResult(Activity.RESULT_OK, resultIntent);
                    SubCategoryFragment fragment = new SubCategoryFragment();
                    finish();
//                    onBackPressed();
//                    controller.onFilterResponse(response.body());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("filter_failure", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("filter_failure", t.getMessage());
                Snackbar.make(filterRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    public void filterSelection(int postion) {
        LinearLayout view;
        for (int i = 0; i < filterLayouts.size(); i++) {
            view = filterLayouts.get(i);
            if (i == postion) {
                Log.e("pos_clicked", "" + i);
                for (int j = 0; j < view.getChildCount(); j++) {
                    View subView = view.getChildAt(j);
                    if (subView instanceof ImageView) {
                        ((ImageView) subView).setVisibility(View.VISIBLE);
//                        ((ImageView) subView).setColorFilter(ContextCompat.getColor(this, R.color.colorRed));
                    }
                    if (subView instanceof TextView) {
                        ((TextView) subView).setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    }
                }
            } else {
                for (int j = 0; j < view.getChildCount(); j++) {
                    View subView = view.getChildAt(j);
                    if (subView instanceof ImageView) {
                        ((ImageView) subView).setVisibility(View.GONE);
//                        ((ImageView) subView).setColorFilter(ContextCompat.getColor(this, R.color.colorBlack));
                    }
                    if (subView instanceof TextView) {
                        ((TextView) subView).setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
                    }
                }
                Log.e("pos_reset", "" + i);
            }


        }
    }

    @OnClick(R.id.rl1)
    public void onConditionClicked() {
        spinnerCondition.performClick();
    }

    @OnClick(R.id.rl2)
    public void onCategorylicked() {
        spinnerCategory.performClick();
    }

    @OnClick(R.id.rl3)
    public void onPaymentClicked() {
        spinnerPaymentMode.performClick();
    }

    @OnClick(R.id.rl4)
    public void onPriceNegoClicked() {
        spinnerNagotiable.performClick();
    }

    @OnClick(R.id.rl6)
    public void onDeliveryClicked() {
        spinnerModeOfDelivery.performClick();
    }
}


