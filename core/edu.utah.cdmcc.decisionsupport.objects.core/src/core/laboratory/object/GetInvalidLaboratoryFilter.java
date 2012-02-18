package core.laboratory.object;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


public class GetInvalidLaboratoryFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		BasicLaboratoryObject result = (BasicLaboratoryObject) element;
		return result.getValid();
	}

}
