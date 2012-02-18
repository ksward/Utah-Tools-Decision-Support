package ventilator.decision.object;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * This adapter factory is connected to the VentilatorPediatricDecision
 * object by a clause in the plugin.xml file so the
 * VentilatorPediatricDecision object is completely decoupled from this code.
 * <p>
 * The adaptor is then used by the properties.view to display
 * the VentilatorPediatricDecision object selected by the user.
 *
 * @author mdean
 *
 */
public class VentilatorPediatricDecisionPropertySourceAdapterFactory implements
		IAdapterFactory {

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IPropertySource.class){
			return new VentilatorPediatricDecisionPropertySource((VentilatorPediatricDecision) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class};
	}

}
