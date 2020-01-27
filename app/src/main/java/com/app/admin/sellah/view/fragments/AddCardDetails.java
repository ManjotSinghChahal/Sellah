package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.app.admin.sellah.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static butterknife.OnTextChanged.Callback.BEFORE_TEXT_CHANGED;

@SuppressLint("ValidFragment")
public class AddCardDetails extends Fragment {

    @BindView(R.id.txt_test)
    TextView txtTest;
    Unbinder unbinder1;
    String status;
    @BindView(R.id.edt_card_name)
    EditText edtCardName;
    @BindView(R.id.cv_card_name)
    CardView cvCardName;
    @BindView(R.id.edt_card_number)
    EditText edtCardNumber;
    @BindView(R.id.cv_card_number)
    CardView cvCardNumber;
    @BindView(R.id.edt_card_exp)
    EditText edtCardExp;
    @BindView(R.id.cv_card_exp)
    CardView cvCardExp;
    @BindView(R.id.edt_cvv)
    EditText edtCvv;
    @BindView(R.id.cv_pin)
    CardView cvPin;
    private static final char space = ' ';
    private TextWatcher nameTextWatcher;
    private TextWatcher cardnumberTextWatcher;
    private TextWatcher cardExpTextWatcher;
    private TextWatcher cardCvvTextWatcher;
    private boolean lock;

    private static final String EXP_DATE_REGAX = "(0[1-9]|1[0-2])[0-9]{2}";
    Unbinder unbinder;
    View view;
    ArrayList<CardView> cardViews;
    ArrayList<TextWatcher> textWatchers;
    ArrayList<EditText> editTexts;

    OnTextWacherCallBack callBack;
    String data;
    private int previousLength;
    String lastInput="";

    @SuppressLint("ValidFragment")
    public AddCardDetails(String status, OnTextWacherCallBack callBack, String data) {
        this.status = status;
        this.callBack = callBack;
        this.data=data;
        // Required empty public constructor
    }

    /*@OnTextChanged(value = R.id.edt_card_exp, callback = BEFORE_TEXT_CHANGED)
    void beforeExpireEtChanged() {
        previousLength = edtCardExp.getText().toString().length();
    }

    @OnTextChanged(R.id.edt_card_exp)
    void autoFixAndMoveToNext() {
        if (before == 1 && count == 2 && s.charAt(s.length()-1) != '/') {
            edtCardExp.setText(edtCardExp.getText().toString() + "/");
        }
        if (edtCardExp.getText().toString().toCharArray().length < 3) {
            edtCardExp.setText(edtCardExp.getText().toString().replace("/", ""));
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_card_details, container, false);
            unbinder = ButterKnife.bind(this, view);
            cardViews = new ArrayList<>();
            textWatchers = new ArrayList<>();
            editTexts = new ArrayList<>();

            cardViews.add(cvCardName);
            cardViews.add(cvCardNumber);
            cardViews.add(cvCardExp);
            cardViews.add(cvPin);

            editTexts.add(edtCardName);
            editTexts.add(edtCardNumber);
            editTexts.add(edtCardExp);
            editTexts.add(edtCvv);

            nameTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    callBack.onNameChangeListner(s.toString());
                }
            };

            cardnumberTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (lock || s.length() > 19) {
                        return;
                    }
                    lock = true;
                    for (int i = 4; i < s.length(); i += 5) {
                        if (s.toString().charAt(i) != ' ') {
                            s.insert(i, " ");
                        }
                    }
                    lock = false;
                    callBack.oncardNumberChangeListner(s.toString());
                    Log.e("Lock_bool", lock + "");
                }
            };

            cardExpTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Remove spacing char

                    String input = s.toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.GERMANY);
                    Calendar expiryDateDate = Calendar.getInstance();
                    try {
                        expiryDateDate.setTime(formatter.parse(input));
                    } catch (ParseException e) {

                        if (s.length() == 2 && !lastInput.endsWith("/")) {
                            int month = Integer.parseInt(input.replace("/",""));
                            if (month <= 12) {
                                edtCardExp.setText(edtCardExp.getText().toString() + "/");
                                edtCardExp.setSelection(edtCardExp.getText().length());
                            }else{
                                edtCardExp.setError("Invalid month");
                            }
                        }else if (s.length() == 2 && lastInput.endsWith("/")) {
                            int month = Integer.parseInt(input.replace("/",""));
                            if (month <= 12) {
                                edtCardExp.setText(edtCardExp.getText().toString().substring(0,1));
                            }
                        }
                        lastInput = edtCardExp.getText().toString();}

                    callBack.oncardExpChangeListner(s.toString());
                }
            };

            cardCvvTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    callBack.oncardCvvChangeListner(s.toString());
                }
            };

            textWatchers.add(nameTextWatcher);
            textWatchers.add(cardnumberTextWatcher);
            textWatchers.add(cardExpTextWatcher);
            textWatchers.add(cardCvvTextWatcher);

        } else {
            unbinder = ButterKnife.bind(this, view);
        }


        unbinder1 = ButterKnife.bind(this, view);
        switch (status) {
            case "1":
                showView(0);
//                txtTest.setText("this is first");
                break;
            case "2":
                showView(1);
//                txtTest.setText("this is second");
                break;
            case "3":
                showView(2);
//                txtTest.setText("this is third");
                break;
            case "4":
                showView(3);
//                txtTest.setText("this is 4th");
                break;
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showView(int pos) {
        for (int i = 0; i < cardViews.size(); i++) {
            if (i == pos) {
                cardViews.get(i).setVisibility(View.VISIBLE);
                editTexts.get(i).addTextChangedListener(textWatchers.get(i));
                editTexts.get(i).setText(data);
            } else {
                editTexts.get(i).setText(data);
                editTexts.get(i).removeTextChangedListener(textWatchers.get(i));
                cardViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    public interface OnTextWacherCallBack {
        void onNameChangeListner(String name);

        void oncardNumberChangeListner(String number);

        void oncardExpChangeListner(String expDate);

        void oncardCvvChangeListner(String cvv);
    }

}
