package joeykblack.organizer.todo.util.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import joeykblack.organizer.todo.util.DateUtil;

/**
 * Created by joey on 6/11/2016.
 */
public class ContractDateTest {

    protected DateUtil dateUtil;
    protected Date date;
    protected String displayDateString;
    protected String databaseDateString;
    protected String badDateString1;
    protected String badDateString2;

    @Before
    public void setUp() {
        dateUtil = new ContractDateUtil();

        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2016);
        c.set(Calendar.MONTH, 5); // Calendar is 0 based
        c.set(Calendar.DAY_OF_MONTH, 0);// Calendar is 0 based
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        date = c.getTime();

        displayDateString = "06/01/2016";
        databaseDateString = "2016-06-01";
        badDateString1 = "2016/06/01";
        badDateString2 = "06-01-2016";
    }

    @Test
    public void parseDateTest() {
        Assert.assertEquals( date, dateUtil.parseDate(displayDateString) );
        Assert.assertEquals( date, dateUtil.parseDate(databaseDateString) );
    }

    @Test
    public void serializeDateDatabaseTest() {
        Assert.assertEquals( dateUtil.serializeDateDatabase(date), databaseDateString );
    }

    @Test
    public void serializeDateDisplayTest() {
        Assert.assertEquals( dateUtil.serializeDateDisplay(date), displayDateString );
    }

    @Test
    public void isDateTest() {
        Assert.assertTrue( dateUtil.isDate(displayDateString) );
        Assert.assertTrue( dateUtil.isDate(databaseDateString) );
        Assert.assertFalse( dateUtil.isDate(badDateString1) );
        Assert.assertFalse( dateUtil.isDate(badDateString2) );
    }

}
