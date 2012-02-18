package edu.utah.cdmcc.decisionsupport.application.core;

import java.util.HashMap;
import java.util.Map;

//import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "edu.utah.cdmcc.decisionsupport.application.core";
	private Map<String, Color> colors = new HashMap<String, Color>();
	private static Activator plugin;
	private String activeDecisionEngineModePreference;
	//private IPreferenceStore studyPrefs;

	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public Color getColor(String type) {
		return colors.get(type);
	}

	public void setColor(final String type, final Color color) {
		colors.put(type, color);
	}

	public static String getUniqueIdentifier() {
		if (getDefault() == null) {
			return PLUGIN_ID;
		}
		return getDefault().getBundle().getSymbolicName();
	}
	
	public String getActiveDecisionEngineModePreference() {
		return activeDecisionEngineModePreference;
	}

	public void setActiveDecisionEngineModePreference(
			String activeDecisionEngineModePreference) {
		this.activeDecisionEngineModePreference = activeDecisionEngineModePreference;
	}
	
}
