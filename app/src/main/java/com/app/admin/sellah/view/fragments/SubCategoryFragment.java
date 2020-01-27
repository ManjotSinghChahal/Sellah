package com.app.admin.sellah.view.fragments;


import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.Database.UserSearch;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.controller.utils.SubCategoryController;
import com.app.admin.sellah.model.extra.BannerModel.BannerModel;
import com.app.admin.sellah.model.extra.Categories.Subcategory;
import com.app.admin.sellah.model.extra.LiveVideoModel.LiveVideoModel;
import com.app.admin.sellah.model.extra.SubCatImageModel;
import com.app.admin.sellah.model.extra.SubCatTextModel;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.TouchDetectableScrollView;
import com.app.admin.sellah.view.activities.FilterActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.LiveVideoPaggerAdapter;
import com.app.admin.sellah.view.adapter.SubCategoryAdapter;
import com.app.admin.sellah.view.adapter.SubCategoryGridAdapter;
import com.app.admin.sellah.view.adapter.SubCategoryLiveAdapter;
import com.app.admin.sellah.view.adapter.VideoSubcategoriesAdpt;
import com.app.admin.sellah.view.adapter.ViewPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.utils.Global.getTotalHeightofGridRecyclerView;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;


public class SubCategoryFragment extends Fragment implements SubCategoryController {

