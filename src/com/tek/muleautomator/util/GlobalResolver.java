package com.tek.muleautomator.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author asgupta
 */
public class GlobalResolver {
    Map<String, String> map;
    
    public GlobalResolver(File location){
        map=new HashMap<>();
        try{
            generateGlobalVarMap(location);
        } catch( Exception E){
            System.err.print(E);
        }
    }
    
    public GlobalResolver(String location){
        this(new File(location));
    }
    
    
    
    
    private String getRelativePathFrom(String path1, String path2){
        return path1.substring(path1.indexOf(path2)+path2.length()+1, path1.length());
    }
    
    public Map<String, String> getMap(){
        return map;
    }
    
    
    private void generateGlobalVarMap(File location) throws IOException, ParserConfigurationException, SAXException{
        List<File> defVarFiles=new ArrayList();
        MuleAutomatorUtil.fileFinder(location, defVarFiles, new String[]{"substvar"});
        if(defVarFiles.size()==0){
            System.err.println("No default Var files found in "+location.getPath());
            return;
        }
        for(File currFile: defVarFiles){
            String prefix="";
            String relativePath=getRelativePathFrom(currFile.getCanonicalPath(), location.getCanonicalPath());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document docIn = documentBuilder.parse(currFile);
            if(relativePath.length()> "defaultVars.substvar".length()){
                prefix=relativePath.substring(0,relativePath.indexOf("defaultVars.substvar"));
            }
            
            NodeList globalVarsNodeList=docIn.getElementsByTagName("globalVariable");
            for(int i=0;i<globalVarsNodeList.getLength();++i){
                Element currGv=(Element)globalVarsNodeList.item(i);
                String key=prefix+currGv.getElementsByTagName("name").item(0).getTextContent();
                key=key.replace("\\", "/");
                String value=currGv.getElementsByTagName("value").item(0).getTextContent();
                map.put(key, value);
            }        
            
        }
        
    }
    
    
    
    /**
     * GET VALUE FROM GLOBAL EXPRESSION PATH
     * Uses String operation to resolve into possible key in GlobalVarsMap
     * Returns corresponding value, if exists; otherwise returns null
     * @param String: expression
     * @return String: value for that Global XPATH
     */
    
    public String getValueFromGlobalExpr(String expr){    
    	if(this.map.size()==0)
    		return expr;
        String key=expr.substring(expr.indexOf("GlobalVariables")+"GlobalVariables".length()+1,expr.length());
        return map.get(key);
    }
    
    
    /**
     * Concatenate Query Resolver
     * This method will take a concat query and try to resolve it into equivalent path. Looks for GlobalVars by calling
     * getValueFromGlobalExpr method (above)
     * 
     * @param String: Entire concat expression
     * @return Concatenated String
     */

    public String resolveConcatQuery(String concatQuery){      
        // Remove &quot;
        concatQuery=concatQuery.replace("&quot;", "");
        // Remove extra quotes
        concatQuery=concatQuery.replace("\"", "");
        if(this.map.size()==0)
        	return concatQuery;
        String result="";
        if(concatQuery.contains("concat")){           
            String expr1=concatQuery.substring(concatQuery.indexOf("(")+1,concatQuery.indexOf(","));
            String expr2=concatQuery.substring(concatQuery.indexOf(",")+1,concatQuery.indexOf(")"));
            if(expr1.contains("GlobalVariables")){
                expr1=getValueFromGlobalExpr(expr1);
            }
            if(expr2.contains("GlobalVariables")){
                expr2=getValueFromGlobalExpr(expr2);
            } else {
                if(expr2.contains("/"))
                    expr2=expr2.replace("/", "");
            }
            if(expr2.startsWith("\'")&&expr2.endsWith("\'"))
                expr2=expr2.replace("\'", "");
            if(expr1.endsWith("\\") && expr2.startsWith("\\"))
                return expr1+expr2.substring(1);
            if(expr1.endsWith("\\") || expr2.startsWith("\\"))
                return expr1+expr2;
            return expr1+"\\"+expr2;
        }
        System.err.println("NOT A CONCAT QUERY");
        return null;
    }
    
}
