/*
 * @(#)Log2.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.logger;
/**
 *
 * Log2
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class Log2 {
    private static Logger logger;

    private Log2() {}
    /**
     * logpath, idDebug(디버그여부)
     */
	public static void setLogger(String logBase, boolean debug)  {
		if(logger == null) {
			logger = new Logger(logBase, debug);
		}
	}
	
    public static void out(String msg) {
        logger.log(msg);
    }

    public static void debug(String debug) {
        logger.log(debug, 3);
    }

    public static void debug(String debug, int level) {
        logger.log(debug, level);
    }

    public static void error(String error) {
        logger.error(error);
    }

    public static void error(Exception e) {
        logger.error(e);
    }
}
