package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.TestimonialModel.TestimonialModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.activities.ImageViewerActivity;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.ViewHolder>  {
    TestimonialModel items;
    Context context;

    public TestimonialAdapter(Context con, TestimonialModel items) {
        this.items = items;
        context=con;
    }

    @NonNull
    @Override
    public TestimonialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.testimonial_adapter, parent, false);
        return new TestimonialAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull TestimonialAdapter.ViewHolder holder, int position) {
        holder.profile.setText("@ "+items.getResult().get(position).getUsername());
        Glide.with(context)
                .load(items.getResult().get(position).getImage())
                .apply(Global.getGlideOptions())
                .into(holder.profileImage);
        holder.ptime.setText(Global.getTimeAgo(Global.convertUTCToLocal(items.getResult().get(position).getCreatedAt())));
        holder.desc.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(200) });;
        holder.desc.setText(items.getResult().get(position).getFeedback());
        holder.ratingBar.setRating(Float.parseFloat(items.getResult().get(position).getRating()));
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageViewerActivity.start(context,items.getResult().get(position).getImage(),0);
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.getResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView profile,ptime,desc;
        public RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            ratingBar = itemView.findViewById(R.id.rtbProductRating);
            profile = (TextView) itemView.findViewById(R.id.profile_name);
            ptime = (TextView) itemView.findViewById(R.id.posted_time);
            desc = (TextView) itemView.findViewById(R.id.comment_desc);
        }
    }
}

