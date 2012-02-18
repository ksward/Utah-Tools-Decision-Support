package edu.utah.cdmcc.decisionsupport.database.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import core.field.editors.LabelFieldEditor;
import core.field.editors.SpacerFieldEditor;
import edu.utah.cdmcc.decisionsupport.application.DecisionSupportActivator;

public class DatabasePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DatabasePreferencePage() {
		super(GRID);
		setPreferenceStore(DecisionSupportActivator.getDefault().getPreferenceStore());
		setDescription("Preference settings for decision support database:");
	}
	
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor(DatabasePreferenceConstants.DATABASE_CHOICE, "\nSelect database:", 1,
				new String[][] { { "&Local HSQLDB", "hsqldb" }, { "&MySQL Server", "mysql" },
						{ "&Other External Server", "external" } }, getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Settings for local HSQLDB database", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER, "JDBC Driver",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL, "Connection URL",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_DIALECT, "Hibernate Dialect",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME, "Username",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.HSQLDB_DATABASE_PASSWORD, "Password",  getFieldEditorParent()));
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Settings for mySQL database", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_DRIVER, "JDBC Driver",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL, "Connection URL",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_DIALECT, "Hibernate Dialect",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME, "Username",  getFieldEditorParent()));
		addField(new StringFieldEditor(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD, "Password",  getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
}
