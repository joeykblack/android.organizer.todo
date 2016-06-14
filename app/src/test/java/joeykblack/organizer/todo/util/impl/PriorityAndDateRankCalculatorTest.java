package joeykblack.organizer.todo.util.impl;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import joeykblack.organizer.todo.model.Task;
import joeykblack.organizer.todo.util.RankCalculator;

/**
 * Created by joey on 6/11/2016.
 */
public class PriorityAndDateRankCalculatorTest {

    RankCalculator rankCalculator = new PriorityAndDateRankCalculator();

    @Test
    public void testSame() {
        Task task1 = new Task().setPriority(5).setDate( getDateOffset(0) );
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5).setDate( getDateOffset(0) );
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 == rank2 );
    }

    @Test
    public void testNoDates() {
        Task task1 = new Task().setPriority(5);
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5);
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 == rank2 );
    }

    @Test
    public void testDiffDates() {
        Task task1 = new Task().setPriority(5).setDate( getDateOffset(0) );
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5).setDate( getDateOffset(1) );
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 > rank2 );
    }

    @Test
    public void testDiffDates2() {
        Task task1 = new Task().setPriority(5).setDate( getDateOffset(0) );
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5).setDate( getDateOffset(-1) );
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 < rank2 );
    }

    @Test
    public void testDiffPriorities() {
        Task task1 = new Task().setPriority(6).setDate( getDateOffset(0) );
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5).setDate( getDateOffset(0) );
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 > rank2 );
    }

    @Test
    public void testDiffBoth() {
        Task task1 = new Task().setPriority(6).setDate( getDateOffset(0) );
        long rank1 = rankCalculator.getRank(task1);

        Task task2 = new Task().setPriority(5).setDate( getDateOffset(-1) );
        long rank2 = rankCalculator.getRank(task2);

        Assert.assertTrue( rank1 == rank2 );
    }

    private Date getDateOffset(int offset) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + offset);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }
}
