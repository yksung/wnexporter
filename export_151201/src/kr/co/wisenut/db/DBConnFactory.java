/*
 * @(#)DBConnFactory.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.db;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.EncryptUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.config.RunTimeArgs;
import kr.co.wisenut.config.datasource.Dsn;
import kr.co.wisenut.config.datasource.DBVender;
import kr.co.wisenut.config.datasource.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.util.Properties;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.io.File;

/**
 * Created by WISEnut
 * <b>Filename : </b>DBConnFactory.java<p>
 * Copyright 2001-2009 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * SLA Exporter Release 19 March 2009
 *
 * Date: 2009. 03. 19
 * @author  WISEnut<br>
 * @version 1.0.5<br>
 */
public class DBConnFactory extends DBVender {
    private HashMap m_dsMap;

    public DBConnFactory(HashMap dsMap) {
        this.m_dsMap = dsMap;
        /*try {
            this.JDBC_ROOT = FileUtil.lastSeparator("lib");
        } catch (Exception e) {
            IOUtil.StackTraceToString(e);
        }*/
    }

    public Connection getConnection(String dsn) throws DBFactoryException {
    	if(RunTimeArgs.isDebug()) Log2.debug("[DBConnFactory] [Get DB Connection]" ,3);
        Connection conn ;

         DataSource dsource = (DataSource) m_dsMap.get(dsn);
        if(dsource == null) {
            throw new DBFactoryException(":Missing DataSource setting in datasource.xml. dsn name("+dsn+")");
        }
        Dsn _dsn = getDsn(dsource);
        if(_dsn == null) {
            throw new DBFactoryException(":Missing DSN setting in datasource.xml. dsn name("+dsn+")");
        }
        if (dsource.getVender() == POSTGRE || dsource.getVender() == ACCESS
                || dsource.getVender() == SYMFOWARE) {
            try  {
                Class.forName(_dsn.getDriver());
                conn = DriverManager.getConnection(_dsn.getUrl(), _dsn.getPrps());
            } catch (Exception e) {
                throw new DBFactoryException(": DB connection fail."+ e.getMessage());
            }
        } else {
            try {
                Class DBMSClass = _dsn.getDbms();
                Driver instance = (Driver)DBMSClass.newInstance();
                conn = instance.connect(_dsn.getUrl(), _dsn.getPrps());
            } catch (SQLException se) {
                throw new DBFactoryException(": DB connection fail. "+ se.getMessage());
            } catch (InstantiationException ne) {
                throw new DBFactoryException(": Unable to load the JDBC Driver. "+ne.getMessage());
            } catch (IllegalAccessException e) {
                throw new DBFactoryException(": JDBC IllegalAccessException. "+ e.getMessage());
            }
            Log2.debug("[info] [DBConnFactory] [DB Connection: Successful]",3);
        }
        return conn;
    }

