/*
 * @(#)RunTimeArgs.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.config;

/**
 * Created by WISEnut
 * <b>Filename : </b>RunTimeArgs.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class RunTimeArgs {
	private static String exportid = "";
    private static String conf = "";
    private static String logdir = "";
    private static String piddir = "";
    private static String mode = "";
    private static boolean failQuery = false;
    private static boolean isDebug = false;

    /**
     *Argument Class Main Function
     * @param args runtime argument
     */
    public static int readargs(String[] args) {
        if (args.length <1 ) {
            return 1;
        }
        
        // get command line options
        GetOpt opt = new GetOpt(args);
        if (opt.getOptionBoolean("h") || opt.getOptionBoolean("help")) {
            return 1;
        }
        
        // configuration file name
        String option = opt.getOptionString("id");
        if (option == null) {
            return 2;
        }
        setExportid(option);
        
        // configuration file name
        option = opt.getOptionString("conf");
        if (option == null) {
            return 2;
        }
        setConf(option);

        option=opt.getOptionString("logdir");
        if (option == null) {
            option = "";
        }
        setLogdir(option);
        
        option=opt.getOptionString("piddir");
        if (option == null) {
            option = "";
        }
        setPiddir(option);
        
        option=opt.getOptionString("mode");
        if (option == null){
        	option = "";
        }
        setMode(option);
        
        if(opt.getOptionBoolean("failquery")){        
        	setFailQuery(true);
        }
        
        if (opt.getOptionBoolean("debug")) {
            setDebug(true);
        }
        
        return 0;
    }

	public static String getExportid() {
		return exportid;
	}

	public static void setExportid(String exportid) {
		RunTimeArgs.exportid = exportid;
	}

	public static String getConf() {
		return conf;
	}

	public static void setConf(String conf) {
		RunTimeArgs.conf = conf;
	}

	public static String getLogdir() {
		return logdir;
	}

	public static void setLogdir(String logdir) {
		RunTimeArgs.logdir = logdir;
	}

	public static String getPiddir() {
		return piddir;
	}

	public static void setPiddir(String piddir) {
		RunTimeArgs.piddir = piddir;
	}


	public static String getMode() {
		return mode;
	}

	public static void setMode(String mode) {
		RunTimeArgs.mode = mode;
	}

	public static boolean isFailQuery() {
		return failQuery;
	}

	public static void setFailQuery(boolean failQuery) {
		RunTimeArgs.failQuery = failQuery;
	}

	public static boolean isDebug() {
		return isDebug;
	}

	public static void setDebug(boolean isDebug) {
		RunTimeArgs.isDebug = isDebug;
	}

}

