package com.tek.muleautomator.handler;

import com.tek.muleautomator.service.FileService;

public class FileHandler {

	static FileService fileService = new FileService();
	
	public static void generateMuleFlow(String activityType, String muleProjectLocation) {
		switch(activityType) {
		case "com.tibco.plugin.file.FileCreateActivity":
			System.out.println("com.tibco.plugin.file.FileCreateActivity-----The Create File activity creates a new file or directory with the specified name. When creating a file, you can also provide the file contents. ");
			fileService.fileCreate(muleProjectLocation);
			break;
			
		case "com.tibco.plugin.file.ListFilesActivity":
			System.out.println("com.tibco.plugin.file.ListFilesActivity-----The List Files activity returns information about files or directories, or a listing of all the files in the specified directory. ");
			break;

		case "com.tibco.plugin.file.FileReadActivity":
			System.out.println("com.tibco.plugin.file.FileReadActivity-----The Read File activity is used to read a file and place its contents into the activity�s output. ");
			break;

		case "com.tibco.plugin.file.FileRemoveActivity":
			System.out.println("com.tibco.plugin.file.FileRemoveActivity-----The Remove File activity removes the specified file. This activity can also remove empty directories. If a directory that is not empty is specified, an exception is thrown. ");
			fileService.fileDelete(muleProjectLocation);
			break;

		case "com.tibco.plugin.file.FileRenameActivity":
			System.out.println("com.tibco.plugin.file.FileRenameActivity-----The Rename File activity is used to rename or move files. This activity can also rename directories, but you cannot use this activity to move a directory to a new location. ");
			break;

		case "com.tibco.plugin.file.FileSignalIn":
			System.out.println("com.tibco.plugin.file.FileSignalIn-----The Wait for File Change activity waits for a file creation, modification, or deletion event to occur during process execution. When this activity is executed, the process instance suspends and waits for the specified change to occur before resuming.");
			break;

		case "com.tibco.plugin.file.FileWriteActivity":
			System.out.println("com.tibco.plugin.file.FileWriteActivity-----The Write File activity writes content to the specified file.");
			break;
		}
	}	

}