    @BindView(R.id.txt_cat_name)
    TextView txtCatName;
    @BindView(R.id.viewPagerSubCat)
    ViewPager viewPagerSubCat;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.rel_adds)
    RelativeLayout relAdds;
    @BindView(R.id.text_live_video)
    TextView textLiveVideo;
    @BindView(R.id.text_no_live_video)
    TextView textNoLiveVideo;
    @BindView(R.id.vp_live)
    ViewPager vpLive;
    @BindView(R.id.vp_live_dots)
    LinearLayout vpLiveDots;
    @BindView(R.id.sub_cat_live_recycler)
    RecyclerView subCatLiveRecycler;
    @BindView(R.id.rec_sub_cat)
    RecyclerView recSubCat;
    @BindView(R.id.sub_cat_grid_recycler)
    RecyclerView subCatGridRecycler;
    @BindView(R.id.pb_home)
    ProgressBar pbHome;
    @BindView(R.id.txt_no_more_item)
    TextView txtNoMoreItem;
    @BindView(R.id.scroll_root)
    TouchDetectableScrollView scrollRoot;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.li_view_container)
    LinearLayout liViewContainer;
    @BindView(R.id.htab_toolbar)
    Toolbar htabToolbar;
    @BindView(R.id.htab_collapse_toolbar)
    CollapsingToolbarLayout htabCollapseToolbar;
    @BindView(R.id.htab_appbar)
    AppBarLayout htabAppbar;
    @BindView(R.id.htab_maincontent)
    CoordinatorLayout htabMaincontent;
    @BindView(R.id.rel_NoDataFound)
    RelativeLayout relNoDataFound;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;
    Call<GetProductList> getProductsCall;
    //For sale recycler initialization
    List<SubCatTextModel> saletextList = new ArrayList<>();
    RecyclerView recSubCategory;
    SubCategoryAdapter subCategoryAdapter;

    //live recycler initialization
    List<SubCatImageModel> liveList = new ArrayList<>();
    RecyclerView liveRecycler;
    SubCategoryLiveAdapter liveVideoAdapter;

    //for Grid recycler initialization
    SubCategoryGridAdapter subCategoryGridAdapter;
    RecyclerView subCategoryGridRecycler;

    int catPosition;
    String catId, sub_cat_id = "";

    List<Subcategory> subcategoriesList;
  //  private ArrayAdapter searchSuggessionAdapter;
    private ArrayList<String> searchSuggestions;
    private TextWatcher searchWacher;
    private TextView.OnEditorActionListener editorActionListener;
    GetProductList productList;
    private ArrayList<Result> resultList;

    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 0;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;
    private int previousPage = 0;
    private boolean isFeedsFetchInProgress = false;
    private String TAG = SubCategoryFragment.class.getSimpleName();
    int count = 0;
    private boolean isSearching = false;
    private Dialog dialog;
    private VideoSubcategoriesAdpt videoCategoriesAdptNew;

    View view;
    Unbinder unbinder;
    private ImageView[] dotsLive;
    private float mOriginalHeight;
    private boolean initialSizeObtained;
    private boolean isShrink;
    private Animation _hideAnimation;
    private Animation _showAnimation;
    private AppBarLayout.OnOffsetChangedListener offsetListner;
    private long timeVisibleDelay = 500;
    TextView txt;
    int dotscount_;

    public SubCategoryFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      /*  if(view==null) {
            view = inflater.inflate(R.layout.fragment_subcategory_fragment_new, container, false);
        }
        unbinder= ButterKnife.bind(this,view);*/
        hideSearch();
        txt = ((MainActivity) getActivity()).findViewById(R.id.title_sell);
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_subcategory_fragment_new, container, false);
            unbinder = ButterKnife.bind(this, view);
            dialog = S_Dialogs.getLoadingDialog(getActivity());
            dialog.show();
            catId = getArguments().containsKey(SAConstants.Keys.CAT_ID) ? getArguments().getString(SAConstants.Keys.CAT_ID) : "";
            catPosition = getArguments().containsKey(SAConstants.Keys.CAT_POS) ? getArguments().getInt(SAConstants.Keys.CAT_POS) : 0;
            subcategoriesList = getArguments().containsKey(SAConstants.Keys.SUB_CAT_LIST) ? getArguments().getParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST) : new ArrayList<>();
            sub_cat_id = getArguments().containsKey(SAConstants.Keys.SUB_CAT_ID) ? getArguments().getString(SAConstants.Keys.SUB_CAT_ID) : "";
            searchSuggestions = new ArrayList<>();
            getIds(view);

            productList = new GetProductList();
            resultList = new ArrayList<>();
            productList.setResult(resultList);
            setupProducList();
            getSubcategoryList(catId, sub_cat_id);
          /*   _hideAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) liViewContainer.getLayoutParams();
                    params.topMargin = -(int) (500 * interpolatedTime);
                    liViewContainer.setLayoutParams(params);
                }
            };

             _showAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) liViewContainer.getLayoutParams();
                    params.topMargin = (int) (mOriginalHeight * (interpolatedTime - 1));
                    liViewContainer.setLayoutParams(params);
                }
            };
            liViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (initialSizeObtained)
                        return;
                    initialSizeObtained = true;
                    mOriginalHeight = liViewContainer.getMeasuredHeight();
                }
            });
            _hideAnimation.setDuration(2000);
            _showAnimation.setDuration(2000);*/
            /**************************************************************/

            setUpAdapter();
            setLiveListVideos();

            /**********************************************************************/
            //for grid recycler set adapter
            /***************************************************************************************/
            //set view pager

            new ApisHelper().getBanner(HelperPreferences.get(getActivity()).getString(UID), new ApisHelper.OnGetDataListners() {
                @Override
                public void onGetDataSuccess(BannerModel body) {

/*                    List<String> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    List<String> list3 = new ArrayList<>();
                    List<String> list4 = new ArrayList<>();



                    for (int i = 0; i < body.getSubcategorybanners().size(); i++) {
                        list3 = new ArrayList<>();
                        list3.add(body.getSubcategorybanners().get(i).getBannerImage());
                        list4.addAll(list3);
                    }
                    Log.e("onGetDataSuccess: ", "" + list4);*/


                    ArrayList<HashMap<String,String>> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    List<String> list3 = new ArrayList<>();
                    List<String> list4 = new ArrayList<>();

                    for (int i = 0; i < body.getHomebanners().size(); i++) {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("bannerImage",body.getHomebanners().get(i).getBannerImage());
                        hashMap.put("bannerLink",body.getHomebanners().get(i).getBannerLink());


                        list1.add(hashMap);
                    }
                    setUpBannerAds(list1);
                }

                @Override
                public void onGetDataFailure() {
                 //   setUpBannerAds(null);
                }
            });

            searchProduct();
            getVideoListOther();
//            pagginationCode();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        setUpSwipeView();
   /*     AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) htabCollapseToolbar.getLayoutParams();
        p.setScrollFlags(0);
        htabCollapseToolbar.setLayoutParams(p);*/
