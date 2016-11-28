package kr.co.wisenut.exporter.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.FileUtil;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.Constants;
import kr.co.wisenut.config.RunTimeArgs;
import kr.co.wisenut.db.DBJob;
import kr.co.wisenut.db.RunSQL;

public class InsertFileToDB extends RunSQL{
	private int totalResultCount = 0;
	
	public InsertFileToDB(DBJob dbjob) {
		super(dbjob);
	}
	
	public boolean work() throws DBFactoryException{

		String resultCacheDirectoryPath = Config.getCachepath()+FileUtil.fileseperator+"done";
		FileUtil.makeDir(resultCacheDirectoryPath);
		
		File[] resultCachesArr = FileUtil.getFileList(resultCacheDirectoryPath, "_success");
		if( resultCachesArr == null || resultCachesArr.length <= 0){
			Log2.error("[info][InsertFileToDB] No cache files.");
			return false;
		}
		
		Log2.out("[info] [Run] [InsertFileToDB] DB getConnection > " + Config.getDsn());
		if(!getConnection()){
			throw new DBFactoryException("Connection Fail");
		}
		
		
		ArrayList<HashMap<Integer,String>> parameters = new ArrayList<HashMap<Integer,String>>(); 
		
		try{
			boolean status =  true;
			for ( File resultCache : resultCachesArr ){
				BufferedReader reader = new BufferedReader(new FileReader(resultCache));
				
				String line;
				String prevDocid = "";
				int productNumberInThisDocid = 1;
				
				int updateCount = 0;
				while((line = reader.readLine()) != null){
					String[] data = line.split("[|]", -1); // 공백도 값으로 간주하고 필드에 포함
					
					// 성유경@151015 : 마지막 컬럼의 hscode는 없는 경우도 있기 때문에 길이가 6보다 작을 수 있다.
					if(data.length < 5){
						continue;
					}
					
					// data[0] : success/fail flag
					String docid = data[Constants.KEY_COL_NUM];
					String price = data[Constants.PRICE_COL_NUM];
					String country = data[Constants.COUNTRY_COL_NUM];
					String parsedGoodsName = data[Constants.GOODS_COL_NUM];
					String companyCode = data[Constants.COMPANY_COL_NUM];
					String dealSep = data[Constants.DEAL_COL_NUM];
					String useSep = data[Constants.USE_COL_NUM];
					String allowCode = data[Constants.ALLOW_COL_NUM];
					// HS CODE가 추천되었으면 적고, 없으면 빈값을 넣음.
					String hscodes = (data.length==7)? data[5]:" ";
					
					// 현재 docid와 이전 docid가 같으면 같은 주문임.
					if( prevDocid.equals(docid) ){
						productNumberInThisDocid++;
					}else{ // 다른 주문이면 초기화
						productNumberInThisDocid = 1;
					}
	
					String[] hscodeArr = hscodes.split(",");
					for(int i=0; i<hscodeArr.length; i++){
						HashMap<Integer, String> preparedStatementParameters = new HashMap<Integer, String>();
						
						// 같은 값을 여러 파라미터에 세팅할 수 있기 때문에 다음과 같이 작성함.
						// 추후 개발 시 변수명을 key -> paremeterNumber 로 변경 (yksung@2016-09-26)
						for(String key : StringUtil.split(Config.getUpdate_output_key_number(), ",")){						
							// pk = docid(주문에 대한 key) + productNumberInThisDocid(한 주문에 포함된 상품의 seq) + i (hscode가 여러개일 경우 hscode seq)
							preparedStatementParameters.put(Integer.parseInt(key), docid+"_"+productNumberInThisDocid+"_"+String.valueOf(i+1));
						}
						for(String key : StringUtil.split(Config.getUpdate_order_key_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), docid);
						}
						for(String key : StringUtil.split(Config.getUpdate_price_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), price);
						}
						for(String key : StringUtil.split(Config.getUpdate_country_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), country);
						}
						for(String key : StringUtil.split(Config.getUpdate_goodsname_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), parsedGoodsName);
						}
						for(String key : StringUtil.split(Config.getUpdate_company_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), companyCode);
						}
						for(String key : StringUtil.split(Config.getUpdate_deal_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), dealSep);
						}
						for(String key : StringUtil.split(Config.getUpdate_use_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), useSep);
						}
						for(String key : StringUtil.split(Config.getUpdate_allow_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), allowCode);
						}
						for(String key : StringUtil.split(Config.getUpdate_hscode_seq_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), String.valueOf(i+1));
						}
						for(String key : StringUtil.split(Config.getUpdate_hscode_val_number(), ",")){
							preparedStatementParameters.put(Integer.parseInt(key), hscodeArr[i].trim());
						}
	
						parameters.add(preparedStatementParameters);
						updateCount++;
					}
					
					// 현재 docid를 prevDocid에 세팅.
					prevDocid = docid;
					
					// preparedStatement를 여러 개 세팅해 한번에 update하는 batch job.
					if(updateCount % Constants.FETCH_SIZE == 0){
						if( m_dbjob.executeBatch(Config.getUpdate_query(), parameters) == 0){
							Log2.error("[InsertFileToDb] executeBatch failed.");
						}else{
							releaseRS();
							if(RunTimeArgs.isDebug()) Log2.debug("[InsertFileToDb] " + updateCount +" rows are updated.");
						}
						parameters.clear(); // List를 비움.
					}
				}
				
				// 남은 row를 update.
				if( m_dbjob.executeBatch(Config.getUpdate_query(), parameters) == 0){
					Log2.error("[InsertFileToDb] executeBatch failed.");
				}else{					
					if(RunTimeArgs.isDebug()) Log2.debug("[InsertFileToDb] " + updateCount +" rows are updated.");
				}
	
				reader.close();
				
				if(Config.isDeleteCache()){
					FileUtil.delete(resultCache);
				}
				
				status = true;
				
				if(m_dbjob.isError()){
					Log2.error("[error] [Run] [InsertFileToDB] DBJob Fail");
					status = false;
				}
			}
			
			return status;
		}catch (IOException o) {
			Log2.error("[error] [Run] [InsertFileToDB][IOException] " + Config.getCachepath() + " > " + StringUtil.printStackTrace(o));
			return false;
		}catch (Exception e) {
			Log2.error("[error] [Run] [InsertFileToDB][Exception] : " + StringUtil.printStackTrace(e));
			return false;
		}finally{
			try {
				releaseRS();
				releaseDB();
			} catch (Exception e2) {
				Log2.error("[error] [Run] [InsertFileToDB][Exception] : " + StringUtil.printStackTrace(e2));
			}
		}
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
