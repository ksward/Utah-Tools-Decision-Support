package drools.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import activator.Activator;

public class DroolsPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(DroolsPreferenceConstants.BRMS_CACHE, "");
		store.setDefault(DroolsPreferenceConstants.BRMS_POLL, 30);
		store.setDefault(DroolsPreferenceConstants.BRMS_URL, "Enter URL for Guvnor repository");
		store.setDefault(DroolsPreferenceConstants.USE_BRMS, false);
		store.setDefault(DroolsPreferenceConstants.HARD_DRL_FILE, "");
	}

}
