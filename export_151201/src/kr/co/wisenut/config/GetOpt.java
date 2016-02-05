/*
 * @(#)GetOpt.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.config;

/**
 * Created by WISEnut
 * <b>Filename : </b>GetOpt.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class GetOpt {
    public GetOpt(String[] options) {
        this.options=options;
    }

    public boolean getOptionBoolean(String name) {
        for (int i=0; i<options.length; i++) {
            if (options[i].equals("-"+name)) {
                return true;
            }
        }
        return false;
    }

    public String getOptionString(String name) {
        for (int i=0; i<options.length-1; i++) {
            if (options[i].equals("-"+name)) {
                return options[i+1];
            }
        }
        return null;
    }

    private String[] options;
}
