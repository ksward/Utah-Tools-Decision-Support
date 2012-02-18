package hypertonic.saline.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import activator.Activator;

public class GenericPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(GenericPreferenceConstants.SI_UNITS, "usa");
		store.setDefault(GenericPreferenceConstants.ADULT_WEIGHT_LIMIT,50);
		//store.setDefault(GenericPreferenceConstants.ACTIVE_MODE, false);
	}

}
