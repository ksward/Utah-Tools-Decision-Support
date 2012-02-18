package ventilator.decision.object;

import java.text.DecimalFormat;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.laboratory.object.BasicLaboratoryObject;

public class VentilatorPediatricLaboratoryLabelProvider implements ITableLabelProvider {

	static final DecimalFormat integerFormat = new DecimalFormat("###");
	static final DecimalFormat floatFormat = new DecimalFormat("#.##");
	static final int SAMPLE_DATE = 0;
	static final int PH = 1;
	static final int PCO2 = 2;
	static final int PO2 = 3;
	
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == SAMPLE_DATE){
			return ((BasicLaboratoryObject) element).getTimeOfSpecimenCollection().getTime().toString();
		}
		
		if (columnIndex == PH){
			if (testIsABG(element)){
				System.out.println(((ArterialBloodGasLaboratoryResult) element).getPhValue());
				return floatFormat.format(((ArterialBloodGasLaboratoryResult) element).getPhValue());
			} else {
				return "";
			}
		}
		
		if (columnIndex == PCO2){
			if (testIsABG(element)){
				return integerFormat.format(((ArterialBloodGasLaboratoryResult) element).getPco2Value());
			} else {
				return "";
			}
		}
		
		if (columnIndex == PO2){
			if (testIsABG(element)){
				return integerFormat.format(((ArterialBloodGasLaboratoryResult) element).getPo2Value());
			} else {
				return "";
			}
		}
		
		return null;
	}
	
	private boolean testIsABG(Object element) {
		return (element instanceof ArterialBloodGasLaboratoryResult);
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
