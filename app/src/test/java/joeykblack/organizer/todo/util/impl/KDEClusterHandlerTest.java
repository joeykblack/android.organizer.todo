package joeykblack.organizer.todo.util.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.ClusterHandler;

/**
 * Created by joey on 6/11/2016.
 */
public class KDEClusterHandlerTest {

    protected ClusterHandler clusterHandler;
    protected List<Task> tasks;

    @Before
    public void setUp() {
        clusterHandler = new KDEClusterHandler();
        tasks = new ArrayList<Task>();

        tasks.add(new Task().setTitle("group1a").setRank(15));
        tasks.add(new Task().setTitle("group1b").setRank(14));
        tasks.add(new Task().setTitle("group1c").setRank(13));

        tasks.add(new Task().setTitle("group2a").setRank(9));
        tasks.add(new Task().setTitle("group2b").setRank(8));
        tasks.add(new Task().setTitle("group2c").setRank(7));

        tasks.add(new Task().setTitle("group3a").setRank(3));
        tasks.add(new Task().setTitle("group3b").setRank(2));
        tasks.add(new Task().setTitle("group3c").setRank(1));
    }

    @Test
    public void labelTasksTest() {
        tasks = clusterHandler.labelTasks(tasks);
        for (Task task : tasks) {
            System.out.println(task + " " + task.getGroup());
        }
        for (int i = 1; i < tasks.size(); i++) {
            Task last = tasks.get(i-1);
            Task task = tasks.get(i);
            if ( Math.abs(last.getRank()-task.getRank()) < 3 ) {
                Assert.assertEquals(last.getGroup(), task.getGroup());
            }
            else {
                Assert.assertTrue(last.getGroup() != task.getGroup());
            }
        }
    }


}
