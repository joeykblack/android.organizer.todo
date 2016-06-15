package joeykblack.organizer.todo.util.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import joeykblack.organizer.todo.database.Contract;
import joeykblack.organizer.todo.util.DateUtil;

/**
 * Created by joey on 6/11/2016.
 */
public class ContractDateUtil implements DateUtil, Serializable {

    @Override
    public Date parseDate(String dateString) {
        Date date = null;
        try {
            if ( dateString != null && dateString.trim().equals("")==false ) {
                if ( Pattern.matches(Contract.DATE_PATTERN, dateString) ) {
                    date = Contract.DATE_FORMAT.parse(dateString);
                }
                else if ( Pattern.matches(Contract.DATE_PATTERN_DISPLAY, dateString) ) {
                    date = Contract.DATE_FORMAT_DISPLAY.parse(dateString);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public String serializeDateDatabase(Date date) {
        String dateString = "";
        if ( date != null ) {
            dateString = Contract.DATE_FORMAT.format( date );
        }
        return dateString;
    }

    @Override
    public String serializeDateDisplay(Date date) {
        String dateString = "";
        if ( date != null ) {
            dateString = Contract.DATE_FORMAT_DISPLAY.format( date );
        }
        return dateString;
    }

    @Override
    public boolean isDate(String dateString) {
        return Pattern.matches(Contract.DATE_PATTERN, dateString) || Pattern.matches(Contract.DATE_PATTERN_DISPLAY, dateString);
    }

}
