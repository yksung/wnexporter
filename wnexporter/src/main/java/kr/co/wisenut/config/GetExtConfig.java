package kr.co.wisenut.config;

import kr.co.wisenut.common.util.XmlUtil;
import kr.co.wisenut.common.Exception.ConfigException;
import kr.co.wisenut.common.logger.Log2;

import org.jdom.Element;

import java.util.HashMap;
import java.util.List;

public class GetExtConfig extends XmlUtil {
		private String mPath = null;
	    /**
	     *
	     * @param path
	     * @throws ConfigException
	     */
	    public GetExtConfig(String path) throws ConfigException {
	        super(path);
	        mPath = path;
	    }
	    
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getCachePath() throws ConfigException {
	      String tmp ;
	      try {
	    	  Element elmnt = this.getRootElement().getChild("cache-path");
	          tmp = elmnt.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <cache-path> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <cache-path> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public boolean getDeleteCache() throws ConfigException {
	      boolean value = false;
	      try {
	    	  Element elmnt = this.getRootElement().getChild("delete-cache");
	          String tmp = elmnt.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <delete-cache> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	          if( !(tmp.equalsIgnoreCase("y")||tmp.equalsIgnoreCase("n")) ){
	        	  Log2.error("[Only 'y' or 'n' (not case-sensitive) is available for <delete-cache>.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	          if(tmp.equalsIgnoreCase("y")) value = true;
	          
	      } catch (ConfigException e) {
	          Log2.error("[Missing <delete-cache> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return value;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getDSN() throws ConfigException {
	      String tmp ;
	      try {
	    	  Element elmnt = this.getRootElement().getChild("dsn");
	          tmp = elmnt.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <dsn> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <dsn> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getSelectKeyNum() throws ConfigException {
		  int tmp=-1;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");

	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("key-column");
	    	  Element grandchild = child.getChild("no");
	    	  tmp = Integer.valueOf(grandchild.getText());
	      } catch (NumberFormatException e) {
	          Log2.error("[Text of <Select> - <key-column> - <no> should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectKeyName() throws ConfigException {
		  String tmp;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      
	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("key-column");
	    	  Element grandchild = child.getChild("name");
	    	  tmp = grandchild.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <key-column> - <name> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <key-column> - <name> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getSelectCountryNum() throws ConfigException {
		  int tmp=-1;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");

	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("country-column");
	    	  Element grandchild = child.getChild("no");
	    	  tmp = Integer.valueOf(grandchild.getText());
	      } catch (NumberFormatException e) {
	          Log2.error("[Text of <Select> - <country-column> - <no> should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectCountryName() throws ConfigException {
		  String tmp;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      
	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("country-column");
	    	  Element grandchild = child.getChild("name");
	    	  tmp = grandchild.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <country-column> - <name> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <country-column> - <name> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getSelectPriceNum() throws ConfigException {
		  int tmp=-1;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");

	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("price-column");
	    	  Element grandchild = child.getChild("no");
	    	  tmp = Integer.valueOf(grandchild.getText());
	      } catch (NumberFormatException e) {
	          Log2.error("[Text of <Select> - <price-column> - <no>  should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectPriceName() throws ConfigException {
		  String tmp;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      
	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("price-column");
	    	  Element grandchild = child.getChild("name");
	    	  tmp = grandchild.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <price-column> - <name> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <price-column> - <name> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getSelectGoodsNum() throws ConfigException {
		  int tmp=-1;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");

	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("goods-column");
	    	  Element grandchild = child.getChild("no");
	    	  tmp = Integer.valueOf(grandchild.getText());
	      } catch (NumberFormatException e) {
	          Log2.error("[Text of <Select> - <goods-column> - <no>  should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectGoodsName() throws ConfigException {
		  String tmp;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      
	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("goods-column");
	    	  Element grandchild = child.getChild("name");
	    	  tmp = grandchild.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <goods-column> - <name> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <goods-column> - <name> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getSelectCompanyNum() throws ConfigException {
		  int tmp=-1;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");

	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("company-column");
	    	  Element grandchild = child.getChild("no");
	    	  tmp = Integer.valueOf(grandchild.getText());
	      } catch (NumberFormatException e) {
	          Log2.error("[Text of <Select> - <company-column> - <no>  should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectCompanyName() throws ConfigException {
		  String tmp;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      
	      try {
	    	  Element child = ((Element)lst.get(0)).getChild("company-column");
	    	  Element grandchild = child.getChild("name");
	    	  tmp = grandchild.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <company-column> - <name> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <company-column> - <name> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSelectQuery() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Select");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Select");
	          tmp = elmnt.getChildText("query");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Select> - <query> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Select> - <query> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSearchIp() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("ip");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <ip> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <ip> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getSearchPort() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("port");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <port> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <port> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getThread() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("thread");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <thread> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <thread> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getTimeout() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("timeout");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <timeout> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <timeout> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getIndexField() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("index-field");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <index-field> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <index-field> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getClassField() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("class-field");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <class-field> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <class-field> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getPageCount() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("page-count");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <page-count> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <page-count> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getRecomCount() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("recommend-count");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <recommend-count> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <recommend-count> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getRecomOpt() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("recommend-option");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <recommend-option> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <recommend-option> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getNoSeparator() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          tmp = elmnt.getChildText("no-separator");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Search> - <no-separator> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Search> - <no-separator> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public int getUpdateFetchSize() throws ConfigException {
		  int tmp = -1;
		  
		  List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = Integer.valueOf(elmnt.getChildText("fetch-size"));
	      }  catch (NumberFormatException e) {
	          Log2.error("[Text of <Update> - <fetch-size>  should be a number type.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateOutputKeyColumn() throws ConfigException {
		  String tmp ;
		  
		  List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("output-key-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <output-key-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <output-key-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateOrderKeyColumn() throws ConfigException {
		  String tmp ;
		  
		  List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("order-key-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <order-key-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <order-key-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdatePriceColumn() throws ConfigException {
		  String tmp ;
		  
		  List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("price-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <price-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <price-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateCountryColumn() throws ConfigException {
		  String tmp ;
			
	      List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("country-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <country-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <country-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateGoodsnameColumn() throws ConfigException {
		  String tmp ;
			
	      List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("goodsname-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <goodsname-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <goodsname-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateHscodeSeqColumn() throws ConfigException {
		  String tmp ;
			
	      List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("hscode-seq-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <hscode-seq-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <hscode-seq-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateHscodeValColumn() throws ConfigException {
		  String tmp ;
			
	      List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("hscode-val-column");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <hscode-val-column> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <hscode-val-column> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getUpdateQuery() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Update");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Update");
	          tmp = elmnt.getChildText("query");
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <Update> - <query> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <query> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	  
	  /**
	  *
	  * @return String
	  * @throws ConfigException
	  */
	 public String getPath() throws ConfigException {
	     String tmp ;
	
	     List lst = this.getChildrenElementList(this.getRootElement(), "DataSource");
	     Element elmnt;
	
	     try {
	         elmnt = this.getElementListChild(lst, "DataSource");
	         tmp = elmnt.getChildText("Path");
	         if(tmp == null || "".equals(tmp)){
	        	 Log2.error("[Missing <DataSource> - <Path> setting in configuration file.]");
		         throw new ConfigException("Could not parse Directory Config.");
	          }
	     } catch (ConfigException e) {
	         Log2.error("[Missing <DataSource> - <Path> setting in configuration file.]");
	         throw new ConfigException("Could not parse Directory Config.");
	     }
	     return tmp;
	 }

	/**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public String getFieldSeparator() throws ConfigException {
	      String tmp ;
	      try {
	    	  Element elmnt = this.getRootElement().getChild("field-separator");
	          tmp = elmnt.getText();
	          if(tmp == null || "".equals(tmp)){
	        	  Log2.error("[Missing <field-separator> setting in configuration file.]");
		          throw new ConfigException("Could not parse Directory Config.");
	          }
	      } catch (ConfigException e) {
	          Log2.error("[Missing <field-separator> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	      return tmp;
	  }
	
	/**
	   *
	   * @return String
	   * @throws ConfigException
	   */
	  public HashMap<String, String> getGoodsSplitRegx() throws ConfigException {
	      String tmp ;
	
	      List lst = this.getChildrenElementList(this.getRootElement(), "Search");
	      Element elmnt;
	      
	      try {
	          elmnt = this.getElementListChild(lst, "Search");
	          List<Element> eles = elmnt.getChild("goods-split-regx").getChildren();
	          HashMap<String, String> rets = new HashMap<String, String>();
	          for(Element e : eles){
	        	  String nodeName = e.getName();
	        	  String key = "";
	        	  String regx = "";
	        	  if(nodeName.equals("case")){
	        		  key = e.getAttributeValue("code");
	        		  regx = e.getText();
	        	  }else{
	        		  key = "else";
	        		  regx = e.getText().trim();
	        	  }
	        	  rets.put(key, regx);
	          }
	          
//	          if(tmp == null || "".equals(tmp)){
//	        	  Log2.error("[Missing <Update> - <query> setting in configuration file.]");
//		          throw new ConfigException("Could not parse Directory Config.");
//	          }
	          
	          return rets;
	      } catch (ConfigException e) {
	          Log2.error("[Missing <Update> - <query> setting in configuration file.]");
	          throw new ConfigException("Could not parse Directory Config.");
	      }
	  }
	  
	  public static void main(String[] args){
		  try {
			GetExtConfig ext = new GetExtConfig("c:\\test\\config\\config04b1.xml");
			
			System.out.println("KEY COLUMN - no : " + ext.getSelectKeyNum());
			System.out.println("KEY COLUMN - name : " + ext.getSelectKeyName());
		} catch (ConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }
}
