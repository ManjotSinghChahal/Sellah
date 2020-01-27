package com.app.admin.sellah.controller.utils;

import android.view.View;

public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, int position);
    public void isOnline(boolean isOnline);

    public void onBottomReached(boolean status);

}