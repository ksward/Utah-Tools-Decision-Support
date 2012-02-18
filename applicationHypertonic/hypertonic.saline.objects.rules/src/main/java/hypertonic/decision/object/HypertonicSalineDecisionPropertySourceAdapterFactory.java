package hypertonic.decision.object;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * This adapter factory is connected to the HypertonicSalineDecision
 * object by a clause in the plugin.xml file so the
 * HypertonicSalineDecision object is completely decoupled from this code.
 * <p>
 * The adaptor is then used by the properties.view to display
 * the HypertonicSalineDecision object selected by the user.
 *
 * @author mdean
 *
 */
public class HypertonicSalineDecisionPropertySourceAdapterFactory implements
		IAdapterFactory {

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IPropertySource.class){
			return new HypertonicSalineDecisionPropertySource((HypertonicSalineDecision) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class};
	}

}
