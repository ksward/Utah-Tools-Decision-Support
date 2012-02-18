package core.model.object;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ModelObject extends BaseEntity {
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(final PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);
	}
	
	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener){
		pcs.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(final PropertyChangeListener listener){
		pcs.removePropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener){
		pcs.removePropertyChangeListener(propertyName, listener);
	}
	
	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue){
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange(final String propertyName, final int oldValue, final int newValue){
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue){
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected Object[] append(final Object[] array, final Object object){
		List<Object> newList = new ArrayList<Object>(Arrays.asList(array));
		newList.add(object);
		return newList.toArray((Object[]) Array.newInstance(array.getClass().getComponentType(), newList.size()));
	}
	
	protected Object[] remove(final Object[] array, final Object object){
		List<Object> newList = new ArrayList<Object>(Arrays.asList(array));
		newList.remove(object);
		return newList.toArray((Object[]) Array.newInstance(array.getClass().getComponentType(), newList.size()));
	}
}

