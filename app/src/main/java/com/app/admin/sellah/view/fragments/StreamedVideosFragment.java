package com.app.admin.sellah.view.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.LiveVideoModel.LiveVideoModel;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.view.CustomViews.TouchDetectableScrollView;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.StreamedVideoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StreamedVideosFragment extends Fragment {

    @BindView(R.id.rv_other_videos)
    RecyclerView rvOtherVideos;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.pb_home)
    ProgressBar pbHome;
    @BindView(R.id.txt_no_more_item)
    TextView txtNoMoreItem;
    @BindView(R.id.touch_scrollview)
    TouchDetectableScrollView touchScrollview;
    @BindView(R.id.nolivevideo_text)
    LinearLayout nolivevideoText;
    private View view;
    private Unbinder unbinder;
    List<VideoList> videoLists = new ArrayList<>();
    private StreamedVideoAdapter streamedVideoAdapter;

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
    private String TAG = StreamedVideosFragment.class.getSimpleName();

    public StreamedVideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_streamed_videos, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        hideView();
        setUpLiveVideoOther();
        getVideoListOther();
        pagginationCode();
        /*VideoList videoList=new VideoList();*/
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpLiveVideoOther() {

        try {
            rvOtherVideos.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//            Collections.reverse(list);
            streamedVideoAdapter = new StreamedVideoAdapter(getActivity(), videoLists);
            rvOtherVideos.setAdapter(streamedVideoAdapter);
            Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 8);
        } catch (Exception e) {

        }

    }

    public void hideView() {
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).searchEditText.setHint("Search streamed Video");
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Streamed Videos");
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(3);

    }

    public void getVideoListOther() {

        new ApisHelper().getStreamedVideoData(getActivity(), String.valueOf(currentPage), new ApisHelper.GetLiveVideoCallback() {
            @Override
            public void onGetLiveVideoSuccess(LiveVideoModel msg) {
                Log.e(TAG, "onGetLiveVideoSuccess: Total_page" + msg.getTotalPages());
                if (TOTAL_PAGES == 0) {
                    TOTAL_PAGES = Integer.parseInt(msg.getTotalPages());
                }
                if (msg.getList().isEmpty()) {
                    nolivevideoText.setVisibility(View.VISIBLE);
                    rvOtherVideos.setVisibility(View.GONE);

                } else {
                    nolivevideoText.setVisibility(View.GONE);
                    rvOtherVideos.setVisibility(View.VISIBLE);
                }
//                Collections.reverse(videoLists);
                videoLists.addAll(msg.getList());
//                Collections.reverse(videoLists);
                isFeedsFetchInProgress = false;
                pbHome.setVisibility(View.GONE);
                streamedVideoAdapter.notifyItemRangeInserted(videoLists.size() > 0 ? videoLists.size() - 1 : 0, videoLists.size());
//                streamedVideoAdapter.notifyDataSetChanged();
                Global.getTotalHeightofGridRecyclerView(getActivity(), rvOtherVideos, R.layout.layout_sub_live_video_adapter, 1);
//                setUpLiveVideoOther(msg.getList());
            }

            @Override
            public void onGetLiveVideoFailure() {
                Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
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
                Log.e(TAG, "onBottomReached: total_page" + TOTAL_PAGES);
                if (TOTAL_PAGES > 1) {
                    previousPage = currentPage;
                    currentPage++;
                    isFeedsFetchInProgress = true;
                    pbHome.setVisibility(View.VISIBLE);
//                        loader = LOADER.BOTTOM;
                    if (currentPage <= TOTAL_PAGES) {
                        getVideoListOther();
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

    @Override
    public void onStop() {
        super.onStop();
        new ApisHelper().streameedvideolist_cancel();
    }
}
