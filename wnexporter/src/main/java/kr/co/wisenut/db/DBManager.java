/*
 * @(#)DBManager.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.db;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.RunTimeArgs;
import kr.co.wisenut.config.datasource.DBVender;
import kr.co.wisenut.config.datasource.DataSource;

/**
 * Created by WISEnut
 * <b>Filename : </b>DBManager.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public abstract class DBManager implements IDBManager {
    private boolean isError = false;
    protected DBColumnData m_columndata;
    protected String characterSet;
    protected String dsnID;
    protected int m_vender;

    /**
     * DBManager constructor
     * @param config
     * @param dsnID
     */
    public DBManager(int vender, String dsnID) {
        m_columndata = new DBColumnData();
        this.m_vender = vender;
        HashMap dscMap = Config.getDataSource();
        DataSource m_datasource = (DataSource) dscMap.get(dsnID);
        characterSet = m_datasource.getChar_set();
        this.dsnID = dsnID;
    }

    /**
     * ResultSet next() call
     * @param rs
     * @return boolean
     */
    public boolean next(ResultSet rs) {
        boolean isBool;
        if(rs == null) {
            return false;
        }
        try {
            isBool = rs.next();
        } catch (SQLException e) {
            Log2.error("[DBManager] [next() SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [next() SQLException "+e.getMessage()+" ]");
            isBool = false;
            isError = true;
            return isBool;
        }
        return isBool;
    }

    /**
     * ResultSet column count
     * @param rs
     * @return int
     */
    public int getColunmCnt(ResultSet rs) {
        int num = 0;
        try {
            if(rs != null) {
                num = rs.getMetaData().getColumnCount();
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [getColunmCnt SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [getColunmCnt SQLException "+e.getMessage()+" ]");
        }
        return num;
    }

    /**
     * setting ResultSet abstract method
     * @param query
     * @throws DBFactoryException
     */
    public abstract void setResultSet(String query) throws DBFactoryException;

    /**
     * PreparedStatement
     * @param conn
     * @param query
     * @param values
     * @return boolean
     * @throws java.sql.SQLException
     * @throws kr.co.wisenut.common.Exception.DBFactoryException
     */
    public int executeQuery(Connection conn, String query, String[][] values)
            throws SQLException, DBFactoryException {
    	PreparedStatement pstmt = null;
    	try {
    		if( values != null){
    			pstmt = conn.prepareStatement(query);
    			pstmt = setPstmt(pstmt, values);
    		}
    		int ret = pstmt.executeUpdate();
    		return ret;
		} catch (SQLException e) {
			throw e;
		} finally {
			releaseDB(pstmt);
		}
        //releaseDB(conn);
    }
    
    public int executeQuery(Connection conn, String query ) throws SQLException, DBFactoryException {
	PreparedStatement pstmt = null;
	try {
		pstmt = conn.prepareStatement(query);
		int ret = pstmt.executeUpdate();
		return ret;
	} catch (SQLException e) {
		throw e;
	} finally {
		releaseDB(pstmt);
	}
	//releaseDB(conn);
	}
    /**
     * @param rs
     * @param idx
     * @param count
     * @return String[][]
     */
    public String[][] getString(ResultSet rs, int idx, int count) throws DBFactoryException {
        String[][] coldata = new String[count][2];
        for(int i=0; i<count; i++){
            //String[][] arr = new String[1][2];
            String[][] arr = getString(rs, idx);
            coldata[i][0] = arr[0][0];
            coldata[i][1] = arr[0][1];
            idx++;
        }
        return coldata;
    }

    /**
     * Get Resultset column data method
     * @param rs ResultSet
     * @param idx column index
     * @return String[][] result
     * @throws DBFactoryException error info
     */
    
    public String getCollName(ResultSet rs,int idx){
    	String result = "";
        ResultSetMetaData meta ;
        try {
            meta = rs.getMetaData();
            result = meta.getColumnName(idx);
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQLException][GetCollName() column name ("+idx+") ]");
            Log2.error("[DBManager] [SQLException][ "
                    +"\n"+IOUtil.StackTraceToString(e)+"\n]");
        }
        return result;
    }
    
    public String[][] getString(ResultSet rs, int idx) throws DBFactoryException {
        debug("[DBManager] [ResultSet Column Index(" + idx + ")]", 4);
        String[][] coldata = new String[1][2];
        ResultSetMetaData meta ;
        try {
            meta = rs.getMetaData();
            String classType = meta.getColumnTypeName(idx).toLowerCase();
            String className = "";
            if(m_vender == DBVender.SYMFOWARE ) {
                className = getSymfowareClassType(classType);
            }else {
                className = meta.getColumnClassName(idx); //Not supported method at symfoware
            }
            if(classType.equals("char")) className = "java.lang.String";
            className = getClassName(className);
            coldata[0][1] = className;
            Object object = rs.getObject(idx);
            if(object != null) {
                coldata[0][0] = ( m_columndata.getColumnData( object, classType) );
                if(!characterSet.equals("")) {
                    String columnData = m_columndata.getColumnData( object, classType);
                    coldata[0][0]
                            = StringUtil.convert(columnData, characterSet);
                }
            } else {
            	if(RunTimeArgs.isDebug()) {
            		Log2.debug("[DBManager] [rs.getObject("+idx+"), " +
                        "columnName=\""+meta.getColumnName(idx)+"\", " +
                        "className=\""+className+"\" is null]", 4);
            	}
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQLException][GetString() column index ("+idx+") ]");
            Log2.error("[DBManager] [SQLException][ "
                    +"\n"+IOUtil.StackTraceToString(e)+"\n]");
        }
        return StringUtil.checkNull( coldata );
    }
    
    public HashMap<String, String> getStringAsMap(ResultSet rs) throws DBFactoryException {
    	HashMap<String, String> row = new HashMap<String, String>();
    	
        ResultSetMetaData meta ;
        try {
            meta = rs.getMetaData();
            
            int colCnt = meta.getColumnCount();
            for(int i = 1 ; i <= colCnt ; i++){
            	String classType = meta.getColumnTypeName(i).toLowerCase();
                String className = "";
                
                if(m_vender == DBVender.SYMFOWARE ) {
                    className = getSymfowareClassType(classType);
                }else {
                    className = meta.getColumnClassName(i); //Not supported method at symfoware
                }
                
                String column_name = meta.getColumnName(i);
                
                
                if(classType.equals("char")) className = "java.lang.String";
                className = getClassName(className);
                
                String data = "";
                Object object = rs.getObject(i);
                if(object != null) {
                	data = ( m_columndata.getColumnData( object, classType) );
                    if(!characterSet.equals("")) {
                        String columnData = m_columndata.getColumnData( object, classType);
                        data = StringUtil.convert(columnData, characterSet);
                    }
                } else {
                	if(RunTimeArgs.isDebug()) {
                		Log2.debug("[DBManager] [rs.getObject("+i+"), " +
                				"columnName=\""+meta.getColumnName(i)+"\", " +
                				"className=\""+className+"\" is null]", 4);
                	}
                }
                
                row.put(column_name, data);
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQLException][getStringAsMap() all column ]");
            Log2.error("[DBManager] [SQLException][ "
                    +"\n"+IOUtil.StackTraceToString(e)+"\n]");
        }
        return row;
    }

    public String addWhereCondition(String query, String[][] condition) {
        int idx = query.indexOf("%s");
        String value = "";
        String type = "";
        if(idx > -1) {
            for(int i=0; condition != null && i<condition.length  ; i++) {
                value = condition[i][0];
                type  = condition[i][1];
                if(type.toLowerCase().equals("java.lang.string") || type.toLowerCase().equals("string")){
                    query = StringUtil.replace(query, "%s"+(i+1), "'"+value+"'");
                }else{
                    query = StringUtil.replace(query, "%s"+(i+1), value);
                }
            }
        }
        return query;
    }

    /**
     * @param conn
     * @param query
     * @param values
     * @return String[]
     */
    public String[][] getQueryData(Connection conn, String query, String[][] values, String seperator) {
        debug("[DBManager] ["+query+"]", 3);
        String [][] retArrs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt =  conn.createStatement();
            rs = stmt.executeQuery(query);

            int columnCnt = getColunmCnt(rs);
            
            //StringBuffer [][] tmpretArrs = new StringBuffer[columnCnt][2];
//            StringBuffer [][] tmpretArrs = new StringBuffer[columnCnt][3]; //colname �뺣낫 諛곕� 異붽�

            retArrs = new String[columnCnt][3]; //
            
            for(int k=0; k<columnCnt; k++) {
//                tmpretArrs[k][0] = new StringBuffer();
//                tmpretArrs[k][1] = new StringBuffer();
//                tmpretArrs[k][3] = new StringBuffer();
                retArrs[k][0] = "";
                retArrs[k][1] = "";
                retArrs[k][2] = "";
            }
            
            int rowCnt = 0;
            
            while(rs.next()) {
            	for(int i=0; i<columnCnt; i++) {
                    if(rowCnt > 0) {
                        retArrs[i][0] += seperator;
                    }else{
                    	retArrs[i][2] += getCollName(rs,i+1);
                    }
                    String[][] arrTmp = getString(rs,i+1);
                    retArrs[i][0] += arrTmp[0][0];
                    retArrs[i][1] += arrTmp[0][1];
                }
                rowCnt++;
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQL Error Query : "+query+"]");
            e.printStackTrace();
        } catch (DBFactoryException e) {
            Log2.error("[DBManager] [DBFactoryException"+e.getMessage()+"]");
        }finally{
        	releaseDB(rs, pstmt);
            releaseDB(rs, stmt);
        }
        
        return StringUtil.checkNull(retArrs);
    }
    
    public ArrayList getQueryData(Connection conn, String query, String[][] values) {
        debug("[DBManager] ["+query+"]", 3);
        
        ArrayList resultValues = new ArrayList();
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt =  conn.createStatement();
            rs = stmt.executeQuery(query);
            int columnCnt = getColunmCnt(rs);
            int index = 0;
            while(rs.next()) {
            	String [] retArry = new String[columnCnt];
            	for(int i=0; i < columnCnt; i++) {
            		String[][] arrTmp = getString(rs,i+1);
            		retArry[i] = arrTmp[0][0];
            	}
            	resultValues.add(index, retArry);
            	index++;
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQL Error Query : "+query+"]");
            e.printStackTrace();
        } catch (DBFactoryException e) {
            Log2.error("[DBManager] [DBFactoryException"+e.getMessage()+"]");
        }finally{
        	releaseDB(rs, pstmt);
            releaseDB(rs, stmt);
            releaseDB(conn);
        }
        
        return resultValues;
    }

    /**
     * @param conn
     * @param query
     * @param values
     * @return String[]
     */
    public String[][] getQueryData(Connection conn, String query, String[][] values, String seperator, int maxRow) {
        debug("[DBManager] ["+query+"]", 3);
        String [][] retArrs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            if(query.indexOf("%s1") > -1) {
                String strQuery = addWhereCondition(query, values);
                stmt =  conn.createStatement();
                rs = stmt.executeQuery(strQuery);
            }else {
                pstmt = conn.prepareStatement(query);
                try {
                    pstmt.setMaxRows(maxRow);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                pstmt = setPstmt(pstmt, values);
                rs = pstmt.executeQuery();
            }
            int columnCnt = getColunmCnt(rs);
            StringBuffer [][] tmpretArrs = new StringBuffer[columnCnt][2];
            retArrs = new String[columnCnt][2];
            for(int k=0; k<columnCnt; k++) {
                tmpretArrs[k][0] = new StringBuffer();
                tmpretArrs[k][1] = new StringBuffer();
                retArrs[k][0] = "";
                retArrs[k][1] = "";
            }
            int rowCnt = 0;
            while(rs.next()) {
                for(int i=0; i<columnCnt; i++) {
                    if(rowCnt > 0) {
                        tmpretArrs[i][0].append(seperator);
                        tmpretArrs[i][1].append(seperator);
                    }
                    String[][] arrTmp = getString(rs, i+1);
                    tmpretArrs[i][0].append(arrTmp[0][0]);
                    tmpretArrs[i][1].append(arrTmp[0][1]);
                }
                rowCnt++;
            }

            for(int n=0; n<columnCnt; n++) {
                retArrs[n][0] = tmpretArrs[n][0].toString();
                retArrs[n][0] = StringUtil.replace(retArrs[n][0], "&", "&amp;");
                retArrs[n][0] = StringUtil.replace(retArrs[n][0], "<", "&lt;");
                retArrs[n][0] = StringUtil.replace(retArrs[n][0], ">", "&gt;");
                retArrs[n][1] = tmpretArrs[n][1].toString();
            }
        } catch (SQLException e) {
            Log2.error("[DBManager] [SQL Error Code : "+e.getErrorCode()+" ]");
            Log2.error("[DBManager] [SQL Error Query : "+query+"]");
            e.printStackTrace();
        } catch (DBFactoryException e) {
            Log2.error("[DBManager] [DBFactoryException"+e.getMessage()+"]");
        } finally{
        	releaseDB(rs, pstmt);
            releaseDB(rs, stmt);
        }
        
        return StringUtil.checkNull(retArrs);
    }
    /**
     * @param pstmt
     * @param values
     * @return PreparedStatement
     * @throws DBFactoryException
     * @throws java.sql.SQLException
     */
    protected PreparedStatement setPstmt(PreparedStatement pstmt, String[][] values)
            throws DBFactoryException, SQLException {
        for(int i=0; values != null && i<values.length ; i++) {
            String value = values[i][0];
            String type  = values[i][1];
            debug("[DBManager] [pstmt set value("+value+"), type("+type+")]", 3);
            if(type.equals(IDBManager.INTEGER)) {
                pstmt.setInt(i+1, Integer.parseInt(value));
            } else if(type.equals(IDBManager.LONG)) {
                pstmt.setLong(i+1, Long.parseLong(value));
            } else if(type.equals(IDBManager.SHORT)) {
                pstmt.setShort(i+1, Short.parseShort(value));
            } else if(type.equals(IDBManager.DOUBLE)) {
                pstmt.setDouble(i+1, Double.parseDouble(value));
            } else if(type.equals(IDBManager.DECIMAL)) {
                pstmt.setBigDecimal(i+1, BigDecimal.valueOf(Long.parseLong(value)));
            } else if(type.equals(IDBManager.STRING)) {
                try {
                    if(!characterSet.equals("")) {
                        String[] encString = StringUtil.split(characterSet, ",");
                        if(encString.length == 2) {
                            pstmt.setString(i+1, StringUtil.convert(value, encString[1], encString[0]));
                        }
                    }else{
                        pstmt.setString(i+1, value);
                    }
                } catch (UnsupportedEncodingException e) {
                    IOUtil.StackTraceToString(e);
                }
            } else {
                throw new DBFactoryException
                        (": DBManager Class PreparedStatement error type ("+type+")");
            }
        }
        debug("[DBManager] [pstmt set end(success)]", 3);
        return pstmt;
    }
    

    private static String[] className = new String[]{"Integer","Short", "Long","Double","BigDecimal","String"};

    /**
     *
     * @param classNm dbms classname
     * @return java ClassName
     */
    private String getClassName(String classNm) {
        String ret = classNm;
        for(int i=0;i<className.length; i++) {
            if(classNm.equals(className[i])) {
                if(classNm.equals("BigDecimal")) {
                    ret  = "java.math.BigDecimal";
                }else{
                    ret = "java.lang."+classNm;
                }
                break;
            }
        }
        return ret;
    }

    /**
     * Using symfoware dbms
     * @param classType symfoware class type
     * @return java class type
     */
    private String getSymfowareClassType(String classType) {
        String className = "";
        if(classType.equals("integer")) {
            className = "java.lang.Integer";
        }else if(classType.indexOf("char") > -1) {
            className = "java.lang.String";
        }else if(classType.equals("long")) {
            className = "java.lang.Long";
        }else if(classType.equals("decimal")) {
            className = "java.math.BigDecimal";
        }else if(classType.equals("smallint")) {
            className = "java.lang.Short";
        }else if(classType.equals("double")) {
            className = "java.lang.Double";
        }
        return className;
    }


    /**
     * @return boolean
     */
    public boolean isError() {
        return isError;
    }

    /**
     * @param msg
     */
    protected void debug(String msg){
        Log2.debug(msg, 4);
    }
    protected void debug(String msg, int level) {
        Log2.debug(msg, level);
    }
    protected void error(String err) {
        Log2.error(err);
    }
    protected void error(Exception e) {
        Log2.error(e);
    }
    /**
     * @param conn
     */
    public void releaseDB(Connection conn){
        if(conn != null) {
            try {
                conn.close();
            }catch(Exception e){
                Log2.error("[DBManager] [releaseDB "+e.getMessage()+"]");
            }
        }
    }

    public void releaseDB(ResultSet rs, Statement stmt) {
        releaseDB(rs);
        releaseDB(stmt);
    }

    public void releaseDB(ResultSet rs, PreparedStatement pstmt) {
        releaseDB(rs);
        releaseDB(pstmt);
    }

    public void releaseDB(ResultSet rs){
        if(rs != null) {
            try {
                rs.close();
            }catch(Exception e){
                Log2.error("[DBManager] [releaseDB "+e.getMessage()+"]");
            }
        }
    }

    public void releaseDB(Statement stmt){
        if(stmt != null) {
            try {
                stmt.close();
            }catch(Exception e){
                Log2.error("[DBManager] [releaseDB "+e.getMessage()+"]");
            }
        }
    }

    public void releaseDB(PreparedStatement pstmt){
        if(pstmt != null) {
            try {
                pstmt.close();
            }catch(Exception e){
                Log2.error("[DBManager] [releaseDB "+e.getMessage()+"]");
            }
        }
    }
}
