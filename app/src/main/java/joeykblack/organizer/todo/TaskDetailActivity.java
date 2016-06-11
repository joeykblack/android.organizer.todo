package joeykblack.organizer.todo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import joeykblack.organizer.todo.database.TaskContract;
import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.fragment.DatePickerFragment;
import joeykblack.organizer.todo.listeners.SaveOnClickListener;
import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.DateUtil;
import joeykblack.organizer.todo.util.impl.ContractDateUtil;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "TaskDetailActivity";

    private TaskDbHelper mHelper;
    private Task task;
    private DateUtil dateUtil = new ContractDateUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);

        // Setup Priority NumberPicker
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.edit_task_priority);
        setRange(numberPicker);
        numberPicker.setWrapSelectorWheel(false);

        // Save button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new SaveOnClickListener(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Prepopulate if Editing
        Task task = (Task) getIntent().getSerializableExtra(Task.TAG);
        if ( task != null ) {
            this.task = task;

            // Title
            EditText editTaskTitle = (EditText) this.findViewById(R.id.edit_task_title);
            editTaskTitle.setText(task.getTitle());

            // Priority
            NumberPicker editTaskPriority = (NumberPicker) this.findViewById(R.id.edit_task_priority);
            editTaskPriority.setValue( ( task.getPriority() - TaskContract.PRIORITY_MIN ) / TaskContract.PRIORITY_STEP );

            // Date
            Button editTaskDate = (Button) this.findViewById(R.id.edit_task_date);
            if ( task.getDate() != null ) {
                editTaskDate.setText( dateUtil.serializeDateDisplay( task.getDate() ) );
            }

            Log.d(TAG, "Loading: " + task.getTitle() + " priority[" + task.getPriority() + "] date[" + task.getDate() + "]");
        }
    }

    private void setRange(NumberPicker numberPicker) {

        numberPicker.setMinValue( 0 );

        int max = (TaskContract.PRIORITY_MAX - TaskContract.PRIORITY_MIN) / TaskContract.PRIORITY_STEP;
        numberPicker.setMaxValue( max );

        String[] displayValues = new String[max + 1];
        for (int i = 0; i <= max; i++) {
            displayValues[i] = String.valueOf( ( i * TaskContract.PRIORITY_STEP ) + TaskContract.PRIORITY_MIN );
        }

        numberPicker.setDisplayedValues(displayValues);

        numberPicker.setValue( (int) Math.ceil( max / 2 ) );

    }


    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment().setDate(task == null ? null : task.getDate());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public TaskDbHelper getmHelper() {
        return mHelper;
    }

    public Task getTask() {
        return task;
    }
}
