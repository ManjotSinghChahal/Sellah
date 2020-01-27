package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.PromotePackages.PackagesList;
import com.app.admin.sellah.view.CustomDialogs.PromoteDialog;

import java.util.List;


public class PromoteOfferAdapter extends RecyclerView.Adapter<PromoteOfferAdapter.ViewHolder> {

    LayoutInflater mInflater;
    Context context;
    View view;
    List<PackagesList> packagesList;
    OfferCallBack callBack;
    int pos=-1;
    int nottoselect_pos;
    String sel_id="";



    public PromoteOfferAdapter(Context context, List<PackagesList> packagesList, OfferCallBack callBack) {
       mInflater=LayoutInflater.from(context);
       this.context=context;
       this.packagesList=packagesList;
       this.callBack=callBack;


    }


    @NonNull
    @Override
    public PromoteOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.layout_promote_offer_list_design, parent, false);
        return new PromoteOfferAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoteOfferAdapter.ViewHolder holder, int position) {


        Log.e( "onBindViewHolder: ",packagesList.get(position).getAmount());

        if (position>=0 && PromoteDialog.promote_selected_id.equalsIgnoreCase(""))
            PromoteDialog.promote_selected_id=packagesList.get(0).getId();

      //  if (sel_id.equalsIgnoreCase(""))



        /*for dark background */

        if (pos==-1)
        {
            if (packagesList.get(position).getAmount().equalsIgnoreCase("0"))
            {
                holder.cardView.setBackgroundResource(R.drawable.red_round_grey_filled);
                callBack.onOfferSelect(packagesList.get(holder.getAdapterPosition()).getId());
            }
            else  if (packagesList.get(position).getAmount().equalsIgnoreCase("10"))
            {
                nottoselect_pos = holder.getAdapterPosition();

                //   if (sel_id.equalsIgnoreCase(""))
                //   callBack.onOfferSelect(packagesList.get(holder.getAdapterPosition()).getId());

                //  holder.main_cd.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                holder.main_cd.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_filled_rounded_corners));
                holder.txtOffer.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.txtDuration.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.dummy_offer.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.dummy_val.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.txtOfferPrice.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }

            else
            {
                holder.cardView.setBackgroundResource(R.drawable.round_grey_filled);
            }

        }





        if (packagesList.get(position).getAmount().contains("00"))
           holder.txtOffer.setText("S$ "+"0");
        else
           holder.txtOffer.setText("S$ "+packagesList.get(position).getAmount());

        holder.txtOfferPrice.setText(packagesList.get(position).getClicks()+" clicks");
        holder.txtDuration.setText(packagesList.get(position).getValidity()+" days");


        if (pos!=-1)
        {
         if (pos==position)
         {
            if (nottoselect_pos==position)
            {
             //    holder.cardView.setBackgroundResource(R.drawable.round_grey_filled);
            }
            else
            {

                 holder.cardView.setBackgroundResource(R.drawable.red_round_grey_filled);

            }

        }
        else
        {
            if (!packagesList.get(position).getAmount().equalsIgnoreCase("10"))
             holder.cardView.setBackgroundResource(R.drawable.round_grey_filled);
        }
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = holder.getAdapterPosition();

                PromoteDialog.promote_selected_id = packagesList.get(position).getId();

                callBack.onOfferSelect(packagesList.get(position).getId());
                sel_id = packagesList.get(position).getId();



                notifyDataSetChanged();

            }
        });






       /* if (position==packagesList.size()-1){
            holder.cardView.setBackgroundResource(R.drawable.live_product_detail_red_background);
            holder.txtOffer.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.txtOfferPrice.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.txtDuration.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }*/
    }



    @Override
    public int getItemCount() {
//        if (saleList != null) {
        return packagesList.size();
//        } else {
//            return 0;
//        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cardView,main_cd,lin_promote_package;
        TextView txtOffer,txtOfferPrice,txtDuration,dummy_val,dummy_offer;
        public ViewHolder(View v) {
            super(v);
            lin_promote_package=view.findViewById(R.id.lin_promote_package);
            cardView=view.findViewById(R.id.cd_root);
            main_cd=view.findViewById(R.id.mian_cd_root);
            txtOffer=view.findViewById(R.id.txt_offer);
            txtOfferPrice=view.findViewById(R.id.txt_offer_price);
            txtDuration=view.findViewById(R.id.txt_offer_duration);
            dummy_val=view.findViewById(R.id.dummy_txtvalidity);
            dummy_offer=view.findViewById(R.id.dummy_txtoffer);
        }
    }

    public interface OfferCallBack{
        void onOfferSelect(String id);
    }

}
