package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.activities.LoginActivity;
import com.bumptech.glide.Glide;

import static com.app.admin.sellah.controller.utils.Global.logout;

public class S_Dialogs {

    public S_Dialogs() {

    }

    public static MaterialDialog getMakeOfferDialog(Context context, String price, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title("Make Offer")
                .content(context.getResources().getString(R.string.dialog_content_makeOffer) + price)
                .positiveText("Make Offer")
                .negativeText("Cancel")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .onNegative(((dialog, which) -> dialog.dismiss()))
                .input(R.string.makeOffer, 0,false, inputCallback)
                .autoDismiss(false)
                .build();
    }
    public static MaterialDialog getSendOfferDialog(Context context,String pName, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title("Send Offer")
                .content(context.getResources().getString(R.string.dialog_content_sendOffer)+" for "+pName)
                .positiveText("Send Offer")
                .negativeText("Cancel")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .onNegative(((dialog, which) -> dialog.dismiss()))
                .input(R.string.makeOffer, 0,false, inputCallback)
                .autoDismiss(false)
                .build();

    }

    public static MaterialDialog getReportCommentDialog(Context context, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title("Report comment")
                .content("Why you are reporting this comment?")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("Submit")
                .negativeText("Cancel")
                .onNegative(((dialog, which) -> dialog.dismiss()))
                .input(R.string.report_reason, 0, inputCallback)
                .autoDismiss(false)
                .build();

    }

    public static MaterialDialog getLogoutDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dialog_title_information))
                .content(context.getString(R.string.dialog_message_logout))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive((dialog, which) -> {
                    new ApisHelper().LogoutApi(context, new ApisHelper.LogOutCallback() {
                        @Override
                        public void onLogOutSuccess(String msg) {
                            logout(context);
                            dialog.dismiss();
                        }

                        @Override
                        public void onLogOutFailure() {
                            Toast.makeText(context, "Unable to logout at this movement", Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getInformation(Context context, String msg, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(context.getString(R.string.dialog_title_information))
                .cancelable(false)
                .content(msg)
                .positiveText("Okay")
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }
    public static MaterialDialog getStipeConnectDialog(Context context, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .content(context.getResources().getString(R.string.dialog_message_striperequired))
                .positiveText(context.getResources().getString(R.string.dialog_action_connect_stripe))
                .negativeText(context.getResources().getString(R.string.dialog_action_cancel))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getConfirmation(Context context, String msg, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .content(msg)
                .positiveText(context.getString(R.string.dialog_action_continue))
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getRemoveCardDialog(Context context, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .content(context.getString(R.string.dialog_message_remove_card))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getLoginDialog(Context context) {
        return new MaterialDialog.Builder(context)
//                .title(context.getString(R.string.dialog_title_information))
                .content(context.getString(R.string.dialog_message_login))
                .positiveText(context.getString(R.string.dialog_action_continue))
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive((dialog, which) -> {
                    HelperPreferences.get(context).clear();
//                    context.getSharedPreferences("sellah", MODE_PRIVATE).edit().clear().commit();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
//                    ((Activity) context).finish();
                })
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getBlockUnblockConfirmation(Context context, String title, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .content(title)
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }
    public static MaterialDialog getLiveConfirmationVideo(Context context, String title, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .title("Confirmation")
                .content(title)
                .cancelable(false)
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getLiveVideoStopedDialog(Context context, String title, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .cancelable(false)
                .title("Information")
                .content(title)
                .positiveText("Okay")
                .onPositive(callback).build();
    }

    public static MaterialDialog getDeleteAccountDilaog(Context context, MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .title("Delete Account")
                .content(context.getString(R.string.dialog_message_del_account))
                .positiveText(context.getString(R.string.dialog_action_yes_DA))
                .negativeText(context.getString(R.string.dialog_action_no_DA))
                .onPositive(callback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }


    public static Dialog getLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loader_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        GIFView gifView=dialog.findViewById(R.id.imageView);
//        gifView.loadGIFResource(context, R.drawable.loading_gif);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        /*final ImageView backgroundOne = dialog.findViewById(R.id.img_loading);
        final ImageView backgroundTwo = dialog.findViewById(R.id.img_loading1);
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, -1.0f); // Modified
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();*/
        ImageView imageView = dialog.findViewById(R.id.img_loading);
//        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(context).load(R.drawable.loading_gif).into(imageView);
//        Animation pulse = AnimationUtils.loadAnimation(context, R.anim.anim_pulses);
//        imageView.startAnimation(pulse);
        return dialog;
    }

    public static MaterialDialog promote_dialog(Context context, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .title("Promote")
                .content("Top-up promotion")
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }


    public static MaterialDialog getRemoveItemDialog(Context context, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .title("Remove item")
                .content(context.getString(R.string.dialog_message_remove_from_cart))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    public static MaterialDialog getRemoveCommentDialog(Context context, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .content(context.getString(R.string.dialog_message_remove_comment))
                .positiveText(context.getString(R.string.dialog_action_yes))
                .negativeText(context.getString(R.string.dialog_action_no))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

    /*public static Alerter getNetworkErrorDialog(Context context){
        return  Alerter.create((Activity) context)
                .setTitle("Network Error")
                .setText("Please Connect to Mobile network or WiFi.")
                .enableSwipeToDismiss()
                .enableProgress(true)
                .setProgressColorInt(Color.WHITE)
                .setBackgroundColorRes(R.color.colorRed);// or setBackgroundColorInt(Color.CYAN)
    }*/
    public static MaterialDialog getNetworkErrorDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .title("Network Error")
                .content("Please Connect to Mobile network or WiFi.")
                .positiveText("Go to settings")
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                        intent.setClassName("com.android.phone", "com.android.phone.W");
                    context.startActivity(intent);
                })
                .onNegative((dialog, which) -> dialog.dismiss()).build();

    }

    public static MaterialDialog getUnfollowDialog(Context context, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .content(context.getString(R.string.dialog_title_un_follow_user))
                .positiveText(context.getString(R.string.dialog_action_unfollow))
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();

    }

    public static MaterialDialog getCancelPromotion(Context context, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .content(context.getString(R.string.dialog_title_cancel_promotion))
                .positiveText(context.getString(R.string.dialog_action_continue))
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();

    }

    public static MaterialDialog getYouBlockedUserDialog(Context context,String title, MaterialDialog.SingleButtonCallback positivCallback) {
        return new MaterialDialog.Builder(context)
                .content(title)
                .positiveText(context.getString(R.string.dialog_action_unblock))
                .negativeText(context.getString(R.string.dialog_action_cancel))
                .onPositive(positivCallback)
                .onNegative((dialog, which) -> dialog.dismiss()).build();

    }
    public static MaterialDialog getUserBlockedYouDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content(context.getString(R.string.dialog_title_user_block_you))
                .negativeText(context.getString(R.string.ok))
                .onNegative((dialog, which) -> dialog.dismiss()).build();
    }

}
