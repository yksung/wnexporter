package kr.co.wisenut.exporter.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

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
	private HashMap<String ,Integer> excludeWord;
	private SearchWorker search;
	
	public RetryFailQuery(DBJob dbjob) {
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
		
		HashMap<String, String> regx = Config.getGoods_split_regxs();
		
		
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
				
				String originalFilePath = cache_path + FileUtil.fileseperator + select.getName().substring(0, select.getName().indexOf("_failed"));
				
				// 실패한 파일 리스트를 저장하는 Array
				ArrayList<Integer> failQueryList = new ArrayList<Integer>();
				String line;
				while((line = completedFileReader.readLine()) != null){
					// 줄의 시작이 F로 시작되지 않는 경우 건너뜀.
					if(!line.startsWith("F|")){
						continue;
					}
					
					String[] data = line.split("[|]");
					
					if(data.length < 2){
						continue;
					}

					int lineNumber = Integer.parseInt(data[1].split("_")[1]);
					// 실패한 쿼리는 여러 항목으로 이미 분화되어 여러개가 있을 수 있으므로 중복을 제거하면서 넣는다.
					if(! failQueryList.contains(lineNumber)){						
						failQueryList.add(lineNumber);
					}
				}
				
				int loopcnt=0;
				for(int failQueryLineNumber : failQueryList){
					// 해당 쿼리 라인을 바로 원본 csv에서 찾아 다시 처리.
					String line2 = Files.readAllLines(Paths.get(originalFilePath), Charset.defaultCharset()).get(failQueryLineNumber-1);
					String[] data = line2.split("[|]");
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
						if(tmpWord.length() <= 3){//3글자 이하일 경우 pass
							threeCharWriter.write(data[Constants.KEY_COL_NUM-1]+"_"+failQueryLineNumber+"|"+tmpWord);
							threeCharWriter.newLine();
							threeCharWriter.flush();
							continue;
						}
						if(tmpWord.length() == 4){//4글자인 경우는 별도 목록으로 저장.
							fourCharWriter.write(data[Constants.KEY_COL_NUM-1]+"_"+failQueryLineNumber+"|"+tmpWord);
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
							successQueryWriter.flush();
						}else{ // 검색 실패. 결과가 없는게 아니고 아예 검색 수행을 못함. 검색기 오류 등으로.
							failedQueryWriter.write(result);
							failedQueryWriter.newLine();
							failedQueryWriter.flush();
						}
					}
					
					if(++loopcnt%1000==0){
						Thread.sleep(5000);
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
			Log2.error("[error][RetryFailQuery][IOException] " + Config.getCachepath() + " > " + o.getMessage());
			return false;
		}catch (Exception e) {
			Log2.error("[error][RetryFailQuery][Exception] : " + e.getMessage());
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
