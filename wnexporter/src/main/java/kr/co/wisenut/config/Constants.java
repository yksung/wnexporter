package kr.co.wisenut.config;

public class Constants {
	public static final String CHARSET = "UTF-8";
	public static final int HIGHLIGHT_ON = 1;
	public static final int HIGHLIGHT_OFF = 0;
	
	public static final int USE_KMA_ON = 1;
	public static final int USE_KMA_OFF = 0;
	
	public static final int IS_CASE_ON = 1;
	public static final int IS_CASE_OFF = 0;
	
	public static final int SET_QUERYLOG_ON = 1;
	public static final int SET_QUERYLOG_OFF = 0;
	
	public static final int ASCENDING = 0;
	public static final int DESCENDING = 1;

	public static final int CONNECTION_KEEP = 0; //recevive mode
	public static final int CONNECTION_REUSE = 2;
	public static final int CONNECTION_CLOSE = 3;
	
	public static final int KEY_COL_NUM = Config.getSelect_key_num();
	public static final int GOODS_COL_NUM = Config.getSelect_goods_num();
	public static final int PRICE_COL_NUM = Config.getSelect_price_num();
	public static final int COUNTRY_COL_NUM = Config.getSelect_country_num();
	public static final int COMPANY_COL_NUM = Config.getSelect_company_num();
	public static final int DEAL_COL_NUM = Config.getSelect_deal_num();
	public static final int USE_COL_NUM = Config.getSelect_use_num();
	public static final int ALLOW_COL_NUM = Config.getSelect_allow_num();
	
	public static final String SELECT_COLUMN[];
	static {
		SELECT_COLUMN = new String[8];
		SELECT_COLUMN[KEY_COL_NUM-1] = Config.getSelect_key_name();
		SELECT_COLUMN[GOODS_COL_NUM-1] = Config.getSelect_goods_name();
		SELECT_COLUMN[PRICE_COL_NUM-1] = Config.getSelect_price_name();
		SELECT_COLUMN[COUNTRY_COL_NUM-1] = Config.getSelect_country_name();
		SELECT_COLUMN[COMPANY_COL_NUM-1] = Config.getSelect_company_name();
		SELECT_COLUMN[DEAL_COL_NUM-1] = Config.getSelect_deal_name();
		SELECT_COLUMN[USE_COL_NUM-1] = Config.getSelect_use_name();
		SELECT_COLUMN[ALLOW_COL_NUM-1] = Config.getSelect_allow_name();
	};
	
	public static final int FETCH_SIZE = Config.getUpdate_fetch_size();
}
