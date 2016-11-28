/*
 * @(#)SetConfig.java   1.0.5 2009/03/19
 *
 */
package kr.co.wisenut.config;

import java.util.HashMap;

import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.Exception.DBFactoryException;
import kr.co.wisenut.common.logger.Log2;
import kr.co.wisenut.common.util.StringUtil;
import kr.co.wisenut.config.datasource.GetDataSource;

public class SetConfig {
    public static void getConfig() throws ConfigException {
        GetExtConfig ext = new GetExtConfig(RunTimeArgs.getConf());
        Config.setDsn(ext.getDSN());
        Config.setCachepath(ext.getCachePath());
        Config.setDeleteCache(ext.getDeleteCache());
        
        Config.setSelect_key_num(ext.getSelectKeyNum());
        Config.setSelect_key_name(ext.getSelectKeyName());
        Config.setSelect_goods_num(ext.getSelectGoodsNum());
        Config.setSelect_goods_name(ext.getSelectGoodsName());
        Config.setSelect_price_num(ext.getSelectPriceNum());
        Config.setSelect_price_name(ext.getSelectPriceName());
        Config.setSelect_country_num(ext.getSelectCountryNum());
        Config.setSelect_country_name(ext.getSelectCountryName());
        Config.setSelect_company_num(ext.getSelectCompanyNum());
        Config.setSelect_company_name(ext.getSelectCompanyName());
        Config.setSelect_deal_num(ext.getSelectDealNum());
        Config.setSelect_deal_name(ext.getSelectDealName());
        Config.setSelect_use_num(ext.getSelectUseNum());
        Config.setSelect_use_name(ext.getSelectUseName());
        Config.setSelect_allow_num(ext.getSelectAllowNum());
        Config.setSelect_allow_name(ext.getSelectAllowName());
        Config.setSelect_query(ext.getSelectQuery());
        Config.setSelect_failquery(ext.getSelectFailQuery());
        
        Config.setSearch_ip(ext.getSearchIp());
        Config.setSearch_port( StringUtil.parseInt(ext.getSearchPort(), 0));
        Config.setSearch_thread( StringUtil.parseInt(ext.getThread(), 0));
        Config.setSearch_timeout( StringUtil.parseInt(ext.getTimeout(), 0));
        
        Config.setUpdate_fetch_size(ext.getUpdateFetchSize());        
        Config.setUpdate_order_key_number(ext.getUpdateOrderKeyColumn());
        Config.setUpdate_output_key_number(ext.getUpdateOutputKeyColumn());
        Config.setUpdate_price_number(ext.getUpdatePriceColumn());
        Config.setUpdate_country_number(ext.getUpdateCountryColumn());
        Config.setUpdate_goodsname_number(ext.getUpdateGoodsnameColumn());
        Config.setUpdate_company_number(ext.getUpdateCompanyColumn());
        Config.setUpdate_deal_number(ext.getUpdateDealColumn());
        Config.setUpdate_use_number(ext.getUpdateUseColumn());
        Config.setUpdate_allow_number(ext.getUpdateAllowColumn());
        Config.setUpdate_hscode_seq_number(ext.getUpdateHscodeSeqColumn());
        Config.setUpdate_hscode_val_number(ext.getUpdateHscodeValColumn());
        Config.setUpdate_query(ext.getUpdateQuery());
        
        Config.setIndex_field(ext.getIndexField());
        Config.setClass_field(ext.getClassField());
        Config.setPage_count(StringUtil.parseInt(ext.getPageCount(), 0));
        Config.setRecom_count(StringUtil.parseInt(ext.getRecomCount(), 0));
        Config.setRecom_option(ext.getRecomOpt());
        Config.setNo_separator(ext.getNoSeparator());
        Config.setGoods_split_regxs(ext.getGoodsSplitRegx());
        
        
        try {
        	String datasourcePath = ext.getPath();
        	HashMap gds = new GetDataSource(datasourcePath).getDataSource();
             Config.setDataSource(gds);
        } catch (ConfigException e) {
        	Log2.error("[error] [ConfigException] datasource.xml parsing fail");
        } catch (DBFactoryException e) {
        	Log2.error("[error] [DBFactoryException] " + e.getMessage());
        }
    }
}
