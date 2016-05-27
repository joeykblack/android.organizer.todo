package joeykblack.organizer.todo.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import joeykblack.organizer.todo.model.Task;

/**
 * Created by joey on 5/27/2016.
 */
public class ClusterUtil {
    private static final String TAG = ClusterUtil.class.getSimpleName();

    private static final double H = 0.1d;

    public static List<Task> getGroups(List<Task> tasks, int showGroupCount) {
        long[] ranks = new long[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            ranks[i] = tasks.get(i).getRank();
            Log.d(TAG, "Task " + tasks.get(i).toString() + " [" + ranks[i] + "]");
        }

        List<Integer> groups = getGroupsUsingKDE(ranks);
        if ( showGroupCount > groups.size() ) {
            showGroupCount = groups.size();
        }

        return tasks.subList(0, groups.get(showGroupCount-1));
    }

    /**
     * Kernel density estimation
     * @param values
     * @return groups[groupNumber] = end of group index + 1
     *  group i = values.subList( groups[i-1], groups[i] )
     */
    private static List<Integer> getGroupsUsingKDE(long[] values) {
        double mean = getMean(values);
        Log.d(TAG, "mean: " + mean);

        double variance = getVariance(values, mean);
        Log.d(TAG, "variance: " + variance);

        long min = getMin(values);
        Log.d(TAG, "min: " + min);

        long max = getMax(values);
        Log.d(TAG, "max: " + max);

        for (long x = min; x <= max; x++) {
            double probability = kde( x, values, mean, variance );
            Log.d(TAG, "kde( rank:" + x + " ) = " + probability);
        }

        List<Integer> groups = new ArrayList<>();
        groups.add(values.length);
        return groups;
    }

    private static double kde(long x, long[] values, double mean, double variance) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            double paramX = ( x - values[i] ) / H;
            total += normal(paramX, mean, variance);
        }
        return total / (values.length * H);
    }

    private static double normal(double x, double mean, double variance) {
        double eExpNumerator  = Math.pow(x - mean, 2);
        double eExpDenominator = 2 * variance;
        double numerator = Math.pow(Math.E, (-1 * eExpNumerator) / eExpDenominator);
        double denominator = Math.sqrt(variance * 2 * Math.PI);
        return numerator / denominator;
    }


    private static double getMean(long[] values) {
        long total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total / values.length;
    }

    private static double getVariance(long[] values, double mean) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            total += Math.pow( ( values[i] - mean ) , 2 );
        }
        return total / values.length;
    }


    private static long getMin(long[] values) {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }
    private static long getMax(long[] values) {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }

}
