package joeykblack.organizer.todo.database;

import java.text.SimpleDateFormat;

/**
 * Created by joey on 6/11/2016.
 */
public interface Contract {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat DATE_FORMAT_DISPLAY = new SimpleDateFormat("MM/dd/yyyy");
    String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    String DATE_PATTERN_DISPLAY = "\\d{2}/\\d{2}/\\d{4}";

}
