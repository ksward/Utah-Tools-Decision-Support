package glucose.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import core.field.editors.SpacerFieldEditor;
import activator.DroolsGlucoseActivator;

public class GlucosePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public GlucosePreferencePage() {
		super(GRID);
		setPreferenceStore(DroolsGlucoseActivator.getDefault().getPreferenceStore());
		setDescription("Preference settings for glucose decision support tool:");
	}

	/**
	 * This method is created solely so I can call it from JUnit tests
	 * to make sure the control is correct.  It should not be called
	 * by production code.
	 * 
	 * @param testingOnly
	 */
	public GlucosePreferencePage(boolean testingOnly){
		super(GRID);
		setDescription("Preference settings for glucose decision support tool:");
	}
	
	public void createFieldEditors() {

		addField(new RadioGroupFieldEditor(GlucosePreferenceConstants.SI_UNITS,
				"\nSelect units of measure:", 1, new String[][] {
						{ "United States (non-S.I.)", "usa" },
						{ "Systeme International (S.I.)", "metric" } },
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				GlucosePreferenceConstants.INSULIN_UNITS,
				"\nSelect insulin dosing display format:", 1, new String[][] {
						{ "Units/hour", "adult" },
						{ "Units/kg/hour", "pediatric" } },
				getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		IntegerFieldEditor mild = new IntegerFieldEditor(
				GlucosePreferenceConstants.MILD_HYPOGLYCEMIA_LIMIT,
				"Glucose level for mild hypoglycemia:", getFieldEditorParent(),3);
		mild.setValidRange(60, 90);
		addField(mild);
		
		IntegerFieldEditor moderate = new IntegerFieldEditor(
				GlucosePreferenceConstants.MODERATE_HYPOGLYCEMIA_LIMIT,
				"Glucose level for moderate hypoglycemia:",
				getFieldEditorParent(),3);
		moderate.setValidRange(40, 70);
		addField(moderate);
		
		IntegerFieldEditor severe = new IntegerFieldEditor(
				GlucosePreferenceConstants.SEVERE_HYPOGLYCEMIA_LIMIT,
				"Glucose level for severe hypoglycemia:",
				getFieldEditorParent(),3);
		severe.setValidRange(20, 60);
		addField(severe);
		
		
		IntegerFieldEditor upperLimit = new IntegerFieldEditor(
				GlucosePreferenceConstants.UPPER_GLUCOSE_TARGET,
				"Upper glucose target:", getFieldEditorParent(),3);
		upperLimit.setValidRange(110, 160);
		addField(upperLimit);
		
		IntegerFieldEditor lowerLimit = new IntegerFieldEditor(
				GlucosePreferenceConstants.LOWER_GLUCOSE_TARGET,
				"Lower glucose target:", getFieldEditorParent(),3);
		lowerLimit.setValidRange(80, 140);
		addField(lowerLimit);
	}

	public void init(final IWorkbench workbench) {
	}

}