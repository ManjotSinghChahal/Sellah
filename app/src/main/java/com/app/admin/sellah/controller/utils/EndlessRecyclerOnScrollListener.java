package com.app.admin.sellah.controller.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = "EndlessScrollListener";

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPage = 1;
    GridLayoutManager mLinearLayoutManager;

    RecyclerViewPositionHelper mRecyclerViewHelper;
    public EndlessRecyclerOnScrollListener(GridLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mRecyclerViewHelper.getItemCount();
        firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();


        if(visibleItemCount%10==0)
        {
            Log.e("vvvvvv----------","0000000000");
            onLoadMore(currentPage,totalItemCount);
        }
        else
        {
            Log.e("vvvvvv----------","111111111");
        }
        Log.e("dddd---------->>>>>",totalItemCount+"");
        Log.e("dddd----------",visibleItemCount+"");



      /*  if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            currentPage++;

            onLoadMore(currentPage,totalItemCount);

            loading = true;
        }*/
    }

    //Start loading
    public abstract void onLoadMore(int currentPage,int totalItemCount);
}
