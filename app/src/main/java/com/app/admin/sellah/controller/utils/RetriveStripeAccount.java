package com.app.admin.sellah.controller.utils;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Account;

import java.util.HashMap;
import java.util.Map;

public class RetriveStripeAccount extends AsyncTask<String, Void, Account> {

    private Exception exception;
    String accountId = "";

    Dialog dialog;

    public Dialog getDialog() {
//            dialog.show();
        return dialog;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    public void showDialog() {
        this.dialog.show();
    }

    public Account doInBackground(String... urls) {
        try {
            Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

            Account account = null;
            try {
                account = Account.retrieve(accountId, null);
                Log.e("Stripe_account", "onStripeConnectClicked: " + account.getId());
            } catch (AuthenticationException e) {
                e.printStackTrace();
                Log.e("Stripe_account", "onStripeConnectClicked: " + e.getMessage());
            } catch (InvalidRequestException e) {
                e.printStackTrace();
                Log.e("Stripe_account", "onStripeConnectClicked: " + e.getMessage());
            } catch (APIConnectionException e) {
                Log.e("Stripe_account", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (CardException e) {
                Log.e("Stripe_account", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIException e) {
                Log.e("Stripe_account", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            }
            return account;
        } catch (Exception e) {
            this.exception = e;

            return null;
        }
    }

    protected void onPostExecute(Account feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (feed != null) {
            Log.e("Stripe_account", "onPostExecute: " + feed.getId());
        } else {
            Log.e("Stripe_account", "onPostExecute: unable to fetch");
        }
    }


}
