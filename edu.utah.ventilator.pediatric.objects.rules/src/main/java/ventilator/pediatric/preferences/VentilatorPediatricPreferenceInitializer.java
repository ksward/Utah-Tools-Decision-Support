package ventilator.pediatric.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import activator.Activator;

public class VentilatorPediatricPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(VentilatorPediatricPreferenceConstants.SI_UNITS, "usa");
		store.setDefault(VentilatorPediatricPreferenceConstants.ADULT_WEIGHT_LIMIT,50);
		//store.setDefault(VentilatorPediatricPreferenceConstants.ACTIVE_MODE, false);
	}

}
