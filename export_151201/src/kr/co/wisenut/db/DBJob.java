/*
 * @(#)DBJob.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.RunTimeArgs;

/**
 * Created by WISEnut
 * <b>Filename : </b>DBJob.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class DBJob extends DBManager  {
    private Connection conn;
    private ResultSet t_rs;
    private PreparedStatement t_pstmt;
    private DBConnFactory m_dbFactory;

    /**
     * DBJob constructor
     * @param config
     * @param dsnID
     */
    public DBJob(int vender, String dsnID) {
        super(vender, dsnID);
        m_dbFactory = new DBConnFactory(Config.getDataSource());
    }

    /**
     * get Target Connection
     * @throws DBFactoryException   error info
     */
    public void setTargetConnection() throws DBFactoryException {
        conn = m_dbFactory.getConnection(dsnID);
        try {
            DatabaseMetaData dbm= conn.getMetaData();
            if(RunTimeArgs.isDebug()) Log2.debug("Connect DB  Product Name :"+dbm.getDatabaseProductName(), 3);
            if(RunTimeArgs.isDebug()) Log2.debug("Connect DB Product Version :"+dbm.getDatabaseProductVersion(), 3);
            if(RunTimeArgs.isDebug()) Log2.debug("JDBC Driver Name :"+dbm.getDriverName(), 3);
            if(RunTimeArgs.isDebug()) Log2.debug("JDBC Driver Version :"+dbm.getDriverVersion(), 3);
        } catch (SQLException e) {
            Log2.error("[DBJob] [setResultSet SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBJob] [setResultSet SQLException "+e.getMessage()+"]");
        }

    }

    /**
     * setting Target Table ResultSet
     * @param query
     * @throws kr.co.wisenut.common.Exception.DBFactoryException
     */
    public void setResultSet(String query) throws DBFactoryException {
        setResultSet(query, null, 0);
    }
    
    /**
     * setting Target Table ResultSet
     * @param query
     * @throws kr.co.wisenut.common.Exception.DBFactoryException
     */
    public void setResultSet(String query, int maxRows) throws DBFactoryException {
    	setResultSet(query, null, maxRows);
    }
    
    /**
     * setting Target Table ResultSet
     * @param query
     * @param value
     * @throws kr.co.wisenut.common.Exception.DBFactoryException
     */
    public void setResultSet(String query, String value, int maxRows) throws DBFactoryException {
        t_rs = null;
        if(RunTimeArgs.isDebug()) Log2.debug("[DBJob]"+" ["+ StringUtil.trimDuplecateSpace(query)+"]", 3);
        try {
            t_pstmt = conn.prepareStatement(query);
            if (maxRows != 0) {
            	t_pstmt.setMaxRows(maxRows);
			}
            if(query.indexOf("?") >0 && value != null) { 
                t_pstmt.setString(1, value);
            }
            t_rs = t_pstmt.executeQuery();
        } catch (SQLException e) {
            Log2.error("[DBJob] [SQL Error Code :  "+e.getErrorCode()+"]");
            Log2.error("[DBJob] [SQL Query : "+query+"]");
            Log2.error("[DBJob] ["+e.getMessage()+"]");
        }
    }
    
    /**
     * setting Target Table ResultSet
     * @param query
     * @param value
     * @throws kr.co.wisenut.common.Exception.DBFactoryException
     */
    public void setResultSet(String query, int maxRows, String[][] values) throws DBFactoryException {
        t_rs = null;
        if(RunTimeArgs.isDebug()) Log2.debug("[DBJob]"+" ["+ StringUtil.trimDuplecateSpace(query)+"]", 3);
        try {
            t_pstmt = conn.prepareStatement(query);
            t_pstmt = setPstmt(t_pstmt, values);
            
            if (maxRows != 0) {
            	t_pstmt.setMaxRows(maxRows);
			}
            t_rs = t_pstmt.executeQuery();
        } catch (SQLException e) {
            Log2.error("[DBJob] [SQL Error Code :  "+e.getErrorCode()+"]");
            Log2.error("[DBJob] [SQL Query : "+query+"]");
            Log2.error("[DBJob] ["+e.getMessage()+"]");
        }
    }

    /**
     * Target Table�� ResultSet String[][]
     * String[0][0] : column data
     * String[0][1] : column ClassName
     * @param idx
     * @return  String[][]
     */
    public String[][] getString(int idx) throws DBFactoryException {
        return getString(t_rs, idx);
    }
    
    public HashMap<String, String> getStringAsMap() throws DBFactoryException {
        return getStringAsMap(t_rs);
    }

    /**
     * Target Table�� ResultSet count
     * @param idx
     * @param count
     * @return String[][]
     */
    public String[][] getString(int idx, int count) throws DBFactoryException {
        return getString(t_rs, idx, count);
    }

    /**
     * @param query
     * @param values
     * @return String[][]
     */
    public String[][] getQueryRsData(String query, String[][] values, String seperator) {
        return getQueryData(conn, query, values, seperator);
    }
    
    /**
     * @param query
     * @param values
     * @return String[][]
     */
    public ArrayList getQueryRsData(String query, String[][] values) {
        return getQueryData(conn, query, values);
    }

    /**
     * @param query
     * @param values
     * @return String[][]
     */
    public String[][] getQueryRsData(String query, String[][] values, String seperator, int maxRow) {
        return getQueryData(conn, query, values, seperator, maxRow);
    }

    public String[][] getQueryRsData(Connection conn, String query, String[][] values, String seperator) {
        return getQueryData(conn, query, values, seperator);
    }

    /**
     * Target ResultSet next()
     * @return boolean
     */
    public boolean next() {
        return next(t_rs);
    }

    /**
     * Taget ResultSet Column count
     * @return int
     */
    public int getTargetColumnCnt() {
        return getColunmCnt(t_rs);
    }

    public int executeQuery(String query) throws SQLException, DBFactoryException {
        return executeQuery(conn, query);
    }

    public int executeQuery(String query, String[][] values) throws SQLException, DBFactoryException {
        return executeQuery(conn, query, values);
    }
    
    public int executeBatch(String query, ArrayList<HashMap<Integer,String>> parameters){
    	int[] batchResult;
    	try {
    		conn.setAutoCommit(false);
			t_pstmt = conn.prepareStatement(query);

			for(HashMap<Integer,String> thisParameter : parameters){
				
				Iterator<Integer> parameterNumbers = thisParameter.keySet().iterator();
				while(parameterNumbers.hasNext()){
					int key = parameterNumbers.next();
					t_pstmt.setString(key, thisParameter.get(key));
				}
				t_pstmt.addBatch();
				t_pstmt.clearParameters();
			}

			batchResult = t_pstmt.executeBatch();
			t_pstmt.clearBatch();
			commit(); 

			return batchResult.length;
		} catch (SQLException e) {
			Log2.error("[DBJob] [SQL Error Code :  "+e.getErrorCode()+"]");
			Log2.error("[DBJob] [SQL Query : "+query+"]");
			Log2.error("[DBJob] ["+e.getMessage()+"]");
			try {
				rollback();
			} catch (Exception e1) {
				Log2.error("[DBJob] ["+e1.getMessage()+"]");
				Log2.error("[DBJob] " + StringUtil.printStackTrace(e1) );
			}
		} catch (Exception e) {
			Log2.error("[DBJob] ["+e.getMessage()+"]");
			Log2.error("[DBJob] " + StringUtil.printStackTrace(e) );
		}
    	
    	return 0;
    }
    
    /**
     * resource release
     */
    public void releaseDB(){
        releaseDB(t_rs, t_pstmt);
        releaseDB(conn);
    }

    public void releaseRs() {
        releaseDB(t_rs, t_pstmt);
    }
    
    public void setAutoCommit(boolean isCommit) throws Exception {
    	conn.setAutoCommit(isCommit);
    }
    
    public void commit() throws Exception {
    	conn.commit();
    }
    
    public void rollback() throws Exception {
    	conn.rollback();
    }
}
