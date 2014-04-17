package com.pir.util;

import org.joda.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pritesh on 12/10/13.
 */
public class AppClock {

    private static Logger logger = LoggerFactory.getLogger(AppClock.class);

    private static DateTime dateTime;

    public static synchronized void setDateTime(DateTime dateTime) {
        AppClock.dateTime = dateTime;
    }

    public static synchronized DateTime realNow() {
        return DateTime.now(DateTimeZone.UTC);
    }

    public static synchronized DateTime now() {
        if (dateTime != null) {
            return dateTime;
        } else {
            return DateTime.now(DateTimeZone.UTC);
        }
    }

    public static synchronized void reset() {
        dateTime = null;
    }

    public static String getAgoString(DateTime end){
        DateTime start = AppClock.now();
        int days = Days.daysBetween(end, start).getDays();
        if (days < 2){
            int hours = Hours.hoursBetween(end, start).getHours();
            if (hours < 1){
                int min = Minutes.minutesBetween(end, start).getMinutes();
                if (min < 2){
                    return " just now";
                } else {
                    return min + " mins ago";
                }
            } else {
                return hours + " hours ago";
            }
        } else {
            return days + " days ago";
        }
    }
}
