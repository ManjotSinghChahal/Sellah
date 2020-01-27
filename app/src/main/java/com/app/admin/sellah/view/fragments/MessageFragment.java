package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.ReportApi;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.model.extra.ChatHeadermodel.Record;
import com.app.admin.sellah.model.extra.DeleteChatModel.DeleteChat;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.SwipeHelper;
import com.app.admin.sellah.view.CustomDialogs.VideoDialog;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.MessageListAdapter;
import com.app.admin.sellah.view.adapter.NewMessageListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //Mess recycler
    TextView txtNewMessage, txtMessage;

    RecyclerView messRecycler, ongoingrecycler;

    //New Mess Recycler
    RecyclerView newMessRecycler;
    TextWatcher searchWatcher;
    @BindView(R.id.rel_root_noti)
    RelativeLayout relRootNoti;
    Unbinder unbinder;
    @BindView(R.id.ll_nochat)
    LinearLayout llNochat;
    @BindView(R.id.rel_top_messageFrag)
    LinearLayout relTopMessageFrag;

    private View view;
    private MessageListAdapter oldMsgAdapter;
    private NewMessageListAdapter newMsgAdapter;
    private MessageListAdapter ongoingadapter;
    private ChattedListModel oldMsgList;
    private String TAG = MessageFragment.class.getSimpleName();
    CardView actionButton;
    public static String post_selId = "";

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        // messRecycler = view.findViewById(R.id.message_recycler);
        messRecycler = view.findViewById(R.id.message_recycler);
        newMessRecycler = view.findViewById(R.id.new_message_recycler);
        ongoingrecycler = view.findViewById(R.id.ongoing_transactionrecyclerview);
        txtMessage = view.findViewById(R.id.txt_message);
        txtNewMessage = view.findViewById(R.id.txt_new_message);
        actionButton = view.findViewById(R.id.floating_action_button);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        onCickListners();

        //Notification New Message List
       /* notificationNewMessList();
        notificationFollowListAdapter = new NotificationFollowListAdapter(notificationNewMessList, getActivity(), 0);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newMessRecycler.setLayoutManager(horizontalLayoutManager1);
        newMessRecycler.setAdapter(notificationFollowListAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_new_mess_adapter, 10);
        new SwipeHelper(getActivity(), newMessRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#E35252"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos){
                                //TODO:onDelete
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Report",
                        0,
                        Color.parseColor("#EEC12D"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos){
                                //TODO:onDelete
                            }
                        }
                ));}};*/
        //Notification Message List
//        notificationMessList();

        searchWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    boolean isNewMessage = filterNewMessages(s.toString());
//                  Global.getTotalHeightofLinearRecyclerView(getActivity(), rVNewFollow, R.layout.notification_new_mess_adapter, 10);
                    boolean isMessage = filterMessages(s.toString());
