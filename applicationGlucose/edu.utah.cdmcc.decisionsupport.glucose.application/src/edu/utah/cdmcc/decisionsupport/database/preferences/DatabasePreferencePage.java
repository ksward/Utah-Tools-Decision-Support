package edu.utah.cdmcc.decisionsupport.database.preferences;

import org.eclipse.ui.IWorkbench;
import edu.utah.cdmcc.preferences.DatabasePreferencePageTemplate;
/**
 * This class is in the application but does not need any changes.  It
 * has to be in the application in order that the preference initialization
 * will work properly, but inherits all of its function from a template
 * that is in the core plugins.  This class should not be changed for
 * new applications.
 * 
 * @author mdean
 *
 */
public class DatabasePreferencePage extends DatabasePreferencePageTemplate{
	
	public DatabasePreferencePage() {
		super();
	}
	
	@Override
	protected void createFieldEditors() {
		super.createFieldEditors();
	}

	@Override
	public void init(IWorkbench workbench) {
		super.init(workbench);
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
}
