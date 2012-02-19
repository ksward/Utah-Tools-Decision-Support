package ventilator.decision.object;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class VentilatorPediatricDecisionLabelProvider implements
		ITableLabelProvider {
	static final DecimalFormat integerFormat = new DecimalFormat("####");
	static final DecimalFormat floatFormat = new DecimalFormat("###.#");
	static final int DECISION_DATE = 0;
	
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	public String getColumnText(final Object element, final int columnIndex) {

		if (columnIndex == DECISION_DATE) {
			return ((VentilatorPediatricDecision) element).getObservationDate()
					.getTime().toString();
		}
		return null;
		
		
	}

	public void addListener(final ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	public void removeListener(final ILabelProviderListener listener) {
	}
}