package com.app.admin.sellah.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.admin.sellah.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Demo extends Fragment {
  RecyclerView recyclerView_liveChat;
  Button btnSend_liveModule;

    public Demo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_demo, container, false);

        btnSend_liveModule = view.findViewById(R.id.btnSend_liveModule);

       /* LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_liveChat.setLayoutManager(layoutManager);
        LiveStreamChatAdapter videoCategoriesAdpt = new LiveStreamChatAdapter(getActivity(),mo);
        recyclerView_liveChat.setAdapter(videoCategoriesAdpt);*/

        btnSend_liveModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    attemptSend();
            }
        });



        return view;
    }

    /*private void attemptSend() {

        String message = "hellloooo";
        if (TextUtils.isEmpty(message)) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
      //  edtMessage.setText("");

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
            jsonObject.put("receiver_id", otherUserId);
            jsonObject.put("message", message);
            jsonObject.put("sender_image", "https://ellahppdiag.blob.core.windows.net/chatimages/21112018015235_1542808355.jpg");
            jsonObject.put("created_at", str);
            jsonObject.put("image_url", "");
            jsonObject.put("type", "t");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("msg_params", jsonObject.toString());
        mSocket.emit("new_message", jsonObject);
    }*/

}
