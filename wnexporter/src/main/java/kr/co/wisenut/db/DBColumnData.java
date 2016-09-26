/*
 * @(#)DBColumnData.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.db;

import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.common.util.StringUtil;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;

/**
 * Created by WISEnut
 * <b>Filename : </b>DBColumnData.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class DBColumnData {

    /**
     * Get column object type method
     * @param object column object
     * @param typeName  column type
     * @return  value column Value
     */
    public String getColumnData(Object object, String typeName) {
        String value = "";
        if(object != null) {
            String className = object.getClass().getName();
            try {
                //clob -> typeName, mssql -> java.sql.Clob type.
                if (className.equals("java.lang.String")
                        && (typeName.equals("text")||typeName.equals("ntext"))
                    ) {
                    value = (String) object;
                }else if(typeName.equals("clob")
                        ||typeName.equals("text")||typeName.equals("ntext")
                        ||object.getClass().getName().equals("java.sql.Clob")){
                    Reader reader = ((Clob)object).getCharacterStream();
                    BufferedReader br = new BufferedReader(reader);
                    StringBuffer sb = new StringBuffer();
                    String line ;
                    try {
                        while( (line = br.readLine()) != null){
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        Log2.error("[DBColumnData] [Get CLOB ColumnData : "
                                + IOUtil.StackTraceToString(e)
                                +"\n]");
                    }finally{
                        if(br != null) br.close();
                        if(reader != null) reader.close();
                        if(sb != null){
                            value = sb.toString();
                        }
                    }
                }else if (className.equals("java.lang.String")) {
                    value = (String) object;
                } else if (className.equals("java.lang.Integer")) {
                    value = object.toString();
                } else if (className.equals("java.lang.Short")) {
                    value = object.toString();
                } else if (className.equals("java.lang.Long")) {
                    value = object.toString();
                } else if (className.equals("java.lang.Double")) {
                    value = object.toString();
                } else if (className.equals("java.math.BigDecimal")) {
                    value = object.toString();
                } else if (className.equals("java.lang.Boolean")) {
                    value = object.toString();
                } else if (className.equals("java.math.BigInteger")) {
                    value = object.toString();
                } else if (className.equals("java.sql.Timestamp")) {
                    value = object.toString();
                } else if (className.equals("java.sql.Date")) {
                    value = object.toString();
                    //oracle.sql.OPAQUE
                    //}else if (className.equals("oracle.xdb.XMLType")) {
                    //     value =  ((XMLType)object).getStringVal();
                } else if (className.equals("com.sybase.jdbc2.tds.SybTimestamp")) {
                    value = object.toString();
                } else {
                    Log2.error("[DBColumnData] [GetColumnData() " +
                            "Unsupported class  Name: " + className+
                            " Please Check Field Type in the Table]");
                }
            } catch (Exception e) {
                Log2.error("[DBColumnData] [GetColumnData() : "
                        +"\n"+IOUtil.StackTraceToString(e)+"\n]");
            }
        }
        return StringUtil.checkNull( value );
    }
}
