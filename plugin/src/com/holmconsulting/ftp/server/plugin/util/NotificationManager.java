package com.holmconsulting.ftp.server.plugin.util;

import java.io.File;

import org.eclipse.swt.widgets.Display;



/**
 * 
 * @author alan mehio
 *
 */
public final class NotificationManager {

	private NotificationManager() {
	}

	@SuppressWarnings("restriction")
	public static void notify(File file, boolean isNew, Display display) {
		MessageNotification notification = new MessageNotification(file, isNew,
				display);
		notification.create();
		notification.open();
	}

}