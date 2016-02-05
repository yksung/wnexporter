package kr.co.wisenut.exporter.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.Constants;
import kr.co.wisenut.db.DBJob;
import kr.co.wisenut.db.RunSQL;

public class MakeInsertQuery extends RunSQL{
	private int totalResultCount = 0;
	private SearchWorker search;
	
	private HashMap<String ,Integer> excludeWord;
	
	public MakeInsertQuery(DBJob dbjob) {
		super(dbjob);
		
		search = new SearchWorker();
		
		excludeWord = new HashMap<String, Integer>();
		excludeWord.put("SET", 0);
		excludeWord.put("FOR", 0);
		excludeWord.put("ALL", 0);
		excludeWord.put("CM", 0);
		excludeWord.put("KG", 0);
		excludeWord.put("ML", 0);
		excludeWord.put("OZ", 0);
		excludeWord.put("IN", 0);
		excludeWord.put("BY", 0);
		excludeWord.put("THE", 0);
		excludeWord.put("EA", 0);
		excludeWord.put("LB", 0);
		excludeWord.put("INCHES", 0);
		excludeWord.put("MAH", 0);
		excludeWord.put("AND", 0);
		excludeWord.put("NEW", 0);
		excludeWord.put("COUNT", 0);
		excludeWord.put("FOOT", 0);
		excludeWord.put("INCH", 0);
		excludeWord.put("ON", 0);
		excludeWord.put("KHZ", 0);
		excludeWord.put("HZ", 0);
		excludeWord.put("PACK", 0);
		excludeWord.put("PAC", 0);
		excludeWord.put("PIECE", 0);
		excludeWord.put("PCS", 0);
		excludeWord.put("GB", 0);
		excludeWord.put("SIZE", 0);		
		excludeWord.put("NJ", 0);		
		excludeWord.put("AR", 0);		
		excludeWord.put("YD", 0);		
		excludeWord.put("SPF", 0);		
		excludeWord.put("TC", 0);		
		excludeWord.put("PER", 0);		
		excludeWord.put("GRAM", 0);		
		excludeWord.put("BU", 0);		
		excludeWord.put("PW", 0);		
		excludeWord.put("CL", 0);		
		excludeWord.put("OUNCE", 0);		
		excludeWord.put("BN", 0);		
		excludeWord.put("DC", 0);		
		excludeWord.put("FL", 0);
		excludeWord.put("WON", 0);
		excludeWord.put("GM", 0);
		//excludeWord.put("&amp;", 0);		
	}