//        unbinder = ButterKnife.bind(this, view);
       /* dialog =S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        catId = getArguments().containsKey(SAConstants.Keys.CAT_ID) ? getArguments().getString(SAConstants.Keys.CAT_ID) : "";
        catPosition = getArguments().containsKey(SAConstants.Keys.CAT_POS) ? getArguments().getInt(SAConstants.Keys.CAT_POS) : 0;
        subcategoriesList = getArguments().containsKey(SAConstants.Keys.SUB_CAT_LIST) ? getArguments().getParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST) : new ArrayList<>();
        sub_cat_id = getArguments().containsKey(SAConstants.Keys.SUB_CAT_ID) ? getArguments().getString(SAConstants.Keys.SUB_CAT_ID) : "";
        *//*if(subcategoriesList.size()!=0){
            sub_cat_id=subcategoriesList.get(0).getId();
        }*/
       /* searchSuggestions = new ArrayList<>();

        getIds(view);
        hideSearch();

        productList = new GetProductList();
        resultList = new ArrayList<>();
        productList.setResult(resultList);
        setupProducList();
        getSubcategoryList(catId, sub_cat_id);

        *//**************************************************************//*

        setUpAdapter();

        *//**********************************************************************//*
        //for grid recycler set adapter
        *//***************************************************************************************//*
        //set view pager

        new ApisHelper().getBanner(HelperPreferences.get(getActivity()).getString(UID), new ApisHelper.OnGetDataListners() {
            @Override
            public void onGetDataSuccess(BannerModel body) {
                setUpBannerAds(body.getRecord());
            }

            @Override
            public void onGetDataFailure() {

            }
        });

        searchProduct();

        setUpSwipeView();*/
        pagginationCode();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    //    Click --> Toggles the top toolbar
    /*public void ToggleTopBar(View view) {
        isShrink = !isShrink;

        liViewContainer.clearAnimation();  //Important
        liViewContainer.startAnimation(isShrink? _hideAnimation : _showAnimation);
    }
*/
    private void getIds(View view) {

        liveRecycler = view.findViewById(R.id.sub_cat_live_recycler);
        recSubCategory = view.findViewById(R.id.rec_sub_cat);
        subCategoryGridRecycler = view.findViewById(R.id.sub_cat_grid_recycler);

    }

    private void pagginationCode() {
        scrollRoot.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
            @Override
            public void onScroll() {

            }

            @Override
            public void onScrollUp() {
                Log.e(TAG, "onScrollUp: ");
                pbHome.setVisibility(View.GONE);
            }

            @Override
            public void onScrollDown() {
                pbHome.setVisibility(View.VISIBLE);
                Log.e(TAG, "onScrollDown: ");
            }

            @Override
            public void onBottomReached() {
                Log.e(TAG, "onBottomReached: ");
                if (isFeedsFetchInProgress) {
                    pbHome.setVisibility(View.VISIBLE);
                    return;
                }
                if (TOTAL_PAGES > 1) {

                    previousPage = currentPage;
                    currentPage = currentPage + 1;
                    isFeedsFetchInProgress = true;
                    pbHome.setVisibility(View.VISIBLE);
//                        loader = LOADER.BOTTOM;
                    if (currentPage <= TOTAL_PAGES) {
                        getSubcategoryList(catId, sub_cat_id);
                        Log.e(TAG, "onBottomReached: LoadNextPage");
                    } else {
                        isFeedsFetchInProgress = false;
                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.VISIBLE);
                    }

//                    }
                } else {
                    isFeedsFetchInProgress = false;
                    pbHome.setVisibility(View.GONE);
                    txtNoMoreItem.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTopReached() {

            }
        });
    }

    private void hideSearchBar() {

        ((MainActivity) getActivity()).rel_search.animate()
                .translationX(((MainActivity) getActivity()).rel_search.getWidth())
                .alpha(0.0f)
                .setDuration(timeVisibleDelay);
/*
        ((MainActivity) getActivity()).rlFilter.animate()
                .translationX(((MainActivity) getActivity()).rlFilter.getWidth())
                .alpha(0.0f)
                .setDuration(timeVisibleDelay);
*/
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlSearchIcon.setVisibility(View.VISIBLE);
/*
        ((MainActivity) getActivity()).rlSearchIcon.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(timeVisibleDelay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ((MainActivity) getActivity()).rlSearchIcon.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
*/

        ((MainActivity) getActivity()).text_sell.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(timeVisibleDelay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
                        ((MainActivity) getActivity()).text_sell.setText(txtCatName.getText());

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        if (txtCatName != null) {
            txtCatName.animate()
                    .translationY(-txtCatName.getHeight())
                    .alpha(0.0f)
                    .setDuration(timeVisibleDelay);
        }

        ((MainActivity) getActivity()).rlSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchBar();
            }
        });
    }

    private void showSearchBar() {
        ((MainActivity) getActivity()).rel_search.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(timeVisibleDelay);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlSearchIcon.setVisibility(View.GONE);
    /*    ((MainActivity) getActivity()).rlFilter.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(timeVisibleDelay);*/

/*
        ((MainActivity) getActivity()).rlSearchIcon.animate()
                .translationX(0)
                .alpha(0.0f)
                .setDuration(timeVisibleDelay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (animation.isPaused())
                            if ( ((MainActivity) getActivity()).rlSearchIcon!=null)
                            ((MainActivity) getActivity()).rlSearchIcon.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
*/
        ((MainActivity) getActivity()).text_sell.animate()
                .translationY(((MainActivity) getActivity()).text_sell.getHeight())
                .alpha(0.0f)
                .setDuration(timeVisibleDelay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


                            if (animation.isPaused())
                                if (txt != null)
                                    txt.setText("");
                            txt.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        if (txtCatName != null) {
            txtCatName.animate()
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(timeVisibleDelay);
        }

    }

    private void showSearchBarInstent() {
        ((MainActivity) getActivity()).rel_search.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(0);
        ((MainActivity) getActivity()).rlFilter.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(0);
        ((MainActivity) getActivity()).rlSearchIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlBack.setVisibility(View.GONE);

    }

    private void setUpSwipeView() {

        txtCatName.setVisibility(View.VISIBLE);
        txtCatName.setText(ExpandableListData.getCatName(catId));

        offsetListner = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                swipeContainer.setEnabled(verticalOffset == 0);
                if (verticalOffset != 0) {
                    Log.e(TAG, "onOffsetChanged: collapsed");
                    hideSearchBar();
                } else {
                    Log.e(TAG, "onOffsetChanged: expanded");
                    showSearchBar();
                }
            }
        };
        htabAppbar.addOnOffsetChangedListener(offsetListner);
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                swipeContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Reload current fragment
                        Bundle bundle = new Bundle();
                        bundle.putString(SAConstants.Keys.CAT_ID, catId);
                        bundle.putInt(SAConstants.Keys.CAT_POS, catPosition);
                        bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategoriesList);
                        SubCategoryFragment fragment = new SubCategoryFragment();
                        fragment.setArguments(bundle);
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(fragment);
                        ft.attach(fragment);
                        ft.commit();
                        swipeContainer.setRefreshing(false);
                    }
                }, 100);
            }
        });
        swipeContainer.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorRed));
