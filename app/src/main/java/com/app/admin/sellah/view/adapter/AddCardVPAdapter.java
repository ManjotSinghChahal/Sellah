package com.app.admin.sellah.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.sql.Array;
import java.util.ArrayList;

public class AddCardVPAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    public AddCardVPAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int pos) {

        switch (pos) {
            case 0:
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            case 3:
                return fragments.get(3);
            default:
                return fragments.get(0);
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
