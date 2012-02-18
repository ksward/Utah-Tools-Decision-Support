package vasoactive.decision.object;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * This adapter factory is connected to the GlucoseDecision
 * object by a clause in the plugin.xml file so the
 * GlucoseDecision object is completely decoupled from this code.
 * <p>
 * The adaptor is then used by the properties.view to display
 * the GlucoseDecision object selected by the user.
 *
 * @author mdean
 *
 */
public class GlucoseDecisionPropertySourceAdapterFactory implements
		IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IPropertySource.class){
			return new GlucoseDecisionPropertySource((VasoactiveDecision) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class};
	}

}
