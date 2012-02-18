package glucose.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import activator.Activator;

public class GlucosePreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(GlucosePreferenceConstants.SI_UNITS, "usa");
		store.setDefault(GlucosePreferenceConstants.INSULIN_UNITS, "adult");
		store.setDefault(GlucosePreferenceConstants.MILD_HYPOGLYCEMIA_LIMIT, 80);
		store.setDefault(GlucosePreferenceConstants.MODERATE_HYPOGLYCEMIA_LIMIT, 60);
		store.setDefault(GlucosePreferenceConstants.SEVERE_HYPOGLYCEMIA_LIMIT, 40);
		store.setDefault(GlucosePreferenceConstants.UPPER_GLUCOSE_TARGET, 110);
		store.setDefault(GlucosePreferenceConstants.LOWER_GLUCOSE_TARGET, 80);
		store.setDefault(GlucosePreferenceConstants.ADULT_WEIGHT_LIMIT,50);
	}

}
