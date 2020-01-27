package com.app.admin.sellah.controller.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.4f;
    private final float mShrinkAmount1 = 0.4f;
    private final float mShrinkDistance = 1.0f;
    private final float mShrinkDistance1 = 1.0f;

    public CenterZoomLayoutManager(Context context) {
        super(context);
    }
    onScrollCallBakk callBakk;

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout,onScrollCallBakk callBakk) {
        super(context, orientation, reverseLayout);
        this.callBakk=callBakk;
    }

    public interface onScrollCallBakk{
        void onScroll();
    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);

        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            float midpoint = getWidth() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance1 * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount1;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
                child.setAlpha(scale-0.1f);
            }
            callBakk.onScroll();
        }

    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            float midpoint = getHeight() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {


        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

            float midpoint = getWidth() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
                child.setAlpha(scale-0.15f);
            }
            callBakk.onScroll();
            return scrolled;
        } else {
            return 0;
        }

    }
}


/*
-dontwarn org.apache.http.**
        -dontwarn android.net.http.AndroidHttpClient
        -dontwarn com.google.android.gms.**
        -dontwarn com.android.volley.toolbox.**
        -dontwarn com.squareup.picasso.**
        -dontwarn okhttp3.internal.**
        -dontwarn okio.**
        -dontwarn retrofit2.**


//---------------------------------

        -dontwarn org.apache.http.**
        -dontwarn android.net.http.AndroidHttpClient
        -dontwarn com.google.android.gms.**
        -dontwarn com.android.volley.toolbox.**
        -dontwarn com.squareup.picasso.**
        -dontwarn okhttp3.internal.**
        -dontwarn okio.**
        -dontwarn retrofit2.**

//----------------------------------


        -dontwarn org.apache.http.**
        -dontwarn android.net.http.AndroidHttpClient
        -dontwarn com.google.android.gms.**
        -dontwarn com.android.volley.toolbox.**
        -dontwarn com.squareup.picasso.**
        -dontwarn okhttp3.internal.**
        -dontwarn okio.**
        -dontwarn retrofit2.**
*/