//                  Global.getTotalHeightofLinearRecyclerView(getActivity(), rVAnnouncments, R.layout.notification_new_mess_adapter, 10);
                } catch (Exception e) {
                }
            }
        };

       /*actionButton.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Log.e( "onSwipeLeft: ","w" );
                getActivity().onBackPressed();
            }
        });*/

        ongoingadapter = new MessageListAdapter(getActivity(), 3);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ongoingrecycler.setLayoutManager(horizontalLayoutManager);
        ongoingrecycler.setAdapter(ongoingadapter);

        getChatedListApi();

        new SwipeHelper(getActivity(), messRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton("Delete", 0, Color.parseColor("#666666"), new UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        new MaterialDialog.Builder(getActivity())
//                .title(context.getString(R.string.dialog_title_information))
                                .content("Are you want to delete all chat with " + oldMsgList.getRecord().get(pos).getFriendName())
                                .positiveText("Ok")
                                .negativeText("Cancel")
                                .onPositive((dialog, which) -> {
                                    deleteApi(oldMsgList.getRecord().get(pos).getRoomName());
                                    dialog.dismiss();
                                })
                                .onNegative((dialog, which) ->
                                        {
                                            dialog.dismiss();
                                        }
                                ).build().show();
                    }
                }
                ));

                underlayButtons.add(new UnderlayButton(
                        "Report",
                        0,
                        Color.parseColor("#ffc53e"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete

                                Log.e(TAG, "onClick: Report");
/*                                ReportUserDialog.create(getActivity(),post_selId, new ReportUserDialog.ReportUserCallback() {
                                    @Override
                                    public void onReportUserSuccess(String msg) {
                                        Log.e("valePrint",msg+" here");
                                        Toast.makeText(getActivity(), msg+"", Toast.LENGTH_SHORT).show();
                                      *//*  Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();*//*
                                    }

                                    @Override
                                    public void onReportUserFailure() {
                                        Toast.makeText(getActivity(), "Something went's wrong", Toast.LENGTH_SHORT).show();
                                        *//*Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();*//*
                                    }
                                }).show();*/


                                //------------------------------------------------------
                                BottomSheetDialog filterDialog = new BottomSheetDialog(getActivity());

                                filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                                filterDialog.setContentView(R.layout.filter_dialog);
                                LinearLayout ll_reporting_item = filterDialog.findViewById(R.id.ll_reporting_item);
                                LinearLayout l2_prohibited = filterDialog.findViewById(R.id.l2_prohibited);
                                LinearLayout l3_mispriced = filterDialog.findViewById(R.id.l3_mispriced);
                                LinearLayout l4_wrongCategroy = filterDialog.findViewById(R.id.l4_wrongCategroy);
                                LinearLayout l5_duplicate = filterDialog.findViewById(R.id.l5_duplicate);
                                LinearLayout l6_offensive = filterDialog.findViewById(R.id.l6_offensive);
                                LinearLayout l7_irrelevant = filterDialog.findViewById(R.id.l7_irrelevant);
                                LinearLayout l8_counterfeit = filterDialog.findViewById(R.id.l8_counterfeit);
                                LinearLayout l9_cancel = filterDialog.findViewById(R.id.l9_cancel);
                                filterDialog.show();

                                l2_prohibited.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l2_prohibited);
                                        filterDialog.dismiss();
                                    }
                                });
                                l3_mispriced.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l3_mispriced);
                                        filterDialog.dismiss();
                                    }
                                });
                                l4_wrongCategroy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l4_wrongCategroy);
                                        filterDialog.dismiss();
                                    }
                                });
                                l5_duplicate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l5_duplicate);
                                        filterDialog.dismiss();
                                    }
                                });
                                l6_offensive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l6_offensive);
                                        filterDialog.dismiss();
                                    }
                                });
                                l7_irrelevant.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l7_irrelevant);
                                        filterDialog.dismiss();
                                    }
                                });
                                l8_counterfeit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        hitReportApi(l8_counterfeit);
                                        filterDialog.dismiss();
                                    }
                                });
                                l9_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        filterDialog.dismiss();
                                    }
                                });


                                //--------------------------------------------------------
                            }
                        }
                ));

            }
        };
        //------------------------------------------------------
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onResume() {
        super.onResume();

       /* if(getUserVisibleHint()) {

            getChatedListApi();
        }*/
        getChatedListApi();
    }

    private boolean filter(String text) {
        //new array list that will hold the filtered data
        /*ChattedListModel filterModel=new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationNewMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        notificationFollowListAdapter.filterList(filteredList);
        if (notificationFollowListAdapter.getItemCount() != 0) {
            txtNewMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtNewMessage.setVisibility(View.GONE);
            return true;
        }
        return false;*/
        return false;
    }

    private boolean filterNewMessages(String text) {

        ChattedListModel filterModel = new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (Record row : oldMsgList.getRecord()) {
            // name match condition. this might differ depending on your requirement
            if (row.getFriendName().toLowerCase().contains(text.toLowerCase()) && !row.getIsRead().equalsIgnoreCase("Y")) {
                filteredList.add(row);
                Log.e(TAG, "filterMessages: found");
            } else {
                Log.e(TAG, "filterMessages: Not Found");
            }
        }
//        filterModel.setRecord(filteredList);

        if (text.length() > 0) {
            newMsgAdapter.filterList(filteredList);
        } else {
            if (oldMsgList != null) {
                filteredList.clear();
                for (Record row : oldMsgList.getRecord()) {
                    // name match condition. this might differ depending on your requirement
                    if (!row.getIsRead().equalsIgnoreCase("Y")) {
                        filteredList.add(row);
                        Log.e(TAG, "filterMessages: found");
                    } else {
                        Log.e(TAG, "filterMessages: Not Found");
                    }
                }
                newMsgAdapter.filterList(filteredList);
            }

        }
        //calling a method of the adapter class and passing the filtered list
        if (newMsgAdapter.getItemCount() != 0) {
            txtNewMessage.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).notificationImg.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtNewMessage.setVisibility(View.GONE);
            ((MainActivity) getActivity()).notificationImg.setVisibility(View.GONE);
            return true;
        }
        //new array list that will hold the filtered data
       /* List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        notificationMessAdapter.filterList(filteredList);
        if (notificationMessAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }*/
