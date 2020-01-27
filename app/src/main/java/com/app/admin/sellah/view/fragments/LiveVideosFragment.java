package com.app.admin.sellah.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SubCategoryController;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.model.extra.LiveVideoModel.LiveVideoModel;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomViews.TouchDetectableScrollView;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.app.admin.sellah.view.adapter.Live_featureViewPagerAdapter;
import com.app.admin.sellah.view.adapter.SubCategoryLive1Adapter;
import com.app.admin.sellah.view.adapter.VideoCategoriesAdptTwo;
import com.app.admin.sellah.view.adapter.VideoSubcategoriesAdpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LiveVideosFragment extends Fragment implements SubCategoryController {

    Unbinder unbinder;
    View view;
    @BindView(R.id.rv_other_videos)
    RecyclerView rvOtherVideos;
    List<VideoList> list;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.pb_home)
    ProgressBar pbHome;
    @BindView(R.id.txt_no_more_item)
    TextView txtNoMoreItem;
    @BindView(R.id.touch_scrollview)
    TouchDetectableScrollView touchScrollview;
    @BindView(R.id.viewPager_live)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.recycler_spin_cat)
    RecyclerView spinCategory;
    @BindView(R.id.ll_no_product)
    LinearLayout llNoProduct;
    @BindView(R.id.ll_no_network)
    LinearLayout llNoNetwork;

    private VideoCategoriesAdptTwo videoCategoriesAdptTwo;
    List<VideoList> videoLists = new ArrayList<>();
    private VideoSubcategoriesAdpt videoCategoriesAdptNew;
    SubCategoryLive1Adapter subCategoryAdapter;
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
    private String TAG = "LiveVideoFragment";
    private int dotscount;
    private ImageView[] dots;
    private Timer timer;
    int timerDelay = 2000;
    String categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        hideSearch();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_videos, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        //  getVideoList();
        setUpLiveVideoOther();


        if (Global.NetworStatus.isOnline(getActivity())) {
            getVideoListOther("");
        } else {
            rvOtherVideos.setVisibility(View.GONE);
            llNoNetwork.setVisibility(View.VISIBLE);
            llNoProduct.setVisibility(View.GONE);
        }

        pagginationCode();

        setupBannerAdds();
        setUpFilter();
        list = new ArrayList<>();
        //  LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        /*LinearLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        rvOtherVideos.setLayoutManager(layoutManager1);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
        arrayList.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

        VideoSubcategoriesAdpt videoCategoriesAdpt1 = new VideoSubcategoriesAdpt(getActivity(), arrayList);
        rvOtherVideos.setAdapter(videoCategoriesAdpt1);
        Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 0);
*/
        setUpSwipeView();
        return view;
    }

    private void setUpFilter() {
        GetCategoriesModel model = ExpandableListData.getData();
        List<String> categoryList = new ArrayList<>();
        categoryList.add("All Videos");
        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                categoryList.add(model.getResult().get(i).getName());

            }
        } catch (Exception e) {

        }
//      *************************************Category recuclerview adapter*******************************
        setUpAdapter(categoryList);

/*
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
               */
/* if(position>0){
                    filterVideoListApi(ExpandableListData.getCatId(spinCategory.getSelectedItem().toString().trim()));
                }else{
                    filterVideoListApi("");
                }*//*

                videoLists.clear();
                videoCategoriesAdptNew.notifyDataSetChanged();
                currentPage = 1;
                getVideoListOther();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
*/
    }

    private void filterVideoListApi(String catId) {

        new ApisHelper().getCategoryLiveVideo(getActivity(), catId, new ApisHelper.SortLiveVideo() {
            @Override
            public void onSortLiveVideoSuccess(LiveVideoModel videoList) {

                videoLists.clear();
                videoCategoriesAdptNew.notifyDataSetChanged();
                videoLists.addAll(videoList.getList());

              /*  list.clear();
                list.addAll(msg.getList());
                Log.e("csdvcsd", list.size() + "");*/
                pbHome.setVisibility(View.GONE);
//                Collections.reverse(videoLists);
                isFeedsFetchInProgress = false;
                videoCategoriesAdptNew.notifyItemRangeInserted(videoLists.size() > 0 ? videoLists.size() - 1 : 0, videoLists.size());
//                videoCategoriesAdptNew.notifyDataSetChanged();
                Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 1);

            }

            @Override
            public void onSortLiveVideoFailure() {

            }
        });
    }

    private void setupBannerAdds() {
        List<String> bannerAddList = new ArrayList<>();
        bannerAddList.add("https://s3.ap-southeast-1.amazonaws.com/sellahmedia/livevideobanners/19012019084208_5c438ba0b0705.jpg");
        bannerAddList.add("https://s3.ap-southeast-1.amazonaws.com/sellahmedia/livevideobanners/19012019084208_5c438ba0b0705.jpg");

        bannerAddList.add("https://s3.ap-southeast-1.amazonaws.com/sellahmedia/livevideobanners/19012019084208_5c438ba0b0705.jpg");

        if (bannerAddList != null) {


            try {
                Live_featureViewPagerAdapter viewPagerAdapter = new Live_featureViewPagerAdapter(getActivity(), bannerAddList);
                viewPager.setAdapter(viewPagerAdapter);
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];
                //automatic slide
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (viewPager != null) {
                            viewPager.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (viewPager != null) {
                                        if (viewPager.getCurrentItem() == viewPager.getChildCount()) {
                                            viewPager.setCurrentItem(0);
//                            Log.e("pagger","pos"+vPBannerAdd.getCurrentItem());
                                        } else {
                                            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1));
//                            Log.e("pagger_else","pos"+vPBannerAdd.getCurrentItem());
                                        }
                                    }
                                }
                            });
                        }
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, timerDelay, timerDelay);


                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageResource(R.drawable.dot_icon);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,
                            15);
                    params.setMargins(5, 0, 8, 0);
                    SliderDots.addView(dots[i], params);
                }

