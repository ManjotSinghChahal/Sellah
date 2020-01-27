package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.fragments.AddCreditCardDetailFragment;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardDetailViewHolder> {

    Context context;
    List<Card> cardList;
    OnCardOptionSelection callBack;

    public CardListAdapter(Context activity, List<Card> cardList, OnCardOptionSelection callBack) {
        context = activity;
        this.cardList = cardList;
        this.callBack = callBack;
    }

    @Override
    public CardDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_adapter_layout, parent, false);
        CardDetailViewHolder cvh = new CardDetailViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CardDetailViewHolder holder, final int position) {
        holder.txtCardNumber.setText(cardList.get(position).getLast4());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCardSelectionClick(position, cardList.get(position).getId());
            }
        });
        holder.txtEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, AddCreditCardDetailFragment.class);
                intent.putExtra(SAConstants.Keys.CARDHOLDER_NAME, cardList.get(position).getName());
                intent.putExtra(SAConstants.Keys.CARD_NUMBER, cardList.get(position).getLast4());
                intent.putExtra(SAConstants.Keys.CARD_EXP_YEAR, cardList.get(position).getExpYear().toString());
                intent.putExtra(SAConstants.Keys.CARD_EXP_MONTH, cardList.get(position).getExpMonth().toString());
                ((Activity) context).startActivityForResult(intent, 2);*/

                S_Dialogs.getRemoveCardDialog(context,((dialog, which) -> {
                    removeCardApi(position,cardList.get(position).getId());
                })).show();
            }
        });
        CreditCardView cardView = new CreditCardView(context);
        switch (cardList.get(position).getBrand()) {
            case "American Express":
                holder.cardImage.setImageResource(R.drawable.american_express_card_icon);
                break;
            case "Diners Club":
                holder.cardImage.setImageResource(R.drawable.diners_club_icon);
                break;
            case "Discover":
                holder.cardImage.setImageResource(R.drawable.discover_card_icon);
                break;
            case "JCB":
                holder.cardImage.setImageResource(R.drawable.jcb_card_icon);
                break;
            case "MasterCard":
                holder.cardImage.setImageResource(R.drawable.mastercard_icon);
                break;
            case "UnionPay":
                holder.cardImage.setImageResource(R.drawable.china_union_pay_icon);
                break;
            case "Visa":
                holder.cardImage.setImageResource(R.drawable.visa_icon);
                break;
            case "Unknown":
                holder.cardImage.setImageResource(R.drawable.credit_card_default);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtCardNumber, txtEditCard;
        ImageView cardImage;

        public CardDetailViewHolder(View view) {
            super(view);
            txtCardNumber = view.findViewById(R.id.txt_card_number);
            txtEditCard = view.findViewById(R.id.edt_card_edit);
            cardImage = view.findViewById(R.id.bankImg);
        }
    }

    public interface OnCardOptionSelection {
        void onCardSelectionClick(int pos, String cardId);
        void onCardRemoveListner(int pos,int updatedSize);

    }

    private void removeCardApi(int pos,String cardId){
        Dialog dialog= S_Dialogs.getLoadingDialog(context);
        dialog.show();

        Call<Common> removeCardCall = Global.WebServiceConstants.getRetrofitinstance().removeCardApi(HelperPreferences.get(context).getString(UID),cardId);
        removeCardCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                if(response.isSuccessful()){
                    if(response.body().getStatus().equalsIgnoreCase("1")){
                        cardList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, cardList.size());
                        callBack.onCardRemoveListner(pos,cardList.size());
                    }
                }else{
                    Toast.makeText(context, "Unable to remove card at the movement", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("removeCardFaid", "onResponse: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(context, "Try again later.", Toast.LENGTH_SHORT).show();
                Log.e("removeCardApi", "onFailure: "+t.getMessage());
            }
        });
    }
}