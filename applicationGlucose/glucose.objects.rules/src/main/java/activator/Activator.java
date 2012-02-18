package activator;

import glucose.preferences.GlucosePreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * There are some domain specific things in this activator, but fairly minimal.
 * @author mdean
 *
 */
public class Activator extends AbstractUIPlugin {
	
	private String metricPreference;
	private String insulinPreference;
	private IPreferenceStore glucosePrefs;
	private static Activator plugin;
	
	public Activator() {
		plugin = this;
	}

	public static Activator getDefault() {
		return plugin;
	}
	
	public void start(final BundleContext context){
		try {
			super.start(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		glucosePrefs = getDefault().getPreferenceStore();
		metricPreference = glucosePrefs.getString(GlucosePreferenceConstants.SI_UNITS);
		insulinPreference = glucosePrefs.getString(GlucosePreferenceConstants.INSULIN_UNITS);
	}
	
	public String getMetricPreference() {
		return metricPreference;
	}

	public void setMetricPreference(final String metricPreference) {
		this.metricPreference = metricPreference;
	}

	public String getInsulinPreference() {
		return insulinPreference;
	}

	public void setInsulinPreference(final String insulinPreference) {
		this.insulinPreference = insulinPreference;
	}
}
