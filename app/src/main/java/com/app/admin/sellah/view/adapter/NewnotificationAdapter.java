package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.Categories.Result;
import com.app.admin.sellah.model.extra.NotificationList.ArrFollow;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.PersonalTestimonialFragment;
import com.app.admin.sellah.view.fragments.ProductFrgament;
import com.app.admin.sellah.view.fragments.SubCategoryFragment;
import com.app.admin.sellah.view.fragments.ViewFollowListFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;
import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class NewnotificationAdapter extends RecyclerView.Adapter<NewnotificationAdapter.AllCategoryViewHolder> {

    List<ArrFollow>  newMessList;
    Context context;
    NotificationListModel notificationListModel;
    OnNotificationModelDissmiss callback;



    public NewnotificationAdapter(List<ArrFollow> results, NotificationListModel notificationListModel, Context activity,OnNotificationModelDissmiss callback) {

        this.newMessList = results;
        context = activity;
        this.notificationListModel = notificationListModel;
        this.callback = callback;


    }

    @Override
    public AllCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapterview_new, parent, false);
        AllCategoryViewHolder cvh = new AllCategoryViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final AllCategoryViewHolder holder, final int position) {

        holder.txtName.setText(newMessList.get(position).getMessage());
      //  holder.time.setText(newMessList.get(position).getMsgArr().getDatetime());
        if (newMessList.get(position).getReadStatus().equals("0"))
        {
            holder.read.setText("Mark as Read");
            holder.notification_rootlayout.setBackgroundColor(Color.parseColor("#fffaec"));
        }
        else
        {
            holder.read.setVisibility(View.GONE);
            holder.notification_rootlayout.setBackgroundColor(Color.parseColor("#ffffff"));

        }

        holder.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApisHelper().readNotificationApi(context, newMessList.get(position).getNotiId(),"one", new ApisHelper.ReadNotificationCallback() {
                    @Override
                    public void onReadNotificationSuccess(String msg) {
                        notificationListModel.getArrFollow().get(position).setReadStatus("1");
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onReadNotificationFailure() {

                    }
                });

            }
        });

        holder.notification_rootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (notificationListModel.getArrFollow().size()>0)
                {
                    String product_id = notificationListModel.getArrFollow().get(position).getProductId();

                    if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("product_added"))
                    {

                        callback.onNotificationDismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("FEATUREDPOSTS",product_id);
                        ProductFrgament fragment = new ProductFrgament();
                        fragment.setArguments(bundle);
                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();


                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("accept_decline_offer"))
                    {

                        callback.onNotificationDismiss();
                        Intent resultIntent;
                        resultIntent = new Intent(context, ChatActivity.class);
                        resultIntent.putExtra("otherUserId", notificationListModel.getArrFollow().get(position).getUserId());
                        resultIntent.putExtra("otherUserImage",notificationListModel.getArrFollow().get(position).getImage());
                        resultIntent.putExtra("otherUserName",notificationListModel.getArrFollow().get(position).getUsername());
                        context.startActivity(resultIntent);
                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("follow"))
                    {
                        callback.onNotificationDismiss();
                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ViewFollowListFragment(HelperPreferences.get(context).getString(UID))).addToBackStack(PROFILETAG).commit();
                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("refund_amount"))
                    {
                        callback.onNotificationDismiss();
                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("payment"))
                    {
                        callback.onNotificationDismiss();
                        Intent resultIntent;
                        resultIntent = new Intent(context, ChatActivity.class);
                        resultIntent.putExtra("otherUserId", notificationListModel.getArrFollow().get(position).getUserId());
                        resultIntent.putExtra("otherUserImage",notificationListModel.getArrFollow().get(position).getImage());
                        resultIntent.putExtra("otherUserName",notificationListModel.getArrFollow().get(position).getUsername());
                        context.startActivity(resultIntent);
                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("make_offer"))
                    {
                        Intent resultIntent;
                        resultIntent = new Intent(context, ChatActivity.class);
                        resultIntent.putExtra("otherUserId", notificationListModel.getArrFollow().get(position).getUserId());
                        resultIntent.putExtra("otherUserImage",notificationListModel.getArrFollow().get(position).getImage());
                        resultIntent.putExtra("otherUserName",notificationListModel.getArrFollow().get(position).getUsername());
                        context.startActivity(resultIntent);
                        callback.onNotificationDismiss();
                    }
                    else if (notificationListModel.getArrFollow().get(position).getNotiType().equalsIgnoreCase("testimonial_added"))
                    {
                        callback.onNotificationDismiss();
                        hideSearch();
                        PersonalTestimonialFragment personalTestimonialFragment = new PersonalTestimonialFragment(notificationListModel.getArrFollow().get(position).getUserId());
                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, personalTestimonialFragment).addToBackStack(null).commit();


                    }

                }



            }
        });
    }

    @Override
    public int getItemCount() {

        if (newMessList!=null)
        return newMessList.size();
        else return 0;
    }

    public class AllCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,read,time;
        RelativeLayout notification_rootlayout;

        public AllCategoryViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txt_name_noti);
            read = view.findViewById(R.id.mark_asread_noti);
            time= view.findViewById(R.id.time_noti);
            notification_rootlayout= view.findViewById(R.id.notification_root_layout);
        }
    }

    public interface OnItemClickedListner{
        void onItemClicked();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void reloadItems(List<ArrFollow> items) {
        this.newMessList = items;
        notifyDataSetChanged();
    }


    public interface OnNotificationModelDissmiss
    {
       void   onNotificationDismiss();



    }
    public void hideSearch() {

        ((MainActivity) context).rlMenu.setVisibility(View.GONE);
        ((MainActivity) context).rel_search.setVisibility(View.GONE);
        ((MainActivity) context).rlFilter.setVisibility(View.GONE);
       /* ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);*/
        ((MainActivity) context).text_sell.setText("Sell");
        ((MainActivity)context).rloptions.setVisibility(View.GONE);
        ((MainActivity) context).rlBack.setVisibility(View.GONE);

       // ((MainActivity) getActivity()).changeOptionColor(2);
    }




}