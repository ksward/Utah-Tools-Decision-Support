package hypertonic.saline.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import activator.Activator;

public class HypertonicSalinePreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(HypertonicSalinePreferenceConstants.SI_UNITS, "usa");
		store.setDefault(HypertonicSalinePreferenceConstants.ADULT_WEIGHT_LIMIT,50);
		//store.setDefault(HypertonicSalinePreferenceConstants.ACTIVE_MODE, false);
	}

}
