/*
 * @(#)XmlUtil.java   3.8.1 2009/03/11
 *
 */
package kr.co.wisenut.common.util;

import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.StringReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kr.co.wisenut.common.Exception.ConfigException;

/**
 *
 * XmlUtil
 *
 * Copyright 2001-2009 KoreaWISEnut, Inc. All Rights Reserved.
 * This software is the proprietary information of WISEnut, Inc.
 * Bridge Release 11 March 2009
 *
 * @author  WISEnut
 * @version 3.8,1. 2009/03/11 Bridge Release
 *
 */
public class XmlUtil {
    private Element rootElement;
    protected String fileseperator = System.getProperty("file.separator");

    public XmlUtil(String path) throws ConfigException {
        File fconf = new File(path);
        if (!fconf.exists()) {
            throw new ConfigException("Unable to load the configuration file.("+path+")");
        }

        try {
            rootElement = new SAXBuilder().build(fconf).getRootElement();
        } catch (JDOMException e) {
            throw new ConfigException("XmlUtil Class, JDOMException "+ e.getMessage());
        }
    }
    public XmlUtil(String xmlstring, boolean isXML) throws ConfigException {
        if (!isXML) {
            throw new ConfigException("Please check the XML string. " + isXML);
        }

        StringReader sr = new StringReader(xmlstring);

        try {
            rootElement = new SAXBuilder().build(sr).getRootElement();
        } catch (JDOMException e) {
            throw new ConfigException("XmlUtil Class, JDOMException "+ e.getMessage());
        }
    }

    protected Element getRootElement(){
        return rootElement;
    }

    protected HashMap getElementHashMap(String nodeName) throws ConfigException {
        List nodes = rootElement.getChildren(nodeName);
        int size = nodes.size();
        HashMap m_map = new HashMap(size);
        String[] chk = new String[size];
        for(int i=0;i<size; i++){
            Element elements = (Element) nodes.get(i);
            if(elements.getAttribute("id") != null){
                String id = elements.getAttribute("id").getValue();
                for(int k=0; k<size; k++){
                    if(chk[k] !=null && chk[k].equals(id)){
                        throw new ConfigException(elements.getName() + " Duplicated id("+id+")");
                    }
                }
                chk[i] = id;
                m_map.put(id, elements);
            }
        }
        return m_map;
    }

    protected Element getElementListChild(List elementList, String childName) throws ConfigException {
        for(int i=0; i < elementList.size(); i++){
            if(((Element)elementList.get(i)).getName().equals(childName)){
                return (Element)elementList.get(i);
            }
        }
        throw new ConfigException("Missing <"+childName+"/> setting in configuration file.");
    }

    protected String getElementValue(Element element, String name) throws ConfigException {
        if(element != null && element.getAttribute(name) != null){
            return element.getAttribute(name).getValue().trim();
        } else {
            if(element != null) {
                throw new ConfigException("Missing <"+element.getName()+" /> "+name+" setting in configuration file.");
            } else {
                throw new ConfigException("Missing "+name+" setting in configuration file.");
            }
        }
    }

    protected String getElementValue(Element element, String name, String def, boolean isTrim) throws ConfigException {
        if(element != null && element.getAttribute(name) != null) {
            if(isTrim) {
                return element.getAttribute(name).getValue().trim();
            } else {
                return element.getAttribute(name).getValue();
            }
        } else {
            return def;
        }
    }

     /**
     *  Read to Node Text value
     * @param element
     * @return String
     */
    protected String getElementText(Element element) throws ConfigException {
         if(element != null ){
             return element.getTextTrim();
         }else{
            throw new ConfigException("Missing Element setting in configuration file.");
         }
    }

    protected String getElementChildText(Element element, String name) throws ConfigException {
        if(element != null) {
            if(element.getChild(name) != null) {
                return element.getChild(name).getTextTrim();
            }
        } else {
            throw new ConfigException("Missing <"+name+" /> setting in configuration file.");
        }
        return "";
    }

    protected String getElementChildText(Element element, String name, String defaultValue) throws ConfigException {
        if(element != null && element.getChild(name) != null) {
            return element.getChild(name).getTextTrim();
        } else {
            return defaultValue;
        }
    }

    protected String getElementValue(Element element, String name, String defaultValue) throws ConfigException {
        return getElementValue(element, name, defaultValue, true);
    }

    protected List getChildrenElementList(Element element, String childName){
        if(element != null && element.getChildren(childName) != null){
            return element.getChildren(childName);
        } else {
            return null;
        }
    }

     /**
     * Read to Node Text value
     * @param element
     * @param name
     * @return  String
     */
    protected String getText(Element element, String name){
        String text = "";
        if(element.getChild(name) != null){
            text = element.getChild(name).getText();
        }
        return text;
    }

    protected String getTextTrim( Element element, String name){ return (getText(element, name)).trim(); }
}
