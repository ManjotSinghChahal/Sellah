package com.app.admin.sellah.view.CustomDialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ReportApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportUserDialog extends BottomSheetDialog {

    @BindView(R.id.ll_reporting_item)
    LinearLayout llReportingItem;
    @BindView(R.id.l2_prohibited)
    LinearLayout l2Prohibited;
    @BindView(R.id.l3_offensiveContent)
    LinearLayout l3OffensiveContent;
    @BindView(R.id.l4_fakeProfile)
    LinearLayout l4FakeProfile;
    @BindView(R.id.l9_cancel)
    LinearLayout l9Cancel;
    @BindView(R.id.filter_dialog_root)
    LinearLayout filterDialogRoot;
    Context context;
    String userId="";
    ReportUserCallback callback;

    public ReportUserDialog(Context context,String userId,ReportUserCallback callback) {
        super(context,R.style.CustomDialog);
        this.context=context;
        this.userId=userId;
        this.callback=callback;
    }

    public static ReportUserDialog create(Context context,String userId,ReportUserCallback callback) {
        return new ReportUserDialog(context,userId,callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_profile_report_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_reporting_item, R.id.l2_prohibited, R.id.l3_offensiveContent, R.id.l4_fakeProfile, R.id.l9_cancel, R.id.filter_dialog_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_reporting_item:
//                hitReportApi(llReportingItem);
                break;
            case R.id.l2_prohibited:
                hitReportApi(l2Prohibited);
                break;
            case R.id.l3_offensiveContent:
                hitReportApi(l3OffensiveContent);
                break;
            case R.id.l4_fakeProfile:
                hitReportApi(l4FakeProfile);
                break;
            case R.id.l9_cancel:
                dismiss();
//                hitReportApi(l9Cancel);
                break;
            case R.id.filter_dialog_root:
                break;
        }
    }

    private void hitReportApi(LinearLayout layout) {

        new ReportApi().hitReportApi(context, layout
                , userId, "", (msg) -> {
                   callback.onReportUserSuccess(msg);
                   dismiss();
                }, () -> {
                    callback.onReportUserFailure();
                });
    }

    public interface ReportUserCallback{
        void onReportUserSuccess(String msg);
        void onReportUserFailure();
    }
    //    user_profile_report_dialog
}
