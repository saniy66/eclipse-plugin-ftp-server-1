package com.holmconsulting.ftp.server.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	private  static FileUtils instance = new FileUtils(); 
	/**
	 * Not Allowed
	 */
	private FileUtils() {
		
	}
	
	public static FileUtils getInstance() {
		return instance;
	}
	
	
	/**
	 * refresh the view since viewer.refresh() does not work since FileWrapper .. FIXME investigate 
	 */
	public List<File> reloadFiles(File rootDir ) {
		List<File> files = new ArrayList<File>();
		loadFiles(rootDir, files);
		
		return files;
	}
	
	/**
	 * Load all files under root directory and its sub directories
	 * @param file the rootDir to load from
	 * @param the list of files to be populated
	 */
	public void loadFiles(File file, List<File> files) {
		if (file.isDirectory()){
			File[] fileArr = file.listFiles();
			if(fileArr != null && fileArr.length != 0) {
				for (File fil : fileArr) {
					 loadFiles(fil, files);	
					}	
			}else {
				return;
			}
			
		}else if( file.isFile()) {
		 files.add(file);
		}
	
	}
	
	
/**
 * Delete all files under the givne root directory which is 
 * the plugin created one
 */
 public void deletFiles(File rootDir) {
		File[] fileArr = rootDir.listFiles();
		doDeleteFiles(fileArr);
 }

 private void doDeleteFiles(File[] fileArr) {
  if(fileArr != null && fileArr.length != 0) {
	for (File file : fileArr) {
		if(file.isFile()) {
			file.delete();
		}else {
			doDeleteFiles(file.listFiles());
			file.delete(); // empty dir
		}
	}
  }else {
	  return;
  }
}

}
