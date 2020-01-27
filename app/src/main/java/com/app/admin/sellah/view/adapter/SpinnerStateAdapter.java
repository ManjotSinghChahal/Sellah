package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.SpinnerStateCheck;

import java.util.ArrayList;
import java.util.List;

public class SpinnerStateAdapter extends ArrayAdapter<SpinnerStateCheck> {
    private Context mContext;
    private ArrayList<SpinnerStateCheck> listState;
    boolean isMultipleSelectable = false;
    OnSelectionCallback selectionCallback;
    String instanseOf;
    String selectedoptions = "";
    ArrayList<String> selectedoptionsArray = new ArrayList<>();


    public SpinnerStateAdapter(Context context, int resource, List<SpinnerStateCheck> objects, boolean isMultipleSelectable, String instanceOf, OnSelectionCallback selectionCallback) {
        super(context, resource, objects);
        this.mContext = context;
        this.isMultipleSelectable = isMultipleSelectable;
        this.listState = (ArrayList<SpinnerStateCheck>) objects;
        this.selectionCallback = selectionCallback;
        this.instanseOf = instanceOf;
    }


    public SpinnerStateAdapter(Context context, int resource, List<SpinnerStateCheck> objects, boolean isMultipleSelectable, String instanceOf, ArrayList<String> selection, OnSelectionCallback selectionCallback) {
        super(context, resource, objects);
        this.mContext = context;
        this.isMultipleSelectable = isMultipleSelectable;
        this.listState = (ArrayList<SpinnerStateCheck>) objects;
        this.selectionCallback = selectionCallback;
        this.instanseOf = instanceOf;

    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner__item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.rl_spinner_root = convertView.findViewById(R.id.rl_spinner_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e( "getCustomView: ", listState.get(position).getTitle());
        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        if (position > 0) {
            if (instanseOf.equalsIgnoreCase("condition")) {
                /*if (listState.get(position).isSelected()) {
                    if (!selectedoptionsArray.contains("Y")) {
                        selectedoptionsArray.add("Y");
                    }
                } else {
                    if (selectedoptionsArray.contains("Y")) {
                        selectedoptionsArray.remove("Y");
                    }
                }*/
            } else {
                if (listState.get(position).isSelected()) {
                    if (!selectedoptionsArray.contains(listState.get(position).getTitle())) {
                        selectedoptionsArray.add(listState.get(position).getTitle());
                    }
                } else {
                    if (selectedoptionsArray.contains(listState.get(position).getTitle())) {
                        selectedoptionsArray.remove(listState.get(position).getTitle());
                    }
                }
            }
        }

       /* holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listState.get(position).setSelected(true);
            }
        });*/
       /* holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
            }
        });*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    listState.get(position).setSelected(true);
                if (!isMultipleSelectable) {
                    for (int i = 0; i < listState.size(); i++) {
                        if (i == position) {
                            listState.get(i).setSelected(true);
                            if (!selectedoptionsArray.contains(listState.get(i).getTitle())) {
                                if (!listState.get(i).getTitle().equalsIgnoreCase(listState.get(0).getTitle())) {
                                    if (!selectedoptionsArray.contains(listState.get(i).getTitle())) {
                                        selectedoptionsArray.add(listState.get(i).getTitle());
                                    }

                                }
                            }
                        } else {
                            selectedoptionsArray.remove(listState.get(i).getTitle());
                            listState.get(i).setSelected(false);
                        }
                    }
                    selectedoptions = TextUtils.join(",", selectedoptionsArray);
                    selectionCallback.onSelection(selectedoptions, instanseOf);
                } else {
                    for (int i = 0; i < listState.size(); i++) {
                        if (instanseOf.equalsIgnoreCase("condition")) {
                            if (!holder.mCheckBox.isChecked()) {
                                if (!selectedoptionsArray.contains(listState.get(position).getTitle())) {
                                    if (!listState.get(position).getTitle().equalsIgnoreCase(listState.get(0).getTitle())) {
                                        if (listState.get(position).getTitle().equalsIgnoreCase("New")) {
                                            if (!selectedoptionsArray.contains("N")) {
                                                selectedoptionsArray.add("N");
                                            }
                                        } else {
                                            if (!selectedoptionsArray.contains("U")) {
                                                selectedoptionsArray.add("U");
                                            }
                                        }
                                    }
                                }
                                listState.get(position).setSelected(true);
                            } else {
                                if (listState.get(position).getTitle().equalsIgnoreCase("New")) {
                                    selectedoptionsArray.remove("N");
                                } else {
                                    selectedoptionsArray.remove("U");
                                }
//                                selectedoptionsArray.remove(listState.get(position).getTitle());
                                listState.get(position).setSelected(false);
                            }
                        } else {
                            if (!holder.mCheckBox.isChecked()) {
                                if (!selectedoptionsArray.contains(listState.get(position).getTitle())) {
                                    if (!listState.get(position).getTitle().equalsIgnoreCase(listState.get(0).getTitle())) {
                                        selectedoptionsArray.add(listState.get(position).getTitle());
                                    }
                                }
                                listState.get(position).setSelected(true);
                            } else {
                                selectedoptionsArray.remove(listState.get(position).getTitle());
                                listState.get(position).setSelected(false);
                            }
                        }

                    }
                    selectedoptions = TextUtils.join(",", selectedoptionsArray);
                    selectionCallback.onSelection(selectedoptions, instanseOf);
                }
                notifyDataSetChanged();
            }

        });

        return convertView;
    }

    public class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
        private RelativeLayout rl_spinner_root;
    }

    public interface OnSelectionCallback {
        public void onSelection(String selection, String instanceOf);
    }
}
