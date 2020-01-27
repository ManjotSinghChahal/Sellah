package com.app.admin.sellah.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AboutUsFragment extends Fragment {

    @BindView(R.id.txt_mediakit_dec)
    TextView txtMediaKitDec;

    private Unbinder unbinder;

    public AboutUsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aboutUsView=inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, aboutUsView);
        hideSearch();
        txtMediaKitDec.setLinkTextColor(Color.BLUE); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Normal Link", Toast.LENGTH_SHORT).show();
            }

        };
        ClickableSpan gmailClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "gmail Link", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"admin@sellah.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Query");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }

        };



        makeLinks(txtMediaKitDec, new String[] {
               "admin@sellah.com"
        }, new ClickableSpan[] {
                normalLinkClickSpan,gmailClick
        });
        /*SpannableString ss = new SpannableString(getString(R.string.media_kit));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View txtMediaKitDec) {
//                startActivity(new Intent(getActivity(), NextActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtMediaKitDec.setText(ss);
        txtMediaKitDec.setMovementMethod(LinkMovementMethod.getInstance());
        txtMediaKitDec.setHighlightColor(Color.TRANSPARENT);*/
        // Inflate the layout for this fragment
        return aboutUsView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("About Us");
//        ((MainActivity) getActivity()).rlBack.setVisibility("Profile");
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).profile.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(7);

    }
    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());


        Log.e( "makeLinks: ",""+ links.length);
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

}
