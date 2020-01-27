package com.app.admin.sellah.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.adapter.LegalSectionListAdapter;
import com.app.admin.sellah.view.activities.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LegalFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.list_options)
    ListView optionsList;

    @BindView(R.id.txt_body)
    TextView bodyText;

    LegalSectionListAdapter adapter;

    public LegalFragment (){

    }

    ArrayList<String> optionList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.legal_section_fragment, container,false);
        unbinder=ButterKnife.bind(this,view);
        hideSearch();
        setupOptions();
        bodyText.setText(getResources().getString(R.string.legal_body));


        adapter=new LegalSectionListAdapter(getActivity(),optionList);
        optionsList.setAdapter(adapter);


        return view;
    }

    private void setupOptions() {

        optionList=new ArrayList<>();
        optionList.add("Terms of Service");
        optionList.add("Privacy Policy");
        optionList.add("Disputes Policy");
        optionList.add("Buyer & Seller Standards");
        optionList.add("Prohibited Content");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Legal Stuff");
//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(7);

    }
}
