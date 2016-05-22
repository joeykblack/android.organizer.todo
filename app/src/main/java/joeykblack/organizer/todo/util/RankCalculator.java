package joeykblack.organizer.todo.util;

import joeykblack.organizer.todo.model.Task;

/**
 * Created by joey on 5/22/2016.
 */
public class RankCalculator {

    public static int getRank(Task task) {
        int rank = task.getPriority();

        return rank;
    }

}
