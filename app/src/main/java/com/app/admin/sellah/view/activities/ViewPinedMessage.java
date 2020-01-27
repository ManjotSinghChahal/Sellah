package com.app.admin.sellah.view.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.LiveStreamChatAdapter;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ViewPinedMessage extends AppCompatActivity {

    @BindView(R.id.relLay_back_live)
    RelativeLayout relLayBackLive;
    @BindView(R.id.relLayoutHeader)
    RelativeLayout relLayoutHeader;
    @BindView(R.id.rec_message)
    RecyclerView recMessage;
    private LiveStreamChatAdapter adapter;
    private ArrayList<HashMap<String, String>> arrayList;
    String groupId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pined_message);
        ButterKnife.bind(this);
        Global.StatusBarLightMode(this);
        arrayList = new ArrayList<>();
        groupId = getIntent() != null ? getIntent().hasExtra("group_id") ? getIntent().getStringExtra("group_id") : "" : "";
        Log.e("groupId", "onCreate: " + groupId);
        getChatData();
    }

    @OnClick(R.id.relLay_back_live)
    public void onViewClicked() {
        onBackPressed();
    }

    private void getChatData() {
        Dialog dialog = S_Dialogs.getLoadingDialog(this);
        dialog.show();
        Call<JsonObject> getChatData = Global.WebServiceConstants.getRetrofitinstance().getPinCommentApi(HelperPreferences.get(this).getString(UID), groupId);
        getChatData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        Log.e("PinMsg", "onResponse: " +response.body().toString());
                        Log.e("getChatPin", "onResponse: status" + status);
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("comment_list");
                            for (int g = 0; g < jsonArray.length(); g++) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(g);
                                String message = jsonObject1.getString("message");
                                String sender_id = jsonObject1.getString("sender_id");
                                String sender_image = jsonObject1.getString("sender_image");
                                String msg_id = jsonObject1.getString("msg_id");
                                String type = jsonObject1.getString("type");
                                String image_url = jsonObject1.getString("image_url");
                                String groupId = jsonObject1.getString("group_id");
                                String commentId = jsonObject1.getString("comment_id");
                                String isPined = jsonObject1.getString("ispined");
                                String created_at = jsonObject1.getString("created_at");
//                                Log.e("Dvsdvfdfv", message);
                                hashMap.put("senderid", sender_id);
                                hashMap.put("image_url", image_url);
                                hashMap.put("sender_image", sender_image);
                                hashMap.put("msg_id", msg_id);
                                hashMap.put("comment_id", commentId);
                                hashMap.put("message", message);
                                hashMap.put("group_id", groupId);
                                hashMap.put("ispined", isPined);
                                hashMap.put("type", type);
                                hashMap.put("owner_id", type);
                                hashMap.put("created_at", created_at);
                                arrayList.add(hashMap);


//                                arrayList.add(hashMap);

                            }
                            setUpMessgae(arrayList);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("jsonExc", "onResponse: " + e.getMessage());
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void setUpMessgae(ArrayList<HashMap<String, String>> models) {

        try {
            adapter = new LiveStreamChatAdapter(models, ViewPinedMessage.this, "", new LiveStreamChatAdapter.OptionsClickCallBack() {
                @Override
                public void onMakeOfferClicked(String comment_id, String senderId) {

                }

                @Override
                public void onPinLiveCommentClick(String commentId) {

                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewPinedMessage.this, LinearLayout.VERTICAL, false);
            recMessage.setLayoutManager(linearLayoutManager);
            recMessage.setItemAnimator(new DefaultItemAnimator());
            recMessage.setAdapter(adapter);
            Log.e("Dvcfsdvc", models.size() + "");
            if (models.size() > 0) {
                recMessage.scrollToPosition(models.size() - 1);
            }
            /*if (Build.VERSION.SDK_INT >= 11) {
                recMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v,
                                               int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            recMessage.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (models != null && models.size() > 0) {
                                            recMessage.smoothScrollToPosition(recMessage.getAdapter().getItemCount() - 1);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }, 100);
                        }
                    }
                });
            }*/
        } catch (Exception e) {
            Log.e("setup_error", "setUpMessgae: ");
        }


    }
}
