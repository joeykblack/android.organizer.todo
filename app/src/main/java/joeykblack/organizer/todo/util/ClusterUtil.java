package joeykblack.organizer.todo.util;

import android.support.annotation.NonNull;
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
     * @return groups[groupNumber] = start of group groupNumber + 1
     *  group i = values.subList( groups[i-1], groups[i] )
     */
    private static List<Integer> getGroupsUsingKDE(long[] values) {
        List<Integer> groups = new ArrayList<>();

        if ( values.length > 0 ) {
            double mean = getMean(values);
            Log.d(TAG, "mean: " + mean);

            double variance = getVariance(values, mean);
            Log.d(TAG, "variance: " + variance);

            long min = getMin(values);
            Log.d(TAG, "min: " + min);

            long max = getMax(values);
            Log.d(TAG, "max: " + max);

            // Calculate prob across value range in descending order
            List<Double> probabilities = getProbabilities(values, mean, variance, min, max);

            // Find the offset from max of each min
            List<Integer> minimaOffset = findMinimaOffset(probabilities);

            // Calculate minima
            List<Long> minima = getMinima(max, minimaOffset);

            addGroups(values, groups, minima);
        }

        groups.add( values.length ); // last group ends at end of list
        return groups;
    }

    @NonNull
    // Calculate prob across value range in descending order
    private static List<Double> getProbabilities(long[] values, double mean, double variance, long min, long max) {
        List<Double> probabilities = new ArrayList<>();
        for (long x = max; x >= min; x--) {
            double probability = kde(x, values, mean, variance);
            Log.d(TAG, "kde( rank:" + x + " ) = " + probability);
            probabilities.add(probability);
        }
        return probabilities;
    }

    @NonNull
    // Calculate minima
    private static List<Long> getMinima(long max, List<Integer> minimaOffset) {
        List<Long> minima = new ArrayList<>();
        for (Integer offset : minimaOffset) {
            minima.add(max - offset); // max - offset from max
        }
        return minima;
    }

    private static void addGroups(long[] values, List<Integer> groups, List<Long> minima) {
        int minimaIndex = 0;
        for (int i = 0; i < values.length && minimaIndex < minima.size(); i++) {
            // If we passed a min (descending order)
            if (values[i] < minima.get(minimaIndex)) {
                // Mark the end of a group with the 1st index of the next group
                groups.add(i);
                minimaIndex++;
            }
        }
    }

    /**
     * Kernel density estimation function
     *
     * @param x
     * @param values
     * @param mean
     * @param variance
     * @return
     */
    private static double kde(long x, long[] values, double mean, double variance) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            double paramX = ( x - values[i] ) / H;
            total += normal(paramX, mean, variance);
        }
        return total / (values.length * H);
    }

    /**
     * Normal Distribution
     *
     * @param x
     * @param mean
     * @param variance
     * @return
     */
    private static double normal(double x, double mean, double variance) {
        double eExpNumerator  = Math.pow(x - mean, 2);
        double eExpDenominator = 2 * variance;
        double numerator = Math.pow(Math.E, (-1 * eExpNumerator) / eExpDenominator);
        double denominator = Math.sqrt(variance * 2 * Math.PI);
        return numerator / denominator;
    }

    /**
     * Find index of each min
     * This represents offsets from the min rank value
     *
     * @param probabilities
     * @return
     */
    private static List<Integer> findMinimaOffset(List<Double> probabilities) {
        List<Integer> minimaOffset = new ArrayList<>();

        if ( probabilities.size() > 2 ) {

            boolean goingUp = probabilities.get(1) - probabilities.get(0) > 0;

            for (int i = 2; i < probabilities.size(); i++) {

                boolean newGoingUp = probabilities.get(i) - probabilities.get(i-1) > 0;

                if ( goingUp==false && newGoingUp==true ) {
                    minimaOffset.add(i-1); // last index was min
                    Log.d(TAG, "Min prob="+probabilities.get(i-1) + " offset=" + (i-1));
                }

                goingUp = newGoingUp;

            }

        }

        return minimaOffset;
    }


    /*
     * Mean and Variance
     */

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


    /*
     * Min and Max
     */

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
