/*
 * @(#)IDBManager.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.db;

import kr.co.wisenut.common.Exception.DBFactoryException;
import java.sql.*;

/**
 * Created by WISEnut
 * <b>Filename : </b>IDBManager.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public interface IDBManager {
    public final static String INTEGER = "Integer";
    public final static String LONG = "Long";
    public final static String DECIMAL = "Decimal";
    public final static String DOUBLE = "Double";
    public final static String STRING = "String";
    public final static String SHORT = "Short";
    public final static String DATETIME = "Datetime";

    public String[][] getString(ResultSet rs, int idx) throws SQLException, DBFactoryException;

    public String[][] getString(ResultSet rs, int idx, int count) throws DBFactoryException;

    public void setResultSet(String query) throws DBFactoryException;

    public boolean next(ResultSet rs);

    public int getColunmCnt(ResultSet rs);

    public int executeQuery(Connection conn, String query, String[][] values) throws SQLException, DBFactoryException;

    public String[][] getQueryData(Connection conn, String query, String[][] values, String seperator);

    public void releaseDB(Connection conn);

    public void releaseDB(ResultSet rs, Statement stmt);

    public void releaseDB(ResultSet rs, PreparedStatement pstmt);
}