//        swipeContainer.setDistanceToTriggerSync(20);// in dips
        swipeContainer.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
    }

    private void setUpAdapter() {

        //sub Category recycler set Adapter
//        forSaleList();
        if (subcategoriesList != null) {
            subCategoryAdapter = new SubCategoryAdapter(subcategoriesList, getActivity(), this, sub_cat_id);
            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recSubCategory.setLayoutManager(horizontalLayoutManager1);
            recSubCategory.setAdapter(subCategoryAdapter);
        }
        /*******************************************************************/
        //for live recycler set adapter


    }

    private void setLiveListVideos() {
        new ApisHelper().getCategoryLiveVideo(getActivity(), catId, new ApisHelper.SortLiveVideo() {
            @Override
            public void onSortLiveVideoSuccess(LiveVideoModel videoList) {
                /*LinearLayoutManager livehorizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                liveRecycler.setLayoutManager(livehorizontalLayoutManager);
                videoCategoriesAdptNew = new VideoSubcategoriesAdpt(getActivity(), videoList.getList());
                liveRecycler.setAdapter(videoCategoriesAdptNew);
*/
                if (videoList.getList().size() == 0) {
                    textNoLiveVideo.setVisibility(View.VISIBLE);
                } else {
                    textNoLiveVideo.setVisibility(View.GONE);
                }
//                Global.getTotalHeightofLinearRecyclerView(getActivity(), liveRecycler, R.layout.layout_sub_live_video_adapter, 0);
//                liveList();

                liveVideoAdapter = new SubCategoryLiveAdapter(videoList.getList(), getActivity());
                LinearLayoutManager livehorizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                liveRecycler.setLayoutManager(livehorizontalLayoutManager);
                liveRecycler.setAdapter(liveVideoAdapter);
            }

            @Override
            public void onSortLiveVideoFailure() {

            }
        });
    }

    private void searchProduct() {

        /*searchSuggessionAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.layout_autocomplete_text_design, R.id.textview_search, searchSuggestions);
        ((MainActivity) getActivity()).searchEditText.setAdapter(searchSuggessionAdapter);*/
        searchWacher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
                ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                 filterProducts(s.toString());
                Log.e(TAG, "afterTextChanged: ");
                if (s.length() < 1) {
                    dialog.show();
                    isSearching = false;
                    Log.e(TAG, "resetData: ");
                    Global.hideKeyboard(((MainActivity) getActivity()).searchEditText, getActivity());
                    currentPage = 1;
                    resultList.clear();
                    productList.setResult(resultList);
                    subCategoryGridAdapter.notifyDataSetChanged();
                    recSubCategory.setVisibility(View.VISIBLE);
                    getSubcategoryList(catId, sub_cat_id);
                    ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
                } else {
                    if (!isSearching) {
                        isSearching = true;
                        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.VISIBLE);
                        ((MainActivity) getActivity()).rlBack.setVisibility(View.GONE);

                    }
                }

            }
        };
        ((MainActivity) getActivity()).searchEditText.addTextChangedListener(searchWacher);
        ((MainActivity) getActivity()).searchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                String autoCompleteText = (String) adapterView.getItemAtPosition(i);
                ((MainActivity) getActivity()).searchEditText.setText(autoCompleteText);
                Global.hideKeyboard(((MainActivity) getActivity()).searchEditText, getActivity());

                Show_tag_result_fragment fragment = new Show_tag_result_fragment();

                Bundle bundle = new Bundle();
                bundle.putString("tag",Global.getText(((MainActivity) getActivity()).searchEditText));
                bundle.putString("root","search");
                fragment.setArguments(bundle);
                ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();


