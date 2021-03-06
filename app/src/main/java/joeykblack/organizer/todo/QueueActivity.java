package joeykblack.organizer.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import joeykblack.organizer.todo.database.TaskContract;
import joeykblack.organizer.todo.database.TaskDbHelper;
import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.ClusterHandler;
import joeykblack.organizer.todo.util.DateUtil;
import joeykblack.organizer.todo.util.impl.ContractDateUtil;
import joeykblack.organizer.todo.util.impl.KDEClusterHandler;

public class QueueActivity extends AppCompatActivity {
    private static final String TAG = QueueActivity.class.getSimpleName();

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<Task> mAdapter;
    private List<Task> mTaskList;

    private ClusterHandler clusterHandler = new KDEClusterHandler();
    private DateUtil dateUtil = new ContractDateUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context myContext = this;
        setContentView(R.layout.activity_queue);

        // Edit action
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                Log.d(TAG, "Edit task: " + task.getTitle());
                Intent gotoTaskDetail = new Intent(myContext, TaskDetailActivity.class);
                gotoTaskDetail.putExtra(Task.TAG, task);
                myContext.startActivity(gotoTaskDetail);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHelper = new TaskDbHelper(this);

        updateUI();

        // Add action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTaskDetail = new Intent(myContext, TaskDetailActivity.class);
                myContext.startActivity(gotoTaskDetail);
            }
        });
    }



    /**
     * Update UI
     */
    public void updateUI() {
        Log.d(TAG, "updaateUI");

        List<Task> taskList = getTasks();

        Collections.sort(taskList);

        taskList = clusterHandler.labelTasks(taskList);

        updateAdapter(taskList);
    }

    @NonNull
    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        // Get Tasks from DB
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_PRIORITY, TaskContract.TaskEntry.COL_TASK_DATE},
                null, null, null, null, null);

        // Create Task list
        while (cursor.moveToNext()) {
            taskList.add(new Task()
                    .setId(Long.parseLong(getValue(cursor, TaskContract.TaskEntry._ID)))
                    .setTitle(getValue(cursor, TaskContract.TaskEntry.COL_TASK_TITLE))
                    .setPriority(Integer.parseInt(getValue(cursor, TaskContract.TaskEntry.COL_TASK_PRIORITY)))
                    .setDate(dateUtil.parseDate(getValue(cursor, TaskContract.TaskEntry.COL_TASK_DATE)))
            );
        }
        return taskList;
    }

    // Get value for key from cursor
    private String getValue(Cursor cursor, String key) {
        int idx = cursor.getColumnIndex(key);
        String value = cursor.getString(idx);
        return value;
    }

    private void updateAdapter(List<Task> taskList) {
        // Update Adapter
        if (mAdapter == null) {
            mTaskList = taskList;
            Log.d(TAG, "New adapter [" + mTaskList.size() + "]");
            mAdapter = new ArrayAdapter<Task>(this,
                    R.layout.item_queue,
                    R.id.task_title,
                    mTaskList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView details = (TextView) view.findViewById(R.id.task_details);
                    Task item = getItem(position);
                    details.setText(item.getDetails());
                    return view;
                }
            };
            mTaskListView.setAdapter(mAdapter);
        } else {
            mTaskList.clear();;
            mTaskList.addAll(taskList);
            Log.d(TAG, "Update adapter [" + mTaskList.size() + "]");
            mAdapter.notifyDataSetChanged();
        }
    }



    /*
     * Button methods
     */

    // onClick of Done button
    public void deleteTask(View view) {
        View parent = (View) view.getParent();

        AdapterView<?> adapterView = (AdapterView<?>) parent.getParent();
        int position = adapterView.getPositionForView(parent);
        Task task = (Task) adapterView.getAdapter().getItem(position);

        // Remove task from DB
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();

        updateUI();
    }



    /*
     * Other mehtods
     */


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
        boolean result = false;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("About");
            alertDialog.setMessage("This is a simple to do list app that sorts tasks by priority and due date."
                + "\n"
                + "\nCreated by: joeykblack@gmail.com"
                + "\n"
                + "\nVersion: " + BuildConfig.VERSION_NAME + " [" + BuildConfig.VERSION_CODE + "]"
                + "\n"
                + "\nSite: joeykblack.github.com");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            result = true;
        }
        else {
            result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    public TaskDbHelper getmHelper() {
        return mHelper;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
