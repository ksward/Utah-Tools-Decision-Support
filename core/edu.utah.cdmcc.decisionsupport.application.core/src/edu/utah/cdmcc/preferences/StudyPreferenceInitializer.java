package edu.utah.cdmcc.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;

public class StudyPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(StudyPreferenceConstants.SITE_NAME, "University of Utah");
		store.setDefault(StudyPreferenceConstants.STUDY_PREFIX, "DEMO");
		store.setDefault(StudyPreferenceConstants.STUDY_NAME, "Demonstration Study");
		store.setDefault(StudyPreferenceConstants.STUDY_NUMBERING_MODE, "calculated");
		store.setDefault(StudyPreferenceConstants.SITE_PREFIX, "UTAH");
		store.setDefault(StudyPreferenceConstants.STUDY_LAST_NUMBER, "0001");
		store.setDefault(StudyPreferenceConstants.ACTIVE_MODE, false);
		store.setDefault(StudyPreferenceConstants.ENCRYPTION_ON, false);
	}
}
