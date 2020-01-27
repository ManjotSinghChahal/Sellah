package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.BlockedUserModel.BlockListModel;
import com.app.admin.sellah.controller.utils.Global;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.BlockedUserViewHolder> {

    private BlockListModel blockedUsers;
    Context context;
    UnblockClickController controller;

    public BlockedUserAdapter(BlockListModel blockeduser, Context activity ,UnblockClickController controller) {

        this.blockedUsers = blockeduser;
        context = activity;
        this.controller=controller;

    }

    @Override
    public BlockedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blockeduser_adapter_view, parent, false);
        BlockedUserViewHolder cvh = new BlockedUserViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final BlockedUserViewHolder holder, final int position) {
//        holder.blockedUserImg.setImageResource(blockedUsers.getRecord().get(position).getImage());
        Glide.with(context)
                .load(blockedUsers.getRecord().get(position).getImage())
                .apply(Global.getGlideOptions())
                .into(holder.blockedUserImg);
        holder.blockedUserTxt.setText(blockedUsers.getRecord().get(position).getUsername());
        holder.txt_unblockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.onUnblockClick(position,blockedUsers.getRecord().get(position).getUserId());/*
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, blockedUsers.size());*/
            }
        });
    }


    @Override
    public int getItemCount() {

        return blockedUsers.getRecord().size();
    }

    public class BlockedUserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView blockedUserImg;
        TextView blockedUserTxt,txt_unblockUser;

        public BlockedUserViewHolder(View view) {
            super(view);
            blockedUserImg = view.findViewById(R.id.img_blocked_user);
            blockedUserTxt = view.findViewById(R.id.txt_blocked_user_name);
            txt_unblockUser = view.findViewById(R.id.txt_unblockUser);
        }
    }

    public interface UnblockClickController{
        void onUnblockClick(int pos, String userId);
    }
}
