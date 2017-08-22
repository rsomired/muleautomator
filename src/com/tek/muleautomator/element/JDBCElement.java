package com.tek.muleautomator.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JDBCElement {

	
	public static class JDBCUpdateActivity {
		private static String description, connectionPath, activityType;
		private int TIMEOUT;
		private String sqlQuery;
		private List<String> queryParams;
		private boolean commit, batchUpdate, emptyStrAsNil;
		
		public JDBCUpdateActivity(Node targetNode){
			queryParams=new ArrayList<>();
			JDBCUpdateActivity.description="The JDBC Update activity performs the specified SQL INSERT, UPDATE, or DELETE statement";
			Element rootActivityElement = (Element)targetNode;
        	JDBCUpdateActivity.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	JDBCUpdateActivity.connectionPath=rootActivityElement.getElementsByTagName("jdbcSharedConfig").item(0).getTextContent();
        	this.TIMEOUT=rootActivityElement.getElementsByTagName("timeout").getLength()>0?Integer.parseInt(rootActivityElement.getElementsByTagName("timeout").item(0).getTextContent()):10;
        	this.commit=rootActivityElement.getElementsByTagName("commit").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("commit").item(0).getTextContent()):false;
        	this.batchUpdate=rootActivityElement.getElementsByTagName("batchUpdate").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("batchUpdate").item(0).getTextContent()):false;
        	this.emptyStrAsNil=rootActivityElement.getElementsByTagName("emptyStrAsNil").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("emptyStrAsNil").item(0).getTextContent()):false;
        	this.sqlQuery=rootActivityElement.getElementsByTagName("statement").item(0).getTextContent();
        	if(this.sqlQuery.contains("?")){
        		NodeList params=rootActivityElement.getElementsByTagName("parameter");
        		for(int i=0;i<params.getLength();++i){
        			Element currParam=(Element)params.item(i);
        			queryParams.add(currParam.getElementsByTagName("parameterName").item(0).getTextContent());
        		}
        	}
		}

		public static String getDescription() {
			return description;
		}

		public static String getConnectionPath() {
			return connectionPath;
		}

		public static String getActivityType() {
			return activityType;
		}

		public int getTIMEOUT() {
			return TIMEOUT;
		}

		public String getSqlQuery() {
			return sqlQuery;
		}

		public List<String> getQueryParams() {
			return queryParams;
		}

		public boolean isCommit() {
			return commit;
		}

		public boolean isBatchUpdate() {
			return batchUpdate;
		}
		
	}
	
	public static class JDBCQueryActivity {
		private static String description, connectionPath, activityType;
		private int TIMEOUT;
		private String sqlQuery;
		private List<String> queryParams;
		private boolean commit, batchUpdate, emptyStrAsNil;
		
		public JDBCQueryActivity(Node targetNode){
			queryParams=new ArrayList<>();
			JDBCUpdateActivity.description="The JDBC Query activity performs the specified SQL SELECT statement";
			Element rootActivityElement = (Element)targetNode;
        	JDBCUpdateActivity.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	JDBCUpdateActivity.connectionPath=rootActivityElement.getElementsByTagName("jdbcSharedConfig").item(0).getTextContent();
        	this.TIMEOUT=rootActivityElement.getElementsByTagName("timeout").getLength()>0?Integer.parseInt(rootActivityElement.getElementsByTagName("timeout").item(0).getTextContent()):10;
        	this.commit=rootActivityElement.getElementsByTagName("commit").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("commit").item(0).getTextContent()):false;
        	this.batchUpdate=rootActivityElement.getElementsByTagName("batchUpdate").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("batchUpdate").item(0).getTextContent()):false;
        	this.emptyStrAsNil=rootActivityElement.getElementsByTagName("emptyStrAsNil").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("emptyStrAsNil").item(0).getTextContent()):false;
        	this.sqlQuery=rootActivityElement.getElementsByTagName("statement").item(0).getTextContent();
        	if(this.sqlQuery.contains("?")){
        		NodeList params=rootActivityElement.getElementsByTagName("parameter");
        		for(int i=0;i<params.getLength();++i){
        			Element currParam=(Element)params.item(i);
        			queryParams.add(currParam.getElementsByTagName("parameterName").item(0).getTextContent());
        		}
        	}
		}

		public static String getDescription() {
			return description;
		}

		public static String getConnectionPath() {
			return connectionPath;
		}

		public static String getActivityType() {
			return activityType;
		}

		public int getTIMEOUT() {
			return TIMEOUT;
		}

		public String getSqlQuery() {
			return sqlQuery;
		}

		public List<String> getQueryParams() {
			return queryParams;
		}

		public boolean isCommit() {
			return commit;
		}

		public boolean isBatchUpdate() {
			return batchUpdate;
		}
		
	}

	public static class JDBCCallActivity {
		private static String description;
		private int TIMEOUT, maxRows;
		private boolean emptyStrAsNil;
		private String procedureName;
		private String packageName;
		private List<ProcedureCallParameter> params;
		class ProcedureCallParameter{
			String colName, typeName;
			int colType, dataType;
			public String getColName() {
				return colName;
			}
			public void setColName(String colName) {
				this.colName = colName;
			}
			public String getTypeName() {
				return typeName;
			}
			public void setTypeName(String typeName) {
				this.typeName = typeName;
			}
			public int getColType() {
				return colType;
			}
			public void setColType(int colType) {
				this.colType = colType;
			}
			public int getDataType() {
				return dataType;
			}
			public void setDataType(int dataType) {
				this.dataType = dataType;
			}
			
			
			
		}
		
		public JDBCCallActivity(Node targetNode){
			this.packageName="default.package";
			JDBCCallActivity.description="The JDBC Call Procedure activity calls a database procedure using the specified JDBC connection";
			Element rootActivityElement = (Element)targetNode;
        	JDBCUpdateActivity.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	JDBCUpdateActivity.connectionPath=rootActivityElement.getElementsByTagName("jdbcSharedConfig").item(0).getTextContent();
        	this.TIMEOUT=rootActivityElement.getElementsByTagName("timeout").getLength()>0?Integer.parseInt(rootActivityElement.getElementsByTagName("timeout").item(0).getTextContent()):10;
        	this.maxRows=rootActivityElement.getElementsByTagName("maxRows").getLength()>0?Integer.parseInt(rootActivityElement.getElementsByTagName("maxRows").item(0).getTextContent()):10;
        	
        	this.emptyStrAsNil=rootActivityElement.getElementsByTagName("emptyStrAsNil").getLength()>0?Boolean.parseBoolean(rootActivityElement.getElementsByTagName("emptyStrAsNil").item(0).getTextContent()):false;
			this.procedureName=rootActivityElement.getElementsByTagName("ProcedureName").item(0).getTextContent();
			
			NodeList allParamsNodeList=rootActivityElement.getElementsByTagName("parameter");
			this.params=new ArrayList<>();
			for(int i=0;i<allParamsNodeList.getLength();++i){
				Element currParam=(Element)allParamsNodeList.item(i);
				ProcedureCallParameter pcp=new ProcedureCallParameter();
				pcp.setColName(currParam.getElementsByTagName("colName").item(0).getTextContent());
				pcp.setColType(Integer.valueOf(currParam.getElementsByTagName("colType").item(0).getTextContent()));
				pcp.setDataType(Integer.valueOf(currParam.getElementsByTagName("dataType").item(0).getTextContent()));
				pcp.setTypeName(currParam.getElementsByTagName("typeName").item(0).getTextContent());
				this.params.add(pcp);
			}
			
		}
	}

	public static class JDBCSQLDirectActivity {
		private static String description, connectionPath, activityType;
		private int TIMEOUT;
		private String sqlQuery;
		private int OUTPUT_noOfUpdates;
		
		public JDBCSQLDirectActivity(Node targetNode){
			
			JDBCUpdateActivity.description="The SQL Direct activity executes a SQL statement that you provide. This activity allows you to build a SQL statement dynamically (using other activities), then pass the SQL statement into this activity’s input";
			Element rootActivityElement = (Element)targetNode;
        	JDBCUpdateActivity.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	JDBCUpdateActivity.connectionPath=rootActivityElement.getElementsByTagName("jdbcSharedConfig").item(0).getTextContent();
        	this.TIMEOUT=rootActivityElement.getElementsByTagName("timeout").getLength()>0?Integer.parseInt(rootActivityElement.getElementsByTagName("timeout").item(0).getTextContent()):10;
        	this.sqlQuery=rootActivityElement.getElementsByTagName("statement").item(0).getTextContent();
        	
		}
		
		public int getNoOfUpdates() {
			return this.OUTPUT_noOfUpdates;
		}

		public static String getDescription() {
			return description;
		}

		public static String getConnectionPath() {
			return connectionPath;
		}

		public static String getActivityType() {
			return activityType;
		}

		public int getTIMEOUT() {
			return TIMEOUT;
		}

		public String getSqlQuery() {
			return sqlQuery;
		}

		
		
	}

}
