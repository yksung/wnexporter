/*
 * @(#)HttpDownLoad.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.util;

import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.config.RunTimeArgs;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * HttpDownLoad
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class HttpDownLoad {
    public  int FileDownLoad(String TgUrl, String DwFileName){
        URL SourceURL = null;
        InputStream is;
        int data=0;
        try
        {
            if(!TgUrl.equals("")){
                String Temp[] = StringUtil.split(TgUrl, " ");
                TgUrl = Temp[0];
                for(int i=1; i<Temp.length; i++){
                    TgUrl = TgUrl + "%20" + Temp[i] ;
                }
                if(RunTimeArgs.isDebug()) Log2.debug("[HttpDownLoad ] [Target URL" + TgUrl+"]", 3);
                if(RunTimeArgs.isDebug()) Log2.debug("[HttpDownLoad ] [DownLoad File Name" + DwFileName+"]", 3);

                SourceURL = new URL(TgUrl);
            } else{
            	if(RunTimeArgs.isDebug()) Log2.debug("[HttpDownLoad ] [Url Path Nothing!!!.]", 2);
                return -1;
            }

            //file download
            File OutFilterFile = new File(DwFileName);
            FileOutputStream fos = new FileOutputStream(OutFilterFile);

            try {
                is = SourceURL.openStream();
            } catch(MalformedURLException e) {
                Log2.error("[HttpDownLoad ] [MalformedURLException, URL Format Info Error!!!]");
                fos.close();
                return -1;
            } catch( IOException e ) {
                Log2.error("[HttpDownLoad ] [IOException, URL IO Error!!! " + e.getMessage()+"]");
                fos.close();
                return -1;
            }
            byte[] _tmp = new byte[1024*2];

            while ((data = is.read(_tmp)) != -1) {
                fos.write(_tmp, 0, data);
            }
            
            fos.close();
        }catch( IOException e){
            Log2.error("[HttpDownLoad ] [IOException, URL IO Error!!! " + e.getMessage()+"]");
        }
        return 0;

    }
}
