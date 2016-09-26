/*
 * @(#)GetDataSource.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.config.datasource;

import org.jdom.Element;
import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.util.XmlUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WISEnut
 * <b>Filename : </b>GetDataSource.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class GetDataSource extends XmlUtil {
    private Element rootElement;

    public GetDataSource(String path) throws ConfigException {
        super(path);
        rootElement = getRootElement();
    }

    public HashMap getDataSource() throws ConfigException, DBFactoryException {
        List lds = rootElement.getChildren("Database");
        int size = lds.size();
        HashMap mapDB = new HashMap(size);
        String[] duplecate = new String[size];
        String id = "";
        DataSource ds = null;
        String[] driver = null;
        for(int i=0; i<size;i++){
            ds = new DataSource();
            Element element = (Element) lds.get(i);
            id = element.getChildText("Id");

            //check duplicated DataSource ID
            for(int j=0; j<size;j++){
                //debug(duplecate[j]);
                if(duplecate[j] != null && duplecate[j].equals(id)){
                    throw new ConfigException(": Duplicated DataSource ID. " +
                            "Please check the <DataSource> - <Database> - <Id> setting in configuration file.");
                }
            }
            duplecate[i] = id;
            ds.setId(id);
            ds.setChar_set(getElementChildText(element, "CharSet", ""));
            ds.setVender(getDBVender(getElementChildText(element, "Vendor")));
            ds.setSid(getElementChildText(element, "SID", ""));
            ds.setDataBaseName(getElementChildText(element, "DatabaseName"));
            ds.setUser(getElementChildText(element, "User"));
            ds.setPwd(getElementChildText(element, "Password"));
            ds.setPort(getElementChildText(element, "PortNumber"));
            ds.setServerName(getElementChildText(element, "ServerName"));
            ds.setJdbcUrl(getElementChildText(element, "Url", ""));

            List listDriver = element.getChildren("Driver");
            int dSize = listDriver.size();
            driver  = new String[dSize];
            for (int j = 0; j < dSize; j++) {
                Element eleDriver = (Element) listDriver.get(j);
                driver[j] = eleDriver.getText() ;
                ds.setDriver(driver);
            }

            ds.setClassname(getElementChildText(element, "ClassName"));
            mapDB.put(id, ds);
        }
        return mapDB;
    }

     protected int getDBVender(String vender) throws DBFactoryException {
        int _ivender = -1;
        vender = vender.toLowerCase();
        if(vender.equals("oracle")){
            _ivender = DBVender.ORACLE;
        } else if(vender.equals("oracle_oci")) {
            _ivender = DBVender.ORACLE_OCI;
        } else if(vender.equals("mssql")) {
            _ivender = DBVender.MSSQL;
        } else if(vender.equals("mssql2005")) {
            _ivender = DBVender.MSSQL2005;
        } else if(vender.equals("mysql")) {
            _ivender = DBVender.MYSQL;
        } else if(vender.equals("informix")) {
            _ivender = DBVender.INFORMIX;
        } else if(vender.equals("db2")) {
            _ivender = DBVender.DB2;
        }else if(vender.equals("as400")) {
            _ivender = DBVender.AS400;
        } else if(vender.equals("sybase")) {
            _ivender = DBVender.SYBASE;
        } else if(vender.equals("access")) {
            _ivender = DBVender.ACCESS;
        } else if(vender.equals("unisql")) {
            _ivender = DBVender.UNISQL;
        } else if(vender.equals("kdbapp")) {
            _ivender = DBVender.KDB_APP;
        } else if(vender.equals("postgre")) {
            _ivender = DBVender.POSTGRE;
        } else if(vender.equals("derby")) {
            _ivender = DBVender.DERBY;
        } else if(vender.equals("symfoware")) {
            _ivender = DBVender.SYMFOWARE;
        } else if(vender.equals("cubrid")) {
            _ivender = DBVender.CUBRID;
        } else {
            throw new DBFactoryException("Please check the DBVender in datasource.xml.");
        }
        return _ivender;
    }
}
