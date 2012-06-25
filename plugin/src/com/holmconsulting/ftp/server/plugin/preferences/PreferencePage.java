package com.holmconsulting.ftp.server.plugin.preferences;

import java.util.ResourceBundle;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.holmconsulting.ftp.server.plugin.Activator;



/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 * 
 * @author alan mehio
 */
public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private final ResourceBundle bundle;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		bundle = Activator.getResourceBundle();
		setDescription(bundle.getString("preference.description"));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		// The port number field
		IntegerFieldEditor port = addIntField(PreferenceConstants.PARAM_PORT,
				"preference.port");
		// Sets range of valid port numbers
		port.setValidRange(1, 65535);
		

		StringFieldEditor username = addStringField(PreferenceConstants.PARAM_USERNAME, "preference.username");
		username.setEmptyStringAllowed(false);
		username.setTextLimit(20); // max 20 characters
		
		
		StringFieldEditor password = addStringField(PreferenceConstants.PARAM_PASSWORD, "preference.password");
		password.setEmptyStringAllowed(false);
		password.setTextLimit(20);// max 20 characters
		
		
		
		// The message persistence field
		addBooleanField(PreferenceConstants.PARAM_PERSIST, "preference.persist");

		// The notification popup field
		addBooleanField(PreferenceConstants.PARAM_NOTIFICATION_ENABLED,
				"preference.notification");

		// The debug output field
		addBooleanField(PreferenceConstants.PARAM_DEBUG, "preference.debug");

		// The
		addBooleanField(PreferenceConstants.PARAM_STARTUP, "preference.startup");
	}

	/**
	 * Creates an true/false input field.
	 * 
	 * @param fieldId
	 * @param messageId
	 * @return
	 */
	private BooleanFieldEditor addBooleanField(String fieldId, String messageId) {
		BooleanFieldEditor field = new BooleanFieldEditor(fieldId,
				bundle.getString(messageId), getFieldEditorParent());
		addField(field);
		return field;
	}

	/**
	 * Creates an integer input field
	 * 
	 * @param fieldId
	 * @param messageId
	 * @return
	 */
	private IntegerFieldEditor addIntField(String fieldId, String messageId) {
		IntegerFieldEditor field = new IntegerFieldEditor(fieldId,
				bundle.getString(messageId), getFieldEditorParent());
		addField(field);
		return field;
	}
	

	/**
	 * Creates an string input field
	 * 
	 * @param fieldId
	 * @param messageId
	 * @return
	 */
	private StringFieldEditor addStringField(String fieldId, String messageId) {
		StringFieldEditor field = new StringFieldEditor(fieldId,
				bundle.getString(messageId), getFieldEditorParent());
		addField(field);
		return field;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	
	}
	
	
}