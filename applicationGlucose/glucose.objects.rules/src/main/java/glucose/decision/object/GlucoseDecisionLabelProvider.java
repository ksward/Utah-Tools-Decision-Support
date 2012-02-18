package glucose.decision.object;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class GlucoseDecisionLabelProvider implements ITableLabelProvider {
	static final DecimalFormat glucoseFormat = new DecimalFormat("####");
	static final DecimalFormat insulinFormat = new DecimalFormat("###.##");
	
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	public String getColumnText(final Object element, final int columnIndex) {
		if (columnIndex == 0) {
			return ((GlucoseDecision) element).getObservationDate().getTime()
					.toString();
		} else if (columnIndex == 1) {
			return glucoseFormat.format(((GlucoseDecision) element).getSerumGlucoseConcentration());
		} else if (columnIndex == 2) {
			return insulinFormat.format(((GlucoseDecision) element).getCurrentInsulinDripRate());
		} else if (columnIndex == 3) {
			return ((GlucoseDecision) element).getAdviceText();
		} else if (columnIndex == 4) {
			return ((GlucoseDecision) element).getUserAction();
		} else if (columnIndex == 5) {
			return ((GlucoseDecision) element).getRationaleText();
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