//       return false;
    }

    private boolean filterMessages(String text) {

        ChattedListModel filterModel = new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (Record row : oldMsgList.getRecord()) {
            // name match condition. this might differ depending on your requirement
            if (row.getFriendName().toLowerCase().contains(text.toLowerCase()) && row.getIsRead().equalsIgnoreCase("Y")) {
                filteredList.add(row);
                Log.e(TAG, "filterMessages: found");
            } else {
                Log.e(TAG, "filterMessages: Not Found");
            }
        }
//        filterModel.setRecord(filteredList);

        if (text.length() > 0) {
            oldMsgAdapter.filterList(filteredList);
        } else {
            if (oldMsgList != null) {
                filteredList.clear();
                for (Record row : oldMsgList.getRecord()) {
                    // name match condition. this might differ depending on your requirement
                    if (row.getIsRead().equalsIgnoreCase("Y")) {
                        filteredList.add(row);
                        Log.e(TAG, "filterMessages: found");
                    } else {
                        Log.e(TAG, "filterMessages: Not Found");
                    }
                }
                oldMsgAdapter.filterList(filteredList);
            }

        }
        //calling a method of the adapter class and passing the filtered list
        if (oldMsgAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }
        //new array list that will hold the filtered data
       /* List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        notificationMessAdapter.filterList(filteredList);
        if (notificationMessAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }*/
//       return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWatcher);
        ((MainActivity) getActivity()).searchEditText.setText("");*/
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && view != null) {
//            if (((MainActivity) getActivity()).searchEditText != null) {
            ((MainActivity) getActivity()).searchEditText.addTextChangedListener(searchWatcher);
//            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(PUSH_NOTIFICATION));

            Log.e("message_Frag", "visible");
// }

        } else {
            if (view != null) {
//                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
                ((MainActivity) getActivity()).searchEditText.setText("");
                ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWatcher);
              /*  boolean isNewMessage = filter("");
                Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_new_mess_adapter, 10);
                boolean isMessage = filterMessages("");
                Global.getTotalHeightofLinearRecyclerView(getActivity(), messRecycler, R.layout.notification_new_mess_adapter, 10);
*/
                Log.e("message_Frag", "not-visible");

            }
