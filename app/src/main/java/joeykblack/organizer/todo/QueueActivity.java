package joeykblack.organizer.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import joeykblack.organizer.todo.database.TaskContract;
import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.model.Task;

public class QueueActivity extends AppCompatActivity {

    private static final String TAG = "QueueActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<Task> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);

        updateUI();

        final Context myContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gotoTaskDetail = new Intent(myContext, TaskDetailActivity.class);
                myContext.startActivity(gotoTaskDetail);
            }
        });
    }

    public void updateUI() {
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query( TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_PRIORITY, TaskContract.TaskEntry.COL_TASK_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            taskList.add( new Task()
                    .setId( Long.parseLong( getValue(cursor, TaskContract.TaskEntry._ID) ) )
                    .setTitle( getValue(cursor, TaskContract.TaskEntry.COL_TASK_TITLE) )
                    .setPriority( Integer.parseInt( getValue(cursor, TaskContract.TaskEntry.COL_TASK_PRIORITY) ) )
                    .setDate( TaskDbHelper.parseDate( getValue(cursor, TaskContract.TaskEntry.COL_TASK_DATE) ) )
            );
        }
        Collections.sort( taskList );

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<Task>(this,
                    R.layout.item_queue,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getValue(Cursor cursor, String key) {
        int idx = cursor.getColumnIndex(key);
        String value = cursor.getString(idx);
        return value;
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();

        AdapterView<?> adapterView = (AdapterView<?>) parent.getParent();
        int position = adapterView.getPositionForView(parent);
        Task task = (Task) adapterView.getAdapter().getItem(position);

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_queue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public TaskDbHelper getmHelper() {
        return mHelper;
    }
}
