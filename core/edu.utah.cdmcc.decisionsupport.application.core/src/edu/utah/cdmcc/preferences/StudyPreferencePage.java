package edu.utah.cdmcc.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import core.field.editors.LabelFieldEditor;
import core.field.editors.SpacerFieldEditor;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;

public class StudyPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private StringFieldEditor site;
	private StringFieldEditor site_pre;
	private StringFieldEditor study;
	private StringFieldEditor study_pre;
	private StringFieldEditor studyNumber;

	public StudyPreferencePage(){
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preference settings for study information");
	}
	@Override
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor(StudyPreferenceConstants.STUDY_NUMBERING_MODE,
				"\nSelect method to create study identification number:", 1, new String[][] {
						{ "Create with site and study prefixes", "calculated" },
						{ "Sequential simple numbers", "sequential" },
						{ "Free form - user must enter", "free"}},
				getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Information below is used to construct record numbers. \nThe prefix strings must be four characters in length.", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));		
		
		studyNumber = new StringFieldEditor(StudyPreferenceConstants.STUDY_LAST_NUMBER, "Last number used", getFieldEditorParent());
		studyNumber.setTextLimit(4);
		studyNumber.setEmptyStringAllowed(false);
		addField(studyNumber);

		
		site = new StringFieldEditor(StudyPreferenceConstants.SITE_NAME, "Name of study site", getFieldEditorParent());
		site.setEmptyStringAllowed(false);
		addField(site);
		site_pre = new StringFieldEditor(StudyPreferenceConstants.SITE_PREFIX, "Prefix of study site (4 characters)", getFieldEditorParent());
		site_pre.setEmptyStringAllowed(false);
		site_pre.setTextLimit(4);
		addField(site_pre);
		study = new StringFieldEditor(StudyPreferenceConstants.STUDY_NAME, "Name of study",  getFieldEditorParent());
		study.setEmptyStringAllowed(false);
		addField(study);
		study_pre = new StringFieldEditor(StudyPreferenceConstants.STUDY_PREFIX, "Prefix of study name (4 characters)", getFieldEditorParent());
		study_pre.setEmptyStringAllowed(false);
		study_pre.setTextLimit(4);
		addField(study_pre);
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Status of the decision inference engine.  If active is checked," +
				" \nthen the inference engine will fire.  If not, the clinician \n" +
				"will be asked to enter their actions without receiving advice.", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(StudyPreferenceConstants.ACTIVE_MODE,"Active Mode",getFieldEditorParent()));
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Set encryption of data exports on or off.  Default is off\n" +
				"to facilitate debugging.  When on, names and medical record numbers will be\n" +
				"encrypted prior to transfer to the MySQL target database.", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(StudyPreferenceConstants.ENCRYPTION_ON,"Encryption On",getFieldEditorParent()));
	}

	@Override
	public boolean performOk() {
		study_pre.setStringValue(study_pre.getStringValue().toUpperCase());
		site_pre.setStringValue(site_pre.getStringValue().toUpperCase());
		return super.performOk();
	};
	
	public void init(IWorkbench workbench) {
	}

}
