/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tek.muleautomator.util;

import java.io.File;

/**
 *
 * @author asgupta
 */
public class MuleAutomatorConstants {
	public static String TIBCO_PROJECT_ROOT_FOLDER;
    static String seperator = File.separator;
    public static GlobalVarsResolver globalVarsResolver;
   
    
    public static void loadGlobalVars(String location) {
    	globalVarsResolver=new GlobalVarsResolver(location);
    }
    
    public static String generateMavenCommand(String projectName){
        return "cmd /c mvn archetype:generate -B -DarchetypeGroupId=org.mule.tools.maven -DarchetypeArtifactId=maven-achetype-mule-app -DarchetypeVersion=1.0 -DgroupId=org.mycompany.app -DartifactId="
				+ projectName + " -Dversion=1.0-SNAPSHOT -DmuleVersion=3.8.1 -Dpackage=org.mycompany.app";
    }
    
    public static String generateMuleConfigPath(String directory,String projectName){
        return directory + seperator + projectName + seperator + "src" + seperator + "main" + seperator
				+ "app" + seperator + "mule-config.xml";
    }
    
    public static String generateSourcePath(String projectName){
        return System.getProperty("user.dir") + seperator + projectName;
    }
    
    public static String generateMuleResourcesPath(String directory, String projectName){
        return directory + seperator + projectName + seperator + "src" + seperator + "main"+ seperator + "resources";
    }
    
    public static String generateMuleTestClassFilesPath(String directory, String projectName){
        return directory + seperator + projectName + seperator + "src" + seperator + "test"
				+ seperator + "java" + seperator + "org";
    }

	public static String generateDefaultVarsPath(String tibcoProjectLocationRootFolder) {
		return tibcoProjectLocationRootFolder+"/defaultVars/";
	}
    
    
    
}
