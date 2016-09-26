/*
 * @(#)Logger.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.logger;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.DateUtil;

import java.io.*;

/**
 *
 * Logger
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class Logger implements ILogger {
    private boolean isStdout = false;
    private boolean isDebug = false;
    private boolean isVerbose = false;
    protected String m_logBase;
    private PrintWriter m_log;
    private PrintWriter m_err;
    protected final static Object lock = new Object();
    protected int verbosity = INFO;
    private String m_module = "exporter-";
    
    public Logger(String logBase, boolean debug){
        this(logBase, debug, 3, false );
    }

    public Logger(String logBase, boolean debug, int verbosity){
        this(logBase, debug, verbosity, false );
    }

    public Logger(String logBase,  boolean debug, int verbosity, boolean verbose) {
        setLogBase(logBase);
        setDebug(debug);
        setPrintWriter();
        setVerbosity(verbosity);
        setVerbose(verbose);
    }
    
    private void setLogBase(String logBase) {
        m_logBase = logBase;
    }
    
    private void setDebug(boolean debug) {
        isDebug = debug;
        if(isDebug) {
            setVerbosity(DEBUG);
        }
    }

    private void setPrintWriter(){
        String logDate = DateUtil.getCurrentDate();
        String logFileName = null;
        String errFileName = null;

        try {
            FileUtil.makeDir(m_logBase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logFileName = m_logBase + FileUtil.fileseperator + m_module + ".info." + logDate + ".log";
        errFileName = m_logBase + FileUtil.fileseperator + m_module + ".error." + logDate + ".log";
        makeLogFile(logFileName, errFileName);
    }

    private void makeLogFile(String logFileName, String errFileName){
        if(m_log == null || ! new File(logFileName).exists()){
            try {
                m_log  = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(m_err == null || ! new File(errFileName).exists()){
            try {
                m_err  = new PrintWriter(new BufferedWriter(new FileWriter(errFileName, true)), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVerbose(boolean verbose){
        isVerbose = verbose;
    }

    private void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    private String getTitle(int level) {
        String title = "";
        switch(level) {
            case CRIT:
                title = "[crit] ";
                break;
            case ERROR:
                title = "[error] ";
                break;
            case WARNING:
                title = "[warning] ";
                break;
            case INFO:
                title = "[info] ";
                break;
            case DEBUG:
                title = "[debug] ";
                break;
            default:
                break;
        }
        return title;
    }

    public void log(String msg){
        if( !isStdout && m_log != null ) {
            synchronized (lock) {
                setPrintWriter();
                m_log.println("["+DateUtil.getTimeStamp()+"]"+" ["+DateUtil.getCurrSysTime()+"] " + msg);
                m_log.flush();
            }
        }
        System.out.println(msg);
    }

    public void error(String msg) {
        if( !isStdout && m_err != null ) {
            synchronized (lock) {
                setPrintWriter();
                m_err.println("["+DateUtil.getTimeStamp()+"]"+" ["+DateUtil.getCurrSysTime()+"] [error] " + msg);
                m_err.flush();
            }
        }
        System.out.println("[error] " + msg);
    }

    public void log(Exception ex) {
        error(ex.toString());
    }

    public void error(Exception ex) {
        error(ex.toString());
    }

    public void log(Exception exception, String msg) {
        error(msg+"\n"+ exception.toString());
    }

    public void log(String msg, Throwable throwable) {
        CharArrayWriter buf = new CharArrayWriter();
        error(buf.toString());
    }

    public void log(String message, int verbosity) {
        if (this.verbosity >= verbosity) {
            log(getTitle(verbosity)+message);
        }
    }

    public void log(String message, Throwable throwable, int verbosity) {
        if (this.verbosity >= verbosity) {
            log(message, throwable);
        }
    }

    public void finalize() {
        try {
            if (m_log != null) {
                m_log.flush();
            }
            if (m_err != null) {
                m_err.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verbose(String message){
        if(isVerbose){
            System.out.println(message);
        }
    }

    public void verbose(String title, String message){
        verbose("[" + title + "] " + message);
    }
}
