package edu.utah.cdmcc.preferences;

import java.sql.SQLException;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import core.field.editors.LabelFieldEditor;
import core.field.editors.PasswordFieldEditor;
import core.field.editors.SpacerFieldEditor;
import core.hibernate.HibernateUtil;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.preferences.DatabasePreferenceConstants;

public class DatabasePreferencePageTemplate extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DatabasePreferencePageTemplate() {
		super(GRID);
	}
	
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor(DatabasePreferenceConstants.DATABASE_CHOICE, "\nSelect database:", 1,
				new String[][] { { "&Local HSQLDB", "hsqldb" }, { "&MySQL Server", "mysql" } }, getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Settings for local HSQLDB database:", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER, "JDBC Driver",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL, "Connection URL",  getFieldEditorParent()));
//		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_DIALECT, "Hibernate Dialect",  getFieldEditorParent()));
//		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME, "Username",  getFieldEditorParent()));
//		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_PASSWORD, "Password",  getFieldEditorParent()));
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Settings for mySQL database:", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_DRIVER, "JDBC Driver",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL, "Connection URL",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_DIALECT, "Hibernate Dialect",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME, "Username",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD, "Password",  getFieldEditorParent()));
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("--------------------------------------------------", getFieldEditorParent()));
		addField(new LabelFieldEditor("Settings for TARGET database for transferring data:", getFieldEditorParent()));
		addField(new LabelFieldEditor("--------------------------------------------------", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new StringFieldEditor(DatabasePreferenceConstants.TARGET_DATABASE_DRIVER, "JDBC Driver",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.TARGET_DATABASE_CONNECTION_URL, "Connection URL",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.TARGET_DATABASE_DIALECT, "Hibernate Dialect",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.TARGET_DATABASE_USERNAME, "Username",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.TARGET_DATABASE_PASSWORD, "Password",  getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(ApplicationControllers.getApplication().getPreferenceStore());
		setDescription("Preference settings for decision support database:");
	}

	public boolean performOk(){
		String oldDatabasePreference = ApplicationControllers.getApplication().getPreferenceStore().getString(DatabasePreferenceConstants.DATABASE_CHOICE);
		if (super.performOk()){ // super.performOk stores all the fields into the preference store
			String databasePreference = ApplicationControllers.getApplication().getPreferenceStore().getString(DatabasePreferenceConstants.DATABASE_CHOICE);
			if (oldDatabasePreference.equals(databasePreference)){
				return true;
			}
			
			// Database preference has changed:
			HibernateUtil.injectDatabasePreference(ApplicationControllers.getDatabaseController()
					.getDatabasePreferenceMap());
			if (databasePreference.equals("hsqldb")
					&& ApplicationControllers.getHsqldbController().getHsqldbIsRunning() == false) {
				ApplicationControllers.getApplication().startServer();
				Session session = HibernateUtil.getSessionFactory().getCurrentSession();
				try {
					session.beginTransaction();
					SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
					su.execute(false, true);
					session.getTransaction().commit();
				} catch (RuntimeException e) {
					throw new UtahToolboxException(ErrorCode.SCHEMA_UPDATE_ERROR, e);
				}
			}
	
			if(databasePreference.equals("mysql")){
				if (ApplicationControllers.getApplication().testMySQLConnection(this)){
					// mySQL is running
					Session	session = HibernateUtil.getSessionFactory().getCurrentSession();
					try {
						session.beginTransaction();
						SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
						su.execute(false, true);
						session.getTransaction().commit();
					} catch (RuntimeException e) {
						throw new UtahToolboxException(ErrorCode.SCHEMA_UPDATE_ERROR, e);
					} 

					if(ApplicationControllers.getHsqldbController().getHsqldbIsRunning()){
						//turn off HSQLDB if running
						try {
							ApplicationControllers.getApplication().stopHsqldb();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else {
					// mySQL is not running so revert to HSQLDB
					ApplicationControllers.getApplication().getPreferenceStore().setValue(DatabasePreferenceConstants.DATABASE_CHOICE, "hsqldb");
					HibernateUtil.injectDatabasePreference(ApplicationControllers.getDatabaseController()
							.getDatabasePreferenceMap());
					if(ApplicationControllers.getHsqldbController().getHsqldbIsRunning()==false){
						ApplicationControllers.getApplication().startServer();
						Session session = HibernateUtil.getSessionFactory().getCurrentSession();
						try {
							session.beginTransaction();
							SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
							su.execute(false, true);
							session.getTransaction().commit();
						} catch (RuntimeException e) {
							throw new UtahToolboxException(ErrorCode.SCHEMA_UPDATE_ERROR, e);
						}
					}
				}
			}

			ApplicationControllers.getDatabaseController().fireDatabaseChanged();
			ApplicationControllers.getDecisionController().addDecisionFiredListener(ApplicationControllers.getClockJob());
			ApplicationControllers.getPatientController().updatePatientList();
			ApplicationControllers.getPatientController().setActivePatient(null);
			ApplicationControllers.getPatientController().firePatientsChanged(null);
			return true;
		}
		return false;
		
	}
	
}
