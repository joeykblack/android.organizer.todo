package joeykblack.organizer.todo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_TASK_PRIORITY + " INTEGER, " +
                TaskContract.TaskEntry.COL_TASK_DATE + " TEXT" + ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    public static Date parseDate(String dateString) {
        Date date = null;
        try {
            if ( dateString != null && dateString.trim().equals("")==false ) {
                if ( Pattern.matches(TaskContract.DATE_PATTERN, dateString) ) {
                    date = TaskContract.DATE_FORMAT.parse(dateString);
                }
                else if ( Pattern.matches(TaskContract.DATE_PATTERN_DISPLAY, dateString) ) {
                    date = TaskContract.DATE_FORMAT_DISPLAY.parse(dateString);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String serializeDateDatabase(Date date) {
        String dateString = "";
        if ( date != null ) {
            dateString = TaskContract.DATE_FORMAT.format( date );
        }
        return dateString;
    }
    public static String serializeDateDisplay(Date date) {
        String dateString = "";
        if ( date != null ) {
            dateString = TaskContract.DATE_FORMAT_DISPLAY.format( date );
        }
        return dateString;
    }
    public static boolean isDate(String dateString) {
        return Pattern.matches(TaskContract.DATE_PATTERN, dateString) || Pattern.matches(TaskContract.DATE_PATTERN_DISPLAY, dateString);
    }
}