package com.paradis.dtserver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Basic command interpreter.  Returns current date in UTC according to the given specification
 */
public class CommandProcessor {

    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DATETIME = "datetime";

    public CommandProcessor(){

    }

    public String processCommand(String command) {
        String cmdResult = null;

        switch (command) {
            case DATE:
                cmdResult = formatDate("yyyy-MM-dd");
                break;
            case TIME:
                cmdResult = formatDate("hh:mm:ss'Z'");
                break;
            case DATETIME:
                cmdResult =  formatDate("yyyy-MM-dd'T'hh:mm:ss'Z'");
                break;
            default:
                // intentionally blank
                break;
        }

        return cmdResult != null ? String.format("%s%n", cmdResult) : null;
    }

    private String formatDate(String format) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(tz);
        return df.format(new Date());
    }

}
