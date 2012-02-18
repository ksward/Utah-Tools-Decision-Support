package edu.utah.cdmcc.decisionsupport.database.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import edu.utah.cdmcc.decisionsupport.application.DecisionSupportActivator;

public class DatabasePreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = DecisionSupportActivator.getDefault().getPreferenceStore();
		store.setDefault(DatabasePreferenceConstants.DATABASE_CHOICE, "hsqldb");
		
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER, "org.hsqldb.jdbcDriver");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL, "jdbc:hsqldb:hsql://localhost:9001");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME, "sa");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_PASSWORD, "");
		store.setDefault(DatabasePreferenceConstants.HSQLDB_DATABASE_DIALECT, "org.hibernate.dialect.HSQLDialect");
		
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_DRIVER, "com.mysql.jdbc.Driver");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL, "jdbc:mysql://localhost:3306/glucose");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME, "mdean");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD, "");
		store.setDefault(DatabasePreferenceConstants.MYSQL_DATABASE_DIALECT, "org.hibernate.dialect.MySQLDialect");

	}

}
