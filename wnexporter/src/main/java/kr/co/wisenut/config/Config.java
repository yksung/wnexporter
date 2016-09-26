package kr.co.wisenut.config;

import java.util.HashMap;

public class Config {
    private static String dsn;
	private static String cachepath;
	private static boolean deleteCache;

	private static int select_key_num;
    private static String select_key_name;
    private static int select_goods_num;
    private static String select_goods_name;
    private static int select_country_num;
    private static String select_country_name;
    private static int select_price_num;
    private static String select_price_name;
    private static int select_company_num;
    private static String select_company_name;
    private static String select_query;

    private static String search_ip;
    private static int search_port;
	private static int search_thread;
	private static int search_timeout;
    private static String index_field;
    private static String class_field;
    private static int page_count;
    private static int recom_count;
    private static String recom_option;
    private static String no_separator;
    private static HashMap<String, String> goods_split_regxs;

    private static int update_fetch_size;
    private static String update_output_key_number;
	private static String update_order_key_number;
	private static String update_price_number;
	private static String update_country_number;
    private static String update_goodsname_number;
    private static String update_hscode_seq_number;
    private static String update_hscode_val_number;
    private static String update_query;
    
    private static HashMap dataSource;
    
	public static String getDsn() {
		return dsn;
	}
	public static void setDsn(String dsn) {
		Config.dsn = dsn;
	}
	public static String getCachepath() {
		return cachepath;
	}
	public static void setCachepath(String cachepath) {
		Config.cachepath = cachepath;
	}
	
	public static boolean isDeleteCache() {
		return deleteCache;
	}
	public static void setDeleteCache(boolean deleteCache) {
		Config.deleteCache = deleteCache;
	}
	public static int getSelect_key_num() {
		return select_key_num;
	}
	public static void setSelect_key_num(int select_key_num) {
		Config.select_key_num = select_key_num;
	}
	public static String getSelect_key_name() {
		return select_key_name;
	}
	public static void setSelect_key_name(String select_key_name) {
		Config.select_key_name = select_key_name;
	}
	public static int getSelect_goods_num() {
		return select_goods_num;
	}
	public static void setSelect_goods_num(int select_goods_num) {
		Config.select_goods_num = select_goods_num;
	}
	public static String getSelect_goods_name() {
		return select_goods_name;
	}
	public static void setSelect_goods_name(String select_goods_name) {
		Config.select_goods_name = select_goods_name;
	}
	public static int getSelect_country_num() {
		return select_country_num;
	}
	public static void setSelect_country_num(int select_country_num) {
		Config.select_country_num = select_country_num;
	}
	public static String getSelect_country_name() {
		return select_country_name;
	}
	public static void setSelect_country_name(String select_country_name) {
		Config.select_country_name = select_country_name;
	}
	public static int getSelect_price_num() {
		return select_price_num;
	}
	public static void setSelect_price_num(int select_price_num) {
		Config.select_price_num = select_price_num;
	}
	public static String getSelect_price_name() {
		return select_price_name;
	}
	public static void setSelect_price_name(String select_price_name) {
		Config.select_price_name = select_price_name;
	}
	public static int getSelect_company_num() {
		return select_company_num;
	}
	public static void setSelect_company_num(int select_company_num) {
		Config.select_company_num = select_company_num;
	}
	public static String getSelect_company_name() {
		return select_company_name;
	}
	public static void setSelect_company_name(String select_company_name) {
		Config.select_company_name = select_company_name;
	}
	public static String getSelect_query() {
		return select_query;
	}
	public static void setSelect_query(String select_query) {
		Config.select_query = select_query;
	}
	public static String getSearch_ip() {
		return search_ip;
	}
	public static void setSearch_ip(String search_ip) {
		Config.search_ip = search_ip;
	}
	public static int getSearch_port() {
		return search_port;
	}
	public static void setSearch_port(int search_port) {
		Config.search_port = search_port;
	}
	public static int getSearch_thread() {
		return search_thread;
	}
	public static void setSearch_thread(int search_thread) {
		Config.search_thread = search_thread;
	}
	public static int getSearch_timeout() {
		return search_timeout;
	}
	public static void setSearch_timeout(int search_timeout) {
		Config.search_timeout = search_timeout;
	}
	public static String getIndex_field() {
		return index_field;
	}
	public static void setIndex_field(String index_field) {
		Config.index_field = index_field;
	}
	public static String getClass_field() {
		return class_field;
	}
	public static void setClass_field(String class_field) {
		Config.class_field = class_field;
	}
	public static int getPage_count() {
		return page_count;
	}
	public static void setPage_count(int page_count) {
		Config.page_count = page_count;
	}
	public static int getRecom_count() {
		return recom_count;
	}
	public static void setRecom_count(int recom_count) {
		Config.recom_count = recom_count;
	}
	public static String getRecom_option() {
		return recom_option;
	}
	public static void setRecom_option(String recom_option) {
		Config.recom_option = recom_option;
	}
	public static String getNo_separator() {
		return no_separator;
	}
	public static void setNo_separator(String no_separator) {
		Config.no_separator = no_separator;
	}
	public static int getUpdate_fetch_size() {
		return update_fetch_size;
	}
	public static void setUpdate_fetch_size(int update_fetch_size) {
		Config.update_fetch_size = update_fetch_size;
	}
	public static String getUpdate_output_key_number() {
		return update_output_key_number;
	}
	public static void setUpdate_output_key_number(String update_output_key_number) {
		Config.update_output_key_number = update_output_key_number;
	}
	public static String getUpdate_order_key_number() {
		return update_order_key_number;
	}
	public static void setUpdate_order_key_number(String update_order_key_number) {
		Config.update_order_key_number = update_order_key_number;
	}
	public static String getUpdate_price_number() {
		return update_price_number;
	}
	public static void setUpdate_price_number(String update_price_number) {
		Config.update_price_number = update_price_number;
	}
	public static String getUpdate_country_number() {
		return update_country_number;
	}
	public static void setUpdate_country_number(String update_country_number) {
		Config.update_country_number = update_country_number;
	}
	public static String getUpdate_goodsname_number() {
		return update_goodsname_number;
	}
	public static void setUpdate_goodsname_number(String update_goodsname_number) {
		Config.update_goodsname_number = update_goodsname_number;
	}
	public static String getUpdate_hscode_seq_number() {
		return update_hscode_seq_number;
	}
	public static void setUpdate_hscode_seq_number(String update_hscode_seq_number) {
		Config.update_hscode_seq_number = update_hscode_seq_number;
	}
	public static String getUpdate_hscode_val_number() {
		return update_hscode_val_number;
	}
	public static void setUpdate_hscode_val_number(String update_hscode_val_number) {
		Config.update_hscode_val_number = update_hscode_val_number;
	}
	public static String getUpdate_query() {
		return update_query;
	}
	public static void setUpdate_query(String update_query) {
		Config.update_query = update_query;
	}
	public static HashMap getDataSource() {
		return dataSource;
	}
	public static void setDataSource(HashMap dataSource) {
		Config.dataSource = dataSource;
	}
	public static HashMap<String, String> getGoods_split_regxs() {
		return goods_split_regxs;
	}
	public static void setGoods_split_regxs(HashMap<String, String> goods_split_regxs) {
		Config.goods_split_regxs = goods_split_regxs;
	}
}
