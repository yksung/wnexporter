/*
 * @(#)DateUtil.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.util;

import kr.co.wisenut.common.logger.Log2;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.FieldPosition;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.sql.Timestamp;

/**
 *
 * DateUtil
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public final class DateUtil {
    public final static String NONE_TIME_T = "19700101090000";
    
    private static long lStartTime = 0L;
	private static long lEndTime = 0L;
	
    private DateUtil() {
    }

    /**
     * Create logfile name method.
     * @return yyyyMMdd
     */
    public static String getCurrentDate() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat
                ("yyyyMMdd", java.util.Locale.KOREA);
        return dateFormat.format(new java.util.Date());
    }

    /**
     * Create logfile TimeStamp
     * @return  [1112749658]
     */
    public static String getTimeStamp(){
        Timestamp ts  = new Timestamp( (new Date()).getTime() );
        String strTime =String.valueOf(ts.getTime()) ;
        if(strTime.length() > 10 ){
            strTime = strTime.substring(0, 10);
        }
        return strTime;
    }

    /**
     * first day of week
     * @return date string
     * @throws Exception error info
     */
    public static String getFirstDayWeek() throws Exception{
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat stf = new SimpleDateFormat("yyyyMMdd");
        return stf.format(rightNow.getTime());
    }

    /**
     * Get number of week
     * @return String
     */
    public static String getFirstWeek() {
        Calendar calendar = Calendar.getInstance();
        int lastDay;
        lastDay = calendar.getFirstDayOfWeek();
        return Integer.toString(lastDay);
    }


    /**
     * Create SCD file format
     * @return  yyMMddHHmm-ssSS
     */
    public static String getScdFileTime() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat
                ("yyyyMMddHHmm-ssSSS", java.util.Locale.KOREA);
        return timeFormat.format(new java.util.Date());
    }

    /**
     * convert java date format
     * @param type configuration date format
     * @return java date format
     */
    public static String inputFormat(String type) {
        String returnvalue = "";
        String datetype = type.toLowerCase();
        if (datetype.equals("none")) {
            returnvalue = "none";
        } else if (datetype.equals("yyyy")) {
            returnvalue = "yyyy";
        } else if (datetype.equals("yyyymmdd")) {
            returnvalue = "yyyyMMdd";
        } else if (datetype.equals("yyyymmddhhmm") || datetype.equals("yyyymmddhhmi")) {
            returnvalue = "yyyyMMddHHmm";
        } else if (datetype.equals("yyyymmdd:hh:mm:ss") || datetype.equals("yyyymmdd:hh:mi:ss")) {
            returnvalue = "yyyyMMdd:HH:mm:ss";
        } else if (datetype.equals("yyyy-mm-dd")) {
            returnvalue = "yyyy-MM-dd";
        } else if (datetype.equals("yyyy/mm/dd")) {
            returnvalue = "yyyy/MM/dd";
        } else if (datetype.equals("yyyy.mm.dd")) {
            returnvalue = "yyyy.MM.dd";
        } else if (datetype.equals("yyyy-mm-dd hh:mm:ss") || datetype.equals("yyyy-mm-dd hh:mi:ss")) {
            returnvalue = "yyyy-MM-dd HH:mm:ss";
        } else if (datetype.equals("yyyy/mm/dd hh:mm:ss") || datetype.equals("yyyy/mm/dd hh:mi:ss")) {
            returnvalue = "yyyy/MM/dd HH:mm:ss";
            //TODO: Add your  code here
        } else {
            //BridgeLogger.write(BridgeLogger.ERR, "DateUtil->inputFormat()  unable convert date type");
        }
        return returnvalue;
    }

    /**
     * parseDate Function : create yyyyMMddHHmmss date format
     * @param dateValue date data
     * @param format  oonvert date format
     * @return yyyyMMddHHmmss
     */
    public static String parseDate(String dateValue, String format) {
        if(format.equals("none")) {
            return dateValue;
        } else {
            if( dateValue == null || dateValue.length() != format.length()) {
                return "";
            }
        }
        if(format.equals("time_t")) {
            dateValue = getUnixTime2NoneTime(dateValue);
            format = "yyyymmdd";
        }
        String none_time_t = "";
        String output = inputFormat(format);
        Date date = null;
        SimpleDateFormat formatter = null;
        try {
            date = (new SimpleDateFormat(output)).parse(dateValue.trim());
        } catch (ParseException e) {
            Log2.error("[DateUtil ] [ParseDate() "+e+"]");
        }
        formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        none_time_t = formatter.format(date);
        return none_time_t;
    }

    // TODO: Add your function code here
    /**
     *
     * @return yyyy.MM.dd
     */
    public static String getCurrentDateWithDot() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat
                ("yyyy.MM.dd", java.util.Locale.KOREA);
        return dateFormat.format(new java.util.Date());
    }

    /**
     *
     * @return yyyy
     */
    public static String getCurrentYear() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat
                ("yyyy", java.util.Locale.KOREA);
        return dateFormat.format(new java.util.Date());
    }

    /**
     *
     * @return HH:mm:ss
     */
    public static String getCurrentTime() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat
                ("HH:mm:ss", java.util.Locale.KOREA);
        return timeFormat.format(new java.util.Date());
    }

    /**
     *
     * @return HH-mm-ss
     */
    public static String getCurrentTime2() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat
                ("HH-mm-ss", java.util.Locale.KOREA);
        return timeFormat.format(new java.util.Date());
    }

    /**
     *
     * @param f
     * @return  yyyyMMdd:HH:mm:ss
     */
    public static String getLastModifiedDateFromFile(File f) {
        long date = f.lastModified();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd:HH:mm:ss",
                java.util.Locale.KOREA);
        return dateFormat.format(new java.util.Date(date));
    }

    /**
     *
     * @param f
     * @param format
     * @return yyyyMMdd
     */
    public static String getLastModifiedDateFromFile(File f, String format) {
        java.text.SimpleDateFormat dateFormat;
        long date = f.lastModified();
        if (format.equals("YYYYMMDD")) {
            dateFormat = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
        } else {
            return "INVALID_GIVEN_FORMAT_ERROR";
        }
        return dateFormat.format(new java.util.Date(date));

    }

    /**
     *
     * @return Mdd
     */
    public static String getTipForPassword() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat
                ("Mdd", java.util.Locale.KOREA);
        return timeFormat.format(new java.util.Date());
    }

    /**
     *
     * @return /yyyy/MMdd/
     */
    public static String getDirInfo() {
        return new java.text.SimpleDateFormat("/yyyy/MMdd/", java.util.Locale.KOREA).format(new java.util.Date());
    }

    /**
     * @return String
     */
    public static String getCurrSysTime(){
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    public static String getBuildTime(){
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", java.util.Locale.ENGLISH);
        return sf.format(date);
    }

    public static String getUnixTime2Year(String longTime){
        String fileseparator = System.getProperty("file.separator");
        int len = longTime.length();
        long ltime = 0;
        if(len > 13){
            longTime = longTime.substring(0, 13);
        }
        ltime = Long.parseLong(longTime);
        Date date = new Date();
        date.setTime(ltime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String yyyyMMdd = Integer.toString(calendar.get(Calendar.YEAR )) + fileseparator;
        yyyyMMdd += getDateFomrmat(Integer.toString(calendar.get(Calendar.MONTH)+1)) ;
        yyyyMMdd += getDateFomrmat(Integer.toString(calendar.get(Calendar.DATE))) ;
        return yyyyMMdd;
    }

    public static String getUnixTime2NoneTime(String longTime){
        String fileseparator = System.getProperty("file.separator");
        int len = longTime.length();
        long ltime = 0;
        if(len > 13){
            longTime = longTime.substring(0, 13);
        }
        ltime = Long.parseLong(longTime);
        Date date = new Date();
        date.setTime(ltime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String yyyyMMdd = Integer.toString(calendar.get(Calendar.YEAR )) + fileseparator;
        yyyyMMdd += getDateFomrmat(Integer.toString(calendar.get(Calendar.MONTH)+1)) ;
        yyyyMMdd += getDateFomrmat(Integer.toString(calendar.get(Calendar.DATE))) ;
        return yyyyMMdd;
    }

    public static String getDateFomrmat(String str){
        int _input = Integer.parseInt(str);
        StringBuffer result = new StringBuffer();
        DecimalFormat df = new DecimalFormat("00");
        df.format( _input, result, new FieldPosition(1) );
        return result.toString();
    }


    /**
     * return days between two date strings with user defined format.
     * @param from  start date
     * @param to end date
     * @return daysBetween value
     * @throws ParseException  erro info
     */
    public static int daysBetween(String from, String to) throws ParseException {
        return daysBetween(from, to, "yyyyMMdd");
    }

    /**
     * return days between two date strings with user defined format.
     * @param from  start date
     * @param to    end date
     * @param format date format
     * @return daysBetween value
     * @throws ParseException erro info
     */
    public static int daysBetween(String from, String to, String format) throws ParseException {
        Date d1 = check(from, format);
        Date d2 = check(to, format);

        long duration = d2.getTime() - d1.getTime();

        return (int)( duration/(1000 * 60 * 60 * 24) );
        // seconds in 1 day
    }

    /**
     *
     * @param s   date string you want to check.
     * @param format string representation of the date format. For example, "yyyy-MM-dd".
     * @return  date Date
     * @throws ParseException error info
     */
    public static Date check(String s, String format) throws ParseException {
        if ( s == null )
            throw new ParseException("date string to check is null", 0);
        if ( format == null )
            throw new ParseException("format string to check date is null", 0);

        SimpleDateFormat formatter =
                new SimpleDateFormat (format, Locale.KOREA);
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch(ParseException e) {
            throw new ParseException(" wrong date:\"" + s + "\" with format \"" + format + "\"", 0);
        }

        if ( ! formatter.format(date).equals(s) )
            throw new ParseException(
                    "Out of bound date:\"" + s + "\" with format \"" + format + "\"",
                    0
            );
        return date;
    }

    /**
     * return Minus day to date strings
     * @param s  input date
     * @param day  date value
     * @return  result
     * @throws ParseException  error info
     */
    public static String minusDays(String s, int day) throws ParseException {
        return minusDays(s, day, "yyyyMMdd");
    }

    /**
     * return Minus day to date strings with user defined format.
     * @param s  input date
     * @param day  date value
     * @param format date format
     * @return  result
     * @throws ParseException  error info
     */
    public static String minusDays(String s, int day, String format) throws ParseException{
        SimpleDateFormat formatter =
                new SimpleDateFormat (format, Locale.KOREA);
        Date date = DateUtil.check(s, format);

        date.setTime(date.getTime() - ((long)day * 1000 * 60 * 60 * 24));
        return formatter.format(date);
    }

    /**
     * return add day to date strings with user defined format.
     * @param s  input date
     * @param day  date value
     * @return addDays result
     * @throws ParseException  error info
     */
    public static String addDays(String s, int day) throws ParseException {
        return addDays(s, day, "yyyyMMdd");
    }

    /**
     * return add day to date strings with user defined format.
     * @param s  input date
     * @param day  date value
     * @param format date format
     * @return  addDays result
     * @throws ParseException   error info
     */
    public static String addDays(String s, int day, String format) throws ParseException{
        SimpleDateFormat formatter =
                new SimpleDateFormat (format, Locale.KOREA);
        Date date = check(s, format);

        date.setTime(date.getTime() + ((long)day * 1000 * 60 * 60 * 24));
        return formatter.format(date);
    }

    /**
     * return Minus day to date strings with user defined format.
     *  @param  s input date
     * @return int FirstDayWeek date value
     * @throws ParseException  error info
     */

    public static String getFirstDayWeek(String s) throws ParseException{
        int minuscnt = whichDay(s);

        minuscnt = minuscnt - 2 >= 0 ? minuscnt - 2 : 6;

        return minusDays(s, minuscnt);
    }

    /**
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static String getLastDayWeek(String s) throws ParseException{
        int minuscnt = whichDay(s);

        minuscnt = minuscnt == 1 ? 0 : 7 - minuscnt + 1;

        return addDays(s, minuscnt);
    }

    public static int whichDay(String s) throws ParseException {
        return whichDay(s, "yyyyMMdd");
    }

    /**
     * return days between two date strings with user defined format.
     * @param s date string you want to check.
     * @param format string representation of the date format. For example, "yyyy-MM-dd".
     * @return whichDay date value
     *          0: SUNDAY (Calendar.SUNDAY)
     *          1: MONDAY (Calendar.MONDAY��)
     *          2: TUESDAY (Calendar.TUESDAY)
     *          3: WENDESDAY (Calendar.WENDESDAY )
     *          4: THURSDAY (Calendar.THURSDAY )
     *          5: FRIDAY (Calendar.FRIDAY )
     *          6: SATURDAY (Calendar.SATURDAY)
     * ��) String s = "2000-05-29";
     *  int dayOfWeek = whichDay(s, "yyyy-MM-dd");
     *  if (dayOfWeek == Calendar.MONDAY)
     *      System.out.println(" MONDAY: " + dayOfWeek);
     *  if (dayOfWeek == Calendar.TUESDAY)
     *      System.out.println(" TUESDAY: " + dayOfWeek);
     * @throws ParseException  error info
     */
    public static int whichDay(String s, String format) throws ParseException {
        SimpleDateFormat formatter =
                new SimpleDateFormat (format, Locale.KOREA);
        Date date = check(s, format);

        Calendar calendar = formatter.getCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * week Between method
     * date format : yyyymmdd
     * @param from start date
     * @param to end date
     * @return week Between date value
     * @throws ParseException  error info
     */
    public static int weekBetween(String from, String to) throws ParseException {
        String firstdayofweek = getFirstDayWeek(from);
        String lastdayofweek = getLastDayWeek(to);

        int daycnt = daysBetween(firstdayofweek, lastdayofweek);

        return daycnt/7 == 0 ? 1 : (daycnt/7 + 1);
    }

    /**
     * months Between method
     * @param from start date
     * @param to end date
     * @return monthsBetween date value
     * @throws ParseException error info
     */
    public static int monthsBetween(String from, String to) throws ParseException {
        return monthsBetween(from, to, "yyyyMMdd");
    }

    /**
     * months Between method
     * @param from start date
     * @param to end date
     * @param format from/to date format
     * @return monthsBetween date value
     * @throws ParseException error info
     */
    public static int monthsBetween(String from, String to, String format) throws ParseException {
        Date fromDate = check(from, format);
        Date toDate = check(to, format);

        // if two date are same, return 0.
        if (fromDate.compareTo(toDate) == 0) return 1;

        SimpleDateFormat yearFormat =
                new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat =
                new SimpleDateFormat("MM", Locale.KOREA);

        int fromYear = Integer.parseInt(yearFormat.format(fromDate));
        int toYear = Integer.parseInt(yearFormat.format(toDate));
        int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
        int toMonth = Integer.parseInt(monthFormat.format(toDate));

        int result = 0;

        result += ((toYear - fromYear) * 12);
        result += (toMonth - fromMonth);

        return result + 1;
    }

    /**
     *
     * @param src
     * @return
     * @throws ParseException
     */
    public static String lastDayOfMonth(String src) throws ParseException {
        return lastDayOfMonth(src, "yyyyMMdd");
    }

    public static String lastDayOfMonth(String src, String format) throws ParseException {
        SimpleDateFormat formatter =
                new SimpleDateFormat (format, Locale.KOREA);
        Date date = check(src, format);

        SimpleDateFormat yearFormat =
                new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat =
                new SimpleDateFormat("MM", Locale.KOREA);

        int year = Integer.parseInt(yearFormat.format(date));
        int month = Integer.parseInt(monthFormat.format(date));
        int day = lastDay(year, month);

        DecimalFormat fourDf = new DecimalFormat("0000");
        DecimalFormat twoDf = new DecimalFormat("00");
        String tempDate = String.valueOf(fourDf.format(year))
                + String.valueOf(twoDf.format(month))
                + String.valueOf(twoDf.format(day));
        date = check(tempDate, format);

        return formatter.format(date);
    }

    /**
     *
     * @param year
     * @param month
     * @return int
     */
    public static int lastDay(int year, int month)  {
        int day = 0;
        switch(month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12: day = 31;
                break;
            case 2: if ((year % 4) == 0) {
                if ((year % 100) == 0 && (year % 400) != 0) { day = 28; }
                else { day = 29; }
            } else { day = 28; }
                break;
            default: day = 30;
        }
        return day;
    }

    public static String getCurrSysTimeUntilMin() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
        return sf.format(date);
    }


    public static int getTimeDiffMin(String format, String startDate, String endDate) {
        Date sDate = null;
        Date eDate = null;
        int ret = 0;
        int sign = 1;
        try {
            sDate = (new SimpleDateFormat(format)).parse(startDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(sDate);

            eDate = (new SimpleDateFormat(format)).parse(endDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(eDate);

            Date date1 = cal1.getTime();
            long t1 = date1.getTime();
            Date date2 = cal2.getTime();
            long t2 = date2.getTime();
            long elapsed = t1 - t2;

            if (elapsed < 0L) {
                elapsed = -elapsed;
                sign = -1;
            }

            int tmp = (int)(elapsed / 1000L);
            //int millisec = (int) (elapsed % 1000L);
            int sec = tmp % 60;
            tmp /= 60;
            int mn = tmp % 60;
            tmp /= 60;
            int hr = tmp % 24;
            int dd = tmp / 24;

            if (dd != 0)
                ret += (dd * 1440);
            if (hr != 0)
                ret += (hr * 60);
            if (mn != 0)
                ret += mn;
            //if (sec != 0)
            //    ret += sec;
            if (sign < 0) ret = -ret;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }
    /**
     * return Minus day to date strings with user defined format.
     * @param format string representation of the date format. For example, "yyyy-MM-dd".
     * @return int 
     */
    public static String minusDate(int day, int type){
    	GregorianCalendar cal = new GregorianCalendar();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	cal.add(Calendar.DAY_OF_MONTH, -1);
    	cal.add(type, - day);
    	return formatter.format(cal.getTime());
    }
    
    public static void tick(){
    	lStartTime = System.nanoTime();
    }
    
    public static void tock(String title){
    	lEndTime = System.nanoTime();
    	
    	System.out.println("### " + title + "'s duration ::::: " + (lEndTime-lStartTime));
    	
    	lStartTime = 0L;
    	lEndTime = 0L;
    }

}
