package com.app.admin.sellah.Extras;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vishal on 7/4/2017.
 */

public class Datedialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView dob;
       int year1;
       int month1;
        int day1;
    Calendar calendar;
public Dialog onCreateDialog(Bundle savedInstanceState)
{
     calendar = Calendar.getInstance();

        year1 =   calendar.get(Calendar.YEAR);
        month1 = calendar.get(Calendar.MONTH);
         day1 =    calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog dialog= new DatePickerDialog(getActivity(), date, calendar
            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));

    //Log.e( "onCreateDialog: ",calendar.getTime() );
    String myDate = "2019/12/30 18:10:45";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = null;
    try {
        date = sdf.parse(myDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    long millis = System.currentTimeMillis();

    long val =  DateUtils.YEAR_IN_MILLIS;

    long value = (13*val)+(DateUtils.DAY_IN_MILLIS*16);


    dialog.getDatePicker().setMaxDate(millis-value);
  //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


   // dialog.show();

  return dialog;

}


    public void seteditext(View v)
    {
        dob = (TextView) v;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i +"-"+i1+1+"-"+i2;
        StringBuilder builder = new StringBuilder();
        builder.append(i);
        builder.append(i1+1);
        builder.append(i2);
        Log.e( "onDateSet: ",""+i );
        Log.e( "onDateSet: ",""+i1 );
        Log.e( "onDateSet: ",""+i2 );
       // dob.setText(builder.toString());




    }

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(calendar.getTime()));
    }


}
