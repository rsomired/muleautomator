/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tek.muleautomator.element;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FileElement {
    
	
    public class FileWriteActivity { 
    	
    	private String activityType;
        private String fileName;
        private String textContent;
        private String encoding;
        
        public FileWriteActivity(Node node){
        	Element rootActivityElement = (Element)node;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileElement=(Element)rootActivityElement.getElementsByTagName("fileName").item(0);
        	this.fileName=fileElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        	Element textContentElement=(Element)rootActivityElement.getElementsByTagName("textContent").item(0);
        	if(textContentElement!=null){
        		this.textContent=textContentElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        	}
        	Element encodingElement=(Element)rootActivityElement.getElementsByTagName("encoding").item(0);
        	if(encodingElement!=null){
        		this.encoding=encodingElement.getTextContent();
        	}
        }      

		public String getActivityType() {
			return activityType;
		}

		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getTextContent() {
			return textContent;
		}

		public void setTextContent(String textContent) {
			this.textContent = textContent;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}
    
        
    }
    
    public class FileCreateActivity {
        private String activityType;
        private String fileName;

        
        public FileCreateActivity(Node activityNode){
        	Element rootActivityElement = (Element)activityNode;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileElement=(Element)rootActivityElement.getElementsByTagName("fileName").item(0);
        	this.fileName=fileElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
    }
    
    public class FileReadActivity {
        private String activityType;
        private String fileName;
        private String encoding;
        
      

        public String getActivityType() {
			return activityType;
		}


		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}


		public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}


		public String getEncoding() {
			return encoding;
		}


		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}


		public FileReadActivity(Node node){
        	Element rootActivityElement = (Element)node;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileElement=(Element)rootActivityElement.getElementsByTagName("fileName").item(0);
        	this.fileName=fileElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        	Element encodingElement=(Element)rootActivityElement.getElementsByTagName("encoding").item(0);
        	if(encodingElement!=null){
        		this.encoding=encodingElement.getTextContent();
        	}
        }
        
    }

    public class FileRemoveActivity{
    	private String activityType;
    	private String fileName;
		public String getActivityType() {
			return activityType;
		}

	
    	public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}


		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}


		public FileRemoveActivity(Node targetNode){
    		Element rootActivityElement = (Element)targetNode;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileElement=(Element)rootActivityElement.getElementsByTagName("fileName").item(0);
        	this.fileName=fileElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
    	}
    	
    }
    	
    public class FileListActivity {
    	private String activityType;
    	private String fileName;
		public String getActivityType() {
			return activityType;
		}

	
    	public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}


		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}


		public FileListActivity(Node targetNode){
    		Element rootActivityElement = (Element)targetNode;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileElement=(Element)rootActivityElement.getElementsByTagName("fileName").item(0);
        	this.fileName=fileElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
    	}
    	
    }

    public class FileRenameActivity {
    	String activityType;
    	String fromFileName, toFileName;
    	
    	public FileRenameActivity(Node targetNode){
    		Element rootActivityElement = (Element)targetNode;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileFromElement=(Element)rootActivityElement.getElementsByTagName("fromFileName").item(0);
        	this.fromFileName=fileFromElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        	Element filetoElement=(Element)rootActivityElement.getElementsByTagName("toFileName").item(0);
        	this.toFileName=filetoElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
    	}

		public String getActivityType() {
			return activityType;
		}

		public String getFromFileName() {
			return fromFileName;
		}

		public String getToFileName() {
			return toFileName;
		}
    	
    }
    
    public class FileCopyActivity {
    	String activityType;
    	String fromFileName, toFileName;
    	
    	public FileCopyActivity(Node targetNode){
    		Element rootActivityElement = (Element)targetNode;
        	this.activityType=rootActivityElement.getElementsByTagName("pd:type").item(0).getTextContent();
        	Element fileFromElement=(Element)rootActivityElement.getElementsByTagName("fromFileName").item(0);
        	this.fromFileName=fileFromElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
        	Element filetoElement=(Element)rootActivityElement.getElementsByTagName("toFileName").item(0);
        	this.toFileName=filetoElement.getElementsByTagName("xsl:value-of").item(0).getAttributes().getNamedItem("select").getNodeValue();
    	}

		public String getActivityType() {
			return activityType;
		}

		public String getFromFileName() {
			return fromFileName;
		}

		public String getToFileName() {
			return toFileName;
		}
    	
    }

}
