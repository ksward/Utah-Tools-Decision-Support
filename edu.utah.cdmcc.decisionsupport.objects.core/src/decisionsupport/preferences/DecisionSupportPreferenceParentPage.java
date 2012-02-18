package decisionsupport.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Used as a parent page for all preferences that are later
 * added by different components.
 * 
 * @author J. Michael Dean, MD
 *
 */
public class DecisionSupportPreferenceParentPage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DecisionSupportPreferenceParentPage(){

		super(GRID);
		setDescription("Expand tree to alter decision support preferences");
	}
	public void init(IWorkbench workbench) {
	}
	
	protected void createFieldEditors() {		
	}
}
