package hypertonic.decision.object;


import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.laboratory.object.BasicLaboratoryObject;


public class HypertonicLaboratoryLabelProvider implements ITableLabelProvider {

	static final DecimalFormat integerFormat = new DecimalFormat("###");
	static final int SAMPLE_DATE = 0;
	static final int SODIUM = 1;
	static final int OSMOLALITY = 2;
	
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == SAMPLE_DATE){
			return ((BasicLaboratoryObject) element).getTimeOfSpecimenCollection().getTime().toString();
		}
		
		if (columnIndex == SODIUM){
			if (testIsSodium(element)){
				return integerFormat.format(((SerumSodiumLaboratoryResult) element).getNumericResult());
			} else {
				return "";
			}
		}
		
		if (columnIndex == OSMOLALITY){
			if (testIsOsmolality(element)){
				return integerFormat.format(((SerumOsmolalityLaboratoryResult) element).getNumericResult());
			} else {
				return "";
			}
		}
		return null;
	}
	
	private boolean testIsOsmolality(Object element) {
		return (element instanceof SerumOsmolalityLaboratoryResult);
	}

	private boolean testIsSodium(Object element) {
		return (element instanceof SerumSodiumLaboratoryResult);
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}



}