//        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot_icon2));

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            } catch (Exception e) {

            }
        }
    }

    private void setUpSwipeView() {

//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                swipeContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Reload current fragment
                        Fragment frg = null;
                        frg = new LiveVideosFragment();
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
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


    private void setUpLiveVideoOther() {

        try {

            rvOtherVideos.setLayoutManager(new LinearLayoutManager(getActivity()));
//          Collections.reverse(videoLists);
            videoCategoriesAdptNew = new VideoSubcategoriesAdpt(getActivity(), videoLists);
            rvOtherVideos.setAdapter(videoCategoriesAdptNew);
            Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 0);

        } catch (Exception e) {

        }

    }

    public void getVideoList() {

       /* new ApisHelper().getLiveVideoData(getActivity(), new ApisHelper.GetLiveVideoCallback() {
            @Override
            public void onGetLiveVideoSuccess(LiveVideoModel msg) {
                list.clear();
                list.addAll(msg.getList());
                videoCategoriesAdpt.notifyDataSetChanged();
            }

            @Override
            public void onGetLiveVideoFailure() {

            }
        });*/
    }

    public void getVideoListOther(String cat) {

        new ApisHelper().getLiveVideoData(getActivity(), String.valueOf(currentPage), ExpandableListData.getCatId(cat), new ApisHelper.GetLiveVideoCallback() {
            @Override
            public void onGetLiveVideoSuccess(LiveVideoModel msg) {
                if (TOTAL_PAGES == 0) {
                    TOTAL_PAGES = Integer.parseInt(msg.getTotalPages());
                }
                Log.e("getPagesSize", "onGetLiveVideoSuccess: " + msg.getTotalPages());
//                TOTAL_PAGES = Integer.parseInt(msg.getTotalPages());
                videoLists.addAll(msg.getList());

                Log.e("getListSize", videoLists.size() + "");
                if (pbHome != null) {
                    pbHome.setVisibility(View.GONE);
                }

                //---------Deep linking---------------------------------------
                if (Global.DEEP_LINKING_STATUS.equalsIgnoreCase("enable")) {

                    for (int i = 0; i < videoLists.size(); i++) {
                        if (videoLists.get(i).getGroupId().equalsIgnoreCase(Global.DEEP_LINKING_PRODUCT_ID)) {
                            Intent intent = new Intent(new Intent(getActivity(), MainActivityLiveStream.class));
                            intent.putExtra("value", "noLiveStream");
                            intent.putExtra("id", videoLists.get(i).getVideoId());
                            intent.putExtra("group_id", videoLists.get(i).getGroupId());
                            intent.putExtra("product_id", videoLists.get(i).getProductId());
                            intent.putExtra("product_name", videoLists.get(i).getProductName());
                            intent.putExtra("seller_id", videoLists.get(i).getSellerId());
                            intent.putExtra("start_time", videoLists.get(i).getStartTime());
                            intent.putExtra("views", videoLists.get(i).getViews());
                            getActivity().startActivity(intent);
                        }
                    }
                }


                //-----------------------------------------------------------


                if (videoLists.size() == 0) {
                    if (llNoProduct != null) llNoProduct.setVisibility(View.VISIBLE);
                    if (rvOtherVideos != null) rvOtherVideos.setVisibility(View.GONE);
                    if (llNoNetwork != null) llNoNetwork.setVisibility(View.GONE);
                } else {
                    if (llNoNetwork != null) llNoNetwork.setVisibility(View.GONE);
                    if (llNoProduct != null) llNoProduct.setVisibility(View.GONE);
                    if (rvOtherVideos != null) rvOtherVideos.setVisibility(View.VISIBLE);

                }


//                Collections.reverse(videoLists);
                isFeedsFetchInProgress = false;
                videoCategoriesAdptNew.notifyItemRangeInserted(videoLists.size() > 0 ? videoLists.size() - 1 : 0, videoLists.size());
//                videoCategoriesAdptNew.notifyDataSetChanged();

                try {
                    Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 1);
                } catch (Exception e) {
                }
            }

            @Override
            public void onGetLiveVideoFailure() {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        new ApisHelper().getvideodata_cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void pagginationCode() {

        touchScrollview.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
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
                    currentPage++;
                    isFeedsFetchInProgress = true;
                    pbHome.setVisibility(View.GONE);
//                        loader = LOADER.BOTTOM;
                    if ((currentPage) <= TOTAL_PAGES) {


                        // vishal

                        getVideoListOther(categories);

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

    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Live Videos");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(1);
    }

    private void setUpAdapter(List<String> list) {

        //sub Category recycler set Adapter
//        forSaleList();
        if (list != null) {
            subCategoryAdapter = new SubCategoryLive1Adapter(list, getActivity(), this);

            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            spinCategory.setLayoutManager(horizontalLayoutManager1);
            spinCategory.setAdapter(subCategoryAdapter);
        }
    }

    @Override
    public void onSubCategotyClick(String subCatId) {


        categories = subCatId;
        videoLists.clear();
        videoCategoriesAdptNew.notifyDataSetChanged();
        currentPage = 1;

        if (Global.NetworStatus.isOnline(getActivity())) {
            getVideoListOther(subCatId);
        } else {
            rvOtherVideos.setVisibility(View.GONE);
            llNoNetwork.setVisibility(View.VISIBLE);
            llNoProduct.setVisibility(View.GONE);
        }


    }

    @Override
    public void onFilterResponse(GetProductList productList) {

    }


}
