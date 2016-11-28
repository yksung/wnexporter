package kr.co.wisenut.exporter.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import QueryAPI453.Search;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.Constants;
import kr.co.wisenut.config.RunTimeArgs;

public class SearchWorker {
	public SearchWorker(){
	}
	
	public String getRecommends(String[] inputData, String query){
		boolean status = true;
		
		/*********************************************************************************
		 * 검색 수행
		 *********************************************************************************/
		String[] collections = new String[] { "reference" };

		String orQuery = query.replaceAll("[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]", " ");
		orQuery = orQuery.replaceAll("[\\s]+", "|");
		
		int ret = 0;
		Search wnsearch = new Search();
		
		ret = wnsearch.w3SetCodePage(Constants.CHARSET);
        if(RunTimeArgs.isDebug()) {
        	ret = wnsearch.w3SetQueryLog(Constants.SET_QUERYLOG_ON);
        }else{
        	ret = wnsearch.w3SetQueryLog(Constants.SET_QUERYLOG_OFF);
        }
        ret = wnsearch.w3SetCommonQuery(orQuery);
        
        // 우선은 세개 컬렉션 모두 동일한 document Field ==> code 하나만을 가진다고 가정.
        String documentFields = Config.getClass_field();
        
        for(String col : collections){
        	ret = wnsearch.w3AddCollection(col);
			
        	ret = wnsearch.w3SetQueryAnalyzer(col, Constants.USE_KMA_ON, Constants.IS_CASE_OFF);
			
        	ret = wnsearch.w3SetHighlight(col, Constants.HIGHLIGHT_ON, 5);
	        
        	ret = wnsearch.w3SetPageInfo(col, 0, Config.getPage_count());
	        
        	ret = wnsearch.w3SetDateRange(col, "1970/01/01", "2030/12/31");
	        
        	ret = wnsearch.w3AddSortField(col, "RANK", Constants.DESCENDING);

	        String[] searchFieldsArr = Config.getIndex_field().split(",");
	        for( String sfield : searchFieldsArr){
	        	ret = wnsearch.w3AddSearchField(col, sfield);
	        }
	        
	        String[] documentFieldArr = documentFields.split(",");
	        for( String dfield : documentFieldArr ){
	        	ret = wnsearch.w3AddDocumentField(col, dfield);
	        }
		}
        
        ret = wnsearch.w3ConnectServer(Config.getSearch_ip(), Config.getSearch_port(), Config.getSearch_timeout());
        
        ret = wnsearch.w3RecvResult(Constants.CONNECTION_CLOSE);
        
        if(ret != 0){
        	Log2.error("[search] error code : " + ret);
        	Log2.error("[search] error code : " + wnsearch.w3GetErrorInfo());
        	Log2.error("[search] error code : " + wnsearch.w3GetErrorInfo(ret));
        	wnsearch.w3CloseServer();
        	status = false;
        }
        
        /*********************************************************************************
		 * 검색 결과 받아옴.
		 *********************************************************************************/        
        // PK|가격|국가|상품명|추천코드 1개
        /*
         *  data[0] : 제출일자
         *  data[1] : 제출년월
         *  data[2] : 거래구분
         *  data[3] : 상대국가
         *  data[4] : 금액
         *  data[5] : HS
         *  data[6] : 품명
         *  data[7] : 용도구분
         */
        StringBuffer resultBuffer = new StringBuffer();
        
        String docid = inputData[Constants.KEY_COL_NUM-1];
        String goods = query; // 쪼개져서 들어온 한 레코드의 상품 일부
        
        StringBuffer sb = new StringBuffer();
        for(String col : collections){
        	int count = wnsearch.w3GetResultCount(col);
        	int i;
        	for(i=0; i<count; i++){
        		if(i==Config.getRecom_count()){
        			break;
        		}
        		String recommended = wnsearch.w3GetField(col, "CODE", i);
    			// 추천된 code 개수가 pageCount만큼이 되면 break
    			sb.append(recommended);
    			sb.append(",");
    		}
        }
        
        // 검색에 성공하면 (0건인 것도 포함)
        if(status){
        	resultBuffer.append("S").append("|");
        	// config에 설정된 컬럼 숫자 순서대로 cache 파일에 입력.
        	for(int idx=0; idx<inputData.length; idx++){
        		if(idx == Constants.KEY_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.PRICE_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.COUNTRY_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.COMPANY_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.GOODS_COL_NUM-1){
        			resultBuffer.append(goods).append("|");
        		}else if(idx == Constants.DEAL_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.USE_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}else if(idx == Constants.ALLOW_COL_NUM-1){
        			resultBuffer.append(inputData[idx]).append("|");
        		}
        	}
        	// 마지막으로 추천 받은  HS CODE를 붙임. 
        	resultBuffer.append(sb.toString().replaceAll(",$", ""));
        }
        // 검색기 오류 등의 이유로 검색이 안된 경우
        else{
        	resultBuffer.append("F").append("|");
        	resultBuffer.append(docid);
        }
        
        wnsearch.w3CloseServer();
        
		return resultBuffer.toString();
	}
	
	public HashMap<String,Integer> getExcludedCodeList(String excludedPath){
		HashMap<String,Integer> excludedList = new HashMap<String, Integer>();
		
		BufferedReader listReader;
		try{
			listReader = new BufferedReader(new FileReader(new File(excludedPath)));
			
			String line = "";
			while( (line = listReader.readLine())!= null){
				excludedList.put(line, 0);
			}
			
			listReader.close();
			
		}catch(IOException ioe){
			Log2.error("[SearchWorker][IOException] " + StringUtil.printStackTrace(ioe) );
		}		
		
		return excludedList;
	}
}
