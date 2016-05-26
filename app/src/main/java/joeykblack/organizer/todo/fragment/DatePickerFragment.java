package joeykblack.organizer.todo.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import joeykblack.organizer.todo.R;

public class DatePickerFragment extends DialogFragment
                            implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private Date date;

    public DatePickerFragment setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        if ( date != null ) {
            c.setTime(date);
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++; // android months are zero based
        Button datePicker = (Button) getActivity().findViewById(R.id.edit_task_date);
        datePicker.setText(year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day));
    }

}