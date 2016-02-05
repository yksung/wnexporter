/*
 * @(#)ILogger.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.logger;
/**
 *
 * ILogger
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public interface ILogger {
    public static final int CRIT = Integer.MIN_VALUE;
    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final String STDOUT = "SDTOUT";
    public static final String ERROUT = "ERROUT";
    public static final String DAILY = "DAILY";
    
    public void log(String message);

    public void log(Exception ex);

    public void log(String message, int verbosity);

    public void log(Exception exception, String msg);

    public void log(String message, Throwable throwable);

    public void log(String message, Throwable throwable, int verbosity);

    public void error(String message);

    public void error(Exception ex);

    public void verbose(String message);

    public void finalize();
}
