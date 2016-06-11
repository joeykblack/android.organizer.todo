package joeykblack.organizer.todo.listeners;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Date;

import joeykblack.organizer.todo.QueueActivity;
import joeykblack.organizer.todo.R;
import joeykblack.organizer.todo.TaskDetailActivity;
import joeykblack.organizer.todo.database.TaskContract;
import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.DateUtil;
import joeykblack.organizer.todo.util.impl.ContractDateUtil;

public class SaveOnClickListener implements View.OnClickListener {
    private static final String TAG = SaveOnClickListener.class.getSimpleName();
    private final TaskDetailActivity myContext;
    private TaskDbHelper mHelper;
    private DateUtil dateUtil = new ContractDateUtil();

    public SaveOnClickListener(TaskDetailActivity myContext) {
        this.myContext = myContext;
        mHelper = new TaskDbHelper(this.myContext);
    }

    @Override
    public void onClick(View view) {
        View parent = (View) view.getParent();

        // Get title
        EditText editTaskTitle = (EditText) parent.findViewById(R.id.edit_task_title);
        String title = String.valueOf(editTaskTitle.getText());

        // Get priority
        NumberPicker editTaskPriority = (NumberPicker) parent.findViewById(R.id.edit_task_priority);
        int priority = ( editTaskPriority.getValue() * TaskContract.PRIORITY_STEP ) + TaskContract.PRIORITY_MIN;

        // Get date
        Button editTaskDate = (Button) parent.findViewById(R.id.edit_task_date);
        String dateString = String.valueOf(editTaskDate.getText());
        Date date = dateUtil.parseDate(dateString);
        dateString = dateUtil.serializeDateDatabase(date);

        // Prepare values
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, title);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, priority);
        values.put(TaskContract.TaskEntry.COL_TASK_DATE, dateString);

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Task task = myContext.getTask();

        if ( task == null ) {
            // Create Task
            Log.d(TAG, "Insert into db: " + title + " priority[" + priority + "] date[" + dateString + "]");
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
        else {
            // Update Task
            Log.d(TAG, "Updating into db: " + title + " priority[" + priority + "] date[" + dateString + "]");
            db.update(TaskContract.TaskEntry.TABLE,
                    values,
                    TaskContract.TaskEntry._ID + " = ?",
                    new String[]{ String.valueOf( task.getId() ) });
        }

        db.close();

        Intent gotoTaskDetail = new Intent(myContext, QueueActivity.class);
        myContext.startActivity(gotoTaskDetail);
    }
}