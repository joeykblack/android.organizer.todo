package joeykblack.organizer.todo.util.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import joeykblack.organizer.todo.model.Group;
import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.ClusterHandler;

/**
 * Created by joey on 5/27/2016.
 */
public class KDEClusterHandler implements ClusterHandler {
    private static final String TAG = KDEClusterHandler.class.getSimpleName();

    private static final double H = 0.1d;
    private static final double MAX_SPLIT_PROB = 0.001d;

    @Override
    public List<Task> labelTasks(List<Task> tasks) {
        long[] ranks = new long[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            ranks[i] = tasks.get(i).getRank();
            Log.d(TAG, "Task " + tasks.get(i).toString() + " [" + ranks[i] + "]");
        }

        List<Integer> groups = getGroupsUsingKDE(ranks);

        tasks = setGroups(tasks, groups);

        return tasks;
    }


    private List<Task> setGroups(List<Task> tasks, List<Integer> groups) {
        int startIndex = 0;
        // for each group
        for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
            int group = groups.get(groupIndex);
            // for each task in group range
            for (int i = startIndex; i < group; i++) {
                // set group
                tasks.get(i).setGroup( Group.valueOf(groupIndex) );
            }
            startIndex = group;
        }
        return tasks;
    }

    /**
     * Kernel density estimation
     * @param values
     * @return groups[groupNumber] = start of group groupNumber + 1
     *  group i = values.subList( groups[i-1], groups[i] )
     */
    private List<Integer> getGroupsUsingKDE(long[] values) {
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

            double bandwidth = H; //1.06 * Math.sqrt(variance) * Math.pow(values.length, -1/5);
            Log.d(TAG, "bandwidth: " + bandwidth);

            // Calculate prob across value range in descending order
            List<Double> probabilities = getProbabilities(values, mean, variance, min, max, bandwidth);

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
    private List<Double> getProbabilities(long[] values, double mean, double variance, long min, long max, double bandwidth) {
        List<Double> probabilities = new ArrayList<>();
        for (long x = max; x >= min; x--) {
            double probability = kde(x, values, mean, variance, bandwidth);
            Log.d(TAG, "\t kde(rank:" + x + "):\t" + probability);
            probabilities.add(probability);
        }
        return probabilities;
    }

    @NonNull
    // Calculate minima
    private List<Long> getMinima(long max, List<Integer> minimaOffset) {
        List<Long> minima = new ArrayList<>();
        for (Integer offset : minimaOffset) {
            minima.add(max - offset); // max - offset from max
        }
        return minima;
    }

    private void addGroups(long[] values, List<Integer> groups, List<Long> minima) {
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
    private double kde(long x, long[] values, double mean, double variance, double bandwidth) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            double paramX = ( x - values[i] ) / bandwidth;
            total += normal(paramX, mean, variance);
        }
        return total / (values.length * bandwidth);
    }

    /**
     * Normal Distribution
     *
     * @param x
     * @param mean
     * @param variance
     * @return
     */
    private double normal(double x, double mean, double variance) {
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
    private List<Integer> findMinimaOffset(List<Double> probabilities) {
        List<Integer> minimaOffset = new ArrayList<>();

        if ( probabilities.size() > 2 ) {

            boolean goingUp = probabilities.get(1) - probabilities.get(0) > 0;

            for (int i = 2; i < probabilities.size(); i++) {

                boolean newGoingUp = probabilities.get(i) - probabilities.get(i-1) > 0;

                if ( goingUp==false && newGoingUp==true && MAX_SPLIT_PROB>probabilities.get(i-1) ) {
                    minimaOffset.add(i-1); // last index was min
                    Log.d(TAG, "Min prob="+probabilities.get(i-1) + "\t offset=" + (i-1));
                }
                else if ( goingUp==false && newGoingUp==true ) {
                    Log.d(TAG, "\t Not low enough: Min prob="+probabilities.get(i-1) + "\t offset=" + (i-1));
                }

                goingUp = newGoingUp;

            }

        }

        return minimaOffset;
    }


    /*
     * Mean and Variance
     */

    private double getMean(long[] values) {
        long total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total / values.length;
    }

    private double getVariance(long[] values, double mean) {
        double total = 0;
        for (int i = 0; i < values.length; i++) {
            total += Math.pow( ( values[i] - mean ) , 2 );
        }
        return total / values.length;
    }


    /*
     * Min and Max
     */

    private long getMin(long[] values) {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }
    private long getMax(long[] values) {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }
}
