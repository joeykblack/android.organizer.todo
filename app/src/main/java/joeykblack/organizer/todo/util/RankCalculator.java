package joeykblack.organizer.todo.util;

import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import joeykblack.organizer.todo.model.Task;

/**
 * Created by joey on 5/22/2016.
 */
public class RankCalculator {
    private static final String TAG = RankCalculator.class.getSimpleName();

    public static long getRank(Task task) {
        Log.d(TAG, ": Ranking " + task.getTitle());

        long rank = task.getPriority();
        Log.d(TAG, "\t priority: " + rank);

        long daysTillDue = getDaysTillDue(task);
        Log.d(TAG, "\t daysTillDue: " + daysTillDue);

        rank -= daysTillDue;
        Log.d(TAG, "\t final: " + rank);

        return rank;
    }

    private static long getDaysTillDue(Task task) {
        long days = 0;
        if ( task.getDate() != null ) {
            Date now = new Date(); // TODO: get date without time
            long diff = task.getDate().getTime() - now.getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        return days;
    }

}
