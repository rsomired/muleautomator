package com.tek.muleautomator.service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.element.ParseElement.ParseDataActivity;
import com.tek.muleautomator.element.ParseElement.RenderDataActivity;
import com.tek.muleautomator.util.MuleConfigConnection;
public class ParseService {
	public  static void parseData(String muleConfigPath, ParseDataActivity parseDataActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element parser = doc.createElement("dw:transform-message");
			
			Element el=(Element)doc.getFirstChild();
			el.setAttribute("xmlns:dw", "http://www.mulesoft.org/schema/mule/ee/dw");
					
			
			parser.setAttribute("xsi:schemaLocation", "http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd");
			parser.setAttribute("set-payload","");
			parser.setAttribute("doc:name", "Transform message");
			flow.appendChild(parser);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		public static  void renderData(String muleConfigPath, RenderDataActivity renderDataActivity, Element flow) {
			try {
				Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
				Element el=(Element)doc.getFirstChild();
				el.setAttribute("xmlns:dw", "http://www.mulesoft.org/schema/mule/ee/dw");
				
				Element render = doc.createElement("dw:transform-message");
				render.setAttribute("xsi:schemaLocation", "http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd");
				
				render.setAttribute("set-payload","");
				render.setAttribute("doc:name", "Transform message");
				flow.appendChild(render);
			} catch (Exception e) {
				e.printStackTrace();
			}
}
}
