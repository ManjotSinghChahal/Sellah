package com.app.admin.sellah.view.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.Prodctfragment_click;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.GolfModel;
import com.app.admin.sellah.model.extra.LikeModel.LikeModel;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.ProductDetails.ProductDetailModel;
import com.app.admin.sellah.model.extra.ProductDetails.ProductImage;
import com.app.admin.sellah.model.extra.ProductDetails.Promote;
import com.app.admin.sellah.model.extra.ProductDetails.Tag;
import com.app.admin.sellah.model.extra.commentModel.CommentModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.BuyerMakeOfferDialog;
import com.app.admin.sellah.view.CustomDialogs.PromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;
import com.app.admin.sellah.view.CustomDialogs.Stripe_image_verification_dialogfragment;
import com.app.admin.sellah.view.activities.AddNewVideos;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.Previewvideo;
import com.app.admin.sellah.view.adapter.GolfAdapter;
import com.app.admin.sellah.view.adapter.ProductInfoCommentAdapter;
import com.app.admin.sellah.view.adapter.PromotePackagesAdapter;
import com.app.admin.sellah.view.adapter.SimpleTagAdapter;
import com.app.admin.sellah.view.adapter.SuggestedPostAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.Global.getTimeAgo;
import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.Global.makeTextViewResizable;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.MAKE_OFFER_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

public class ProductFrgament extends Fragment implements View.OnClickListener, Prodctfragment_click {


    ViewPager mPager;
    int currentPage = 0;
    View view;
    Integer[] viewImages = {R.drawable.carlogo, R.drawable.carlogo, R.drawable.carlogo, R.drawable.carlogo, R.drawable.carlogo};
    ArrayList<String> viewImagesArray = new ArrayList<>();
    ArrayList<GolfModel> listData = new ArrayList<>();
    RecyclerView commentsRecyclerView, suggesteRecyclerView;
    ProductInfoCommentAdapter gAdapter;
    SimpleTagAdapter tagAdapter;
    SuggestedPostAdapter suggestedPostAdapter;
    RelativeLayout rlBack, title, options;
    public ImageView thumbLike, rateImage;
    boolean thumbStatus = false;
    boolean rateStatus = false;
    BottomSheetDialog filterDialog;
    ImageView add_camera, add_gallery;
    int CAMERA_PIC_REQUEST = 1313;
    final int ACTIVITY_SELECT_IMAGE = 1234;
    String product = "";
    @BindView(R.id.btn_chat)
    Button btnChat;
    @BindView(R.id.btn_makeOffer)
    Button btnMakeOffer;
    @BindView(R.id.txt_product_name)
    TextView txtProductName;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.rec_tag)
    RecyclerView recTag;
    @BindView(R.id.starImg)
    ImageView starImg;
    @BindView(R.id.thumbImage)
    ImageView thumbImage;
    @BindView(R.id.count_tv)
    TextView countTv;
    @BindView(R.id.txt_product_location)
    TextView txtProductLocation;
    @BindView(R.id.txt_product_condition)
    TextView txtProductCondition;

    @BindView(R.id.card1)
    RelativeLayout card1;
    @BindView(R.id.txt_product_description)
    TextView txtProductDescription;
    @BindView(R.id.img_user_profile)
    CircleImageView imgUserProfile;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.item_recycler)
    RecyclerView itemRecycler;
    @BindView(R.id.item1_recycler)
    RecyclerView item1Recycler;
    @BindView(R.id.txt_product_post_time)
    TextView txtProductPostTime;
    @BindView(R.id.product_detail_root)
    RelativeLayout productDetailRoot;
    @BindView(R.id.txt_otheruser_name)
    TextView txtOtheruserName;
    @BindView(R.id.img_medal)
    ImageView imgMedal;
    @BindView(R.id.img_send_camera)
    ImageView imgSendCamera;
    @BindView(R.id.img_send_gallery)
    ImageView imgSendGallery;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.li_chat_offer)
    LinearLayout liChatOffer;
    boolean ishasthumbnail = false;

    @BindView(R.id.img_online)
    ImageView imgOnline;
    @BindView(R.id.rv_offer_list)
    RecyclerView rvOfferList;
    @BindView(R.id.txt_offer)
    TextView txtOffer;
    @BindView(R.id.card_promote_packages)
    CardView cardPromotePackages;
    @BindView(R.id.txt_remaining_package)
    TextView txtRemainingPackage;
    @BindView(R.id.li_promotion_detail)
    LinearLayout liPromotionDetail;
    @BindView(R.id.txt_offer_price)
    TextView txtOfferPrice;
    @BindView(R.id.txt_offer_duration)
    TextView txtOfferDuration;
    @BindView(R.id.card_promote_detail)
    CardView cardPromoteDetail;

    @BindView(R.id.txt_negotiable_product)
    TextView txtNegotiableProduct;
    @BindView(R.id.txt_sellall_product)
    TextView txtSellallProduct;
    @BindView(R.id.productback)
    ImageView productback;
    @BindView(R.id.edit_quanitity)
    TextView editQuanitity;
    @BindView(R.id.sub_cat_option)
    ImageView subCatOption;
    @BindView(R.id.promote_daysleft)
    TextView promoteDaysleft;
    @BindView(R.id.txt_views)
    TextView txtViews;
    @BindView(R.id.shareBlackIcon)
    ImageView shareBlackIcon;
    private RecyclerView tagRcView;
    ArrayList<String> tagList;
    Button btn_makeOffer;
    ImageView user_profile;
    Unbinder unbinder;
    Result productDetial;
    WebService service;
    private Dialog dialog;
    String videourl;
    String prodct_id, package_id;

    GetProductList getProductList;
    String quantity = "";
    String product_id;
    boolean showAll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(PUSH_NOTIFICATION));
        view = inflater.inflate(R.layout.product_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        hideSearch();
        service = Global.WebServiceConstants.getRetrofitinstance();

        if (getArguments() != null && getArguments().containsKey(SAConstants.Keys.PRODUCT_DETAIL)) {
            Log.e("productPrint", "inner");
            productDetial = getArguments().getParcelable(SAConstants.Keys.PRODUCT_DETAIL);

            //   Log.e("valueFrag",SAConstants.Keys.PRODUCT_DETAIL);
            //   Log.e("valueFrag",getArguments().getParcelable(SAConstants.Keys.PRODUCT_DETAIL));

            Log.e("productPrint", productDetial.getId() + " inner");
            Log.e("productPrint", productDetial.getName() + " inner");

            Gson gson = new Gson();
            String json = gson.toJson(productDetial);
            Log.e("responsePrint", json);


        } else if (getArguments() != null && getArguments().containsKey("FEATUREDPOSTS")) {
            product_id = getArguments().getString("FEATUREDPOSTS");

        }

        dialog = S_Dialogs.getLoadingDialog(getActivity());

        initViews(view);


        if (productDetial != null) {


            getProductDetailsApi(productDetial.getId());
            getProductUrlApi(productDetial.getId());
            //  dummcheck(productDetial.getId());
//            setUpData(productDetial);
        }
        //-------comming from featured posts--------------
        else if (product_id != null) {
            getProductDetailsApi(product_id);
            getProductUrlApi(product_id);
        } else {

            //----------------to open specific product from deep link----------------------
            if (Global.DEEP_LINKING_STATUS.equalsIgnoreCase("enable")) {

                getProductDetailsApi(Global.DEEP_LINKING_PRODUCT_ID);
                getProductUrlApi(Global.DEEP_LINKING_PRODUCT_ID);

                //--------------clearing deep linking status-----------------
                if (Global.DEEP_LINKING_STATUS.equalsIgnoreCase("enable"))
                    Global.DEEP_LINKING_STATUS = "disable";

            }

            //----------------------------------------------
            else {

                getActivity().onBackPressed();
            }

        }


        return view;
    }

    private void initViews(View view) {
        commentsRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler);
        suggesteRecyclerView = view.findViewById(R.id.item1_recycler);
        tagRcView = view.findViewById(R.id.rec_tag);
        btn_makeOffer = view.findViewById(R.id.btn_makeOffer);
        user_profile = view.findViewById(R.id.img_user_profile);

        rlBack = view.findViewById(R.id.rl_back);
        options = view.findViewById(R.id.rl_option);
        thumbLike = view.findViewById(R.id.thumbImage);
        rateImage = view.findViewById(R.id.starImg);
        filterDialog = new BottomSheetDialog(getActivity());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_camera = view.findViewById(R.id.img_send_camera);
        add_gallery = view.findViewById(R.id.img_send_gallery);
