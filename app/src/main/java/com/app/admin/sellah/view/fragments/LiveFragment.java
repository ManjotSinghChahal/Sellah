package com.app.admin.sellah.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;

public class LiveFragment extends Fragment  {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_videos_chat, container, false);

            Intent intent = new Intent(new Intent(getActivity(),MainActivityLiveStream.class));
            intent.putExtra("value", "noLiveStream");
            getActivity().startActivity(intent);
            getActivity().finish();


        }
        return view;
    }


}