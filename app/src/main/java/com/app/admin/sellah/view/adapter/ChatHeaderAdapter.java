package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.RecyclerViewClickListener;
import com.app.admin.sellah.model.extra.ChatHeadermodel.Record;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHeaderAdapter extends RecyclerView.Adapter<ChatHeaderAdapter.ChatHeaderViewHolder> {

    private List<Record> chatUsers;
    Context context;
    RecyclerViewClickListener listener;
    boolean[] isonline;

    public ChatHeaderAdapter(List<Record> chatusers, Context activity) {
        this.chatUsers = chatusers;
        context = activity;
        listener = new ChatActivity();
        isonline = new boolean[chatusers.size()];
    }

    @Override
    public ChatHeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_header, parent, false);
        ChatHeaderViewHolder chatHeaderViewHolder = new ChatHeaderViewHolder(groceryProductView);
        return chatHeaderViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatHeaderViewHolder holder, final int position) {
//        holder.userImg.setImageResource(chatUsers.get(position).ge);
        Glide.with(context).load(chatUsers.get(position).getFriendImage()).apply(Global.getGlideOptions()).into(holder.userImg);
        /*if (position == 1 || position == 4) {
            isonline[position] = true;
        }*/
//        listener.isOnline(isonline[position]);
    }


    @Override
    public int getItemCount() {
        return chatUsers.size();
    }

    public class ChatHeaderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImg;


        public ChatHeaderViewHolder(View view) {
            super(view);
            userImg = view.findViewById(R.id.img_user);


            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        /*Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_in_tv);
                        view.startAnimation(anim);
                        anim.setFillAfter(true);*/
                        Log.e("ViewFocus", "onFocusChange: yes");
                    } else {
                        Log.e("ViewFocus", "onFocusChange: yes");
                        // run scale animation and make it smaller
                       /* Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_out_tv);
                        view.startAnimation(anim);
                        anim.setFillAfter(true);*/
                    }
                }
            });
        }
    }



}
