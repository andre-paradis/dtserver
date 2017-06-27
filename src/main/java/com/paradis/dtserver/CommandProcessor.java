package com.paradis.dtserver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by andre.paradis on 2017-06-27.
 */
public class CommandProcessor {

    public CommandProcessor(){

    }

    public String processCommand(String command) {
        String cmdResult = null;

        switch (command) {
            case "date":
                cmdResult = getIsoDate();
                break;
            case "time":
                cmdResult = getIsoTime();
                break;
            case "datetime":
                cmdResult =  getIsoDateTime();
                break;
            default:
                // intentionally blank
                break;
        }

        return cmdResult != null ? String.format("%s%n", cmdResult) : null;
    }

    private String getIsoDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private String getIsoTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("hh:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private String getIsoDateTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

}
