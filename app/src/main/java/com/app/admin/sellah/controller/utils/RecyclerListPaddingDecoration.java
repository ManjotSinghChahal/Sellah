package com.app.admin.sellah.controller.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerListPaddingDecoration extends RecyclerView.ItemDecoration {
    private final int padding;

    public RecyclerListPaddingDecoration(int padding) {
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, padding,0);
        }else if(position == 0){
            outRect.set(padding, 0, 0,0);
        }
    }
}
