package drools.properties;

import java.util.Properties;
import org.eclipse.jface.preference.IPreferenceStore;

import activator.Activator;

import drools.preferences.DroolsPreferenceConstants;


@SuppressWarnings("serial")
public class DroolsProperties extends Properties {
	private String poll;
	private String url;
	private String cache;
	
	public DroolsProperties() {
		super();
		getPreferences();
		setProperty("poll", poll);
		setProperty("localCacheDir",cache);
		setProperty("url",url);
	}

	private void getPreferences() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		poll = preferences.getString(DroolsPreferenceConstants.BRMS_POLL);
		url = preferences.getString(DroolsPreferenceConstants.BRMS_URL);
		cache = preferences.getString(DroolsPreferenceConstants.BRMS_CACHE);	
	}



}

