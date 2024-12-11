package com.example.calmscope.CalmDatabase;

import java.util.Calendar;
import java.sql.Date;

public class DateUtils {

    // Get the start date for last week
    public static Date getLastWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);  // Move back 7 days
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    // Get the end date for last week
    public static Date getLastWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Date(cal.getTimeInMillis());
    }

    // Get the start date for last month
    public static Date getLastMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);  // Move back 1 month
        cal.set(Calendar.DAY_OF_MONTH, 1);  // Set to first day of the month
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    // Get the end date for last month
    public static Date getLastMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);  // Set to first day of current month
        cal.add(Calendar.DAY_OF_YEAR, -1);  // Go back 1 day to get last day of the last month
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Date(cal.getTimeInMillis());
    }

    // Get the start date for last year
    public static Date getLastYearStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);  // Move back 1 year
        cal.set(Calendar.MONTH, Calendar.JANUARY);  // Set to January
        cal.set(Calendar.DAY_OF_MONTH, 1);  // Set to first day of the year
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    // Get the end date for last year
    public static Date getLastYearEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);  // Set to December
        cal.set(Calendar.DAY_OF_MONTH, 31);  // Set to last day of the month
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Date(cal.getTimeInMillis());
    }
}

