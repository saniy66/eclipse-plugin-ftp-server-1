package com.holmconsulting.ftp.server.plugin.util;

import java.io.File;

import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.mylyn.internal.provisional.commons.ui.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.holmconsulting.ftp.server.plugin.Activator;



@SuppressWarnings("restriction")
public class MessageNotification extends AbstractNotificationPopup implements
		INotification {

	private final File file;
	private boolean isNew;

	private static final ResourceBundle I18N = Activator.getResourceBundle();

	private static final Image IMG_LOGO_NEW = ImageDescriptor.createFromFile(
			Activator.class, "/icons/file_new.png").createImage();

	private static final Image IMG_LOGO_REMOVE = ImageDescriptor.createFromFile(
			Activator.class, "/icons/delete.gif").createImage();
	
	public MessageNotification(File file,boolean isNew, Display display) {
		// super(display, SWT.NO_TRIM | SWT.NO_FOCUS | SWT.TOOL);
		super(display);
		setFadingEnabled(true);
		this.file = file;
		this.isNew = isNew;
	}

	@Override
	protected void createContentArea(Composite parent) {

		parent.setLayout(new GridLayout(1, true));
		Label testLabel = new Label(parent, SWT.WRAP);
		testLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		StringBuilder builder = new StringBuilder();

		builder.append(getValue(file.getName(),
				I18N.getString("notify.unknown.name"), 75));
		
		builder.append("\n")
		.append(I18N.getString("header.size"))
		.append(": ")
		.append(getValue(file.length() + "",
				I18N.getString("notify.unknown.size"), 75));
		
		builder.append("\n")
		.append(I18N.getString("header.path"))
		.append(": ")
		.append(getValue(file.getPath(),
				I18N.getString("notify.unknown.path"), 75));
		
		builder.append("\n\n");

		testLabel.setText(builder.toString());
		testLabel.setBackground(parent.getBackground());
	}

	private String getValue(String value, String defaultValue, int maxLength) {
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		int end = value.length() > maxLength ? maxLength : value.length();
		return value.substring(0, end);
	}

	@Override
	protected Image getPopupShellImage(int maximumHeight) {
		if(isNew) {
			return IMG_LOGO_NEW;
		}else {
			return IMG_LOGO_REMOVE;
		}
	}

	@Override
	protected String getPopupShellTitle() {
		if(isNew) {
		  return I18N.getString("notify.title.new");
		}else {
		  return I18N.getString("notify.title.remove");
		}
		
	}

	@Override
	public boolean isFadingEnabled() {
		return true;
	}
}
