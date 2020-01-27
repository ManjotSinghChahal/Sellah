package com.app.admin.sellah.controller.utils;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Account;

import java.util.HashMap;
import java.util.Map;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.USER_EMAIL;

public class CreateStripeAccount extends AsyncTask<String, Void, Account> {

    private Exception exception;
    CreateAccountController createAccountController;
    Context context;

    public CreateStripeAccount(Context context,CreateAccountController controller) {
        createAccountController = controller;
        this.context=context;
    }

    Dialog dialog;

    public Dialog getDialog() {
//            dialog.show();
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    public void showDialog() {
        this.dialog.show();
    }

    public Account doInBackground(String... keys) {
        try {
            Stripe.apiKey = keys[0] ;

            Map<String, Object> accountParams = new HashMap<String, Object>();
            accountParams.put("type", "standard");
//            accountParams.put("managed", false);
            accountParams.put("country", "US");
            accountParams.put("email",HelperPreferences.get(context).getString(USER_EMAIL));
            Account create = null;
            try {
                create = Account.create(accountParams, null);
                Log.e("StripeConnect", "onStripeConnectClicked: " + create.getCountry());
            } catch (AuthenticationException e) {
                e.printStackTrace();
                Log.e("StripeConnect", "onStripeConnectClicked: " + e.getMessage());
            } catch (InvalidRequestException e) {
                e.printStackTrace();
                Log.e("StripeConnect", "onStripeConnectClicked: " + e.getMessage());
            } catch (APIConnectionException e) {
                Log.e("StripeConnect", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (CardException e) {
                Log.e("StripeConnect", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIException e) {
                Log.e("StripeConnect", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            }
            return create;
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
            createAccountController.onAccountCreationSuccess(feed);
            Log.e("StripeConnect", "onPostExecute: " + feed.getId());
        } else {
            createAccountController.onAccountCreationFailure();
            Log.e("StripeConnect", "onPostExecute: unable to register");
        }
    }

    public interface CreateAccountController {
        void onAccountCreationSuccess(Account account);
        void onAccountCreationFailure();
    }
}