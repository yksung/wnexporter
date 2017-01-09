package kr.co.wisenut.config;

import java.util.HashMap;

public class SF1Collection {
	private String collectionName;
	private String searchFields;
	private HashMap<String,String> documentFieldsMap;
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collection) {
		this.collectionName = collection;
	}
	public String getSearchFields() {
		return searchFields;
	}
	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}
	public HashMap<String, String> getDocumentFieldsMap() {
		return documentFieldsMap;
	}
	public void setDocumentFieldsMap(HashMap<String, String> documentFieldsMap) {
		this.documentFieldsMap = documentFieldsMap;
	}
	
}
