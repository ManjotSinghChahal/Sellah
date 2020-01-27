package com.app.admin.sellah.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class NonSwipableViewPaggerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public NonSwipableViewPaggerAdapter(FragmentManager manager) {
        super(manager);
        this.mFragmentList=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragmentList.get(position);
    }

    @Override
    public int getCount() {
//      return this.mFragmentList.size();
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        this.mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "test";
    }

}

