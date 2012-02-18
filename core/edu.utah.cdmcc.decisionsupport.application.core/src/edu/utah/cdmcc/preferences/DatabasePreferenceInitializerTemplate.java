package edu.utah.cdmcc.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.preferences.DatabasePreferenceConstants;

public class DatabasePreferenceInitializerTemplate extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = ApplicationControllers.getApplication().getPreferenceStore();
		store.setDefault(DatabasePreferenceConstants.DATABASE_CHOICE, "hsqldb");
		
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER, "org.hsqldb.jdbcDriver");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL, "jdbc:hsqldb:hsql://localhost:9001");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME, "sa");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_PASSWORD, "");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_DIALECT, "org.hibernate.dialect.HSQLDialect");
		
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_DRIVER, "com.mysql.jdbc.Driver");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME, "root");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD, "root");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_DIALECT, "org.hibernate.dialect.MySQLDialect");

		store.setDefault(DatabasePreferenceConstants.TARGET_DATABASE_DRIVER, "com.mysql.jdbc.Driver");
		store.setDefault(DatabasePreferenceConstants.TARGET_DATABASE_USERNAME, "root");
		store.setDefault(DatabasePreferenceConstants.TARGET_DATABASE_PASSWORD, "root");
		store.setDefault(DatabasePreferenceConstants.TARGET_DATABASE_DIALECT, "org.hibernate.dialect.MySQLDialect");
	}

}
