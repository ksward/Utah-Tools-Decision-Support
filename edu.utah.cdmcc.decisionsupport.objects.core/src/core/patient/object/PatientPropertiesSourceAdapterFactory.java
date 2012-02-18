package core.patient.object;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * This adapter factory is connected to the Patient
 * object by a clause in the plugin.xml file so the
 * Patient object is completely decoupled from this code.
 * <p>
 * The adaptor is then used by the properties.view to display
 * the Patient object selected by the user.
 *
 * @author mdean
 *
 */
public class PatientPropertiesSourceAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IPropertySource.class){
			return new PatientPropertySource((Patient) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class};
	}

}
