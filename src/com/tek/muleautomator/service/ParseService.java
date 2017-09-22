package com.tek.muleautomator.service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.element.ParseElement.ParseDataActivity;
import com.tek.muleautomator.element.ParseElement.RenderDataActivity;
import com.tek.muleautomator.util.MuleConfigConnection;
public class ParseService {
	public static void parseData(String muleConfigPath, ParseDataActivity parseDataActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element parser = doc.createElement("dw:transform-message");
			parser.setAttribute("input-payload mimeType", "application/json");
			parser.setAttribute("set-payload","");
			parser.setAttribute("doc:name", "Transform message");
			flow.appendChild(parser);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		public static void renderData(String muleConfigPath, RenderDataActivity renderDataActivity, Element flow) {
			try {
				Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
				Element render = doc.createElement("dw:transform-message");
				render.setAttribute("input-payload mimeType", "application/json");
				render.setAttribute("set-payload","");
				render.setAttribute("doc:name", "Transform message");
				flow.appendChild(render);
			} catch (Exception e) {
				e.printStackTrace();
			}
}
}
