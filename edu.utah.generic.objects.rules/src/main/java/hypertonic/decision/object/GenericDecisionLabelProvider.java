package hypertonic.decision.object;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class GenericDecisionLabelProvider implements
		ITableLabelProvider {
	static final DecimalFormat integerFormat = new DecimalFormat("####");
	static final DecimalFormat floatFormat = new DecimalFormat("###.#");
	static final int DECISION_DATE = 0;
	static final int SODIUM = 1;
	static final int OSMOLALITY = 2;
	static final int ICP = 3;
	static final int CVP = 4;
	static final int MAP = 5;
	static final int OLD_RATE = 6;
	static final int NEW_RATE = 7;
	static final int SALINE_BOLUS = 8;
	static final int MANNITOL_BOLUS = 9;
	
	public Image getColumnImage(final Object element, final int columnIndex) {
		return null;
	}

	public String getColumnText(final Object element, final int columnIndex) {

		if (columnIndex == DECISION_DATE) {
			return ((GenericDecision) element).getObservationDate()
					.getTime().toString();
		}
		
		if (columnIndex == SODIUM) {
			if (((GenericDecision) element).getCurrentSodiumValue() != -1) {
				return integerFormat
						.format(((GenericDecision) element)
								.getCurrentSodiumValue());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == OSMOLALITY) {
			if (((GenericDecision) element)
					.getCurrentOsmolalityValue() != -1) {
				return floatFormat.format(((GenericDecision) element)
						.getCurrentOsmolalityValue());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == ICP) {
			if (((GenericDecision) element).getIntracranialPressure() != null) {
				return integerFormat
						.format(((GenericDecision) element)
								.getIntracranialPressure());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == CVP) {
			if (((GenericDecision) element).getCentralVenousPressure() != null) {
				return integerFormat
						.format(((GenericDecision) element)
								.getCentralVenousPressure());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == MAP) {
			if (((GenericDecision) element).getMeanArterialPressure() != null) {
				return integerFormat
						.format(((GenericDecision) element)
								.getMeanArterialPressure());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == OLD_RATE) {
			if (((GenericDecision) element)
					.getCurrentHypertonicSalineDripRate() != 0.0) {
				return floatFormat.format(((GenericDecision) element)
						.getCurrentHypertonicSalineDripRate());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == NEW_RATE) {
			if (((GenericDecision) element)
					.getRecommendedHypertonicSalineDripRate() != 0.0) {
				return floatFormat.format(((GenericDecision) element)
						.getRecommendedHypertonicSalineDripRate());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == SALINE_BOLUS) {
			if (((GenericDecision) element)
					.getRecommendedHypertonicSalineBolus() != 0.0) {
				return floatFormat.format(((GenericDecision) element)
						.getRecommendedHypertonicSalineBolus());
			} else {
				return "None";
			}
		}
		
		if (columnIndex == MANNITOL_BOLUS) {
			if (((GenericDecision) element)
					.getRecommendedMannitolBolus() != 0.0) {
				return floatFormat.format(((GenericDecision) element)
						.getRecommendedMannitolBolus());
			} else {
				return "None";
			}
		} else {
			return null;
		}
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