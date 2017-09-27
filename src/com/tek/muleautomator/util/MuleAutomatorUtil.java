package com.tek.muleautomator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.element.JavaElement.JavaCodeActivity;

public class MuleAutomatorUtil {

	public static void trasfromData(Document doc, String filepath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loggerElement(String muleConfigPath, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element loggerElement = doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			flow.appendChild(loggerElement);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeToFile(String filePath, String sourceCode) {
		BufferedWriter bufferedWriter = null;
		try {
			String folderPath = filePath.substring(0, filePath.lastIndexOf("\\"));
			// File folder = new File(folderPath);
			// folder.mkdirs();
			// check if file exist, otherwise creates the file before writing
			File myFile = new File(filePath);
			myFile.getParentFile().mkdirs();
			myFile.createNewFile();

			Writer writer = new FileWriter(myFile);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(sourceCode);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedWriter != null)
					bufferedWriter.close();
			} catch (Exception ex) {

			}
		}
	}

	public static boolean renameFile(String oldFile, String newFile) {
		File file = new File(oldFile);
		File file2 = new File(newFile);
		return file.renameTo(file2);

	}

	public static void fileFinder(File rootFile, List<File> fileList, String[] fileTypes) {
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

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();

	}
}
