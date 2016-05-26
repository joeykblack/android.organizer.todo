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

import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.fragment.DatePickerFragment;
import joeykblack.organizer.todo.listeners.SaveOnClickListener;
import joeykblack.organizer.todo.model.Task;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "TaskDetailActivity";

    private TaskDbHelper mHelper;
    private Task task;

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

        Task task = (Task) getIntent().getSerializableExtra(Task.TAG);
        if ( task != null ) {
            this.task = task;
            EditText editTaskTitle = (EditText) this.findViewById(R.id.edit_task_title);
            editTaskTitle.setText(task.getTitle());
            EditText editTaskPriority = (EditText) this.findViewById(R.id.edit_task_priority);
            editTaskPriority.setText( String.valueOf( task.getPriority() ) );
            Button editTaskDate = (Button) this.findViewById(R.id.edit_task_date);
            if ( task.getDate() != null ) {
                editTaskDate.setText( TaskDbHelper.serializeDate( task.getDate() ) );
            }
            Log.d(TAG, "Loading: " + task.getTitle() + " priority[" + task.getPriority() + "] date[" + task.getDate() + "]");
        }
    }


    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment().setDate(task.getDate());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public TaskDbHelper getmHelper() {
        return mHelper;
    }

    public Task getTask() {
        return task;
    }
}
