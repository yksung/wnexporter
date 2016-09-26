/*
 * @(#)EncryptUtil.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * EncryptUtil
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class EncryptUtil {
    static final int key = 0xF3; // encrypt/decrypt key

    public static String MD5(String str) {
        return MD5(str.getBytes());
    }

    /**
     * MD5  encrypt method
     * @param msg original data
     * @return encrypt data
     */
    public static String MD5(byte[] msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg);

            byte[] digest = md.digest();
            StringBuffer buf = new StringBuffer(digest.length * 2);

            for (int i = 0; i < digest.length; i++) {
                int intVal = digest[i] & 0xff;

                if (intVal < 0x10) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(intVal));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException ne) {
            return null;
        }
    }

    /**
     * SHA-1 encrypt method
     * @param data original data
     * @return encrypt data
     */
    public static String SHA1(byte[] data) {
        MessageDigest md ;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(data);
        byte[] encdata = md.digest();

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<encdata.length; i++){
            sb.append(Integer.toHexString( encdata[i] & 0xff ));
        }

        return sb.toString();
    }

    public static String encryptString(String str) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < str.length(); i++){
            int c = str.charAt(i);
            sb.append(Integer.toHexString(c ^ key)).append(" ");
        }
        return sb.toString();
    }

    public static String decryptString(String str){
        String[] arry = StringUtil.split(str, " ");
        StringBuffer sb = new StringBuffer();
        for(int n=0; n < arry.length; n++){
            String temp = arry[n];
            char ch = (char)(Integer.parseInt(temp, 16) ^ key);
            sb.append(ch);
        }
        return sb.toString();
    }

    public static boolean isHexa(String str){
        boolean isHexa = false;
        if (str.startsWith("0x00")) {
             isHexa = true;
        }

        return isHexa;
    }
}
