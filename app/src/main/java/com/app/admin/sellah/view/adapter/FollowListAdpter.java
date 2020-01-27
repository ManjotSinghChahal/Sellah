package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.FolowModelNew.FollowModelNew;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.fragments.PersonalProfileFragment;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;

public class FollowListAdpter extends RecyclerView.Adapter<FollowListAdpter.FollowListViewHolder> {

    private FollowModelNew followerModels;
    Context context;
    String title;
    UnlickClickController controller;
    boolean isOwner;

    public FollowListAdpter(FollowModelNew followerModels, Context activity, String title,boolean isOwner,UnlickClickController controller) {

        this.followerModels = followerModels;
        context = activity;
        this.title = title;
        this.controller=controller;
        this.isOwner=isOwner;

    }

    @Override
    public FollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_follow_list, parent, false);
        FollowListViewHolder cvh = new FollowListViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final FollowListViewHolder holder, final int position) {

        if (title.equalsIgnoreCase("Followers")) {
            Glide.with(context)
                    .load(followerModels.getFollower().get(position).getImage())
                    .apply(Global.getGlideOptions())
                    .into(holder.followUserImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SAConstants.Keys.OTHER_USER_ID, followerModels.getFollower().get(position).getUserId());
                    PersonalProfileFragment fragment=new PersonalProfileFragment();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();

                }
            });
//            holder.followUserImage.setImageResource(followerModels.getFollower().get(position).getUsername());
            holder.followUserName.setText(followerModels.getFollower().get(position).getUsername());

            holder.unLike.setVisibility(View.GONE);

        } else {
            Glide.with(context)
                    .load(followerModels.getFollowing().get(position).getImage())
                    .apply(Global.getGlideOptions())
                    .into(holder.followUserImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SAConstants.Keys.OTHER_USER_ID, followerModels.getFollowing().get(position).getUserId());
                    PersonalProfileFragment fragment=new PersonalProfileFragment();
                    fragment.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();

                }
            });

//            holder.followUserImage.setImageResource(followerModels.getFollower().get(position).getUsername());
            holder.followUserName.setText(followerModels.getFollowing().get(position).getUsername());

            if(isOwner){
                holder.unLike.setVisibility(View.VISIBLE);
            }else{
                holder.unLike.setVisibility(View.GONE);
            }


            holder.unLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    S_Dialogs.getUnfollowDialog(context,((dialog, which) -> {
                        controller.onUnlikeClick(followerModels.getFollowing().get(position).getUserId(),position);
                    })).show();
                    /*followerModels.getFollower().remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, followerModels.getFollower().size());*/
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if (title.equalsIgnoreCase("Followers") ){
            return followerModels.getFollower().size();
        }else{
            return followerModels.getFollowing().size();
        }
    }

    public class FollowListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView followUserImage;
        TextView followUserName, unLike;

        public FollowListViewHolder(View view) {
            super(view);
            followUserImage = view.findViewById(R.id.img_follow_user);
            followUserName = view.findViewById(R.id.txt_follow_user_name);
            unLike = view.findViewById(R.id.txt_unlikeUser);
        }
    }


    public interface UnlickClickController{
        void onUnlikeClick(String userId,int pos);
    }
}
