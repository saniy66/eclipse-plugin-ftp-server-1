package com.holmconsulting.ftp.server.plugin.preferences;

/**
 * Constant definitions for plug-in preferences. Each key is used to reference a
 * preference in the preference store.
 * 
 * @author alan mehio
 */
public class PreferenceConstants {

	/**
	 * FTP port server will listen on
	 */
	public static final String PARAM_PORT = "com.holmconsulting.plugin.ftp.server.port";
	
	/**
	 * FTP user name
	 */
	public static final String PARAM_USERNAME = "com.holmconsulting.plugin.ftp.server.username";
	
	/**
	 * FTP password
	 */
	public static final String PARAM_PASSWORD = "com.holmconsulting.plugin.ftp.server.password";

	/**
	 * Will send debug statements to the system console output
	 */
	public static final String PARAM_DEBUG = "com.holmconsulting.plugin.ftp.server.debug";

	/**
	 * Enables/Disables the notification popup upon the arrival of new file from FTP client
	 */
	public static final String PARAM_NOTIFICATION_ENABLED = "com.holmconsulting.plugin.ftp.server.notification.enabled";

	/**
	 * Persists the files between eclipse sessions
	 */
	public static final String PARAM_PERSIST = "com.holmconsulting.plugin.ftp.server.file.persist";

	/**
	 * Will start the FTP Server when eclipse starts up.
	 */
	public static final String PARAM_STARTUP = "com.holmconsulting.plugin.ftp.server.startup";

}

