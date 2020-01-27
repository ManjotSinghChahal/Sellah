package com.app.admin.sellah.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.MainActivity;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Legalpdf_view extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.pdfv)
    PDFView pdfv;


    public Legalpdf_view() {

    }

    ArrayList<String> optionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.legal_section_pdf_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        hideSearch();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.e( "onCreateView: ", bundle.getString("data"));
            if (bundle.getString("data").equalsIgnoreCase("Terms of Service")) {
                pdfv.fromAsset("terms_and_services.pdf").load();
                ((MainActivity) getActivity()).text_sell.setText("Terms of Service");
            }
            if (bundle.getString("data").equalsIgnoreCase("Privacy Policy")) {
                pdfv.fromAsset("privacy_policy.pdf").load();
                ((MainActivity) getActivity()).text_sell.setText("Privacy Policy");
            }
            if (bundle.getString("data").equalsIgnoreCase("Disputes Policy")) {
                pdfv.fromAsset("disputes.pdf").load();
                ((MainActivity) getActivity()).text_sell.setText("Disputes Policy");
            }
            if (bundle.getString("data").equalsIgnoreCase("Buyer & Seller Standards")) {
                pdfv.fromAsset("buyer_and_seller.pdf").load();
                ((MainActivity) getActivity()).text_sell.setText("Buyer & Seller Standards");
            }
            if (bundle.getString("data").equalsIgnoreCase("Prohibited Content")) {
                pdfv.fromAsset("prohibited_policy.pdf").load();
                ((MainActivity) getActivity()).text_sell.setText("Prohibited Content");
            }


        }


        return view;
    }

    private void setupOptions() {

        optionList = new ArrayList<>();
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

//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(7);

    }

    public String getAssetsPdfPath(Context context,String name) {
        String filePath = context.getFilesDir() + File.separator + name;
        File destinationFile = new File(filePath);

        try {
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            InputStream inputStream = context.getAssets().open("myFile.pdf");
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(context.getClass().getSimpleName(), "Error.");
        }

        return destinationFile.getPath();
    }
}
