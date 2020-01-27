package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.NotificationList.ArrAnnouncement;
import com.app.admin.sellah.model.extra.NotificationList.ArrFollow;
import com.app.admin.sellah.model.extra.NotificationList.ArrPost;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.model.extra.NotificationMessModel;
import com.app.admin.sellah.model.extra.SwipeHelper;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.NotificationActivityAdapter;
import com.app.admin.sellah.view.adapter.NotificationFollowListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;


public class NotificationFragmentN extends Fragment {

    RecyclerView rVAnnouncments, rVPostActivity;
    List<NotificationMessModel> notificationMessList;
    NotificationActivityAdapter notificationMessAdapter;
    NotificationActivityAdapter notificationPostActivityAdapter;
    LinearLayout li_error;
    FloatingActionButton actionButton;
    TextView txtNewFollowers, txtAnnouncments, txtPostActivities;
    //New Mess Recycler
    RecyclerView rVNewFollow;
    List<NotificationMessModel> notificationNewMessList;
    NotificationFollowListAdapter notificationFollowListAdapter;
    private TextWatcher searchWacher;
    private View view;
    WebService service;
    NotificationListModel notificationListModel;

    public NotificationFragmentN() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_fragment_n, container, false);
        service = Global.WebServiceConstants.getRetrofitinstance();
        getIds();
        getNotificationList();


        notificationMessList = new ArrayList<>();
        notificationNewMessList = new ArrayList<>();

