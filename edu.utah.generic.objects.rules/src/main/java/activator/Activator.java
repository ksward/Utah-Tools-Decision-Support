package activator;

import hypertonic.saline.preferences.GenericPreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	
	private String metricPreference;
	private IPreferenceStore hypertonicSalinePrefs;
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
			System.out.println("Failed in start of super of droolsglucoseactivator");
			e.printStackTrace();
		}
		hypertonicSalinePrefs = getDefault().getPreferenceStore();
		metricPreference = hypertonicSalinePrefs.getString(GenericPreferenceConstants.SI_UNITS);	
	}
	
	public String getMetricPreference() {
		return metricPreference;
	}

	public void setMetricPreference(final String metricPreference) {
		this.metricPreference = metricPreference;
	}
}
