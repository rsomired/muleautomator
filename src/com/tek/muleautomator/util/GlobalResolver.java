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
    
    private void fileFinder(File rootFile, List<File> fileList, String[] fileTypes) {
		if (rootFile == null)
			return;
		if (rootFile.isDirectory()) {
			for (int i = 0; i < rootFile.listFiles().length; ++i) {
				fileFinder(rootFile.listFiles()[i], fileList, fileTypes);
			}
		} else {
			for (String fileType : fileTypes) {
				if (!fileType.startsWith("."))
					fileType = "." + fileType;
				if (rootFile.getName().toLowerCase().endsWith(fileType)) {
					fileList.add(rootFile);
				}
			}
		}
	}
    
    
    private String getRelativePathFrom(String path1, String path2){
        return path1.substring(path1.indexOf(path2)+path2.length()+1, path1.length());
    }
    
    public Map<String, String> getMap(){
        return map;
    }
    
    
    private void generateGlobalVarMap(File location) throws IOException, ParserConfigurationException, SAXException{
        List<File> defVarFiles=new ArrayList();
        fileFinder(location, defVarFiles, new String[]{"substvar"});
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

    public String resolveConcatQuery(String y){      
        // Remove &quot;
        y=y.replace("&quot;", "");
        // Remove extra quotes
        y=y.replace("\"", "");
        
        String result="";
        if(y.contains("concat")){           
            String a=y.substring(y.indexOf("(")+1,y.indexOf(","));
            String b=y.substring(y.indexOf(",")+1,y.indexOf(")"));
            if(a.contains("GlobalVariables")){
                a=getValueFromGlobalExpr(a);
            }
            if(b.contains("GlobalVariables")){
                b=getValueFromGlobalExpr(b);
            } else {
                if(b.contains("/"))
                    b=b.replace("/", "");
            }
            if(b.startsWith("\'")&&b.endsWith("\'"))
                b=b.replace("\'", "");
            if(a.endsWith("\\") && b.startsWith("\\"))
                return a+b.substring(1);
            if(a.endsWith("\\") || b.startsWith("\\"))
                return a+b;
            return a+"\\"+b;
        }
        System.err.println("NOT A CONCAT QUERY");
        return null;
    }
    
}
