package hypertonic.saline.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import activator.Activator;
import core.field.editors.SpacerFieldEditor;

public class HypertonicSalinePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public HypertonicSalinePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preference settings for hypertonic saline decision support tool:");
	}

	/**
	 * This method is created solely so I can call it from JUnit core.tests
	 * to make sure the control is correct.  It should not be called
	 * by production code.
	 * 
	 * @param testingOnly
	 */
	public HypertonicSalinePreferencePage(boolean testingOnly){
		super(GRID);
		setDescription("Preference settings for hypertonic saline decision support tool:");
	}
	
	public void createFieldEditors() {

		addField(new RadioGroupFieldEditor(HypertonicSalinePreferenceConstants.SI_UNITS,
				"\nSelect units of measure:", 1, new String[][] {
						{ "United States (non-S.I.)", "usa" },
						{ "Systeme International (S.I.)", "metric" } },
				getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		IntegerFieldEditor weightLimit = new IntegerFieldEditor(
				HypertonicSalinePreferenceConstants.ADULT_WEIGHT_LIMIT,
				"Adult weight limit:", getFieldEditorParent(),3);
		weightLimit.setValidRange(40, 100);
		addField(weightLimit);
		
//		addField(new SpacerFieldEditor(getFieldEditorParent()));
//		
//		addField(new BooleanFieldEditor(HypertonicSalinePreferenceConstants.ACTIVE_MODE,"Active Mode",getFieldEditorParent()));
	}

	
	public void init(final IWorkbench workbench) {
	}

}