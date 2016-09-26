package kr.co.wisenut;

import java.io.IOException;

import kr.co.wisenut.msg.InfoMsg;
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
public class Exporter {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        InfoMsg.header();
        
        Exporter exporter = new Exporter();
        exporter.createRuntimeArgument(args);
        
        if("".equals(RunTimeArgs.getLogdir())){
        	RunTimeArgs.setLogdir("." + FileUtil.fileseperator + "log" + FileUtil.fileseperator + "exporter");
        }
        
        if("".equals(RunTimeArgs.getPiddir())){
        	RunTimeArgs.setPiddir("." + FileUtil.fileseperator + "pid");
        }
        
        //logpath, idDebug(디버그여부)
        Log2.setLogger(RunTimeArgs.getLogdir(), RunTimeArgs.isDebug());
        
        PidUtil pidUtil = null;
        
        try {
            long start = System.currentTimeMillis();
            Log2.out("[start export] read config..");
            
            SetConfig.getConfig();
            
            if(RunTimeArgs.isDebug()){
            	Log2.debug("[arguments infomation] -id " + RunTimeArgs.getExportid());
            	Log2.debug("[arguments infomation] -conf " + RunTimeArgs.getConf());
            	Log2.debug("[arguments infomation] -logdir " + RunTimeArgs.getLogdir());
            	Log2.debug("[arguments infomation] -piddir " + RunTimeArgs.getPiddir());
            	Log2.debug("[arguments infomation] -failQuery " + RunTimeArgs.isFailQuery());
            	Log2.debug("[arguments infomation] -mode " + RunTimeArgs.getMode());
            	
            	Log2.debug("[config infomation] dsn : " + Config.getDsn());
            	Log2.debug("[config infomation] Select-key-column : " + Config.getSelect_key_num() + " / " + Config.getSelect_key_name());
            	Log2.debug("[config infomation] Select-goods-column : " + Config.getSelect_goods_num() + " / " + Config.getSelect_goods_name());
            	Log2.debug("[config infomation] Select-price-column : " + Config.getSelect_price_num() + " / " + Config.getSelect_price_name());
            	Log2.debug("[config infomation] Select-country-column : " + Config.getSelect_country_num() + " / " + Config.getSelect_country_name());
            	Log2.debug("[config infomation] Select-query : " + Config.getSelect_query());
            	
            	Log2.debug("[config infomation] Search-ip : " + Config.getSearch_ip());
            	Log2.debug("[config infomation] Search-port : " + Config.getSearch_port());
            	Log2.debug("[config infomation] Search-thread : " + Config.getSearch_thread());
            	Log2.debug("[config infomation] Search-index-filde : " + Config.getIndex_field());
            	Log2.debug("[config infomation] Search-class-field : " + Config.getClass_field());
            	Log2.debug("[config infomation] Search-page-count : " + Config.getPage_count());
            	Log2.debug("[config infomation] Search-recommend-count : " + Config.getRecom_count());
            	Log2.debug("[config infomation] Search-recommend-option : " + Config.getRecom_option());
            	Log2.debug("[config infomation] Search-no-separator : " + Config.getNo_separator());
            	Log2.debug("[config infomation] Search-goods-split-regx<else> : " + Config.getGoods_split_regxs().get("else"));
            	
            	Log2.debug("[config infomation] Update-output-key-column : " + Config.getUpdate_output_key_number());
            	Log2.debug("[config infomation] Update-order-key-column : " + Config.getUpdate_order_key_number());
            	Log2.debug("[config infomation] Update-price-column : " + Config.getUpdate_price_number());
            	Log2.debug("[config infomation] Update-country-column : " + Config.getUpdate_country_number());
            	Log2.debug("[config infomation] Update-goodsname-column : " + Config.getUpdate_goodsname_number());
            	Log2.debug("[config infomation] Update-hscode-seq-column : " + Config.getUpdate_hscode_seq_number());
            	Log2.debug("[config infomation] Update-hscode-val-column : " + Config.getUpdate_hscode_val_number());
            	Log2.debug("[config infomation] Update-query : " + Config.getUpdate_query());
            }
            
            //PID 생성함
            pidUtil = new PidUtil(RunTimeArgs.getExportid(), RunTimeArgs.getPiddir());
			try {
				if (pidUtil.existsPidFile()) {
					Log2.error("[Exporter already running]" + StringUtil.newLine);
					System.exit(-1);
				}
				pidUtil.makePID();
			} catch (IOException e) {
				Log2.error("[Make PID file fail " + "\n" + IOUtil.StackTraceToString(e) + "\n]");
			}
			
            //실행
			Run export;
			if(RunTimeArgs.isFailQuery()){
				export = new Run(RunTimeArgs.isFailQuery());
			}else{
				export = new Run();
			}
        	
        	if(!export.run()){
        		pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
        		Log2.error("Export Fail!!");
        		System.exit(-1); 
        	}
            
            pidUtil.deletePID(); // Normal Exit PidUtil Object
            long end = System.currentTimeMillis();
            double div = ((double)(end-start)/1000);
            Log2.out("[info] [end export] run time: "+div+" sec");
        } catch (ConfigException e) {
			Log2.error("[error] [ConfigException: " + IOUtil.StackTraceToString(e) + StringUtil.newLine + "]");
			if(pidUtil != null)
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			System.exit(-1); 
		} catch (DBFactoryException e) {
			Log2.error("[error] [DBFactory Exception " + "\n" + IOUtil.StackTraceToString(e) + "\n]");
			if(pidUtil != null)
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			System.exit(-1); 
		} catch (Throwable e) {
			Log2.error("[error] [Throwable message]" + "[" + IOUtil.StackTraceToString(e) + "]");
			if(pidUtil != null)
			pidUtil.leaveErrorPID(); // Abnormal Exit PidUtil Object
			System.exit(-1);
		}
    }

    /**
     *
     * @param args
     * @return RunTimeArgs
     */
    private void createRuntimeArgument(String[] args) {
        int ret  = RunTimeArgs.readargs(args) ;
        if ( ret != 0) {
            printArgsError(ret) ;
            System.exit(-1);
        }
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
