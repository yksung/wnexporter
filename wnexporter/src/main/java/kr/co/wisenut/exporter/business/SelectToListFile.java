package kr.co.wisenut.exporter.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import kr.co.wisenut.exporter.Run;

public class SelectToListFile extends RunSQL{
	private int totalResultCount = 0;
	
	public SelectToListFile(DBJob dbjob) {
		super(dbjob);
	}
	
	public boolean work() throws DBFactoryException{
		String query = Config.getSelect_query();
		//  성유경@20161128 : cache디렉토리만 받고 하부 디렉토리는 export id를 이용해 만드는것으로 변경
		String cache_path = Config.getCachepath()+FileUtil.fileseperator+RunTimeArgs.getExportid();
		
		FileUtil.makeDir(cache_path);
		
		if(!cache_path.endsWith(FileUtil.fileseperator)){
			cache_path = cache_path + FileUtil.fileseperator;
		}
		
		String fullPathOfCacheFile = cache_path + StringUtil.getTimeBasedUniqueID() + ".csv";
		
		Log2.out("[info] [Run] [SelectToListFile] DB getConnection > " + Config.getDsn());
		if(!getConnection()){
			throw new DBFactoryException("Connection Fail");
		}
		
		BufferedWriter writer = null;
		
		try{
			Log2.out("[info] [Run] [SelectToListFile] create cache file > " + fullPathOfCacheFile + "_writing");
			writer = new BufferedWriter(new FileWriter(new File(fullPathOfCacheFile + "_writing")));
			
			//한꺼번에 객체에 내려받지 않고 한 로우씩 받아서 처리하자
			Log2.out("[info] [Run] [SelectToListFile] start getting data from table");
			m_dbjob.setResultSet(query, RunTimeArgs.getExportid(), 0);
			
			while (!m_dbjob.isError() && m_dbjob.next()) {
				HashMap<String, String> row =  m_dbjob.getStringAsMap();
				
				if(row == null || row.size() < 1){
					continue;
				}
				
				StringBuffer sb = new StringBuffer();
				for(String sel : Constants.SELECT_COLUMN){
					sb.append(row.get(sel));
					sb.append("|");
				}
				
				/*if(DOCID.equals("")){
					Log2.error("[error] [Run] [SelectToListFile] there is no column (" + key_colnum + ") in query" );
					continue;
				}
				
				if(WORD.equals("")){
					Log2.error("[error] [Run] [SelectToListFile] there is no column (" + goods_colnum + ") in query" );
					continue;
				}*/
				
				// 마지막 구분자는 제거
				writer.write(sb.toString().replaceAll("\\|$", ""));
				writer.newLine();
				writer.flush();
			}//while (!m_dbjob.isError() && m_dbjob.next()) {
			writer.close();
			
			boolean status = true;
			
			if ((int)new File(fullPathOfCacheFile + "_writing").length() > 0){
				FileUtil.rename(new File(fullPathOfCacheFile + "_writing"), new File(fullPathOfCacheFile));
				Log2.out("[info] [Run] [SelectToListFile] created cache success > " + fullPathOfCacheFile);
			}else{
				FileUtil.delete(new File(fullPathOfCacheFile + "_writing"));
				Log2.out("[info] [Run] [SelectToListFile] there is no selected data");
				status = false;
			}
			
			if(m_dbjob.isError()){
				Log2.error("[error] [Run] [SelectToListFile] DBJob Fail");
				status = false;
			}
			
			return status;
		}catch (IOException o) {
			Log2.error("[error] [Run] [SelectToListFile][IOException] " + Config.getCachepath() + " > " + StringUtil.printStackTrace(o));
			return false;
		}catch(DBFactoryException e){
			Log2.error("[error] [Run] [SelectToListFile][DBFactoryException]" + StringUtil.printStackTrace(e));
			return false;
		}catch (Exception e) {
			Log2.error("[error] [Run] [SelectToListFile][Exception] : " + StringUtil.printStackTrace(e));
			return false;
		}finally{
			try {
				if(writer != null)writer.close();
				releaseRS();
				releaseDB();
			} catch (Exception e2) {
				Log2.error("[error] [Run] [SelectToListFile][Exception] : " + StringUtil.printStackTrace(e2));
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