/*                String autoCompleteText = (String) adapterView.getItemAtPosition(i);
                ((MainActivity) getActivity()).searchEditText.setText(autoCompleteText);
                Global.hideKeyboard(((MainActivity) getActivity()).searchEditText, getActivity());
                new ApisHelper().getSeaarchResultApi(getActivity(), new ApisHelper.SearchResultCallback() {
                    @Override
                    public void onProductListSuccess(GetProductList body) {
                        Log.e("searchResult", "onProductListSuccess: ");
//                        setupProducList(body);
                        recSubCategory.setVisibility(View.GONE);
                        productList.setStatus(body.getStatus());
                        productList.setMessage(body.getMessage());
                        productList.setResult(body.getResult());
                        subCategoryGridAdapter.notifyDataSetChanged();
                        if (productList.getResult().size() > 0) {
                            getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
                        }
                    }

                    @Override
                    public void onProductListFailure() {
                        Log.e("searchResult", "onProductListFailure: ");
                    }
                }, Global.getText(((MainActivity) getActivity()).searchEditText));*/
            }
        });
        editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String text = Global.getText(((MainActivity) getActivity()).searchEditText);
                    if (!text.equalsIgnoreCase("")) {


                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(Global.getText(((MainActivity) getActivity()).searchEditText));


                        if (MainActivity.arrayListSearched.size() > 0) {
                            if (MainActivity.arrayListSearched.size() > 0 && MainActivity.arrayListSearched.size() < 5) {
                                for (int i = 0; i < MainActivity.arrayListSearched.size(); i++) {
                                    arrayList.add(MainActivity.arrayListSearched.get(i));

                                }
                            } else {
                                for (int i = 0; i < 4; i++) {
                                    arrayList.add(MainActivity.arrayListSearched.get(i));
                                }
                            }
                        }


                        MainActivity.arrayListSearched.clear();
                        MainActivity.arrayListSearched.addAll(arrayList);
                        ((MainActivity) getActivity()).searchAdapter.notifyDataSetChanged();


                        UserSearch userSearch = new UserSearch();
                        userSearch.setSearched(Global.getText(((MainActivity) getActivity()).searchEditText));
                        ((MainActivity) getActivity()).myDatabase.myDao().addSearch(userSearch);

                        ((MainActivity) getActivity()).recyclerviewSearch.setVisibility(View.GONE);

                        Show_tag_result_fragment fragment = new Show_tag_result_fragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("tag", Global.getText(((MainActivity) getActivity()).searchEditText));
                        bundle.putString("root", "search");
                        fragment.setArguments(bundle);
                        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

                    }

/*                    new ApisHelper().getSeaarchResultApi(getActivity(), new ApisHelper.SearchResultCallback() {
                        @Override
                        public void onProductListSuccess(GetProductList body) {
                            Log.e("searchResult", "onProductListSuccess: ");
//                            setupProducList(body);
                            recSubCategory.setVisibility(View.GONE);
                            productList.setStatus(body.getStatus());
                            productList.setMessage(body.getMessage());
                            productList.setResult(body.getResult());
                            subCategoryGridAdapter.notifyDataSetChanged();
                            if (productList.getResult().size() > 0) {
                                getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
                            }
                        }

                        @Override
                        public void onProductListFailure() {
                            Log.e("searchResult", "onProductListFailure: ");
                        }
                    }, Global.getText(((MainActivity) getActivity()).searchEditText));*/

