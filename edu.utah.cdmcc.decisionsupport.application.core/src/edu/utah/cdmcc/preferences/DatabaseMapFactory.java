package edu.utah.cdmcc.preferences;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.preference.IPreferenceStore;
//import edu.utah.cdmcc.decisionsupport.application.DecisionSupportActivator;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

/**
 * 
 * This  little class simply makes the preference map from the database
 * preference dialogs so that it can be passed to HibernateUtil.
 * 
 * Usage:  preferenceMap = new DatabaseMapFactory().getDatabasePreferenceMap();
 * 
 * J. Michael Dean, MD
 * December 2, 2010
 */
  public class DatabaseMapFactory {
	  private Map<String, String> databasePreferenceMap = new HashMap<String, String>();
	  
	  public DatabaseMapFactory(){
		IPreferenceStore databasePrefs = ApplicationControllers.getApplication().getPreferenceStore();
		databasePreferenceMap.put("DATABASE_CHOICE", databasePrefs.getString(DatabasePreferenceConstants.DATABASE_CHOICE));
		
		databasePreferenceMap.put("HSQLDB_DATABASE_DRIVER", databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER));
		databasePreferenceMap.put("HSQLDB_DATABASE_CONNECTION_URL", databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL));
		databasePreferenceMap.put("HSQLDB_DATABASE_DIALECT", databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_DIALECT));
		databasePreferenceMap.put("HSQLDB_DATABASE_USERNAME", databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME));
		databasePreferenceMap.put("HSQLDB_DATABASE_PASSWORD", databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_PASSWORD));
		
		databasePreferenceMap.put("MYSQL_DATABASE_DRIVER", databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_DRIVER));
		databasePreferenceMap.put("MYSQL_DATABASE_CONNECTION_URL", databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL));
		databasePreferenceMap.put("MYSQL_DATABASE_DIALECT", databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_DIALECT));
		databasePreferenceMap.put("MYSQL_DATABASE_USERNAME", databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME));
		databasePreferenceMap.put("MYSQL_DATABASE_PASSWORD", databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD));
		
		databasePreferenceMap.put("TARGET_DATABASE_DRIVER", databasePrefs.getString(DatabasePreferenceConstants.TARGET_DATABASE_DRIVER));
		databasePreferenceMap.put("TARGET_DATABASE_CONNECTION_URL", databasePrefs.getString(DatabasePreferenceConstants.TARGET_DATABASE_CONNECTION_URL));
		databasePreferenceMap.put("TARGET_DATABASE_DIALECT", databasePrefs.getString(DatabasePreferenceConstants.TARGET_DATABASE_DIALECT));
		databasePreferenceMap.put("TARGET_DATABASE_USERNAME", databasePrefs.getString(DatabasePreferenceConstants.TARGET_DATABASE_USERNAME));
		databasePreferenceMap.put("TARGET_DATABASE_PASSWORD", databasePrefs.getString(DatabasePreferenceConstants.TARGET_DATABASE_PASSWORD));
	  }

	public Map<String, String> getDatabasePreferenceMap() {
		return databasePreferenceMap;
	}
	  


}
