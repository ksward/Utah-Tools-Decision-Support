package activator;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ventilator.pediatric.preferences.VentilatorPediatricPreferenceConstants;

public class Activator extends AbstractUIPlugin {
	
	private String metricPreference;
	private IPreferenceStore ventilatorPediatricPrefs;
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
		ventilatorPediatricPrefs = getDefault().getPreferenceStore();
		metricPreference = ventilatorPediatricPrefs.getString(VentilatorPediatricPreferenceConstants.SI_UNITS);	
	}
	
	public String getMetricPreference() {
		return metricPreference;
	}

	public void setMetricPreference(final String metricPreference) {
		this.metricPreference = metricPreference;
	}
}
