package joeykblack.organizer.todo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.fragment.DatePickerFragment;
import joeykblack.organizer.todo.listeners.SaveOnClickListener;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "TaskDetailActivity";

    private TaskDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new SaveOnClickListener(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public TaskDbHelper getmHelper() {
        return mHelper;
    }
}
