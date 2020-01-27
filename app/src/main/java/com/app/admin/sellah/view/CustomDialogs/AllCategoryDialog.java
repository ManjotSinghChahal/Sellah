package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.model.extra.Categories.Result;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.view.adapter.AllCategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllCategoryDialog extends AlertDialog {
    @BindView(R.id.cancel_details)
    ImageView cancelDetail;
    @BindView(R.id.downArrow)
    ImageView downArrow;
    @BindView(R.id.rec_categories)
    RecyclerView recCategories;
    @BindView(R.id.filter_dialog_root)
    LinearLayout filterDialogRoot;
    private Context context;


    protected AllCategoryDialog(Context context) {
        super(context);
        this.context = context;
    }

    public static AllCategoryDialog create(Context context) {
        return new AllCategoryDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.layout_all_category_dialog);
        ButterKnife.bind(this);
        List<Result> records = ExpandableListData.getData().getResult();
        List<Integer> colorList = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < records.size(); i++) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colorList.add(color);
        }
        AllCategoryAdapter adapter = new AllCategoryAdapter(records, context, colorList, () -> {
            dismiss();
        });
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recCategories.setLayoutManager(horizontalLayoutManager1);
        recCategories.setAdapter(adapter);

    }

    @OnClick(R.id.cancel_details)
    public void onViewClicked() {
        dismiss();
    }
}
