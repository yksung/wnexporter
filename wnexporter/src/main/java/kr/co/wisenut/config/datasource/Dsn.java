/*
 * @(#)Dsn.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.config.datasource;

import java.util.Properties;

/**
 * Created by WISEnut
 * <b>Filename : </b>Dsn.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class Dsn {
    private String driver;
    private String url;
    private Properties prps;
    private Class dbms;

    public Class getDbms() {
        return dbms;
    }

    public void setDbms(Class dbms) {
        this.dbms = dbms;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Properties getPrps() {
        return prps;
    }

    public void setPrps(Properties prps) {
        this.prps = prps;
    }
}
