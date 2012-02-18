package vasoactive.decision.object;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class GlucoseDecisionLabelProvider implements ITableLabelProvider {
	static final DecimalFormat glucoseFormat = new DecimalFormat("####");
	static final DecimalFormat insulinFormat = new DecimalFormat("###.#");
	
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	public String getColumnText(final Object element, final int columnIndex) {
		if (columnIndex == 0) {
			return ((VasoactiveDecision) element).getObservationDate().getTime()
					.toString();
		} else if (columnIndex == 1) {
			return null;//  glucoseFormat.format(((VasoactiveDecision) element).getSerumGlucoseConcentration());
		} else if (columnIndex == 2) {
			return 	null;//insulinFormat.format(((VasoactiveDecision) element).getCurrentInsulinDripRate());
		} else if (columnIndex == 3) {
			return ((VasoactiveDecision) element).getAdviceText();
		} else if (columnIndex == 4) {
			return ((VasoactiveDecision) element).getUserAction();
		} else if (columnIndex == 5) {
			return ((VasoactiveDecision) element).getRationaleText();
		} else
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
