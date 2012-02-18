package edu.utah.cdmcc.decisionsupport.database.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.preferences.DatabasePreferenceConstants;
import edu.utah.cdmcc.preferences.DatabasePreferenceInitializerTemplate;

public class DatabasePreferenceInitializer extends DatabasePreferenceInitializerTemplate {
	// For new applications, change the names of the databases in the lines below.
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = ApplicationControllers.getApplication().getPreferenceStore();
		super.initializeDefaultPreferences();
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL, "jdbc:mysql://localhost:3306/genericDatabase");
		store.setDefault(DatabasePreferenceConstants.TARGET_DATABASE_CONNECTION_URL, "jdbc:mysql://localhost:3306/targetSaline");
	}
}
