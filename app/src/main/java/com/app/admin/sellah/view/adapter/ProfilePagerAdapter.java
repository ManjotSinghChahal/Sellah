package com.app.admin.sellah.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ProfilePagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}

//
//public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
//    int mNumOfTabs = 3;
//    List<String> tags;
//
//    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs, List<String> tags) {
//        super(fm);
////        this.tags=tags;
//
//        this.tags =new ArrayList<>();
//        this.tags.add("FOR SALE(4)");
//        this.tags.add("Wishlist");
//        this.tags.add("RECORDS");
//        this.mNumOfTabs = NumOfTabs;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                ProfileSalesFragment salesFragment = new ProfileSalesFragment();
//                return salesFragment;
//            case 1:
//                WishListFragment wishListFragment = new WishListFragment();
//                return wishListFragment;
//            case 2:
//                RecordFragment recordFragment = new RecordFragment();
//                return recordFragment;
//            default:
//                return null;
//        }
//    }
//
//
//    @Override
//    public int getCount() {
//        return mNumOfTabs;
//    }
//
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tags.get(position);
//    }
//
//}
//