//                    yourcalc();

                    return true;
                }
                return false;
            }
        };


        ((MainActivity) getActivity()).searchEditText.setOnEditorActionListener(editorActionListener);

    }

    private void setUpBannerAds(ArrayList<HashMap<String,String>> bannerAds) {
        if (bannerAds != null) {

//            .setVisibility(View.VISIBLE);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), bannerAds);
            viewPagerSubCat.setAdapter(viewPagerAdapter);
            dotscount_ = viewPagerAdapter.getCount();
            dots = new ImageView[dotscount_];
            //automatic slide
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (viewPagerSubCat != null) {
                        viewPagerSubCat.post(new Runnable() {
                            @Override
                            public void run() {


                                if (viewPagerSubCat!=null && viewPagerSubCat.getCurrentItem() == dotscount_ - 1) {
                                    viewPagerSubCat.setCurrentItem(0);
                                } else {
                                    viewPagerSubCat.setCurrentItem((viewPagerSubCat.getCurrentItem() + 1));
                                }
                            }
                        });

                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 5000, 5000);


            for (int i = 0; i < dotscount_; i++) {
                dots[i] = new ImageView(getActivity());
                dots[i].setImageResource(R.drawable.dot_icon);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,
                        15);
                params.setMargins(5, 0, 8, 0);
                SliderDots.addView(dots[i], params);
            }

//        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));

            viewPagerSubCat.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotscount_; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
//            cvAdds.setVisibility(View.GONE);
        }

    }

    private void setLiveVideos(LiveVideoModel body) {
        if (body.getList().size() > 0) {
            vpLive.setVisibility(View.VISIBLE);
            vpLiveDots.setVisibility(View.VISIBLE);
            textNoLiveVideo.setVisibility(View.GONE);
            LiveVideoPaggerAdapter viewPagerAdapter = new LiveVideoPaggerAdapter(getActivity(), body.getList());
            vpLive.setAdapter(viewPagerAdapter);
            if (body.getList().size() > 3) {
                dotscount = 3/*viewPagerAdapter.getCount()*/;
            } else {
                dotscount = viewPagerAdapter.getCount();
            }
//        dotscount = 4/*viewPagerAdapter.getCount()*/;
            dotsLive = new ImageView[dotscount];
            //automatic slide
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    vpLive.post(new Runnable() {
                        @Override
                        public void run() {

                            if (vpLive != null) {


                                if (vpLive.getCurrentItem() == dotscount - 1) {
                                    vpLive.setCurrentItem(0);
//                            Log.e("pagger","pos"+vPBannerAdd.getCurrentItem());
                                } else {
                                    vpLive.setCurrentItem((vpLive.getCurrentItem() + 1));
//                            Log.e("pagger_else","pos"+vPBannerAdd.getCurrentItem());
                                }
                            }
                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 5000, 5000);


            for (int i = 0; i < dotscount; i++) {
                dotsLive[i] = new ImageView(getActivity());
                dotsLive[i].setImageResource(R.drawable.dot_icon);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,
                        15);
                params.setMargins(5, 0, 8, 0);
                vpLiveDots.addView(dotsLive[i], params);
            }
//      dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));
            vpLive.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        for (int i = 0; i < dotscount; i++) {
                            dotsLive[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon));
                        }
                        dotsLive[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));

                    } catch (Exception e) {

                    }

                }


                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            vpLive.setVisibility(View.GONE);
            vpLiveDots.setVisibility(View.GONE);
            textNoLiveVideo.setVisibility(View.VISIBLE);
        }
    }

    public void getVideoListOther() {

        new ApisHelper().getLiveVideoData(getActivity(), String.valueOf(currentPage), catId, new ApisHelper.GetLiveVideoCallback() {
            @Override
            public void onGetLiveVideoSuccess(LiveVideoModel body) {
                setLiveVideos(body);
            }

            @Override
            public void onGetLiveVideoFailure() {
                textNoLiveVideo.setVisibility(View.VISIBLE);
            }
        });
    }


    /*************************************************************************************/
    /*private void forSaleList() {

        SubCatTextModel sale = new SubCatTextModel("For Sale");
//        sale.setProductName("For Sale");
        SubCatTextModel rent = new SubCatTextModel("For Rent");
        SubCatTextModel auto = new SubCatTextModel("Auto Parts");
        SubCatTextModel sale1 = new SubCatTextModel("For Sale");
        SubCatTextModel rent1 = new SubCatTextModel("For Rent");
        SubCatTextModel sale2 = new SubCatTextModel("Auto Parts");

        saletextList.add(sale);
        saletextList.add(rent);
        saletextList.add(auto);
        saletextList.add(sale1);
        saletextList.add(rent1);
        saletextList.add(sale2);



    }*/

    /*********************************************************************************************/
   /* private void liveList() {
        SubCatImageModel cars = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel job = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel tools = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel electro = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel cars1 = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel job1 = new SubCatImageModel(R.drawable.carlogo);
        SubCatImageModel tools1 = new SubCatImageModel(R.drawable.carlogo);

        liveList.add(cars1);
        liveList.add(job);
        liveList.add(tools);
        liveList.add(tools);
        liveList.add(electro);
        liveList.add(cars);
        liveList.add(cars1);
        liveList.add(job);
        liveList.add(tools);
        liveList.add(tools);
        liveList.add(electro);
        liveList.add(cars);
        liveList.add(cars1);
        liveList.add(job);
        liveList.add(tools);
        liveList.add(tools);
        liveList.add(electro);


    }*/
    private void getSubcategoryList(String cat_id, String sub_catId) {

        Log.e("cat_IDSubCatId", cat_id + " : " + sub_catId);
        WebService service = Global.WebServiceConstants.getRetrofitinstance();
//        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();


        getProductsCall = service.getProductListApi(HelperPreferences.get(getActivity()).getString(UID), cat_id, sub_catId, String.valueOf(currentPage));
        getProductsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    relNoDataFound.setVisibility(View.GONE);
                    subCatGridRecycler.setVisibility(View.VISIBLE);



                    TOTAL_PAGES = Integer.parseInt(response.body().getTotalPages());
                    isFeedsFetchInProgress = false;
                    if (currentPage > 1) {
                        productList.setStatus(response.body().getStatus());
                        productList.setMessage(response.body().getMessage());
                        resultList.addAll(response.body().getResult());
                        productList.setResult(resultList);
                        pbHome.setVisibility(View.GONE);
//                    subCategoryGridAdapter.notifyDataSetChanged();
                        subCategoryGridAdapter.notifyItemRangeInserted(productList.getResult().size() > 0 ? productList.getResult().size() - 1 : 0, productList.getResult().size());
                        txtNoMoreItem.setVisibility(View.GONE);
                        if (productList.getResult().size() > 0) {
                            getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
                        }
                    } else {
                        productList.setStatus(response.body().getStatus());
                        productList.setMessage(response.body().getMessage());
                        resultList.clear();
                        resultList.addAll(response.body().getResult());
                        productList.setResult(resultList);
                        pbHome.setVisibility(View.GONE);
                        txtNoMoreItem.setVisibility(View.GONE);
                        subCategoryGridAdapter.notifyDataSetChanged();
//                      subCategoryGridAdapter.notifyItemRangeInserted(productList.getResult().size()>0?productList.getResult().size()-1:0,productList.getResult().size());
//                        if (productList.getResult().size() > 0) {
                        getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
//                        }
                    }
                    setSearchData();
                    Log.e("GetSubproducts", "Success" + response.body().toString());
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    pbHome.setVisibility(View.GONE);
                    relNoDataFound.setVisibility(View.VISIBLE);
                    subCatGridRecycler.setVisibility(View.GONE);


                    try {
                        Log.e("GetSubproducts", "failure" + response.errorBody().string());

                        Snackbar.make(scrollRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

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
                Log.e("GetSubproducts", "failure" + t.getMessage());
                if (t.getMessage().equalsIgnoreCase("java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING at line 1 column 91 path $.result")) {
                    relNoDataFound.setVisibility(View.VISIBLE);
                    subCatGridRecycler.setVisibility(View.GONE);
                    pbHome.setVisibility(View.GONE);

                } else {
                    try {
                        Snackbar.make(scrollRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    } catch (Exception e) {
                    }
                }


            }
        });

    }

    @Override
    public void onSubCategotyClick(String subCatId) {
        Log.e("SubCat_id", subCatId);
        currentPage = PAGE_START;
        getSubcategoryList(catId, subCatId);
    }

    @Override
    public void onFilterResponse(GetProductList productList) {
        Log.e("filtered_data", "Received");
        productList.setStatus(productList.getStatus());
        productList.setMessage(productList.getMessage());
        productList.setResult(productList.getResult());
        subCategoryGridAdapter.notifyDataSetChanged();
        if (productList.getResult().size() > 0) {
            getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
        }
//        setupProducList(productList);
    }

    private void setSearchData() {
        try {
            for (int i = 0; i < productList.getResult().size(); i++) {
                searchSuggestions.add(productList.getResult().get(i).getName());
            }
        } catch (Exception e) {

        }
    }

    private void setupProducList() {
        Log.e("subCatListSize", productList.getResult().size() + "");
        int numberOfColumns = 2;
        setSearchData();
        Global.hideKeyboard(scrollRoot, getActivity());
//        searchSuggessionAdapter.notifyDataSetChanged();
        subCategoryGridRecycler.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        subCategoryGridAdapter = new SubCategoryGridAdapter(getActivity(), productList);
        subCategoryGridRecycler.setAdapter(subCategoryGridAdapter);
        Global.getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }

        showSearchBarInstent();
        htabAppbar.removeOnOffsetChangedListener(offsetListner);
        ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWacher);
        ((MainActivity) getActivity()).searchEditText.setText("");
        ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.GONE);
