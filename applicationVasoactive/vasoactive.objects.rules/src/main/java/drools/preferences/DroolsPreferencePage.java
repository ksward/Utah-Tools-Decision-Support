package drools.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import activator.DroolsGlucoseActivator;

public class DroolsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public DroolsPreferencePage() {
		super(GRID);
		setPreferenceStore(DroolsGlucoseActivator.getDefault().getPreferenceStore());
		setDescription("Preference settings for Drools inference engine:\n");
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(DroolsPreferenceConstants.BRMS_POLL, "Polling interval in seconds:",
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(DroolsPreferenceConstants.BRMS_CACHE,"Directory for cache:", getFieldEditorParent()));
		addField(new StringFieldEditor(DroolsPreferenceConstants.BRMS_URL, "URL of rules repository:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(DroolsPreferenceConstants.USE_BRMS,"Use BRMS",getFieldEditorParent()));
		addField(new FileFieldEditor(DroolsPreferenceConstants.HARD_DRL_FILE,"Experimental DRL file:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

}