//        title = view.findViewById(R.id.rl_title);


        listeners();
        txtSellallProduct.setPaintFlags(txtSellallProduct.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (productDetial != null && productDetial.getCatId() != null)
            getSuggestedPostList(productDetial.getCatId());
        else
            getSuggestedPostList("");

        productback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();

                if (bundle != null) {


                    String myString = bundle.containsKey("from_add") ? bundle.getString("from_add") : "default";

                    Log.e("onClick: ", myString);
                    if (myString.equalsIgnoreCase("from_add")) {
                        Log.e("onClick: ", "ff");
                        ((MainActivity) getActivity()).loadFragment(new HomeFragment(), HOMETAG);
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment());
                    } else {
                        Log.e("onClick: ", "esl");
                        getActivity().onBackPressed();
                    }
                } else {
                    ((MainActivity) getActivity()).loadFragment(new HomeFragment(), HOMETAG);
                }
            }
        });


        shareBlackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = Global.DEEP_LINKING_PRODUCT_URL;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Do check out this product at Sellah!:");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));

            }
        });



       /* LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        gAdapter = new ProductInfoCommentAdapter(getActivity(), listData, getActivity().getSupportFragmentManager());
        commentsRecyclerView.setAdapter(gAdapter);*/


    }

    private void setUpSuggestedPosts(GetProductList list) {

        getProductList = list;
        Log.e("fetchDataProduct", list.getResult().get(0).getCategoryName());
        List<Result> resultList = list.getResult();
        for (int i = 0; i < resultList.size(); i++) {
            Result row = resultList.get(i);
            if (productDetial != null && productDetial.getId() != null) {
                if (row.getId().equals(productDetial.getId())) {
                    list.getResult().remove(list.getResult().indexOf(row));
                }
            } else if (product_id != null) {
                if (row.getId().equals(product_id)) {
                    list.getResult().remove(list.getResult().indexOf(row));
                }
            }

        }
        suggestedPostAdapter = new SuggestedPostAdapter(getActivity(), list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //    LinearLayoutManager addLayoutManager = new LinearLayoutManager(getApplicationContext());
        suggesteRecyclerView.setLayoutManager(layoutManager);
        suggesteRecyclerView.setAdapter(suggestedPostAdapter);
    }

    private void setUpData(com.app.admin.sellah.model.extra.ProductDetails.Result productDetial1) {

        if (productDetial1 != null) {
            productDetial = new Result();
            productDetial.setUserId(productDetial1.getUserId());
            productDetial.setUsername(productDetial1.getUsername());
            productDetial.setNoOfClicks(productDetial1.getNoOfClicks());
            productDetial.setId(productDetial1.getId());
            productDetial.setPrice(productDetial1.getPrice());
            productDetial.setName(productDetial1.getName());
            productDetial.setPromoteProduct(productDetial1.getPromoteProduct());
            productDetial.setDescription(productDetial1.getDescription());
            productDetial.setQuantity(productDetial1.getQuantity());
            productDetial.setProductType(productDetial1.getProductType());
            productDetial.setFixedPrice(productDetial1.getFixedPrice());
            productDetial.setCategoryName(productDetial1.getCategoryName());
            productDetial.setSubcategoryName(productDetial1.getSubcategoryName());

            productDetial.setName(productDetial1.getName());


            try {

                setTitle(productDetial1.getName());
                txtProductName.setText(productDetial1.getName());
                //        txtProductLocation.setText(productDetial.get);

                if (productDetial1.getProduct_view_count().isEmpty())
                    txtViews.setText("0 view");
                else
                    txtViews.setText(productDetial1.getProduct_view_count() + " views");

                /*if (productDetial1.getSellInternationally().equalsIgnoreCase("N"))
                    txtProductLocation.setText("Singapore");
                else
                    txtProductLocation.setText("Internationally");*/

                if (productDetial1.getLocation() == null || productDetial1.getLocation().equalsIgnoreCase(""))
                    txtProductLocation.setText("Singapore");
                else
                    txtProductLocation.setText(productDetial1.getLocation());


                if (productDetial1.getFixedPrice().equalsIgnoreCase("Y")) {

                    txtNegotiableProduct.setText("Non-negotiable");


                } else {
                    txtNegotiableProduct.setText("Negotiable");
                }


                if (!productDetial1.getOnlineStatus().equalsIgnoreCase("off")) {
                    tvStatus.setText("Online");
                    imgOnline.setVisibility(View.VISIBLE);
                    imgOnline.setBackgroundResource(R.drawable.dot_online);
                } else {
                    tvStatus.setText("Last seen at : " + Global.getTimeAgo(Global.convertUTCToLocal(productDetial1.getLastSeenTime())));
                    imgOnline.setVisibility(View.VISIBLE);
                    imgOnline.setBackgroundResource(R.drawable.red_dot_icon);
                }

                if (productDetial1.getProductType().equalsIgnoreCase("U")) {
                    txtProductCondition.setText("Used");
                } else {
                    txtProductCondition.setText("New");
                }
                if (productDetial1.getQuantity().isEmpty() || Integer.parseInt(productDetial1.getQuantity()) <= 0) {
                    editQuanitity.setText("Out of Stock");
                    btn_makeOffer.setBackground(getContext().getResources().getDrawable(R.drawable.round_red_border_testimonial_unselected));


                } else {
                    editQuanitity.setText(productDetial1.getQuantity() + " in stock only");
                    btn_makeOffer.setBackground(getContext().getResources().getDrawable(R.drawable.make_offer_btn_bg));

                }


                txtProductDescription.setText(productDetial1.getDescription());

                if (txtProductDescription.getLineCount() > 5) {
                    makeTextViewResizable(txtProductDescription, 5, ".. See More", true);
                }


                if (HelperPreferences.get(getActivity()).getString(UID) != null && HelperPreferences.get(getActivity()).getString(UID).equalsIgnoreCase(productDetial.getUserId())) {
                    rateImage.setVisibility(View.GONE);
                    liChatOffer.setVisibility(View.GONE);
                } else {

                    /* no need of rate image now */
                    rateImage.setVisibility(View.GONE);
                    liChatOffer.setVisibility(View.VISIBLE);
                }
                if (!productDetial1.getIsWishlist().equalsIgnoreCase("Y")) {
                    rateImage.setImageResource(R.drawable.star_icon_new);
                    rateStatus = true;
                } else {
                    rateImage.setImageResource(R.drawable.star_icon);
                    rateStatus = false;
                }
                if (!productDetial1.getIsLiked().equalsIgnoreCase("N")) {
                    thumbLike.setImageResource(R.drawable.like);
                    thumbStatus = true;
                } else {
                    thumbLike.setImageResource(R.drawable.thumb_icon);
                    thumbStatus = false;

                }
                countTv.setText(productDetial1.getLikeCount().toString());
//                Global.convertUTCToLocal(productDetial.getCreatedAt());
                txtProductPostTime.setText("Posted " + getTimeAgo(Global.convertUTCToLocal(productDetial1.getCreatedAt())));
//                txtProductPostTime.setText(getTimeAgo(productDetial.getCreatedAt()));
                setTagAdapter(productDetial1.getTags());
                setUpPackages(productDetial1.getUserId(), productDetial1.getPromotes(), productDetial1.getPromoteProduct(), productDetial1.getNoOfClicks());

                if (productDetial1.getProductVideo().equals("") || productDetial1.getProductVideo() == null) {
                    ishasthumbnail = false;
                } else {
                    videourl = productDetial1.getProductVideo();
                    viewImagesArray.add(productDetial1.getProductVideo_thumbnail());
                    ishasthumbnail = true;
                }


                initViewPagger(productDetial1.getProductImages());
                prodct_id = productDetial1.getId();
                if (productDetial1.getPromotes() != null && productDetial1.getPromotes().size() > 0) {
                    setupPromoteOptions(productDetial1.getId(), productDetial1.getPromotes().get(0).getId());
                    package_id = productDetial1.getPromotes().get(0).getId();
                } else {
                    setupPromoteOptions(productDetial1.getId(), "");
                }
//                RequestOptions requestOptions = Global.getGlideOptions();
                Glide.with(getActivity())
                        .load(productDetial1.getImage())
                        .apply(Global.getGlideOptions())
                        .into(imgUserProfile);
                txtOtheruserName.setText(!TextUtils.isEmpty(productDetial1.getUsername()) ? productDetial1.getUsername() : "Sellah! user");

                getCommentApi(productDetial1.getId(), false);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    private void setUpPackages(String userId, List<Promote> promoteDetail, String promoteStatus, String no_of_clicks) {

        if (promoteDetail != null && userId.equalsIgnoreCase(HelperPreferences.get(getActivity()).getString(UID)) && promoteStatus.equalsIgnoreCase("S")) {

            if (promoteDetail.size() > 0) {
                cardPromotePackages.setVisibility(View.VISIBLE);
                cardPromoteDetail.setVisibility(View.VISIBLE);
                promoteDaysleft.setText(promoteDetail.get(0).getDays_left() + " " + "days");

           /* List<PackagesList> packagesList = new ArrayList<>();
            PackagesList list = new PackagesList();
            list.setAmount("25");
            list.setClicks("125");
            list.setValidity("25");
            packagesList.add(list);
            list = new PackagesList();
            list.setAmount("50");
            list.setClicks("135");
            list.setValidity("55");
            packagesList.add(list);*/
                int _totalCount = 0;
                try {
                    _totalCount = Integer.parseInt(promoteDetail.get(0).getClicks()) + Integer.parseInt(no_of_clicks);

                } catch (Exception e) {
                    _totalCount = Integer.parseInt(promoteDetail.get(0).getClicks());

                }
                PromotePackagesAdapter adapter = new PromotePackagesAdapter(getActivity(), no_of_clicks, _totalCount, promoteDetail, (id) -> {


                    PopupMenu popup = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    } else {
                        popup = new PopupMenu(getActivity(), rvOfferList);
                    }
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.promote_option_new, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (!Global.NetworStatus.isOnline(getActivity()) || Global.NetworStatus.isInternetAvailable()) {
                                S_Dialogs.getNetworkErrorDialog(getActivity()).show();
                            } else {

                                switch (item.getItemId()) {

                                    case R.id.promote_cancel_promote:
                                        try {
                                            S_Dialogs.getCancelPromotion(getActivity(), ((dialog, which) -> {
                                                cancelPromotionApi(productDetial.getUserId(), prodct_id, id);
                                            })).show();
                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), "Invalid inputs.", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case R.id.promote_update_promote:
                                        PromoteDialog.create(getActivity(), prodct_id, new PromoteDialog.PromoteCallback() {
                                            @Override
                                            public void onPromoteSuccess() {
                                                Toast.makeText(getActivity(), "Promote package is updated successfully.", Toast.LENGTH_SHORT).show();
                                                productDetial.setPromoteProduct("S");
                                                getProductDetailsApi(prodct_id);
                                            }

                                            @Override
                                            public void onPromoteFailure() {
                                                Toast.makeText(getActivity(), "Unable to update promote package at this movement.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).show();
                                        break;


                                }
                            }
                            return true;
                        }
                    });


                });
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rvOfferList.setLayoutManager(horizontalLayoutManager1);
                rvOfferList.setAdapter(adapter);
                Global.getTotalHeightofLinearRecyclerView(getActivity(), rvOfferList, R.layout.layout_packages_detail, 0);
                int totalCount = 0;
                try {
                    totalCount = Integer.parseInt(promoteDetail.get(0).getClicks()) + Integer.parseInt(productDetial.getNoOfClicks());

                } catch (Exception e) {
                    totalCount = Integer.parseInt(promoteDetail.get(0).getClicks());

                }
                if (no_of_clicks == null || no_of_clicks.equalsIgnoreCase(""))
                    txtRemainingPackage.setText("0" + "/" + totalCount + " clicks");
                else
                    txtRemainingPackage.setText(no_of_clicks + "/" + totalCount + " clicks");


            } else {
                cardPromoteDetail.setVisibility(View.GONE);
                cardPromotePackages.setVisibility(View.GONE);
            }
        } else {

            cardPromoteDetail.setVisibility(View.GONE);
            cardPromotePackages.setVisibility(View.GONE);
        }
    }

    private void setTagAdapter(List<Tag> productTags) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        tagAdapter = new SimpleTagAdapter(getActivity(), productTags, true);
        tagRcView.setLayoutManager(layoutManager);
        tagRcView.setAdapter(tagAdapter);
    }

    private void listeners() {
        add_camera.setOnClickListener(this);
        add_gallery.setOnClickListener(this);
        thumbLike.setOnClickListener(this);
        rateImage.setOnClickListener(this);
        btn_makeOffer.setOnClickListener(this);
        user_profile.setOnClickListener(this);
        cardPromoteDetail.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.card_promote_detail:

                PopupMenu popup = new PopupMenu(getActivity(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.promote_option_new, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (!Global.NetworStatus.isOnline(getActivity()) || Global.NetworStatus.isInternetAvailable()) {
                            S_Dialogs.getNetworkErrorDialog(getActivity()).show();
                        } else {

                            switch (item.getItemId()) {

                                case R.id.promote_cancel_promote:
                                    try {
                                        S_Dialogs.getCancelPromotion(getActivity(), ((dialog, which) -> {
                                            cancelPromotionApi(productDetial.getUserId(), prodct_id, package_id);
                                        })).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Invalid inputs.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.promote_update_promote:
                                    PromoteDialog.create(getActivity(), prodct_id, new PromoteDialog.PromoteCallback() {
                                        @Override
                                        public void onPromoteSuccess() {
                                            Toast.makeText(getActivity(), "Promote package is updated successfully.", Toast.LENGTH_SHORT).show();
                                            productDetial.setPromoteProduct("S");
                                            getProductDetailsApi(prodct_id);
                                        }

                                        @Override
                                        public void onPromoteFailure() {
                                            Toast.makeText(getActivity(), "Unable to update promote package at this movement.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                                    break;


                            }
                        }
                        return true;
                    }
                });
                break;


            case R.id.img_send_camera:
                if (isLogined(getActivity())) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_PIC_REQUEST);
                        } else {
                            Log.v("", "Permission is revoked");
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                    }
                } else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }

                break;
/******************************************************************************************/
            case R.id.img_send_gallery:
                if (isLogined(getActivity())) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            Intent i = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                        } else {
                            Log.v("", "Permission is revoked");
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                    } else {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                    }
                } else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }

                break;
/***********************************************************************************************/
            case R.id.thumbImage:

                /*if (!thumbStatus) {
                    thumbLike.setImageResource(R.drawable.like);
                    thumbStatus = true;
                } else {
                    thumbLike.setImageResource(R.drawable.thumb_icon);
                    thumbStatus = false;

                }*/
                if (isLogined(getActivity())) {
                    if (thumbStatus) {

                        if (productDetial != null) {
                            unlikeProductApi(productDetial.getId() != null ? productDetial.getId() : "");
                            removeItemFromWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                        } else if (product_id != null) {
                            unlikeProductApi(product_id != null ? product_id : "");
                            removeItemFromWishlist(product_id != null ? product_id : "");
                        }

                    } else {

                        if (productDetial != null) {
                            likeProductApi(productDetial.getId() != null ? productDetial.getId() : "");
                            addItemToWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                        } else if (product_id != null) {
                            likeProductApi(product_id != null ? product_id : "");
                            addItemToWishlist(product_id != null ? product_id : "");
                        }


                    }
                } else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }

                break;
            /********************************************************************************/
            case R.id.starImg:

                if (isLogined(getActivity())) {
                    if (!rateStatus) {
//                        rateImage.setImageResource(R.drawable.star_icon_new);
//                        rateStatus = true;
                        if (productDetial != null)
                            removeItemFromWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                        else if (product_id != null)
                            removeItemFromWishlist(product_id != null ? product_id : "");
                    } else {
                        if (productDetial != null)
                            addItemToWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                        else if (product_id != null)
                            addItemToWishlist(product_id != null ? product_id : "");
//                        rateImage.setImageResource(R.drawable.star_icon);
//                        rateStatus = false;
                    }
                } else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }
                break;
            /***********************************************************************************/
            case R.id.btn_makeOffer:
                if (isLogined(getActivity())) {
//                    navLogout.setVisibility(View.VISIBLE);


                    if (quantity.isEmpty() || Integer.parseInt(quantity) <= 0) {
                        Toast.makeText(getActivity(), "Product is out of stock", Toast.LENGTH_SHORT).show();
                    } else {

                        if ((HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED).equals("") || HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED).equals("N"))) {
                            S_Dialogs.getLiveVideoStopedDialog(getActivity(), "You are not currently connected with stripe. Press ok to connect.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
                                stripe_dialogfragment.show(getActivity().getFragmentManager(), "");

                            })).show();
                        } else if ((HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED).equalsIgnoreCase("P"))) {
                            S_Dialogs.getLiveVideoStopedDialog(getActivity(), "You have not uploaded you Idenitification Documents. Press ok to upload.", ((dialog, which) -> {
                                //--------------openHere-----------------

                                Stripe_image_verification_dialogfragment stripe_dialogfragment = new Stripe_image_verification_dialogfragment();
                                stripe_dialogfragment.show(getActivity().getFragmentManager(), "");

                            })).show();
                        } else {
                            BuyerMakeOfferDialog.create(getActivity(), productDetial.getPrice(), quantity, new BuyerMakeOfferDialog.OnmakeOfferClick() {
                                @Override
                                public void onMakeOfferClick(String price, String quantity, BuyerMakeOfferDialog buyerMakeOfferDialog) {

                                    Gson gson = new Gson();
                                    String json = gson.toJson(productDetial);
                                    Log.e("responsePrintOffer", json);


                                    makeOfferApi(productDetial.getId(), price, productDetial.getName(), quantity, buyerMakeOfferDialog);
                                }
                            }).show();

                        }

                    }



                /*    S_Dialogs.getMakeOfferDialog(getActivity(), productDetial.getPrice(), (dialog, input) -> {
                        if (TextUtils.isEmpty(input) || input.toString().equalsIgnoreCase("0")) {
                            Toast.makeText(getActivity(), "Please enter offering amount", Toast.LENGTH_SHORT).show();
                        } else {
                            makeOfferApi(productDetial.getId(), input.toString().trim(), productDetial.getName(), dialog);
                        }
                    }).show();*/
                } else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }

                break; /***********************************************************************************/
            case R.id.img_user_profile:

                Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.OTHER_USER_ID, productDetial.getUserId());
                if (HelperPreferences.get(getActivity()).getString(UID) != null && HelperPreferences.get(getActivity()).getString(UID).equalsIgnoreCase(productDetial.getUserId())) {
                    loadFragment(new ProfileFragment(), PROFILETAG, bundle);
                    ((MainActivity) getActivity()).changeOptionColor(4);
                } else {
                    loadFragment(new PersonalProfileFragment(), HOMETAG, bundle);
                }
                break;
        }
    }


    public void initViewPagger(List<ProductImage> productImages) {


        for (int i = 0; i < productImages.size(); i++)

            viewImagesArray.add(productImages.get(i).getImage());

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new GolfAdapter(ishasthumbnail, getActivity(), viewImagesArray, this));

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


    }

    private void setTitle(String title) {
        ((MainActivity) getActivity()).text_sell.setText(title);
    }

    public void hideSearch() {


        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Golf Plus 2005");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(0);


        /**********************************************************************************************/

    }

    private void setupPromoteOptions(String productId, String packageId) {
        subCatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isLogined(getActivity())) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_options, popup.getMenu());
                if (rateStatus)
                    popup.getMenu().findItem(R.id.wishlist).setTitle("Add To Wishlist");
                else
                    popup.getMenu().findItem(R.id.wishlist).setTitle("Remove From Wishlist");
                if (isLogined(getActivity()) && !TextUtils.isEmpty(productDetial.getUserId()) && productDetial.getUserId().equalsIgnoreCase(HelperPreferences.get(getActivity()).getString(UID))
                        && !TextUtils.isEmpty(packageId)) {
                    if (productDetial.getPromoteProduct().equalsIgnoreCase("S")) {
                        popup.getMenu().findItem(R.id.menu_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_update_promote).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.menu_promote).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_update_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(false);
                    }
                } else {
                    if (isLogined(getActivity()) && productDetial.getUserId().equalsIgnoreCase(HelperPreferences.get(getActivity()).getString(UID))) {
                        popup.getMenu().findItem(R.id.menu_promote).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_update_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.wishlist).setVisible(false);
                    } else {
                        popup.getMenu().findItem(R.id.wishlist).setVisible(true);
                        popup.getMenu().findItem(R.id.menu_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_update_promote).setVisible(false);
                        popup.getMenu().findItem(R.id.menu_cancel_promote).setVisible(false);
                    }

                }

                if (productDetial.getUserId().equalsIgnoreCase(HelperPreferences.get(getActivity()).getString(UID))) {
                    popup.getMenu().findItem(R.id.menu_mark_sold).setVisible(true);
                    popup.getMenu().findItem(R.id.menu_delete).setVisible(true);
                    popup.getMenu().findItem(R.id.menu_report).setVisible(false);
                    popup.getMenu().findItem(R.id.wishlist).setVisible(false);
                    popup.getMenu().findItem(R.id.menu_edit_product).setVisible(true);
                } else {

                    popup.getMenu().findItem(R.id.menu_mark_sold).setVisible(false);
                    popup.getMenu().findItem(R.id.menu_delete).setVisible(false);
                    popup.getMenu().findItem(R.id.wishlist).setVisible(true);
                    popup.getMenu().findItem(R.id.menu_report).setVisible(true);
                    popup.getMenu().findItem(R.id.menu_edit_product).setVisible(false);
                }
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (!Global.NetworStatus.isOnline(getActivity()) || Global.NetworStatus.isInternetAvailable()) {
                            S_Dialogs.getNetworkErrorDialog(getActivity()).show();
                        } else {

                            switch (item.getItemId()) {

                                case R.id.wishlist:

                                    if (isLogined(getActivity())) {
                                        if (!rateStatus) {
//                        rateImage.setImageResource(R.drawable.star_icon_new);
//                        rateStatus = true;
                                            removeItemFromWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                                            popup.getMenu().findItem(R.id.wishlist).setTitle("Add To Wishlist");
                                        } else {
                                            addItemToWishlist(productDetial.getId() != null ? productDetial.getId() : "");
                                            popup.getMenu().findItem(R.id.wishlist).setTitle("Remove From Wishlist");
//                        rateImage.setImageResource(R.drawable.star_icon);
//                        rateStatus = false;
                                        }
                                    } else {
                                        S_Dialogs.getLoginDialog(getActivity()).show();
                                    }

                                    break;
                                case R.id.menu_cancel_promote:
                                    try {
                                        S_Dialogs.getCancelPromotion(getActivity(), ((dialog, which) -> {
                                            cancelPromotionApi(productDetial.getUserId(), productId, packageId);
                                        })).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Invalid inputs.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.menu_update_promote:
                                    PromoteDialog.create(getActivity(), productId, new PromoteDialog.PromoteCallback() {
                                        @Override
                                        public void onPromoteSuccess() {
                                            Toast.makeText(getActivity(), "Promote package is updated successfully.", Toast.LENGTH_SHORT).show();
                                            productDetial.setPromoteProduct("S");
                                            getProductDetailsApi(productId);
                                        }

                                        @Override
                                        public void onPromoteFailure() {
                                            Toast.makeText(getActivity(), "Unable to update promote package at this movement.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                                    break;
                                case R.id.menu_promote:
                                    PromoteDialog.create(getActivity(), productId, new PromoteDialog.PromoteCallback() {
                                        @Override
                                        public void onPromoteSuccess() {
                                            Toast.makeText(getActivity(), "Promote package is updated successfully.", Toast.LENGTH_SHORT).show();
//                                saleList.getResult().get(position).setPromoteProduct("S");
//                                notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onPromoteFailure() {
                                            Toast.makeText(getActivity(), "Unable to update promote package at this movement.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                                    break;

                                case R.id.menu_share:
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    String shareBodyText = Global.DEEP_LINKING_PRODUCT_URL;
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Do check out this product at Sellah!:");
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                                    startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                                    return true;
                                case R.id.menu_delete:
                                    S_Dialogs.getConfirmation(getActivity(), getActivity().getResources().getString(R.string.dialog_title_delete_product), ((dialog1, which) -> {
                                        deleteProduct(productDetial.getId());
                                    })).show();
                                    return true;
                                case R.id.menu_mark_sold:
                                    S_Dialogs.getConfirmation(getActivity(), getActivity().getResources().getString(R.string.dialog_title_mark_as_sold), ((dialog1, which) -> {
                                        markProductAsSold(productDetial.getId());
                                    })).show();
                                    return true;
                                case R.id.menu_edit_product:

                                    ArrayList<Result> list = new ArrayList<>();
                                    list.addAll(getProductList.getResult());

                                    Gson gson = new Gson();
                                    String resp = gson.toJson(productDetial);
                                    Log.e("valuePrintHere", resp);

                                    Intent intentt = new Intent(getActivity(), AddNewVideos.class);
                                    Bundle bundle = new Bundle();
                                    if (productDetial != null) {
                                        bundle.putParcelable("modelProductList", productDetial);
                                        bundle.putString("Product", product);
                                        intentt.putExtra("way_status", "SalesAdapter");
                                        intentt.putExtra("position", String.valueOf(0));
                                        intentt.putExtras(bundle);
                                        getActivity().startActivity(intentt);

                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
                                    }


                                  /*  Bundle bundle = new Bundle();
                                    bundle.putParcelable(PRODUCT_DETAIL, productDetial);
                                    SellFragment fragment = new SellFragment();
                                    fragment.setArguments(bundle);
                                    loadFragment(fragment, ADDPRODUCTTAG, bundle);*/

                                    return true;
                                case R.id.menu_report:
                                    if (isLogined(getActivity())) {
                                        filterDialog.setContentView(R.layout.filter_dialog);
                                        LinearLayout ll_reporting_item = filterDialog.findViewById(R.id.ll_reporting_item);
                                        LinearLayout l2_prohibited = filterDialog.findViewById(R.id.l2_prohibited);
                                        LinearLayout l3_mispriced = filterDialog.findViewById(R.id.l3_mispriced);
                                        LinearLayout l4_wrongCategroy = filterDialog.findViewById(R.id.l4_wrongCategroy);
                                        LinearLayout l5_duplicate = filterDialog.findViewById(R.id.l5_duplicate);
                                        LinearLayout l6_offensive = filterDialog.findViewById(R.id.l6_offensive);
                                        LinearLayout l7_irrelevant = filterDialog.findViewById(R.id.l7_irrelevant);
                                        LinearLayout l8_counterfeit = filterDialog.findViewById(R.id.l8_counterfeit);
                                        LinearLayout l9_cancel = filterDialog.findViewById(R.id.l9_cancel);
                                        filterDialog.show();

                                        l2_prohibited.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l2_prohibited);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l3_mispriced.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l3_mispriced);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l4_wrongCategroy.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l4_wrongCategroy);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l5_duplicate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l5_duplicate);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l6_offensive.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l6_offensive);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l7_irrelevant.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l7_irrelevant);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l8_counterfeit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                hitReportApi(l8_counterfeit);
                                                filterDialog.dismiss();
                                            }
                                        });
                                        l9_cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                filterDialog.dismiss();
                                            }
                                        });

                                    } else {
                                        S_Dialogs.getLoginDialog(getActivity());
                                    }
                            }
                        }
                        return true;
                    }
                });
                /*} else {
                    S_Dialogs.getLoginDialog(getActivity()).show();
                }*/


            }
        });
    }

    private void cancelPromotionApi(String userId, String productId, String packageId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> cancelPromotionCall = Global.WebServiceConstants.getRetrofitinstance().cancelPromotionApi(
                userId, productId, packageId
        );
        cancelPromotionCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        productDetial.setPromoteProduct("N");
                        getProductDetailsApi(productId);
                        Toast.makeText(getActivity(), "Your promotion against this product has been canceled successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("CancelPromotionApi", "error: " + response.body().getMessage());
                    }
                } else {
                    try {
                        Log.e("CancelPromotionApi", "error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("CancelPromotionApi", "onFailure: " + t.getMessage());
            }
        });

    }

    private void hitReportApi(LinearLayout layout) {

        new ReportApi().hitReportApi(getActivity(), layout
                , "", productDetial.getId(), (msg) -> {
                    Snackbar.make(productDetailRoot, msg, Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }, () -> {
                    Snackbar.make(productDetailRoot, "Please try again later", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    public boolean loadFragment(Fragment fragment, String Tag, Bundle bundle) {
        if (fragment != null) {
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(Tag).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @OnClick(R.id.btn_chat)
    public void onViewClicked(View view) {

        if (isLogined(getActivity())) {
//                    navLogout.setVisibility(View.VISIBLE);
            Log.e("OtheruserIt", "onViewClicked: " + productDetial.getId());
            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
            chatIntent.putExtra("otherUserId", productDetial.getUserId());
            chatIntent.putExtra("otherUserImage", productDetial.getImage());
            chatIntent.putExtra("otherUserName", productDetial.getUsername());
            startActivity(chatIntent);
        } else {
            S_Dialogs.getLoginDialog(getActivity()).show();
        }

    }

    private void addItemToWishlist(String productId) {
//        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();

        Call<Common> addItemToWishListcall = service.addToWishListApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        addItemToWishListcall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        rateImage.setImageResource(R.drawable.star_icon);
                        rateStatus = false;
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void removeItemFromWishlist(String productId) {
//        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();

        Call<Common> removeItemFromWishlist = service.removeFromWishListApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        removeItemFromWishlist.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        rateImage.setImageResource(R.drawable.star_icon_new);
                        rateStatus = true;
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        if (isLogined(getActivity())) {
            if (!TextUtils.isEmpty(Global.getText(edtMessage))) {
                addCommentApi(Global.getText(edtMessage), productDetial.getId());
            } else {
                Snackbar.make(productDetailRoot, "Comment can't be empty.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        } else {
            S_Dialogs.getLoginDialog(getActivity()).show();
        }

    }

    private void addCommentApi(String comment, String productId) {
//        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> addCommentCall = service.addCommentApi(HelperPreferences.get(getActivity()).getString(UID), productId, comment, "t");
        addCommentCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        edtMessage.setText("");
                        showAll=true;
                        getCommentApi(productDetial.getId(), showAll);
                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("comment_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
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
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE) {

            if (data != null) {
                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                addImageCommentApi(imagePath, productDetial.getId());


                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(this.getActivity(), imageBitmap);
            addImageCommentApi(getRealPathFromURI(tempUri), productDetial.getId());


        }
    }

    private void addImageCommentApi(String imagePath, String productId) {
//        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> addCommentCall = service.addImageCommentApi(
                (RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(getActivity()).getString(UID))))
                , (RequestBody.create(MediaType.parse("text/plain"), productId)), (RequestBody.create(MediaType.parse("text/plain"), ""))
                , (RequestBody.create(MediaType.parse("text/plain"), "img"))
                , ImageUploadHelper.convertImageTomultipart(imagePath, "image"));
        addCommentCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        getCommentApi(productDetial.getId(), false);/*
                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();*/
                    }

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("comment_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void getSuggestedPostList(String cat_id) {
        Call<GetProductList> addCommentCall = service.suggestedProductsApi(cat_id);
        addCommentCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        setUpSuggestedPosts(response.body());
                    }

                } else {
                    try {
                        Log.e("comment_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {

            }
        });
    }

    private void setCommentData(CommentModel commentData, boolean show) {


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        gAdapter = new ProductInfoCommentAdapter(getActivity(), show, commentData, getActivity().getSupportFragmentManager(), productDetial.getId(), new ProductInfoCommentAdapter.CommentCallbacks() {
            @Override
            public void onEditComment() {
                getCommentApi(productDetial.getId(), false);
            }

            @Override
            public void onDeleteComment(int pos) {
                commentData.getResult().remove(pos);
                gAdapter.notifyItemRemoved(pos);
                gAdapter.notifyItemRangeChanged(pos, commentData.getResult().size());
            }
        });
        commentsRecyclerView.setAdapter(gAdapter);
    }

    private void getCommentApi(String productId, boolean show) {
//        dialog = S_Dialogs.getLoadingDialog(getActivity());
//        dialog.show();
        Call<CommentModel> addCommentCall = service.getProductComment(HelperPreferences.get(getActivity()).getString(UID), productId);
        addCommentCall.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        if (response.body().getResult().size() < 2 || response.body().getResult().isEmpty()) {
                            if (txtSellallProduct != null)
                                txtSellallProduct.setVisibility(View.GONE);
                        } else {
                            if (txtSellallProduct != null)
                                txtSellallProduct.setVisibility(View.VISIBLE);
                        }
                        setCommentData(response.body(), show);

                        txtSellallProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (showAll) {
                                    showAll=false;
                                    getCommentApi(productDetial.getId(), showAll);
                                } else {
                                    showAll=true;
                                    getCommentApi(productDetial.getId(), showAll);
                                }
                            }
                        });
/*

                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();*/
                    }

                } else {

                    if (txtSellallProduct != null) txtSellallProduct.setVisibility(View.GONE);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                   /* Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();*/
                    try {
                        Log.e("comment_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    txtSellallProduct.setVisibility(View.GONE);
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void likeProductApi(String productId) {
        dialog.show();
        Call<LikeModel> likeProductCall = service.likeProductApi(HelperPreferences.get(getActivity()).getString(UID)
                , productId);
        likeProductCall.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        thumbLike.setImageResource(R.drawable.like);
                        thumbStatus = true;
                        countTv.setText(response.body().getLikeCount().toString());
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.e("likeapi", "onResponse: ");
                    }
                } else {
                    thumbLike.setImageResource(R.drawable.thumb_icon);
                    thumbStatus = false;
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("likeApiError", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                thumbLike.setImageResource(R.drawable.thumb_icon);
                thumbStatus = false;
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void unlikeProductApi(String productId) {
        dialog.show();
        Call<LikeModel> likeProductCall = service.unLikeProductApi(HelperPreferences.get(getActivity()).getString(UID)
                , productId);
        likeProductCall.enqueue(new Callback<LikeModel>() {
            @Override
            public void onResponse(Call<LikeModel> call, Response<LikeModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        thumbLike.setImageResource(R.drawable.thumb_icon);
                        thumbStatus = false;
                        countTv.setText(response.body().getLikeCount().toString());
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.e("unlikeapi", "onResponse: ");
                    }
                } else {
                    thumbLike.setImageResource(R.drawable.like);
                    thumbStatus = true;
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("likeApiError", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                thumbLike.setImageResource(R.drawable.thumb_icon);
                thumbStatus = false;
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void makeOfferApi(String productId, String price, String name, String quantity, BuyerMakeOfferDialog dialog) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(getActivity());
        dialog1.show();


        Log.e("rrrrr1", HelperPreferences.get(getActivity()).getString(UID));
        Log.e("rrrrr2", productId);
        Log.e("rrrrr3", productDetial.getUserId());
        Log.e("rrrrr4", price);
        Log.e("rrrrr5", name);
        Log.e("rrrrr6", quantity);

        Call<MakeOfferModel> makeOfferCall = Global.WebServiceConstants.getRetrofitinstance().makeOfferApi(HelperPreferences.get(getActivity()).getString(UID), productId, productDetial.getUserId(), price, "P", name, quantity);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        if (dialog1 != null && dialog1.isShowing()) {
                            dialog1.dismiss();
                        }
                        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                        chatIntent.putExtra("otherUserId", productDetial.getUserId());
                        chatIntent.putExtra("otherUserImage", productDetial.getImage());
                        chatIntent.putExtra("otherUserName", productDetial.getUsername());
                        chatIntent.putExtra(MAKE_OFFER_DATA, response.body());
                        getActivity().startActivity(chatIntent);
                    }
                } else {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    Snackbar.make(productDetailRoot, "Unable to make offer at the movement", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("MakeOfferFailure", "productFrag: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();

                Log.e("MakeOfferApi", "onFailure: " + t.getMessage());
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                if (message != null) {
                    switch (message.getNotiType()) {
                        case NT_ACCEPT_REJECT:

                            break;
                        case NT_FOLLOW:

                            break;
                        case NT_COMMENT_ADDED:
                            if (message.getProductId().equalsIgnoreCase(productDetial.getId())) {
//                                setUpData(productDetial);
                                getProductDetailsApi(message.getProductId());
                                Log.e("Product_comment", "onReceive");
                            } else {
                                Log.e("Product_comment", "onReceive: otherProduct");
                            }
                            break;
                        case NT_PRODUCT_ADDED:

                            break;
                        case NT_CHAT:

                            break;
                        case NT_PAYMENT:

                            break;
                        case NT_OFFER_MAKE:

                            break;
                        default:

                            break;
                    }
                }
                Log.e("receiver", "Got message: Produc" + message.getMessage());
            } catch (Exception e) {
                Log.e("receiver_failure", "onReceive: " + e.getMessage());
            }
        }
    };

    private void deleteProduct(String productId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<Common> deleteItemCall = service.deleteProductApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        deleteItemCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
//                        setSalesData(response.body());
                        getActivity().onBackPressed();
                    }
                } else {
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
                Log.e("Delete_Product", "onFailure: " + t.getMessage());
            }
        });
    }

    Call<Common> markAsSoldCall;

    private void markProductAsSold(String productId) {

        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        markAsSoldCall = service.markProductAsSoldApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        markAsSoldCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        getActivity().onBackPressed();
                    }
                } else {
                    Snackbar.make(productDetailRoot, "Something went's wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
                Log.e("MarkAsSold_Product", "onFailure: " + t.getMessage());
            }
        });
    }

    /*category click not need now
     */

   /* @OnClick(R.id.rel_category)
    public void onCategoryClicked() {
        Bundle extras = new Bundle();
        extras.putString(SAConstants.Keys.CAT_ID, productDetial.getCatId());
//        extras.putInt(SAConstants.Keys.CAT_POS,spinnerCategory.getSelectedItemPosition());
        extras.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) ExpandableListData.getSubcatgoriesList(productDetial.getCatId()));
        SubCategoryFragment fragment = new SubCategoryFragment();
        fragment.setArguments(extras);
        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(HOMETAG).commit();

    }
*/

    Call<ProductDetailModel> productDetailCall;

    public void getProductDetailsApi(String productId) {

        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        productDetailCall = service.getProductDetail(HelperPreferences.get(getActivity()).getString(UID), productId);
        productDetailCall.enqueue(new Callback<ProductDetailModel>() {
            @Override
            public void onResponse(Call<ProductDetailModel> call, Response<ProductDetailModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        Gson gson = new GsonBuilder().create();
                        quantity = response.body().getResult().getQuantity();

                        product = gson.toJson(response.body().getResult());
                        Log.e("ProductDetailApi", gson.toJson(response.body()));

//                        Snackbar.make(productDetailRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
//                                .setAction("", null).show();
//                        if (productDetial != null) {
                        // com.app.admin.sellah.model.extra.ProductDetails.ResultFeatured prductdetail1= response.body().getResult();
                        setUpData(response.body().getResult());
//                        } else {
//                            getActivity().onBackPressed();
//                        }
                    }


                } else {
                    Snackbar.make(productDetailRoot, "Something went wrong.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                    try {
                        Log.e("ProductDetailApi", "onResponse: failed" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getActivity().onBackPressed();

                }
            }

            @Override
            public void onFailure(Call<ProductDetailModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Snackbar.make(productDetailRoot, "Please Try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
                getActivity().onBackPressed();
                Log.e("ProductDetail_failure", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (productDetailCall != null) {
            productDetailCall.cancel();
        }
        if (markAsSoldCall != null) {
            markAsSoldCall.cancel();
        }
        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);

    }

    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    @Override
    public void onclick(boolean visible) {
        Intent previewintent = new Intent(getActivity(), Previewvideo.class);
        previewintent.putExtra("video", videourl);
        startActivity(previewintent);
    }


    //------------------generate product url---------------------
    public void getProductUrlApi(String productId) {
        Call<JsonObject> productUrl;
        productUrl = service.getProductUrl(HelperPreferences.get(getActivity()).getString(UID), productId, "p");
        productUrl.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            Global.DEEP_LINKING_PRODUCT_URL = jsonObject.getString("url");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}


