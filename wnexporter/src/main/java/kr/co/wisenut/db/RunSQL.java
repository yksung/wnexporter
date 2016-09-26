/*
 * @(#)RunSQL.java   1.0.5 2009/03/19
 *
 */

package kr.co.wisenut.db;

import java.sql.SQLException;
import java.util.ArrayList;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.io.IOUtil;

/**
 * Created by WISEnut
 * <b>Filename : </b>RunSQL.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class RunSQL {

    protected DBJob m_dbjob;

    public RunSQL(DBJob m_dbjob) {
        this.m_dbjob = m_dbjob;
    }

    public boolean  getConnection() {
        boolean isConnect = false;
        try {
            m_dbjob.setTargetConnection();
        }catch (DBFactoryException e) {
        	Log2.error("[error] [DBFactoryException " + IOUtil.StackTraceToString(e)+"]");
        	return isConnect;
		}
        isConnect = true;
        return isConnect;
    }

    public String [][] getResult(String sql, String values[][]) {
        return m_dbjob.getQueryRsData(sql, values, "\t");
    }

    
    public ArrayList getResultByArrayList(String sql, String values[][]) {
        return m_dbjob.getQueryRsData(sql, values);
    }
    
    public String [][] getResult(String sql, String values[][], int maxRow) {
        return m_dbjob.getQueryRsData(sql, values, "\t", maxRow);
    }
    
    public int execQuery(String sql) throws SQLException {
        int rst = 0;
        try {
            rst = m_dbjob.executeQuery(sql);
        } catch (DBFactoryException e) {
            Log2.error("[DBFactoryException "+e.getMessage()+" ]");
        }
        return rst;
    }

    public int execQuery(String sql, String values[][]) throws SQLException {
        int rst = 0;
        try {
            rst = m_dbjob.executeQuery(sql, values);
        } catch (DBFactoryException e) {
            Log2.error("[DBFactoryException "+e.getMessage()+" ]");
        }
        return rst;
    }
    
    public boolean getIsExist(String sql, String values[][]) throws SQLException {
    	boolean isExist = false;
    	try{
    		m_dbjob.setResultSet(sql, 1, values);
    		isExist = m_dbjob.next();
    	} catch (DBFactoryException e) {
    		Log2.error("[DBFactoryException "+e.getMessage()+" ]");
		}
    	return isExist;
    }
    
    /**
     *
     * @param a
     * @param b
     * @return double
     */
    public double getRound(double a, double b) {
        return Math.round(a*Math.pow(10, b))*Math.pow(0.1, b);
    }

    public void releaseRS() {
        m_dbjob.releaseRs();
    }
    
    public void releaseDB() {
        m_dbjob.releaseDB();
    }
    
}
