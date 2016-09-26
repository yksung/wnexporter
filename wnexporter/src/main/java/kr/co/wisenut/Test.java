package kr.co.wisenut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import QueryAPI453.Search;
import kr.co.wisenut.msg.InfoMsg;
import kr.co.wisenut.config.Constants;
import kr.co.wisenut.config.RunTimeArgs;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.SetConfig;
import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.PidUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.exporter.Run;
import kr.co.wisenut.exporter.business.SearchWorker;

/**
 * Created by WISEnut
 * Copyright 2001-2013 WISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Release 2013/07/22
 *
 * Date: 2013. 07. 22
 * @author  WISEnut<br>
 * @version 1.0.0<br>
 */
public class Test {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	int count = 0;
    	System.out.println(count++%2000);
    	
    	count =0;
    	System.out.println(++count%2000);
    	
    	
    	
    }

    /**
     *
     * @param args
     * @return RunTimeArgs
     */
    private RunTimeArgs createRuntimeArgument(String[] args) {
        RunTimeArgs rta = new RunTimeArgs();
        int ret  = rta.readargs(args) ;
        if ( ret != 0) {
            printArgsError(ret) ;
            System.exit(-1);
        }
        return rta;
    } 

    /**
     * return code define
     * 1: none argument parameter or request help message
     * 2: missing config file
     * 3: missing runtime mode
     * 4: empty mode
     * 5: unknown mode code
     * @param retCode error code
     */
    private void printArgsError(int retCode) {
        switch(retCode){
            case 1 :
                InfoMsg.usage();
                break;
            case 2 :
                System.out.println("Missing configuration file.");
                InfoMsg.usage();
                break;
        }
    }
}
