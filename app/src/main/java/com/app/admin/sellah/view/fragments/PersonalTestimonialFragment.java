package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.TestiModelClass;
import com.app.admin.sellah.model.extra.TestimonialModel.TestimonialModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.adapter.TestimonialAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalTestimonialFragment extends Fragment {

    RecyclerView testiRecycler;
    ArrayList<TestiModelClass> testiData = new ArrayList<>();
    TestimonialAdapter testimonialAdapter;
    String userId;

    public PersonalTestimonialFragment(){

    }
    @SuppressLint("ValidFragment")
    public PersonalTestimonialFragment(String id) {
        userId=id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_testimonial_fragment, container, false);

        testiRecycler=view.findViewById(R.id.testimonial_recycler);

        getUserTestimonialList(userId);
        prepareData();

        return view;
    }

    private void setTestimonialData(TestimonialModel data){
        testimonialAdapter = new TestimonialAdapter(getActivity(), data);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        testiRecycler.setLayoutManager(mLayoutManager);
        testiRecycler.setAdapter(testimonialAdapter);
    }



    public void prepareData() {
        TestiModelClass testiModelClass = new TestiModelClass(R.drawable.profile_icon_img,R.drawable.star_icon, "@Dave", "Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience. The delivery time is phenomenal.","8 minutes ago" );
        testiData.add(testiModelClass);
        testiModelClass = new TestiModelClass(R.drawable.profile_icon_img,R.drawable.star_icon, "@Radha", "Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience. The delivery time is phenomenal.Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience.\" ", "11 minutes ago");
        testiData.add(testiModelClass);
        testiModelClass = new TestiModelClass(R.drawable.profile_icon_img,R.drawable.star_icon, "@Ev", "Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience. The delivery time is phenomenal.", " 34 minutes ago");
        testiData.add(testiModelClass);
        testiModelClass = new TestiModelClass(R.drawable.profile_icon_img,R.drawable.star_icon, "Biz Stone", "Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience. The delivery time is phenomenal.","37 minutes ago" );
        testiData.add(testiModelClass);
        testiModelClass = new TestiModelClass(R.drawable.profile_icon_img,R.drawable.star_icon, "@Dave", "Excellent. I have been wearing custom shirts for over 40-years and Proper Cloth has done an great job on all facets of the experience. The delivery time is phenomenal.", "48 minutes ago");
        testiData.add(testiModelClass);
    }

    private void getUserTestimonialList(String userId){
        Call<TestimonialModel> testimonialCall=Global.WebServiceConstants.getRetrofitinstance().getUserTestimonialsApi(userId);
        testimonialCall.enqueue(new Callback<TestimonialModel>() {
            @Override
            public void onResponse(Call<TestimonialModel> call, Response<TestimonialModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equalsIgnoreCase("1")){
                      setTestimonialData(response.body());
                    }
                }else{

                }
            }
            @Override
            public void onFailure(Call<TestimonialModel> call, Throwable t) {

            }
        });
    }

}
