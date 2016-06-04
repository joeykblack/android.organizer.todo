package joeykblack.organizer.todo.database;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;

public class TaskContract {
    public static final String DB_NAME = "joeykblack.organizer.todo.db";
    public static final int DB_VERSION = 6;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DISPLAY = new SimpleDateFormat("MM/dd/yyyy");
    public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    public static final String DATE_PATTERN_DISPLAY = "\\d{2}/\\d{2}/\\d{4}";

    public static final int PRIORITY_MIN = 1;
    public static final int PRIORITY_MAX = 10;
    public static final int PRIORITY_STEP = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_PRIORITY = "priority";
        public static final String COL_TASK_DATE = "dueDate";
    }
}