/*        ((MainActivity) getActivity()).searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Global.hideKeyboard(scrollRoot, getActivity());
//                    ((MainActivity) getActivity()).searchEditText.setText("");
                } else {
//                    ((MainActivity) getActivity()).searchEditText.setText("");
                    Global.hideKeyboard(scrollRoot, getActivity());
                }

                return false;
            }
        });*/
       /* ((MainActivity) getActivity()).searchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String autoCompleteText = (String) adapterView.getItemAtPosition(i);
                ((MainActivity) getActivity()).searchEditText.setText(autoCompleteText);
            }
        });*/
        ((MainActivity) getActivity()).searchEditText.setText("");
        searchSuggestions = new ArrayList<>();
      //  searchSuggessionAdapter.notifyDataSetChanged();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        count++;
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Log.e("filtered_data", "Received");
                    GetProductList getProductList = bundle.getParcelable("productList");
                    productList.setStatus(getProductList.getStatus());
                    productList.setMessage(getProductList.getMessage());
                    productList.setResult(getProductList.getResult());
                    subCategoryGridAdapter.notifyDataSetChanged();
//                    if (productList.getResult().size() > 0) {
                    getTotalHeightofGridRecyclerView(getActivity(), subCategoryGridRecycler, R.layout.main_categories_adapter, 1);
