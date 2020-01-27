package com.app.admin.sellah.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.admin.sellah.model.extra.ProfileModel.ProfileModel;
import com.app.admin.sellah.view.fragments.PersonalProfileAboutFragment;
import com.app.admin.sellah.view.fragments.PersonalProfileSalesFragment;
import com.app.admin.sellah.view.fragments.PersonalTestimonialFragment;

import java.util.ArrayList;
import java.util.List;

public class PersonalProfilePagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs = 3;
    List<String> tags;
    ProfileModel profileData;

    public PersonalProfilePagerAdapter(FragmentManager fm, int NumOfTabs, List<String> tags, ProfileModel profileModel) {
        super(fm);
        this.tags=tags;

        this.tags =new ArrayList<>();
        this.tags.add("FOR SALE(0)");
        this.tags.add("TESTIMONIALS");
        this.tags.add("ABOUT");
        this.mNumOfTabs = NumOfTabs;
        this.profileData=profileModel;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PersonalProfileSalesFragment salesFragment = new PersonalProfileSalesFragment(profileData.getResult().getId());
                return salesFragment;
            case 1:
                PersonalTestimonialFragment testimonialFragment = new PersonalTestimonialFragment(profileData.getResult().getId());
                return testimonialFragment;
            case 2:
                PersonalProfileAboutFragment aboutFragment = new PersonalProfileAboutFragment(profileData);
                return aboutFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tags.get(position);
    }

}



