/*
 * @(#)InfoMsg.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.msg;

import kr.co.wisenut.common.util.StringUtil;

/**
 * Created by WISEnut
 * <b>Filename : </b>InfoMsg.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class InfoMsg {
    private final static String version = "1.0.0";
    private final static String build = "01";
    private final static String build_date = "2013 10 31 07:28:00";
    private final static String copyright = "Copyright 2001-2013 WISEnut, Inc. All Rights Reserved.";

    /**
     * Print LogCacheGenerator infomation message
     */
    public static void header() {
        System.out.println(new StringBuffer().append("\nSearch Formula-1 Exporter v.").append(version)
                .append(" (Build ").append(build).append(" ").append("-  Release").append("), ").append(build_date).toString());
        System.out.println(copyright);
    }

    /**
     *  usage
     */
    public static void usage() {
        String usage = "Usage : java -Xmx256m kr.co.wisenut.Exporter " +
                "-conf [configuration file] -logdir [log path] -piddir [pid path]";
        usage += option("-debug", "");
        System.out.println(usage);
    }

    public static String option(String oName, String desc){
        String option = StringUtil.padRight("    "+oName, ' ', 20);
        option += desc+"\n";
        return option;
    }

    public static String desc(String desc){
        return StringUtil.padRight("", ' ', 20) + desc;
    }
}
