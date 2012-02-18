package edu.utah.cdmcc.decisionsupport.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class LaboratoryContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
		return ApplicationControllers.getLaboratoryController().getLabsForPatient().toArray();
	}
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
