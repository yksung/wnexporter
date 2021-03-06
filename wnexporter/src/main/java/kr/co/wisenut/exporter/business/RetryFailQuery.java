package kr.co.wisenut.exporter.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.Constants;
import kr.co.wisenut.db.DBJob;
import kr.co.wisenut.db.RunSQL;

/*
 *  
 * 
 * 
 */
public class RetryFailQuery extends RunSQL{
	private int totalResultCount = 0;
	private SearchWorker search;
	private HashMap<String,String> regxMap;
	private HashMap<String,Integer> excludedWord;
	
	@SuppressWarnings("unchecked")
	public RetryFailQuery(DBJob dbjob) throws DBFactoryException {
		super(dbjob);
		
		Log2.out("[info] [Run] [MakeInsertQuery] DB getConnection > " + Config.getDsn());
		if(!getConnection()){
			throw new DBFactoryException("Connection Fail");
		}
		
		search = new SearchWorker();
		
		// Config>Search>goods-split-regx의 SQL로 업체별 상품 구분자를 받아와 map에 저장.
		try{
			for(String[] regx : (ArrayList<String[]>)m_dbjob.getQueryRsData(Config.getGoods_split_regxs(), null)){
				regxMap.put(regx[0], regx[1]); // regx[0] : 특송업체부호, regx[1] : 구분자 정규식
			}
			for(String[] word : (ArrayList<String[]>)m_dbjob.getQueryRsData(Config.getExcluded_word(), null)){
				excludedWord.put(word[0], 0);
			}
		}catch(Exception e){
			Log2.error(StringUtil.printStackTrace(e));
		}finally{
			releaseRS();
			releaseDB();
		}
	}
	