//                    }
//                    setupProducList(bundle.getParcelable("productList"));
                    catId = bundle.containsKey(SAConstants.Keys.CAT_ID) ? bundle.getString(SAConstants.Keys.CAT_ID) : "";
                    subcategoriesList.clear();
                    List<Subcategory> subcategoriesList1 = bundle.containsKey(SAConstants.Keys.SUB_CAT_LIST) ? bundle.getParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST) : new ArrayList<>();
                    subcategoriesList.addAll(subcategoriesList1);
                    sub_cat_id = bundle.containsKey(SAConstants.Keys.SUB_CAT_ID) ? bundle.getString(SAConstants.Keys.SUB_CAT_ID) : "";
                    subCategoryAdapter.notifyDataSetChanged();

                    setSearchData();
                } else {
                    Log.e("filtered_data", "Received_failed");
                }
            }
        }

    }

    public void hideSearch() {

//        new MainActivity().hideToolbar(false);


        ((MainActivity) getActivity()).rel_search.setVisibility(View.VISIBLE);
//        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).rlTitle.setVisibility(View.GONE);
        ((MainActivity) getActivity()).searchEditText.setHint("Search Sellah...");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(0);
        ((MainActivity) getActivity()).changeOptionColor(0);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra(SAConstants.Keys.CAT_ID, catId);
                intent.putExtra(SAConstants.Keys.FLTR_COUNT, count);
                startActivityForResult(intent, 111);
                count++;
            }
        });

        ((MainActivity) getActivity()).rlResetSearch.setOnClickListener(view1 -> {
            ((MainActivity) getActivity()).searchEditText.setText("");
            ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).rlResetSearch.setVisibility(View.GONE);
            ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);

        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (getProductsCall != null) {
            getProductsCall.cancel();
        }
        new ApisHelper().cancel_banner_request();
        new ApisHelper().getvideodata_cancel();

        if (getProductsCall != null)
            getProductsCall.cancel();
    }
}


