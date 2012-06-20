package com.holmconsulting.ftp.server.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.holmconsulting.ftp.server.plugin.Activator;

/**
 * This will be called by the eclipse runtime the first time 
 * it needs to populate the default Preference values
 * 
 * @author alan mehio
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.PARAM_PORT, 2121);
		store.setDefault(PreferenceConstants.PARAM_USERNAME, "user");
		store.setDefault(PreferenceConstants.PARAM_PASSWORD, "password");
		store.setDefault(PreferenceConstants.PARAM_NOTIFICATION_ENABLED, true);
		store.setDefault(PreferenceConstants.PARAM_DEBUG, true);
		store.setDefault(PreferenceConstants.PARAM_PERSIST, true);
		store.setDefault(PreferenceConstants.PARAM_STARTUP, false);
	}

}
