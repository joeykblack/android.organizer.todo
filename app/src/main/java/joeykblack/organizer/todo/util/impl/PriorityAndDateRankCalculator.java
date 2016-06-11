package joeykblack.organizer.todo.util.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.RankCalculator;

/**
 * Created by joey on 5/22/2016.
 */
public class PriorityAndDateRankCalculator implements RankCalculator {
    private static final String TAG = PriorityAndDateRankCalculator.class.getSimpleName();

    @Override
    public long getRank(Task task) {
        Log.d(TAG, ": Ranking " + task.getTitle());

        long rank = task.getPriority();
        Log.d(TAG, "\t priority: " + rank);

        long daysTillDue = getDaysTillDue(task);
        Log.d(TAG, "\t daysTillDue: " + daysTillDue);

        long moddedDaysTillDue = modDaysTillDue(daysTillDue);
        Log.d(TAG, "\t moddedDaysTillDue: " + moddedDaysTillDue);

        rank += moddedDaysTillDue;
        Log.d(TAG, "\t final: " + rank);

        return rank;
    }

    private long modDaysTillDue(long daysTillDue) {
        return (daysTillDue * -1) + 7;
    }

    private long getDaysTillDue(Task task) {
        long days = 0;
        if ( task.getDate() != null ) {
            long diff = task.getDate().getTime() - getToday().getTimeInMillis();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        return days;
    }

    @NonNull
    private Calendar getToday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }
}
