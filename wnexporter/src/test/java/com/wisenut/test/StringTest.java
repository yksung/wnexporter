package com.wisenut.test;

import java.util.ArrayList;

import kr.co.wisenut.common.util.StringUtil;

public class StringTest {

	public static void main(String[] args) {
		String str = "1PCS CUFFLINKS AND TIE CLIPS METAL   1PCS CUFFLINKS AND TIE CLIPS METAL";
		String sep = "(,|\\d\\s{1,2}|\\dPCS\\s|\\dYD\\s|\\dEA\\s)";
		String[] tokens = str.split(sep, -1);
		
		for(String token :  tokens ){			
			System.out.println(">>>> " + token);
		}
		System.out.println("\n");
		String[] tokens2 = splitUsingIndexOf(str, "   ");
		for(String token :  tokens2 ){			
			System.out.println(">>>> " + token);
		}
	}

	
	public static String[] splitUsingIndexOf(String splittee, String splitter){
		ArrayList<String> list = new ArrayList<String>();
		if(splittee.endsWith(splitter)){
			splittee += " "+splitter;
		}
        int pos = 0, end;
        while ((end = splittee.indexOf(splitter, pos)) >= 0) {
            list.add(splittee.substring(pos, end));
            pos = end + splitter.length();
        }
        String[] result = new String[list.size()];
        for(int i=0; i<result.length; i++){
        	result[i] = list.get(i);
        }
        
        return result;
	}
}