//            Log.e("message_Frag", "not-visible");
        }
    }

    private void onCickListners() {
        actionButton.setOnClickListener(view1 -> {
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StreamedVideosFragment()).addToBackStack(null).commit();
            VideoDialog videoDialog=new VideoDialog(getActivity());
            videoDialog.show();
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                if (message != null) {
                    switch (message.getNotiType()) {
                        case NT_ACCEPT_REJECT:
                            break;
                        case NT_FOLLOW:
                            break;
                        case NT_COMMENT_ADDED:
                            break;
                        case NT_PRODUCT_ADDED:
                            break;
                        case NT_CHAT:
                            getChatedListApi();
                            break;
                        case NT_PAYMENT:
                            break;
                        case NT_OFFER_MAKE:
                            break;
                        default:

                            break;
                    }
                }
                Log.e("receiver", "Got message: Message " + message.getMessage());
            } catch (Exception e) {
                Log.e("receiver_failure", "onReceive: " + e.getMessage());
            }
        }
    };


    private void deleteApi(String roomId) {
        Log.e("delroomid", roomId + "//");
        new ApisHelper().getDeleteChat(HelperPreferences.get(getActivity()).getString(UID), roomId, new ApisHelper.OnDeleteChat() {
            @Override
            public void onDeleteChatSuccess(DeleteChat body) {

                try {
                    Log.e("responseSuccess", body.getMessage() + "//" + body.getStatus());
                    //if(body.getStatus().equalsIgnoreCase("1"))
                    //Snackbar.make(getActivity().getWindow().getDecorView(), body.getMessage(), Snackbar.LENGTH_SHORT).setAction("", null).show();
                    Log.e("responseSuccess", body.getMessage());
                    Toast.makeText(getActivity(), body.getMessage() + "", Toast.LENGTH_SHORT).show();

                    getChatedListApi();

                } catch (Exception e) {

                }

            }

            @Override
            public void onDeleteChatFailure() {
                Log.e("responsefail", "chatdelfail");
            }
        });

    }

    private void getChatedListApi() {
        new ApisHelper().getChattedUsersListApi(getActivity(), new ApisHelper.OnGetChatedListDataListners() {
            @Override
            public void onGetChattedListSuccess(ChattedListModel body, Dialog dialog) {
                try {
                    Log.e("chatid", body.getStatus() + "//");
                    if (body.getStatus().equals("1")) {
                        relRootNoti.setVisibility(View.VISIBLE);
                        llNochat.setVisibility(View.GONE);

                        Log.e("idPrint", body.getRecord().get(0).getFriendId());
                        Log.e("deleteAfter", "works");

                        //body.getRecord().get(0).get

                        if (Global.DEEP_LINKING_STATUS.equalsIgnoreCase("enable")) {
                            for (int i = 0; i < body.getRecord().size(); i++) {
                                Log.e("newmsgdata", body.getRecord().get(i).getRoomName() + "//");
                                if (body.getRecord().get(i).getFriendId().equalsIgnoreCase(Global.DEEP_LINKING_PRODUCT_ID)) {
                                    PersonalProfileFragment fragment = new PersonalProfileFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(SAConstants.Keys.OTHER_USER_ID, body.getRecord().get(i).getFriendId());
                                    fragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

                                    break;
                                }
                            }

                        } else {
                            setupOldMessageList(body);
                        }

                    } else {
                        relRootNoti.setVisibility(View.GONE);
                        llNochat.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    if (relRootNoti != null)
                        relRootNoti.setVisibility(View.GONE);
                    if (llNochat != null)
                        llNochat.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetChattedListFailure() {

            }
        });
    }

    private void setupOldMessageList(ChattedListModel body) throws Exception {

        oldMsgList = body;
        List<Record> oldMsgList1 = new ArrayList<>();
        List<Record> newMsgList = new ArrayList<>();

        for (Record row : body.getRecord()) {

            //name match condition. this might differ depending on your requirement
            Log.e("oldrwodata", row.getRoomName() + "//" + row.getIsRead());

            if (row.getIsRead().equalsIgnoreCase("Y")) {
                oldMsgList1.add(row);
                Log.e(TAG, "filterMessages: old");
            } else {
                newMsgList.add(row);
                Log.e(TAG, "filterMessages: Not old");
            }

        }

        if (newMsgList != null && newMsgList.size() > 0) {
            ((NotificationFragment) getParentFragment()).messageImage.setVisibility(View.VISIBLE);
            if (((MainActivity) getActivity()).activityImage != null)
                ((MainActivity) getActivity()).notificationImg.setVisibility(View.VISIBLE);

        } else {
            if (((MainActivity) getActivity()).activityImage != null)
                ((MainActivity) getActivity()).notificationImg.setVisibility(View.GONE);

            ((NotificationFragment) getParentFragment()).messageImage.setVisibility(View.GONE);
        }
        if (oldMsgList1 != null && oldMsgList1.size() > 0) {
            txtMessage.setVisibility(View.VISIBLE);
        } else {
            txtMessage.setVisibility(View.GONE);
        }

        int valOld = checkOldwMessageCount(oldMsgList1);
        oldMsgAdapter = new MessageListAdapter(valOld, oldMsgList1, getActivity(), 0, getActivity().getSupportFragmentManager(), messRecycler, new MessageListAdapter.onSwipeLeft() {
            @Override
            public void onSwipeLeftSuccess(int postion) {

            }

            @Override
            public void onSwipeLeftFailure() {

            }
        });

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messRecycler.setLayoutManager(horizontalLayoutManager);
        messRecycler.setAdapter(oldMsgAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), messRecycler, R.layout.notification_mess_adapter, 10);
        int valNew = checkNewMessageCount(newMsgList);

        newMsgAdapter = new NewMessageListAdapter(valNew, newMsgList, getActivity(), 0, getActivity().getSupportFragmentManager(), messRecycler, new MessageListAdapter.onSwipeLeft() {
            @Override
            public void onSwipeLeftSuccess(int pos) {

            }

            @Override
            public void onSwipeLeftFailure() {

            }
        });

//      notificationMessAdapter = new NotificationActivityAdapter(notificationMessList, getActivity(), 0);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newMessRecycler.setLayoutManager(horizontalLayoutManager1);
        newMessRecycler.setAdapter(newMsgAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_mess_adapter, 0);

        if (newMsgList != null && newMsgList.size() > 0) {
            //txtNewMessage.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).notificationImg.setVisibility(View.VISIBLE);
        } else {
            Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_mess_adapter, 0);
            //txtNewMessage.setVisibility(View.GONE);
            ((MainActivity) getActivity()).notificationImg.setVisibility(View.GONE);
        }

          /*new SwipeHelper(getActivity(), newMessRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton("Delete",0,
                        Color.parseColor("#E35252"),
                        new UnderlayButtonClickListener(){
                            @Override
                            public void onClick(int pos){
                                //TODO:onDelete
                                Log.e(TAG, "onClick: delete");
                            }}));
                underlayButtons.add(new UnderlayButton(
                        "Report",0,
                        Color.parseColor("#EEC12D"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Log.e(TAG, "onClick: Report");
                                ReportUserDialog.create(getActivity(), newMsgList.get(pos).getFriendId(), new ReportUserDialog.ReportUserCallback() {
                                    @Override
                                    public void onReportUserSuccess(String msg) {
                                        Log.e("ggggg",msg);
                                        Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).setAction("", null).show();
                                    }
                                    @Override
                                    public void onReportUserFailure() {
                                        Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }
                                }).show();
                            }}));}};*/
    }

    @Override
    public void onStop() {
        super.onStop();
        new ApisHelper().cancel_chatlist();
    }

    private void hitReportApi(LinearLayout layout) {

        new ReportApi().hitReportApi(getActivity(), layout
                , post_selId, "", (msg) -> {
                    Snackbar.make(relTopMessageFrag, msg, Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }, () -> {
                    Snackbar.make(relTopMessageFrag, "Please try again later", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                });
    }

    public int checkNewMessageCount(List<Record> newMsgList) {
        int valNew = 0;
        for (int i = 0; i < newMsgList.size(); i++) {
            if (newMsgList.get(i).getIsDeleted().equalsIgnoreCase("N")) {
                valNew = valNew + 1;
            }
        }

        if (valNew == 0) {
            txtNewMessage.setVisibility(View.GONE);
            newMessRecycler.setVisibility(View.GONE);
        } else {
            txtNewMessage.setVisibility(View.VISIBLE);
            newMessRecycler.setVisibility(View.VISIBLE);
        }
        return valNew;

    }

    public int checkOldwMessageCount(List<Record> oldMsgList) {
        int valNew = 0;
        for (int i = 0; i < oldMsgList.size(); i++) {
            if (oldMsgList.get(i).getIsDeleted().equalsIgnoreCase("N")) {
                valNew = valNew + 1;
            }
        }

        if (valNew == 0) {
            txtMessage.setVisibility(View.GONE);
            messRecycler.setVisibility(View.GONE);
        } else {
            txtMessage.setVisibility(View.VISIBLE);
            messRecycler.setVisibility(View.VISIBLE);
        }
        return valNew;

    }

}
