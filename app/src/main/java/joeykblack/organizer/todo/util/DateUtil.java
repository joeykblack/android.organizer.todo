package joeykblack.organizer.todo.util;

import java.util.Date;

/**
 * Created by joey on 6/11/2016.
 */
public interface DateUtil {

    public Date parseDate(String dateString);
    public String serializeDateDatabase(Date date);
    public String serializeDateDisplay(Date date);
    public boolean isDate(String dateString);

}
