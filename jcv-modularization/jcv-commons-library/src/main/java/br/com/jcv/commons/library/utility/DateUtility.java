package br.com.jcv.commons.library.utility;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateUtility {

    public static LocalDate from(Date input) {
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(DAY_OF_MONTH, days);
        return calendar.getTime();
    }
    public static Date getDateWithDiffBetweenDaysFromNow(int days) {
        return addDays(new Date(), days);
    }
    public static Date getDateWithDiffBetweenDaysFromDateReference(Date dateReference, int days) {
        return addDays(dateReference, days);
    }
    public static Long getDifferenceDays(Date dReference, Date d2) {
        long diff = d2.getTime() - dReference.getTime();
        Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return days;
    }
    public static Date getDate(int dd, int mm, int yy){
        Calendar calendar = Calendar.getInstance();
        calendar.set(DAY_OF_MONTH, dd);
        calendar.set(MONTH, mm - 1);
        calendar.set(YEAR, yy);
        return calendar.getTime();
    }

    public static Date getDate(Long time) {
        return new Date(time);
    }
    public static Date getDate(LocalDate localDate) {
        assert localDate != null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDate getLocalDate(int dd, int mm, int yy){
        return from(getDate(dd,mm,yy));
    }


    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return (calendar1.get(DAY_OF_MONTH) == calendar2.get(DAY_OF_MONTH))
                && (calendar1.get(MONTH) == calendar2.get(MONTH))
                && (calendar1.get(YEAR) == calendar2.get(YEAR));
    }

    public static boolean checkDayOfWeek(Date date, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(DAY_OF_WEEK) == dayOfWeek;
    }

    public static int compare(Date dateReference, Date dateB) {
        if(isSameDay(dateReference, dateB)) return 0;
        Calendar calendarReferencia = Calendar.getInstance();
        calendarReferencia.setTime(dateReference);
        Calendar calendarB = Calendar.getInstance();
        calendarB.setTime(dateB);

        if(calendarB.before(calendarReferencia)) return -1;
        return 1;
    }


}