//        ((MainActivity) getActivity()).searchEditText.setn;

        searchWacher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                notificationFollowListAdapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    boolean isNewNotification = filter(s.toString());
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVNewFollow, R.layout.notification_new_mess_adapter, 10);
                    boolean isNewAnnouncement = filterAnnounceMents(s.toString());
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVAnnouncments, R.layout.notification_mess_adapter, 10);
                    boolean isNewPostActivity = filterPostAcivities(s.toString());
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVPostActivity, R.layout.notification_mess_adapter, 10);

                    if (isNewAnnouncement && isNewNotification && isNewPostActivity) {
                        li_error.setVisibility(View.VISIBLE);
                    } else {
                        li_error.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        };

        ((MainActivity) getActivity()).searchEditText.addTextChangedListener(searchWacher);


        return view;
    }
    private void setUpPostActivityList(NotificationListModel body) {

        notificationPostActivityAdapter = new NotificationActivityAdapter(body, getActivity(), 1, "p");
        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rVPostActivity.setLayoutManager(vLayoutManager);
        rVPostActivity.setAdapter(notificationPostActivityAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), rVPostActivity, R.layout.notification_mess_adapter, 10);
        new SwipeHelper(getActivity(), rVPostActivity) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Read",
                        0,
                        Color.parseColor("#EEC12D"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                new ApisHelper().readNotificationApi(getActivity(), body.getArrPost().get(pos).getNotiId(),"one", new ApisHelper.ReadNotificationCallback() {
                                    @Override
                                    public void onReadNotificationSuccess(String msg) {
                                        body.getArrPost().get(pos).setReadStatus("1");
                                        notificationMessAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onReadNotificationFailure() {

                                    }
                                });
                            }
                        }
                ));

            }
        };
        if (notificationPostActivityAdapter.getItemCount() != 0) {
            txtPostActivities.setVisibility(View.VISIBLE);
//            return false;
        } else {
            txtPostActivities.setVisibility(View.GONE);
//            return true;
        }

    }

    private void setUpAnnouncmentList(NotificationListModel body) {
        //Notification Message List
        notificationMessList();
        notificationMessAdapter = new NotificationActivityAdapter(body, getActivity(), 1, "a");
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rVAnnouncments.setLayoutManager(horizontalLayoutManager);
        rVAnnouncments.setAdapter(notificationMessAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), rVAnnouncments, R.layout.notification_mess_adapter, 10);
        new SwipeHelper(getActivity(), rVAnnouncments) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Read",
                        0,
                        Color.parseColor("#EEC12D"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                new ApisHelper().readNotificationApi(getActivity(), body.getArrPost().get(pos).getNotiId(),"one", new ApisHelper.ReadNotificationCallback() {
                                    @Override
                                    public void onReadNotificationSuccess(String msg) {

                                        body.getArrAnnouncements().get(pos).setReadStatus("1");
                                        notificationMessAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onReadNotificationFailure() {

                                    }
                                });
                            }
                        }
                ));

            }
        };
        if (notificationMessAdapter.getItemCount() != 0) {
            txtAnnouncments.setVisibility(View.VISIBLE);
//            return false;
        } else {
            txtAnnouncments.setVisibility(View.GONE);
//            return true;
        }


    }

    private void setUpFollowerList(NotificationListModel body) {
        //Notification New Message List
        notificationFollowListAdapter = new NotificationFollowListAdapter(body.getArrFollow(), getActivity(), 1);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rVNewFollow.setLayoutManager(horizontalLayoutManager1);
        rVNewFollow.setAdapter(notificationFollowListAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), rVNewFollow, R.layout.notification_new_mess_adapter, 10);
        new SwipeHelper(getActivity(), rVNewFollow) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Read",
                        0,
                        Color.parseColor("#EEC12D"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                new ApisHelper().readNotificationApi(getActivity(), body.getArrPost().get(pos).getNotiId(),"one", new ApisHelper.ReadNotificationCallback() {
                                    @Override
                                    public void onReadNotificationSuccess(String msg) {
                                        body.getArrFollow().get(pos).setReadStatus("1");
                                        notificationMessAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onReadNotificationFailure() {

                                    }
                                });
                            }
                        }
                ));

            }
        };
        if (notificationFollowListAdapter.getItemCount() != 0) {
            txtNewFollowers.setVisibility(View.VISIBLE);
//            return false;
        } else {
            txtNewFollowers.setVisibility(View.GONE);
//            return true;
        }
    }

    private void getIds() {
        rVAnnouncments = view.findViewById(R.id.message_recycler);
        rVPostActivity = view.findViewById(R.id.post_activity_recycler);
        rVNewFollow = view.findViewById(R.id.new_message_recycler);
        txtNewFollowers = view.findViewById(R.id.txt_new_followers);
        txtAnnouncments = view.findViewById(R.id.txt_announcements);
        txtPostActivities = view.findViewById(R.id.txt_post_activites);
        li_error = view.findViewById(R.id.li_error);
        actionButton = view.findViewById(R.id.floating_action_button);
        onCickListners();
    }

    private void onCickListners() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StreamedVideosFragment()).addToBackStack(null).commit();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event

    /*  public void notificationNewMessList() {

          NotificationMessModel newmess1 = new NotificationMessModel(R.drawable.profile_icon_img, "Elijah Jackson", "Advertising Outsdoor");
          NotificationMessModel newmess2 = new NotificationMessModel(R.drawable.profile_icon_img, "julie Jackson", "Marketing");
          NotificationMessModel newmess3 = new NotificationMessModel(R.drawable.profile_icon_img, "Amieson", "Dramebaz");
          notificationNewMessList.add(newmess1);
          notificationNewMessList.add(newmess2);
          notificationNewMessList.add(newmess3);


      }
  */
    public void notificationMessList() {
/*

        NotificationMessModel mess1 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");
        NotificationMessModel mess2 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");
        NotificationMessModel mess3 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");
        NotificationMessModel mess4 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");
        NotificationMessModel mess5 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");
        NotificationMessModel mess6 = new NotificationMessModel(R.drawable.profile, "Mark Farmer", "Freelance Design Tricks");


        notificationMessList.add(mess1);
        notificationMessList.add(mess2);
        notificationMessList.add(mess3);
        notificationMessList.add(mess4);
        notificationMessList.add(mess5);
        notificationMessList.add(mess6);
*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWacher);
        ((MainActivity) getActivity()).searchEditText.setText("");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (view != null) {
                ((MainActivity) getActivity()).searchEditText.addTextChangedListener(searchWacher);
                Log.e("Notification_Frag", "visible");
            }

        } else {

            if (view != null) {

                try {
                    ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWacher);
                    ((MainActivity) getActivity()).searchEditText.setText("");
                   /* boolean isNewNotification = filter("");
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVNewFollow, R.layout.notification_new_mess_adapter, 10);

                    boolean isNewAnnouncement = filterAnnounceMents("");
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVAnnouncments, R.layout.notification_new_mess_adapter, 10);

                    boolean isNewPostActivity = filterPostAcivities("");
                    Global.getTotalHeightofLinearRecyclerView(getActivity(), rVPostActivity, R.layout.notification_new_mess_adapter, 10);

                    if (isNewAnnouncement && isNewNotification && isNewPostActivity) {
                        li_error.setVisibility(View.VISIBLE);
                    } else {
                        li_error.setVisibility(View.GONE);
                    }*/
                    Log.e("Notification_Frag", "not-visible");
                } catch (Exception e) {

                }
            }


        }
    }

    private boolean filter(String text) {
        //new array list that will hold the filtered data
        /*List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationNewMessList) {
            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        notificationFollowListAdapter.filterList(filteredList);
        if (notificationFollowListAdapter.getItemCount() != 0) {
            txtNewFollowers.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtNewFollowers.setVisibility(View.GONE);
            return true;
        }*/
//        return false;
        try {
            NotificationListModel filterdListmodel = new NotificationListModel();
            filterdListmodel.setArrFollow(notificationListModel.getArrFollow());
            filterdListmodel.setArrAnnouncements(notificationListModel.getArrAnnouncements());
            List<ArrFollow> filteredList = new ArrayList<>();
            for (ArrFollow row : notificationListModel.getArrFollow()) {
                // name match condition. this might differ depending on your requirement
                if (row.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(row);
                }
            }

            //calling a method of the adapter class and passing the filtered list
            if (text.length() > 0) {
                filterdListmodel.setArrFollow(filteredList);
            } else {
                filterdListmodel.setArrFollow(notificationListModel.getArrFollow());
            }
            notificationFollowListAdapter.filterList(filterdListmodel.getArrFollow());
            if (notificationFollowListAdapter.getItemCount() != 0) {
                txtNewFollowers.setVisibility(View.VISIBLE);
                return false;
            } else {
                txtNewFollowers.setVisibility(View.GONE);
                return true;
            }
//            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean filterAnnounceMents(String text) {
        //new array list that will hold the filtered data
        /*List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        notificationMessAdapter.filterList(filteredList);
        if (notificationMessAdapter.getItemCount() != 0) {
            txtAnnouncments.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtAnnouncments.setVisibility(View.GONE);
            return true;
        }*/
//        return false;
        try {
            NotificationListModel filterdListmodel = new NotificationListModel();
            filterdListmodel.setArrFollow(notificationListModel.getArrFollow());
            filterdListmodel.setArrAnnouncements(notificationListModel.getArrAnnouncements());
            List<ArrAnnouncement> filteredList = new ArrayList<>();
            for (ArrAnnouncement row : notificationListModel.getArrAnnouncements()) {
                // name match condition. this might differ depending on your requirement
                if (row.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(row);
                }
            }

            //calling a method of the adapter class and passing the filtered list
            if (text.length() > 0) {
                filterdListmodel.setArrAnnouncements(filteredList);
            } else {
                filterdListmodel.setArrAnnouncements(notificationListModel.getArrAnnouncements());
            }
            notificationMessAdapter.filterList(filterdListmodel);
            if (notificationMessAdapter.getItemCount() != 0) {
                txtAnnouncments.setVisibility(View.VISIBLE);
                return false;
            } else {
                txtAnnouncments.setVisibility(View.GONE);
                return true;
            }
//            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean filterPostAcivities(String text) {
        //new array list that will hold the filtered data
        try {
//            NotificationListModel filterdListmodel = notificationListModel;
            NotificationListModel filterdListmodel = new NotificationListModel();
            filterdListmodel.setArrFollow(notificationListModel.getArrFollow());
            filterdListmodel.setArrAnnouncements(notificationListModel.getArrAnnouncements());
            List<ArrPost> filteredList = new ArrayList<>();
            for (ArrPost row : notificationListModel.getArrPost()) {
                // name match condition. this might differ depending on your requirement
                if (row.getUsername().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(row);
                }
            }
            filterdListmodel.setArrPost(filteredList);
            //calling a method of the adapter class and passing the filtered list
            if (text.length() > 0) {
                notificationPostActivityAdapter.filterList(filterdListmodel);
            } else {
                notificationPostActivityAdapter.filterList(notificationListModel);
            }

            if (notificationPostActivityAdapter.getItemCount() != 0) {
                txtPostActivities.setVisibility(View.VISIBLE);
                return false;
            } else {
                txtPostActivities.setVisibility(View.GONE);
                return true;
            }
//            return true;
        } catch (Exception e) {
            return false;
        }//        return txtPostActivities.getVisibility();
    }

    private void getNotificationList() {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<NotificationListModel> notificationListCall = service.getNotificationList(HelperPreferences.get(getActivity()).getString(UID));
        notificationListCall.enqueue(new Callback<NotificationListModel>() {
            @Override
            public void onResponse(Call<NotificationListModel> call, Response<NotificationListModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    Log.e("GetNotificationList", "onResponse: " + response.body().getStatus());
                    notificationListModel = response.body();
                    setUpFollowerList(response.body());
                    setUpPostActivityList(response.body());
                    setUpAnnouncmentList(response.body());

                    if (response.body().getListReadStatus().equalsIgnoreCase("1")) {
                        ((NotificationFragment) getParentFragment()).notifivcationImage.setVisibility(View.VISIBLE);
                    } else {
                        ((NotificationFragment) getParentFragment()).notifivcationImage.setVisibility(View.GONE);

                    }
                } else {
                    try {
                        Log.e("GetNotificationList", "onResponse: errorBody" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationListModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("GetNotificationList", "onFailure: " + t.getMessage());
            }
        });
    }
}
