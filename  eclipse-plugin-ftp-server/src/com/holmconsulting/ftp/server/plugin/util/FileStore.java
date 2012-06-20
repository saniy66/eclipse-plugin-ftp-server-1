package com.holmconsulting.ftp.server.plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileStore {
	
	private static final int BUFFER_SIZE = 8192; // chuck of 8 kbytes
	
	/**
	 * This is valid for human readable file i.e text file such as .java, .txt , .rtf ..
	 * @param file
	 * @return a string represntation 
	 */
	public static final String getData(File file) {

		Reader reader = null;

		try {

			StringBuilder builder = new StringBuilder();

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			int bytesRead;
			char[] buffer = new char[BUFFER_SIZE];
			while ( (bytesRead = reader.read(buffer)) != -1 ) {
				builder.append(buffer, 0, bytesRead);
			}

			return builder.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
			}
		}

		return "";
	}

}
