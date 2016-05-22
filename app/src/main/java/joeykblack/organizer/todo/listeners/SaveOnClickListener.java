package joeykblack.organizer.todo.listeners;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import joeykblack.organizer.todo.QueueActivity;
import joeykblack.organizer.todo.database.TaskContract;
import joeykblack.organizer.todo.R;
import joeykblack.organizer.todo.database.TaskDbHelper;

public class SaveOnClickListener implements View.OnClickListener {
    private static final String TAG = "SaveClickListenerSave";
    private final Context myContext;
    private TaskDbHelper mHelper;

    public SaveOnClickListener(Context myContext) {
        this.myContext = myContext;
        mHelper = new TaskDbHelper(this.myContext);
    }

    @Override
    public void onClick(View view) {
        View parent = (View) view.getParent();

        EditText editTaskTitle = (EditText) parent.findViewById(R.id.edit_task_title);
        String task = String.valueOf(editTaskTitle.getText());

        EditText editTaskPriority = (EditText) parent.findViewById(R.id.edit_task_priority);
        int priority = Integer.valueOf( String.valueOf(editTaskPriority.getText()) );

        Date taskDate = null;
        Button editTaskDate = (Button) parent.findViewById(R.id.edit_task_date);
        String dateString = String.valueOf(editTaskDate.getText());

        Log.d(TAG, "Insert into db: " + task + " priority[" + priority + "] date[" + dateString + "]");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, priority);
        values.put(TaskContract.TaskEntry.COL_TASK_DATE, dateString);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Intent gotoTaskDetail = new Intent(myContext, QueueActivity.class);
        myContext.startActivity(gotoTaskDetail);
    }
}