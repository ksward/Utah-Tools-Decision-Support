package edu.utah.cdmcc.decisionsupport.controller.core;

import java.util.Map;
import org.eclipse.core.runtime.ListenerList;
import edu.utah.cdmcc.listeners.IDatabaseListener;
import edu.utah.cdmcc.preferences.DatabaseMapFactory;

public class DatabaseController extends PropertyChangeController {
	private ListenerList databaseChangeListeners;
	
	public DatabaseController() {
	}

	public ListenerList getDatabaseChangeListeners() {
		return databaseChangeListeners;
	}

	public void setDatabaseChangeListeners(ListenerList databaseChangeListeners) {
		this.databaseChangeListeners = databaseChangeListeners;
	}

	public void addDatabaseChangedListener(final IDatabaseListener listener) {
		if (databaseChangeListeners == null) {
			databaseChangeListeners = new ListenerList();
		}
		databaseChangeListeners.add(listener);
	}

	public void removeDatabaseChangedListener(final IDatabaseListener listener) {

		if (databaseChangeListeners != null) {
			databaseChangeListeners.remove(listener);
			if (databaseChangeListeners.isEmpty())
				databaseChangeListeners = null;
		}
	}

	public void fireDatabaseChanged() {
		if (databaseChangeListeners == null)
			return;
		Object[] rls = databaseChangeListeners.getListeners();
		for (Object o : rls) {
			IDatabaseListener listener = (IDatabaseListener) o;
			listener.databaseChanged();
		}
	}

	public Map<String, String> getDatabasePreferenceMap() {
		return new DatabaseMapFactory().getDatabasePreferenceMap();
	}
}
