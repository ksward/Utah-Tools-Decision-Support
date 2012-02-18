package core.patient.object;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class PatientSelectLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return ((Patient) element).getStudyID();
		}
		if (columnIndex == 1) {
			return ((Patient) element).getName();
		}  
		if (columnIndex == 2) {
			return ((Patient) element).getMedRecNum();
		}  
		if (columnIndex == 3) {
			return ((Patient) element).getBirthdateString();
		}  
		return null;
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

}
