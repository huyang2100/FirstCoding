package net.sourceforge.simcpux.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import net.sourceforge.simcpux.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDateFragment extends DialogFragment {

    public static final String KEY_DATE = "date";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        final DatePicker datePicker = v.findViewById(R.id.datepicker);
        Date date = (Date) getArguments().getSerializable(KEY_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity())
                .setTitle("犯罪日期：")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Date d = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()).getTime();

                            Intent intent = new Intent();
                            intent.putExtra(KEY_DATE, d);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .create();
    }

    public static CrimeDateFragment newInstance(Date date) {
        CrimeDateFragment crimeDateFragment = new CrimeDateFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATE, date);
        crimeDateFragment.setArguments(args);
        return crimeDateFragment;
    }

}
