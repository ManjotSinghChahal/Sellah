/*
package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.admin.sellah.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteSearchAdapter extends ArrayAdapter<String> {

    private ArrayList<String> mOriginalValues;
    Context mContext;

    public AutoCompleteSearchAdapter(Context context, int resource, ArrayList<String> objects) {

        super(context, resource, objects);
        mOriginalValues = objects;
        mContext=context;

    }

    @Override
    public int getCount() {
        return mOriginalValues.size();
    }

    @Override
    public String getItem(int position) {
        return mOriginalValues.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_autocomplete_text_design, null,false);

        TextView textView= rowView.findViewById(R.id.text1);

        textView.setText(mOriginalValues.get(position));

        return rowView;
    }

}
*/
