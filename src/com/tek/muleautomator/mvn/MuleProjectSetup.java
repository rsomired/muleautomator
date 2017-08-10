package com.tek.muleautomator.mvn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tek.muleautomator.util.Utilities;

public class MuleProjectSetup {

	String seperator = File.separator;

	public void createMuleProject(String tibcoProjectLocationRootFolder, String directory, String projectName)
			throws IOException {

		String sourceFile = Utilities.generateSourcePath(projectName);
		String destinationFile = directory + seperator + projectName;
		String muleConfigPath = Utilities.generateMuleConfigPath(directory, projectName);
		String muleResourcesPath = Utilities.generateMuleResourcesPath(directory, projectName);
		String testclassFilesPath = Utilities.generateMuleTestClassFilesPath(directory);
		String cmd = Utilities.generateMavenCommand(projectName);
                
		try {
			String outlist[] = createMuleProjectByMaven(cmd);
			for (int i = 0; i < outlist.length; i++) {
				System.out.println(outlist[i]);
			}
			moveMuleProjectToSpecifiedDirectory(sourceFile, destinationFile);

			deleteDefaultTestFiles(new File(testclassFilesPath));

			if (new File(muleConfigPath).exists()) {
				//removeDefultFlow(muleConfigPath);
				setDefaultNamespace(muleConfigPath);
			}

			List<File> tibcoFiles = new ArrayList<>();
                        
			fileFinder(new File(tibcoProjectLocationRootFolder), tibcoFiles, new String[] { "wsdl", "xsd", "xsl" });
                        moveTibcoFilesToMuleProject(tibcoFiles, muleResourcesPath);
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private void setDefaultNamespace(String muleConfigPath) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(muleConfigPath);
			Element Mule = (Element) doc.getFirstChild();
			Mule.setAttribute("xmlns:doc", "http://www.mulesoft.org/schema/mule/documentation");
                        
                        // ashish
                        
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(muleConfigPath));
			transformer.transform(source, result);
                        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void moveTibcoFilesToMuleProject(List<File> tibcoFiles, String muleResourcesPath) {
		File muleRootDirectory = new File(muleResourcesPath);
		for (File f : tibcoFiles) {
                    System.out.println("Copying file: "+f.getPath()+" to "+muleRootDirectory.getAbsolutePath() + seperator + f.getName());
			copyFile(f, new File(muleRootDirectory.getAbsolutePath() + seperator + f.getName()));
		}
	}

	private void copyFile(File source, File destination) {
		if (!destination.exists())
			destination.mkdirs();
		try {
			Files.copy(Paths.get(source.getPath()), Paths.get(destination.getPath()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

	private void moveMuleProjectToSpecifiedDirectory(String sourceFile, String destinationFile) {
		File srcFile = new File(sourceFile);
		File destFile = new File(destinationFile);
		destFile.mkdirs();
		moveFiles(srcFile, destFile);
		deleteDirectory(srcFile);
	}

	private static boolean moveFiles(File sourceFile, File destFile) {
		if (sourceFile.isDirectory()) {
			for (File file : sourceFile.listFiles()) {
				moveFiles(file, new File(destFile.getPath() + File.separator + file.getName()));
			}
		} else {
			try {
				destFile.mkdirs();
				Files.move(Paths.get(sourceFile.getPath()), Paths.get(destFile.getPath()),
						StandardCopyOption.REPLACE_EXISTING);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private static void deleteDirectory(File dir) {
		if (dir.listFiles().length == 0) {
			dir.delete();

		} else {
			for (File file : dir.listFiles()) {
				deleteDirectory(file);
			}
		}
		dir.delete();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String[] createMuleProjectByMaven(String cmd) throws IOException {
		ArrayList list = new ArrayList();
		Process proc = Runtime.getRuntime().exec(cmd);
		InputStream istr = proc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(istr));
		String str;
		while ((str = br.readLine()) != null)
			list.add(str);
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			System.err.println("Process was interrupted");
		}
		br.close();

		return (String[]) list.toArray(new String[0]);
	}

	private static void deleteDefaultTestFiles(File dir) {
		if (dir.isDirectory()) {
			String[] DirChildren = dir.list();
			for (int i = 0; i < DirChildren.length; i++) {
				deleteDefaultTestFiles(new File(dir, DirChildren[i]));
			}
		}
		dir.delete();
	}

	private static void removeDefultFlow(String filepath) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document docIn = documentBuilder.parse(new File(filepath));
			NodeList flowTags = docIn.getElementsByTagName("flow");
			Node[] flowTagNodes = new Node[flowTags.getLength()];
			for (int i = 0; i < flowTags.getLength(); ++i) {
				flowTagNodes[i] = flowTags.item(i);
			}
			Node muleTag = docIn.getDocumentElement();
			for (Node X : flowTagNodes) {
				muleTag.removeChild(X);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(docIn);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