	public boolean work() {
		
		Log2.out("Start 'MakeInsertQuery'! ");
		
		boolean status = true;		
		
		String recomOption = Config.getRecom_option();
		String cache_path = Config.getCachepath();
		String done_path = cache_path + FileUtil.fileseperator + "done";
		
		FileUtil.makeDir(cache_path);
		
		if(!cache_path.endsWith(FileUtil.fileseperator)){
			cache_path = cache_path + FileUtil.fileseperator;
		}
		
		HashMap<String, String> regx = Config.getGoods_split_regxs();
		
		
		if(!recomOption.equals("rank")){
			recomOption = "statistic";
		}
		
		File[] cacheFilesArr = FileUtil.getFileList(cache_path, ".csv");
		if( cacheFilesArr == null || cacheFilesArr.length <= 0){
			Log2.error("[info][MakeInsertQuery] No cache files.");
			return false;
		}
		
		BufferedWriter successQueryWriter = null;
		BufferedWriter failedQueryWriter = null;
		BufferedWriter threeCharWriter = null;
		BufferedWriter fourCharWriter = null;
		
		try{
			for(File cacheFile : cacheFilesArr){
				String fileComplete = "";
				String fileWriting = cacheFile.getAbsolutePath() + "_ing";
				String fileFailed = cacheFile.getAbsolutePath() + "_failed";
				String fileThreeChar = cacheFile.getAbsolutePath() + "_three";
				String fileFourChar = cacheFile.getAbsolutePath() + "_four";
				
				successQueryWriter = new BufferedWriter(new FileWriter(new File(fileWriting)));		
				failedQueryWriter = new BufferedWriter(new FileWriter(new File(fileFailed)));
				threeCharWriter = new BufferedWriter(new FileWriter(new File(fileThreeChar)));
				fourCharWriter = new BufferedWriter(new FileWriter(new File(fileFourChar)));
				
				Log2.out("[info][MakeInsertQuery] Start writing " + fileWriting);
				
				BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
				
				int lineNumber = 0;
				String line;
				while((line = reader.readLine()) != null){
					if(line.equals("")){
						continue;
					}
					
					String[] data = line.split("[|]");
					// 필수로 필요한 정보(제출일자, 가격, 국가, 상품명) 4가지만 있으면 파싱하는 걸로.
					if(data.length < 4){
						continue;
					}
	
					String inputSearchWords = data[Constants.GOODS_COL_NUM-1];
					String comp = "";// 아직은 회사코드를 받지 못한 상태이므로 그냥 else로 처리. 
					String sep = "";
					if(regx.get(comp) == null || regx.get(comp).trim().equals("")){
						sep = regx.get("else");
					}
					
					String[] searchWordsArr;
					
					
					
					//송은우 임시 추가 : 통으로 할건지 아니면 나눌건지
					// 유경이 자리에서는 split으로 돌리고, 회의용 탁자에서는 통으로 돌림
					// 성유경@20151111 : config에서 설정하고 받아올 수 있도록 변경.
					boolean isTong = Config.getNo_separator().equalsIgnoreCase("y") ? true:false;
					
					if(isTong){
						searchWordsArr = new String[]{inputSearchWords};
					}else{
						searchWordsArr = inputSearchWords.split(sep);
					}
					
					int count=0;
					for(String searchWord : searchWordsArr){
						//송은우 임시 추가 : 지난 회의때 나왔던 제외 처리
						//1. 빈칸일때 검색 안함
						//2. 3글자 이하일 경우 검색 안함
						//3. 숫자만 있을 경우 검색 안함
						if(searchWord == null){
							continue;
						}
						
						// 성유경@20151111 : 숫자, 단위 등을 모두 제거한 결과물 중에서 세글자, 네글자, 공백인지를 판단하는 것으로 변경.
						String tmpWord = getTunedQuery(searchWord.trim()).trim();
						
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
							failedQueryWriter.write(result);
							failedQueryWriter.newLine();
							failedQueryWriter.flush();
						}
					}
					
					// Connection fail 이 너무 자주 일어나 매번 0.01초로 속도 제한.(2015-12-02)
					if( (++lineNumber)%1000 == 0 ){
						Thread.sleep(1000);
					}
					
					/*
					//코드리스트 통계를 낸다.
					String suggest = "";
					if(recomOption.equals("rank")){
						//순서대로 추천 개수까지
					}else{
						//통계를 내서 많은 수로 
					}
					*/
				}
				
				reader.close();
				successQueryWriter.close();
				failedQueryWriter.close();
				threeCharWriter.close();
				fourCharWriter.close();
				
				if ((int)new File(fileWriting).length() > 0){
					fileComplete = done_path + FileUtil.fileseperator + cacheFile.getName() + "_success";
					FileUtil.rename(new File(fileWriting), new File(fileComplete));
					Log2.out("[info][MakeInsertQuery] Successfully complete to write " + fileComplete);
				}else{
					FileUtil.delete(new File(fileWriting));
					Log2.out("[info][MakeInsertQuery] There is no searched data.");
					status = false;
				}
				
				if(Config.isDeleteCache()){				
					FileUtil.delete(cacheFile);
					FileUtil.delete(new File(fileFailed));
					FileUtil.delete(new File(fileFourChar));
					FileUtil.delete(new File(fileThreeChar));
				}
			}
			return status;
				
		}catch (IOException o) {
			Log2.error("[error][MakeInsertQuery][IOException] " + StringUtil.printStackTrace(o));
			return false;
		}catch (Exception e) {
			Log2.error("[error][MakeInsertQuery][Exception] " + StringUtil.printStackTrace(e));
			return false;
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
		
		if(excludeWord.containsKey(checkString.toUpperCase())){
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