    private Dsn getDsn(DataSource dsource){
        Dsn dsn = new Dsn();
        Properties prps = new Properties();
        //2006.09.06 database userid와 password가 encryption되어 있을 경우
        //decryption하는 기능을 추가함
        String userID = dsource.getUser();
        String userPWD = dsource.getPwd();
        if(EncryptUtil.isHexa(userID)) {
            if(userID.length() > 4 && userPWD.length() >4){
                userID = userID.substring(4, userID.length());
                userPWD = userPWD.substring(4, userPWD.length());
            }
            userID =  EncryptUtil.decryptString(userID) ;
            userPWD =  EncryptUtil.decryptString(userPWD) ;
        }
        prps.setProperty("user", userID);
        prps.setProperty("password", userPWD);
        //prps.setProperty("encoding", charSet);
        if(dsource.getDataBaseName() != null && !dsource.getDataBaseName().equals("") && dsource.getVender() != SYMFOWARE){
            prps.setProperty("DatabaseName", dsource.getDataBaseName());
        }

        URLClassLoader loader ;
        try{
            dsn.setPrps(prps);
            switch (dsource.getVender()) {
                case ORACLE:		//by oracle
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:oracle:thin:@" + dsource.getServerName()
                            +":"+dsource.getPort()+":"+dsource.getSid());
                    break;
                case ORACLE_OCI:		//by oracle oci
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:oracle:oci8:@" + dsource.getServerName());
                    break;
                case MSSQL:		//by mssql v7, v2000
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    //dsn.setUrl("jdbc:jtds:sqlserver://" + dsource.getServerName()+":"
                    // +dsource.getPort()+ "/" + dsource.getDataBaseName());
                    dsn.setUrl("jdbc:jtds:sqlserver://" + dsource.getServerName()
                            +":"+dsource.getPort()+ "/" + dsource.getDataBaseName());
                    break;
                case MSSQL2005:		//by v2005
                    //com.microsoft.sqlserver.jdbc.SQLServerDriver
                    //jdbc:sqlserver://61.82.137.177:1433;database=arq
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:sqlserver://" + dsource.getServerName()
                            +":"+dsource.getPort()+ ";database=" + dsource.getDataBaseName());
                    break;
                    //by mysql  old
                    //dsn.setDriver("org.gjt.mm.mysql.Driver");
                case MYSQL:     //by mysql v4.x
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:mysql://" + dsource.getServerName()
                            +":"+dsource.getPort()+ "/" + dsource.getDataBaseName());
                    break;
                case INFORMIX:		//by INFORMIX
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:informix-sqli://" + dsource.getServerName()
                            +":"+dsource.getPort()+ "/" +dsource.getDataBaseName());
                    break;
                case DB2:		//by DB2 8.x connect
                    //dsn.setDriver("COM.ibm.db2.jdbc.app.DB2Driver");
                    //dsn.setDriver("COM.ibm.db2.jdbc.net.DB2Driver");
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    if(dsource.getPort().length() < 1){
                        dsn.setUrl("jdbc:db2://"+ dsource.getServerName()
                                +"/"+dsource.getDataBaseName());
                    } else{
                        dsn.setUrl("jdbc:db2://"+ dsource.getServerName()
                                +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    }
                    break;
                case AS400:		//by AS400 connect
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    if(dsource.getPort().length() < 1){
                        dsn.setUrl("jdbc:as400://"+ dsource.getServerName()
                                +"/"+dsource.getDataBaseName());
                    } else{
                        dsn.setUrl("jdbc:as400://"+ dsource.getServerName()
                                +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    }
                    break;
                case UNISQL:		//by UNISQL
                    //dsn.setDriver("unisql.jdbc.driver.UniSQLDriver");
                    //con = Driver.Manager.getConnection("jdbc:unisql:10.20.30.40:"+port+":"+dbName+":::", userId, pwd);
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:unisql:"+dsource.getServerName()
                            +":"+dsource.getPort()+":"+dsource.getDataBaseName()+":::");
                    break;
                case SYBASE:		//by SYBASE
                    //dsn.setDriver("com.sybase.jdbc2.jdbc.SybDriver");
                    //For jConnect 5.x com.sybase.jdbc2.jdbc.SybDriver
                    //jdbc:sybase:Tds:172.30.1.131:6789/junet"
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));    //jdbc:sybase:Tds:myserver
                    dsn.setUrl("jdbc:sybase:Tds:"+dsource.getServerName()
                            +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    break;
                case POSTGRE:		//by POSTGRE
                    dsn.setDriver("org.postgresql.Driver");
                    dsn.setUrl("jdbc:postgresql://"+dsource.getServerName()
                            +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    break;
                case ACCESS:		//by ACCESS
                    dsn.setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
                    dsn.setUrl("jdbc:odbc:"+dsource.getServerName());
                    break;
                case ALTIBASE:   //by ALTIBASE 4
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:Altibase://"+dsource.getServerName()
                            +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    break;
                case CUBRID:		//by CUBRID
                    //cubrid.jdbc.driver.CUBRIDDriver
                    //jdbc:cubrid:210.216.33.250:43300:demodb
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:cubrid:"+dsource.getServerName()
                            +":"+dsource.getPort()+":"+dsource.getDataBaseName());
                    break;
                case DERBY: //by SLA on DERBY
                    //org.apache.derby.jdbc.ClientDriver
                    //jdbc:derby://211.39.140.159:1368/sla;create=true
                    loader = setJDBCClassLoader(dsource) ;
                    dsn.setDbms(loader.loadClass(dsource.getClassname()));
                    dsn.setUrl("jdbc:derby://"+dsource.getServerName()+":"+dsource.getPort()+"/"+dsource.getDataBaseName()+";create=true");
                    break;
                case SYMFOWARE:		//by SYMFOWARE
                    dsn.setDriver(dsource.getClassname());
                    dsn.setUrl("jdbc:symford://"+dsource.getServerName()
                            +":"+dsource.getPort()+"/"+dsource.getDataBaseName());
                    break;
                default:
                    break;
            }

            String jdbcUrl = dsource.getJdbcUrl();
            if(jdbcUrl != null && !jdbcUrl.equals("")) {
                dsn.setUrl(jdbcUrl);
                if(RunTimeArgs.isDebug()) Log2.debug("[DBConnFactory] [JDBC URL : " + jdbcUrl + "]", 3);
            }
        }catch(ClassNotFoundException e){
            Log2.error("[DBConnFactory] [Not Found JDBC Class : "
            +"\n"+IOUtil.StackTraceToString(e)+"\n]");
        }

        return dsn;
    }

    /**
     *
     * @param dsource DataSource
     * @return URLClassLoader
     */
    public URLClassLoader setJDBCClassLoader(DataSource dsource){
        URL url[] = new URL[dsource.driverSize()];
        URLClassLoader loader = null;
        String[] JDBC_ROOT = dsource.getDriver();
        try{
            for(int i=0; i<url.length; i++) {
                url[i] = new File(JDBC_ROOT[i]).toURL();
            }

            loader = new URLClassLoader(url);
        }catch(MalformedURLException e){
             Log2.error("[DBConnFactory] [Please check the jdbc folder."
                    +"\n"+IOUtil.StackTraceToString(e)+"\n]");
        }
        return loader;
    }
  /**
     *
     * @param dsn String
     * @return dbms vendor
     * @throws DBFactoryException  error info
     */
    public int getDbmsType(String dsn) throws DBFactoryException {
        if( (DataSource)(m_dsMap.get(dsn)) == null) {
            String debug = "<Database/> configuration value "+StringUtil.newLine ;
            Collection collection = m_dsMap.values();
            Iterator ite = collection.iterator();
            while(ite.hasNext()) {
                debug += " Id=\""+((DataSource)ite.next()).getId() +"\""+StringUtil.newLine;
            }
            if(RunTimeArgs.isDebug()) Log2.debug("[DBConnFactory] [ "+debug+"]", 3);
            if( ((DataSource)(m_dsMap.get(dsn))) == null) {
                Log2.error("Unable to read the dsn=\""+dsn+"\" and id in datasource.xml." +
                        " Please check the <DataSource> <name>"+dsn);
                return -1;
            }
            throw new DBFactoryException("Unable to read the dsn=\""+dsn+"\" and id in datasource.xml." +
                         " Please check the <DataSource> <name>"+dsn);
        }

        return ((DataSource)(m_dsMap.get(dsn))).getVender();
    }

    protected void error(String err) {
        Log2.error(err);
    }

    protected void debug(String msg) {
        Log2.debug(msg, 4);
    }
}