	// 여기서는 기존에 success한 결과물들을 다시 읽으면서
	// F로 시작하는 줄에 대해서만 다시 분류한다.
	public boolean work() {
		
		Log2.out("Start 'RetryFailQuery'! ");
		
		boolean status = true;		
		
		String recomOption = Config.getRecom_option();
		String cache_path = Config.getCachepath();
		String backupDir = cache_path + FileUtil.fileseperator + "backup";
		
		FileUtil.makeDir(backupDir);
		FileUtil.makeDir(cache_path);
		
		if(!cache_path.endsWith(FileUtil.fileseperator)){
			cache_path = cache_path + FileUtil.fileseperator;
		}
		
		if(!recomOption.equals("rank")){
			recomOption = "statistic";
		}
		
		// 이미 분류한 결과 파일을 읽어들인다.
		File[] completedFileArr  = FileUtil.getFileList(cache_path, ".csv_failed");
		
		BufferedWriter successQueryWriter = null;
		BufferedWriter failedQueryWriter = null;
		BufferedWriter threeCharWriter = null;
		BufferedWriter fourCharWriter = null;
		
		try{
			if(completedFileArr == null || completedFileArr.length < 1){
				Log2.out("[info][RetryFailQuery] There is no file completed.");
				return false;
			}
			
			//송은우 임시 추가 (3글자 이하인 애들 저장 하기위
			
			for(File select : completedFileArr){
				
				String fileWriting = select.getAbsolutePath() + "_ing";
				String fileFailed = select.getAbsolutePath() + "_failed";
				String fileThreeChar = select.getAbsolutePath() + "_three";
				String fileFourChar = select.getAbsolutePath() + "_four";
				
				successQueryWriter = new BufferedWriter(new FileWriter(new File(fileWriting)));		
				failedQueryWriter = new BufferedWriter(new FileWriter(new File(fileFailed)));
				threeCharWriter = new BufferedWriter(new FileWriter(new File(fileThreeChar)));
				fourCharWriter = new BufferedWriter(new FileWriter(new File(fileFourChar)));
				
				Log2.out("[info][RetryFailQuery] Start writing " + fileWriting);
				
				BufferedReader completedFileReader = new BufferedReader(new FileReader(select));
				
				// 실패한 파일 리스트를 저장하는 Array
				String line;
				int lineNumber = 0;
				while((line = completedFileReader.readLine()) != null){
					if(line.equals("")){
						continue;
					}
					
					String[] data = line.split("[|]", -1);
					// 2016-10-17 성유경 : 특송업체부호 추가
					// 2016-11-25 성유경 : 거래유형코드,용도코드 추가.
					// 필수로 필요한 정보(제출일자, 가격, 국가, 상품명, 특송업체부호, 거래유형코드, 용도코드) 7가지만 있으면 파싱하는 걸로.
					if(data.length < 7){
						continue;
					}
	
					String inputSearchWords = data[Constants.GOODS_COL_NUM-1];
					String comp = data[Constants.COMPANY_COL_NUM-1];// 아직은 회사코드를 받지 못한 상태이므로 그냥 else로 처리. 
					String sep = "";
					if(regxMap.get(comp) == null ){
						sep = regxMap.get("else");
					}else{
						sep = regxMap.get(comp);
					}
					
					String[] searchWordsArr;
					
					
					
					//송은우 임시 추가 : 통으로 할건지 아니면 나눌건지
					// 유경이 자리에서는 split으로 돌리고, 회의용 탁자에서는 통으로 돌림
					// 성유경@20151111 : config에서 설정하고 받아올 수 있도록 변경.
					boolean isTong = Config.getNo_separator().equalsIgnoreCase("y") ? true:false;
					
					if( isTong || "".equals(sep.trim()) ){ // 구분자를 사용안하거나, 구분자가 빈 문자열인 경우는 단품으로 간주.
						searchWordsArr = new String[]{inputSearchWords};
					}else{
						searchWordsArr = inputSearchWords.split(sep);
					}
					
					int count=0;
					for(int i=0; i<searchWordsArr.length; i++){
						//송은우 임시 추가 : 지난 회의때 나왔던 제외 처리
						//1. 빈칸일때 검색 안함
						//2. 3글자 이하일 경우 검색 안함
						//3. 숫자만 있을 경우 검색 안함
						if(searchWordsArr[i] == null){
							continue;
						}
						
						// 성유경@20151111 : 숫자, 단위 등을 모두 제거한 결과물 중에서 세글자, 네글자, 공백인지를 판단하는 것으로 변경.
						String tmpWord = getTunedQuery(searchWordsArr[i].trim()).trim();
						
						if(tmpWord.equals("")){//빈값일 경우 검색 pass
							continue;
						}
						if(tmpWord.length() <= 3 && searchWordsArr.length!=1){//3글자 이하일 경우 pass
							threeCharWriter.write(data[Constants.KEY_COL_NUM-1]+"|"+tmpWord);
							threeCharWriter.newLine();
							threeCharWriter.flush();
							continue;
						}
						if(tmpWord.length() == 4){//4글자인 경우는 별도 목록으로 저장.
							fourCharWriter.write(data[Constants.KEY_COL_NUM-1]+"|"+tmpWord);
							fourCharWriter.newLine();
							fourCharWriter.flush();
						}
						if(tmpWord.matches("[0-9]+")){//숫자만 있을 경우 pass
							continue;
						}
						
						String result = search.getRecommends(data, tmpWord);//검색해서 코드 리스트를 가져온다.
						/*********************************************************************************
						 * 검색 결과 파일에 기록.
						 *********************************************************************************/
						if(result.startsWith("S|")){
							successQueryWriter.write(result);
							successQueryWriter.newLine();
							if((++count%2000) == 0){							
								successQueryWriter.flush();
								count = 0;
							}
						}else{ // 검색 실패. 결과가 없는게 아니고 아예 검색 수행을 못함. 검색기 오류 등으로.
							failedQueryWriter.write(line);
							failedQueryWriter.newLine();
							failedQueryWriter.flush();
							
							break; // 같은 목록통관상 남은 미분류 항목이 있더라도 다음 목록통관으로 건너뜀.
						}
					}
					
					// Connection fail 이 너무 자주 일어나 매번 0.01초로 속도 제한.(2015-12-02)
					if( (++lineNumber)%1000 == 0 ){
						Thread.sleep(1000);
					}
				}
								
				completedFileReader.close();
				successQueryWriter.close();
				failedQueryWriter.close();
				threeCharWriter.close();
				fourCharWriter.close();
				
				if ((int)new File(fileWriting).length() > 0){
					String fileComplete = select.getAbsolutePath() + "_success";
					FileUtil.rename(new File(fileWriting), new File(fileComplete));
					Log2.out("[info][RetryFailQuery] Successfully complete to write " + fileComplete);
				}else{
					FileUtil.delete(new File(fileWriting));
					Log2.out("[info][RetryFailQuery] There is no searched data.");
					status = false;
				}
				
				FileUtil.rename(select, new File(backupDir + FileUtil.fileseperator + select.getName()));
			}//for(File select : selects){
			
			return status;
			
		}catch (IOException o) {
			Log2.error("[error][RetryFailQuery][IOException] " + Config.getCachepath() + " > " + StringUtil.printStackTrace(o));
			return false;
		}catch (Exception e) {
			Log2.error("[error][RetryFailQuery][Exception] : " + StringUtil.printStackTrace(e));
			return false;
		}finally{
			try {
				successQueryWriter.close();
				failedQueryWriter.close();
				threeCharWriter.close();
			} catch (IOException e) {
				Log2.error("[error][RetryFailQuery][IOException] " + StringUtil.printStackTrace(e));
			}
		}
	}
	
	public String getTunedQuery(String query){
		String tunedQuery = "";
		StringTokenizer st = new StringTokenizer(query);
		while(st.hasMoreTokens()){
			String tempString = st.nextToken();
			//예외키워드인지 확인하여 검색어에 추가
			if(!checkExcludedWord(tempString) && tempString.length() > 1 && tempString != null && !tempString.equals(" ")){
				tunedQuery += tempString.trim() + " ";
			}	
		}
		
		tunedQuery = tunedQuery.trim();
		
		return tunedQuery;
	}
	
	public boolean checkExcludedWord(String checkString){
		checkString = checkString.replaceAll("\\p{Space}","");
		if(checkString == null || checkString.equals("")){
			return false;
		}
		
		if(excludedWord.containsKey(checkString.toUpperCase())){
			return true;
		}
		
		if("&amp;".equalsIgnoreCase(checkString)){
			return true;
		}
		
		if(checkString.matches("[0-9]+")){
			return true;
		}
		
		return false;
	}
	
	public String getNormalPath(String path){
		if(!path.endsWith("\\") && !path.endsWith("/")){
			path = path + FileUtil.fileseperator;
		}
		return path;
	}
	
	public void printLog(){
		if(totalResultCount%100 == 0){
			if(totalResultCount == 0){ 
				System.out.println();
				System.out.print("Run Insert.. ");
				System.out.println();
			}
			System.out.print(".");
			if(totalResultCount%1000 == 0){
				System.out.print("[" + totalResultCount + "]");
			}
		}
	}
}
