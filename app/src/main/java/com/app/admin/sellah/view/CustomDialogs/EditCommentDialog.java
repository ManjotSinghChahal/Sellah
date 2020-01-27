package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCommentDialog extends AlertDialog {


    @BindView(R.id.txt_testimonial)
    TextView txtTestimonial;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.btn_submit_review)
    Button btnSubmitReview;
    private WebService webService;
    private Dialog dialog;
    private Context context;
    Float givenRating;
    String commentId;
    String comment;
    EditCommentCallback callback;

    protected EditCommentDialog(Context context, String commentId, String comment, EditCommentCallback callback) {
        super(context);
        this.context = context;
        this.commentId = commentId;
        this.comment = comment;
        this.callback = callback;
    }

    public static EditCommentDialog create(Context context, String commentId, String comment, EditCommentCallback callback) {
        return new EditCommentDialog(context, commentId, comment, callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);
        setContentView(R.layout.layout_edit_comment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);
        edtComment.setText(comment);
    }

    @OnClick(R.id.btn_submit_review)
    public void onViewClicked() {
        if (!Global.getText(edtComment).equalsIgnoreCase("")) {
            editCommentApi(commentId, Global.getText(edtComment));
        } else {
            dismiss();
        }
    }

    private void editCommentApi(String commentId, String feedBack) {

        new ApisHelper().editCommentApi(context, commentId, feedBack, new ApisHelper.EditCommentCallback() {
            @Override
            public void onCommentEditionSuccess(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                callback.onEditSuccess();
                dismiss();
            }

            @Override
            public void onCommentEditionFailure() {
                dismiss();
                Toast.makeText(context, "Unable to edit comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface EditCommentCallback {
        void onEditSuccess();
    }
}