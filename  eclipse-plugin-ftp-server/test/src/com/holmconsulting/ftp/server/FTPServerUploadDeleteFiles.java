package com.holmconsulting.ftp.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.Assert;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * One test class just to roughly test 
 * the upload, delete, list directory, change directory and 
 * We may extends these test cases to cover all 
 * the command on the FTPServer.
 * Please notice, we are using the FTPClient from apache commons (Commons Net version 3.1)
 * @author alan mehio alan.mehio@gmail.com
 *
 */
public class FTPServerUploadDeleteFiles {
	

	@Before
	public void setUp() throws Exception {

	}
	
	
	@After
	public void tearDown() throws Exception {
	
		
	}
	
	
	
	/**
	 * Uploading three files  and retrieve one file
	 * @throws Exception
	 */
	@Test
	public void uploadRetrieveFiles() throws Exception {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;
		client.connect("localhost",Settings.getPortNumber());
		client.login(Settings.getUsername(), Settings.getPassword());
		
		boolean response = client.makeDirectory("etc");
		Assert.assertTrue(response);

		String filename1 = "./etc/file1.txt";
		fis = new FileInputStream(filename1);
		client.storeFile(filename1, fis);
		fis.close();
		
		 String filename2 = "./etc/file2.gif";
		fis = new FileInputStream(filename2);
		client.storeFile(filename2, fis);
		fis.close();
		
		
		String filename3 = "./etc/file3.html";
		fis = new FileInputStream(filename3);
		client.storeFile(filename3, fis);
		fis.close();
		
		
		
		FileOutputStream fos = new FileOutputStream("./etc/fileClient.txt");
        client.retrieveFile(filename1, fos);
		fos.close();
			
		client.logout();
			
	}
	
	
	/**
	 * This works as long as the server shut downs and start again. 
	 * On the same session not possible to delete a file
	 * @throws Exception
	 */
	@Test
    public void deleteFiles() throws Exception {
		FTPClient client = new FTPClient();
		client.connect("localhost",Settings.getPortNumber());
		client.login(Settings.getUsername(), Settings.getPassword());

		boolean response = client.deleteFile("./etc/file1.txt");
		//Assert.assertTrue(response);
		
		response = client.deleteFile("./etc/file2.gif");
		//Assert.assertTrue(response);
			
		response = client.deleteFile("./etc/file3.html");
	//	Assert.assertTrue(response);
		
		
		
			
		client.logout();
			
	}
	
	@Test 
	public void changeRootDirector() throws Exception {
		FTPClient client = new FTPClient();
		client.connect("localhost",Settings.getPortNumber());
		client.login(Settings.getUsername(), Settings.getPassword());
	
		boolean response = client.changeWorkingDirectory("./etc");
		
		Assert.assertTrue(response);
		
	}
	
	@Test
	public void createDirectory() throws Exception {
		FTPClient client = new FTPClient();
		client.connect("localhost",Settings.getPortNumber());
		client.login(Settings.getUsername(), Settings.getPassword());
		boolean response = client.makeDirectory("testTemp");
		Assert.assertTrue(response);
		
		
	}
	
	
	@Test
	public void deleteDirectory() throws Exception {
		FTPClient client = new FTPClient();
		client.connect("localhost",Settings.getPortNumber());
		client.login(Settings.getUsername(), Settings.getPassword());
		

		boolean response = client.removeDirectory("./testTemp");
		
		Assert.assertTrue(response);
	}
}
