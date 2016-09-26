package kr.co.wisenut.exporter;

import kr.co.wisenut.config.Config;
import kr.co.wisenut.config.RunTimeArgs;
import kr.co.wisenut.db.DBJob;
import kr.co.wisenut.db.DBConnFactory;
import kr.co.wisenut.exporter.business.InsertFileToDB;
import kr.co.wisenut.exporter.business.MakeInsertQuery;
import kr.co.wisenut.exporter.business.RetryFailQuery;
import kr.co.wisenut.exporter.business.SelectToListFile;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.io.IOUtil;
import kr.co.wisenut.common.Exception.DBFactoryException;

public class Run {
	 private DBJob m_dbjob;
	 private boolean m_onlyFailQuery;
	 
	 public Run(boolean onlyFailQuery) throws DBFactoryException {
		 this.m_onlyFailQuery = onlyFailQuery;
		 DBConnFactory m_dbFactory = new DBConnFactory(Config.getDataSource());
		 m_dbjob = new DBJob(m_dbFactory.getDbmsType(Config.getDsn()) ,Config.getDsn());
	 }
	 
	 
	 public Run() throws DBFactoryException {
		 this(false);
	 }
	 
	 
	 public boolean run(){
		 try{
			 if(!m_onlyFailQuery && !"".equals(RunTimeArgs.getMode()) ){
				 // all 모드일 때만 cache 생성
				 if( "all".equalsIgnoreCase(RunTimeArgs.getMode()) || "cache".equalsIgnoreCase(RunTimeArgs.getMode()) ){ 
					if( !new SelectToListFile(m_dbjob).work() ){
						Log2.error("[Run] 'SelectToListFile' failed.");
					}
				 }
				 
				 if( "all".equalsIgnoreCase(RunTimeArgs.getMode()) || "recommend".equalsIgnoreCase(RunTimeArgs.getMode()) ){
					 if(!new MakeInsertQuery(m_dbjob).work()){
						 Log2.error("[Run] 'MakeInsertQuery' failed.");
					 }
				 }

				 if( "all".equalsIgnoreCase(RunTimeArgs.getMode()) || "export".equalsIgnoreCase(RunTimeArgs.getMode()) ){
					 if(!new InsertFileToDB(m_dbjob).work()){
						 Log2.error("[Run] 'InsertFileToDB' failed.");
					 }
				 }
			 }else{
				 /*if(!new SelectToListFile(m_cfg, m_dbjob).work()){
					 //로그 부리고 리턴
				 }*/
			 
				 if(!new RetryFailQuery(m_dbjob).work()){
					 //로그 부리고 리턴
				 }
				 /* 송은우 임시 수정 
				 if(!new InsertFileToDB(m_cfg, m_dbjob).work()){
					 //로그 부리고 리턴
				 }
				 */
			 }
			 return true;
		 }catch(Exception e){
			 Log2.error(IOUtil.StackTraceToString(e));
		 }
		 
		 return false;
	 }
}
